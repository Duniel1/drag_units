package de.daniel.home.drag_units_2.krap;

import de.daniel.home.drag_units_2.krap.Definitions.Ressource;
import de.daniel.home.drag_units_2.krap.Definitions.StorageBuildingRequestType;

public interface IStorageBuildingRequest {
	IProductionFigure getProductionFigure();
	void setProductionFigure(IProductionFigure ipf);
	int getAmount();
	void setAmount(int i);
	Ressource getRessource();
	void setRessource(Ressource r);
	void take();
	boolean isTaken();
	StorageBuildingRequestType getType();
}
