import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

class Packet implements Serializable {

	private static final long serialVersionUID = 1L;
	public int type;
	public String text;
	public int x;
	public int y;

	public Packet(String text) {
		this.text = text;
		type = 1;
	}

	public Packet(int type, int x, int y) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
}

class Receiver extends Thread {
	JButton[][] mojePola;
	JButton[][] obcePola;
	int[][] pola = new int[10][10];
	Socket s;
	DefaultListModel<String> chat;
	boolean connected = false;
	InputStream in;
	OutputStream out;
	ObjectOutputStream output;
	ObjectInputStream input;
	int x = -1;
	int y = -1;
	String chatText = null;
	boolean locked = true;

	public Receiver(JButton[][] mojePola, JButton[][] obcePola,
			DefaultListModel<String> chat) {
		this.mojePola = mojePola;
		this.obcePola = obcePola;
		this.chat = chat;
	}

	public void setSocket(Socket s) throws IOException {
		this.s = s;
		connected = true;
		in = s.getInputStream();
		out = s.getOutputStream();
		output = new ObjectOutputStream(out);
		input = new ObjectInputStream(in);
		s.setSoTimeout(1);
	}

	public void unlock() {
		locked = false;
	}

	public void lock() {
		locked = true;
	}

	public void setMove(int x, int y) {
		if (!locked) {
			this.x = x;
			this.y = y;
		}
	}

	public void setText(String text) {
		this.chatText = text;
	}

	public boolean isReady() {
		if (x != -1) {
			return true;
		}
		return false;
	}

	public boolean isReadyChat() {
		if (chatText != null) {
			return true;
		}
		return false;
	}

