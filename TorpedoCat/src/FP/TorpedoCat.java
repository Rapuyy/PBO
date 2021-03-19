package FP;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;

public class TorpedoCat extends Canvas implements Runnable,KeyListener, MouseListener {
	public static final int WIDHT = 640, HEIGHT = 480; // mengatur besar kecil layar game
	private boolean running = false; // untuk mengecek apakah game berjalan atau tidak
	private Thread thread;
	static double score;
	static double highscore = 0;
	public static Room room;
	public Cat cat;
	private static Menu menu;
	
	private enum STATE{
		MENU, GAME
	};
	
	private static STATE state = STATE.MENU;
	public TorpedoCat () {
		Dimension d = new Dimension (TorpedoCat.WIDHT,TorpedoCat.HEIGHT);
		setPreferredSize(d);
		addKeyListener(this);
		addMouseListener(this);
		room = new Room(80, 8); // jarak rintangan
		cat = new Cat(20,TorpedoCat.HEIGHT/2,room.tubes);
	}
	
	public synchronized void start() {
		if(running) return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if(running) return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args) {
		JFrame frame = new JFrame("Torpedo cat");
		TorpedoCat flappy = new TorpedoCat();
		menu = new Menu();
		frame.add(flappy);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		flappy.start();
	}

	@Override
	public void run() {
		int fps = 0;
		double timer = System.currentTimeMillis();
		long lastTime = System.nanoTime();
		double delta = 0;
		double ns = 1000000000 / 60;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			while (delta >= 1) {
				update();
				render();
				fps++;
				delta--;
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + fps);
				fps = 0;
				timer+=1000;
			}
		}
		stop();
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0,0, TorpedoCat.WIDHT, TorpedoCat.HEIGHT);
		if(state == STATE.GAME) {
			room.render(g);
			cat.render(g);
		}
		else if(state == STATE.MENU) {
			menu.render(g);
		}
		if(state == STATE.GAME) {
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 19));
			g.drawString("Score: " + (int)score, 10, 20);
			g.drawString("Highscore: " + (int)highscore, 10, 37);	
		}
		bs.show();
	}	

	private void update() {
		room.update();
		cat.update();
	}
	
	public String GetHighScore() {
		FileReader readFile = null;
		BufferedReader reader = null;
		try {
			readFile = new FileReader("highscore.dat");
			reader = new BufferedReader(readFile);
			return reader.readLine();
		} 
		catch (Exception e) {
			return "0";
		}
		finally {
			try {
				if (reader != null) {
				reader.close();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(state == STATE.GAME) {
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				cat.isPressed = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				Cat.gameOver = false;
				cat.newRoom(80);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			cat.isPressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {	
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
				
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		if(state == STATE.MENU) {
			if(mx >= TorpedoCat.WIDHT/2 - 100 && mx <= TorpedoCat.WIDHT + 100) {
				if(my >= 180 && my <= 280) {
					state = STATE.GAME;
					Cat.gameOver = false;
				}
				if(my >= 250 && my <= 350) {
					System.exit(1);
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
}
