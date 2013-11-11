package cloud.test.rest;

import static org.junit.Assert.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import javax.servlet.ServletException;

import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.exporter.zip.ZipExporterImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;

public class ResourceInTomcatTest {
	private static final String workingDir = System.getProperty("java.io.tmpdir");
	
	private Tomcat embeddedTomcat;
	
	@Before
	public void setup() throws IOException, LifecycleException, ServletException {
		embeddedTomcat = new Tomcat();
		embeddedTomcat.setPort(findFreePort());
		embeddedTomcat.setBaseDir(workingDir);
		
		Host host = embeddedTomcat.getHost();
		if (host instanceof StandardHost) {
			((StandardHost) host).setUnpackWARs(false);
		}
		host.setAppBase(workingDir);
		host.setAutoDeploy(true);
		host.setDeployOnStartup(true);
		
		//build archive
		WebArchive archive = ShrinkWrap.create(WebArchive.class);
		archive.addPackage(Resource.class.getPackage());
		
		File webApp = new File(workingDir, archive.getName());
		webApp.deleteOnExit();
		new ZipExporterImpl(archive).exportTo(webApp, true);
		
		String contextName = "/jason";  //something different to cloud-test to ensure the url pattern is not hard-coded anywhere
		embeddedTomcat.addWebapp(contextName, webApp.getAbsolutePath());
		
		embeddedTomcat.start();
	}

	@After
	public final void teardown() throws Throwable {
		if (embeddedTomcat.getServer() != null && embeddedTomcat.getServer().getState() != LifecycleState.DESTROYED) {
			if (embeddedTomcat.getServer().getState() != LifecycleState.STOPPED) {
				embeddedTomcat.stop();
			}
			embeddedTomcat.destroy();
		}
	}
	
	@Test
	public void test() throws IOException, InterruptedException, ExecutionException {
		@SuppressWarnings("resource")
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		ListenableFuture<Response> f = asyncHttpClient.prepareGet("http://localhost:" + embeddedTomcat.getConnector().getPort() + "/jason/resources/hello").execute();
		Response r = f.get();
		
		assertEquals(200, r.getStatusCode());
		assertEquals("Hello, World!", r.getResponseBody());
	}
	
	private static class SocketHolder implements Closeable {
		private final ServerSocket socket;
		
		public SocketHolder(ServerSocket socket) {
			this.socket = socket;
		}
		
		public ServerSocket getSocket() {
			return socket;
		}
		
		@Override
		public void close() throws IOException {
			socket.close();
		}
	}
	
	private static int findFreePort() throws IOException {
		try (SocketHolder holder = new SocketHolder(new ServerSocket(0))) {
			holder.getSocket().setReuseAddress(true);
			int port = holder.getSocket().getLocalPort();
			return port;
		}
	}
}
