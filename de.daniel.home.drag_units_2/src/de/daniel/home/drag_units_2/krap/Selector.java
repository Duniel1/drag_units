package de.daniel.home.drag_units_2.krap;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Selector implements MouseListener, MouseMotionListener {
	private Screen screen;
	private int selectedX;
	private int selectedY;
	private int selectedX2;
	private int selectedY2;

	public Selector(Screen s) {
		super();
		screen = s;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == 1) screen.leftClick(e.getX(), e.getY());
		else if(e.getButton() == 3) screen.rightClick(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		selectedX = e.getX();
		selectedY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (selectedX != selectedX2 || selectedY != selectedY2) {
			screen.finalizeSelection();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		screen.select(selectedX, selectedY, e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}
