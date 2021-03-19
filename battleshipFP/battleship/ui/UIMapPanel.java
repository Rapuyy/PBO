package battleship.ui;

import java.awt.Cursor;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UIMapPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static int X = 570;
	static int Y = 630;
	int numC = 10;
	int dimC = 48;
	int horoff = 1;
	int veroff = 1;
	int c1Off = 0;
	int c2Off = 0;
	JButton[][] bottom;
	JLabel[] COr;
	JLabel[] CVer;
	protected JLabel label;
	UIJPanelBG sea;
	Cursor cursorHand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	Cursor cursorDefault = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	public UIMapPanel(String etichetta) {

		this.setSize(X, Y);
		this.setLayout(null);
		this.setOpaque(false);
		// Label
		label = new JLabel();
		label.setIcon(new ImageIcon(getClass().getResource(("/res/images/" + etichetta + ".png"))));
		this.add(label);
		label.setBounds(50, 0, 550, 60);
		// Panel containing the boxes
		sea = new UIJPanelBG(
				Toolkit.getDefaultToolkit().createImage(FrameManageship.class.getResource("/res/images/sea.png")));
		sea.setBounds(34, 45, 550, 550);
		bottom = new JButton[numC][numC];
		ImageIcon gray = new ImageIcon(getClass().getResource("/res/images/grayButtonOpaque.png"));
		for (int i = 0; i < numC; i++) {
			for (int j = 0; j < numC; j++) {
				bottom[i][j] = new JButton(gray);
				bottom[i][j].setSize(dimC, dimC);
				sea.add(bottom[i][j]);
				bottom[i][j].setCursor(cursorHand);
				bottom[i][j].setBorder(null);
				bottom[i][j].setOpaque(false);
				bottom[i][j].setBorderPainted(false);
				bottom[i][j].setContentAreaFilled(false);
				bottom[i][j].setBounds(horoff, veroff, dimC, dimC);
				if (etichetta.equals("player")) {
					bottom[i][j].setCursor(cursorDefault);
					bottom[i][j].setDisabledIcon(gray);
					bottom[i][j].setEnabled(false);
				} else {
					bottom[i][j].setCursor(cursorHand);
				}
				horoff += dimC + 2;
			}
			veroff += dimC + 2;
			horoff = 1;
		}
		horoff = 40;
		veroff = 0;
		JPanel grid = new JPanel(null);
		grid.setOpaque(false);
		grid.add(sea);
		COr = new JLabel[10];
		CVer = new JLabel[10];
		// For that load the coordinate images
		for (int i = 0; i < 10; i++) {
			COr[i] = new JLabel();
			CVer[i] = new JLabel();
			grid.add(COr[i]);
			grid.add(CVer[i]);
			CVer[i].setIcon(new ImageIcon(getClass().getResource((("/res/images/coord/" + (i + 1) + ".png")))));
			CVer[i].setBounds(veroff, horoff, dimC, dimC);
			COr[i].setIcon(new ImageIcon(getClass().getResource((("/res/images/coord/" + (i + 11) + ".png")))));
			COr[i].setBounds(horoff, veroff, dimC, dimC);
			horoff += 50;
		}

		this.add(grid);
		grid.setBounds(0, 45, 550, 660);

	}

	void drawShip(int[] data) {
		int x = data[0];
		int y = data[1];
		int dim = data[2];
		int dir = data[3];
		ImageIcon shipDim1orizz = new ImageIcon(
				getClass().getResource("/res/images/shipDim1orizz.png"));
		ImageIcon shipDim1vert = new ImageIcon(getClass().getResource("/res/images/shipDim1vert.png"));
		if (dim == 1) {
			bottom[x][y].setEnabled(false);
			if (dir == 0)
				bottom[x][y].setDisabledIcon(shipDim1orizz);
			else if (dir == 1)
				bottom[x][y].setDisabledIcon(shipDim1vert);
		} else {
			ImageIcon shipHeadLeft = new ImageIcon(
					getClass().getResource("/res/images/shipHeadLeft.png"));
			ImageIcon shipHeadTop = new ImageIcon(
					getClass().getResource("/res/images/shipHeadTop.png"));
			ImageIcon shipBodyLeft = new ImageIcon(
					getClass().getResource("/res/images/shipBodyLeft.png"));
			ImageIcon shipBodyTop = new ImageIcon(
					getClass().getResource("/res/images/shipBodyTop.png"));
			ImageIcon shipFootLeft = new ImageIcon(
					getClass().getResource("/res/images/shipFootLeft.png"));
			ImageIcon shipFootTop = new ImageIcon(
					getClass().getResource("/res/images/shipFootTop.png"));
			if (dir == 0) {// horizontal direction
				// Ship Head
				bottom[x][y].setDisabledIcon(shipHeadLeft);
				bottom[x][y].setEnabled(false);
				// Ship Body
				for (int i = 1; i < dim - 1; i++) {
					bottom[x][y + i].setDisabledIcon(shipBodyLeft);
					bottom[x][y + i].setEnabled(false);
				}
				// Ship Foot
				bottom[x][y + dim - 1].setDisabledIcon(shipFootLeft);
				bottom[x][y + dim - 1].setEnabled(false);
			} else { // vertical direction
				// Ship Head
				bottom[x][y].setDisabledIcon(shipHeadTop);
				bottom[x][y].setEnabled(false);
				// Ship Body
				for (int i = 1; i < dim - 1; i++) {
					bottom[x + i][y].setDisabledIcon(shipBodyTop);
					bottom[x + i][y].setEnabled(false);
				}
				// Ship Foot
				bottom[x + dim - 1][y].setDisabledIcon(shipFootTop);
				bottom[x + dim - 1][y].setEnabled(false);
			}
		}
	}

}
