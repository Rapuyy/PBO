package FP;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Cat extends Rectangle{
	public float spd = 4;
	public boolean isPressed = false;
	public static boolean gameOver = false;
	private BufferedImage sheet;
	private BufferedImage[] texture;
	private ArrayList<Rectangle> tubes;
	private int imageIndex = 0;
	private  boolean isFalling = false;
	private int frames = 0;
	private float gravity = 0.3f;
	
	public Cat(int x, int y, ArrayList<Rectangle> tubes) {
		setBounds(x, y, 90, 32);
		this.tubes = tubes;
		texture = new BufferedImage[4];
		try {
			sheet = ImageIO.read(getClass().getResource("/Cat.png"));
			texture [0] = sheet.getSubimage(0,0,200,100);			 //
			texture [1] = sheet.getSubimage(220,0,200,100);          // pemisahan sprites
			texture [2] = sheet.getSubimage(430,0,200,100);			 //
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void newRoom(int time) {
		TorpedoCat.room = new Room(time, 8);
		tubes = TorpedoCat.room.tubes;
		y = TorpedoCat.HEIGHT/2;          //restart room saat game dimulai
		TorpedoCat.score = 0;
		spd = 4;
	}
	
	public void update() {
		isFalling = false;
		if(isPressed) {
			spd = 4;
			y-=spd;
			imageIndex = 2;
			frames = 0;
		}
		else {
			isFalling = true;
			y+=spd;
			frames++;
			if(frames > 10) frames = 10;
		}
		if(isFalling) {
			spd += gravity;
			if(frames >= 10) imageIndex = 0;
			imageIndex = 1;
		}
			
		for(int i = 0; i < tubes.size(); i++) {
			if(this.intersects(tubes.get(i))) {
				gameOver = true;
				spd = 0;
				TorpedoCat.room = new Room(0, 0);		
				break;
			}
		}
		
		if(y >= TorpedoCat.HEIGHT) {
			//restart the game
			gameOver = true;
			spd = 0;
			TorpedoCat.room = new Room(0, 0);
		}
//		if(y <= 0) {
//			y = 0;  //mentok atas
//		}
	}

	public void render(Graphics g) {
		g.drawImage(texture[imageIndex], x, y, width, height, null);
		if(gameOver==true) {
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
			g.drawString("Press SPACE to Play", TorpedoCat.WIDHT / 2 - 100, (TorpedoCat.HEIGHT/2) - 150);
			Sound.OPENIGN.loop(); // lagu iringan saat main game
		}
	}
}
