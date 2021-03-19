package battleship.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import battleship.Computer;
import battleship.Map;
import battleship.Ship;
import battleship.Position;
import battleship.Report;

public class FrameBattle implements ActionListener, KeyListener {
	UIMapPanel playerPanel = new UIMapPanel("player");
	UIMapPanel cpuPanel = new UIMapPanel("cpu");
	JFrame frame = new JFrame("Battleship");
	JPanel comandPanel = new JPanel();
	Cursor cursorDefault = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	UIJPanelBG panel = new UIJPanelBG(
			Toolkit.getDefaultToolkit().createImage(getClass().getResource("/res/images/battleImg.jpg")));
	Report rep;
	Computer cpu;
	Map cpuMap;
	Map playerMap;
	int numShipPlayer = 10;
	int numShipCPU = 10;
	StringBuilder sb = new StringBuilder();
	boolean b = true;
	UIStatPanel statPlayer;
	UIStatPanel statCPU;
	JPanel targetPanel = new JPanel(null);
	UIJPanelBG target = new UIJPanelBG(
			Toolkit.getDefaultToolkit().createImage(getClass().getResource("/res/images/target.png")));
	ImageIcon wreck = new ImageIcon(getClass().getResource("/res/images/wreck.gif"));
	Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	Timer timer;
	boolean cpuTurn;

	public FrameBattle(LinkedList<int[]> playerShips, Map mappa) {
		playerMap = mappa;
		cpu = new Computer(mappa);
		cpuMap = new Map();
		cpuMap.fillMapRandom();
		frame.setSize(1080, 700);
		frame.setTitle("Battleship");
		frame.setFocusable(true);
		frame.requestFocusInWindow();
		frame.addKeyListener(this);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/images/icon.png")));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Panel containing the ships to be eliminated
		statPlayer = new UIStatPanel();
		statCPU = new UIStatPanel();
		statPlayer.setBounds(30, 595, 500, 80);
		statCPU.setBounds(570, 595, 500, 80);
		frame.add(statPlayer);
		frame.add(statCPU);
		// Target Panel
		targetPanel.setBounds(0, 0, 500, 500);
		targetPanel.setOpaque(false);
		playerPanel.sea.add(targetPanel);

		panel.add(playerPanel);
		playerPanel.setBounds(0, 0, UIMapPanel.X, UIMapPanel.Y);
		playerPanel.setOpaque(false);
		panel.add(cpuPanel);
		cpuPanel.setBounds(540, 0, UIMapPanel.X, UIMapPanel.Y);
		panel.add(comandPanel);
		frame.add(panel);
		frame.setResizable(false);
		timer = new Timer(2000, new TimerManager());
		cpuTurn = false;

		for (int i = 0; i < cpuPanel.bottom.length; i++) {
			for (int j = 0; j < cpuPanel.bottom[i].length; j++) {
				cpuPanel.bottom[i][j].addActionListener(this);
				cpuPanel.bottom[i][j].setActionCommand("" + i + " " + j);
			}
		}
		for (int[] v : playerShips) {
			playerPanel.drawShip(v);
		}

	}

