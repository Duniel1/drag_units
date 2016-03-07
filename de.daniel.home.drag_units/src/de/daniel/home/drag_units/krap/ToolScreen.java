package de.daniel.home.drag_units.krap;

import java.awt.Color;

import javax.swing.JPanel;

public class ToolScreen extends JPanel {

	private int width;
	private int height;

	public ToolScreen(int w, int h) {
		width = w;
		height = h;
		setBackground(Color.BLACK);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