	public void run() {
		chat.addElement("polaczyles sie");
		while (true) {
			try {
				if (isReady() && !locked) {
					output.writeObject(new Packet(2, x, y));
					obcePola[x][y].setText("X");
					x = -1;
					y = -1;
					lock();
				}
				if (isReadyChat()) {
					output.writeObject(new Packet(chatText));
					chatText = null;
				}
				try {
					Packet tmp = (Packet) input.readObject();
					if (tmp.type == 1) {
						chat.addElement("On: " + tmp.text);
					}
					if (tmp.type == 2) {
						mojePola[tmp.x][tmp.y].setText("X");
						if (mojePola[tmp.x][tmp.y].getBackground() == Color.RED) {
							output.writeObject(new Packet(3, tmp.x, tmp.y));
						}
						unlock();
					}
					if (tmp.type == 3) {
						obcePola[tmp.x][tmp.y].setBackground(Color.RED);
						obcePola[tmp.x][tmp.y].setText("X");
					}
					if (tmp.type == 4) {
						obcePola[tmp.x][tmp.y].setText("X");
					}
				} catch (Exception e) {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

public class Main {

	class Buttons implements ActionListener {
		int i;
		int j;

		public Buttons(int i, int j) {
			this.i = i;
			this.j = j;
		}

		public void actionPerformed(ActionEvent e) {
			buttonPressed(i, j);
		}
	}

	JFrame frame;
	JPanel panel;
	JPanel info;
	JLabel infoLabel = new JLabel("nothing here!");
	DefaultListModel<String> model = new DefaultListModel<String>();
	JList<String> list = new JList<String>(model);
	JButton[][] mojePola = new JButton[10][10];
	JButton[][] obcePola = new JButton[10][10];
	final Receiver connection;

	public static void main(String[] args) {

		// Use the event dispatch thread for Swing components
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				new Main();
			}
		});

	}

	public boolean setShip(int x, int y, boolean k, int size) {
		if (k) {
			if((x-1)>=0){
				if (mojePola[x - 1][y].getBackground() == Color.red){
					return false;
				}
			}
			if((x+size)<10){
				if (mojePola[x + size][y].getBackground() == Color.red){
					return false;
				}
			}
			for (int i = 0; i < size; i++) {
				if (x + i >= 10) {
					return false;
				}
				if (mojePola[x + i][y].getBackground() == Color.red){
					return false;
				}
				if((y+1)<10){
				if(mojePola[x + i][y + 1].getBackground() == Color.red){
					return false;
				}}
				if((y-1)>=0){
				if(mojePola[x + i][y - 1].getBackground() == Color.red)	{	
					return false;
				}}
				
			}
			for (int i = 0; i < size; i++) {
				mojePola[x + i][y].setBackground(Color.red);
			}
			return true;
		} else {
			if((y-1)>=0){
				if (mojePola[x][y-1].getBackground() == Color.red){
					return false;
				}
			}
			if((y+size)<10){
				if (mojePola[x][y+size].getBackground() == Color.red){
					return false;
				}
			}
			for (int i = 0; i < size; i++) {
				if (y + i >= 10) {
					return false;
				}
				if (mojePola[x][y + i].getBackground() == Color.red){
					return false;
				}
			if((x+1)<10){
				if(mojePola[x + 1][y + i].getBackground() == Color.red){
					return false;
				}}
			if((x-1)>=0){
				if(mojePola[x - 1][y + i].getBackground() == Color.red)	{	
					return false;
				}}
			}
			for (int i = 0; i < size; i++) {
				mojePola[x][y + i].setBackground(Color.red);
			}
			return true;
		}
	}

	public void buttonPressed(int i, int j) {
		connection.setMove(i, j);
		
	}

	public void setGrid() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				if (j == 10) {
					info.add(new JLabel());
				}
				if (j < 10) {
					mojePola[i][j] = new JButton("");
					mojePola[i][j].setBackground(Color.black);
					// mojePola[i][j].addActionListener(new Buttons(i,j));
					info.add(mojePola[i][j]);
				} else {

					obcePola[i][j - 10] = new JButton("");
					obcePola[i][j - 10].setBackground(Color.black);
					obcePola[i][j - 10]
							.addActionListener(new Buttons(i, j - 10));
					info.add(obcePola[i][j - 10]);
				}

			}
		}
		int czworki = 1;
		int trojki = 2;
		int dwojki = 3;
		int jedynki = 4;
		Random randomizer = new Random();
		while (true) {
			if (czworki == 0 && trojki == 0 && dwojki == 0 && jedynki == 0) {
				break;
			}
			int x = randomizer.nextInt(10);
			int y = randomizer.nextInt(10);
			boolean k = randomizer.nextBoolean();
			if (czworki > 0) {
				if (setShip(x, y, k, 4)) {
					czworki--;
				}
			}
			if (trojki > 0) {
				if (setShip(x, y, k, 3)) {
					trojki--;
				}
			}
			if (dwojki > 0) {
				if (setShip(x, y, k, 2)) {
					dwojki--;
				}
			}
			if (jedynki > 0) {
				if (setShip(x, y, k, 1)) {
					jedynki--;
				}
			}
		}

	}

	public Main() {
		info = new JPanel();
		panel = new JPanel();
		frame = new JFrame();

		GridLayout grid = new GridLayout(10, 21);
		JButton host = new JButton("Host Game");
		JButton join = new JButton("Join Game");
		JButton chat = new JButton("Chat");

		connection = new Receiver(mojePola, obcePola, model);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Creating a Table Example");
		frame.setSize(1100, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.add(panel, BorderLayout.PAGE_START);
		frame.add(info, BorderLayout.CENTER);
		info.setLayout(grid);
		setGrid();

		panel.add(infoLabel);
		panel.add(host);
		panel.add(join);
		panel.add(chat);
		host.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame hostFrame;
				hostFrame = new JFrame();
				JLabel label = new JLabel("Waiting for player...");
				hostFrame.setTitle("Creating a Table Example");
				hostFrame.setSize(300, 100);
				hostFrame.setLocationRelativeTo(null);
				hostFrame.setVisible(true);
				hostFrame.add(label, BorderLayout.PAGE_START);
				ServerSocket serverSocket;
				try {
					serverSocket = new ServerSocket(32777);
					Socket connectionSocket = serverSocket.accept();
					connection.setSocket(connectionSocket);
					connection.start();
					connection.unlock();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				label.setText("CONNECTED!!!!1111oneone");
				infoLabel.setText("Connected!");
				hostFrame.dispose();
			}
		});
		join.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame hostFrame;
				hostFrame = new JFrame();
				JButton frameButton = new JButton("Join");
				final JTextField addr = new JTextField(10);
				addr.setText("127.0.0.1");

				hostFrame.setTitle("Creating a Table Example");
				hostFrame.setSize(300, 100);
				hostFrame.setLocationRelativeTo(null);
				hostFrame.setVisible(true);
				hostFrame.add(addr, BorderLayout.PAGE_START);
				hostFrame.add(frameButton, BorderLayout.PAGE_END);
				frameButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							Socket clientSocket = new Socket(addr.getText(),
									32777);
							connection.setSocket(clientSocket);
							connection.start();
							infoLabel.setText("Connected!");
							hostFrame.dispose();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		});
		chat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame hostFrame;
				hostFrame = new JFrame();
				hostFrame.setTitle("Creating a Table Example");
				hostFrame.setSize(600, 200);
				hostFrame.setLocationRelativeTo(null);
				hostFrame.setVisible(true);
				hostFrame.setLayout(new BorderLayout());
				final JTextField text = new JTextField(40);
				JScrollPane pane = new JScrollPane(list);
				JButton sendButton = new JButton("Send");
				hostFrame.add(pane, BorderLayout.NORTH);
				hostFrame.add(text, BorderLayout.WEST);
				hostFrame.add(sendButton, BorderLayout.EAST);
				sendButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						model.addElement("Ja: " + text.getText());
						connection.setText(text.getText());
					}
				});
			}
		});
	}
}