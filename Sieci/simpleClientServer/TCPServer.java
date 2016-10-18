import java.io.*;
import java.net.*;

class TCPServer {
	public static void main(String argv[]) throws Exception {
		String clientSentence;
		String capitalizedSentence;
		InetAddress add = InetAddress.getByName("127.0.0.5");
		ServerSocket welcomeSocket = new ServerSocket(777,7,add);
		System.out.println("Utworzono Socket");
		Socket connectionSocket = welcomeSocket.accept();
		System.out.println("IP klienta: " + connectionSocket.getLocalAddress()+" "+connectionSocket.getRemoteSocketAddress()+" "+connectionSocket.getPort()+" "+connectionSocket.getLocalPort());
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		outToClient.writeBytes("Pol¹czyles sie!");
		clientSentence = inFromClient.readLine();
		System.out.println("Received: " + clientSentence);
		capitalizedSentence = clientSentence.toUpperCase() + '\n';
		outToClient.writeBytes(capitalizedSentence);
		welcomeSocket.close();

	}
}