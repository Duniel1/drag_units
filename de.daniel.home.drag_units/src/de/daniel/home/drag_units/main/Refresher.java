package de.daniel.home.drag_units.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Refresher {

	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private static ScheduledFuture<?> refresherHandle;
	private static AtomicBoolean running = new AtomicBoolean(false);
	private static ShowPanel p;
	
	public static void init(final ShowPanel p){
		Refresher.p = p;
	}

	public static void refresh() {
		final Runnable refresher = new Runnable() {
			public void run() {
				p.refresh();
			}
		};
		    
		refresherHandle = scheduler.scheduleAtFixedRate(refresher, 0, 10, TimeUnit.MILLISECONDS);
		running.compareAndSet(false, true);
	}	

	public static void end() {
		if (running.getAndSet(false)) refresherHandle.cancel(true);
	}
	
	public static void reset(){
		p.reset();
	}
}
