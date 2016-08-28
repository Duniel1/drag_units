package de.daniel.home.drag_units_2.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import de.daniel.home.drag_units_2.krap.Screen;
import de.daniel.home.drag_units_2.krap.ToolScreen;

public class Refresher {

	private static ScheduledExecutorService schedulerGraphics = Executors.newScheduledThreadPool(1);
	private static ScheduledFuture<?> refresherHandleGraphics;
	private static AtomicBoolean runningGraphics = new AtomicBoolean(false);
	private static ScheduledExecutorService schedulerProduction = Executors.newScheduledThreadPool(1);
	private static ScheduledFuture<?> refresherHandleProduction;
	private static AtomicBoolean runningProduction = new AtomicBoolean(false);
	private static Screen s;
	private static ShowPanel p;
	private static ToolScreen ts;
	
	public static void init(final ShowPanel p, final Screen s, final ToolScreen ts){
		Refresher.p = p;
		Refresher.s = s;
		Refresher.ts = ts;
	}

	public static void refreshGraphics() {
		final Runnable refresher = new Runnable() {
			public void run() {
				p.repaint();
				s.repaint();
				ts.repaint();
			}
		};
		    
		refresherHandleGraphics = schedulerGraphics.scheduleAtFixedRate(refresher, 0, 10, TimeUnit.MILLISECONDS);
		runningGraphics.compareAndSet(false, true);
	}	

	public static void endGraphics() {
		if (runningGraphics.getAndSet(false)) refresherHandleGraphics.cancel(true);
	}
	
	public static void resetGraphics(){
		p.reset();
	}
	
	public static void refreshProduction() {
		final Runnable refresher = new Runnable() {
			public void run() {
				s.calculateRessources();
			}
		};
		    
		refresherHandleProduction = schedulerProduction.scheduleAtFixedRate(refresher, 0, 1000, TimeUnit.MILLISECONDS);
		runningProduction.compareAndSet(false, true);
	}	

	public static void endProduction() {
		if (runningProduction.getAndSet(false)) refresherHandleProduction.cancel(true);
	}
	
	public static void reseProduction(){
//		p.reset();
	}
}
