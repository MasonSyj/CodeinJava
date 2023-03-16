package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Token {

//	String query;
// 	ArrayList<String> tokens;
	static String[] specialCharacters = {"(",")",",",";"};

	public static List<String> setup(String query) {
		List<String> tokens = new ArrayList<String>();
		// Remove any whitespace at the beginning and end of the query
		query = query.trim();
		// Split the query on single quotes (to separate out query characters from string literals)
		String[] fragments = query.split("'");
		for (int i = 0; i < fragments.length; i++) {
			// Every odd fragment is a string literal, so just append it without any alterations
			if (i % 2 != 0) tokens.add("'" + fragments[i] + "'");
				// If it's not a string literal, it must be query characters (which need further processing)
			else {
				// Tokenise the fragments into an array of strings
				String[] nextBatchOfTokens = tokenise(fragments[i]);
				// Then add these to the "result" array list (needs a bit of conversion)
				tokens.addAll(Arrays.asList(nextBatchOfTokens));
			}
		}

		// Finally, loop through the result array list, printing out each token a line at a time
		/*
		for (int i = 0; i < tokens.size(); i++) {
			System.out.println(tokens.get(i));
		}
		 */

		return tokens;
	}

	public static String[] tokenise(String input)
	{
		// Add in some extra padding spaces around the "special characters"
		// so we can be sure that they are separated by AT LEAST one space (possibly more)
		for(int i=0; i<specialCharacters.length ;i++) {
			input = input.replace(specialCharacters[i], " " + specialCharacters[i] + " ");
		}
		// Remove all double spaces (the previous replacements may had added some)
		// This is "blind" replacement - replacing if they exist, doing nothing if they don't
		while (input.contains("  ")) input = input.replaceAll("  ", " ");
		// Again, remove any whitespace from the beginning and end that might have been introduced
		input = input.trim();
		// Finally split on the space char (since there will now ALWAYS be a space between tokens)
		return input.split(" ");
	}

	public static void main(String[] args){

		String query = "  INSERT  INTO  people.233   VALUES(  'Simon Lock'  ,35, 'simon@bristol.ac.uk' , 1.8  ) ; ";

		List<String> first = Token.setup(query);

		String[] second = Token.tokenise(query);

		for (String str: first){
			System.out.println(str);
		}

		System.out.println("----------Separate Line--------");

		for (String str: second){
			System.out.println(str);
		}


	}
}


