package FP;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Room {
	public ArrayList<Rectangle> tubes;
	private int time;
	private int currentTime = 0;
	private int spd = 20; // kecepatan rintangan
	
	private Random random;
	private final int SPACE_TUBES =128;
	private final int WIDTH_TUBES =32;
	
	public Room(int time, int spd){
		tubes = new ArrayList<>();
		this.time = time;
		this.spd = spd;
		random = new Random();
	}
	
	public void update() {
		currentTime++;
		if(currentTime == time) {
			currentTime = 0;
			int height1 = random.nextInt(TorpedoCat.HEIGHT/2);
			int y2 = height1 + SPACE_TUBES;
			int height2 = TorpedoCat.HEIGHT - y2;
			tubes.add(new Rectangle(TorpedoCat.WIDHT, 0, WIDTH_TUBES, height1));
			tubes.add(new Rectangle(TorpedoCat.WIDHT, y2, WIDTH_TUBES, height2));
		}
		for(int i = 0; i < tubes.size(); i++) {
			Rectangle rect = tubes.get(i);
			rect.x-=spd;
			if(rect.x + rect.width <= 0) {
				Sound.SCORE.play(); //sound saat score++
				tubes.remove(i--);
				TorpedoCat.score += 0.5;
				continue;
			}
			Sound.SAD.play();
			if (TorpedoCat.highscore < TorpedoCat.score){
				TorpedoCat.highscore = TorpedoCat.score;
			}
		}
	}
	
	public void render(Graphics g) {
		//rintangan
		g.setColor(Color.ORANGE);                    
		for (int i = 0; i < tubes.size(); i++) {
			Rectangle rect = tubes.get(i);
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
		}
	}
}