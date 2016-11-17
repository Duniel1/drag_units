package de.daniel.home.drag_units_2.krap;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.daniel.home.drag_units_2.krap.Definitions.Building;
import de.daniel.home.drag_units_2.krap.Definitions.Ressource;
import de.daniel.home.drag_units_2.krap.Definitions.StorageBuildingRequestType;

public class ProductionFigure extends Figure implements IProductionFigure {
	private List<Ressource> ressources;
	private Map<Ressource, Integer> productionDelay;
	private Map<Ressource, Integer> productionTimeLeft;
	private Map<Ressource, Integer> finishedRessources;
	private Map<Ressource, Map<Ressource, Integer>> neededRessources;
	private Map<Ressource, Integer> currentRessources;
	private Map<Ressource, Integer> currentRessourcesStorageLimit;
	private boolean isProductionDirect;
	private Map<Ressource, Integer> producedRessourcesStorageLimit;
	private Map<Ressource, Integer> producedRessourcesStorage;
	private Map<Ressource, Integer> reservedRessources;
	private double productivity = 1;
	private Building type;
	private int productionAmount = 1;
	private Map<IStorageRunner, IStorageRunnerPackage> storageRunnerPackages;
	private List<IStorageBuilding> associatedStorageBuildings;
	private Map<Ressource, Integer> pendingOrderRequest;
	private List<IStorageBuildingRequest> rejectedStorageBuildingRequest;

	private Screen screen;
	private boolean hasRadius;
	private int radiusSize;
	private List<IRessourceField> associatedRessourceFields;
	private IRessourceField currentRessourceField;
	private IInteractingFigure runner;
	private boolean isRunnerOnTheWayRessource = false;
	private boolean isRunnerOnTheWayHome = false;
	private boolean runnerOnRessourceField = false;
	private boolean runnerAtHome = true;
	private boolean runnerAtHomeWaiting = true;
	private boolean cameFromRessource = false;
	private int workOnFieldLeft;
	private Ressource radiusRessource;

	public ProductionFigure(double x, double y, double width, double height) {
		super(x, y, width, height);
		productionDelay = new HashMap<>();
		productionTimeLeft = new HashMap<>();
		finishedRessources = new HashMap<>();
		neededRessources = new HashMap<>();
		currentRessources = new HashMap<>();
		currentRessourcesStorageLimit = new HashMap<>();
		producedRessourcesStorageLimit = new HashMap<>();
		producedRessourcesStorage = new HashMap<>();
		reservedRessources = new HashMap<>();
		storageRunnerPackages = new HashMap<>();
		associatedStorageBuildings = new ArrayList<>();
		pendingOrderRequest = new HashMap<>();
		rejectedStorageBuildingRequest = new ArrayList<>();
	}

	@Override
	public Map<Ressource, Integer> getFinishedRessources() {
		return finishedRessources;
	}

