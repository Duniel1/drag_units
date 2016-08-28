package de.daniel.home.drag_units_2.krap;

import java.util.List;

import de.daniel.home.drag_units_2.krap.Definitions.FigureType;
import de.daniel.home.drag_units_2.krap.Definitions.WallDefinition;

public interface IInteractingFigure extends IFigure{
    boolean isMovable();
    void setIsMovable(boolean b);
    boolean canAttack();
    void setCanAttack(boolean b);
    
    void setHealth(double d);
	double getHealth();
	void setMaxHealth(double d);
	double getMaxHealth();
	
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
    void setLeftBImage(String image);
    void setRightBImage(String image);
    void setUpBImage(String image);
    void setDownBImage(String image);
    void setIsTransient(boolean b);
    boolean isTransient();
    void setSpeed(double i);
    double getSpeed();
    void calculatePath(int maxX, int maxY, List<IFigure>... obstacles);
    
    double getAttack();
	void setAttack(double d);
	double getRange();
	void setRange(double d);
	double getAttackSpeed();
	void setAttackSpeed(double d);
	void fight();
	void setEnemy(IInteractingFigure u);
	IInteractingFigure getEnemy();
	void checkIfEnemyInRange(List<IInteractingFigure> enemies);
	void setIsDead(boolean b);
	boolean isDead();
	void setLeftBImageFight(String image);
    void setRightBImageFight(String image);
    void setUpBImageFight(String image);
    void setDownBImageFight(String image);
    void setSelectedEnemy(IInteractingFigure u);
    IInteractingFigure getSelectedEnemy();
    
    void setFigureType(FigureType ft);
    FigureType getFigureType();
}
