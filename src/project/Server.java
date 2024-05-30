package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Server {

	private final int PORT = 5151;
	public Vector<ConnectedUser> connectedUsers = new Vector<>();
	private ServerFrame serverFrame;

	private JTextArea mainBoard;

	private ServerSocket serverSocket;
	private Socket socket;

	private FileWriter fileWriter;

	private String protocol;
	private String from;
	private String message;
	
	public Vector<ConnectedUser> getConnectedUsers() {
		return connectedUsers;
	}

	public Server() {
		serverFrame = new ServerFrame(this);
		mainBoard = serverFrame.getMainBoard();
	}

	
	
	// 서버 열기
	public void startServer() {
		try {
			serverSocket = new ServerSocket(PORT);
			serverViewAppendWriter("포트 번호 "+ PORT + " 서버 대기\n");
			serverFrame.getConnectBtn().setEnabled(false);
			serverFrame.getDeConnectBtn().setEnabled(true);
			connectClient();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "이미 서버가 열림", "알림", JOptionPane.ERROR_MESSAGE);
		}

	}

	// 서버 중단
	public void stopServer() {
		try {
			serverSocket.close();
			serverViewAppendWriter("서버종료\n");
			serverFrame.getConnectBtn().setEnabled(true);
			serverFrame.getDeConnectBtn().setEnabled(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 서버 로그
	private void serverViewAppendWriter(String log) {
		try {
			fileWriter = new FileWriter("server_log.txt", true);
			mainBoard.append(log);
			fileWriter.write(log);
			fileWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void connectClient() {
		new Thread(() -> {
			while (true) {
				try {
					socket = serverSocket.accept();
					serverViewAppendWriter("클라이언트 연결 완료\n");
					ConnectedUser user = new ConnectedUser(socket);
					user.start();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void broadcastMessage(String message) {
		for (int i = 0; i < connectedUsers.size(); i++) {
			ConnectedUser user = connectedUsers.elementAt(i);
			user.writer(message);
		}
	}

	private class ConnectedUser extends Thread implements ProtocolImpl {

		private Socket socket;

		private BufferedReader reader;
		// private PrintWriter writer;
		private BufferedWriter writer;

//		public BufferedWriter getWriter() {
//			return writer;
//		}

		private String id;

		public ConnectedUser(Socket socket) {
			this.socket = socket;
			connectIO();
		}

		private void connectIO() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new BufferedWriter(new PrintWriter(socket.getOutputStream()));

				sendInFomation();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void sendInFomation() {
			try {
				id = reader.readLine();
				serverViewAppendWriter(id + "님이 접속함\n");
				newUser();
				connectedUser();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				while(true) {
				String str = reader.readLine();
				checkProtocol(str);
				}
//				while ((str = reader.readLine()) != null) {
//				}
			} catch (IOException e) {
				serverViewAppendWriter(id +" 님이 연결을 끊음\n");
				connectedUsers.remove(this);
				broadcastMessage("UserOut/" + id);
			}
		}

		
			private void checkProtocol(String str) {
				StringTokenizer tokenizer = new StringTokenizer(str, "/");

				protocol = tokenizer.nextToken();
				from = tokenizer.nextToken();

				if (protocol.equals("Chatting")) {
					message = tokenizer.nextToken();
					chatting();

				} else if (protocol.equals("OutRoom")) {
					outRoom();
					
				} else if (protocol.equals("EnterRoom")) {
					enterRoom();
				}
			}
			
//		public void printlnln(String message) {
//			writer.println(message);
//		}

		private void writer(String str) {
			try {
				writer.write(str +"\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void chatting() {
			serverViewAppendWriter(from + " : " + message + "\n");
			for (int i = 0; i < connectedUsers.size(); i++) {
				ConnectedUser user = connectedUsers.elementAt(i);
				user.writer("Chatting/" + id + "/"+ message);
			}			
		}
		
		@Override
		public void outRoom() {
			for (int i = 0; i < connectedUsers.size(); i++) {
				ConnectedUser user = connectedUsers.elementAt(i);
				user.writer("UserOut/" + id);
			}	
		}
		
		@Override
		public void enterRoom() {
			for (int i = 0; i < connectedUsers.size(); i++) {
				ConnectedUser user = connectedUsers.elementAt(i);
				user.writer("EnterUser/" + id);
			}	
		}

		@Override
		public void newUser() {
			connectedUsers.add(this);
			for (int i = 0; i < connectedUsers.size(); i++) {
				ConnectedUser user = connectedUsers.elementAt(i);
				user.writer("NewUser/" + id);
			}	
			
		}

		@Override
		public void connectedUser() {
			for (int i = 0; i < connectedUsers.size(); i++) {
				ConnectedUser user = connectedUsers.elementAt(i);
				writer("ConnectedUser/" + user.id);
			}			
		}

	}

	public static void main(String[] args) {
		new Server();
	}

}