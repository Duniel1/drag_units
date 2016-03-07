package de.daniel.home.drag_units.krap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class Screen extends JPanel {

	private static final long serialVersionUID = -2091777385337312828L;
	private ArrayList<IMovingFigure> movingFigures = new ArrayList<>();
	private ArrayList<IMovingUnit> units = new ArrayList<>();
	private ArrayList<IMovingUnit> enemyUnits = new ArrayList<>();
	private ArrayList<IFigure> objects = new ArrayList<>();
	private ArrayList<IBuilding> buildings = new ArrayList<>();
	private int maxX;
	private int maxY;
	private Graphics2D graphics2D;
	private int selectedX;
	private int selectedY;
	private int selectedX2;
	private int selectedY2;
	private boolean selectionActive;
	private boolean elementsSelected;
	private Random r;

	public Screen(int maxX, int maxY) {
		super();
		this.maxX = maxX;
		this.maxY = maxY;
		init();
	}

	private void init() {
		r = new Random();
		for (int i = 0; i < 4; i++) {
			MovingFigure rect = null;

			while (true) {
				boolean newRect = true;
				int x = r.nextInt(maxX);
				int y = r.nextInt(maxY);
				rect = new MovingFigure(x, y, 25, 25);
				rect.setAx(r.nextInt(2) + 1);
				rect.setAy(r.nextInt(2) + 1);
				try {
					rect.setImage("resources/orange.png");
					rect.setUpBImage("resources/up.png");
					rect.setDownBImage("resources/down.png");
					rect.setRightBImage("resources/right.png");
					rect.setLeftBImage("resources/left.png");
				} catch (IOException e) {
					e.printStackTrace();
				}

				for (IMovingFigure tempR : movingFigures) {
					if (rect != tempR) {
						Collision tempCollision = rect.checkCollision(tempR, x, y);
						if (tempCollision.isCollision) {
							newRect = false;
						}
					}
				}

				if (newRect)
					break;
			}

			movingFigures.add(rect);
		}

		for (int i = 0; i < 20; i++) {
			IFigure ifig = new Figure(500, i * 25, 25, 25);
			try {
				ifig.setImage("resources/orange.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			objects.add(ifig);
		}
		
		for (int i = 0; i < 4; i++) {
			IBuilding ibui = new Building(r.nextInt(maxX), r.nextInt(maxY), 50, 50);
			try {
				ibui.setImage("resources/building.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			buildings.add(ibui);
		}


		for (int i = 0; i < 15; i++) {
			IMovingUnit iunit = new MovingUnit(10 + 35 * i, 10 + 35 * i, 25, 25);
			iunit.setSpeed(2);
			iunit.setMaxHealth(1000);
			iunit.setHealth(900 + 5 * i);
			iunit.setAttack(1);
			iunit.setRange(10);
			try {
				iunit.setImage("resources/boi.png");
				iunit.setUpBImage("resources/boi_up.png");
				iunit.setDownBImage("resources/boi_down.png");
				iunit.setRightBImage("resources/boi_right.png");
				iunit.setLeftBImage("resources/boi_left.png");
				iunit.setUpBImageFight("resources/boi_up_fight.png");
				iunit.setDownBImageFight("resources/boi_down_fight.png");
				iunit.setRightBImageFight("resources/boi_right_fight.png");
				iunit.setLeftBImageFight("resources/boi_left_fight.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			units.add(iunit);
		}

		for (int i = 0; i < 10; i++) {
			IMovingUnit iunit = new MovingUnit(100 + 35 * i, 10 + 35 * i, 25, 25);
			iunit.setMaxHealth(1000);
			iunit.setHealth(900 + 5 * i);
			iunit.setAttack(1);
			iunit.setRange(10);
			try {
				iunit.setImage("resources/boi_red.png");
				iunit.setUpBImage("resources/boi_up_red.png");
				iunit.setDownBImage("resources/boi_down_red.png");
				iunit.setRightBImage("resources/boi_right_red.png");
				iunit.setLeftBImage("resources/boi_left_red.png");
				iunit.setUpBImageFight("resources/boi_up_fight_red.png");
				iunit.setDownBImageFight("resources/boi_down_fight_red.png");
				iunit.setRightBImageFight("resources/boi_right_fight_red.png");
				iunit.setLeftBImageFight("resources/boi_left_fight_red.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			enemyUnits.add(iunit);
		}

		Selector selector = new Selector(this);
		this.addMouseListener(selector);
		this.addMouseMotionListener(selector);
	}

	public void paintComponent(Graphics g) {
		Calculator.init(maxX, maxY);
		Calculator.calculateNewPositions(movingFigures, objects);
		Calculator.calculateNewPositionsUnits(units);
		// Calculator.calculateNewPositionsEnemyUnits(enemyUnits);
		Calculator.checkEnemyInRange(new ArrayList<IUnit>(units), new ArrayList<IUnit>(enemyUnits));
		Calculator.fight(new ArrayList<IUnit>(units), new ArrayList<IUnit>(enemyUnits));

		List<IUnit> deadUnits = new ArrayList<IUnit>();
		for (IUnit unit : units) {
			if (unit.isDead()) {
				deadUnits.add(unit);
			}
		}
		for (IUnit unit : deadUnits) {
			units.remove(unit);
		}

		deadUnits = new ArrayList<IUnit>();
		for (IUnit unit : enemyUnits) {
			if (unit.isDead()) {
				deadUnits.add(unit);
			}
		}
		for (IUnit unit : deadUnits) {
			enemyUnits.remove(unit);
		}

		graphics2D = (Graphics2D) g;

		for (IMovingFigure r : movingFigures) {
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			if (r.isSelected()) {
				drawSelection(r);
			}
		}

		for (IFigure r : objects) {
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			if (r.isSelected()) {
				drawSelection(r);
			}
		}
		
		for (IBuilding r : buildings) {
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			if (r.isSelected()) {
				drawSelection(r);
			}
		}

		for (IMovingUnit r : units) {
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			double health = (r.getHealth() / r.getMaxHealth()) * r.getWidth();
			Rectangle r2 = new Rectangle((int) r.getX(), (int) r.getY() - 5, (int) health, 4);
			int red = (int) ((1 - (r.getHealth() / r.getMaxHealth())) * 255);
			int green = (int) ((r.getHealth() / r.getMaxHealth()) * 255);
			Color co = new Color(red, green, 0);
			graphics2D.setColor(co);
			graphics2D.fill(r2);
			if (r.isSelected()) {
				drawSelection(r);

				if (r.getMovingDestination() != null) {
					drawDestination(r);
				}
			}
			if (r.getEnemy() != null) {
				drawAttack(r, Color.GREEN);
			}
		}

		for (IUnit r : enemyUnits) {
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			double health = (r.getHealth() / r.getMaxHealth()) * r.getWidth();
			Rectangle r2 = new Rectangle((int) r.getX(), (int) r.getY() - 5, (int) health, 4);
			int red = (int) ((1 - (r.getHealth() / r.getMaxHealth())) * 255);
			int green = (int) ((r.getHealth() / r.getMaxHealth()) * 255);
			Color co = new Color(red, green, 0);
			graphics2D.setColor(co);
			graphics2D.fill(r2);
			if (r.getEnemy() != null) {
				drawAttack(r, Color.RED);
			}
		}

		if (selectionActive) {
			graphics2D.setColor(Color.BLACK);
			graphics2D.drawLine(selectedX, selectedY, selectedX, selectedY2);
			graphics2D.drawLine(selectedX, selectedY, selectedX2, selectedY);
			graphics2D.drawLine(selectedX2, selectedY2, selectedX, selectedY2);
			graphics2D.drawLine(selectedX2, selectedY2, selectedX2, selectedY);
			selectFigures();
		}
	}

	private void selectFigures() {
		// for (IMovingFigure r : movingFigures) {
		// selectFigure(r);
		// }

		// for (IFigure r : objects) {
		// selectFigure(r);
		// }

		for (IUnit r : units) {
			selectFigure(r);
		}
	}

	private void selectOneFigure(int x, int y) {
		boolean selected = false;
		for (IMovingFigure r : movingFigures) {
			if (selectOneFigure(x, y, r)) {
				r.setIsSelected(true);
				elementsSelected = true;
				break;
			}
		}

		if (!selected) {
			for (IFigure r : objects) {
				if (selectOneFigure(x, y, r)) {
					r.setIsSelected(true);
					elementsSelected = true;
					break;
				}
			}
		}
		
		if (!selected) {
			for (IBuilding r : buildings) {
				if (selectOneFigure(x, y, r)) {
					r.setIsSelected(true);
					elementsSelected = true;
					break;
				}
			}
		}

		if (!selected) {
			for (IUnit r : units) {
				if (selectOneFigure(x, y, r)) {
					r.setIsSelected(true);
					elementsSelected = true;
					break;
				}
			}
		}
	}

	private boolean selectOneFigure(int x, int y, IFigure fig) {
		int fx = (int) fig.getX();
		int fy = (int) fig.getY();
		if ((x > fx && x < (fx + fig.getWidth())) && (y > fy && y < (fy + fig.getHeight()))) {
			return true;
		}
		return false;
	}

	private void selectFigure(IFigure fig) {
		int x = (int) (fig.getX() + 0.5 * fig.getWidth());
		int y = (int) (fig.getY() + 0.5 * fig.getHeight());
		if (((x > selectedX && x < selectedX2) || (x < selectedX && x > selectedX2))
				&& ((y > selectedY && y < selectedY2) || (y < selectedY && y > selectedY2))) {
			fig.setIsSelected(true);
			elementsSelected = true;
		} else {
			fig.setIsSelected(false);
		}
	}

	private void drawSelection(IFigure fig) {
		Rectangle r = new Rectangle((int) fig.getX(), (int) fig.getY() - 6, (int) fig.getWidth(),
				(int) fig.getHeight() + 6);
		graphics2D.setColor(Color.GREEN);
		graphics2D.draw(r);
	}

	private void drawDestination(IMovingUnit unit) {
		double x = unit.getMovingDestination().getPosition().getX() + unit.getWidth() / 2;
		double y = unit.getMovingDestination().getPosition().getY() + unit.getHeight() / 2;
		graphics2D.setColor(Color.GREEN);
		graphics2D.drawOval((int) x - 2, (int) y - 2, 4, 4);
	}

	private void drawAttack(IUnit unit, Color color) {
		double x = unit.getX() + unit.getWidth() / 2;
		double y = unit.getY() + unit.getHeight() / 2;
		double x2 = unit.getEnemy().getX() + unit.getEnemy().getWidth() / 2;
		double y2 = unit.getEnemy().getY() + unit.getEnemy().getHeight() / 2;
		graphics2D.setColor(color);
		graphics2D.drawLine((int) x, (int) y, (int) x2, (int) y2);
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public void select(int x, int y, int x2, int y2) {
		selectionActive = true;
		selectedX = x;
		selectedY = y;
		selectedX2 = x2;
		selectedY2 = y2;
	}

	public void finalizeSelection() {
		selectionActive = false;
	}

	public void leftClick(int x, int y) {
		if (elementsSelected)
			deselect();
		else {
			selectOneFigure(x, y);
		}
	}

	public void rightClick(int x, int y) {
		if (elementsSelected) {
			IUnit enemy = checkIfRightClickOnEnemy(x, y);

			List<IMovingUnit> selectedFigures = new ArrayList<>();
			for (IMovingUnit fig : units) {
				if (fig.isSelected()) {
					selectedFigures.add(fig);
				}
			}

			int maxInRow = (int) Math.sqrt(selectedFigures.size()) + 1;
			int row = 0;
			int col = 0;
			int leftIndex = 0;
			int topIndex = 0;

			if (enemy == null) {
				for (IMovingUnit fig : selectedFigures) {
					if (col == 0) {
						leftIndex = -1;
					} else {
						leftIndex = col + maxInRow * row - 1;
					}

					if (row == 0) {
						topIndex = -1;
					} else {
						topIndex = (row - 1) * maxInRow + col;
					}

					double newX = x - fig.getWidth() / 2;
					double newY = y - fig.getHeight() / 2;

					if (leftIndex != -1) {
						newX = selectedFigures.get(leftIndex).getMovingDestination().getPosition().getX();
						newX += selectedFigures.get(leftIndex).getWidth() + 5;
					} else {

					}

					if (topIndex != -1) {
						newY = selectedFigures.get(topIndex).getMovingDestination().getPosition().getY();
						newY += selectedFigures.get(topIndex).getHeight() + 5;
					} else {

					}

					fig.setMovingDestination(new MovingDestination(newX, newY));
					col++;
					if (col == maxInRow) {
						row++;
						col = 0;
					}
				}

				if (selectedFigures.size() > 1) {
					double sumWidth = selectedFigures.get(maxInRow - 1).getMovingDestination().getPosition().getX()
							- selectedFigures.get(0).getMovingDestination().getPosition().getX();
					double sumHeight = selectedFigures.get(selectedFigures.size() - 1).getMovingDestination()
							.getPosition().getY() - selectedFigures.get(0).getMovingDestination().getPosition().getY();
					sumHeight /= 2;
					sumWidth /= 2;
					for (IMovingUnit fig : selectedFigures) {
						fig.getMovingDestination().getPosition()
								.setX(fig.getMovingDestination().getPosition().getX() - sumWidth);
						fig.getMovingDestination().getPosition()
								.setY(fig.getMovingDestination().getPosition().getY() - sumHeight);
					}
				}
			}else{
				for (IMovingUnit fig : selectedFigures) {
					double mox = 0;
					double moy = 0;
					double diffX = fig.getX() - enemy.getX();
					double diffY = fig.getY() - enemy.getY();
					if(Math.abs(diffX) < Math.abs(diffY)){
						if(fig.getY() <  enemy.getY()){
							moy = enemy.getY() - fig.getRange() + 1;
							mox = enemy.getX() - fig.getRange() + 1 + r.nextInt((int)(enemy.getWidth() + 2 * fig.getRange())); 
						}else{
							moy = enemy.getY() + enemy.getHeight() + fig.getRange() - 1;
							mox = enemy.getX() - fig.getRange() + 1 + r.nextInt((int)(enemy.getWidth() + 2 * fig.getRange()));
						}	
					}else{
						if(fig.getX() < enemy.getY()){
							mox = enemy.getX() - fig.getRange() + 1;
							moy = enemy.getY() - fig.getRange() + 1 + r.nextInt((int)(enemy.getHeight() + 2 * fig.getRange())); 
						}else{
							mox = enemy.getX() + enemy.getWidth() + fig.getRange() - 1;
							moy = enemy.getY() - fig.getRange() + 1 + r.nextInt((int)(enemy.getHeight() + 2 * fig.getRange())); 
						}
					}
					fig.setMovingDestination(new MovingDestination(mox, moy));
				}
			}
			for (IMovingUnit fig : selectedFigures) {
				List<IFigure> nl = new ArrayList<IFigure>(objects);
				nl.addAll(buildings);
				fig.calculatePath(nl, maxX, maxY);
			}
		}
	}

	private IUnit checkIfRightClickOnEnemy(int x, int y) {
		for (IUnit unit : enemyUnits) {
			if ((x < unit.getX() + unit.getWidth()) && (x > unit.getX()) && (y < unit.getY() + unit.getHeight())
					&& (y > unit.getY())) {
				return unit;
			}
		}
		return null;
	}

	private void deselect() {
		for (IMovingFigure r : movingFigures) {
			r.setIsSelected(false);
		}

		for (IFigure r : objects) {
			r.setIsSelected(false);
		}

		for (IBuilding r : buildings) {
			r.setIsSelected(false);
		}
		
		for (IUnit r : units) {
			r.setIsSelected(false);
		}
		elementsSelected = false;
	}
}
