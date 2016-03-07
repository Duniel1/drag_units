package de.daniel.home.drag_units.krap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Figure implements IFigure {

	private static int counter = 0;
	protected int id;
	protected boolean isSelected;
	protected BufferedImage bImage;
	protected double x;
	protected double y;
	protected double width;
	protected double height;

	public Figure(double a, double b, double c, double d) {
		x = a;
		y = b;
		width = c;
		height = d;
		id = counter++;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public Collision checkCollision(IFigure f, double nx, double ny) {

		Collision c = new Collision();

		int deltaX = 0;
		int deltaY = 0;

		// right
		if ((nx + this.width > f.getX()) && (ny + this.height > f.getY()) && (ny < f.getY() + f.getHeight())
				&& (nx < f.getX() + f.getWidth())) {
			c.right = true;
			c.isCollision = true;
			deltaX = (int) Math.abs(this.x - f.getX());
		}

		// left
		if ((nx < f.getX() + f.getWidth()) && (ny + this.height > f.getY()) && (ny < f.getY() + f.getHeight())
				&& (nx + this.width > f.getX())) {
			c.left = true;
			c.isCollision = true;
			deltaX = (int) Math.abs(this.x - f.getX());
		}

		// top
		if ((nx < f.getX() + f.getWidth()) && (nx + this.width > f.getX()) && (ny < f.getY() + f.getHeight())
				&& (ny + this.height > f.getY())) {
			c.top = true;
			c.isCollision = true;
			deltaY = (int) Math.abs(this.y - f.getY());
		}

		// bottom
		if ((nx + this.width > f.getX()) && (nx < f.getX() + f.getWidth()) && (ny + this.height > f.getY())
				&& (ny < f.getY() + f.getHeight())) {
			c.bottom = true;
			c.isCollision = true;
			deltaY = (int) Math.abs(this.y - f.getY());
		}

		if (deltaX > deltaY) {
			c.top = false;
			c.bottom = false;
		}

		if (deltaX < deltaY) {
			c.right = false;
			c.left = false;
		}

		c.versus = f;
		return c;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void setIsSelected(boolean b) {
		isSelected = b;
	}

	@Override
	public BufferedImage getImage() {
		return bImage;
	}

	@Override
	public void setImage(String image) throws IOException {
		bImage = ImageIO.read(new File(image));
	}

}