	@Override
	public void calculateRessources() {
		if (!hasRadius) {
			for (Entry<Ressource, Integer> entry : productionTimeLeft.entrySet()) {
				Ressource curRessource = entry.getKey();
				boolean canProduce = true;
				if (neededRessources.get(curRessource) != null) {
					for (Entry<Ressource, Integer> currentResForToProdRes : neededRessources.get(curRessource)
							.entrySet()) {
						if (!(currentRessources.get(currentResForToProdRes.getKey()) >= currentResForToProdRes
								.getValue())) {
							canProduce = false;
						}
						// request für benötigte resourcen:
						if (pendingOrderRequest.get(currentResForToProdRes.getKey()) == null) {
							pendingOrderRequest.put(currentResForToProdRes.getKey(), 0);
						}
						if ((currentRessources.get(currentResForToProdRes.getKey()) + pendingOrderRequest
								.get(currentResForToProdRes.getKey())) < (currentRessourcesStorageLimit
										.get(currentResForToProdRes.getKey()))) {
							sendStorageRunnerOrderRequests(currentResForToProdRes.getKey(), 1);
						}
					}
				}

				int currentRessourcesStorageNewValue = productionAmount;
				if (!isProductionDirect && producedRessourcesStorage.get(curRessource) != null) {
					currentRessourcesStorageNewValue = producedRessourcesStorage.get(curRessource) + productionAmount;
					if (producedRessourcesStorageLimit.get(curRessource) < currentRessourcesStorageNewValue) {
						canProduce = false;
					}
				}

				if (canProduce) {
					int curLeft = entry.getValue() - 1;
					if (curLeft == 0) {
						if (neededRessources.get(curRessource) != null) {
							for (Entry<Ressource, Integer> currentResForToProdRes : neededRessources.get(curRessource)
									.entrySet()) {
								currentRessources.put(currentResForToProdRes.getKey(),
										currentRessources.get(currentResForToProdRes.getKey())
												- currentResForToProdRes.getValue());
							}
						}
						productionTimeLeft.put(curRessource, (int) (productionDelay.get(curRessource) / productivity));
						if (isProductionDirect) {
							finishedRessources.put(curRessource, productionAmount);
						} else {
							producedRessourcesStorage.put(curRessource, currentRessourcesStorageNewValue);
							sendStorageRunnerRequests(curRessource, productionAmount);
						}
					} else {
						productionTimeLeft.put(entry.getKey(), curLeft);
						finishedRessources.put(curRessource, 0);
					}
				}
			}
		} else {
			if (isRunnerOnTheWayRessource) {
				if (isAtRessourceField()) {
					isRunnerOnTheWayRessource = false;
					runnerOnRessourceField = true;
					workOnFieldLeft = currentRessourceField.getEffort();
				}
			} else if (runnerOnRessourceField) {
				workOnFieldLeft--;
				if (workOnFieldLeft == 0) {
					if (!currentRessourceField.cutting()) {
						// screen.getRessourceFields().remove(currentRessourceField);
						currentRessourceField.setIsEmpty(true);
						associatedRessourceFields.remove(currentRessourceField);
						currentRessourceField = null;
					}
					setRunnerPathToHome();
				}
			} else if (isRunnerOnTheWayHome) {
				if (isArrivedAtHome()) {
					isRunnerOnTheWayHome = false;
					runnerAtHome = true;
					runnerAtHomeWaiting = true;
					for (Entry<Ressource, Integer> entry : productionTimeLeft.entrySet()) {
						Ressource curRessource = entry.getKey();
						if (radiusRessource == curRessource) {
							productionTimeLeft.put(curRessource,
									(int) (productionDelay.get(curRessource) / productivity));
							break;
						}
					}
				}
			} else if (runnerAtHome) {
				if (runnerAtHomeWaiting) {
					for (Entry<Ressource, Integer> entry : productionTimeLeft.entrySet()) {
						Ressource curRessource = entry.getKey();
						if (radiusRessource == curRessource) {

							boolean canProduce = true;
							int currentRessourcesStorageNewValue = productionAmount;
							if (producedRessourcesStorage.get(curRessource) == null) {
								producedRessourcesStorage.put(curRessource, 0);
								currentRessourcesStorageNewValue = productionAmount;
							}

							if (!isProductionDirect) {
								currentRessourcesStorageNewValue = producedRessourcesStorage.get(curRessource)
										+ productionAmount;
								if (producedRessourcesStorageLimit
										.get(curRessource) < currentRessourcesStorageNewValue) {
									canProduce = false;
								}
							}
							if (canProduce) {
								int curLeft = entry.getValue() - 1;
								if (curLeft == 0) {
									productionTimeLeft.put(curRessource,
											(int) (productionDelay.get(curRessource) / productivity));

									if (cameFromRessource) {
										if (isProductionDirect) {
											finishedRessources.put(curRessource, productionAmount);
										} else {
											producedRessourcesStorage.put(curRessource,
													currentRessourcesStorageNewValue);
											sendStorageRunnerRequests(curRessource, productionAmount);
										}
										cameFromRessource = false;
									}

									if (currentRessourcesStorageNewValue < producedRessourcesStorageLimit
											.get(curRessource)) {
										if (currentRessourceField == null) {
											if (associatedRessourceFields.size() > 0) {
												setCurrentRessourceRandom();
												if (currentRessourceField != null)
													setRunnerPathToCurrentRessource();
											} else {
												initAssociatedRessourceFields(screen.getRessourceFields(),
														radiusRessource);
											}
										} else {
											setRunnerPathToCurrentRessource();
										}
									}
								} else {
									productionTimeLeft.put(entry.getKey(), curLeft);
									finishedRessources.put(curRessource, 0);
								}
							}
						}
						break;
					}
				}
			}
		}
		handleRejectedStorageRunnerRequests();
	}

	@Override
	public Map<Ressource, Integer> getProductionDelay() {
		return productionDelay;
	}

	@Override
	public void setProductionDelay(Map<Ressource, Integer> i) {
		productionDelay = i;
	}

	@Override
	public Map<Ressource, Integer> getProductionTimeLeft() {
		return productionTimeLeft;
	}

	@Override
	public void setProductionTimeLeft(Map<Ressource, Integer> i) {
		productionTimeLeft = i;
	}

	@Override
	public List<Ressource> getRessources() {
		return ressources;
	}

	@Override
	public void setRessources(List<Ressource> l) {
		ressources = l;
	}

