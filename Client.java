import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    
    public void createClientSocket(int portNumber) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", portNumber));
            dataOut = new DataOutputStream(socket.getOutputStream());
            dataIn = new DataInputStream(socket.getInputStream());
            System.out.println("Successfully connected to host ");
        } catch (Exception e) {
            System.err.println("Some Error Occured: " + e);
        }
    }

    public void communicate() throws IOException{
        String clientMessage = "", serverMessage = "";
        Scanner sc = new Scanner(System.in);
        do{
            System.out.print("Client: ");
            clientMessage = sc.nextLine();
            dataOut.writeUTF(clientMessage);
            serverMessage = dataIn.readUTF();

            System.out.println("Server: " + serverMessage);

        }while(!clientMessage.equals("QUIT"));

        sc.close();
    }
    public static void main(String []args) throws IOException{
        int portNumber = Integer.parseInt(args[0]);

        Client client = new Client();
        client.createClientSocket(portNumber);

        client.communicate();
        
    }

}
