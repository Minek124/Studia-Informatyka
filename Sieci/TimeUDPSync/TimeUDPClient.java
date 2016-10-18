import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class TimeUDPClient {
	private static int PORT_TIME;
	private static final String QUERY = "Ktora godzina?";
	private static final int LINELEN = 5555;
	private static final long UNIXEPOCH = 2208988800L;

	public static void main(String[] args) {
		try {
			PORT_TIME = Integer.parseInt(args[1]);
			byte[] buffer = new byte[LINELEN];
			DatagramSocket sock = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(QUERY.getBytes(), 0,
					QUERY.length(), InetAddress.getByName(args[0]), PORT_TIME);
			sock.send(dp);
			dp = new DatagramPacket(buffer, LINELEN);
			sock.receive(dp);
			String s = new String(buffer,0,dp.getData().length);
			System.out.println("Ladny czas to : "+s);
			long time = 0;
			int i;
			for (i = 0; i < 4; i++) {
				time *= 256;
				time += (buffer[i] & 255);
			}
			System.out.println(time);
			time -= UNIXEPOCH;
			time *= 1000;
			Date d = new Date(time);
			System.out.println(d);
			sock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}