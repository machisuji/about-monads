package about.monads.scala

import java.util.StringTokenizer

/**
 * Parse Step monad.
 */
abstract sealed trait Step {
  val tokens: List[String]

  def >>=(f: List[String] => Step): Step = flatMap(f)
  def flatMap(f: List[String] => Step): Step
  def map(f: List[String] => List[String]): Step

  // see ReaderT monad transformer aka Kleisli
  def orElse(step: Step): Step
}

object Step {
  def apply(tokens: String) = {
    val tk = new StringTokenizer(tokens, " +-*/()", true)
    Accept(Iterator.continually(tk).takeWhile(
      _.hasMoreTokens).map(_.nextToken).filter(" " !=).toList)
  }
}

case class Accept(tokens: List[String]) extends Step {
  def flatMap(f: List[String] => Step): Step = f(tokens)
  def map(f: List[String] => List[String]): Step = Accept(f(tokens))
  def orElse(step: Step) = this
}

case class Reject(error: String, tokens: List[String]) extends Step {
  def flatMap(f: List[String] => Step): Step = this
  def map(f: List[String] => List[String]) = this
  def orElse(step: Step) = step
}
