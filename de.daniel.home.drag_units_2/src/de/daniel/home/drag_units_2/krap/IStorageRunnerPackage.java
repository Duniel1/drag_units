package de.daniel.home.drag_units_2.krap;

import de.daniel.home.drag_units_2.krap.Definitions.Ressource;

public interface IStorageRunnerPackage {
	IStorageRunner getRunner();
	Ressource getType();
	int getAmount();
}
