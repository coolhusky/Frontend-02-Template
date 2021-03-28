package socket.server;

public class NIOServerTest {
    public static void main(String[] args) {
        int port = 8081;
        new Thread(new NIOServer(port), "NIOServer-001").start();
    }
}
