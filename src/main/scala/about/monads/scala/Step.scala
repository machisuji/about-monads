package about.monads.scala

import java.util.StringTokenizer

/**
 * Parse Step monad.
 */
abstract sealed trait Step {
  val value: Option[String]
  val tokens: List[String]

  def >>=(f: (Option[String], List[String]) => Step): Step = flatMap(f)
  def flatMap(f: (Option[String], List[String]) => Step): Step
  def map(f: (Option[String], List[String]) => List[String]): Step

  // see ReaderT monad transformer aka Kleisli
  def orElse(step: Step): Step
}

object Step {
  def apply(tokens: String) = {
    val tk = new StringTokenizer(tokens, " +-*/()", true)
    Accept(None, Iterator.continually(tk).takeWhile(
      _.hasMoreTokens).map(_.nextToken).filter(" " !=).toList)
  }
}

case class Accept(value: Option[String], tokens: List[String]) extends Step {
  def flatMap(f: (Option[String], List[String]) => Step): Step = f(value, tokens)
  def map(f: (Option[String], List[String]) => List[String]): Step = Accept(f(value, tokens))
  def orElse(step: Step) = this
}

object Accept {
  def apply(tokens: List[String]): Accept = Accept(None, tokens)
}

case class Reject(error: String, tokens: List[String]) extends Step {
  val value = None

  def flatMap(f: (Option[String], List[String]) => Step): Step = this
  def map(f: (Option[String], List[String]) => List[String]) = this
  def orElse(step: Step) = step
}
