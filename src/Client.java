import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client( Socket socket,String username) {
        try {
            this.username = username;
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

//    public void startConnection() throws IOException {
//        Socket socket = new Socket();
//        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//    }

    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + " " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }


        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);


        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);

                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String username = scanner.nextLine();
        Socket socket = null;
       try {
           socket = new Socket("localhost",5000);
           Client client = new Client(socket, username);
           client.listenForMessage();
           client.sendMessage();
       }catch (IOException e){
           e.printStackTrace();

       }
        if ( socket != null && socket.isConnected()){
            socket.close();
        }
       }




}
