A little example for one use case of monads used in The Functional Club meeting at 12th July 2012 in Berlin.

There are three implementations of a simple parser for mathmatical expressions such as (5 + 9).
Two Java implementations, one based on Exceptions, motivate the usage of a monad in the Scala implementation.
You can find the second Java implementation in the package `about.monads.java.ifelse`.

Punch this into your repl (sbt console; no project file required):

    import about.monads.scala._;
    import Parser._
    
    val input = Step("(((42 * 8) / 3) + (8 - 99))")
    input >>= expr

The result of which should be:

    res0: about.monads.scala.Step = Accept(Some(21),List())