package de.gtrefs.util;

import static de.gtrefs.util.Stream.*;
import static de.gtrefs.util.Stream.StreamSupport.empty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.gtrefs.util.Stream.Cons;

public class StreamShould {
	
	@Test
	public void beMadeOfLazyCons(){
		Cons<String> one = cons(() -> "Just one", Stream::empty);
		assertThat(one.head.get(), is("Just one"));
		assertThat(one.tail.get(), is(empty()));
	}
	
	@Test
	public void beEmptyWhenAllElementsAreFiltered(){
		final Stream<String> empty = cons(() -> "one", () -> cons(() -> "second", Stream::empty)).filter("nope"::equals);
		assertThat(empty, is(empty()));
	}
	
	@Test
	public void containOnlyElementsWhichAreNotFiltered(){
		final Stream<String> empty = cons(() -> "one", () -> cons(() -> "second", Stream::empty)).filter("one"::equals);
		assertThat(empty, is(cons(() -> "one", Stream::empty)));
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
		Stream<Integer> ones = cons(() -> 1, () -> cons(() -> 3, Stream::empty));
		
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
	public void matchIfElementsOfStreamContains2(){
		Stream<Integer> stream = from(1);
		
		boolean match = stream.anyMatch(i -> i == 2);
		
		assertThat(match, is(true));
	}
	
	@Test
	public void matchShouldBeTheOppositeOfNoneMatch(){
		Stream<Integer> stream = cons(() -> 2, () -> cons(() -> 4, Stream::empty));
		
		boolean match = stream.anyMatch(i -> i % 2 == 0);
		boolean noneMatch = stream.noneMatch(i -> i % 2 == 0);
	
		assertThat(match, is(not(noneMatch)));
	}
	
	@Test
	public void allMatchShouldMatchIfThereIsNoElementWhereThePredicateDoesNotHold(){
		Stream<Integer> stream = cons(() -> 2, () -> cons(() -> 4, Stream::empty));
		
		boolean match = stream.allMatch(i -> i % 2 == 0);
		boolean noneMatch = stream.noneMatch(i -> i % 2 == 0);
	
		assertThat(match, is(not(noneMatch)));
	}
	
	@Test
	public void generateStreamWithinRange(){
		Stream<Integer> streamRange = Stream.rangeClosed(8,9);
		Stream<Integer> manualStream = cons(() -> 8, () -> cons(() -> 9, Stream::empty));
		
		assertThat(streamRange, is(manualStream));
	}
	
	@Test
	public void generateRangeWithLowerBoundInclusiveAndUpperBoundInclusive(){
		Stream<Integer> streamRange = Stream.rangeClosed(1, 3);
		boolean lowerBoundIsOne = streamRange.anyMatch(i -> i == 1);
		boolean upperBoundIsNot3 = streamRange.noneMatch(i -> i >= 4);
		
		assertThat(lowerBoundIsOne, is(true));
		assertThat(upperBoundIsNot3, is(true));
	}
	
	@Test
	public void generateEmptyRangeIfUpperBoundIsLowerThanLowerBound(){
		assertThat(Stream.rangeClosed(3, 2), is(empty()));
	}
	
	@Test
	public void generateRangeWithOneElementIfUpperBoundIsowerBound(){
		assertThat(Stream.rangeClosed(3, 3), is(cons(() -> 3, Stream::empty)));
	}
	
	@Test
	public void limitInfiniteStreamTo1(){
		Stream<Integer> limited = constant(1).limit(1);
		
		assertThat(limited, is(cons(() -> 1, Stream::empty)));
	}
	
	@Test
	public void limitInfiniteStreamTo2(){
		Stream<Integer> limited = constant(1).limit(2);
		
		Cons<Integer> expected = cons(() -> 1, () -> cons(() -> 1, Stream::empty));
		
		assertThat(limited, is(expected));
	}
	
	@Test
	public void limitInfiteStreamToEmptyWhenLimitIs0(){
		Stream<Integer> limited = constant(1).limit(0);
		
		assertThat(limited, is(empty()));
	}
	
	@Test
	public void limitEmptyToEmpty(){
		assertThat(empty().limit(10), is(empty()));
	}
	
	@Test
	public void limitFrom1To10ShouldBeTheSameAsRangeClosedFrom1To10(){
		assertThat(from(1).limit(10), is(rangeClosed(1, 10)));
	}

	@Test
	public void containOnlyPrimesWhenIfPrimeStream(){
		Cons<Integer> primes = (Cons<Integer>) Stream.primes();
		final List<Integer> expectedPrimes = Arrays.asList(1,2,3,5,7); 
		for (Integer prime: expectedPrimes) {
			assertThat(primes.head.get(), is(prime));
			primes = (Cons<Integer>) primes.tail.get();
		}
	}
}
