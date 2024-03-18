package client.consoles;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Базовый класс для консолей, отвечающий за построчное чтение из переданного потока или источника ввода
 */

public abstract class BaseConsole {

    protected final Scanner scanner;

    public BaseConsole(Readable readable) {
        scanner = new Scanner(readable);
    }

    public BaseConsole(InputStream stream) {
        scanner = new Scanner(stream);
    }

    /**
     * Метод, возвращающий следующую строку из потока ввода
     * @return строка из потока ввода
     * */
    public String getLine() {
        return scanner.nextLine();
    }

    /**
     * Метод, проверяющий наличие следующей строки в потоке ввода
     * @return true, если следующая строка есть, иначе - false
     * */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

}
