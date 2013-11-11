package cloud.test.rest;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

@WebServlet(
		urlPatterns = "/resources/*", 
		initParams = @WebInitParam(name = "javax.ws.rs.Application", 
								   value = "cloud.test.rest.Application"))
public class Servlet extends ServletContainer {
	private static final long serialVersionUID = 2555264403197579468L;

	// Servlet containers typically use the Class.newInstance() method to load servlets,
	// so you must be careful to add an explicit default constructor if you add non-default
	// constructors.
	public Servlet() {
	}
	
	public Servlet(ResourceConfig resourceConfig) {
		super(resourceConfig);
	}
}