	@Override
	public Map<Ressource, Map<Ressource, Integer>> getNeededRessources() {
		return neededRessources;
	}

	@Override
	public Map<Ressource, Integer> getNeededRessource(Ressource r) {
		return neededRessources.get(r);
	}

	@Override
	public void setNeededRessources(Ressource r, Map<Ressource, Integer> m) {
		neededRessources.put(r, m);
	}

	@Override
	public Map<Ressource, Integer> getCurrentRessources() {
		return currentRessources;
	}

	@Override
	public int getCurrentRessource(Ressource r) {
		return currentRessources.get(r);
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
	public boolean isProductionDirect() {
		return isProductionDirect;
	}

	@Override
	public void setIsProductionDirect(boolean b) {
		isProductionDirect = b;
	}

	@Override
	public Map<Ressource, Integer> getProducedRessourcesStorageLimit() {
		return producedRessourcesStorageLimit;
	}

	@Override
	public int getProducedRessourceStorageLimit(Ressource r) {
		return producedRessourcesStorageLimit.get(r);
	}

	@Override
	public void setProducedRessourcesStorageLimit(Map<Ressource, Integer> m) {
		producedRessourcesStorageLimit = m;
	}

	@Override
	public Map<Ressource, Integer> getProducedRessourcesStorage() {
		return producedRessourcesStorage;
	}

	@Override
	public int getProducedRessourcesStorage(Ressource r) {
		return producedRessourcesStorage.get(r);
	}

	@Override
	public void setProductivity(double d) {
		productivity = d;
	}

	@Override
	public double getProductivity() {
		return productivity;
	}

	@Override
	public Building getType() {
		return type;
	}

	@Override
	public void setType(Building b) {
		type = b;
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
	public List<IRessourceField> getAssociatedRessourceFields() {
		return associatedRessourceFields;
	}

	@Override
	public void setAssociatedRessourceFields(List<IRessourceField> irfList) {
		associatedRessourceFields = irfList;
	}

	@Override
	public void initAssociatedRessourceFields(List<IRessourceField> rl, Ressource r) {
		radiusRessource = r;
		associatedRessourceFields = new ArrayList<IRessourceField>();
		for (IRessourceField irf : rl) {
			if (irf.getType() == r /* && !irf.isTaken() */) {
				int middleX = (int) (x + 0.5 * width);
				int middleY = (int) (y + 0.5 * height);
				int middleXRes = (int) (irf.getX() + 0.5 * irf.getWidth());
				int middleYRes = (int) (irf.getY() + 0.5 * irf.getHeight());
				int xDiff = Math.abs(middleX - middleXRes);
				int yDiff = Math.abs(middleY - middleYRes);
				int distance = (int) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
				if (distance <= radiusSize) {
					associatedRessourceFields.add(irf);
					// irf.take(true);
				}
			}
		}

	}

	@Override
	public void initRunner(BufferedImage biNormal, BufferedImage biLeft, BufferedImage biRight, BufferedImage biUp,
			BufferedImage biDown, int width, int height, int speed) {
		int runnerX = (int) (this.x + 0.5 * this.width - 0.5 * width);
		int runnerY = (int) (this.y + 0.5 * this.height - 0.5 * height);
		runner = new InteractingFigure(runnerX, runnerY, width, height);
		runner.setImageReference(biNormal);
		runner.setSpeed(speed);
		runner.setUpBImageReference(biUp);
		runner.setDownBImageReference(biDown);
		runner.setRightBImageReference(biRight);
		runner.setLeftBImageReference(biLeft);
		screen.getMyRunners().add(runner);
	}

	@Override
	public IInteractingFigure getRunner() {
		return runner;
	}

	@Override
	public void setScreen(Screen s) {
		screen = s;
	}

	private boolean isAtRessourceField() {
		return runner.checkCollision(currentRessourceField, runner.getX(), runner.getY()).isCollision;
	}

	private boolean isArrivedAtHome() {
		return runner.checkCollision(this, runner.getX(), runner.getY()).isCollision;
	}

	private void setCurrentRessourceRandom() {
		currentRessourceField = null;
		Collections.shuffle(associatedRessourceFields);
		for (IRessourceField irf : associatedRessourceFields) {
			if (!irf.isTaken()) {
				currentRessourceField = irf;
				irf.take(true);
				break;
			}
		}
	}

	private void setRunnerPathToCurrentRessource() {
		int newX = (int) (currentRessourceField.getX() + 0.5 * currentRessourceField.getWidth()
				- 0.5 * runner.getWidth());
		int newY = (int) (currentRessourceField.getY() + 0.5 * currentRessourceField.getHeight()
				- 0.5 * runner.getHeight());
		runner.setMovingDestination(new MovingDestination(newX, newY));
		List<IFigure> myProds = new LinkedList<IFigure>(screen.getMyProductionFigures());
		myProds.remove(this);
		runner.calculatePath(screen.getMaxX(), screen.getMaxY(), screen.getAdditionalBlockingObjects(), myProds);
		isRunnerOnTheWayRessource = true;
		runnerAtHome = false;
		cameFromRessource = false;
	}

	private void setRunnerPathToHome() {
		int newX = (int) (x + 0.5 * width - 0.5 * runner.getWidth());
		int newY = (int) (y + 0.5 * height - 0.5 * runner.getHeight());
		runner.setMovingDestination(new MovingDestination(newX, newY));
		List<IFigure> myProds = new LinkedList<IFigure>(screen.getMyProductionFigures());
		myProds.remove(this);
		runner.calculatePath(screen.getMaxX(), screen.getMaxY(), screen.getAdditionalBlockingObjects(), myProds);
		runnerOnRessourceField = false;
		isRunnerOnTheWayHome = true;
		cameFromRessource = true;
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

	private void sendStorageRunnerRequests(Ressource r, int amount) {
		System.out.println("sendStorageRunnerRequests " + r + " " + amount);
		boolean canSend = true;
		if (associatedStorageBuildings.size() > 0) {
			Collections.shuffle(associatedStorageBuildings);

			if (producedRessourcesStorage.get(r) == null)
				producedRessourcesStorage.put(r, 0);

			if (producedRessourcesStorage.get(r) < amount)
				amount = producedRessourcesStorage.get(r);

			IStorageBuilding isb = associatedStorageBuildings.get(0);
			if ((isb.getStorage().get(r) + amount) > isb.getCapacity()) {
				canSend = false;
			} else {
				Integer currentRessReserved = reservedRessources.get(r);
				if (currentRessReserved == null || currentRessReserved == 0) {
					reservedRessources.put(r, amount);
				} else {
					reservedRessources.put(r, amount + currentRessReserved);
				}

				IStorageBuildingRequest isbr = new StorageBuildingRequest(amount, r, this,
						StorageBuildingRequestType.PRODUCTION);
				isb.addRequest(isbr);
			}
		} else {
			canSend = false;
		}
		if (!canSend) {
			System.out.println("cant send");
			IStorageBuildingRequest isbr = new StorageBuildingRequest(amount, r, this,
					StorageBuildingRequestType.PRODUCTION);
			rejectedStorageBuildingRequest.add(isbr);
		}
		System.out.println("sendStorageRunnerRequests ENDE");
	}

	private void handleRejectedStorageRunnerRequests() {
		System.out.println("handle");
		if (rejectedStorageBuildingRequest != null) {
			List<IStorageBuildingRequest> dummy = new ArrayList<>(rejectedStorageBuildingRequest);
			rejectedStorageBuildingRequest.clear();
			for (IStorageBuildingRequest isbr : dummy) {
				System.out.println("handle rejected");
				sendStorageRunnerRequests(isbr.getRessource(), isbr.getAmount());
			}
		}
	}

	private void sendStorageRunnerOrderRequests(Ressource r, int amount) {
		System.out.println("order request " + r + " " + amount + " from " + this);
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
						StorageBuildingRequestType.ORDER);
				isb.addRequest(isbr);
				System.out.println("amount" + amount);
			}
		}
	}

	@Override
	public boolean handleStorageRunner(IStorageRunner isr) {
		IStorageBuildingRequest isbr = isr.getStorageBuildingRequest();
		if (isbr.getType() == StorageBuildingRequestType.PRODUCTION) {
			reservedRessources.put(isbr.getRessource(), reservedRessources.get(isbr.getRessource()) - isbr.getAmount());
			producedRessourcesStorage.put(isbr.getRessource(),
					producedRessourcesStorage.get(isbr.getRessource()) - isbr.getAmount());
		} else if (isbr.getType() == StorageBuildingRequestType.ORDER) {
			if (pendingOrderRequest.get(isbr.getRessource()) == null) {
				pendingOrderRequest.put(isbr.getRessource(), 0);
			}
			pendingOrderRequest.put(isbr.getRessource(),
					pendingOrderRequest.get(isbr.getRessource()) - isbr.getAmount());
			currentRessources.put(isbr.getRessource(), currentRessources.get(isbr.getRessource()) + isbr.getAmount());
		}
		isr.setPathToHome(screen);
		return false;
	}

	@Override
	public Map<Ressource, Integer> getCurrentRessourcesStorageLimit() {
		return currentRessourcesStorageLimit;
	}
}
