package de.daniel.home.drag_units_2.krap;

import de.daniel.home.drag_units_2.krap.Definitions.Ressource;

public class RessourceField extends Figure implements IRessourceField {
	
	private int capacity;
	private Ressource type;
	private int effort;
	private boolean taken = false;
	private boolean isEmpty = false;
	
	public RessourceField(double x, double y, double width, double height, Ressource type, int capacity, int effort){
		this(x, y, width, height);
		this.type = type;
		this.capacity = capacity;
		this.effort = effort;
	}
	
	public RessourceField(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	@Override
	public Ressource getType() {
		return type;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public void setCapacity(int i) {
		capacity = i;
	}

	@Override
	public int getEffort() {
		return effort;
	}

	@Override
	public void setEffort(int i) {
		effort = i;
	}

	@Override
	public boolean cutting() {
		capacity--;
		return capacity > 0;
	}

	@Override
	public boolean isTaken() {
		return taken;
	}

	@Override
	public void take(boolean b) {
		taken = b;
	}

	@Override
	public boolean isEmpty() {
		return isEmpty;
	}

	@Override
	public void setIsEmpty(boolean b) {
		isEmpty = b;
	}
}
