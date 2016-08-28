package de.daniel.home.drag_units_2.krap;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import de.daniel.home.drag_units_2.krap.Definitions.Building;

public class ToolScreen extends JPanel {

	private int width;
	private int height;
	private JPanel leftPanel;
	private JPanel middlePanel;
	private JPanel rightPanel;
	private GridLayout totalLayout;
	private GridLayout leftLayout;
	private GridLayout middleLayout;
	private GridLayout rightLayout;
	private JLabel selectedFiguresInfo;
	private JLabel woodInfo;
	private JLabel goldInfo;
	private JLabel stoneInfo;
	private JLabel moneyInfo;
	private Screen screen;
	private JLabel resident;
	private JLabel society;
	private JLabel societyBack;
	private JLabel production;
	private JLabel productionBack;
	private JLabel military;
	private JLabel militaryBack;
	private JLabel goldMine;
	private JLabel lumberjack;
	private JPanel rightPanelLvl1;
	private JPanel rightPanelSociety;
	private JPanel rightPanelProduction;
	private JPanel rightPanelMilitary;

	public ToolScreen(int w, int h) {
		width = w;
		height = h;
		totalLayout = new GridLayout(0, 3);
		this.setLayout(totalLayout);
		
		leftLayout = new GridLayout(0, 1);
		middleLayout = new GridLayout(0, 1);
		rightLayout = new GridLayout(0, 1);
		
		leftPanel = new JPanel();
		middlePanel = new JPanel();
		rightPanel = new JPanel();
		
		society = new JLabel("Society");
		production = new JLabel("Production");
		military = new JLabel("Military");
		resident = new JLabel("Resident");
		societyBack = new JLabel("Back");
		lumberjack = new JLabel("Lumberjack");
		goldMine = new JLabel("Gold Mine");
		productionBack = new JLabel("Back");
		militaryBack = new JLabel("Back");
		
		initListeners();
		
		rightPanelLvl1 = new JPanel();
		rightPanelLvl1.setLayout(rightLayout);
		rightPanelLvl1.add(society);
		rightPanelLvl1.add(production);
		rightPanelLvl1.add(military);
		
		rightPanelSociety = new JPanel();
		rightPanelSociety.setLayout(rightLayout);
		rightPanelSociety.add(resident);
		rightPanelSociety.add(societyBack);
		
		rightPanelProduction = new JPanel();
		rightPanelProduction.setLayout(rightLayout);
		rightPanelProduction.add(lumberjack);
		rightPanelProduction.add(goldMine);
		rightPanelProduction.add(productionBack);
		
		rightPanelMilitary = new JPanel();
		rightPanelMilitary.setLayout(rightLayout);
		rightPanelMilitary.add(militaryBack);
		
		leftPanel.setLayout(leftLayout);
		middlePanel.setLayout(middleLayout);
		rightPanel.setLayout(rightLayout);
		
		Border outerBorder = BorderFactory.createLineBorder(Color.black);
		this.setBorder(outerBorder);
		
		leftPanel.setBorder(outerBorder);
		middlePanel.setBorder(outerBorder);
		rightPanel.setBorder(outerBorder);
		
		selectedFiguresInfo = new JLabel("selectedFiguresInfo");
		woodInfo = new JLabel("Wood: 0");
		goldInfo = new JLabel("Gold: 0");
		stoneInfo = new JLabel("Stone: 0");
		moneyInfo = new JLabel("Money: 0");
		
		leftPanel.add(moneyInfo);
		leftPanel.add(woodInfo);
		leftPanel.add(goldInfo);
		leftPanel.add(stoneInfo);
		middlePanel.add(selectedFiguresInfo);
		rightPanel.add(rightPanelLvl1);
		
		this.add(leftPanel);
		this.add(middlePanel);
		this.add(rightPanel);
	}
	
	private void initListeners(){
		society.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				rightPanel.remove(rightPanelLvl1);
				rightPanel.add(rightPanelSociety);
				rightPanel.revalidate();
			}
		});
		
		societyBack.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				rightPanel.remove(rightPanelSociety);
				rightPanel.add(rightPanelLvl1);
				rightPanel.revalidate();
				screen.deactivateBuildingMode();
			}
		});
		
		production.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				rightPanel.remove(rightPanelLvl1);
				rightPanel.add(rightPanelProduction);
				rightPanel.revalidate();
			}
		});
		
		productionBack.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				rightPanel.remove(rightPanelProduction);
				rightPanel.add(rightPanelLvl1);
				rightPanel.revalidate();
				screen.deactivateBuildingMode();
			}
		});
		
		military.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				rightPanel.remove(rightPanelLvl1);
				rightPanel.add(rightPanelMilitary);
				rightPanel.revalidate();
			}
		});
		
		militaryBack.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				rightPanel.remove(rightPanelMilitary);
				rightPanel.add(rightPanelLvl1);
				rightPanel.revalidate();
				screen.deactivateBuildingMode();
			}
		});
		
		resident.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				IBuildingInfo bi = new BuildingInfo(Building.RESIDENT, 50, 50);
				bi.setCost(100, 2, 1, 0);
				screen.buildBuilding(bi);
			}
		});
		
		lumberjack.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				IBuildingInfo bi = new BuildingInfo(Building.LUMBERJACK, 50, 50);
				bi.setCost(200, 1, 3, 0);
				bi.setRadiusSize(125);
				screen.buildBuilding(bi);
			}
		});
		
		goldMine.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				IBuildingInfo bi = new BuildingInfo(Building.GOLDMINE, 50, 50);
				bi.setCost(500, 4, 2, 0);
				screen.buildBuilding(bi);
			}
		});
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setSelectedFiguresInfo(String s){
		selectedFiguresInfo.setText(s);
	}
	
	public void setBuildBuildingInfo(String s){
		selectedFiguresInfo.setText(s);
	}
	
	public void setWoodInfo(int i){
		woodInfo.setText("Wood: " + i);
	}
	
	public void setGoldInfo(int i){
		goldInfo.setText("Gold: " + i);
	}
	
	public void setStoneInfo(int i){
		stoneInfo.setText("Stone: " + i);
	}
	
	public void setMoneyInfo(int i){
		moneyInfo.setText("Money: " + i);
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}
}
