package de.daniel.home.drag_units.krap;

import java.io.IOException;
import java.util.List;

public interface IUnit extends IFigureWithHealth{
	double getAttack();
	void setAttack(double d);
	double getRange();
	void setRange(double d);
	double getAttackSpeed();
	void setAttackSpeed(double d);
	void fight();
	void setEnemy(IUnit u);
	IUnit getEnemy();
	void checkIfEnemyInRange(List<IUnit> enemys);
	void setIsDead(boolean b);
	boolean isDead();
	void setLeftBImageFight(String image) throws IOException;
    void setRightBImageFight(String image) throws IOException;
    void setUpBImageFight(String image) throws IOException;
    void setDownBImageFight(String image) throws IOException;
    void setSelectedEnemy(IUnit u);
    IUnit getSelectedEnemy();
}
