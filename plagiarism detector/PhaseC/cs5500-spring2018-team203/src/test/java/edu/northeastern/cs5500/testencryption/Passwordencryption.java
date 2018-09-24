package edu.northeastern.cs5500.testencryption;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import edu.northeastern.cs5500.encryption.PassSecure;

@RunWith(SpringRunner.class)
public class Passwordencryption {
	
	@Test
	public void testEncryption() {
		PassSecure ps=PassSecure.getInstance();
		String hash=ps.encrypt("mypassword");
		assertThat(true).isEqualTo(ps.isPass("mypassword", hash));
		
	}
	
	
	@Test
	public void testNegEncryption() {
		PassSecure ps=PassSecure.getInstance();
		String hash=ps.encrypt("mypassword");
		assertThat(false).isEqualTo(ps.isPass("notmypassword", hash));
		
	}
	
	

}
