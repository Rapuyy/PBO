package FP;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Menu {
	//inisiasi button	
	public Rectangle playButton = new Rectangle(TorpedoCat.WIDHT/2 - 50, 180, 100, 50);
	public Rectangle quitButton = new Rectangle(TorpedoCat.WIDHT/2 - 50, 250, 100, 50);
	
	private BufferedImage background; // Background Menu
	
	public Menu(){
		try {
			background = ImageIO.read(getClass().getResource("/Cover2.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
//------------------------------------------------------------------------------------------------
//tampilan judul
		g.drawImage(background, 0, 0, 640, 480 , background.getWidth(), background.getHeight(),0, 0, null);
		Font font = new Font("Comic Sans MS", Font.BOLD, 50);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Torpedo Cat", TorpedoCat.WIDHT/2 - 150, 80);		
//------------------------------------------------------------------------------------------------		
//tampilan play quit
		g.setColor(Color.BLACK);
		g.fillRect(TorpedoCat.WIDHT/2 - 50, 180, 100, 50); //kotaknya
		g.fillRect(TorpedoCat.WIDHT/2 - 50, 250, 100, 50);

		g.setColor(Color.YELLOW);
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 40));
		g.drawString("Play", TorpedoCat.WIDHT/2 - 40, 220);  //tulisannya
		g.drawString("Quit", TorpedoCat.WIDHT/2 - 40, 290);
		
//------------------------------------------------------------------------------------------------		
		g2.draw(playButton);
		g2.draw(quitButton);
	}
}
