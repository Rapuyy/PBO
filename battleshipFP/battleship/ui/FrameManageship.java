package battleship.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;

import battleship.Map;

public class FrameManageship extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 2923975805665801740L;
	private static final int NUM_SHIP = 10;
	LinkedList<int[]> playerShips;// contains the ships inserted, it is used to build the frameBattle
	boolean finished = false;
	int insertShip = 0;
	int[] counterShip = { 1, 2, 3, 4 };
	Map map;
	UIManagePanel choosePan;
	UIMapPanel mapPanel;

	public FrameManageship() {
		super("Battleship");
		map = new Map();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(900, 672);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.addKeyListener(this);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/images/icon.png")));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);
		UIJPanelBG container = new UIJPanelBG(
				Toolkit.getDefaultToolkit().createImage(getClass().getResource("/res/images/wood.jpg")));
		mapPanel = new UIMapPanel("manage");
		container.add(mapPanel);
		choosePan = new UIManagePanel();
		container.add(choosePan);
		mapPanel.setBounds(25, 25, 600, 620);
		choosePan.setBounds(580, 25, 280, 800);
		// Internal panel containing the ships to be positioned.
		this.add(container);
		for (int i = 0; i < mapPanel.bottom.length; i++) {
			for (int j = 0; j < mapPanel.bottom[i].length; j++) {
				mapPanel.bottom[i][j].addActionListener(this);
				mapPanel.bottom[i][j].setActionCommand("" + i + " " + j);
			}
		}
		choosePan.random.addActionListener(this);
		choosePan.reset.addActionListener(this);
		choosePan.play.addActionListener(this);
		playerShips = new LinkedList<int[]>();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		String text = source.getText();
		// RESET
		if (text.equals("reset")) {
			reset();
		}
		// RANDOM
		else if (text.equals("random")) {
			random();
		}
		// PLAY
		else if (text.equals("play")) {
			play();

		} else {
			if (finished) {
				return;
			}
			StringTokenizer st = new StringTokenizer(source.getActionCommand(), " ");
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int ship = -1;
			int dim = 0;
			int dir;
			for (int i = 0; i < choosePan.ship.length; i++) {
				if (choosePan.ship[i].isSelected())
					ship = i;
			}
			switch (ship) {
			case 0:
				dim = 4;
				break;
			case 1:
				dim = 3;
				break;
			case 2:
				dim = 2;
				break;
			case 3:
				dim = 1;
				break;
			}
			if (choosePan.direction[0].isSelected())// 0=orizzontale 1=verticale
				dir = 0;
			else
				dir = 1;
			boolean add = map.insertShip(x, y, dim, dir);
			if (add) {
				// increases the number of ships inserted
				insertShip++;
				// decreases the counter of the inserted ship
				counterShip[ship]--;
				choosePan.counterLabel[ship].setText("" + counterShip[ship]);
				// disable the ship if all have been entered and selected
				// automatically another ship to insert
				if (choosePan.counterLabel[ship].getText().equals("0")) {
					choosePan.ship[ship].setEnabled(false);
					for (int i = 0; i < choosePan.ship.length; i++) {
						if (choosePan.ship[i].isEnabled() && !choosePan.ship[i].isSelected()) {
							choosePan.ship[i].setSelected(true);
							break;
						}
					}
				}
				// check if we have add all ships (10)
				if (insertShip == NUM_SHIP) {
					finished = true;
					choosePan.direction[0].setEnabled(false);
					choosePan.direction[1].setEnabled(false);
					choosePan.play.setEnabled(true);
				}
				int[] data = { x, y, dim, dir };
				playerShips.add(data);
				mapPanel.drawShip(data);
			}
		}
		this.requestFocusInWindow();
	}

	private void random() {
		if (insertShip == NUM_SHIP) {
			reset();
		}
		Random r = new Random();
		int[] data = new int[4];
		for (int i = 0; i < counterShip.length; i++) {
			for (int j = 0; j < counterShip[i]; j++) {
				data = map.insertRandomShip(r, counterShip.length - i);
				playerShips.add(data);
				mapPanel.drawShip(data);
			}
		}
		insertShip = NUM_SHIP;
		finished = true;
		choosePan.play.setEnabled(true);
		for (int i = 0; i < choosePan.ship.length; i++) {
			choosePan.ship[i].setEnabled(false);
		}
		choosePan.direction[0].setEnabled(false);
		choosePan.direction[1].setEnabled(false);
		for (int i = 0; i < counterShip.length; i++) {
			counterShip[i] = 0;
			choosePan.counterLabel[i].setText("0");
		}
		choosePan.ship[0].setSelected(true);

	}

	private void reset() {
		map = new Map();
		playerShips = new LinkedList<int[]>();
		for (int i = 0; i < Map.DIM_MAP; i++) {
			for (int j = 0; j < Map.DIM_MAP; j++) {
				mapPanel.bottom[i][j].setEnabled(true);
			}
		}
		finished = false;
		choosePan.play.setEnabled(false);
		for (int i = 0; i < choosePan.ship.length; i++) {
			choosePan.ship[i].setEnabled(true);
		}
		choosePan.direction[0].setEnabled(true);
		choosePan.direction[1].setEnabled(true);
		for (int i = 0; i < counterShip.length; i++) {
			counterShip[i] = i + 1;
			choosePan.counterLabel[i].setText("" + (i + 1));
		}
		choosePan.ship[0].setSelected(true);
		insertShip = 0;
	}

	private void play() {
		FrameBattle battle = new FrameBattle(playerShips, map);
		battle.frame.setVisible(true);
		this.setVisible(false);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		char s = Character.toLowerCase(arg0.getKeyChar());
		int key = arg0.getKeyCode();
		if (s == 'g') {

			random();
			play();
		} else {
			if (s == 'r') {
				random();
			} else {
				if (key == KeyEvent.VK_DELETE || key == KeyEvent.VK_BACK_SPACE) {
					reset();
				} else {
					if (key == KeyEvent.VK_ESCAPE) {
						System.exit(0);
					}
				}
				if (key == KeyEvent.VK_ENTER) {
					if (finished) {
						play();
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

}
