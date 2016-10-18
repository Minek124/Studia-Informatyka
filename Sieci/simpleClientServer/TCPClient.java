import java.io.*;
import java.net.*;

class TCPClient {
	public static void main(String argv[]) throws Exception {
		String odpowiedz;
		InetAddress add = InetAddress.getByName("127.0.0.5");
		InetAddress add2 = InetAddress.getByName("127.0.0.4");
		Socket clientSocket = new Socket(add, 777,add2,888);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes("lolololo" + '\n');
		odpowiedz = inFromServer.readLine();
		System.out.println("FROM SERVER: " + odpowiedz);
		clientSocket.close();
	}
}