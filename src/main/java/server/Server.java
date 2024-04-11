package server;

import common.routeClasses.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import server.serverModules.CommandExecutionModule;
import server.serverModules.RequestResponseModule;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

        this.manager = new CollectionManager(new Stack<Route>(), logger);
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

    public void run() throws IOException, ClassNotFoundException {
        this.manager.loadInitialCollection();
        InetSocketAddress address = new InetSocketAddress(port); // создаем адрес сокета (IP-адрес и порт)

        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    try {
                        closeSocket();
                        logger.info("Server finishes working");
                        saveCollection();
                    } catch (IOException e) {
                        //do nothing;
                    }
                }
        ));
        openSocket();
        logger.info("Server is listening on port %s. Print \"save\" to save current collection".formatted(port));
        startSavingTask(saveInterval);
        handleSave();

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

    private void saveCollection() {
        try {
            if (manager.getIsEmpty()) {
                logger.info("Коллекция пуста, нечего сохранять");
                return;
            }
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = builder.newDocument();
            Element root = document.createElement("Routes");
            document.appendChild(root);
            manager.addCollectionToRoot(document, root);
            logger.info("Коллекция успешно сохранена в файл");

        } catch (ParserConfigurationException e) {
            logger.error("Ошибка при сохранение");
        }

    }

    public void startSavingTask(int intervalInSeconds) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable saveTask = this::saveCollection;

        // Schedule the save task to run every n seconds
        scheduler.scheduleAtFixedRate(saveTask, 0, intervalInSeconds, TimeUnit.SECONDS);
    }

    public void handleSave() {
        Runnable task = () -> {
            Scanner scanner = new Scanner(System.in);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String line = scanner.nextLine();
                    if (line.equalsIgnoreCase("save")) {
                        saveCollection();
                    }
                } catch (NoSuchElementException ignored) {

                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}

