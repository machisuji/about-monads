package about.monads.scala

import java.util.Scanner

/**
 * Accepts expressions such as:
 *
 *   <expression> ::= "(" <expression> <operator> <expression> ")" | <number>
 *   <operator>   ::= "+" | "-" | "*" | "/"
 *   <number>     ::= ...
 */
object ExpressionAcceptor {

  def expr(tokens: List[String]): Step = (Accept(tokens) >>=
    open >>= expr >>= operator >>= expr >>= close) orElse (Accept(tokens) >>= number)

	def number(tokens: List[String]): Step = tokens match {
    case token::rest if new Scanner(token).hasNextInt => Accept(rest)
    case _ => Reject("Expected number, got: " + tokens.headOption.mkString, tokens)
  }

  def operator(tokens: List[String]): Step = tokens match {
    case token::rest if token.size == 1 && "+-*/".contains(token) => Accept(rest)
    case _ => Reject("Expected operator, got: " + tokens.headOption.mkString, tokens)
  }

  def open(tokens: List[String]): Step = tokens match {
    case token::rest if token == "(" => Accept(rest)
    case _ => Reject("Expected \"(\", got: " + tokens.headOption.mkString, tokens)
  }

  def close(tokens: List[String]): Step = tokens match {
    case token::rest if token == ")" => Accept(rest)
    case _ => Reject("Expected \")\", got: " + tokens.headOption.mkString, tokens)
  }
}
