import java.awt.EventQueue;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

class Packet implements Serializable {

	private static final long serialVersionUID = 1L;
	public int type;
	public String s1 = null;
	public String s2 = null;
	public boolean b1 ;
	public ArrayList<FriendElement> friendElements = null;

	public Packet(int type) {
		this.type = type;  
	}
	public Packet(int type,String s1,boolean b1) {
		this.type = type;  
		this.s1 = s1;
		this.b1 = b1;
	}
	
	public Packet(int type,String s1,ArrayList<FriendElement> friendElements) {
		this.type = type;  // 2 update friends status
		this.s1 = s1;
		this.friendElements = friendElements;
	}	
	public Packet(int type,String s1) {
		this.type = type;  // 4 update status
		this.s1 = s1;
	}
	public Packet(int type, String s1, String s2) {
		this.type = type;
		this.s1 = s1;
		this.s2 = s2;
	}		
}

class FriendElement implements Serializable {

	private static final long serialVersionUID = 1L;
	public String name;
	public String status;
	public boolean online;
	public ArrayList<String> buffer = new ArrayList<String>();
	
	public FriendElement(String name,String status){
		this.name = name;
		this.status = status;
	}
	public FriendElement(String name,String status,boolean online){
		this.name = name;
		this.status = status;
		this.online=online;
	}
	public FriendElement(String name){
		this.name = name;
		this.status = "UNKNOWN";
	}
}

class User{
	
	public String login;
	public String password;
	public ArrayList<FriendElement> friendElements = new ArrayList<FriendElement>();
	public String status;
	public boolean online;
	public Connection con;
	
	public void logOut(){
		con = null;
		online = false;
	}
	public void logIn(Connection con){
		this.con = con;
		online = true;
	}	
	public User(String login,String password) {
		this.login=login;
		this.password=password;
	}
}

class Connection{
	
	Socket s;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	public User u;
	
	public void close() throws IOException{
		out.close();
		in.close();
		s.close();
	}
	
	public Connection(Socket s) throws IOException{
		this.s=s;
		out = new ObjectOutputStream(s.getOutputStream());
		in = new ObjectInputStream(s.getInputStream());
	}
}

public class KomunikatorServer {
	
	ServerSocket ss;
	ArrayList<Connection> connections = new ArrayList<Connection>();
	ArrayList<User> users = new ArrayList<User>();
		
