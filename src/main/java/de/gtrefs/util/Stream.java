package de.gtrefs.util;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

public interface Stream<T> {
	
	IntPredicate isPrime = x -> rangeClosed(2, (int) (Math.sqrt(x))).allMatch(n -> x % n != 0);
	
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

	static <T> Stream<T> from(List<T> list){
		return unfold(list, l -> l.isEmpty() ? Optional.empty() : Optional.of(Pair.of(l.get(0), l.subList(1, l.size()))));
	}

	static <A, S> Stream<A> unfold(S z, Function<S, Optional<Pair<A,S>>> f) {
		final Optional<Pair<A,S>> next = f.apply(z);
		return next.map(p -> (Stream<A>) cons(() -> p.left, () -> unfold(p.right, f)))
				.orElseGet(Stream::empty);
	}

	default Stream<T> filter(Predicate<T> p){
		return foldRight(Stream::<T>empty, (el, stream) -> p.test(el)? cons(() -> el, stream) : stream.get());
	}

	default <S> Stream<S> map(Function<T, S> f){
		return foldRight(Stream::<S>empty, (el, stream) -> cons(() -> f.apply(el), stream));
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

	class Pair<L,R> {
		public final L left;
		public final R right;

		public Pair(L left, R right){
			this.left = left;
			this.right = right;
		}

		public static <L,R> Pair<L, R> of(L left, R right){
			return new Pair<>(left, right);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Pair<?, ?> pair = (Pair<?, ?>) o;

			if (left != null ? !left.equals(pair.left) : pair.left != null) return false;
			return right != null ? right.equals(pair.right) : pair.right == null;

		}

		@Override
		public int hashCode() {
			int result = left != null ? left.hashCode() : 0;
			result = 31 * result + (right != null ? right.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "Pair{" +
					"left=" + left +
					", right=" + right +
					'}';
		}
	}
	
	class Cons<T> implements Stream<T>{
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
			}
		}; 
		
		@SuppressWarnings("unchecked")
		public static <T> Stream<T> empty(){
		   return (Stream<T>) EMPTY; 
		}

	}
}
