package util;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import main.Searchmain;


@WebListener
public class Scheduler implements ServletContextListener{

	private volatile ScheduledExecutorService executor;

	TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
	    	Searchmain mains = new Searchmain();
			mains.Updatecode();
			
		}
	};
	
    public void contextInitialized(ServletContextEvent sce)
    {
        executor = Executors.newScheduledThreadPool(2);
        executor.scheduleAtFixedRate(task, 0, 360, TimeUnit.MINUTES);
    }

    public void contextDestroyed(ServletContextEvent sce)
    {
        final ScheduledExecutorService executor = this.executor;

        if (executor != null)
        {
            executor.shutdown();
            this.executor = null;
        }
    }
	
}
