package de.daniel.home.drag_units_2.krap;

import de.daniel.home.drag_units_2.krap.Definitions.Ressource;
import de.daniel.home.drag_units_2.krap.Definitions.StorageBuildingRequestType;

public class StorageBuildingRequest implements IStorageBuildingRequest {

	private int amount;
	private Ressource ressource;
	private IProductionFigure associatedProdFig;
	private IResidentBuilding associatedResidentBuilding;
	private boolean isTaken = false;
	private StorageBuildingRequestType type;
	
	public StorageBuildingRequest(int amount, Ressource ressource, IProductionFigure ipf, StorageBuildingRequestType type) {
		this.amount = amount;
		this.ressource = ressource;
		associatedProdFig = ipf;
		this.type = type;
	}
	
	public StorageBuildingRequest(int amount, Ressource ressource, IResidentBuilding irb, StorageBuildingRequestType type) {
		this.amount = amount;
		this.ressource = ressource;
		associatedResidentBuilding = irb;
		this.type = type;
	}
	
	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public void setAmount(int i) {
		amount = i;
	}

	@Override
	public Ressource getRessource() {
		return ressource;
	}

	@Override
	public void setRessource(Ressource r) {
		ressource = r;
	}

	@Override
	public IProductionFigure getProductionFigure() {
		return associatedProdFig;
	}

	@Override
	public void setProductionFigure(IProductionFigure ipf) {
		associatedProdFig = ipf;
	}

	@Override
	public void take() {
		isTaken = true;
	}

	@Override
	public boolean isTaken() {
		return isTaken;
	}

	@Override
	public StorageBuildingRequestType getType() {
		return type;
	}

}
