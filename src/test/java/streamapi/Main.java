package streamapi;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Stream.of(2, 3, 0, 1, 2).flatMapToInt(x -> IntStream.range(0, x)).forEach(System.out::println);
    }
}
