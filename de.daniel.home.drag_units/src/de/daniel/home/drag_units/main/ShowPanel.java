package de.daniel.home.drag_units.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import de.daniel.home.drag_units.krap.Screen;
import de.daniel.home.drag_units.krap.Selector;
import de.daniel.home.drag_units.krap.ToolScreen;

public class ShowPanel extends JPanel {
	private static final long serialVersionUID = -8958621368965838316L;

	private Screen screen;
	private ToolScreen toolScreen;
	

	private JMenuBar jmbMenuBar;
	private JMenu jmStart;
	private JMenuItem jmiRefresh, jmiExit;

	public ShowPanel() {
		this.setLayout(null);
		screen = new Screen(800, 580);
		screen.setBounds(0, 20, 800, 580);
		toolScreen = new ToolScreen(800, 150);
		toolScreen.setBounds(0, 600, 800, 150);

		jmbMenuBar = new JMenuBar();
		jmStart = new JMenu("Start");
		jmbMenuBar.add(jmStart);
		jmiRefresh = new JMenuItem("Refresh");
		jmiExit = new JMenuItem("Exit");
		jmStart.add(jmiRefresh);
		jmStart.add(jmiExit);
		jmbMenuBar.setBounds(0, 0, 800, 20);
		this.add(jmbMenuBar);

		Refresher.init(this);
		Refresher.refresh();
		add(screen);
		add(toolScreen);

		jmiRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Refresher.end();
				Refresher.reset();
				Refresher.refresh();
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

	public void refresh() {
		this.repaint();
	}

}
