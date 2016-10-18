import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class EchoUDPServer {
	private static final int PORT_ECHO = 7;
	private static final int LINELEN = 270;

	public static void main(String[] args) throws IOException {
		byte[] buffer = new byte[LINELEN];
		DatagramSocket sock = new DatagramSocket(PORT_ECHO);
		DatagramPacket packet = new DatagramPacket(buffer, 256);
		DatagramPacket dp;	
		while(true){
			sock.receive(packet);
			InetAddress address = packet.getAddress();
			int port = packet.getPort();
			System.out.println(packet + "  " + address + "   " + port);
			System.out.println(new String(buffer, 0, packet.getData().length));
			//buffer[k]='\0';
			dp = new DatagramPacket(buffer, 0, buffer.length, address, port);
			sock.send(dp);		
		}
		
	}
}