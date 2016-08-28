package de.daniel.home.drag_units_2.krap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.daniel.home.drag_units_2.krap.Definitions.Building;
import de.daniel.home.drag_units_2.krap.Definitions.Ressource;

public class ProductionFigure extends Figure implements IProductionFigure {
	private List<Ressource> ressources;
	private Map<Ressource, Integer> productionDelay;
	private Map<Ressource, Integer> productionTimeLeft;
	private Map<Ressource, Integer> finishedRessources;
	private Map<Ressource, Integer> pendingRessources;
	private Map<Ressource, Map<Ressource, Integer>> neededRessources;
	private Map<Ressource, Integer> currentRessources;
	private Map<Ressource, Integer> currentRessourcesStorageLimit;
	private boolean isProductionDirect;
	private Map<Ressource, Integer> producedRessourcesStorageLimit;
	private Map<Ressource, Integer> producedRessourcesStorage;
	private double productivity = 1;
	private Building type;
	private boolean hasRadius;
	private int radiusSize;
	private List<IRessourceField> associatedRessourceFields;

	public ProductionFigure(double x, double y, double width, double height) {
		super(x, y, width, height);
		productionDelay = new HashMap<>();
		productionTimeLeft = new HashMap<>();
		finishedRessources = new HashMap<>();
		pendingRessources = new HashMap<>();
		neededRessources = new HashMap<>();
		currentRessources = new HashMap<>();
		currentRessourcesStorageLimit = new HashMap<>();
		producedRessourcesStorageLimit = new HashMap<>();
		producedRessourcesStorage = new HashMap<>();
	}

	@Override
	public Map<Ressource, Integer> getFinishedRessources() {
		return finishedRessources;
	}

	@Override
	public void calculateRessources() {
		for (Entry<Ressource, Integer> entry : productionTimeLeft.entrySet()) {
			Ressource curRessource = entry.getKey();
			boolean canProduce = true;
			if (neededRessources.get(curRessource) != null) {
				for (Entry<Ressource, Integer> currentResForToProdRes : neededRessources.get(curRessource).entrySet()) {
					if (!(currentRessources.get(currentResForToProdRes.getKey()) >= currentResForToProdRes
							.getValue())) {
						canProduce = false;
						break;
					}
				}
			}
			
			int currentRessourcesStorageNewValue = 1;
			if (!isProductionDirect && producedRessourcesStorage.get(curRessource) != null) {
				currentRessourcesStorageNewValue = producedRessourcesStorage.get(curRessource) + 1;
				if(producedRessourcesStorageLimit.get(curRessource) <  currentRessourcesStorageNewValue){
					canProduce = false;
				}
			}
			
			if (canProduce) {
				int curLeft = entry.getValue() - 1;
				if (curLeft == 0) {
					if (neededRessources.get(curRessource) != null) {
						for (Entry<Ressource, Integer> currentResForToProdRes : neededRessources.get(curRessource)
								.entrySet()) {
							currentRessources.put(currentResForToProdRes.getKey(),
									currentRessources.get(currentResForToProdRes.getKey())
											- currentResForToProdRes.getValue());
						}
					}
					productionTimeLeft.put(curRessource, (int)(productionDelay.get(curRessource) / productivity));
					if (isProductionDirect) {
						finishedRessources.put(curRessource, 1);
					}else{
						producedRessourcesStorage.put(curRessource, currentRessourcesStorageNewValue);
					}
				} else {
					productionTimeLeft.put(entry.getKey(), curLeft);
					finishedRessources.put(curRessource, 0);
				}
			}
		}
	}

	@Override
	public Map<Ressource, Integer> getPendingRessources() {
		return pendingRessources;
	}

	@Override
	public Map<Ressource, Integer> getProductionDelay() {
		return productionDelay;
	}

	@Override
	public void setProductionDelay(Map<Ressource, Integer> i) {
		productionDelay = i;
	}

	@Override
	public Map<Ressource, Integer> getProductionTimeLeft() {
		return productionTimeLeft;
	}

	@Override
	public void setProductionTimeLeft(Map<Ressource, Integer> i) {
		productionTimeLeft = i;
	}

	@Override
	public List<Ressource> getRessources() {
		return ressources;
	}

	@Override
	public void setRessources(List<Ressource> l) {
		ressources = l;
	}

	@Override
	public Map<Ressource, Map<Ressource, Integer>> getNeededRessources() {
		return neededRessources;
	}

	@Override
	public Map<Ressource, Integer> getNeededRessource(Ressource r) {
		return neededRessources.get(r);
	}

	@Override
	public void setNeededRessources(Ressource r, Map<Ressource, Integer> m) {
		neededRessources.put(r, m);
	}

	@Override
	public Map<Ressource, Integer> getCurrentRessources() {
		return currentRessources;
	}

	@Override
	public int getCurrentRessource(Ressource r) {
		return currentRessources.get(r);
	}

	@Override
	public void setCurrenetRessources(Ressource r, int i) {
		currentRessources.put(r, i);
	}

	@Override
	public boolean addToCurrentRessource(Ressource r, int i) {
		if (currentRessourcesStorageLimit.get(r) < (currentRessources.get(r) + i)) {
			return false;
		} else {
			currentRessources.put(r, currentRessources.get(r) + 1);
			return true;
		}
	}

	@Override
	public void setCurrentRessourceStorageLimit(Ressource r, int i) {
		currentRessourcesStorageLimit.put(r, i);
	}

	@Override
	public boolean isProductionDirect() {
		return isProductionDirect;
	}

	@Override
	public void setIsProductionDirect(boolean b) {
		isProductionDirect = b;
	}

	@Override
	public Map<Ressource, Integer> getProducedRessourcesStorageLimit() {
		return producedRessourcesStorageLimit;
	}

	@Override
	public int getProducedRessourceStorageLimit(Ressource r) {
		return producedRessourcesStorageLimit.get(r);
	}

	@Override
	public void setProducedRessourcesStorageLimit(Map<Ressource, Integer> m) {
		producedRessourcesStorageLimit = m;
	}

	@Override
	public Map<Ressource, Integer> getProducedRessourcesStorage() {
		return producedRessourcesStorage;
	}

	@Override
	public int getProducedRessourcesStorage(Ressource r) {
		return producedRessourcesStorage.get(r);
	}

	@Override
	public void setProductivity(double d) {
		productivity = d;
	}

	@Override
	public double getProductivity() {
		return productivity;
	}

	@Override
	public Building getType() {
		return type;
	}

	@Override
	public void setType(Building b) {
		type = b;
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

	@Override
	public List<IRessourceField> getAssociatedRessourceFields() {
		return associatedRessourceFields;
	}

	@Override
	public void setAssociatedRessourceFields(List<IRessourceField> irfList) {
		associatedRessourceFields = irfList;
	}

	@Override
	public void initAssociatedRessourceFields(List<IRessourceField> rl, Ressource r) {
		int middleX = (int)(x + 0.5 * width);
		int middleY = (int)(y + 0.5 * height);
	}
}
