package site.code4fun;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class BcryptTest {
	String name;
	public static void main(String[] args) {
		String passDB = new BCryptPasswordEncoder().encode("123456");
		boolean decode = new BCryptPasswordEncoder().matches("123456", "$2a$10$TlALWvTp2CWGzNET9bJUwe13UPZraNkQGoyfzqiZynJVIt1wa1Ktm");
		System.out.println(decode);
		System.out.println(passDB);
	}
}
