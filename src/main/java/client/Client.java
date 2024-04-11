package client;

import client.consoles.FileConsole;
import client.consoles.SystemInConsole;
import client.validators.*;
import common.Request;
import common.Response;
import common.exceptions.ExitException;
import common.exceptions.LostConnectionException;
import common.exceptions.UnknownCommandException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Client {

    private final Map<String, BaseValidator> validators;

    private final static int RECONNECTION_TIMEOUT_IN_SECONDS = 5;
    private final static int MAX_RECONNECTION_ATTEMPTS = 5;

    private Socket socket;
    private ObjectInputStream fromServer;
    private ObjectOutputStream toServer;
    private int depth = 0;

    private String username;

    private boolean isLoggedIn = false;

    private final int port = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 1234;

    public Client() {
        validators = new HashMap<>() {
            {
                put("help", new NoArgumentsValidator());
                put("info", new NoArgumentsValidator());
                put("show", new NoArgumentsValidator());
                put("add", new AddValidator());
                put("update", new UpdateByIdValidator());
                put("remove_by_id", new OneIntArgValidator());
                put("clear", new NoArgumentsValidator());
                put("remove_at", new OneIntArgValidator());
                put("reorder", new NoArgumentsValidator());
                put("sort", new NoArgumentsValidator());
                put("count_greater_than_distance", new OneIntArgValidator());
                put("print_ascending", new NoArgumentsValidator());
                put("print_field_descending_distance", new NoArgumentsValidator());
                put("exit", new ExitValidator());
                put("execute_script", new ExecuteScriptValidator());
                put("register", new AuthValidator());
                put("login", new AuthValidator());
            }
        };
    }


    private boolean connectToServer() throws IOException {
        int reconnectionAttempt = 0;
        do {
            try {
                if (reconnectionAttempt > 0) {
                    Thread.sleep(RECONNECTION_TIMEOUT_IN_SECONDS * 1000);
                    System.out.printf("Пытаюсь переподключиться (попытка %d)\n", reconnectionAttempt);
                }
                socket = new Socket("localhost", port);
                toServer = new ObjectOutputStream(socket.getOutputStream());
                fromServer = new ObjectInputStream(socket.getInputStream());
                System.out.println("Соединение установлено.");
                reconnectionAttempt = 0;
                return true;
            } catch (IOException e) {
                System.out.println("Ошибка подключения");
                reconnectionAttempt++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (reconnectionAttempt <= MAX_RECONNECTION_ATTEMPTS);
        return false;
    }

    public void run() {

        try {
            boolean connected = connectToServer();
            if (!connected) {
                System.err.println("Не удалось подключиться к серверу, завершение работы клиента...");
                System.exit(1);
            }
            SystemInConsole sc = new SystemInConsole();

            System.out.println("Приветствую вас в программе для работы с коллекцией Route! Для дальнейшей работы введите логин и пароль");
            while (true) {
                try {
                    Request request = lineToRequest(sc.getLine());
                    handleRequest(request);
                } catch (ExitException e) {
                    System.out.println("Выход из программы...");
                    break;
                } catch (NoSuchElementException e) {
                    System.err.println("Достигнут конец ввода, завершение работы программы...");
                    System.exit(130);
                } catch (LostConnectionException e) {
                    System.err.println("Потеряно соединение с сервером, завершаем работу клиента...");
                    System.exit(1);
                } catch (ClassNotFoundException e) {
                    System.err.println("Ошибка при чтении ответа от сервера: " + e.getMessage());
                }
            }
            System.out.println("Socket channel closed");
        } catch (IOException e) {
            System.out.println("Проблема с подключением к серверу: " + e.getMessage());
        }

    }

    private Request lineToRequest(String line) throws ExitException, NoSuchElementException {
        if (line.isEmpty()) {
            return null;
        }

        ArrayList<String> commandParts = new ArrayList<>(Arrays.asList(line.trim().split(" ")));

        String commandName = commandParts.get(0).toLowerCase();
        commandParts.remove(0);

        try {
            BaseValidator.checkIsValidCommand(commandName, validators.keySet());
            BaseValidator validator = validators.get(commandName);
            if (validator.getNeedParse()) {
                return validator.validate(commandName, commandParts.toArray(new String[0]), depth > 0, username);
            }
            return validator.validate(commandName, commandParts.toArray(new String[0]), username);
        } catch (UnknownCommandException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void handleRequest(Request request) throws LostConnectionException, IOException, ClassNotFoundException {
        if (request == null) return;
        if (request.getCommand().equals("execute_script")) {
            handleExecuteScript(request);
        } else {
            Response response = makeRequest(request);
            if (response.isSuccess() && (request.getCommand().equals("login")
                    || request.getCommand().equals("register"))) {
                isLoggedIn = true;
                username = request.getArgs()[0];
            }
        }
    }

    public void handleExecuteScript(Request request) throws LostConnectionException, ClassNotFoundException {
        String filename = request.getArgs()[0];
        try (FileReader reader = new FileReader(filename)) {
            FileConsole console = new FileConsole(reader);
            ++depth;
            while (console.hasNextLine()) {
                Request newRequest = lineToRequest(console.getLine());
                handleRequest(newRequest);
            }
            --depth;
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }


    public Response makeRequest(Request request) throws IOException, LostConnectionException, ClassNotFoundException {

        toServer.writeObject(request);
        toServer.flush();

        Response response = (Response) fromServer.readObject();

        System.out.println(response.getMessage());
        System.out.println("-----------------------------------\n");
        return response;
    }
}
