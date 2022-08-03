import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
      Client client;
      Client client2;
      Socket socket = new Socket("localhost",5000);
    Socket socket2 = new Socket("localhost",5000);
//    ClientImpl cli  = new ClientImpl(socket);
//    ClientImpl cli2  = new ClientImpl(socket);
    List<ClientImpl> clientImpls = new ArrayList<>();
    //List<ClientImpl> clientImpls2 = new ArrayList<>();

    ClientTest() throws IOException {
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

       client = new Client(socket,"salifu");
       client2 = new Client(socket,"amanda");
//       client.startConnection("192.168.88.88",1234);
//       client2.startConnection("192.168.88.88",1234);
    }

    @Test
    void testIfClientAdded(){
        ClientImpl cli  = new ClientImpl(socket);
        int size = ClientImpl.getClientImpls().size();

        ClientImpl cli2  = new ClientImpl(socket);
        int size2 = ClientImpl.getClientImpls().size();
        assertNotEquals(size,size2);

    }

    @org.junit.jupiter.api.Test
    void sendMessage() {
     client.sendMessage();
     client2.sendMessage();

    }

    @org.junit.jupiter.api.Test
    void listenForMessage() {
    }

    @org.junit.jupiter.api.Test
    void closeEverything() {
    }
}