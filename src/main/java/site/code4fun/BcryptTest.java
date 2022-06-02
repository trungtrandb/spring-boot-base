package site.code4fun;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BcryptTest {
	public static void main(String[] args) {
		String passDB = new BCryptPasswordEncoder().encode("123456");
		boolean decode = new BCryptPasswordEncoder().matches("123456", "$2a$10$TlALWvTp2CWGzNET9bJUwe13UPZraNkQGoyfzqiZynJVIt1wa1Ktm");
		System.out.println(decode);
		System.out.println(passDB);
		
		List<String> emails = new ArrayList<>();
		emails.add("user@domain.com");
		emails.add("user@domain.co.in");
		emails.add("user1@domain.com");
		emails.add("user.name@domain.com");
		emails.add("user_name@domain.co.in");
		emails.add("user-name@domain.co.in");
		emails.add("user@domaincom");
		 
		//Invalid emails
		emails.add("@yahoo.com");
		 		 
		Pattern pattern = Pattern.compile("^[\\\\w!#$%&'*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$");
		 
		for(String email : emails){
		    Matcher matcher = pattern.matcher(email);
		    System.out.println(email +" : "+ matcher.matches());
		}
	}
}
