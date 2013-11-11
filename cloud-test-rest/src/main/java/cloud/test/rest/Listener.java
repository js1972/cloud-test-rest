package cloud.test.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.bridge.SLF4JBridgeHandler;

@WebListener
public class Listener implements ServletContextListener {
	public Listener() {
	}

	public void contextInitialized(ServletContextEvent sce) {
		// Optionally remove existing handlers attached to j.u.l root logger
	    SLF4JBridgeHandler.removeHandlersForRootLogger(); // (since SLF4J 1.6.5)
	    
	    // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
	    // the initialization phase of your application
		SLF4JBridgeHandler.install();
	}

	public void contextDestroyed(ServletContextEvent sce) {
		SLF4JBridgeHandler.uninstall();
	}
}
