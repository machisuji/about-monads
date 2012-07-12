package about.monads

package object scala {
	def time[T](block: => T): (T, Long) = {
		val start = System.nanoTime
		val result = block
		val ms = (System.nanoTime - start) / 1000000
		(result, ms)
	}
}