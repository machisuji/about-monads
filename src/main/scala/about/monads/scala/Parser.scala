package about.monads.scala

import java.util.Scanner

object Parser {

  /**
   * Accepts expressions such as:
   *
   *   <expression> ::= "(" <expression> <operator> <expression> ")" | <number>
   *   <operator>   ::= "+" | "-" | "*" | "/"
   *   <number>     ::= ...
   */
  def expr(value: Option[String], tokens: List[String]): Step = {
    val input = Accept(value, tokens)
    val expression = input >>= open >>= expr >>= operator >>= expr >>= close
    
    (expression >>= calculate) orElse (input >>= number)
  }

  def number(value: Option[String], tokens: List[String]): Step = tokens match {
    case token::rest if new Scanner(token).hasNextInt =>
      Accept(value.map(_ + token) orElse Some(token), rest)
    case _ =>
      Reject("Expected number, got: " + tokens.headOption.mkString, tokens)
  }

  def operator(value: Option[String], tokens: List[String]): Step = tokens match {
    case token::rest if token.size == 1 && "+-*/".contains(token) =>
      Accept(value.map(_ + " " + token + " "), rest)
    case _ =>
      Reject("Expected operator, got: " + tokens.headOption.mkString, tokens)
  }

  def open(value: Option[String], tokens: List[String]): Step = tokens match {
    case "("::rest => Accept(value, rest)
    case _ => Reject("Expected \"(\", got: " + tokens.headOption.mkString, tokens)
  }

  def close(value: Option[String], tokens: List[String]): Step = tokens match {
    case ")"::rest => Accept(value, rest)
    case _ => Reject("Expected \")\", got: " + tokens.headOption.mkString, tokens)
  }

  def calculate(value: Option[String], tokens: List[String]): Step = {
    val Op = """(.* )?(\-?\d+) ([\+\-\*\/]) (\-?\d+)$""".r
    val result = value match {
      case Some(Op(prefix, a, op, b)) => {
        val fun: (Int, Int) => Int = op match {
          case "+" => _ + _
          case "-" => _ - _
          case "*" => _ * _
          case "/" => _ / _
        }
        val result = Seq(a, b).map(_.toInt).reduce(fun).toString
        Some(if (prefix ne null) prefix + result else result)
      }
      case _ => None
    }
    result.map(res =>
      Accept(Some(res), tokens)).getOrElse(
        Reject("Expected expression, got: \"" + value, tokens))
  }
}
