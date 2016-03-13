package de.daniel.home.drag_units.krap;


public class PathNode {
	private PathNode nParent;
	private PathPosition position;
	private double dCost;

	public PathNode getParent() 
	{
		return nParent;
	}

	public void setParent(PathNode n) 
	{
		nParent = n;
	}

	public PathPosition getPosition() 
	{
		return position;
	}

	public void setPosition(PathPosition p) 
	{
		position = p;
	}
	
	public double getCost()
	{
		return dCost;
	}
	public void setCost(double f)
	{
		dCost = f;
	}
}
