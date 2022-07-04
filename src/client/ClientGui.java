package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.ObjIntConsumer;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ClientGui extends JFrame {
	private JPanel CheckerBoard; // guiboard
	private Image BackGround;
	private client Client;
	private Socket Socket=null; //多人遊戲使用
	private static ObjectOutputStream objoutputstream; //for multiplayer
	private static ObjectInputStream objinputstream;//for multiplayer
	ClientGui(client _client) {
		Client = _client;
		this.StartPageInit();// 視窗的初始化
	}

	// StartPageInit
	private void StartPageInit() {
		var cp = this.getContentPane();
		cp.removeAll();
		cp.setLayout(null);

		// Local player Button
		JButton Local = new JButton();
		Local.setText("Local Play");
		Local.setBounds(450, 350, 100, 50);// x,y,w,h
		cp.add(Local);
		
		// Local player Button ActionListener
		Local.addActionListener((e) -> {
			gameBoardGuiInit("Single");
		});
		// Multiplayer Button
		JButton multiplayer = new JButton();
		multiplayer.setText("Multiplayer");
		multiplayer.setBounds(450, 410, 100, 50);
		cp.add(multiplayer);
		// MultiPlayer Button ActionListener
		multiplayer.addActionListener((e) -> { 
			gameBoardGuiInit("Multi");
			try {
				Socket = Client.getSocket();
				objoutputstream = new ObjectOutputStream(Socket.getOutputStream());
				objinputstream = new ObjectInputStream(Socket.getInputStream());
				// send action message
				objoutputstream.writeUTF("Pair");
				objoutputstream.flush();
			}catch(IOException er) {
	            System.out.println("Error : " + er.getMessage());
			}	
		});
		// -------------------------
		//
		this.setBounds(300, 50, 1000, 750);
		this.setTitle("Gobang Client");
		this.setVisible(true);
		this.repaint();
	}
//棋盤Gui初始化
	private void gameBoardGuiInit(String s) {
		var cp = this.getContentPane();
		cp.removeAll();
		cp.setLayout(null);

		// background
		try {
			BackGround = ImageIO.read(new File("D:\\codes\\eclipse-workspace\\GUIpr\\bin\\picture\\background.jpg"));
			BackGround = BackGround.getScaledInstance(725, 725, Image.SCALE_DEFAULT);
		} catch (IOException ex) {
			System.out.print("Can't get the background picture");
		}
		// CheckerBoard
		CheckerBoard = new JPanel() {

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(BackGround, 0, 0, this);
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(2));
				g2.setColor(Color.black);
				for (int x = 57; x < 705;) {
					g.drawLine(x, 10, x, 715);// 內線(直)
					g.drawLine(10, x, 715, x);// 橫
					x += 47;
				}
				g.drawRect(10, 10, 705, 705);// 外框

			}
		};
		CheckerBoard.setBounds(0, 0, 725, 725);

		// add
		cp.add(CheckerBoard);
		this.setBounds(this.getX(), this.getY(), 1000, 800);
		this.setTitle("Gobang Client");
		this.setVisible(true);
		// actionListener
		if (s == "Single") {
			SiglePlayerGameBoardGuiactionListener();
		} else if (s == "Multi") {
			MultiPlayerGameBoardGuiactionListener();
		}
	}
//遊戲結束頁面
	private void gameEndPage() {
		var cp = this.getContentPane();
		cp.removeAll();
		// 重新開始按鈕
		var restartBtn = new JButton();
		restartBtn.setText("Restart");
		restartBtn.setBounds(450, 350, 100, 50);// x,y,w,h
		cp.add(restartBtn);
		// Button ActionListener
		restartBtn.addActionListener((e) -> {
			gameBoardGuiInit("Single");
		});
		// -------------------------
		// 回到主選單按鈕
		var backtoMenuBtn = new JButton();
		backtoMenuBtn.setText("Back to Menu");
		backtoMenuBtn.setBounds(450, 405, 100, 50);
		cp.add(backtoMenuBtn);
		// Button ActionListener
		backtoMenuBtn.addActionListener((e) -> {
			StartPageInit();
		});
		// 結束了Label
		var gameOverLabel = new JLabel();
		gameOverLabel.setBounds(450, 300, 150, 75);
		gameOverLabel.setFont(new Font("Serif", Font.BOLD, 20));
		gameOverLabel.setText("Game Over");
		cp.add(gameOverLabel);
		this.setBounds(300, 50, 1000, 750);
		this.setTitle("Gobang Client");
		this.setVisible(true);
	}

