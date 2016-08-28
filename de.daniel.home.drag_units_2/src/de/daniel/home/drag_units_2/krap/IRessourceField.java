package de.daniel.home.drag_units_2.krap;

import de.daniel.home.drag_units_2.krap.Definitions.Ressource;

public interface IRessourceField extends IFigure{
	Ressource getType();
	int getCapacity();
	void setCapacity(int i);
	int getEffort();
	void setEffort(int i);
}
