package de.daniel.home.drag_units_2.krap;

import java.util.List;

import de.daniel.home.drag_units_2.krap.Definitions.WallDefinition;

public class Calculator {
	
	private static int maxX;
	private static int maxY;
	
	public static void init(int maxX, int maxY){
		Calculator.maxX = maxX;
		Calculator.maxY = maxY;
	}

	public static void calculateNewPositions(List<IInteractingFigure> figures, List<IFigure> objects){
        for(IInteractingFigure fig: figures){
            int newX = (int) (fig.getX() + fig.getFactor() * fig.getAx());
            int newY = (int) (fig.getY() + fig.getFactor() * fig.getAy());

            Collision tempCollision;
            Collision completeCollision = new Collision();
            
            fig.setNewAx(fig.getAx());
            fig.setNewAy(fig.getAy());
            
            for(IInteractingFigure fig2: figures){
                if(fig.getId() != fig2.getId()){
                    tempCollision = fig.checkCollision(fig2, newX, newY);
                    if(tempCollision.isCollision) {
                        completeCollision.isCollision = true;
                        completeCollision.right |= tempCollision.right;
                        completeCollision.left |= tempCollision.left;
                        completeCollision.top |= tempCollision.top;
                        completeCollision.bottom |= tempCollision.bottom;
                        //fig.setNewAx(tempCollision.newAx);
                        //fig.setNewAy(tempCollision.newAy);
                        //fig.setAx(tempCollision.newAx);
                        //fig.setAy(tempCollision.newAy);
//                        System.out.println(fig.getId() + " vs " + completeCollision.versus + " : " + completeCollision.newAx + " " + completeCollision.newAy);

                        break;
                    }
                }
            }
            
            for(IFigure obj: objects){
                tempCollision = fig.checkCollision(obj, newX, newY);
                if(tempCollision.isCollision) {
                    completeCollision.isCollision = true;
                    completeCollision.right |= tempCollision.right;
                    completeCollision.left |= tempCollision.left;
                    completeCollision.top |= tempCollision.top;
                    completeCollision.bottom |= tempCollision.bottom;
                }
            }

            //move right
            if(!completeCollision.right){
                if(newX + fig.getWidth() > maxX){
                    fig.setX((int) (maxX - fig.getWidth()));
                    fig.setNewAx(-1 * fig.getAx());
                    if(!fig.isOnWall(WallDefinition.RIGHT)){
                        fig.setOnWall(WallDefinition.RIGHT, true);
                    }
                }
                else{
                    fig.setX(newX);
                    fig.setOnWall(WallDefinition.RIGHT, false);
                }
            }
            else{
            	fig.setNewAx(-1 * fig.getAx());
            }

            //move down
            if(!completeCollision.bottom){
                if(newY + fig.getHeight() > maxY){
                    fig.setY((int) (maxY - fig.getHeight()));
                    fig.setNewAy(-1 * fig.getAy());
                    if(!fig.isOnWall(WallDefinition.BOTTOM)){
                        fig.setOnWall(WallDefinition.BOTTOM, true);
                    }
                }
                else{
                    fig.setY(newY);
                    fig.setOnWall(WallDefinition.BOTTOM, false);
                }
            }
            else{
            	fig.setNewAy(-1 * fig.getAy());
            }

            //move up
            if(!completeCollision.top){
                if(newY < 0){
                    fig.setY(0);
                    fig.setNewAy(-1 * fig.getAy());
                    if(!fig.isOnWall(WallDefinition.TOP)){
                        fig.setOnWall(WallDefinition.TOP, true);
                    }
                }
                else if(newY + fig.getHeight() < maxY){
                    fig.setY(newY);
                    fig.setOnWall(WallDefinition.TOP, false);
                }
            }
            else{
            	fig.setNewAy(-1 * fig.getAy());
            }

            //move left
            if(!completeCollision.left){
                if(newX < 0){
                    fig.setX(0);
                    fig.setNewAx(-1 * fig.getAx());
                    if(!fig.isOnWall(WallDefinition.LEFT)){
                        fig.setOnWall(WallDefinition.LEFT, true);
                    }
                }
                else if(newX + fig.getWidth() < maxX){
                    fig.setX(newX);
                    fig.setOnWall(WallDefinition.LEFT, false);
                }
            }
            else{
            	fig.setNewAx(-1 * fig.getAx());
            }
        }
        
        for(IInteractingFigure fig: figures){
        	fig.finalizeA();
        }
        
    }
	
	public static void calculateNewPositionsUnits(List<IInteractingFigure> units){
		for(IInteractingFigure fig: units){
			fig.move();
		}
	}
	
	public static void checkEnemyInRange(List<IInteractingFigure> units, List<IInteractingFigure> enemyUnits){
		for(IInteractingFigure unit: units){
			unit.checkIfEnemyInRange(enemyUnits);
		}
		for(IInteractingFigure unit: enemyUnits){
			unit.checkIfEnemyInRange(units);
		}
	}
	
	public static void fight(List<IInteractingFigure> units, List<IInteractingFigure> enemyUnits){
		for(IInteractingFigure unit: units){
			unit.fight();
		}
		for(IInteractingFigure unit: enemyUnits){
			unit.fight();
		}
	}
	
	public static void calculateRessources(List<IProductionFigure> l){
		for(IProductionFigure ipf: l){
			ipf.calculateRessources();
		}
	}
}
