package br.com.bluefood.util;

import java.util.Collection;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class StringUtils {

	public static boolean isEmpty(String str) {
		if(str == null) {
			return true;
		}
		
		return str.trim().length() == 0;
	}
	
	public static String encrypt(String rawString) {
		if(isEmpty(rawString)) {
			return null;
		}
		
		PasswordEncoder enconder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return enconder.encode(rawString);
	}
	
	public static String concatenate(Collection<String> strings) {
		if(strings == null || strings.isEmpty()) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		String delimiter = ", ";
		boolean first = true;
		
		for (String string : strings) {
			if(!first) {
				sb.append(delimiter);
			}
			
			sb.append(string);
			first = false;
		}
		
		return sb.toString();
	}
}
