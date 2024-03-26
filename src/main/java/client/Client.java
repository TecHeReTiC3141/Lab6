package client;

import client.consoles.FileConsole;
import client.consoles.SystemInConsole;
import client.validators.*;
import common.exceptions.ExitException;
import common.exceptions.LostConnectionException;
import common.exceptions.UnknownCommandException;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Client {

    private final Map<String, BaseValidator> validators;

    private SocketChannel socketChannel;

    private int depth = 0;

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
            }
        };
    }

    public void openSocket() throws IOException {
        socketChannel = SocketChannel.open();
    }

    public void closeSocket() throws IOException {
        if (socketChannel != null && socketChannel.isOpen()) {
            socketChannel.close();
        }
    }

    private final int port = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 1234;

    private void connectToServer() throws IOException {
        while (!socketChannel.isConnected()) {
            try {
                System.out.println("Trying to connect to the server...");
                socketChannel.connect(new InetSocketAddress("localhost", port));
                System.out.println("Connected to the server.");
            } catch (IOException e) {
                System.err.println("Failed to connect to the server. Retrying...");
                try {
                    Thread.sleep(Math.round(5000 + Math.random() * 3000)); // Wait for 5 seconds before retrying
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void run() {

        try {
            openSocket();
            connectToServer();
            SystemInConsole sc = new SystemInConsole();

            System.out.println("Приветствую вас в программе для работы с коллекцией Route! Введите help для получения списка команд");
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
                    System.err.println("Потеряно соединение с сервером, завершение работы программы...");
                    System.exit(0);
                }
            }
            closeSocket();
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
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        ObjectOutputStream toServer = new ObjectOutputStream(bais);
        toServer.writeObject(request);

        socketChannel.write(ByteBuffer.wrap(bais.toByteArray()));


        // Receive response from the server
        ByteBuffer fromServer = ByteBuffer.allocate(4096);
        int bytesRead = socketChannel.read(fromServer);
        if (bytesRead == -1) {
            throw new LostConnectionException();
        }

        String response = new String(fromServer.array()).trim();

        // Display the response received from the server
        System.out.println(response);
        System.out.println("-----------------------------------\n");
    }

    private void reconnectToServer() {
        try {
            closeSocket();
            openSocket();
            connectToServer();
        } catch (IOException e) {
            System.err.println("Failed to reconnect to the server: " + e.getMessage());
        }
    }
}
