A little example for one use case of monads used in The Functional Club meeting at 12th July 2012 in Berlin.

There are three implementations of a simple parser for mathmatical expressions such as (5 + 9).
Two Java implementations, one based on Exceptions, motivate the usage of a monad in the Scala implementation.

Punch this into your repl:
    import about.monads.scala.Parser_
    
    val input = Step("(5 + 9)")
    input >>= expr
