import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientImpl implements Runnable {
    public static List<ClientImpl> clientImpls = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientImpl(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
          clientImpls.add(this);
          broadCastMessage(  clientUsername + " has entered the chat!");
        }catch(IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public static List<ClientImpl> getClientImpls() {
        return clientImpls;
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()){
           try{
               messageFromClient = bufferedReader.readLine();
               if(messageFromClient == null)throw new IOException();
               broadCastMessage(messageFromClient);
           }catch (IOException e){
               closeEverything(socket,bufferedReader,bufferedWriter);
               break;

           }
        }

    }
    public void broadCastMessage(String messageToSend){
      for (ClientImpl clientImpl : clientImpls){
          try{
              if(!clientImpl.clientUsername.equals(clientUsername)){
                 clientImpl.bufferedWriter.write(messageToSend);
                 clientImpl.bufferedWriter.newLine();
                 clientImpl.bufferedWriter.flush();
              }
          }catch (IOException e){
              closeEverything(socket,bufferedReader,bufferedWriter);
          }

      }
    }
    public void removeClientHandler(){
        clientImpls.remove(this);
        broadCastMessage( clientUsername + " has left the chat! ");
    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
       removeClientHandler();
       try{
           if(bufferedReader != null){
               bufferedReader.close();
           }
           if (bufferedWriter != null){
               bufferedWriter.close();
           }
           if (socket != null){
               socket.close();
       }
        }catch (IOException e){
           e.printStackTrace();
       }
    }

}
