package project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class ServerFrame extends JFrame {

	private Server mContext;

	private ScrollPane scrollPane; // UI 들의 칸이 부족하면 위아래로 스크롤을 제공해준다.

	// 백그라운드 패널
	private BackgroundPanel backgroundPanel;

	// 메인보드
	private JPanel mainPanel;
	private JTextArea mainBoard; // JTextArea : 여러줄 입력가능 (줄바꿈차이)

	// 포트패널
	private JPanel portPanel;
	private JButton connectBtn;
	private JButton deconnectBtn;

	public ServerFrame(Server mContext) {
		this.mContext = mContext;
		initData();
		setInitLayout();
		addEventListener();
	}

	public JTextArea getMainBoard() {
		return mainBoard;
	}

	public JButton getConnectBtn() {
		return connectBtn;
	}

	public JButton getDeConnectBtn() {
		return deconnectBtn;
	}

	public void initData() {
		// 백그라운드 패널
		backgroundPanel = new BackgroundPanel();

		// 메인 패널
		mainPanel = new JPanel();
		mainBoard = new JTextArea();

		scrollPane = new ScrollPane();

		// 포트 패널
		portPanel = new JPanel();
		connectBtn = new JButton("서버 열기");
		deconnectBtn = new JButton("서버 끄기");
	}

	public void setInitLayout() {
		setTitle("레전드채팅 - 서버관리자"); // 프레임 타이틀 설정
		setSize(400, 500); // 프레임 크기 지정
		setLocationRelativeTo(null); // 화면 가운데로 보내기 위해
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫기 버튼을 눌렀을때 프로그램 종료
		setLayout(null); // 좌표값 세팅 하기위해서
		setResizable(false);

		// 백그라운드 패널
		backgroundPanel.setSize(getWidth(), getHeight()); // 백그라운드패널 사이즈 설정
		backgroundPanel.setLayout(null); // 좌표값 세팅 하기위해서
		add(backgroundPanel); // 백그라운드 생성

		// 포트패널 컴포넌트
		portPanel.setBounds(25, 80, 350, 50); // 절대위치와 크기지정 (지정된 x,y 위치로부터의 크기)
		portPanel.setBackground(new Color(0, 0, 0, 0)); // (r,g,b,a(alpha 투명도))
		portPanel.add(connectBtn); // 포트패널 위에 커넥트버튼 생성
		portPanel.add(deconnectBtn);
		backgroundPanel.add(portPanel); // 백그라운드패널 위에 포트패널 생성

		// 메인패널 컴포넌트
		mainPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0, 0)), "서버 로그"));
		mainPanel.setBounds(40, 150, 320, 300); // 절대위치와 크기지정 (지정된 x,y 위치로부터의 크기)
		mainPanel.setBackground(Color.WHITE); // 메인패널 백그라운드 흰색설정

		// mainBoard.setEnabled(false); // 입력불가상태 (텍스트 흰색고정)
		mainBoard.setEditable(false); // 입력불가상태 (텍스트 색상변경가능)
		mainBoard.setForeground(Color.BLACK); // 메인보드 텍스트 검정색
		mainPanel.setBackground(new Color(0, 0, 0, 0)); // 메인패널 투명
		mainPanel.add(scrollPane); // 메인 패널 위에 스크롤페인 생성

		scrollPane.setBounds(45, 100, 300, 290);
		scrollPane.add(mainBoard); // mainBoard = JTextArea
		backgroundPanel.add(mainPanel); // 백그라운드패널위에 메인패널 생성

		setVisible(true);
	}

	public void addEventListener() {
		// 필요한 메서드만 사용하기위해 -> MouseListener 보다 MouseAdapter 사용
		connectBtn.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mouseClicked(e);
				mContext.startServer();

			}
		});

		deconnectBtn.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mouseClicked(e);
				mContext.stopServer();
			}
		});
	}

	// 백그라운드 이미지 설정
	private class BackgroundPanel extends JPanel {

		private JPanel backgroundPanel;
		private Image backgroundImage;

		public BackgroundPanel() {
			backgroundImage = new ImageIcon("img/background.png").getImage();
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
