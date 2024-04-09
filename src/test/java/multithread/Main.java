package multithread;

public class Main {

    public static void main(String[] args) {
        Runnable r = () -> {
            String name = Thread.currentThread().getName();
            System.out.println("Hello, " + name);
            try {
                Thread.sleep(500 + (int) (Math.random() * 500));
            } catch (InterruptedException e) {
                return;
            }
            System.out.println(name + " finished");
        };
        for (int i = 0; i < 10; ++i) {
            (new Thread(r)).start();
        }
    }
}