//單人遊戲的事件追蹤
	private void SiglePlayerGameBoardGuiactionListener() {
		// CheckerBoard
		CheckerBoard.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				var x = e.getX() - 10;
				var y = e.getY() - 10;
				var temp = whosClicked(x, y, CheckerBoard.getWidth(), CheckerBoard.getHeight());
				// System.out.printf("position(%d, %d) was clicked\n", temp[0], temp[1]);
				// //print
				// where was clicked

				var accept = Client.getBoard().placechessman(Client.getTurner(), temp[0], temp[1]);// place the chessman
																									// on the visual
																									// board
				var _color = colorProvide(Client.getTurner());
				if (accept == 0) {//visual board place success
					drawChessmanOnBoardGui(e.getX(), e.getY(), _color);
					Client.Turnner();
				} // check the return value make sure the position was not clicked
				if (Client.getBoard().gameOver() == 1) { // game over
					System.out.println("the game is over!!!");
					gameEndPage();// clear the Gui
					Client.renewBoard();// renew the client visual board
				} // game over

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseClicked(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

	}
//多人遊戲的事件追蹤 (還沒改2021/08/02
	private void MultiPlayerGameBoardGuiactionListener() {
		//顯示配對等待中
		JLabel matching = new JLabel();
		matching.setText("matching....");
		matching.setFont(new Font("Serif", Font.BOLD, 16));
		matching.setBounds(0, 0, 200, 100);
		CheckerBoard.add(matching);
		CheckerBoard.repaint();

		
			try {
		//		socket = Client.connect();
		//		objoutputstream = new ObjectOutputStream(socket.getOutputStream());
		//		objinputstream = new ObjectInputStream(socket.getInputStream());
					var cmd = objinputstream.readUTF();
					System.out.println(cmd);
					if(cmd == "Matched") { //如果配對成功了
						CheckerBoard.remove(matching);//移除等待中字體
						CheckerBoard.repaint();
					}
				
			} catch (IOException e1) {
				System.out.println("IOException in MultiPlayerGui");
				e1.printStackTrace();
			}
		//配對成功就跳出並開始傳送滑鼠事件
		
		// CheckerBoard
		CheckerBoard.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//var x = e.getX() - 10;
				//var y = e.getY() - 10;
				//var temp = whosClicked(x, y, CheckerBoard.getWidth(), CheckerBoard.getHeight());
				//send message to server
			//	var obj = new model.SendingObj();
			//	obj.setChessLoaction(temp[0],temp[1]);
			//	objoutputstream.writeObject(obj);
			//	objoutputstream.flush();
				//get the echo from the server				
				
	
			//var accept = Client.getBoard().placechessman(Client.getTurner(), temp[0], temp[1]);// place the chessman
																								// on the visual
																									// board

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseClicked(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

	}


	private static Color colorProvide(Integer color) {
		if (color == model.Checkerboard.BLACK) {
			return Color.BLACK;
		} else {
			return Color.WHITE;
		}
	}

	private Integer[] whosClicked(int x, int y, int height, int width) { // 解析在棋盤GUI上誰被點擊
		if (x < 0 || x > 705 || y < 0 || y > 705) {
			return null;
		}
		height -= 20;
		width -= 20;
		var fx = x * 15. / width;
		var fy = y * 15. / height;
		Integer[] ans = { (int) fx, (int) fy };
		return ans;
	}

	private void drawChessmanOnBoardGui(Integer x, Integer y, Color color) {// 在GUI上畫棋子
		var g = CheckerBoard.getGraphics();
		g.setColor(color);
		g.fillOval(x - 20, y - 20, 40, 40);

		CheckerBoard.paintComponents(g);
	}

}