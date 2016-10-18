import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

//13 37
public class TimeUDPServer {
	private static int PORT_TIME;
	private static final int LINELEN = 270;
	private static final long UNIXEPOCH = 2208988800L;
	private static long time;

	public static Date bytesToDate(byte[] buffer) {
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
		return d;
	}

	public static void main(String[] args) {
		try {
			byte[] buffer = new byte[LINELEN];
			PORT_TIME = Integer.parseInt(args[0]);
			DatagramSocket sock = new DatagramSocket(PORT_TIME);
			DatagramPacket packet = new DatagramPacket(buffer, 256);
			//System.out.println(packet + "  " + address + "   " + port);
			while(true){
			if (PORT_TIME == 37) {
				sock.receive(packet);
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				Calendar cal = Calendar.getInstance();
				time = cal.getTimeInMillis();
				time /= 1000L;
				time += UNIXEPOCH;
				byte[] bytes = new byte[5];

				//System.out.println("wysylam czas: " + time);
				bytes = ByteBuffer.allocate(4).putInt((int) time).array();

				//System.out.println(bytesToDate(bytes));
				DatagramPacket dp = new DatagramPacket(bytes, 0, 4, address,
						port);
				sock.send(dp);
			}
			if (PORT_TIME == 13) {
				sock.receive(packet);
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				String ladnyCzas = new Date().toString();
				
				byte[] bytes = ladnyCzas.getBytes();

				//System.out.println("wysylam czas: " + ladnyCzas);

				DatagramPacket dp = new DatagramPacket(bytes, 0, bytes.length, address,
						port);
				sock.send(dp);
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}