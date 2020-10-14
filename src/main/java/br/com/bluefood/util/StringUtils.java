package br.com.bluefood.util;

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
		
		//PasswordEncoder enconder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		//return enconder.encode(rawString);
		return rawString;
	}
}
