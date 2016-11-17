package de.daniel.home.drag_units_2.krap;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.daniel.home.drag_units_2.krap.Definitions.Building;
import de.daniel.home.drag_units_2.krap.Definitions.Ressource;

public interface IProductionFigure extends IFigure, IRadiusHandler{
	Map<Ressource, Integer> getFinishedRessources();
	void calculateRessources();
	Map<Ressource, Integer> getProductionDelay();
	void setProductionDelay(Map<Ressource, Integer> i);
	Map<Ressource, Integer> getProductionTimeLeft();
	void setProductionTimeLeft(Map<Ressource, Integer> i);
	List<Ressource> getRessources();
	void setRessources(List<Ressource> l);
	Map<Ressource, Map<Ressource, Integer>> getNeededRessources();
	Map<Ressource, Integer> getNeededRessource(Ressource r);
	void setNeededRessources(Ressource r, Map<Ressource, Integer> m);
	Map<Ressource, Integer> getCurrentRessources();
	int getCurrentRessource(Ressource r);
	void setCurrentRessources(Ressource r, int i);
	void setCurrentRessourceStorageLimit(Ressource r, int i);
	boolean isProductionDirect();
	void setIsProductionDirect(boolean b);
	Map<Ressource, Integer> getProducedRessourcesStorageLimit();
	int getProducedRessourceStorageLimit(Ressource r);
	void setProducedRessourcesStorageLimit(Map<Ressource, Integer> m);
	Map<Ressource, Integer> getProducedRessourcesStorage();
	int getProducedRessourcesStorage(Ressource r);
	void setProductivity(double d);
	double getProductivity();
	Building getType();
	void setType(Building b);
	List<IRessourceField> getAssociatedRessourceFields();
	void setAssociatedRessourceFields(List<IRessourceField> irfList);
	void initAssociatedRessourceFields(List<IRessourceField> rl, Ressource r);
	void initRunner(BufferedImage biNormal, BufferedImage biLeft, BufferedImage biRight, BufferedImage biUp, BufferedImage biDown, 
			int width, int height, int speed);
	IInteractingFigure getRunner();
	void setScreen(Screen s);
	void setProductionAmount(int i);
	int getProductionAmount();
	Map<IStorageRunner, IStorageRunnerPackage> getStorageRunnerPackages();
	IStorageRunnerPackage getStorageRunnerPackage(IStorageRunner isr);
//	IStorageRunnerPackage reserveStorageRunnerPackage(IStorageRunner isr, Ressource r, int amount);
	List<IStorageBuilding> getAssociatedStorageBuildings();
	void addStorageBuilding(IStorageBuilding isb);
	boolean handleStorageRunner(IStorageRunner isr);
	Map<Ressource, Integer> getCurrentRessourcesStorageLimit();
}
