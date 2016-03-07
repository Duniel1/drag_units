package de.daniel.home.drag_units.krap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public class MovingFigure extends Figure implements IMovingFigure {

	private EnumMap<WallDefinition, Boolean> mapOnWall = new EnumMap<>(WallDefinition.class);
	private double fFactor = 1;
	protected double speed = 0;
	protected double ax;
	protected double ay;
	private double newAx;
	private double newAy;
	protected MovingDestination movingDestination = null;
	private EnumMap<Direction, BufferedImage> mapBImages = new EnumMap<>(Direction.class);
	private boolean isTransient;
	protected List<PathPosition> path;

	public MovingFigure(double x, double y, double width, double height) {
		super(x, y, width, height);

		this.mapOnWall.put(WallDefinition.TOP, false);
		this.mapOnWall.put(WallDefinition.RIGHT, false);
		this.mapOnWall.put(WallDefinition.BOTTOM, false);
		this.mapOnWall.put(WallDefinition.LEFT, false);
	}

	@Override
	public void setOnWall(WallDefinition wd, boolean b) {
		mapOnWall.put(wd, b);
	}

	@Override
	public double getFactor() {
		return fFactor;
	}

	@Override
	public void setFactor(double f) {
		fFactor = f;
	}

	@Override
	public double getAx() {
		return ax;
	}

	@Override
	public void setAx(double ax) {
		this.ax = ax;
	}

	@Override
	public double getAy() {
		return ay;
	}

	@Override
	public void setAy(double ay) {
		this.ay = ay;
	}

	@Override
	public MovingDestination getMovingDestination() {
		return movingDestination;
	}

	@Override
	public void setMovingDestination(MovingDestination destination) {
		movingDestination = destination;
	}

	@Override
	public boolean isOnWall(WallDefinition wd) {
		return mapOnWall.get(wd);
	}

	// @Override
	// public void move() {
	// x += ax;
	// y += ay;
	// }

	@Override
	public void setNewAx(double ax) {
		newAx = ax;
	}

	@Override
	public void setNewAy(double ay) {
		newAy = ay;
	}

	@Override
	public void finalizeA() {
		ax = newAx;
		ay = newAy;
	}

	@Override
	public double getNewAx() {
		return newAx;
	}

	@Override
	public double getNewAy() {
		return newAy;
	}

	@Override
	public void setLeftBImage(String image) throws IOException {
		mapBImages.put(Direction.LEFT, ImageIO.read(new File(image)));
	}

	@Override
	public void setRightBImage(String image) throws IOException {
		mapBImages.put(Direction.RIGHT, ImageIO.read(new File(image)));
	}

	@Override
	public void setUpBImage(String image) throws IOException {
		mapBImages.put(Direction.UP, ImageIO.read(new File(image)));
	}

	@Override
	public void setDownBImage(String image) throws IOException {
		mapBImages.put(Direction.DOWN, ImageIO.read(new File(image)));
	}

	@Override
	public BufferedImage getImage() {
		Direction d = null;
		if (ax == 0 && ay == 0) {
			return bImage;
		}

		if (Math.abs(ax) < Math.abs(ay)) {
			if (ay > 0)
				d = Direction.DOWN;
			else
				d = Direction.UP;
		} else {
			if (ax > 0)
				d = Direction.RIGHT;
			else
				d = Direction.LEFT;
		}
		return mapBImages.get(d);
	}

	@Override
	public void setIsTransient(boolean b) {
		isTransient = b;
	}

	@Override
	public boolean isTransient() {
		return isTransient;
	}

	@Override
	public void setSpeed(double i) {
		speed = i;
	}

	@Override
	public double getSpeed() {
		return speed;
	}

	@Override
	public void calculatePath(List<IFigure> obstacles, int maxX, int maxY) {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				int nodecount = 0;
				long millis = System.currentTimeMillis();
				double dMinCost = 9999;
				double currentCost = 0;
				boolean bFound = false;

				LinkedList<PathPosition> dummyPath = new LinkedList<PathPosition>();
				LinkedList<PathNode> nodes = new LinkedList<PathNode>();
				LinkedList<PathPosition> visited = new LinkedList<PathPosition>();
				LinkedList<PathPosition> neighbours;

				PathNode newNode = new PathNode();
				PathNode parentNode = new PathNode();
				PathNode minCostNode = null;

				parentNode.setPosition(new PathPosition(x, y, 0, 0));
				parentNode.setParent(null);
				parentNode.setCost(
						getCost(x, y, movingDestination.getPosition().getX(), movingDestination.getPosition().getY()));

				nodes.add(parentNode);

				do {
					dMinCost = 9999;
					for (PathNode node : nodes) {
						currentCost = node.getCost();
						if (currentCost < dMinCost) {
							dMinCost = currentCost;
							minCostNode = node;
						}
					}

					nodes.remove(minCostNode);
					parentNode = minCostNode;
					neighbours = new LinkedList<>();

					Position newP = parentNode.getPosition();
					for (IFigure fig : obstacles) {
					if (newP.getX() + speed < maxX) {
						boolean collides = false;
						for (IFigure fig : obstacles) {
							if (new Figure(newP.getX() + speed, newP.getY(), width, height).checkCollisionRight(fig,
									newP.getX(), newP.getY())) {
								collides = true;
								break;
							}
						}
						if (!collides) {
							PathPosition nPP = new PathPosition(newP.getX() + speed, newP.getY(), speed, 0);
							if (!checkIfAlreadyVisited(visited, nPP))
								neighbours.add(nPP);
						}
					}

					if (newP.getX() - speed > 0) {
						boolean collides = false;
						for (IFigure fig : obstacles) {
							if (new Figure(newP.getX() - speed, newP.getY(), width, height).checkCollisionLeft(fig,
									newP.getX(), newP.getY())) {
								collides = true;
								break;
							}
						}
						if (!collides) {
							PathPosition nPP = new PathPosition(newP.getX() - speed, newP.getY(), -1 * speed, 0);
							if (!checkIfAlreadyVisited(visited, nPP))
								neighbours.add(nPP);
						}
					}

					if (newP.getY() + speed < maxY) {
						boolean collides = false;
						for (IFigure fig : obstacles) {
							if (new Figure(newP.getX(), newP.getY() + speed, width, height).checkCollisionBottom(fig,
									newP.getX(), newP.getY())) {
								collides = true;
								break;
							}
						}
						if (!collides) {
							PathPosition nPP = new PathPosition(newP.getX(), newP.getY() + speed, 0, speed);
							if (!checkIfAlreadyVisited(visited, nPP))
								neighbours.add(nPP);
						}
					}

					if (newP.getY() - speed > 0) {
						boolean collides = false;
						for (IFigure fig : obstacles) {
							if (new Figure(newP.getX(), newP.getY() - speed, width, height).checkCollisionTop(fig,
									newP.getX(), newP.getY())) {
								collides = true;
								break;
							}
						}
						if (!collides) {
							PathPosition nPP = new PathPosition(newP.getX(), newP.getY() - speed, 0, -1 * speed);
							if (!checkIfAlreadyVisited(visited, nPP))
								neighbours.add(nPP);
						}
					}
					}

					PathPosition pc5 = getNextStep(newP.getX(), newP.getY());
					if (pc5 != null) {
						boolean collides = false;
						for (IFigure fig : obstacles) {
							Collision c = new Figure(pc5.getX(), pc5.getY(), width, height).checkCollision(fig,
									newP.getX(), newP.getY());
							if (c.isCollision) {
								collides = true;
								break;
							}
						}
						if (!collides) {
							if (!checkIfAlreadyVisited(visited, pc5))
								neighbours.add(pc5);
						}
					}

					for (PathPosition pos : neighbours) {
						PathNode n = new PathNode();
						n.setPosition(pos);
						n.setParent(parentNode);
						n.setCost(getCost(n.getPosition().getX(), n.getPosition().getY(),
								movingDestination.getPosition().getX(), movingDestination.getPosition().getY()));
						nodecount++;
						nodes.add(n);
						visited.add(pos);

						if (n.getCost() < speed) {
							PathNode n2 = new PathNode();
							n2.setPosition(new PathPosition(movingDestination.getPosition().getX(),
									movingDestination.getPosition().getY(), speed, 0));
							n2.setParent(n);
							n.setCost(0);
							newNode = n2;
							bFound = true;
							break;
						}
					}
				} while ((nodes.size() > 0) && bFound == false);

				while (newNode != null) {
					dummyPath.add(0, newNode.getPosition());
					newNode = newNode.getParent();
				}
				path = dummyPath;
				System.out.println(nodecount);
				System.out.println(System.currentTimeMillis() - millis);
			}
		});

		th.start();
	}

	private double getCost(double x, double y, double x2, double y2) {
		return Math.sqrt((float) (Math.pow(x - x2, 2)) + (Math.pow(y - y2, 2)));
	}

	private PathPosition getNextStep(double x, double y) {
		Position nextPosition = movingDestination.getNextPosition(new Position(x, y));
		if (nextPosition != null) {
			double toMoveX = nextPosition.getX() - x;
			double toMoveY = nextPosition.getY() - y;
			double absToMoveX = Math.abs(toMoveX);
			double absToMoveY = Math.abs(toMoveY);
			double xCorrection = 1;
			double yCorrection = 1;

			if (toMoveX < 0)
				xCorrection = -1;
			if (toMoveY < 0)
				yCorrection = -1;

			if (toMoveY != 0) {
				double alpha = Math.atan(absToMoveX / absToMoveY);
				double moveX = Math.sin(alpha) * speed;
				double moveY = Math.cos(alpha) * speed;
				double newX = x + moveX * xCorrection;
				double newY = y + moveY * yCorrection;
				double ax = moveX * xCorrection;
				double ay = moveY * yCorrection;
				return new PathPosition(newX, newY, ax, ay);
			}
		}
		return null;
	}

	private boolean checkIfAlreadyVisited(List<PathPosition> visited, PathPosition pos) {
		for (PathPosition visitedPosition : visited) {
			if (visitedPosition.equals(pos)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void move() {
		if (path != null && path.size() != 0) {
			PathPosition p = path.remove(0);
			if (p != null) {
				x = p.getX();
				y = p.getY();
				ax = p.getAx();
				ay = p.getAy();
			}

			if (path.size() == 0) {
				movingDestination = null;
			}
		} else {
			ax = 0;
			ay = 0;
		}
	}
}
