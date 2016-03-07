package de.daniel.home.drag_units.krap;

import java.util.LinkedList;
import java.util.Queue;

public class Path {
	private Queue<Position> positions = new LinkedList<>();
	
	public void addPosition(Position p){
		positions.offer(p);
	}
	
	public Position getNextPosition(){
		return positions.poll();
	}
}
