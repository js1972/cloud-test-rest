package cloud.test.rest;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.glassfish.jersey.filter.LoggingFilter;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class Application extends javax.ws.rs.core.Application {
	/**
	 * add Jersey resources - our JAX-RS annotated pojo
	 */
	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<>();
		
		classes.add(Resource.class);
		
		return classes;
	}
	
	/**
	 * configure Jersey logging
	 */
	@Override
	public Set<Object> getSingletons() {
		final HashSet<Object> instances = new HashSet<>();
		
		instances.add(new LoggingFilter(Logger.getLogger(Application.class.getName()), false));
		instances.add(new JacksonJsonProvider());
		
		return instances;
	}
}
