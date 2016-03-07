package de.daniel.home.drag_units.krap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;

import javax.imageio.ImageIO;

public class MovingUnit extends MovingFigure implements IMovingUnit{
	
	public MovingUnit(double x, double y, double width, double height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	private double maxHealth;
	private double health;
	private double attackSpeed;
	private double attack;
	private double range;
	private boolean isDead = false;
	private IUnit enemy;
	private IUnit selectedEnemy;
	private EnumMap<Direction, BufferedImage> mapBImagesFight = new EnumMap<>(Direction.class);

	@Override
	public double getAttack() {
		return attack;
	}

	@Override
	public void setAttack(double d) {
		attack = d;
	}

	@Override
	public double getRange() {
		return range;
	}

	@Override
	public void setRange(double d) {
		range = d;
	}

	@Override
	public double getAttackSpeed() {
		return attackSpeed;
	}

	@Override
	public void setAttackSpeed(double d) {
		attackSpeed = d;
	}

	@Override
	public void fight() {
		if (enemy != null) {
			enemy.setHealth(enemy.getHealth() - attack);
			if (enemy.getHealth() <= 0) {
				enemy.setIsDead(true);
				this.enemy = null;
			}
		}
	}

	@Override
	public void setEnemy(IUnit u) {
		enemy = u;
	}

	@Override
	public IUnit getEnemy() {
		return enemy;
	}

	@Override
	public void checkIfEnemyInRange(List<IUnit> enemys) {
		for (IUnit enemy : enemys) {
			double dummyX = enemy.getX() - range;
			double dummyY = enemy.getY() - range;
			double dummyWidth = enemy.getWidth() + 2 * range;
			double dummyHeight = enemy.getHeight() + 2 * range;
			IFigure dummyEnemy = new Figure(dummyX, dummyY, dummyWidth, dummyHeight);
			Collision collision = checkCollision(dummyEnemy, x, y);
			if (collision.isCollision) {
				if (selectedEnemy != null/* && ay == 0 && ax == 0*/) {
					if (enemy.equals(selectedEnemy)) {
						this.enemy = enemy;
						selectedEnemy = null;
						break;
					} else {
						continue;
					}
				} else if (selectedEnemy == null) {
					this.enemy = enemy;
					break;
				}
			}
			if (collision.isCollision) {
				this.enemy = enemy;
				break;
			}else{
				this.enemy = null;
			}
		}

	}

	@Override
	public void setIsDead(boolean b) {
		isDead = b;
	}

	@Override
	public boolean isDead() {
		return isDead;
	}

	@Override
	public void setLeftBImageFight(String image) throws IOException {
		mapBImagesFight.put(Direction.LEFT, ImageIO.read(new File(image)));
	}

	@Override
	public void setRightBImageFight(String image) throws IOException {
		mapBImagesFight.put(Direction.RIGHT, ImageIO.read(new File(image)));
	}

	@Override
	public void setUpBImageFight(String image) throws IOException {
		mapBImagesFight.put(Direction.UP, ImageIO.read(new File(image)));
	}

	@Override
	public void setDownBImageFight(String image) throws IOException {
		mapBImagesFight.put(Direction.DOWN, ImageIO.read(new File(image)));
	}

	@Override
	public BufferedImage getImage() {
		if (enemy != null) {
			Direction d = null;
			double diffX = x - enemy.getX();
			double diffY = y - enemy.getY();

			if (Math.abs(diffX) < Math.abs(diffY)) {
				if (diffY < 0)
					d = Direction.DOWN;
				else
					d = Direction.UP;
			} else {
				if (diffX < 0)
					d = Direction.RIGHT;
				else
					d = Direction.LEFT;
			}
			return mapBImagesFight.get(d);
		} else {
			return super.getImage();
		}
	}

	@Override
	public void setSelectedEnemy(IUnit u) {
		selectedEnemy = u;
	}

	@Override
	public IUnit getSelectedEnemy() {
		return selectedEnemy;
	}

	@Override
	public void setHealth(double d) {
		health = d;
	}

	@Override
	public double getHealth() {
		return health;
	}

	@Override
	public void setMaxHealth(double d) {
		maxHealth = d;
	}

	@Override
	public double getMaxHealth() {
		return maxHealth;
	}
}
