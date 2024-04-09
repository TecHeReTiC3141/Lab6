package client;

import client.consoles.FileConsole;
import client.consoles.SystemInConsole;
import client.validators.*;
import common.Request;
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

    private int username;

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
                System.out.println("Ошибка подключения.");
                reconnectionAttempt++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (reconnectionAttempt <= MAX_RECONNECTION_ATTEMPTS);
        return false;
    }

    public void authorizeUser() {
        while (true) {
            System.out.println("To register a new user, type 'register'. To authorize, type 'authorize'. To exit, type 'exit'.");
            String input = new SystemInConsole().getLine().trim().toLowerCase();
            if (input.equals("exit")) {
                System.out.println("Exiting...");
                System.exit(0);
            }
            if (input.equals("register")) {
                System.out.println("Enter your username:");
                String username = new SystemInConsole().getLine();
                System.out.println("Enter your password:");
                String password = new SystemInConsole().getLine();
                Request request = new Request("register", new String[]{username, password}, null);
                try {
                    makeRequest(request); // Make separate method for authorization
                } catch (IOException | LostConnectionException e) {
                    System.err.println("Failed to register user: " + e.getMessage());
                }
            } else if (input.equals("authorize")) {
                System.out.println("Enter your username:");
                String username = new SystemInConsole().getLine();
                System.out.println("Enter your password:");
                String password = new SystemInConsole().getLine();
                Request request = new Request("authorize", new String[]{username, password}, null);
                try {
                    makeRequest(request); // Make separate method for authorization
                } catch (IOException | LostConnectionException e) {
                    System.err.println("Failed to authorize user: " + e.getMessage() + ". Check your credentials");
                }
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public void run() {

        try {
            connectToServer();
            SystemInConsole sc = new SystemInConsole();

            System.out.println("Приветствую вас в программе для работы с коллекцией Route! Для дальнейшей работы введите логин и пароль");
            authorizeUser();
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
                return validator.validate(commandName, commandParts.toArray(new String[0]), depth > 0);
            }
            return validator.validate(commandName, commandParts.toArray(new String[0]));
        } catch (UnknownCommandException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void handleRequest(Request request) throws LostConnectionException, IOException {
        if (request == null) return;
        if (request.getCommand().equals("execute_script")) {
            handleExecuteScript(request);
        } else {
            makeRequest(request);
        }

    }

    public void handleExecuteScript(Request request) throws LostConnectionException {
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



    public void makeRequest(Request request) throws IOException, LostConnectionException {


        // Display the response received from the server
        System.out.println(response);
        System.out.println("-----------------------------------\n");
    }
}
