package de.daniel.home.drag_units_2.krap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.JPanel;

import de.daniel.home.drag_units_2.krap.Definitions.Building;
import de.daniel.home.drag_units_2.krap.Definitions.Ressource;

public class Screen extends JPanel {

	private static final long serialVersionUID = -2091777385337312828L;
	private List<IFigure> additionalFigures = new ArrayList<>();
	private List<IFigure> additionalBlockingObjects = new ArrayList<>();
	private List<IInteractingFigure> movingAdditionalFigures = new ArrayList<>();
	private List<IInteractingFigure> myFigures = new ArrayList<>();
	private List<IProductionFigure> myProductionFigures = new ArrayList<>();
	private List<IStorageBuilding> myStorageBuildings = new ArrayList<>();
	private List<IResidentBuilding> myResidentBuildings = new ArrayList<>();
	private List<IInteractingFigure> myRunners = new ArrayList<>();
	private List<IStorageRunner> myStorageRunners = new ArrayList<>();
	private List<IInteractingFigure> enemyFigures = new ArrayList<>();
	private List<IRessourceField> ressourceFields = new ArrayList<>();
	private int maxX;
	private int maxY;
	private int selectedX;
	private int selectedY;
	private int selectedX2;
	private int selectedY2;
	private boolean selectionActive;
	private boolean elementsSelected;
	private boolean interactingSelected;
	private boolean productionSelected;
	private boolean storageSelected;
	private boolean buildMode = false;
	private Random r;
	private ToolScreen toolScreen;
	private Map<Ressource, Integer> ressources;
	private IBuildingInfo buildingInfo;
	private Collision buildingCollision;
	private boolean buildingCollisionFlag;
	private boolean buildingCostFlag;
	private int buildX1;
	private int buildX2;
	private int buildY1;
	private int buildY2;

	public Screen(int maxX, int maxY) {
		super();
		this.maxX = maxX;
		this.maxY = maxY;
		init();
	}

