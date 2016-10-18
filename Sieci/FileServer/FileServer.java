import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class FileServer {
	
	public byte[][] divideArray(byte[] source, int chunksize) {


        byte[][] ret = new byte[(int)Math.ceil(source.length / (double)chunksize)][chunksize];

        int start = 0;

        for(int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(source,start, start + chunksize);
            start += chunksize ;
        }

        return ret;
    }
	
	public static void main(String[] args) throws Exception {
		//File folder = new File("C:/Users/minek/Documents/pliki");
		//File[] listOfFiles = folder.listFiles();
		ServerSocket serverSocket = new ServerSocket(777);
		System.out.println("Utworzono Socket");
		Socket connectionSocket = serverSocket.accept();
		System.out.println("Mam klienta, jego IP: " + connectionSocket.getRemoteSocketAddress()+" moj address"+connectionSocket.getLocalSocketAddress());
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		//outToClient.writeBytes("Polaczyles sie!"+ '\n');
		while (true){
			String komenda;
			komenda = inFromClient.readLine();
			System.out.println("Received: " + komenda);
			if(komenda.matches("dir")){
				File folder = new File("C:/Users/minek/Documents/pliki");
				File[] listOfFiles = folder.listFiles();
				for (int i = 0; i < listOfFiles.length; i++) {
					outToClient.writeBytes(""+listOfFiles[i].getName()+ '\n');
					//System.out.println("Wyslano: " + komenda);
				}
			}else
			if(komenda.startsWith("get")){
				boolean znaleziono = false;
				File folder = new File("C:/Users/minek/Documents/pliki");
				File[] listOfFiles = folder.listFiles();
				for (int i = 0; i < listOfFiles.length; i++) {
					if(komenda.endsWith(listOfFiles[i].getName())){
						znaleziono = true;
						System.out.println("wysylam "+listOfFiles[i].getName());
						int port = (int) (Math.random()*1000+6000);
						System.out.println("wysylam prosbe otwarcia portu "+port+" aby wyslac pliki");
						outToClient.writeBytes(""+port+'\n');
						ServerSocket wysylacz = new ServerSocket(port);
						System.out.println("Utworzono socket do wysylania pliku na porcie "+port);
						Socket w = wysylacz.accept();
						System.out.println("Mam go, jego IP: " + w.getRemoteSocketAddress()+" moj address "+w.getLocalSocketAddress());
						
						OutputStream fileToClient = w.getOutputStream();
						fileToClient.write(listOfFiles[i].getName().getBytes(), 0, listOfFiles[i].getName().getBytes().length);
						FileInputStream s = new FileInputStream(listOfFiles[i]);
						byte[] fileBytes = new byte[1400];
						while (true){
							int ileBytes=s.read(fileBytes);
							fileToClient.write(fileBytes, 0, ileBytes);
							fileToClient.flush();
							System.out.println("wyslano bajty: "+ ileBytes);
							if(ileBytes<1400){
								break;
							}
						}
						wysylacz.close();
					}
				}
				if(!znaleziono){
					outToClient.writeBytes("nie ma takiego pliku"+ '\n');
				}
			}else{
				outToClient.writeBytes("zla komenda"+ '\n');
			}
		}
		
		/*
		 * File file = new File("ala.txt"); Scanner in = new Scanner(file);
		 * String zdanie = in.nextLine(); System.out.println(zdanie);
		 */
	}
}