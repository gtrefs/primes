package de.gtrefs.util;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Stream<T> {
	
	final static IntPredicate isPrime = x -> rangeClosed(2, (int) (Math.sqrt(x))).allMatch(n -> x % n != 0);
	
	static <T> Cons<T> cons(Supplier<T> head, Supplier<Stream<T>> rest){
		return new Cons<>(head, rest);
	}
	
	static <T> Stream<T> empty(){
		return StreamSupport.empty();
	}
	
	static <T> Stream<T> constant(T u){
		return cons(() -> u, () -> constant(u));
	}
	
	static Stream<Integer> from(int start){
		return cons(() -> start, () -> from(start + 1));
	}
	
	static Stream<Integer> primes(){
		return Stream.from(1).filter(isPrime::test);
	}
	
	static Stream<Integer> rangeClosed(int i, int j){
		if(j < i) return empty();
		return cons(() -> i, () -> i<j ? rangeClosed(i+1, j) : empty());
	}
	
	default Stream<T> filter(Predicate<T> p){
		return foldRight(() -> Stream.<T>empty(), (el, stream) -> p.test(el)? cons(() -> el, stream) : stream.get());
	}
	
	default <U> U foldRight(Supplier<U> z, BiFunction<T, Supplier<U>, U> f){
		if(this instanceof Cons){
			final Cons<T> self = (Cons<T>) this;
			return f.apply(self.head.get(), () -> self.tail.get().foldRight(z, f));
		}
		return z.get();
	}
	
	default boolean noneMatch(Predicate<T> p){
		return filter(p) == empty();
	}
	
	default boolean anyMatch(Predicate<T> p){
		return filter(p) != empty();
	}
	
	default boolean allMatch(Predicate<T> p){
		return noneMatch(p.negate());
	}

	default Stream<T> limit(int i){
		if(i == 0) return empty();
		if(this instanceof Cons){
			final Cons<T> self = (Cons<T>) this;
			return cons(self.head, () -> self.tail.get().limit(i-1));
		}
		return empty();
	}
	
	public class Cons<T> implements Stream<T>{
		public final Supplier<T> head;
		public final Supplier<Stream<T>> tail;

		public Cons(Supplier<T> head, Supplier<Stream<T>> rest) {
			this.tail = rest;
			this.head = head;
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object obj) {
			if(obj == null) return false;
			if(!(obj instanceof Cons)) return false;
			final Cons other = (Cons) obj;
			return Objects.equals(this.head.get(), other.head.get()) 
					&& Objects.equals(this.tail.get(), other.tail.get());
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(head.get(), tail.get());
		}
		
		@Override
		public String toString() {
			return head.get().toString()+", "+tail.get().toString();
		}
	}

	class StreamSupport {
		@SuppressWarnings("rawtypes")
		private static final Stream EMPTY = new Stream(){
			public String toString() {
				return "EMPTY";
			};
		}; 
		
		@SuppressWarnings("unchecked")
		public static <T> Stream<T> empty(){
		   return (Stream<T>) EMPTY; 
		}

	}

}
