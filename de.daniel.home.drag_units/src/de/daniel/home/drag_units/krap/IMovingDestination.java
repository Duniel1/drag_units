package de.daniel.home.drag_units.krap;

public interface IMovingDestination {
	Position getNextPosition(Position p);
	Position getPosition();
}
