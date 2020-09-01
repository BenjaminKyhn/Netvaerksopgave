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
                fromServer = new DataInputStream(socket.getInputStream());

                // Get temperature
                byte[] data = fromServer.readAllBytes();
                fromServer.close();
                String dataStr = new String(data, StandardCharsets.UTF_8);
                String tempStr = dataStr.substring(0, 4);
                String humStr = dataStr.substring(4,8);
                double temp = Double.parseDouble(tempStr);
                double hum = Double.parseDouble(humStr);

                Thread.sleep(2000);
            }
        } catch (IOException | InterruptedException ex1) {
            ex1.printStackTrace();
        }
    }
}

