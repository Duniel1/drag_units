package de.daniel.home.drag_units_2.krap;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.daniel.home.drag_units_2.krap.Definitions.Ressource;
import de.daniel.home.drag_units_2.krap.Definitions.StorageBuildingRequestType;

public class ResidentBuilding extends Figure implements IResidentBuilding {

	private int residents;
	private int maxResidents;
	private int satisfaction = 10;
	private int maxSatisfaction = 10;
	private int residentCheckCounterDown = 0;
	private int residentCheckDown = 10;
	private int residentCheckCounterUp = 0;
	private int residentCheckUp = 10;
	private int level;
	private int productionDelay;
	private int productionTimeLeft;
	private Map<Ressource, Integer> neededRessources;
	private Map<Ressource, Integer> currentRessources;
	private Map<Ressource, Integer> currentRessourcesStorageLimit;
	private int productionAmount = 1;
	private Map<IStorageRunner, IStorageRunnerPackage> storageRunnerPackages;
	private List<IStorageBuilding> associatedStorageBuildings;
	private IStorageBuilding currentStorageBuilding;
	private Map<Ressource, Integer> pendingOrderRequest;
	private int producedMoney;
	private Screen screen;
	private boolean hasRadius;
	private int radiusSize;
	private IStorageRunner runner;
	private boolean runnerOnTheWayStorage = false;
	private boolean runnerOnTheWayHome = false;
	private boolean runnerAtStorage = false;
	private boolean runnerAtHome = true;
	private boolean runnerAtHomeWaiting = true;

	public ResidentBuilding(double x, double y, double width, double height) {
		super(x, y, width, height);
		neededRessources = new HashMap<>();
		currentRessources = new HashMap<>();
		currentRessourcesStorageLimit = new HashMap<>();
		storageRunnerPackages = new HashMap<>();
		associatedStorageBuildings = new ArrayList<>();
		pendingOrderRequest = new HashMap<>();
	}

	@Override
	public void calculateRessources() {
		if (runnerOnTheWayStorage) {
			if (isAtStorageBuilding()) {
				setRunnerPathToHome();
				runnerOnTheWayStorage = false;
				runnerAtStorage = true;
			}
		} else if (runnerAtStorage) {
			currentStorageBuilding.handleRunner(runner);
			setRunnerPathToHome();
		} else if (runnerOnTheWayHome) {
			if (isArrivedAtHome()) {
				runnerOnTheWayHome = false;
				runnerAtHome = true;
				runnerAtHomeWaiting = true;
				currentRessources.put(runner.getStorageBuildingRequest().getRessource(),
						currentRessources.get(runner.getStorageBuildingRequest().getRessource())
								+ runner.getStorageBuildingRequest().getAmount());
				productionTimeLeft = productionDelay;
			}
		} else if (runnerAtHome) {
			if (runnerAtHomeWaiting) {
				productionTimeLeft--;
				if (productionTimeLeft == 0) {
					producedMoney += productionAmount * satisfaction * residents;
					for (Entry<Ressource, Integer> entry : neededRessources.entrySet()) {
						int newValueCurrentRessources = currentRessources.get(entry.getKey()) - entry.getValue();
						if (newValueCurrentRessources < 0) {
							newValueCurrentRessources = 0;
							satisfaction -= 1;
							residentCheckCounterUp = 0;
							residentCheckCounterDown++;
							if(residentCheckCounterDown == residentCheckDown){
								residents--;
								if(residents < 1) 
									residents = 1;
							}
							if (satisfaction <= 1) {
								satisfaction = 1;
								System.out.println("heul doch");
							}
						}else{
							satisfaction++;
							if(satisfaction > maxSatisfaction){
								satisfaction = maxSatisfaction;
							}
							residentCheckCounterDown = 0;
							residentCheckCounterUp++;
							if(residentCheckCounterUp == residentCheckUp){
								residents++;
								if(residents > maxResidents) residents = maxResidents;
							}
						}
						currentRessources.put(entry.getKey(), newValueCurrentRessources);
					}
				}

				for (Entry<Ressource, Integer> entry : neededRessources.entrySet()) {
					if (currentRessources.get(entry.getKey()) < currentRessourcesStorageLimit.get(entry.getKey())) {
						// handlerrequest
						if (pendingOrderRequest.get(entry.getKey()) == null) {
							pendingOrderRequest.put(entry.getKey(), 0);
						}
						if ((currentRessources.get(entry.getKey()) + pendingOrderRequest
								.get(entry.getKey()) < (currentRessourcesStorageLimit.get(entry.getKey())))) {
							sendStorageRunnerOrderRequests(entry.getKey(), 1);
						}
					}
				}
			}
		}
	}

	@Override
	public int getProductionDelay() {
		return productionDelay;
	}

	@Override
	public void setProductionDelay(int i) {
		productionDelay = i;
	}

	@Override
	public int getProductionTimeLeft() {
		return productionTimeLeft;
	}

	@Override
	public void setProductionTimeLeft(int i) {
		productionTimeLeft = i;
	}

	@Override
	public Map<Ressource, Integer> getNeededRessources() {
		return neededRessources;
	}

	@Override
	public void setNeededRessources(Map<Ressource, Integer> m) {
		neededRessources = m;
	}

	@Override
	public Map<Ressource, Integer> getCurrentRessources() {
		return currentRessources;
	}

	@Override
	public void setCurrentRessources(Ressource r, int i) {
		currentRessources.put(r, i);
	}

	@Override
	public void setCurrentRessourceStorageLimit(Ressource r, int i) {
		currentRessourcesStorageLimit.put(r, i);
	}

	@Override
	public boolean hasRadius() {
		return hasRadius;
	}

	@Override
	public void setHasRadius(boolean b) {
		hasRadius = b;
	}

