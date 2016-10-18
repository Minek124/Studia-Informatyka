import java.net.*;

class TCPConnection extends Thread {
	
	public int i;
	public String adress;
	public TCPConnection (int i,String adress){
		this.i = i;
		this.adress = adress;
	}
	
	public void run()  {
		Socket clientSocket;
		try {
			clientSocket = new Socket(InetAddress.getByName(adress),i);
			//System.out.println("Polaczyles sie");
			System.out.println("moje ip "+clientSocket.getLocalSocketAddress()+" jego ip "+clientSocket.getRemoteSocketAddress());
			clientSocket.close();
		} catch (Exception e) {
			//System.out.println("port "+i+" zamkniety");
		}
	}
}
class UDPConnection extends Thread {
	
	public int i;
	public String adress;
	public UDPConnection (int i,String adress){
		this.i = i;
		this.adress = adress;
	}
	
	public void run()  {
		byte[] buffer = new byte[20000];
		DatagramSocket sock;
		DatagramPacket dp;
		try {
		sock = new DatagramSocket();
		sock.setSoTimeout(1000);
		dp = new DatagramPacket("hello".getBytes(), 0, "hello".length(), InetAddress.getByName(adress),i);
		sock.send(dp);
		dp = new DatagramPacket(buffer, 20000);
		sock.receive(dp);
		System.out.println("otwarty port UDP: "+i);
		//System.out.println(new String(dp.getData(), 0, dp.getLength()));
		sock.close();
		}
		catch (Exception e) {
			//System.out.println("zamkniety port UDP: "+i);
		}
		
	}
}

class Scanner {
	public static void main(String argv[]) throws Exception {
		String adress = "google.pl";
		 //InetAddress add = InetAddress.getByName("127.0.0.5");
		for(int i = 1; i<=1024;i++){
			new TCPConnection(i,adress).start();
			new UDPConnection(i,adress).start();
		}
		System.out.println("Koniec");
	}
}