	void setBox(Report rep, boolean player) {
		int x = rep.getP().getCoordX();
		int y = rep.getP().getCoordY();
		ImageIcon fire = new ImageIcon(getClass().getResource("/res/images/fireButton.gif"));
		ImageIcon water = new ImageIcon(getClass().getResource("/res/images/grayButton.gif"));
		String stuff;
		if (rep.isHit())
			stuff = "X";
		else
			stuff = "A";
		UIMapPanel mappanel;
		if (!player) {
			mappanel = playerPanel;
		} else {
			mappanel = cpuPanel;
		}
		if (stuff == "X") {
			mappanel.bottom[x][y].setIcon(fire);
			mappanel.bottom[x][y].setEnabled(false);
			mappanel.bottom[x][y].setDisabledIcon(fire);
			mappanel.bottom[x][y].setCursor(cursorDefault);
		} else {
			mappanel.bottom[x][y].setIcon(water);
			mappanel.bottom[x][y].setEnabled(false);
			mappanel.bottom[x][y].setDisabledIcon(water);
			mappanel.bottom[x][y].setCursor(cursorDefault);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (cpuTurn)
			return;
		JButton source = (JButton) e.getSource();
		StringTokenizer st = new StringTokenizer(source.getActionCommand(), " ");
		int x = Integer.parseInt(st.nextToken());
		int y = Integer.parseInt(st.nextToken());
		Position newP = new Position(x, y);
		boolean hit = cpuMap.shoot(newP);
		Report rep = new Report(newP, hit, false);
		this.setBox(rep, true);
		if (hit) { // the player continues to play
			Ship sunkenShip = cpuMap.sink(newP);
			if (sunkenShip != null) {
				numShipCPU--;
				setSink(sunkenShip);
				if (numShipCPU == 0) {
					Object[] options = { "New match", "Exit" };
					int n = JOptionPane.showOptionDialog(frame, (new JLabel("You won!", JLabel.CENTER)),
							"End Game", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
							options[1]);
					if (n == 0) {
						FrameManageship restart = new FrameManageship();
						restart.setVisible(true);
						this.frame.setVisible(false);
					} else {
						System.exit(0);
					}
				}
			}
		} else { // it's up to the CPU

			if (b) {
				timer.start();
				cpuTurn = true;
			}
		}
		frame.requestFocusInWindow();
	}

	private void setSink(Position p) {
		LinkedList<String> possibility = new LinkedList<String>();
		if (p.getCoordX() != 0) {
			possibility.add("N");
		}
		if (p.getCoordX() != Map.DIM_MAP - 1) {
			possibility.add("S");
		}
		if (p.getCoordY() != 0) {
			possibility.add("O");
		}
		if (p.getCoordY() != Map.DIM_MAP - 1) {
			possibility.add("E");
		}
		String direction;
		boolean found = false;
		Position currentPos;
		do {
			currentPos = new Position(p);
			if (possibility.isEmpty()) {
				deleteShip(1, statPlayer);
				playerPanel.bottom[currentPos.getCoordX()][currentPos.getCoordY()].setIcon(wreck);
				playerPanel.bottom[currentPos.getCoordX()][currentPos.getCoordY()].setEnabled(false);
				playerPanel.bottom[currentPos.getCoordX()][currentPos.getCoordY()].setDisabledIcon(wreck);
				playerPanel.bottom[currentPos.getCoordX()][currentPos.getCoordY()].setCursor(cursorDefault);
				return;
			}
			direction = possibility.removeFirst();
			currentPos.move(direction.charAt(0));
			if (playerMap.hit(currentPos)) {
				found = true;
			}
		} while (!found);
		int dim = 0;
		currentPos = new Position(p);
		do {

			playerPanel.bottom[currentPos.getCoordX()][currentPos.getCoordY()].setIcon(wreck);
			playerPanel.bottom[currentPos.getCoordX()][currentPos.getCoordY()].setEnabled(false);
			playerPanel.bottom[currentPos.getCoordX()][currentPos.getCoordY()].setDisabledIcon(wreck);
			playerPanel.bottom[currentPos.getCoordX()][currentPos.getCoordY()].setCursor(cursorDefault);
			currentPos.move(direction.charAt(0));

			dim++;
		} while (currentPos.getCoordX() >= 0 && currentPos.getCoordX() <= 9 && currentPos.getCoordY() >= 0
				&& currentPos.getCoordY() <= 9 && !playerMap.water(currentPos));

		deleteShip(dim, statPlayer);
	}

	private void setSink(Ship sunkenShip) {
		int dim = 0;
		for (int i = sunkenShip.getXin(); i <= sunkenShip.getXfin(); i++) {
			for (int j = sunkenShip.getYin(); j <= sunkenShip.getYfin(); j++) {
				cpuPanel.bottom[i][j].setIcon(wreck);
				cpuPanel.bottom[i][j].setEnabled(false);
				cpuPanel.bottom[i][j].setDisabledIcon(wreck);
				cpuPanel.bottom[i][j].setCursor(cursorDefault);
				dim++;
			}
		}
		deleteShip(dim, statCPU);
	}

	private void deleteShip(int dim, UIStatPanel panel) {
		switch (dim) {
		case 4:
			panel.ships[0].setEnabled(false);
			break;
		case 3:
			if (!panel.ships[1].isEnabled())
				panel.ships[2].setEnabled(false);
			else
				panel.ships[1].setEnabled(false);
			break;
		case 2:
			if (!panel.ships[3].isEnabled())
				if (!panel.ships[4].isEnabled())
					panel.ships[5].setEnabled(false);
				else
					panel.ships[4].setEnabled(false);
			else
				panel.ships[3].setEnabled(false);
			break;
		case 1:
			if (!panel.ships[6].isEnabled())
				if (!panel.ships[7].isEnabled())
					if (!panel.ships[8].isEnabled())
						panel.ships[9].setEnabled(false);
					else
						panel.ships[8].setEnabled(false);
				else
					panel.ships[7].setEnabled(false);
			else
				panel.ships[6].setEnabled(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int key = arg0.getKeyCode();
		if (key == KeyEvent.VK_ESCAPE) {
			FrameManageship manage = new FrameManageship();
			manage.setVisible(true);
			frame.setVisible(false);
		}

		sb.append(arg0.getKeyChar());
		if (sb.length() == 4) {
			int z = sb.toString().hashCode();
			if (z == 3194657) {
				sb = new StringBuilder();
				b = !b;
			} else {
				String s = sb.substring(1, 4);
				sb = new StringBuilder(s);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	public class TimerManager implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			timer.stop();
			boolean flag;

			Report report = cpu.myTurn();
			drawTarget(report.getP().getCoordX() * 50, report.getP().getCoordY() * 50);
			flag = report.isHit();
			setBox(report, false);
			if (report.isSunk()) {
				numShipPlayer--;
				setSink(report.getP());
				if (numShipPlayer == 0) {
					Object[] options = { "New match", "Exit" };
					int n = JOptionPane.showOptionDialog(frame, (new JLabel("You lost!", JLabel.CENTER)),
							"End Game", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
							options[1]);
					if (n == 0) {
						FrameManageship restart = new FrameManageship();
						restart.setVisible(true);
						frame.setVisible(false);
					} else {
						System.exit(0);
					}
				}
			}

			cpuTurn = false;
			if (flag) {
				timer.start();
				cpuTurn = true;
			}
			frame.requestFocusInWindow();
		}

	}

	public void drawTarget(int i, int j) {
		target.setBounds(j, i, 50, 50);
		target.setVisible(true);
		targetPanel.add(target);
		targetPanel.repaint();
	}
}
