package de.daniel.home.drag_units_2.krap;

import java.util.Map;

public interface IStorageRunner extends IInteractingFigure{
	int getCapacity();
	void setCapacity(int i);
	boolean isRunnerOnTheWayProdFig();
	void setRunnerOnTheWayProdFig(boolean b);
	boolean isRunnerOnTheWayHome();
	void setRunnerOnTheWayHome(boolean b);
	boolean isRunnerAtProdFig();
	void setRunnerAtProdFig(boolean b);
	boolean isRunnerAtHome();
	void setRunnerAtHome(boolean b);
	IStorageBuildingRequest getStorageBuildingRequest();
	void setStorageBuildingRequest(IStorageBuildingRequest isbr);
	void setPathToProdFig(Screen screen);
	void setPathToHome(Screen screen);
	boolean isAtProdFig();
	boolean isAtHome();
	void setStorageBuilding(IStorageBuilding isb);
	IStorageBuilding getStorageBuilding();
}
