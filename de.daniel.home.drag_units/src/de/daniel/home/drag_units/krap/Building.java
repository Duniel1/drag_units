package de.daniel.home.drag_units.krap;

public class Building extends Figure implements IBuilding{
	
	private double maxHealth;
	private double health;

	public Building(double a, double b, double c, double d) {
		super(a, b, c, d);
	}

	@Override
	public void setHealth(double d) {
		health = d;
	}

	@Override
	public double getHealth() {
		return health;
	}

	@Override
	public void setMaxHealth(double d) {
		maxHealth = d;
	}

	@Override
	public double getMaxHealth() {
		return maxHealth;
	}



}
