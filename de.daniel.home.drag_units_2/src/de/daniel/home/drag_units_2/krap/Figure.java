package de.daniel.home.drag_units_2.krap;

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
	protected String name;

	public Figure(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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
		if(checkCollisionRight(f, nx, ny)){
			c.right = true;
			c.isCollision = true;
			deltaX = (int) Math.abs(this.x - f.getX());
		}

		// left
		if (checkCollisionLeft(f, nx, ny)) {
			c.left = true;
			c.isCollision = true;
			deltaX = (int) Math.abs(this.x - f.getX());
		}

		// top
		if (checkCollisionTop(f, nx, ny)) {
			c.top = true;
			c.isCollision = true;
			deltaY = (int) Math.abs(this.y - f.getY());
		}

		// bottom
		if (checkCollisionBottom(f, nx, ny)) {
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
	public boolean checkCollisionRight(IFigure f, double nx, double ny){
		if ((nx + this.width > f.getX()) && (ny + this.height > f.getY()) && (ny < f.getY() + f.getHeight())
				&& (nx < f.getX() + f.getWidth())) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean checkCollisionLeft(IFigure f, double nx, double ny){
		if ((nx < f.getX() + f.getWidth()) && (ny + this.height > f.getY()) && (ny < f.getY() + f.getHeight())
				&& (nx + this.width > f.getX())) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean checkCollisionBottom(IFigure f, double nx, double ny){
		if ((nx + this.width > f.getX()) && (nx < f.getX() + f.getWidth()) && (ny + this.height > f.getY())
				&& (ny < f.getY() + f.getHeight())) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean checkCollisionTop(IFigure f, double nx, double ny){
		if ((nx < f.getX() + f.getWidth()) && (nx + this.width > f.getX()) && (ny < f.getY() + f.getHeight())
				&& (ny + this.height > f.getY())) {
			return true;
		}
		return false;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public BufferedImage getImage() {
		return bImage;
	}

	@Override
	public void setImage(String image){
		try {
			bImage = ImageIO.read(new File(image));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
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
	public void setImageReference(BufferedImage image) {
		bImage = image;
	}

}
