package de.daniel.home.drag_units_2.krap;

import de.daniel.home.drag_units_2.krap.Definitions.Building;

public interface IBuildingInfo extends IRadiusHandler{
	void setBuilding(Building b);
	double getX();
	double getY();
	void setX(double x);
	void setY(double y);
	Building getBuildingType();
	int getCostMoney();
	int getCostWood();
	int getCostStone();
	int getCostGold();
	double getWidth();
	double getHeight();
	void setCost(int money, int wood, int stone, int gold);
	Collision checkCollision(IFigure f);
}
