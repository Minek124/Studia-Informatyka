
import java.io.*;
import java.net.*;
import java.util.Scanner;

class Watek extends Thread {
	
	public BufferedReader inFromServer;
	public Watek(BufferedReader in){
		inFromServer = in;
	}
	
	public void www() throws Exception {
		while (true) {
			String odpowiedz = "";
			odpowiedz = inFromServer.readLine();
			System.out.println("FROM SERVER: " + odpowiedz);
			int port = 0;
			try {
				port = Integer.parseInt(odpowiedz);
				System.out.println("xxx");
			} catch (Exception e) {}
			if (port <= 7000 && port >= 6000) {
				System.out.println("lacze sie z serverem na porcie : " + port);
				Socket socketDoTransferu = null;
				socketDoTransferu = new Socket("127.0.0.6", port);
				InputStream pobieraczek = null;
				pobieraczek = socketDoTransferu.getInputStream();
				byte[] buffer = new byte[1400];
				int ileBytes = 0;
				ileBytes = pobieraczek.read(buffer);
				String nazwaPliku = new String(buffer);
				
				nazwaPliku= nazwaPliku.trim();
				File plik = new File(nazwaPliku);
				
				FileOutputStream s = new FileOutputStream(plik);
				while(true){
					ileBytes = pobieraczek.read(buffer);
					s.write(buffer, 0, ileBytes);
					s.flush();
					System.out.println("odebralem plik: " +  ileBytes);
					if(ileBytes<1400){
						break;
					}
				}

				socketDoTransferu.close();
			}
		}
	}
	
	public void run()  {
		try {
			www();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


class TCPClient {
	public static void main(String argv[]) throws Exception {
		Socket clientSocket = new Socket("127.0.0.6", 777);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		Watek t = new Watek(inFromServer);
		t.start();
		Scanner input = new Scanner(System.in);
		while(true){
			String komenda =  input.nextLine();
			outToServer.writeBytes(komenda + '\n');
			//System.out.println("wyslano: " + komenda);
			//input.close();
			
		}
	}
}