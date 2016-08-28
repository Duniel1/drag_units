package de.daniel.home.drag_units_2.krap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Definitions {

	public static Map<FigureType, String> nameMap;
	public static BufferedImage ressourceImageWood;
	public static BufferedImage buildingImageLumberjack;
	
	static {
		nameMap = new HashMap<FigureType, String>();
		nameMap.put(FigureType.KNIGHT, "Knight");
		nameMap.put(FigureType.TOWER, "Tower");
		
		try {
			ressourceImageWood = ImageIO.read(new File("resources/wood.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			buildingImageLumberjack = ImageIO.read(new File("resources/building.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public enum WallDefinition {
		TOP, BOTTOM, LEFT, RIGHT;
	}

	public enum Direction {
		UP, DOWN, LEFT, RIGHT;
	}

	public enum FigureType {
		KNIGHT, TOWER;
	}
	
	public enum Ressource{
		GOLD, WOOD, STONE, MONEY;
	}
	
	public enum Building{
		RESIDENT, LUMBERJACK, GOLDMINE;
	}
}
