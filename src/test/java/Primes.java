import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import org.junit.Test;

public class Primes {

	final IntPredicate isPrime = x -> IntStream.rangeClosed(2, (int) (Math.sqrt(x))).allMatch(n -> x % n != 0);
	final Function<Integer, IntStream> till = max -> IntStream.iterate(1, i -> i + 1).filter(isPrime).limit(max);

	@Test
	public void primesTill20() {
		till.apply(20).forEach(prime -> System.out.println("Prime: " + prime));
	}
}