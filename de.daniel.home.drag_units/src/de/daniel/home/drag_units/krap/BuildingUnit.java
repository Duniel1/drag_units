//package de.daniel.home.drag_units.krap;
//
//import java.io.IOException;
//import java.util.List;
//
//public class BuildingUnit extends Building implements IBuildingUnit{
//
//	private IUnit unit;
//	public BuildingUnit(double a, double b, double c, double d) {
//		super(a, b, c, d);
//	}
//	
//	void setUnit(IUnit unit){
//		this.unit = unit;
//	}
//	
//	public IUnit getUnit(){
//		return unit;
//	}
//
//	@Override
//	public double getAttack() {
//		return unit.getAttack();
//	}
//
//	@Override
//	public void setAttack(double d) {
//		unit.setAttack(d);
//	}
//
//	@Override
//	public double getRange() {
//		return unit.getRange();
//	}
//
//	@Override
//	public void setRange(double d) {
//		unit.setRange(d);
//	}
//
//	@Override
//	public double getAttackSpeed() {
//		return unit.getAttackSpeed();
//	}
//
//	@Override
//	public void setAttackSpeed(double d) {
//		unit.setAttackSpeed(d);
//	}
//
//	@Override
//	public void fight() {
//		unit.fight();
//	}
//
//	@Override
//	public void setEnemy(IUnit u) {
//		unit.setEnemy(u);
//	}
//
//	@Override
//	public IUnit getEnemy() {
//		return unit.getEnemy();
//	}
//
//	@Override
//	public void checkIfEnemyInRange(List<IUnit> enemys) {
//		unit.checkIfEnemyInRange(enemys);
//	}
//
//	@Override
//	public void setIsDead(boolean b) {
//		unit.setIsDead(b);
//	}
//
//	@Override
//	public boolean isDead() {
//		return unit.isDead();
//	}
//
//	@Override
//	public void setLeftBImageFight(String image) throws IOException {
//		unit.setLeftBImageFight(image);
//	}
//
//	@Override
//	public void setRightBImageFight(String image) throws IOException {
//		unit.setRightBImageFight(image);
//	}
//
//	@Override
//	public void setUpBImageFight(String image) throws IOException {
//		unit.setUpBImageFight(image);
//	}
//
//	@Override
//	public void setDownBImageFight(String image) throws IOException {
//		unit.setDownBImageFight(image);
//	}
//
//	@Override
//	public void setSelectedEnemy(IUnit u) {
//		unit.setSelectedEnemy(u);
//	}
//
//	@Override
//	public IUnit getSelectedEnemy() {
//		return unit.getSelectedEnemy();
//	}
//
//}
