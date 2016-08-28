package de.daniel.home.drag_units_2.krap;

public class Position {
	private static final double TOLERANCE = 1;
	private double x;
	private double y;
	
	public Position(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public boolean equals(Position other){
		if(!(x < other.x + TOLERANCE && x > other.x - TOLERANCE)) return false;
		if(!(y < other.y + TOLERANCE && y > other.y - TOLERANCE)) return false;
		return true;
	}
	
	public boolean equals(Position other, double tolerance){
		if(!(x < other.x + tolerance && x > other.x - tolerance)) return false;
		if(!(y < other.y + tolerance && y > other.y - tolerance)) return false;
		return true;
	}
}
