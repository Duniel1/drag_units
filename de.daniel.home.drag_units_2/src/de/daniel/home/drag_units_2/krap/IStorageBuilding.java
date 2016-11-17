package de.daniel.home.drag_units_2.krap;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import de.daniel.home.drag_units_2.krap.Definitions.Ressource;

public interface IStorageBuilding extends IFigure, IRadiusHandler{
	void initProductionFiguresInRadius(List<IProductionFigure> ipf);
	int getRessource(Definitions.Ressource r);
	Map<Definitions.Ressource, Integer> getRessources();
	List<IStorageRunner> getRunners();
	boolean addRunner(IStorageRunner isr);
	boolean addRunner(BufferedImage biNormal, BufferedImage biLeft, BufferedImage biRight, BufferedImage biUp,
			BufferedImage biDown, int width, int height, int speed, int capacity);
	int getNumberOfRunners();
	void calculateStorage();
	void setMaxNumOfRunners(int i);
	int getMaxNumOfRunners();
	int getCapacity();
	void setCapacity(int i);
	int getNumberOfRunnersOnTheWay();
	void setScreen(Screen s);
	boolean addRequest(IStorageBuildingRequest isbr);
	Map<Definitions.Ressource, Integer> getStorage();
	Map<Definitions.Ressource, Integer> getReservedStorage();
	void handleRunner(IStorageRunner isr);
}
