package parser;

public class LexerHelper {

	/**
	 * Gets a String and returns the int of that String
	 * @param str String that is transformed to int
	 * @return String parsed to int
	 */
	public static int lexemeToInt(String str) {
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e) {
			System.err.println(e.getMessage());
		}
		return -1;
	}

	/**
	 * Gets a String and returns the char of that String
	 * @param str String that is transformed to char
	 * @return String parsed to char
	 */
	public static char lexemeToChar(String text) {
		String content = text.substring(1, text.length() - 1);

		if (content.equals("\\n")) return '\n';
		if (content.equals("\\t")) return '\t';

		if (content.startsWith("\\") && content.length() > 1 && Character.isDigit(content.charAt(1))) {
			return (char) Integer.parseInt(content.substring(1));
		}

		return content.charAt(0);
	}

	/**
	 * Gets a String and returns the double of that String
	 * @param str String that is transformed to double
	 * @return String parsed to double
	 */
	public static double lexemeToReal(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			System.err.println(e.getMessage());
		}
		return 0.0;
	}

}
