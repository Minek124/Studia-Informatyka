import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class EchoUDPClient {
	private static final int PORT_ECHO = 7;
	private static final int LINELEN = 100;

	public static void main(String[] args) {
		try {
			byte[] buffer = new byte[LINELEN];
			DatagramSocket sock = new DatagramSocket();
			DatagramPacket dp;
			for (int i = 1; i < args.length; i++) {
				dp = new DatagramPacket(args[i].getBytes(), 0, args[i].length(), InetAddress.getByName(args[0]),PORT_ECHO);
				sock.send(dp);
				dp = new DatagramPacket(buffer, LINELEN);
				sock.receive(dp);
				System.out.println(new String(dp.getData(), 0, dp.getLength()));
			}
			sock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}