package de.gtrefs.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

import de.gtrefs.util.Stream.Cons;

public class StreamShould {
	
	@Test
	public void beMadeOfLazyCons(){
		Cons<String> one = Stream.cons(() -> "Just one", () -> Stream.empty());
		assertThat(one.head.get(), is("Just one"));
		assertThat(one.tail.get(), is(Stream.StreamSupport.EMPTY));
	}
	
	@Test
	public void beEmptyWhenAllElementsAreFiltered(){
		final Stream<String> empty = Stream.cons(() -> "one", () -> Stream.cons(() -> "second", () -> Stream.empty())).filter("nope"::equals);
		assertThat(empty, is(Stream.StreamSupport.EMPTY));
	}
	
	@Test
	public void containOnlyElementsWhichAreNotFiltered(){
		final Stream<String> empty = Stream.cons(() -> "one", () -> Stream.cons(() -> "second", () -> Stream.empty())).filter("one"::equals);
		assertThat(empty, is(Stream.cons(() -> "one", () -> Stream.empty())));
	}
	
	@Test
	public void containSameNumberIfConstantStream(){
		Cons<Integer> ones = (Cons<Integer>) Stream.constant(1);
		for (int i = 0; i < 10; i++) {
			assertThat(ones.head.get(), is(1));
			ones = (Cons<Integer>) ones.tail.get();
		}
		
	}
	
	@Test
	public void containStreamOfIntegersIfFromStream(){
		Cons<Integer> numbers = (Cons<Integer>) Stream.from(0);
		for (int i = 0; i < 10; i++) {
			assertThat(numbers.head.get(), is(i));
			numbers = (Cons<Integer>) numbers.tail.get();
		}
	}
	
	@Test
	public void containOnlyPrimesWhenIfPrimeStream(){
		Cons<Integer> primes = (Cons<Integer>) Stream.primes();
		for (int i = 0; i < 20; i++) {
			System.out.println("Prime: "+i+" Value: "+primes.head.get());
			primes = (Cons<Integer>) primes.tail.get();
		}
	}
}
