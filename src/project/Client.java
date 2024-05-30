package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Client implements CallBackClientService, ProtocolImpl {

	private JList<String> userList;
	private Vector<String> userIdList = new Vector<>();

	private ClientFrame clientFrame;

	private JTextArea mainMessageBox;

	private Socket socket;

	private BufferedReader reader;
	private BufferedWriter writer;

	private String ip;
	private int port;
	private String id;

	private String protocol;
	private String from;
	private String message;

	public Client() {
		clientFrame = new ClientFrame(this);

		mainMessageBox = clientFrame.getMainMessageBox();

		userList = clientFrame.getUserList();
	}

	private void checkProtocol(String msg) {
		StringTokenizer tokenizer = new StringTokenizer(msg, "/");

		protocol = tokenizer.nextToken();
		from = tokenizer.nextToken();

		if (protocol.equals("ConnectedUser")) {
			connectedUser();
		} else if (protocol.equals("UserOut")) {
			userIdList.remove(from);
			userList.setListData(userIdList);
			outRoom();
		} else if (protocol.equals("Chatting")) {
			message = tokenizer.nextToken();
			chatting();
		} else if (protocol.equals("EnterUser")) {
			enterRoom();
		} else if (protocol.equals("NewUser")) {
			newUser();
		}
	}

	private void connectNetwork() {

		try {
			socket = new Socket(ip, port);

		} catch (UnknownHostException e) {
		} catch (IOException e) {
			clickOutRoomBtn();
		}
	}

	private void readThread() {
		new Thread(() -> {
			while (true) {
				try {
					String msg = reader.readLine();
					checkProtocol(msg);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "클라이언트 출력 장치 에러 !", "알림", JOptionPane.ERROR_MESSAGE);
					//
					break;
				}
			}
		}).start();
	}

	private void connetIO() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new PrintWriter(socket.getOutputStream()));

			readThread();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "클라이언트 출력 장치 에러 !", "알림", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void clickConnectServerBtn(String ip, int port, String id) {
		this.ip = ip;
		this.port = port;
		this.id = id;
		try {
			connectNetwork();
			connetIO();

			writer.write(id.trim() + "\n");
			writer.flush();

		} catch (Exception e) {
		}

	}

	private void writer(String str) {
		try {
			writer.write(str + "\n");
			writer.flush();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "클라이언트 출력 장치 에러 !", "알림", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void clickSendMessageBtn(String messageText) {
		writer("Chatting/" + id + "/" + messageText);
	}

	@Override
	public void clickOutRoomBtn() {
		writer("OutRoom/" + id);
	}

	@Override
	public void clickEnterRoomBtn() {
		writer("EnterRoom/" + id);
	}

	@Override
	public void chatting() {
		mainMessageBox.append(from + " ∶ " + message + "\n");
	}

	@Override
	public void newUser() {
		if (!from.equals(this.id)) {
			userIdList.add(from);
			userList.setListData(userIdList);
		}

	}

	@Override
	public void connectedUser() {
		userIdList.add(from);
		userList.setListData(userIdList);
	}

	@Override
	public void outRoom() {
		mainMessageBox.append(from + "님이 나가셨습니다.\n");
	}

	@Override
	public void enterRoom() {
		mainMessageBox.append(from + "님이 입장하셨습니다.\n");
	}

	public static void main(String[] args) {
		new Client();
	}

}
