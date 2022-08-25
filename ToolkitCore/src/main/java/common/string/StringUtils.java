package common.string;

import java.util.Arrays;
import java.util.Objects;

public class StringUtils {

	public final static String concat(String ... args) {
		return concat(50, args);
	}
	
	public final static String concat(int stringLengthAverage, String ... args) {
		Objects.requireNonNull(args);
		if (stringLengthAverage <= 0) {
			throw new IllegalArgumentException("stringLengthAverage must be greater than zero.");
		}
		
		StringBuilder sb = new StringBuilder(args.length * stringLengthAverage);
		
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
		}
		
		return sb.toString();
	}
	
	public final static boolean isNullOrEmpty(String arg) {
		return arg == null || arg.isEmpty();
	}
	
	public final static boolean isNullOrEmptyOrWhiteSpaces(String arg) {
		return arg == null || arg.trim().isEmpty();
	}
	
	public final static String grammaticalInflectionByNumber(String text, char flagPlural, boolean isSingular) {
		char[] chars = new char[text.length()];
		char[] originalChars = text.toCharArray();
		
		int charsFound = 0;
		
		boolean skip = false;
		for (int i = 0; i < originalChars.length; i++) {
			if (skip) {
				skip = false;
			} else {
				char c = originalChars[i];
				if (c == flagPlural) {
					skip = true;
				} else {
					chars[charsFound++] = c;
				}
			}
		}
		
		return new String(originalChars, 0, charsFound);
	}
}
