package org.afc.util;

import static java.util.stream.Collectors.*;
import static org.afc.util.OptionalUtil.*;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.afc.AFCException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

public class CollectionUtil extends org.springframework.util.CollectionUtils {

	@SuppressWarnings("squid:S1150")
	public static class ArrayEnumeration<T> implements Enumeration<T> {

		private final T[] array;

		private int cursor;

		private final int size;

		public ArrayEnumeration(T[] array) {
			this.array = array;
			this.size = array.length;
			this.cursor = 0;
		}

		@Override
		public boolean hasMoreElements() {
			return (cursor < size);
		}

		@Override
		public T nextElement() {
			return array[cursor++];
		}
	}

	@Data
	@AllArgsConstructor
	public static class Batch<T> {
		private int num;
		private List<T> values;
	}

	@AllArgsConstructor
	public static class CoStream<X, Y> {
		public final X x;
		public final Y y;
	}

	@SuppressWarnings("squid:S1150")
	public static class ListEnumeration<T> implements Enumeration<T> {

		private int cursor;

		private final List<T> list;

		private final int size;

		public ListEnumeration(List<T> list) {
			this.list = list;
			this.size = list.size();
			this.cursor = 0;
		}

		@Override
		public boolean hasMoreElements() {
			return (cursor < size);
		}

		@Override
		public T nextElement() {
			return list.get(cursor++);
		}
	}

	public static class Mergeable<K, V> {

		@Getter
		private final Map<K, V> map;

		private Mergeable() {
			this(new HashMap<>());
		}

		private Mergeable(Map<K, V> map) {
			super();
			this.map = map;
		}

		public Mergeable<K, V> merge(K key, V value) {
			if (key != null && value != null) {
				map.put(key, value);
			}
			return this;
		}

	}

	public static <T, K> List<K> attributes(List<T> list, Function<? super T, ? extends K> keyMapper) {
		return stream(list).map(keyMapper).collect(Collectors.toList());
	}

	public static <T> Stream<Batch<T>> batches(List<T> source, int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("length = " + length);
		}
		int size = source.size();
		if (size <= 0) {
			return Stream.empty();
		}