	public static void main(String[] args) {	
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new KomunikatorServer();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});	
	}
	public void setUsers(){

		User u = new User("login","haslo");
		u.friendElements.add(new FriendElement("janusz"));
		u.friendElements.add(new FriendElement("haxior"));
		u.friendElements.add(new FriendElement("ania"));
		u.status = "Dostepny";
		
		User u2 = new User("janusz","januszek111");
		u2.friendElements.add(new FriendElement("login"));
		u2.friendElements.add(new FriendElement("haxior"));
		u2.status = "Dostepny";
		
		User u3 = new User("haxior","xxxxxx");
		u3.friendElements.add(new FriendElement("login"));
		u3.friendElements.add(new FriendElement("janusz"));
		u3.status = "Dostepny";
		
		User u4 = new User("ania","ania");
		u4.friendElements.add(new FriendElement("login"));
		u4.status = "Zaraz wracam";
		
		users.add(u);
		users.add(u2);
		users.add(u3);
		users.add(u4);
	}
	public void updateStatus(){
		for(User u : users){
			for(FriendElement friend : u.friendElements){
				for(User u1 : users){
					if(friend.name == u1.login){
						friend.status = u1.status;
						friend.online = u1.online;
					}
				}
			}
		}
	}
	
	public void sendStatus(User u) throws IOException{
		boolean online = u.online;
		for(FriendElement element : u.friendElements){ // buffered msg's clear
			for(User u1 :users){
				if(element.name.equals(u1.login)){
					if(u1.con!=null){
						u1.con.out.writeObject(new Packet(5,u.login,online));
					}
				}
			}
		}
	}
	
	public KomunikatorServer() throws IOException {
		
		setUsers();
		updateStatus();
		ss = new ServerSocket(32777);
		
		ss.setSoTimeout(1);
		Socket s;
		while(true){
			try{
				s = ss.accept();
				s.setSoTimeout(1);
				connections.add(new Connection(s));
			}catch(SocketTimeoutException e){}
			
			for(Connection con : connections){
				try {
					Packet p = (Packet) con.in.readObject();
					System.out.println("Server IN: "+p.type);
					switch(p.type){
					case 100:
						for(User u : users){
							if(p.s1.equals(u.login) && p.s2.equals(u.password)){
								if(!u.online){
									u.logIn(con);
									con.u = u;
									updateStatus();
									con.out.writeObject(new Packet(1,p.s1,(ArrayList<FriendElement>) u.friendElements.clone())); // welcome packet
									System.out.println("Server OUT: "+1);
									for(FriendElement element : u.friendElements){ // buffered msg's clear
										element.buffer.clear();
									}
									sendStatus(u);
								}
							}
						}
						if(con.u==null){
							con.out.writeObject(new Packet(4));
						}
						break;
					case 101: // wiadomosc
						for(User u : users){
							if(p.s1.equals(u.login)){
								if(u.con != null){
									if(u.online){
										u.con.out.writeObject(new Packet(3,con.u.login,p.s2));
										
										break;
									}
								}
								for(FriendElement element : u.friendElements){
									if(element.name.equals(con.u.login)){
										element.buffer.add(con.u.login+": "+p.s2);
									}
								}	
							}
						}
						break;
					case 102: // update status
						con.u.status = p.s1;
						updateStatus();
						for(FriendElement element :con.u.friendElements){
							for(User u1 : users){
								if(u1.login.equals(element.name)){
									if(u1.con!=null){
										u1.con.out.writeObject(new Packet(2,con.u.login,p.s1));
									}
								}
							}
						}
						break;
					case 103: // new account
						boolean found = false;
						for(User u : users){
							if(u.login.equals(p.s1)){
								con.out.writeObject(new Packet(6)); // error
								found = true;
							}
						}
						if(found){
							break;
						}
						User newUser = new User(p.s1,p.s2);
						newUser.logIn(con);
						newUser.status = "Dostepny";
						con.u = newUser;
						users.add(newUser);
						con.out.writeObject(new Packet(1,p.s1,newUser.friendElements)); // welcome packet			
						break;
					case 104: // add friend
						found = false;
						for(FriendElement element :con.u.friendElements){
							if(p.s1.equals(element.name)){
								found=true;
							}
						}
						for(User u : users){
							if(u.login.equals(p.s1)){
								if(!found){
									con.u.friendElements.add(new FriendElement(p.s1,u.status,u.online));
									u.friendElements.add(new FriendElement(con.u.login,con.u.status,con.u.online));
									if(con.u.friendElements.isEmpty()){
										System.out.println("Server FL empty");
									}
									if(u.online){
										u.con.out.writeObject(new Packet(1,u.login,(ArrayList<FriendElement>) u.friendElements.clone()));
									}
									con.out.writeObject(new Packet(1,con.u.login,(ArrayList<FriendElement>) con.u.friendElements.clone())); // welcome packet
								}
							}
						}
						
						break;
					case 105: // get users
						con.out.writeObject(new Packet(8));
						for(User u : users){
							con.out.writeObject(new Packet(7,u.login));
						}
						break;
					default:
						System.out.println("Server: Wrong packet id");
					}
					if(con.u==null){
						con.close();
						connections.remove(con);
						break;
					}
				}catch(SocketTimeoutException e){
				}catch(EOFException e){	
					System.out.println("Server zamykam socket1");
					con.u.logOut();
					con.close();
					connections.remove(con);
					updateStatus();
					sendStatus(con.u);
					break;
				}catch(SocketException e){
					System.out.println("Server zamykam socket2");
					con.u.logOut();
					con.close();
					connections.remove(con);
					updateStatus();
					sendStatus(con.u);
					break;
				}catch(Exception e){
					System.out.println("Server zamykam socket3");
					con.u.logOut();
					con.close();
					connections.remove(con);
					updateStatus();
					sendStatus(con.u);
					e.printStackTrace();
				}
				
			}
		}
	}
}