	@Override
	public int getRadiusSize() {
		return radiusSize;
	}

	@Override
	public void setRadiusSize(int i) {
		radiusSize = i;
		hasRadius = true;
	}

	@Override
	public void initAssociatedStorageBuildings(List<IStorageBuilding> sb) {
		associatedStorageBuildings = new ArrayList<IStorageBuilding>();
		for (IStorageBuilding isb : sb) {
			int middleX = (int) (x + 0.5 * width);
			int middleY = (int) (y + 0.5 * height);
			int middleXRes = (int) (isb.getX() + 0.5 * isb.getWidth());
			int middleYRes = (int) (isb.getY() + 0.5 * isb.getHeight());
			int xDiff = Math.abs(middleX - middleXRes);
			int yDiff = Math.abs(middleY - middleYRes);
			int distance = (int) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
			if (distance <= radiusSize) {
				associatedStorageBuildings.add(isb);
			}
		}

	}

	@Override
	public void initRunner(BufferedImage biNormal, BufferedImage biLeft, BufferedImage biRight, BufferedImage biUp,
			BufferedImage biDown, int width, int height, int speed) {
		int runnerX = (int) (this.x + 0.5 * this.width - 0.5 * width);
		int runnerY = (int) (this.y + 0.5 * this.height - 0.5 * height);
		runner = new StorageRunner(runnerX, runnerY, width, height);
		runner.setImageReference(biNormal);
		runner.setSpeed(speed);
		runner.setUpBImageReference(biUp);
		runner.setDownBImageReference(biDown);
		runner.setRightBImageReference(biRight);
		runner.setLeftBImageReference(biLeft);
		screen.getMyRunners().add(runner);
	}

	@Override
	public IStorageRunner getRunner() {
		return runner;
	}

	@Override
	public void setScreen(Screen s) {
		screen = s;
	}

	private boolean isAtStorageBuilding() {
		return runner.checkCollision(currentStorageBuilding, runner.getX(), runner.getY()).isCollision;
	}

	private boolean isArrivedAtHome() {
		return runner.checkCollision(this, runner.getX(), runner.getY()).isCollision;
	}

	private void setRunnerPathStorageBuilding() {
		int newX = (int) (currentStorageBuilding.getX() + 0.5 * currentStorageBuilding.getWidth()
				- 0.5 * runner.getWidth());
		int newY = (int) (currentStorageBuilding.getY() + 0.5 * currentStorageBuilding.getHeight()
				- 0.5 * runner.getHeight());
		runner.setMovingDestination(new MovingDestination(newX, newY));
		List<IFigure> myProds = new LinkedList<IFigure>(screen.getMyProductionFigures());
		myProds.remove(this);
		runner.calculatePath(screen.getMaxX(), screen.getMaxY(), screen.getAdditionalBlockingObjects(), myProds);
		runnerOnTheWayStorage = true;
		runnerAtHome = false;
	}

	private void setRunnerPathToHome() {
		int newX = (int) (x + 0.5 * width - 0.5 * runner.getWidth());
		int newY = (int) (y + 0.5 * height - 0.5 * runner.getHeight());
		runner.setMovingDestination(new MovingDestination(newX, newY));
		List<IFigure> myBuildings = new LinkedList<IFigure>(screen.getMyProductionFigures());
		myBuildings.addAll(screen.getMyResidentBuildings());
		myBuildings.remove(this);
		runner.calculatePath(screen.getMaxX(), screen.getMaxY(), screen.getAdditionalBlockingObjects(), myBuildings);
		runnerAtStorage = false;
		runnerOnTheWayHome = true;
	}

	@Override
	public void setProductionAmount(int i) {
		productionAmount = i;
	}

	@Override
	public int getProductionAmount() {
		return productionAmount;
	}

	@Override
	public Map<IStorageRunner, IStorageRunnerPackage> getStorageRunnerPackages() {
		return storageRunnerPackages;
	}

	@Override
	public IStorageRunnerPackage getStorageRunnerPackage(IStorageRunner isr) {
		return storageRunnerPackages.get(isr);
	}

	@Override
	public List<IStorageBuilding> getAssociatedStorageBuildings() {
		return associatedStorageBuildings;
	}

	@Override
	public void addStorageBuilding(IStorageBuilding isb) {
		associatedStorageBuildings.add(isb);
	}

	private void sendStorageRunnerOrderRequests(Ressource r, int amount) {
		System.out.println("resident order request " + r + " " + amount + " from " + this);
		if (associatedStorageBuildings.size() > 0) {
			Collections.shuffle(associatedStorageBuildings);

			if (currentRessources.get(r) != null) {
				if (currentRessources.get(r) + amount > currentRessourcesStorageLimit.get(r))
					amount = currentRessourcesStorageLimit.get(r) - currentRessources.get(r);

				IStorageBuilding isb = associatedStorageBuildings.get(0);
				if (!(isb.getStorage().get(r) - isb.getReservedStorage().get(r) >= amount)) {
					return;
				}

				if (pendingOrderRequest.get(r) == null) {
					pendingOrderRequest.put(r, 0);
				}

				pendingOrderRequest.put(r, pendingOrderRequest.get(r) + amount);

				IStorageBuildingRequest isbr = new StorageBuildingRequest(amount, r, this,
						StorageBuildingRequestType.RESIDENTORDER);
				isb.addRequest(isbr);
				System.out.println("amount" + amount);
			}
		}
	}

	@Override
	public Map<Ressource, Integer> getCurrentRessourcesStorageLimit() {
		return currentRessourcesStorageLimit;
	}

	@Override
	public int takeProducedMoney() {
		int ret = producedMoney;
		producedMoney = 0;
		return ret;
	}
}
