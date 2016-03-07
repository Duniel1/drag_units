package de.daniel.home.drag_units.main;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	
	private JPanel jpPanel = new ShowPanel();

	public static void main(String[] args) {
		new Main();
	}
	
	public Main(){
		JFrame jfWindow = new JFrame();
		jfWindow.setSize(800, 750);
		jfWindow.setTitle("a_star_graphics");
		jfWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfWindow.setVisible(true);
		jfWindow.setLayout(null);
		jfWindow.setContentPane(jpPanel);		
		jfWindow.validate();
	}

}