	private void init() {
		Calculator.init(maxX, maxY);
		this.setBackground(new Color(255, 255, 255));
		this.setOpaque(true);
		r = new Random();
		for (int i = 0; i < 4; i++) {
			IInteractingFigure rect = null;

			while (true) {
				boolean newRect = true;
				int x = r.nextInt(maxX);
				int y = r.nextInt(maxY);
				rect = new InteractingFigure(x, y, 25, 25);
				rect.setAx(r.nextInt(2) + 1);
				rect.setAy(r.nextInt(2) + 1);
				rect.setImage("resources/orange.png");
				rect.setUpBImage("resources/up.png");
				rect.setDownBImage("resources/down.png");
				rect.setRightBImage("resources/right.png");
				rect.setLeftBImage("resources/left.png");

				for (IInteractingFigure tempR : movingAdditionalFigures) {
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

			movingAdditionalFigures.add(rect);
		}

		for (int i = 0; i < 20; i++) {
			IFigure ifig = new Figure(500, i * 25, 25, 25);
			ifig.setImage("resources/orange.png");
			additionalBlockingObjects.add(ifig);
		}

		for (int i = 0; i < 15; i++) {
			IInteractingFigure iunit = new InteractingFigure(10 + 35 * i, 10 + 35 * i, 25, 25);
			iunit.setSpeed(2);
			iunit.setMaxHealth(1000);
			iunit.setHealth(900 + 5 * i);
			iunit.setAttack(1);
			iunit.setRange(10);
			iunit.setImage("resources/boi.png");
			iunit.setUpBImage("resources/boi_up.png");
			iunit.setDownBImage("resources/boi_down.png");
			iunit.setRightBImage("resources/boi_right.png");
			iunit.setLeftBImage("resources/boi_left.png");
			iunit.setUpBImageFight("resources/boi_up_fight.png");
			iunit.setDownBImageFight("resources/boi_down_fight.png");
			iunit.setRightBImageFight("resources/boi_right_fight.png");
			iunit.setLeftBImageFight("resources/boi_left_fight.png");
			iunit.setIsMovable(true);
			iunit.setFigureType(Definitions.FigureType.KNIGHT);
			myFigures.add(iunit);
		}
		IInteractingFigure iif = createBuilding(200, 200, 50, 50, "resources/building.png", 50);
		iif.setFigureType(Definitions.FigureType.TOWER);
		myFigures.add(iif);

		for (int i = 0; i < 10; i++) {
			IInteractingFigure iunit = new InteractingFigure(100 + 35 * i, 10 + 35 * i, 25, 25);
			iunit.setMaxHealth(1000);
			iunit.setHealth(900 + 5 * i);
			iunit.setAttack(1);
			iunit.setRange(10);
			iunit.setImage("resources/boi_red.png");
			iunit.setUpBImage("resources/boi_up_red.png");
			iunit.setDownBImage("resources/boi_down_red.png");
			iunit.setRightBImage("resources/boi_right_red.png");
			iunit.setLeftBImage("resources/boi_left_red.png");
			iunit.setUpBImageFight("resources/boi_up_fight_red.png");
			iunit.setDownBImageFight("resources/boi_down_fight_red.png");
			iunit.setRightBImageFight("resources/boi_right_fight_red.png");
			iunit.setLeftBImageFight("resources/boi_left_fight_red.png");
			enemyFigures.add(iunit);
		}

		/*IProductionFigure prodFigure = new ProductionFigure(500, 500, 50, 50);
		Map<Ressource, Integer> prodDelayMap = new HashMap<>();
		prodDelayMap.put(Ressource.WOOD, 5);
		prodFigure.setProductionDelay(prodDelayMap);
		List<Ressource> ressourceList = new LinkedList<>();
		ressourceList.add(Ressource.WOOD);
		prodFigure.setRessources(ressourceList);
		Map<Ressource, Integer> productionTimeLeftMap = new HashMap<>();
		productionTimeLeftMap.put(Ressource.WOOD, 5);
		prodFigure.setProductionTimeLeft(productionTimeLeftMap);
		prodFigure.setImage("resources/building.png");
		prodFigure.setIsProductionDirect(false);
		Map<Ressource, Integer> prodStorageLimit = new HashMap<>();
		prodStorageLimit.put(Ressource.WOOD, 5);
		prodFigure.setProducedRessourcesStorageLimit(prodStorageLimit);
		prodFigure.setName("Lumberjack");
		prodFigure.setProductivity(0.5);
		myProductionFigures.add(prodFigure);

		IProductionFigure prodFigure2 = new ProductionFigure(550, 100, 50, 50);
		Map<Ressource, Integer> prodDelayMap2 = new HashMap<>();
		prodDelayMap2.put(Ressource.GOLD, 10);
		prodFigure2.setProductionDelay(prodDelayMap2);
		List<Ressource> ressourceList2 = new LinkedList<>();
		ressourceList2.add(Ressource.GOLD);
		prodFigure2.setRessources(ressourceList2);
		Map<Ressource, Integer> productionTimeLeftMap2 = new HashMap<>();
		productionTimeLeftMap2.put(Ressource.GOLD, 10);
		prodFigure2.setProductionTimeLeft(productionTimeLeftMap2);
		prodFigure2.setCurrentRessources(Ressource.STONE, 20);
		prodFigure2.setCurrentRessources(Ressource.WOOD, 20);
		Map<Ressource, Integer> neededMap = new HashMap<>();
		neededMap.put(Ressource.STONE, 2);
		neededMap.put(Ressource.WOOD, 5);
		prodFigure2.setNeededRessources(Ressource.GOLD, neededMap);
		prodFigure2.setImage("resources/building.png");
		prodFigure2.setIsProductionDirect(true);
		prodFigure2.setName("Gold Mine");
		myProductionFigures.add(prodFigure2);*/
		
		for(int i = 0; i < 20; i++){
			IRessourceField irf = new RessourceField(20 + i * 10, 350, 10, 10, Ressource.WOOD, 3, 1);
			irf.setImageReference(Definitions.ressourceImageWood);
			ressourceFields.add(irf);
		}

		ressources = new HashMap<>();
		ressources.put(Ressource.WOOD, 100);
		ressources.put(Ressource.GOLD, 100);
		ressources.put(Ressource.STONE, 100);
		ressources.put(Ressource.MONEY, 10000);

		Selector selector = new Selector(this);
		this.addMouseListener(selector);
		this.addMouseMotionListener(selector);
	}

	@Override
	public void paintComponent(Graphics g) {
		// super.paintComponent(g);
		// g.clearRect(0, 0, this.getWidth(), this.getHeight());

		// Calculator.calculateNewPositions(movingFigures, objects);
		Calculator.calculateNewPositionsUnits(myFigures);
		Calculator.calculateNewPositionsUnits(myRunners);
		Calculator.calculateNewPositionsUnits(myStorageRunners);
		// Calculator.calculateNewPositionsEnemyUnits(enemyUnits);
		Calculator.checkEnemyInRange(new ArrayList<IInteractingFigure>(myFigures),
				new ArrayList<IInteractingFigure>(enemyFigures));
		Calculator.fight(new ArrayList<IInteractingFigure>(myFigures), new ArrayList<IInteractingFigure>(enemyFigures));

		List<IInteractingFigure> deadUnits = new ArrayList<IInteractingFigure>();
		for (IInteractingFigure unit : myFigures) {
			if (unit.isDead()) {
				deadUnits.add(unit);
			}
		}
		
		myFigures.removeAll(deadUnits);
		
//		for (IInteractingFigure unit : deadUnits) {
//			myFigures.remove(unit);
//		}

		deadUnits = new ArrayList<IInteractingFigure>();
		for (IInteractingFigure unit : enemyFigures) {
			if (unit.isDead()) {
				deadUnits.add(unit);
			}
		}
		
		enemyFigures.removeAll(deadUnits);
//		for (IInteractingFigure unit : deadUnits) {
//			enemyFigures.remove(unit);
//		}

		buildingCollisionFlag = false;
		if (buildMode) {
			Point p1 = this.getLocationOnScreen();
			Point p2 = MouseInfo.getPointerInfo().getLocation();
			double x = p2.getX() - p1.getX();
			double y = p2.getY() - p1.getY();
			
			x = x - (x % 10);
			y = y - (y % 10);
			
			buildX1 = (int) (x - (0.5 * buildingInfo.getWidth()));
			buildX2 = (int) (x + (0.5 * buildingInfo.getWidth()));
			buildY1 = (int) (y - (0.5 * buildingInfo.getHeight()));
			buildY2 = (int) (y + (0.5 * buildingInfo.getHeight()));
			buildingInfo.setX(buildX1);
			buildingInfo.setY(buildY1);
			
			if(buildingInfo.getCostMoney() <= ressources.get(Ressource.MONEY)
				&& buildingInfo.getCostWood() <= ressources.get(Ressource.WOOD)
				&& buildingInfo.getCostStone() <= ressources.get(Ressource.STONE)
				&& buildingInfo.getCostGold() <= ressources.get(Ressource.GOLD)){
				buildingCostFlag = true;
			}else{
				buildingCostFlag = false;
			}
			
			StringBuilder sb = new StringBuilder("<html>");
			sb.append("Build: " + buildingInfo.getBuildingType() + "<br>");
			sb.append("Costs:<br>");
			sb.append("-Money: " + buildingInfo.getCostMoney() + "/" + ressources.get(Ressource.MONEY) + "<br>");
			sb.append("-Wood: " + buildingInfo.getCostWood() + "/" + ressources.get(Ressource.WOOD) + "<br>");
			sb.append("-Stone: " + buildingInfo.getCostStone() + "/" + ressources.get(Ressource.STONE) + "<br>");
			sb.append("-Gold: " + buildingInfo.getCostGold() + "/" + ressources.get(Ressource.GOLD) + "</html>");
			toolScreen.setBuildBuildingInfo(sb.toString());
		}
		
		Graphics2D graphics2D = (Graphics2D) g;
		
		for (IFigure r : additionalBlockingObjects) {
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			if (buildMode && !buildingCollisionFlag && buildingCostFlag) {
				checkBuildBuildingCollision(r);
			}
		}

		List<IRessourceField> ressFieldsToRemove = new ArrayList<>();

		for (IRessourceField r : ressourceFields) {
			if(r.isEmpty()){
				ressFieldsToRemove.add(r);
				continue;
			}
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			if (buildMode && !buildingCollisionFlag && buildingCostFlag) {
				checkBuildBuildingCollision(r);
			}
		}
		
		ressourceFields.removeAll(ressFieldsToRemove);
		
		for(IInteractingFigure r: myRunners){
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			if (buildMode && !buildingCollisionFlag && buildingCostFlag) {
				checkBuildBuildingCollision(r);
			}
		}
		
		for(IInteractingFigure r: myStorageRunners){
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			if (buildMode && !buildingCollisionFlag && buildingCostFlag) {
				checkBuildBuildingCollision(r);
			}
		}
		
		for (IFigure r : myProductionFigures) {
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			if (r.isSelected()) {
				drawSelection(r, graphics2D);
				graphics2D.setColor(Color.BLACK);
				graphics2D.drawString(r.getName(), (int) r.getX(), (int) r.getY());
				if(((IRadiusHandler)r).hasRadius()){
					IProductionFigure r2 = (IProductionFigure)r;
					int radiusX = (int)(r2.getX() + 0.5 * r.getWidth() - r2.getRadiusSize());
					int radiusY = (int)(r2.getY() + 0.5 * r.getHeight() - r2.getRadiusSize());
					graphics2D.setColor(Color.GREEN);
					graphics2D.drawOval(radiusX, radiusY, 2 * r2.getRadiusSize(), 2 * r2.getRadiusSize());
				}
			}
			if (buildMode && !buildingCollisionFlag && buildingCostFlag) {
				checkBuildBuildingCollision(r);
			}
		}
		
		for (IFigure r : myStorageBuildings) {
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			if (r.isSelected()) {
				drawSelection(r, graphics2D);
				graphics2D.setColor(Color.BLACK);
				graphics2D.drawString(r.getName(), (int) r.getX(), (int) r.getY());
				if(((IRadiusHandler)r).hasRadius()){
					IStorageBuilding r2 = (IStorageBuilding)r;
					int radiusX = (int)(r2.getX() + 0.5 * r.getWidth() - r2.getRadiusSize());
					int radiusY = (int)(r2.getY() + 0.5 * r.getHeight() - r2.getRadiusSize());
					graphics2D.setColor(Color.GREEN);
					graphics2D.drawOval(radiusX, radiusY, 2 * r2.getRadiusSize(), 2 * r2.getRadiusSize());
				}
			}
			if (buildMode && !buildingCollisionFlag && buildingCostFlag) {
				checkBuildBuildingCollision(r);
			}
		}

		StringBuilder myFiguresStringBuilder = new StringBuilder("<html>");

		for (IInteractingFigure r : myFigures) {
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			double health = (r.getHealth() / r.getMaxHealth()) * r.getWidth();
			Rectangle r2 = new Rectangle((int) r.getX(), (int) r.getY() - 5, (int) health, 4);
			int red = (int) ((1 - (r.getHealth() / r.getMaxHealth())) * 255);
			int green = (int) ((r.getHealth() / r.getMaxHealth()) * 255);
			Color co = new Color(red, green, 0);
			graphics2D.setColor(co);
			graphics2D.fill(r2);
			if (interactingSelected) {
				if (r.isSelected()) {
					drawSelection(r, graphics2D);

					if (r.getMovingDestination() != null) {
						drawDestination(r, graphics2D);
					}

					myFiguresStringBuilder.append(
							r.getFigureType().toString() + " " + r.getHealth() + "/" + r.getMaxHealth() + "<br>");
				}

				if (r.getEnemy() != null) {
					drawAttack(r, Color.GREEN, graphics2D);
				}
			}
			if (buildMode && !buildingCollisionFlag && buildingCostFlag) {
				checkBuildBuildingCollision(r);
			}
		}

		for (IInteractingFigure r : enemyFigures) {
			graphics2D.drawImage(r.getImage(), (int) r.getX(), (int) r.getY(), null);
			double health = (r.getHealth() / r.getMaxHealth()) * r.getWidth();
			Rectangle r2 = new Rectangle((int) r.getX(), (int) r.getY() - 5, (int) health, 4);
			int red = (int) ((1 - (r.getHealth() / r.getMaxHealth())) * 255);
			int green = (int) ((r.getHealth() / r.getMaxHealth()) * 255);
			Color co = new Color(red, green, 0);
			graphics2D.setColor(co);
			graphics2D.fill(r2);
			if (r.getEnemy() != null) {
				drawAttack(r, Color.RED, graphics2D);
			}

			if (buildMode && !buildingCollisionFlag && buildingCostFlag) {
				checkBuildBuildingCollision(r);
			}
		}

		if (productionSelected) {
			for (IProductionFigure ipf : myProductionFigures) {
				if (ipf.isSelected()) {
					myFiguresStringBuilder.append(ipf.getName() + " [" + (ipf.getProductivity() * 100) + "%]" + "<br>");
					Map<Ressource, Integer> leftMap = ipf.getProductionTimeLeft();
					for (Entry<Ressource, Integer> leftMapEntry : leftMap.entrySet()) {
						myFiguresStringBuilder.append("-" + leftMapEntry.getKey());
						myFiguresStringBuilder.append(" (" + leftMapEntry.getValue() + ") [+" + ipf.getProductionAmount() + "]");
						myFiguresStringBuilder.append("<br>needs:");
						if (ipf.getNeededRessource(leftMapEntry.getKey()) != null) {
							for (Entry<Ressource, Integer> neededEntry : ipf.getNeededRessource(leftMapEntry.getKey())
									.entrySet()) {
								myFiguresStringBuilder
										.append("<br>-" + neededEntry.getKey() + ": " + neededEntry.getValue());
							}
						} else {
							myFiguresStringBuilder.append(" -");
						}
					}

					myFiguresStringBuilder.append("<br>Storage:");
					Map<Ressource, Integer> currentMap = ipf.getCurrentRessources();
					for (Entry<Ressource, Integer> storageEntry : currentMap.entrySet()) {
						myFiguresStringBuilder.append("<br>-" + storageEntry.getKey() + ": " + storageEntry.getValue() + "/" +
					ipf.getCurrentRessourcesStorageLimit().get(storageEntry.getKey()));
					}

					if (!ipf.isProductionDirect()) {
						myFiguresStringBuilder.append("<br>Produces Ressources Storage:");
						Map<Ressource, Integer> currentProducedMap = ipf.getProducedRessourcesStorage();
						for (Entry<Ressource, Integer> producesStorageEntry : currentProducedMap.entrySet()) {
							myFiguresStringBuilder.append("<br>-" + producesStorageEntry.getKey() + ": "
									+ producesStorageEntry.getValue() + " / "
									+ ipf.getProducedRessourceStorageLimit(producesStorageEntry.getKey()));
						}
					}

					break;
				}
			}
		}
		
		if (storageSelected) {
			for (IStorageBuilding isb : myStorageBuildings) {
				if (isb.isSelected()) {
					myFiguresStringBuilder.append(isb.getName() + " [Cap: " + isb.getCapacity() + ", Runners Cap:" + isb.getMaxNumOfRunners() + "]" + "<br>");
					myFiguresStringBuilder.append("<br>Ressources:<br>");
					myFiguresStringBuilder.append("Gold: " + isb.getRessource(Ressource.GOLD) + "<br>");
					myFiguresStringBuilder.append("Wood: " + isb.getRessource(Ressource.WOOD) + "<br>");
					myFiguresStringBuilder.append("Stone: " + isb.getRessource(Ressource.STONE) + "<br>");
					myFiguresStringBuilder.append("Runners: " + isb.getNumberOfRunners() + " (" + isb.getNumberOfRunnersOnTheWay() + " running)<br>");
					break;
				}
			}
		}
		
		myFiguresStringBuilder.append("</html>");

		if (selectionActive) {
			graphics2D.setColor(Color.BLACK);
			graphics2D.drawLine(selectedX, selectedY, selectedX, selectedY2);
			graphics2D.drawLine(selectedX, selectedY, selectedX2, selectedY);
			graphics2D.drawLine(selectedX2, selectedY2, selectedX, selectedY2);
			graphics2D.drawLine(selectedX2, selectedY2, selectedX2, selectedY);
			selectFigures();
		}

		if (buildMode) {
			if (buildingCollisionFlag || !buildingCostFlag) {
				graphics2D.setColor(Color.RED);
			} else {
				graphics2D.setColor(Color.GREEN);
			}

			graphics2D.drawLine(buildX1, buildY1, buildX2, buildY1);
			graphics2D.drawLine(buildX2, buildY1, buildX2, buildY2);
			graphics2D.drawLine(buildX1, buildY1, buildX1, buildY2);
			graphics2D.drawLine(buildX1, buildY2, buildX2, buildY2);
			
			if(buildingInfo.hasRadius()){
				int radiusX = (int)(buildX1 + 0.5 * buildingInfo.getWidth() - buildingInfo.getRadiusSize());
				int radiusY = (int)(buildY1 + 0.5 * buildingInfo.getHeight() - buildingInfo.getRadiusSize());
				graphics2D.drawOval(radiusX, radiusY, 2 * buildingInfo.getRadiusSize(), 2 * buildingInfo.getRadiusSize());
			}
		}

		if(elementsSelected || interactingSelected || productionSelected || storageSelected){
			toolScreen.setSelectedFiguresInfo(myFiguresStringBuilder.toString());
		}
		
		toolScreen.setGoldInfo(ressources.get(Ressource.GOLD));
		toolScreen.setWoodInfo(ressources.get(Ressource.WOOD));
		toolScreen.setStoneInfo(ressources.get(Ressource.STONE));
		toolScreen.setMoneyInfo(ressources.get(Ressource.MONEY));
		graphics2D.dispose();
	}

	private void selectFigures() {
		for (IInteractingFigure r : myFigures) {
			selectFigure(r);
		}
	}

	private void selectOneFigure(int x, int y) {
		for (IInteractingFigure r : myFigures) {
			if (selectOneFigure(x, y, r)) {
				r.setIsSelected(true);
				elementsSelected = true;
				interactingSelected = true;
				productionSelected = false;
				storageSelected = false;
				return;
			}
		}

		for (IProductionFigure ipf : myProductionFigures) {
			if (selectOneFigure(x, y, ipf)) {
				ipf.setIsSelected(true);
				elementsSelected = true;
				interactingSelected = false;
				productionSelected = true;
				storageSelected = false;
				return;
			}
		}
		
		for (IStorageBuilding ipf : myStorageBuildings) {
			if (selectOneFigure(x, y, ipf)) {
				ipf.setIsSelected(true);
				elementsSelected = true;
				interactingSelected = false;
				productionSelected = false;
				storageSelected = true;
				return;
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

	private void selectFigure(IInteractingFigure fig) {
		int x = (int) (fig.getX() + 0.5 * fig.getWidth());
		int y = (int) (fig.getY() + 0.5 * fig.getHeight());
		if (((x > selectedX && x < selectedX2) || (x < selectedX && x > selectedX2))
				&& ((y > selectedY && y < selectedY2) || (y < selectedY && y > selectedY2))) {
			fig.setIsSelected(true);
			elementsSelected = true;
			interactingSelected = true;
			productionSelected = false;
		} else {
			fig.setIsSelected(false);
		}
	}

	private void drawSelection(IFigure fig, Graphics2D graphics2D) {
		Rectangle r = new Rectangle((int) fig.getX(), (int) fig.getY() - 6, (int) fig.getWidth(),
				(int) fig.getHeight() + 6);
		graphics2D.setColor(Color.GREEN);
		graphics2D.draw(r);
	}

	private void drawDestination(IInteractingFigure unit, Graphics2D graphics2D) {
		double x = unit.getMovingDestination().getPosition().getX() + unit.getWidth() / 2;
		double y = unit.getMovingDestination().getPosition().getY() + unit.getHeight() / 2;
		graphics2D.setColor(Color.GREEN);
		graphics2D.drawOval((int) x - 2, (int) y - 2, 4, 4);
	}

	private void drawAttack(IInteractingFigure unit, Color color, Graphics2D graphics2D) {
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
		
		if (buildMode && !buildingCollisionFlag && buildingCostFlag) {
			buildBuildingFinalize();
		}
	}

	public void rightClick(int x, int y) {
		if (buildMode) {
			buildMode = false;
		}
		if (elementsSelected) {
			IInteractingFigure enemy = checkIfRightClickOnEnemy(x, y);

			List<IInteractingFigure> selectedFigures = new ArrayList<>();
			for (IInteractingFigure fig : myFigures) {
				if (fig.isSelected() && fig.isMovable()) {
					selectedFigures.add(fig);
				}
			}

			int maxInRow = (int) Math.sqrt(selectedFigures.size()) + 1;
			int row = 0;
			int col = 0;
			int leftIndex = 0;
			int topIndex = 0;

			if (enemy == null) {
				for (IInteractingFigure fig : selectedFigures) {
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
					for (IInteractingFigure fig : selectedFigures) {
						fig.getMovingDestination().getPosition()
								.setX(fig.getMovingDestination().getPosition().getX() - sumWidth);
						fig.getMovingDestination().getPosition()
								.setY(fig.getMovingDestination().getPosition().getY() - sumHeight);
					}
				}
			} else {
				for (IInteractingFigure fig : selectedFigures) {
					double mox = 0;
					double moy = 0;
					double diffX = fig.getX() - enemy.getX();
					double diffY = fig.getY() - enemy.getY();
					if (Math.abs(diffX) < Math.abs(diffY)) {
						if (fig.getY() < enemy.getY()) {
							moy = enemy.getY() - fig.getRange() + 1;
							mox = enemy.getX() - fig.getRange() + 1
									+ r.nextInt((int) (enemy.getWidth() + 2 * fig.getRange()));
						} else {
							moy = enemy.getY() + enemy.getHeight() + fig.getRange() - 1;
							mox = enemy.getX() - fig.getRange() + 1
									+ r.nextInt((int) (enemy.getWidth() + 2 * fig.getRange()));
						}
					} else {
						if (fig.getX() < enemy.getY()) {
							mox = enemy.getX() - fig.getRange() + 1;
							moy = enemy.getY() - fig.getRange() + 1
									+ r.nextInt((int) (enemy.getHeight() + 2 * fig.getRange()));
						} else {
							mox = enemy.getX() + enemy.getWidth() + fig.getRange() - 1;
							moy = enemy.getY() - fig.getRange() + 1
									+ r.nextInt((int) (enemy.getHeight() + 2 * fig.getRange()));
						}
					}
					fig.setMovingDestination(new MovingDestination(mox, moy));
				}
			}
			for (IInteractingFigure fig : selectedFigures) {
				fig.calculatePath(maxX, maxY, additionalBlockingObjects, new LinkedList<IFigure>(myProductionFigures));
			}
		}
	}

	private IInteractingFigure checkIfRightClickOnEnemy(int x, int y) {
		for (IInteractingFigure unit : enemyFigures) {
			if ((x < unit.getX() + unit.getWidth()) && (x > unit.getX()) && (y < unit.getY() + unit.getHeight())
					&& (y > unit.getY())) {
				return unit;
			}
		}
		return null;
	}

	public void deselect() {
		for (IFigure r : myFigures) {
			r.setIsSelected(false);
		}

		for (IFigure r : myProductionFigures) {
			r.setIsSelected(false);
		}
		
		for (IFigure r : myStorageBuildings) {
			r.setIsSelected(false);
		}

		elementsSelected = false;
		interactingSelected = false;
		productionSelected = false;
		storageSelected = false;
		
		toolScreen.setSelectedFiguresInfo("");
	}

	private IInteractingFigure createBuilding(double x, double y, double width, double height, String image,
			double maxHealth) {
		IInteractingFigure building = new InteractingFigure(x, y, width, height);
		building.setImage(image);
		building.setIsMovable(false);
		building.setHealth(maxHealth);
		building.setMaxHealth(maxHealth);
		return building;
	}

	private IInteractingFigure createAttackingBuilding(double x, double y, double width, double height, String image,
			double maxHealth, double attack, double range) {
		IInteractingFigure building = createBuilding(x, y, width, height, image, maxHealth);
		building.setCanAttack(true);
		building.setAttack(attack);
		building.setRange(range);
		return building;
	}

	public void setToolScreen(ToolScreen ts) {
		this.toolScreen = ts;
	}

	public void calculateRessources() {
		Calculator.calculateRessources(myProductionFigures);
		for (IProductionFigure ipf : myProductionFigures) {
			if (ipf.isProductionDirect()) {
				for (Entry<Ressource, Integer> entry : ipf.getFinishedRessources().entrySet()) {
					ressources.put(entry.getKey(), ressources.get(entry.getKey()) + entry.getValue());
					entry.setValue(0);
				}
			} else {

			}
		}
		
		Calculator.calculateStorage(myStorageBuildings);
	}
	
	public void deactivateBuildingMode(){
		buildMode = false;
		buildingCollision = null;
		buildingCollisionFlag = false;
		buildingCostFlag = false;
	}

	public void buildBuilding(IBuildingInfo b) {
		deselect();
		buildMode = true;
		buildingInfo = b;
	}
	
	private void calculateRessourcesAfterBuilding(){
		ressources.put(Ressource.MONEY, ressources.get(Ressource.MONEY) - buildingInfo.getCostMoney());
		ressources.put(Ressource.WOOD, ressources.get(Ressource.WOOD) - buildingInfo.getCostWood());
		ressources.put(Ressource.STONE, ressources.get(Ressource.STONE) - buildingInfo.getCostStone());
		ressources.put(Ressource.GOLD, ressources.get(Ressource.GOLD) - buildingInfo.getCostGold());
	}
	
	private void buildBuildingFinalize(){
		deactivateBuildingMode();
		calculateRessourcesAfterBuilding();
		
		switch(buildingInfo.getBuildingType()){
		case GOLDMINE:
			IProductionFigure ipf1 = getBuildingGoldMine(buildingInfo.getX(), buildingInfo.getY());
			myProductionFigures.add(ipf1);
			break;
		case LUMBERJACK:
			IProductionFigure ipf2 = getBuildingLumberjack(buildingInfo.getX(), buildingInfo.getY());
			myProductionFigures.add(ipf2);
			break;
		case RESIDENT:
			IProductionFigure ipf3 = getBuildingResident(buildingInfo.getX(), buildingInfo.getY());
			myProductionFigures.add(ipf3);
			break;
		case LITTLESTORAGE:
			IStorageBuilding isb = getBuildingLittleStorageBuilding(buildingInfo.getX(), buildingInfo.getY());
			myStorageBuildings.add(isb);
		default:
			break;
		}
		
		initProductionFiguresInRadiusOfStorage();
		
		buildingInfo = null;
		toolScreen.setBuildBuildingInfo("");
	}

	private void initProductionFiguresInRadiusOfStorage() {
		for(IStorageBuilding isb: myStorageBuildings){
			isb.initProductionFiguresInRadius(getMyProductionFigures());
		}
	}

	private void checkBuildBuildingCollision(IFigure r) {
		buildingCollision = buildingInfo.checkCollision(r);
		if (buildingCollision.isCollision) {
			buildingCollisionFlag = true;
		}
	}
	
	private IProductionFigure getBuildingGoldMine(double x, double y){
		IProductionFigure prodFigure = new ProductionFigure(x, y, 50, 50);
		prodFigure.setScreen(this);
		Map<Ressource, Integer> prodDelayMap2 = new HashMap<>();
		prodDelayMap2.put(Ressource.GOLD, 2);
		prodFigure.setProductionDelay(prodDelayMap2);
		List<Ressource> ressourceList2 = new LinkedList<>();
		ressourceList2.add(Ressource.GOLD);
		prodFigure.setRessources(ressourceList2);
		Map<Ressource, Integer> productionTimeLeftMap2 = new HashMap<>();
		productionTimeLeftMap2.put(Ressource.GOLD, 2);
		prodFigure.setProductionTimeLeft(productionTimeLeftMap2);
		prodFigure.setCurrentRessources(Ressource.STONE, 20);
		prodFigure.setCurrentRessources(Ressource.WOOD, 20);
		prodFigure.setCurrentRessourceStorageLimit(Ressource.STONE, 20);
		prodFigure.setCurrentRessourceStorageLimit(Ressource.WOOD, 20);
		Map<Ressource, Integer> neededMap = new HashMap<>();
		neededMap.put(Ressource.STONE, 2);
		neededMap.put(Ressource.WOOD, 5);
		prodFigure.setNeededRessources(Ressource.GOLD, neededMap);
		prodFigure.setImage("resources/building.png");
		prodFigure.setIsProductionDirect(false);
		Map<Ressource, Integer> prodStorageLimit = new HashMap<>();
		prodStorageLimit.put(Ressource.GOLD, 10);
		prodFigure.setProducedRessourcesStorageLimit(prodStorageLimit);
		prodFigure.setName("Gold Mine");
		prodFigure.setType(Building.GOLDMINE);
		return prodFigure;
	}
	
	private IProductionFigure getBuildingLumberjack(double x, double y){
		IProductionFigure prodFigure = new ProductionFigure(x, y, 50, 50);
		prodFigure.setScreen(this);
		Map<Ressource, Integer> prodDelayMap = new HashMap<>();
		prodDelayMap.put(Ressource.WOOD, 1);
		prodFigure.setProductionDelay(prodDelayMap);
		List<Ressource> ressourceList = new LinkedList<>();
		ressourceList.add(Ressource.WOOD);
		prodFigure.setRessources(ressourceList);
		Map<Ressource, Integer> productionTimeLeftMap = new HashMap<>();
		productionTimeLeftMap.put(Ressource.WOOD, 1);
		prodFigure.setProductionTimeLeft(productionTimeLeftMap);
		prodFigure.setImageReference(Definitions.buildingImageLumberjack);
		prodFigure.setIsProductionDirect(false);
		Map<Ressource, Integer> prodStorageLimit = new HashMap<>();
		prodStorageLimit.put(Ressource.WOOD, 500);
		prodFigure.setProducedRessourcesStorageLimit(prodStorageLimit);
		prodFigure.setName("Lumberjack");
		prodFigure.setProductivity(1);
		prodFigure.setType(Building.LUMBERJACK);
		prodFigure.setHasRadius(true);
		prodFigure.setRadiusSize(125);
		prodFigure.initAssociatedRessourceFields(ressourceFields, Ressource.WOOD);
		prodFigure.initRunner(Definitions.figureImageWorker, Definitions.figureImageWorker, Definitions.figureImageWorker, Definitions.figureImageWorker, Definitions.figureImageWorker, 10, 10, 5);
		return prodFigure;
	}
	
	private IProductionFigure getBuildingResident(double x, double y) {
		IProductionFigure prodFigure = new ProductionFigure(x, y, 50, 50);
		Map<Ressource, Integer> prodDelayMap2 = new HashMap<>();
		prodDelayMap2.put(Ressource.MONEY, 5);
		prodFigure.setProductionDelay(prodDelayMap2);
		List<Ressource> ressourceList2 = new LinkedList<>();
		ressourceList2.add(Ressource.MONEY);
		prodFigure.setRessources(ressourceList2);
		Map<Ressource, Integer> productionTimeLeftMap2 = new HashMap<>();
		productionTimeLeftMap2.put(Ressource.MONEY, 5);
		prodFigure.setProductionTimeLeft(productionTimeLeftMap2);
		prodFigure.setImageReference(Definitions.buildingImageResident);
		prodFigure.setIsProductionDirect(true);
		prodFigure.setName("Resident");
		prodFigure.setType(Building.RESIDENT);
		return prodFigure;
	}
	
	private IStorageBuilding getBuildingLittleStorageBuilding(double x, double y) {
		IStorageBuilding isb = new StorageBuilding(x, y, 50, 50);
		isb.setScreen(this);
		isb.setImageReference(Definitions.buildingImageLittleStorage);
		isb.setName("Little Storage Building");
		isb.setHasRadius(true);
		isb.setRadiusSize(250);
		isb.setCapacity(100);
		isb.setMaxNumOfRunners(5);
		isb.initProductionFiguresInRadius(myProductionFigures);
		isb.addRunner(Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, 10, 10, 2, 1);
		isb.addRunner(Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, 10, 10, 2, 1);
		isb.addRunner(Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, 10, 10, 2, 1);
		isb.addRunner(Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, 10, 10, 2, 1);
		isb.addRunner(Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, Definitions.figureImageStorageRunner, 10, 10, 2, 1);
		isb.getRessources().put(Ressource.WOOD, 20);
		isb.getRessources().put(Ressource.STONE, 20);
		isb.getRessources().put(Ressource.GOLD, 20);
		isb.getReservedStorage().put(Ressource.WOOD, 0);
		isb.getReservedStorage().put(Ressource.STONE, 0);
		isb.getReservedStorage().put(Ressource.GOLD, 0);
		return isb;
	}
	
	public List<IInteractingFigure> getMyRunners(){
		if(myRunners == null) 
			myRunners = new LinkedList<>();
			
		return myRunners;
	}
	
	public List<IStorageRunner> getMyStorageRunners(){
		if(myStorageRunners == null) 
			myStorageRunners = new LinkedList<>();
			
		return myStorageRunners;
	}
	
	public List<IFigure> getAdditionalBlockingObjects(){
		return additionalBlockingObjects;
	}
	
	public List<IProductionFigure> getMyProductionFigures(){
		return myProductionFigures;
	}
	
	public List<IRessourceField> getRessourceFields(){
		return ressourceFields;
	}
	
	public List<IStorageBuilding> getMyStorageBuildings(){
		return myStorageBuildings;
	}
	
	public List<IResidentBuilding> getMyResidentBuildings(){
		return myResidentBuildings;
	}
}
