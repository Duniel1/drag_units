package de.daniel.home.drag_units_2.krap;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import de.daniel.home.drag_units_2.krap.Definitions.Ressource;
import de.daniel.home.drag_units_2.krap.Definitions.StorageBuildingRequestType;

public class StorageBuilding extends Figure implements IStorageBuilding {

	private int randomizeRequests = 5;
	private int randomizeRequestsCounter = 0;
	private boolean hasRadius;
	private int radiusSize;
	private int capacity;
	private Map<Definitions.Ressource, Integer> storage = new HashMap<>();
	private Map<Definitions.Ressource, Integer> reservedStorage = new HashMap<>();
	private List<IStorageRunner> runners = new LinkedList<>();
	private List<IStorageRunner> runnersOnTheWayList = new LinkedList<>();
	private int maxNumOfRunners;
	private List<IProductionFigure> associatedProductionFigures = new ArrayList<>();
	private Screen screen;
	private List<IStorageBuildingRequest> requestQueue = new LinkedList<>();

	public StorageBuilding(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	@Override
	public void initProductionFiguresInRadius(List<IProductionFigure> ipf) {
		associatedProductionFigures = new ArrayList<IProductionFigure>();
		for (IProductionFigure currentIpf : ipf) {
			if (!currentIpf.isProductionDirect()) {
				int middleX = (int) (x + 0.5 * width);
				int middleY = (int) (y + 0.5 * height);
				int middleXProd = (int) (currentIpf.getX() + 0.5 * currentIpf.getWidth());
				int middleYProd = (int) (currentIpf.getY() + 0.5 * currentIpf.getHeight());
				int xDiff = Math.abs(middleX - middleXProd);
				int yDiff = Math.abs(middleY - middleYProd);
				int distance = (int) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
				if (distance <= radiusSize) {
					associatedProductionFigures.add(currentIpf);
					currentIpf.addStorageBuilding(this);
				}
			}
		}
	}

	@Override
	public int getRessource(Ressource r) {
		Integer i = storage.get(r);
		if (i == null)
			return 0;
		else
			return i;
	}

	@Override
	public Map<Ressource, Integer> getRessources() {
		return storage;
	}

	@Override
	public List<IStorageRunner> getRunners() {
		return runners;
	}

	@Override
	public int getNumberOfRunners() {
		return runners.size() + runnersOnTheWayList.size();
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
	public void calculateStorage() {
		if(--randomizeRequestsCounter <= 0){
			randomizeRequestsCounter = randomizeRequests;
			System.out.println("shuffle req1: " + requestQueue);
			Collections.shuffle(requestQueue);
			System.out.println("shuffle req2: " + requestQueue);
		}
		// Start runners
		for (IStorageBuildingRequest isbr : requestQueue) {
			if (isbr.isTaken() || isbr.getType() == StorageBuildingRequestType.RESIDENTORDER) {
				continue;
			}

			if (runners.size() == 0) {
				break;
			}

			IStorageRunner currentISR = runners.get(0);
			runners.remove(0);
			runnersOnTheWayList.add(currentISR);
			currentISR.setStorageBuildingRequest(isbr);
			currentISR.setPathToProdFig(screen);

			if (isbr.getType() == StorageBuildingRequestType.ORDER) {
				System.out.println("take order " + isbr + " from " + isbr.getProductionFigure());
				storage.put(isbr.getRessource(), storage.get(isbr.getRessource()) - isbr.getAmount());
				reservedStorage.put(isbr.getRessource(), reservedStorage.get(isbr.getRessource()) - isbr.getAmount());
			}

		}

		List<IStorageRunner> rightBackHome = new ArrayList<>();

		// check runners
		for (IStorageRunner isr : runnersOnTheWayList) {
			if (isr.isRunnerOnTheWayProdFig()) {
				if (isr.isAtProdFig()) {
					isr.setRunnerAtHome(false);
					isr.setRunnerAtProdFig(true);
					isr.setRunnerOnTheWayHome(false);
					isr.setRunnerOnTheWayProdFig(false);
					IProductionFigure theTarget = isr.getStorageBuildingRequest().getProductionFigure();
					theTarget.handleStorageRunner(isr);
				}
			} else if (isr.isRunnerOnTheWayHome()) {
				if (isr.isAtHome()) {
					IStorageBuildingRequest isbr = isr.getStorageBuildingRequest();

					if (isbr.getType() == StorageBuildingRequestType.PRODUCTION) {
						if (storage.get(isbr.getRessource()) == null) {
							storage.put(isbr.getRessource(), 0);
						}
						storage.put(isbr.getRessource(), storage.get(isbr.getRessource()) + isbr.getAmount());
					}

					requestQueue.remove(isr.getStorageBuildingRequest());
					rightBackHome.add(isr);
				}
			}
		}

		// justify runners and runnersOnTheWayList
		for (IStorageRunner isr : rightBackHome) {
			runners.add(isr);
			runnersOnTheWayList.remove(isr);
		}
	}

	@Override
	public void setMaxNumOfRunners(int i) {
		maxNumOfRunners = i;
	}

	@Override
	public int getMaxNumOfRunners() {
		return maxNumOfRunners;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public void setCapacity(int i) {
		capacity = i;
	}

	@Override
	public int getNumberOfRunnersOnTheWay() {
		return runnersOnTheWayList.size();
	}

	@Override
	public boolean addRunner(IStorageRunner isr) {
		if (runners == null)
			runners = new LinkedList<IStorageRunner>();
		if (runners.size() < maxNumOfRunners) {
			screen.getMyRunners().add(isr);
			runners.add(isr);
			isr.setStorageBuilding(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean addRunner(BufferedImage biNormal, BufferedImage biLeft, BufferedImage biRight, BufferedImage biUp,
			BufferedImage biDown, int width, int height, int speed, int capacity) {
		int runnerX = (int) (this.x + 0.5 * this.width - 0.5 * width);
		int runnerY = (int) (this.y + 0.5 * this.height - 0.5 * height);
		IStorageRunner runner = new StorageRunner(runnerX, runnerY, width, height);
		runner.setImageReference(biNormal);
		runner.setSpeed(speed);
		runner.setUpBImageReference(biUp);
		runner.setDownBImageReference(biDown);
		runner.setRightBImageReference(biRight);
		runner.setLeftBImageReference(biLeft);
		return addRunner(runner);
	}

	@Override
	public void setScreen(Screen s) {
		screen = s;
	}

	@Override
	public boolean addRequest(IStorageBuildingRequest isbr) {
		System.out.println("addRequest " + isbr + " from " + isbr.getProductionFigure());
		requestQueue.add(isbr);
		if(isbr.getType() == StorageBuildingRequestType.ORDER || isbr.getType() == StorageBuildingRequestType.RESIDENTORDER){
			if(reservedStorage.get(isbr.getRessource()) == null){
				reservedStorage.put(isbr.getRessource(), 0);
			}
			System.out.println("order req: " + isbr.getRessource() + " " + isbr.getAmount());
			reservedStorage.put(isbr.getRessource(), reservedStorage.get(isbr.getRessource()) + isbr.getAmount());
		}
		System.out.println("addRequest ENDE");
		return true;
	}

	@Override
	public Map<Ressource, Integer> getStorage() {
		return storage;
	}

	@Override
	public Map<Ressource, Integer> getReservedStorage() {
		return reservedStorage;
	}

	@Override
	public void handleRunner(IStorageRunner isr) {
		Ressource res = isr.getStorageBuildingRequest().getRessource();
		int amount = isr.getStorageBuildingRequest().getAmount();
		storage.put(res, storage.get(res) - amount);
		reservedStorage.put(res, reservedStorage.get(res) - amount);
	}
}
