import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String Username) {
        try {
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = Username;

        } catch (IOException e) {
            CloseClient(socket, bufferedWriter, bufferedReader);
        }
    }

    public void SendingMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                bufferedWriter.write(username + ":" + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            CloseClient(socket, bufferedWriter, bufferedReader);
        }
    }

    public void ReceivingMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                try {
                    while (socket.isConnected()) {
                        message = bufferedReader.readLine();
                        System.out.println(message);
                    }

                } catch (IOException e) {
                    CloseClient(socket, bufferedWriter, bufferedReader);
                }
            }

        }).start();
    }

    private void CloseClient(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        String name = scanner.nextLine();
        Socket socket = new Socket("localhost", 4242);
        Client client = new Client(socket, name);
        System.out.println("Welcome to the ChatApp")
        client.ReceivingMessage();
        client.SendingMessage();

    }
}
