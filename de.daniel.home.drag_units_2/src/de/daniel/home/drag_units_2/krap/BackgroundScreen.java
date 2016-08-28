package de.daniel.home.drag_units_2.krap;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundScreen extends JPanel {
	
	private Random r = new Random();
	private Graphics2D graphics2D;
	private boolean painted = false;
	
	public BackgroundScreen(int maxX, int maxY) {
		super();
	}
	
	public void paintComponent(Graphics g) {
		try {
			if(!painted)paintMap(g);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void paintMap(Graphics g) throws IOException{
		graphics2D = (Graphics2D) g;
		String pngString = "";
		for(int i = 0; i < 80; i++){
			for(int j = 0; j < 58; j++){
				int x = r.nextInt(4);
				switch(i){
				case 0:
					pngString = "resources/grass.png";
				}
				graphics2D.drawImage(ImageIO.read(new File(pngString)), i * 10, j * 10, null);
			}
		}
		painted = true;
	}
}
