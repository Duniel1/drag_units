package de.daniel.home.drag_units.krap;

import java.io.IOException;
import java.util.List;

public interface IMovingFigure extends IFigure{
	boolean isOnWall(WallDefinition wd);
    void setOnWall(WallDefinition wd, boolean b);
    double getFactor();
    void setFactor(double f);
    double getAx();
    void setAx(double ax);
    double getAy();
    void setAy(double ay);
    MovingDestination getMovingDestination();
    void setMovingDestination(MovingDestination destination);
    void move();
    void setNewAx(double ax);
    void setNewAy(double ay);
    double getNewAx();
    double getNewAy();
    void finalizeA();
    void setLeftBImage(String image) throws IOException;
    void setRightBImage(String image) throws IOException;
    void setUpBImage(String image) throws IOException;
    void setDownBImage(String image) throws IOException;
    void setIsTransient(boolean b);
    boolean isTransient();
    void setSpeed(double i);
    double getSpeed();
    void calculatePath(List<IFigure> obstacles, int maxX, int maxY);
    
    public enum WallDefinition{
		TOP, BOTTOM, LEFT, RIGHT;
	}
    
    public enum Direction{
    	UP, DOWN, LEFT, RIGHT;
    }
}
