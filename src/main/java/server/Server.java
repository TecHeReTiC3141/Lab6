package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.serverModules.CommandExecutionModule;
import server.serverModules.RequestResponseModule;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int saveInterval = 300;

    /**
     * Обработчик команд
     */
    private final CommandExecutionModule executor;

    /**
     * Менеджер коллекции
     */
    private final CollectionManager manager;

    private final DatabaseManager databaseManager;

    private final int port = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 1234;

    private static final Logger logger = LogManager.getLogger("server");

    private ServerSocket socket;

    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(16);

    String jdbcURL = "jdbc:postgresql://localhost:5488/studs";


    /**
     * Конструктор приложения
     */
    public Server() {
        String credentials = getUserCredentials();
        String[] credentialsParts = credentials.split(" ");
        this.databaseManager = new DatabaseManager(jdbcURL, credentialsParts[0], credentialsParts[1], logger);

        this.manager = new CollectionManager(new CopyOnWriteArrayList<>(), logger, databaseManager);
        this.executor = new CommandExecutionModule(manager, databaseManager);

    }

    public String getUserCredentials() {
        try {
            Scanner credentialsScanner = new Scanner(new FileReader("credentials.txt"));
            String username = credentialsScanner.nextLine().trim();
            String password = credentialsScanner.nextLine().trim();
            return username + " " + password;
        } catch (FileNotFoundException e) {
            logger.error("File with credentials (credentials.txt) not found");
        } catch (NoSuchElementException e) {
            logger.error("Username or password not found in credentials.txt");
        }
        System.exit(-1);
        return null;
    }

    public void openSocket() throws IOException {
        socket = new ServerSocket(port);
    }

    public void closeSocket() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public void run() throws IOException {
        this.manager.loadInitialCollection();
        InetSocketAddress address = new InetSocketAddress(port); // создаем адрес сокета (IP-адрес и порт)

        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    try {
                        closeSocket();
                        logger.info("Server finishes working");
                    } catch (IOException e) {
                        //do nothing;
                    }
                }
        ));
        openSocket();
        logger.info("Server is listening on port %s. Print \"save\" to save current collection".formatted(port));

        while (true) {
            Socket client = handleAccept();
            fixedThreadPool.submit(new RequestResponseModule(client, executor, logger));
        }
    }

    public Socket handleAccept() throws IOException {
        try {
            return socket.accept();
        } catch (IOException e) {
            throw new IOException("Error accepting connection", e);
        }
    }

}

