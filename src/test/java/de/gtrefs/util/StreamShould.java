package de.gtrefs.util;

import static org.hamcrest.CoreMatchers.*;
import static de.gtrefs.util.Stream.StreamSupport.empty;
import static de.gtrefs.util.Stream.*;
import static org.junit.Assert.*;
import org.junit.Test;

import de.gtrefs.util.Stream;

public class StreamShould {
	
	@Test
	public void beMadeOfLazyCons(){
		Cons<String> one = cons(() -> "Just one", () -> empty());
		assertThat(one.head.get(), is("Just one"));
		assertThat(one.tail.get(), is(empty()));
	}
	
	@Test
	public void beEmptyWhenAllElementsAreFiltered(){
		final Stream<String> empty = cons(() -> "one", () -> cons(() -> "second", () -> empty())).filter("nope"::equals);
		assertThat(empty, is(empty()));
	}
	
	@Test
	public void containOnlyElementsWhichAreNotFiltered(){
		final Stream<String> empty = cons(() -> "one", () -> cons(() -> "second", () -> empty())).filter("one"::equals);
		assertThat(empty, is(cons(() -> "one", () -> empty())));
	}
	
	@Test
	public void containSameNumberIfConstantStream(){
		Cons<Integer> ones = (Cons<Integer>) constant(1);
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
	public void notMatchIfElementsOfStreamAreNot2(){
		Stream<Integer> ones = cons(() -> 1, () -> cons(() -> 3, () -> empty()));
		
		boolean noneMatch = ones.noneMatch(i -> i == 2);
		
		assertThat(noneMatch, is(true));
	}
	
	@Test
	public void matchIfElementofStreamIs2(){
		Stream<Integer> stream = from(1);
		
		boolean noneMatch = stream.noneMatch(i -> i == 2);
		
		assertThat(noneMatch, is(false));
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
