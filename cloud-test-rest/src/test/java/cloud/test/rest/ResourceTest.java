package cloud.test.rest;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResourceTest {
	@Test
	public void test() {
		assertEquals("Hello, World!", new Resource().getIt());
	}
}
