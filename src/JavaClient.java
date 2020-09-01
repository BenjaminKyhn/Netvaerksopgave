import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class JavaClient {
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    String host = "192.168.1.6";
    int port = 65432;

    public static void main(String[] args) {
        new JavaClient();
    }

    public JavaClient() {
        try {
            while (true) {
                Socket socket = new Socket(host, port);
                toServer = new DataOutputStream(socket.getOutputStream());
                fromServer = new DataInputStream(socket.getInputStream());

                // Get temperature
                byte[] temp = fromServer.readAllBytes();
                fromServer.close();
                String tempStr = new String(temp, StandardCharsets.UTF_8);
                System.out.println(tempStr);

//                // Get humidity
//                byte[] hum = fromServer.readAllBytes();
//                fromServer.close();
//                String humStr = new String(hum, StandardCharsets.UTF_8);
//                System.out.println(humStr);

                Thread.sleep(2000);
            }
        } catch (IOException | InterruptedException ex1) {
            ex1.printStackTrace();
        }
    }
}

