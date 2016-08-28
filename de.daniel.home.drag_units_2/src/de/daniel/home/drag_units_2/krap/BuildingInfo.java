package de.daniel.home.drag_units_2.krap;

import de.daniel.home.drag_units_2.krap.Definitions.Building;

public class BuildingInfo implements IBuildingInfo {
	
	private double height;
	private double width;
	private double x;
	private double y;
	private Building buildingType;
	private int costMoney;
	private int costStone;
	private int costWood;
	private int costGold;
	private IFigure figureDummy;
	private boolean hasRadius;
	private int radiusSize;
	
	public BuildingInfo(Building b, double width, double height){
		this.width = width;
		this.height = height;
		buildingType = b;
		figureDummy = new Figure(0, 0, width, height);
	}

	@Override
	public void setBuilding(Building b) {
		buildingType = b;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public Building getBuildingType() {
		return buildingType;
	}

	@Override
	public int getCostMoney() {
		return costMoney;
	}

	@Override
	public int getCostWood() {
		return costWood;
	}

	@Override
	public int getCostStone() {
		return costStone;
	}

	@Override
	public int getCostGold() {
		return costGold;
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
	public void setCost(int money, int wood, int stone, int gold) {
		costMoney = money;
		costWood = wood;
		costStone = stone;
		costGold = gold;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public Collision checkCollision(IFigure f) {
		return figureDummy.checkCollision(f, x, y);
	}

	@Override
	public boolean hasRadius() {
		return hasRadius;
	}

	@Override
	public void setHasRadius(boolean b) {
		hasRadius = b;
	}

	@Override
	public int getRadiusSize() {
		return radiusSize;
	}

	@Override
	public void setRadiusSize(int i) {
		radiusSize = i;
		hasRadius = true;
	}

}
