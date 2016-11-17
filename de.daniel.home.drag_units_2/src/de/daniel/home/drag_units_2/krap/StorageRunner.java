package de.daniel.home.drag_units_2.krap;

import java.util.LinkedList;
import java.util.List;

public class StorageRunner extends InteractingFigure implements IStorageRunner {

	private IStorageBuildingRequest storageBuildingRequest;
	private IStorageBuilding storageBuilding;
	private boolean isRunnerAtHome;
	private boolean isRunnerAtProdFig;
	private boolean isRunnerOnTheWayHome;
	private boolean isRunnerOnTheWayProdFig;
	private int capacity;

	public StorageRunner(double x, double y, double width, double height) {
		super(x, y, width, height);
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
	public boolean isRunnerOnTheWayProdFig() {
		return isRunnerOnTheWayProdFig;
	}

	@Override
	public void setRunnerOnTheWayProdFig(boolean b) {
		isRunnerOnTheWayProdFig = b;
	}

	@Override
	public boolean isRunnerOnTheWayHome() {
		return isRunnerOnTheWayHome;
	}

	@Override
	public void setRunnerOnTheWayHome(boolean b) {
		isRunnerOnTheWayHome = b;
	}

	@Override
	public boolean isRunnerAtProdFig() {
		return isRunnerAtProdFig;
	}

	@Override
	public void setRunnerAtProdFig(boolean b) {
		isRunnerAtProdFig = b;
	}

	@Override
	public boolean isRunnerAtHome() {
		return isRunnerAtHome;
	}

	@Override
	public void setRunnerAtHome(boolean b) {
		isRunnerAtHome = b;
	}

	@Override
	public IStorageBuildingRequest getStorageBuildingRequest() {
		return storageBuildingRequest;
	}

	@Override
	public void setStorageBuildingRequest(IStorageBuildingRequest isbr) {
		storageBuildingRequest = isbr;
		isbr.take();
	}

	@Override
	public void setPathToProdFig(Screen screen) {
		if (storageBuildingRequest != null) {
			int newX = (int) (storageBuildingRequest.getProductionFigure().getX() + 0.5 * storageBuildingRequest.getProductionFigure().getWidth()
					- 0.5 * this.width);
			int newY = (int) (storageBuildingRequest.getProductionFigure().getY() + 0.5 * storageBuildingRequest.getProductionFigure().getHeight()
					- 0.5 * this.height);
			setMovingDestination(new MovingDestination(newX, newY));
			List<IFigure> myProds = new LinkedList<IFigure>(screen.getMyProductionFigures());
			myProds.remove(storageBuildingRequest.getProductionFigure());
			calculatePath(screen.getMaxX(), screen.getMaxY(), screen.getAdditionalBlockingObjects(), myProds);
			isRunnerAtHome = false;
			isRunnerAtProdFig = false;
			isRunnerOnTheWayHome = false;
			isRunnerOnTheWayProdFig = true;
		}
	}

	@Override
	public boolean isAtProdFig() {
		if (storageBuildingRequest != null) {
			return checkCollision(storageBuildingRequest.getProductionFigure(), x, y).isCollision;
		}
		return false;
	}

	@Override
	public void setStorageBuilding(IStorageBuilding isb) {
		storageBuilding = isb;
	}

	@Override
	public IStorageBuilding getStorageBuilding() {
		return storageBuilding;
	}

	@Override
	public void setPathToHome(Screen screen) {
		int newX = (int) (storageBuilding.getX() + 0.5 * storageBuilding.getWidth()
				- 0.5 * this.width);
		int newY = (int) (storageBuilding.getY() + 0.5 * storageBuilding.getHeight()
				- 0.5 * this.height);
		setMovingDestination(new MovingDestination(newX, newY));
		List<IFigure> myProds = new LinkedList<IFigure>(screen.getMyProductionFigures());
		myProds.remove(storageBuildingRequest.getProductionFigure());
		calculatePath(screen.getMaxX(), screen.getMaxY(), screen.getAdditionalBlockingObjects(), myProds);
		isRunnerAtHome = false;
		isRunnerAtProdFig = false;
		isRunnerOnTheWayHome = true;
		isRunnerOnTheWayProdFig = false;
	}

	@Override
	public boolean isAtHome() {
		return checkCollision(storageBuilding, x, y).isCollision;
	}
}