		int fullChunks = (size - 1) / length;
		return IntStream.range(0, fullChunks + 1).mapToObj(
			n -> new Batch<>(n, source.subList(n * length, n == fullChunks ? size : (n + 1) * length))
		);
	}

	public static boolean containAll(Collection<?> references, Collection<?> targets) {
		return targets.stream().allMatch(references::contains);
	}

	public static boolean containAny(Collection<?> references, Collection<?> targets) {
		return targets.stream().anyMatch(references::contains);
	}

	public static <X, Y> Stream<CoStream<X, Y>> coStream(Iterable<X> list1, Iterable<Y> list2) {
		List<CoStream<X, Y>> list = new LinkedList<>();
		Iterator<X> itr1 = list1 != null ? list1.iterator() : new ArrayList<X>().iterator();
		Iterator<Y> itr2 = list2 != null ? list2.iterator() : new ArrayList<Y>().iterator();
		while (itr1.hasNext() && itr2.hasNext()) {
			list.add(new CoStream<>(itr1.next(), itr2.next()));
		}
		while (itr1.hasNext()) {
			list.add(new CoStream<>(itr1.next(), null));
		}
		while (itr2.hasNext()) {
			list.add(new CoStream<>(null, itr2.next()));
		}
		return list.stream();
	}

	public static <E> int drain(BlockingQueue<E> from, Collection<E> to) {
		try {
			to.add(from.take());
			from.drainTo(to);
			return to.size();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new AFCException(e);
		}
	}

	public static <E> int drain(BlockingQueue<E> from, Collection<E> to, int maxElements) {
		try {
			to.add(from.take());
			from.drainTo(to, maxElements);
			return to.size();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new AFCException(e);
		}
	}

	public static <K, T> T ensure(Map<K, T> map, K key, Supplier<T> factory) {
		return	map.computeIfAbsent(key, k -> factory.get());
	}

	@SuppressWarnings("squid:S2445")
	public static <K, T> T ensureConcurrently(Map<K, T> map, K key, Supplier<T> factory) {
		T target = map.get(key);
		if (target == null) {
			synchronized(map) {
				target = ensure(map, key, factory);
			}
		}
		return target;
	}

	public static <K, V> Entry<K, V> entry(K key, V value) {
		return new SimpleEntry<>(key, value);
	}

	public static <T> T first(List<T> list) {
		if (isEmpty(list)) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public static <T, R> R first(List<T> list, Function<T, R> getter) {
		if (isEmpty(list)) {
			return null;
		} else {
			return getter.apply(list.get(0));
		}
	}

	@SafeVarargs
	public static <I extends Iterable<T>, T> I forEach(I itr, Consumer<T>... actions) {
		stream(itr).forEach(i -> {
			for (Consumer<T> action : actions) {
				action.accept(i);
			}
		});
		return itr;
	}

	@SafeVarargs
	public static <K, V> Map<K, V> forEach(Map<K, V> map, BiConsumer<K, V>... actions) {
		if (map != null && !map.isEmpty()) {
			map.forEach((k, v) -> {
				for (BiConsumer<K, V> action : actions) {
					action.accept(k, v);
				}
			});
		}
		return map;
	}

	public static <K, V> Map<K, List<V>> groupingMap(Iterable<V> itr, Function<V, K> keyMapper) {
		return StreamSupport.stream(itr.spliterator(), false).collect(groupingBy(keyMapper, toList()));
	}

	public static <T, K> T ifFoundThenConsume(List<T> sources, T target, Function<? super T, ? extends K> keyMapper, BiConsumer<? super T, ? super T> consumer) {
		K targetKey = keyMapper.apply(target);
		if (targetKey != null) {
			sources.stream()
			.filter(source -> targetKey.equals(keyMapper.apply(source)))
			.findFirst().ifPresent(e -> {
				consumer.accept(e, target);
			});
		}
		return target;
	}

	public static <S, T, K> T ifFoundThenConsume(Map<K, S> sources, T target, Function<? super T, ? extends K> keyMapper, BiConsumer<? super S, ? super T> consumer) {
		K targetKey = keyMapper.apply(target);
		if (targetKey != null) {
			S source = sources.get(targetKey);
			if (source != null) {
				consumer.accept(source, target);
			}
		}
		return target;
	}

	public static <T, K, R> R ifFoundThenMap(List<T> sources, T target, Function<? super T, ? extends K> keyMapper, BiFunction<? super T, ? super T, R> function) {
		K targetKey = keyMapper.apply(target);
		if (targetKey != null) {
			return sources.stream()
			.filter(source -> targetKey.equals(keyMapper.apply(source)))
			.findFirst().map(source -> {
				return function.apply(source, target);
			}).orElse(null);
		}
		return null;
	}

	public static <S, T, K, R> R ifFoundThenMap(Map<K, S> sources, T target, Function<? super T, ? extends K> keyMapper, BiFunction<? super S, ? super T, R> function) {
		K targetKey = keyMapper.apply(target);
		if (targetKey != null) {
			S source = sources.get(targetKey);
			if (source != null) {
				return function.apply(source, target);
			}
		}
		return null;
	}

	public static <C extends Collection<?>> void ifNotEmpty(C collection, Consumer<C> ifAction) {
		if (isNotEmpty(collection)) {
			ifAction.accept(collection);
		}
	}

	public static <C extends Collection<?>> void ifNotEmptyElse(C collection, Consumer<C> ifAction, Runnable elseAction) {
		if (isNotEmpty(collection)) {
			ifAction.accept(collection);
		} else {
			elseAction.run();
		}
	}

	public static <C extends Collection<?>> C iifNotEmpty(C collection) {
		return isNotEmpty(collection) ? collection : null;
	}

	public static <C extends Collection<?>, R> R iifNotEmptyElse(C collection, Function<C, R> ifReturn, Supplier<R> elseReturn) {
		return (isNotEmpty(collection)) ? ifReturn.apply(collection) : elseReturn.get();
	}

	public static <C extends Collection<?>> C iifNotEmptyElse(C collection, Supplier<C> elseReturn) {
		return isNotEmpty(collection) ? collection : elseReturn.get();
	}

	public static List<String> intersect(List<String> left, List<String> right) {
		return left.stream().filter(right::contains).collect(toList());
	}

	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.size() == 0);
	}

	public static <T extends Collection<?>> boolean isEntryValueEmpty(Map.Entry<?, T> entry) {
		return isEmpty(entry.getValue());
	}

	public static <T extends Collection<?>> boolean isEntryValueNotEmpty(Map.Entry<?, T> entry) {
		return !isEntryValueEmpty(entry);
	}

	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	@SafeVarargs
	public static <C extends Collection<T>, T> C join(Supplier<C> factory, C... colls) {
		C joined = factory.get();
		for (C coll : colls) {
			ifNotNull(coll, joined::addAll);
		}
		return joined;
	}

	/**
	 * merge source list to target list by checking key,
	 * 1. remove entity in target list if source not found
	 * 2. add source entity to target if key is not contained (null or w/ value),
	 * 3. invoke merger to perform manual merge if target has source's key
	 *
	 * @param <T> - entity type
	 * @param <K> - key typ
	 * @param sources - source to get merge
	 * @param targets - target to merge
	 * @param keyMapper - key identifier
	 * @param merger - manual merge if key conflict
	 * @return merged target stream
	 */
	@SuppressWarnings("squid:S1612")
	public static <T, K> Stream<T> keyedUpdate(List<T> sources, List<T> targets, Function<? super T, ? extends K> keyMapper, BiConsumer<? super T, ? super T> merger) {
		List<K> sourceKeys = attributes(sources, keyMapper);
		List<K> targetKeys = attributes(targets, keyMapper);

		/* delete */
		targets = iifNotNull(targets, () -> new LinkedList<>());
		targets.removeIf(target -> !sourceKeys.contains(keyMapper.apply(target)));

		/* add */
		stream(sources)
			.filter(source -> iifNotNullElse(keyMapper.apply(source),
				key -> !targetKeys.contains(key),
				() -> true
			))
			.forEach(targets::add);

		/* return merged */
		stream(targets).forEach(target -> ifFoundThenConsume(sources, target, keyMapper, merger));
		return targets.stream();
	}

	public static <T> T last(List<T> list) {
		if (isEmpty(list)) {
			return null;
		} else {
			return list.get(list.size() - 1);
		}
	}

	public static <T, R> R last(List<T> list, Function<T, R> getter) {
		if (isEmpty(list)) {
			return null;
		} else {
			return getter.apply(list.get(list.size() - 1));
		}
	}

	public static <T> List<T> list(Iterable<T> itr) {
		return StreamSupport.stream(itr.spliterator(), false).collect(toList());
	}

	@SafeVarargs
	@SuppressWarnings("squid:S3012")
	public static <L extends List<T>, T> L list(L list, T... values) {
		for (T value : values) {
			list.add(value);
		}
		return list;
	}

	@SafeVarargs
	public static <T> List<T> list(T... values) {
		return list(new ArrayList<T>(), values);
	}

	public static <K, V> Map<K, V> map(Iterable<V> itr, Function<V, K> keyMapper) {
		return stream(itr).collect(toMap(keyMapper, v -> v));
	}

	public static <O, K, V> Map<K, V> map(List<O> list, @NonNull Function<O, K> keyMapper, @NonNull Function<O, V> valueMapper) {
		if (list != null) {
			return list.parallelStream().collect(toMap(keyMapper, valueMapper, (a, b) -> a));
		}
		return new HashMap<>();
	}

	public static <O, K, V> Map<K, V> map(List<O> list, @NonNull Predicate<O> filter, @NonNull Function<O, K> keyMapper, @NonNull Function<O, V> valueMapper) {
		if (list != null) {
			return list.parallelStream().filter(filter).collect(toMap(keyMapper, valueMapper, (a, b) -> a));
		}
		return new HashMap<>();
	}

	public static <M extends Map<K, V>, K, V> M map(M map, Consumer<M> initializer) {
		if (initializer != null) {
			initializer.accept(map);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static <M extends Map<K, V>, K, V> M map(M map, Object... keyValue) {
		if (keyValue.length % 2 != 0) {
			throw new IllegalArgumentException("missing one key or value");
		}
		for (int i = 0; i < keyValue.length; i += 2) {
			map.put((K) keyValue[i], (V) keyValue[i + 1]);
		}
		return map;
	}

	@SafeVarargs
	public static <K, V> Map<K, V> map(Map<K, V> map, Entry<K, V>... entries) {
		if (entries != null) {
			for (Entry<K, V> entry : entries) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		return map;
	}

	public static <M extends Map<K, V>, K, V> M map(Supplier<M> map, Object... keyValue) {
		return map(map.get(), keyValue);
	}

	@SafeVarargs
	public static <T, C extends Collection<T>> C merge(Collector<T, ?, C> collector, C... colls) {
		return Stream.of(colls).filter(Objects::nonNull).flatMap(Collection::stream).collect(collector);
	}

	public static <K, V> Mergeable<K, V> mergeable() {
		return new Mergeable<>();
	}

	public static <K, V> Mergeable<K, V> mergeable(Map<K, V> map) {
		return new Mergeable<>(map);
	}

	@SafeVarargs
	public static <T> List<T> mergeList(List<T>... colls) {
		return merge(Collectors.toList(), colls);
	}

	@SafeVarargs
	public static <T> List<T> mergeList(List<T> list, T... entries) {
		return mergeList(list, Arrays.stream(entries).collect(Collectors.toList()));
	}

	@SafeVarargs
	public static <T> Set<T> mergeSet(Set<T>... colls) {
		return merge(Collectors.toSet(), colls);
	}

	public static <R> Predicate<R> not(Predicate<R> predicate) {
	    return predicate.negate();
	}

	public static <R> Predicate<R> or(Predicate<R> p1, Predicate<R> p2) {
	    return p1.or(p2);
	}

	public static <R> Predicate<R> and(Predicate<R> p1, Predicate<R> p2) {
	    return p1.and(p2);
	}

	public static <T> Stream<T> parallelStream(Iterable<T> itr) {
		return StreamSupport.stream(itr.spliterator(), true);
	}

	public static <T, K> List<T> removeIfFound(List<T> sources, List<T> targets, Function<? super T, ? extends K> keyMapper) {
		List<K> sourceIds = attributes(sources, keyMapper);
		targets.removeIf(target -> {
			K key = keyMapper.apply(target);
			return key != null && sourceIds.contains(key);
		});
		return targets;
	}

	public static <T, K> List<T> removeIfNotFound(List<T> sources, List<T> targets, Function<? super T, ? extends K> keyMapper) {
		if (isEmpty(sources) || isEmpty(targets)) {
			return targets;
		}

		List<K> sourceIds = attributes(sources, keyMapper);
		targets.removeIf(target -> {
			K key = keyMapper.apply(target);
			return key != null && !sourceIds.contains(key);
		});
		return targets;
	}

	@SafeVarargs
	@SuppressWarnings("squid:S3012")
	public static <S extends Set<T>, T> S set(S set, T... values) {
		for (T value : values) {
			set.add(value);
		}
		return set;
	}

	@SafeVarargs
	public static <T> Set<T> set(T... values) {
		return Arrays.stream(values).collect(Collectors.toSet());
	}

	public static <T> Stream<T> stream(Iterable<T> itr) {
		return iifNotNullElse(itr, i -> StreamSupport.stream(i.spliterator(), false), Stream::of);
	}

	public static <T> Stream<T> stream(Iterator<T> itr) {
		return iifNotNullElse(itr, i -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(i, Spliterator.ORDERED), false), Stream::of);
	}

	public static <T> Stream<T> stream(Enumeration<T> enumeration) {
		return iifNotNullElse(enumeration, e -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(enumeration.asIterator(), Spliterator.ORDERED), false), Stream::of);
	}

	public static <C extends Collection<T>, T> void strip(C coll, Consumer<C> setter) {
		if (isEmpty(coll)) {
			setter.accept(null);
		}
	}

	public static <M extends Map<?, ?>> void strip(M map, Consumer<M> setter) {
		if (isEmpty(map)) {
			setter.accept(null);
		}
	}

	public static <V> V match(Map<String, V> map, String key) {
		return iifNotNull(map.get(key), () -> {
			return map.entrySet().stream()
				.filter(e -> e.getKey().matches(key) || key.matches(e.getKey()))
				.findFirst()
				.map(Map.Entry::getValue)
				.orElse(null);
		});
	}

	public static <T, V> List<T> nonCyclic(List<T> list, Function<T, V> parent, Function<T, V> child) {
		nonCyclic(list, parent, child, list, new LinkedList<>());
		return list;
	}

	private static <T, V> void nonCyclic(List<T> list, Function<T, V> parent, Function<T, V> child, List<T> subList, List<V> path) {
		subList.stream().forEach(s -> {
			List<V> traversing = list(new LinkedList<>(path), parent.apply(s));
			if (path.contains(child.apply(s))) {
				throw new AFCException("in node [" + s + "] from [" + parent.apply(s) + "] to [" +  child.apply(s) + "] again in traversed path " + traversing);
			}
			nonCyclic(
				list,
				parent,
				child,
				list.stream()
					.filter(l -> parent.apply(l).equals(child.apply(s)))
					.collect(toList()),
				traversing
			);
		});
	}
}

