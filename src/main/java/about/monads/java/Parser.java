package about.monads.java;

import java.util.*;

public class Parser {

	/**
 	 * Accepts expressions such as:
 	 *
 	 *   <expression> ::= "(" <expression> <operator> <expression> ")" | <number>
 	 *   <operator>   ::= "+" | "-" | "*" | "/"
 	 *   <number>     ::= ...
 	 */
	public static String expr(LinkedList<String> tokens) throws ParseException {
		try {
			open(tokens);
			String exp1 = expr(tokens);
			String op = operator(tokens);
			String exp2 = expr(tokens);
			close(tokens);

			return calculate(exp1, op, exp2);
		} catch (ParseException e) {
			return number(tokens); // doesn't work, need to rollback tokens first!
		}
	}

	public static String number(LinkedList<String> tokens) throws ParseException {
		String token = tokens.peek();
		if (token == null) {
			throw new ParseException("Expected number, got EOI");
		}
		Scanner scanner = new Scanner(token);
		if (scanner.hasNextInt()) {
			return tokens.poll();
		} else {
			throw new ParseException("Expected number, got " + tokens.peek());
		}
	}

	public static String operator(LinkedList<String> tokens) throws ParseException {
		String token = tokens.peek();
		if (token == null) {
			throw new ParseException("Expected operator, got EOI");
		}
		if (token.length() == 1 && "+-*/".contains(token)) {
			return tokens.poll();
		} else {
			throw new ParseException("Expected operator, got " + token);
		}
	}

	public static String open(LinkedList<String> tokens) throws ParseException {
		String token = tokens.peek();
		if (token == null) {
			throw new ParseException("Expected (, got EOI");
		}
		if (token.equals("(")) {
			return tokens.poll();
		} else {
			throw new ParseException("Expected (, got " + token);
		}
	}

	public static String close(LinkedList<String> tokens) throws ParseException {
		String token = tokens.peek();
		if (token == null) {
			throw new ParseExcpetion("Expected ), got EOI");
		}
		if (token.equals(")")) {
			return tokens.poll();
		} else {
			throw new ParseException("Expected ), got " + token);
		}
	}

	public static String calculate(String exp1, String op, String exp2) throws ParseException {
		int a = new Scanner(exp1).nextInt();
		int b = new Scanner(exp2).nextInt();
		Integer result = null;
		
		if (op.equals("+")) {
			result = a + b;
		} else if (op.equals("-")) {
			result = a - b;
		} else if (op.equals("*")) {
			result = a * b;
		} else if (op.equals("/")) {
			result = a / b;
		}
		if (result != null) {
			return result.toString();
		} else {
			throw new ParseException("Unknown operator: " + op);
		}
	}

	public static LinkedList<String> tokenize(String input) {
		StringTokenizer st = new StringTokenizer(input, " +-*/()", true);
		LinkedList<String> tokens = new LinkedList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (!token.equals(" ")) {
				tokens.add(token);
			}
		}
		return tokens;
	}
}

class ParseException extends Exception {
	public ParseException(String message) {
		super(message);
	}
}
