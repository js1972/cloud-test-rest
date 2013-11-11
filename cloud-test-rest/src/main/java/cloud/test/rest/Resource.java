package cloud.test.rest;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cloud.test.rest.model.Output;

@Path("hello")
public class Resource {
	private static Logger log = LoggerFactory.getLogger(Resource.class);
	
	public Resource() {
		log.info("Jersey Resource created!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
	
	@GET
	public String getIt() {
		return "Hello, World!";
	}

	@GET
	@Path("json")
	@Produces(MediaType.APPLICATION_JSON)
	public Output getItJson() {
		Output output = new Output();
		
		output.setId((long) 1);
		output.setName("test");
		output.setDate(new Date());
		
		return output;
	}
}
