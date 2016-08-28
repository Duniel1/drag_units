package de.daniel.home.drag_units_2.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import de.daniel.home.drag_units_2.krap.BackgroundScreen;
import de.daniel.home.drag_units_2.krap.Screen;
import de.daniel.home.drag_units_2.krap.Selector;
import de.daniel.home.drag_units_2.krap.ToolScreen;

public class ShowPanel extends JPanel {
	private static final long serialVersionUID = -8958621368965838316L;

	private Screen screen;
	private ToolScreen toolScreen;
	private BackgroundScreen backgroundScreen;

	private JMenuBar jmbMenuBar;
	private JMenu jmStart;
	private JMenuItem jmiRefresh, jmiExit;

	public ShowPanel() {
		this.setLayout(null);
		backgroundScreen = new BackgroundScreen(800, 580);
		backgroundScreen.setBounds(0, 20, 800, 580);
		screen = new Screen(800, 580);
		screen.setBounds(0, 20, 800, 580);
		toolScreen = new ToolScreen(800, 150);
		toolScreen.setBounds(0, 600, 800, 150);
		screen.setToolScreen(toolScreen);
		toolScreen.setScreen(screen);

		jmbMenuBar = new JMenuBar();
		jmStart = new JMenu("Start");
		jmbMenuBar.add(jmStart);
		jmiRefresh = new JMenuItem("Refresh");
		jmiExit = new JMenuItem("Exit");
		jmStart.add(jmiRefresh);
		jmStart.add(jmiExit);
		jmbMenuBar.setBounds(0, 0, 800, 20);
		this.add(jmbMenuBar);

		Refresher.init(this, screen, toolScreen);
		Refresher.refreshGraphics();
		Refresher.refreshProduction();
		
		add(backgroundScreen);
		add(screen);
		add(toolScreen);

		jmiRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Refresher.endGraphics();
				Refresher.resetGraphics();
				Refresher.refreshGraphics();
			}
		});

		jmiExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}

	public void reset() {
		this.remove(screen);
		this.revalidate();
		screen = new Screen(800, 580);
		screen.setBounds(0, 20, 800, 600);
		add(screen);
	}
}
