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
	public static Result expr(LinkedList<String> tokens) {
		Result open = open(tokens);
		if (open.isSuccess()) {
			Result exp1 = expr(tokens);
			if (exp1.isSuccess()) {
				Result op = operator(tokens);
				if (op.isSuccess()) {
					Result exp2 = expr(tokens);
					if (exp2.isSuccess()) {
						Result close = close(tokens);
						if (close.isSuccess()) {
							int a = new Scanner(exp1.getValue()).nextInt();
							int b = new Scanner(exp2.getValue()).nextInt();
							int c = 0;
							if (op.getValue().equals("+")) {
								c = a + b;
							} else if (op.getValue().equals("-")) {
								c = a - b;
							} else if (op.getValue().equals("*")) {
								c = a * b;
							} else if (op.getValue().equals("/")) {
								c = a / b;
							}
							return new Success("" + c);
						} else {
							return close;
						}
					} else {
						return exp2;
					}
				} else {
					return op;
				}
			} else {
				return exp1;
			}
		} else {
			Result number = number(tokens);
			if (number.isSuccess()) {
				return number;
			} else {
				return open;
			}
		}
	}

	public static Result number(LinkedList<String> tokens) {
		String token = tokens.peek();
		if (token == null) {
			return new Error("Expected number, got EOI");
		}
		Scanner scanner = new Scanner(token);
		if (scanner.hasNextInt()) {
			return new Success(tokens.poll());
		} else {
			return new Error("Expected number, got " + tokens.peek());
		}
	}

	public static Result operator(LinkedList<String> tokens) {
		String token = tokens.peek();
		if (token == null) {
			return new Error("Expected operator, got EOI");
		}
		if (token.length() == 1 && "+-*/".contains(token)) {
			return new Success(tokens.poll());
		} else {
			return new Error("Expected operator, got " + token);
		}
	}

	public static Result open(LinkedList<String> tokens) {
		String token = tokens.peek();
		if (token == null) {
			return new Error("Expected (, got EOI");
		}
		if (token.equals("(")) {
			tokens.poll();
			return new Success(null);
		} else {
			return new Error("Expected (, got " + token);
		}
	}

	public static Result close(LinkedList<String> tokens) {
		String token = tokens.peek();
		if (token == null) {
			return new Error("Expected ), got EOI");
		}
		if (token.equals(")")) {
			tokens.poll();
			return new Success(null);
		} else {
			return new Error("Expected ), got " + token);
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
