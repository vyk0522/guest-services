package com.onejava;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class GuestDTOServicesApplicationTests {
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Test
	void contextLoads() {
		System.out.println(encoder.encode("password1"));
		System.out.println(bCryptPasswordEncoder.encode("password1"));
		System.out.println("$2y$11$pM1MdNaQWEOKC0uVgWZHV.Shn1zELaZNjhuFOwhQIRL.94ShTxNyS");
	}

}
