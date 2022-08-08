import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clienthandler = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String Usernameofclient;

    private String Server = "Server:";

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.Usernameofclient = bufferedReader.readLine();
            clienthandler.add(this);
            broadcastMessage(Server + Usernameofclient + " has joined the chat");


        } catch (IOException e) {
            CloseClientHandler(socket, bufferedReader, bufferedWriter);
        }
    }


    @Override
    public void run() {
        String ClientMessage;

        while (socket.isConnected()) {
            try {
                ClientMessage = bufferedReader.readLine();
                broadcastMessage(ClientMessage);
            } catch (IOException e) {
                CloseClientHandler(socket, bufferedReader, bufferedWriter);
                break;
            }
        }

    }

    public void broadcastMessage(String Messagesender) {
        for (ClientHandler Clienthandler : clienthandler) {
            try {
                if (!Clienthandler.Usernameofclient.equals(Usernameofclient)) {
                    Clienthandler.bufferedWriter.write(Messagesender);
                    Clienthandler.bufferedWriter.newLine();
                    Clienthandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                CloseClientHandler(socket, bufferedReader, bufferedWriter);
            }

        }
    }

    public void RemoveClienthandler() {
        clienthandler.remove(this);
        broadcastMessage(Server + Usernameofclient + " has left" + "\nTotal user count:" + clienthandler.size());
        System.out.println(Server + " User " + Usernameofclient + " has disconnected " + "\nClient's count:" + clienthandler.size());
    }

    private void CloseClientHandler(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        RemoveClienthandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
