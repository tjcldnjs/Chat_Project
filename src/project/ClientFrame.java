package project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientFrame extends JFrame {

	private CallBackClientService callBackService;
	private Client mContext;
	private JTextField userListText;
	private BackgroundPanel backgroundPanel;

	private JTextField portTextField;
	private JTextField ipTextField;
	private JTextField idTextField;
	private JButton connectbtn;

	private JTextArea mainMessageBox;
	private	JScrollPane messageBoxScrollPane;
	
	private JTextField textArea;
	private JButton sendbtn;
	
	private JList<String> userList;

	public JButton getConnectbtn() {
		return connectbtn;
	}

	public JTextArea getMainMessageBox() {
		return mainMessageBox;
	}
	
	public JList<String> getUserList() {
		return userList;
	}

	public ClientFrame(Client mContext) {
		callBackService = mContext;
		this.mContext = mContext;
		initData();
		setInitLayout();
		addEventListener();
		connectbtn.addMouseListener(connectBtnMouseListener); 
	}
	

	private void initData() {
		backgroundPanel = new BackgroundPanel();

		ipTextField = new JTextField();
		portTextField = new JTextField();
		idTextField = new JTextField();
		connectbtn = new JButton("연결 하기");

		mainMessageBox = new JTextArea();
		messageBoxScrollPane = new JScrollPane();

		textArea = new JTextField();
		sendbtn = new JButton("전송");
		
		userList = new JList<String>();
		
		userListText = new JTextField("  참여자 목록");
		
	}

	private void setInitLayout() {
		setTitle("레전드 채팅 - 클라이언트");
		setSize(500, 350);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);

		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(backgroundPanel);

		ipTextField.setBounds(0, 1, 101, 28);
		ipTextField.setText("IP 입력");
		backgroundPanel.add(ipTextField);
		portTextField.setBounds(102, 1, 100, 28);
		portTextField.setText("PORT 입력");
		backgroundPanel.add(portTextField);
		idTextField.setBounds(202, 1, 100, 28);
		idTextField.setText("ID 입력");
		backgroundPanel.add(idTextField);
		connectbtn.setBounds(302, 1, 100, 28);
		backgroundPanel.add(connectbtn);

		mainMessageBox.setEditable(false);
		mainMessageBox.setForeground(Color.black);
		messageBoxScrollPane.setViewportView(mainMessageBox);
		messageBoxScrollPane.setBounds(0, 30, 402, 247);
	
		backgroundPanel.add(messageBoxScrollPane);
		

		textArea.setBounds(0, 278, 402, 30);
		backgroundPanel.add(textArea);
		sendbtn.setBounds(402, 278, 81, 30);
		backgroundPanel.add(sendbtn);
		
		userListText.setBounds(403,2,80,27);
		userListText.setEditable(false);
		backgroundPanel.add(userListText);
		userList.setBounds(403,30,80,247);
		
		backgroundPanel.add(userList);
		
		setVisible(true);
	}

	public void autoScrollPane() {
		messageBoxScrollPane.getVerticalScrollBar().setValue(messageBoxScrollPane.getVerticalScrollBar().getMaximum());
	}
	
	private void clickConnectBtn() {
		if ((!ipTextField.getText().equals(null)) && (!portTextField.getText().equals(null))
				&& (!idTextField.getText().equals(null))) {

			String ip = ipTextField.getText();
			String stringPort = portTextField.getText();
			int port = Integer.parseInt(stringPort);
			String id = idTextField.getText();

			callBackService.clickConnectServerBtn(ip, port, id);
	        connectbtn.setEnabled(false); // 연결 버튼 비활성화
	        connectbtn.removeMouseListener(connectBtnMouseListener);
		} else {
			JOptionPane.showMessageDialog(null, "입력한 정보를 확인하세요", "알림", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void sendMessage() {
		if (!textArea.getText().equals(null)) {
			String msg = textArea.getText();
			callBackService.clickSendMessageBtn(msg);
			textArea.setText("");
			textArea.requestFocus();
		}
	}

	private MouseAdapter connectBtnMouseListener = new MouseAdapter() {
	    public void mousePressed(MouseEvent e) {
	        clickConnectBtn();
	        mContext.clickEnterRoomBtn();
	    }
	};
	private void addEventListener() {
//		connectbtn.addMouseListener (new MouseAdapter () {
//			public void mousePressed(MouseEvent e) {
//				clickConnectBtn();
//				mContext.clickEnterRoomBtn();
//				
//			}
//		});
		sendbtn.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				sendMessage();
			}
		});
		textArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});
		
		ipTextField.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				ipTextField.setText("");
			}
		});
		
		portTextField.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				portTextField.setText("");
			}
		});
		
		idTextField.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				idTextField.setText("");
			}
		});
	}

	private class BackgroundPanel extends JPanel {

		private JPanel backgroundPanel;
		private Image backgroundImage;

		public BackgroundPanel() {
			backgroundImage = new ImageIcon("img/background1.png").getImage();
			backgroundPanel = new JPanel();
			add(backgroundPanel);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		}

	}
}
