package de.daniel.home.drag_units_2.krap;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import de.daniel.home.drag_units_2.krap.Definitions.Ressource;

public interface IResidentBuilding extends IFigure, IRadiusHandler{
	void setProductionDelay(int i);
	int getProductionTimeLeft();
	void setProductionTimeLeft(int i);
	Map<Ressource, Integer> getNeededRessources();
	void setNeededRessources(Map<Ressource, Integer> m);
	Map<Ressource, Integer> getCurrentRessources();
	void setCurrentRessources(Ressource r, int i);
	void setCurrentRessourceStorageLimit(Ressource r, int i);
//	boolean handleStorageRunner(IStorageRunner isr);
	void calculateRessources();
	int getProductionDelay();
	IStorageRunner getRunner();
	void setScreen(Screen s);
	void setProductionAmount(int i);
	int getProductionAmount();
	Map<IStorageRunner, IStorageRunnerPackage> getStorageRunnerPackages();
	IStorageRunnerPackage getStorageRunnerPackage(IStorageRunner isr);
	List<IStorageBuilding> getAssociatedStorageBuildings();
	void addStorageBuilding(IStorageBuilding isb);
	Map<Ressource, Integer> getCurrentRessourcesStorageLimit();
	void initAssociatedStorageBuildings(List<IStorageBuilding> sb);
	void initRunner(BufferedImage biNormal, BufferedImage biLeft, BufferedImage biRight, BufferedImage biUp,
			BufferedImage biDown, int width, int height, int speed);
	int takeProducedMoney();
}
