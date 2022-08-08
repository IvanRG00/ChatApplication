import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket socket;

    private String Server = "Server: ";

    public Server(ServerSocket socket) {
        this.socket = socket;
    }

    public void Serverstart() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (!socket.isClosed()) {
                Socket socket1 = socket.accept();
                System.out.println(Server + "User has connected");
                ClientHandler clientHandler = new ClientHandler(socket1);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (IOException e) {
            CloseServer();
        }
    }

    public void CloseServer() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4242);
        Server server = new Server(serverSocket);
        server.Serverstart();

    }
}
