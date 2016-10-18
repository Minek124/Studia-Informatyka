import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

class Receiver extends Thread {
	
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	Client ui;
	public boolean connected;

	public Receiver(Client ui) {
		this.connected = false;
		this.ui=ui;
	}
	public void setSocket(Socket s) throws IOException {
		this.s = s;
		out = new ObjectOutputStream(s.getOutputStream());
		in = new ObjectInputStream(s.getInputStream());
	}	
	public void sendPacket(Packet p) throws IOException{
		out.writeObject(p);
	}	
	public void switchChat(int index){ 
		if(index == -1){
			return;
		}
		ui.chatElements.clear();
        for(String msg :ui.people.get(index).buffer){
        	ui.chatElements.addElement(msg);
        }
	}
	
	public void refreshFriends(){
		ui.friendsElements.clear();
		for(FriendElement element : ui.people){
			if(!element.online){
				ui.friendsElements.addElement("[Niedostepny] "+element.name);
			}else{
				if(element.status.equals("Niewidoczny")){
					ui.friendsElements.addElement("[Niedostepny] "+element.name);
				}else{
					ui.friendsElements.addElement("["+element.status+"] "+element.name);
				}
			}
		}
	}
	
	public void logOut() throws IOException{
		connected=false;
		s.close();
		ui.infoLabel.setText("Logged out!");
		ui.people.clear();
		ui.friendsElements.clear();
		ui.chatElements.clear();
		ui.join.setText("Connect");
		ui.register.setText("new account");
		ui.addressField.setVisible(true);
		ui.passwordField.setVisible(true);
	}
	

	
	public void run() {
		while (true) {
			try {
				Thread.sleep(1); // ?
			} catch (InterruptedException e1) {}
			
			if(connected){
				try {
					Packet p = (Packet) in.readObject();
					System.out.println("Client IN: "+p.type);
					switch(p.type){
					case 1: //welcome packet
						ui.infoLabel.setText("Zalogowano jako "+p.s1);
						ui.people.clear();
						if(p.friendElements.isEmpty()){
							System.out.println("FL empty");
						}
						for(FriendElement element : p.friendElements){
							ui.people.add(element);
						}
						refreshFriends();
						break;
					case 2: // update friend list
						for(FriendElement element : ui.people){
							if(element.name.equals(p.s1)){
								element.status=p.s2;
							}
						}
						refreshFriends();
						break;
					case 3:   // wiadomosc
						int i = 0;
						for(FriendElement element : ui.people){
							if(p.s1.equals(element.name)){
								element.buffer.add(ui.people.get(i).name+": "+p.s2);
								if(ui.friendList.getSelectedIndex() == i){
									ui.chatElements.addElement(ui.people.get(i).name+": "+p.s2);
								}else{
									switchChat(i);
									ui.friendList.setSelectedIndex(i);
								}
							}
							i++;
						}
						break;
					case 4: //login error
						logOut();
						ui.infoLabel.setText("niepoprawny login lub haslo");
						break;
					case 5: // set friend as online
						for(FriendElement element : ui.people){
							if(element.name.equals(p.s1)){
								element.online = p.b1;
							}
						}
						refreshFriends();
						break;
					case 6: // creating account error
						logOut();
						ui.infoLabel.setText("new account error");
						break;
					case 7:
						ui.chatElements.clear();
						break;	
					case 8:
						ui.chatElements.addElement(p.s1);
						break;
					case 10: // custom error
						ui.infoLabel.setText("error: "+p.s1);
						break;
					default:
						System.out.println("Client: Wrong packet id");
					}
				}catch (SocketException e) {
				}catch (NullPointerException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}

public class Client {

	JFrame frame;
	JPanel top;
	JPanel middle;
	JPanel bottom;
	ArrayList<FriendElement> people = new ArrayList<FriendElement>();
	DefaultListModel<String> chatElements = new DefaultListModel<String>();
	DefaultListModel<String> friendsElements = new DefaultListModel<String>();
	JList<String> chatList = new JList<String>(chatElements);
	JList<String> friendList = new JList<String>(friendsElements);
	JButton join = new JButton("Connect");
	JButton register = new JButton("New Account");
	JButton sendButton = new JButton("Send");
	JTextField sendText = new JTextField(40);
	JTextField loginField = new JTextField(10);
	JTextField passwordField = new JTextField(10);
	JTextField addressField = new JTextField(10);
	JLabel infoLabel = new JLabel("nothing here!");
	JLabel chatInfoLabel = new JLabel("-");
	Receiver connection;
	


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Client();
			}
		});
	}
	public Client() {
		
		connection = new Receiver(this);
		connection.start();

		frame = new JFrame();
		top = new JPanel();
		middle = new JPanel();
		bottom = new JPanel();

		JRadioButton dostepny   = new JRadioButton("Dostêpny"  , true);
		JRadioButton niewidoczny    = new JRadioButton("Niewidzoczny"   , false);
		JRadioButton zw = new JRadioButton("Zaraz Wracam", false);

		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(dostepny);
		bgroup.add(niewidoczny);
		bgroup.add(zw);
		
		loginField.setText("login");
		passwordField.setText("haslo");
		addressField.setText("127.0.0.1");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Komunikator XXX");
		frame.setSize(850, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.add(top, BorderLayout.PAGE_START);
		frame.add(middle, BorderLayout.CENTER);
		frame.add(bottom, BorderLayout.PAGE_END);
		
		top.add(chatInfoLabel);
		top.add(addressField);
		top.add(join);
		top.add(infoLabel);
		top.add(loginField);
		top.add(passwordField);
		top.add(register);

		JScrollPane jsp1 = new JScrollPane(chatList);
		JScrollPane jsp2 = new JScrollPane(friendList);
		jsp1.setPreferredSize( new Dimension( 600, 350 ) );
		jsp2.setPreferredSize( new Dimension( 200, 350 ) );
		
		middle.add(jsp1, BorderLayout.EAST);
		middle.add(jsp2, BorderLayout.WEST);
		
		bottom.add(sendText);
		bottom.add(sendButton);
		bottom.add(dostepny);
		bottom.add(niewidoczny);
		bottom.add(zw);
	
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(friendList.getSelectedIndex() == -1){
					return;
				}
				chatElements.addElement("Ja: " + sendText.getText());
				people.get(friendList.getSelectedIndex()).buffer.add("Ja: " + sendText.getText());
				String name = people.get(friendList.getSelectedIndex()).name;
				if(name != null){
					try {
						connection.out.writeObject(new Packet(101,name,sendText.getText()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		dostepny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(connection.out!=null){
						connection.out.writeObject(new Packet(102,"Dostepny"));
					}
				} catch (IOException e1) {
				}
			}
		});
		
		niewidoczny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(connection.out!=null){
						connection.out.writeObject(new Packet(102,"Niewidoczny"));
					}
				} catch (IOException e1) {
				}
			}
		});
		
		zw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(connection.out!=null){
						connection.out.writeObject(new Packet(102,"Zaraz wracam"));
					}
				} catch (IOException e1) {
				}
			}
		});
		
		friendList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 1) {
		        	if(friendList.getSelectedIndex() == -1){
		        		return;
		        	}
		           connection.switchChat(friendList.getSelectedIndex());
		           chatInfoLabel.setText("rozmawiasz z: "+people.get(friendList.getSelectedIndex()).name);
		         }
		    }
		});
		
		join.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!connection.connected){
						
						Socket clientSocket = new Socket(addressField.getText(),32777);
						connection.setSocket(clientSocket);
						connection.connected=true;
						connection.sendPacket(new Packet(100,loginField.getText(),passwordField.getText()));
						infoLabel.setText("try again!");
						join.setText("Log out");
						loginField.setText("dodaj przyjaciela");
						register.setText("add friend");
						addressField.setVisible(false);
						passwordField.setVisible(false);
					}else{
						connection.logOut();
					}
				} catch (ConnectException e1) {
					infoLabel.setText("Connection refused!");
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!connection.connected){
						Socket clientSocket = new Socket(addressField.getText(),32777);
						connection.setSocket(clientSocket);
						
						connection.connected=true;
						connection.sendPacket(new Packet(103,loginField.getText(),passwordField.getText()));
						infoLabel.setText("Connected not logged in!");
						loginField.setText("dodaj przyjaciela");
						join.setText("Log out");
						register.setText("add friend");
						addressField.setVisible(false);
						passwordField.setVisible(false);
						
					}else{
						// add friend
						connection.sendPacket(new Packet(104,loginField.getText()));
					}
				} catch (ConnectException e1) {
					infoLabel.setText("Connection refused!");
				}catch (Exception e1) {
					try {
						connection.logOut();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					e1.printStackTrace();
				}
			}
		});
	}
}