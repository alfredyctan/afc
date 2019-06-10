package org.afc.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;

public class ListUtil {

	@SafeVarargs
	public static <T> List<T> list(List<T> list, T... values) {
		for (T value : values) {
			list.add(value);
		}
		return list;
	}

	@AllArgsConstructor
	public static class CoStream<X, Y> {
		public X x;
		public Y y;
	}
	
	public static <X, Y> Stream<CoStream<X, Y>> coStream(Collection<X> list1, Collection<Y> list2) {
		List<CoStream<X, Y>> list = new LinkedList<>();
		Iterator<X> itr1 = list1.iterator();
		Iterator<Y> itr2 = list2.iterator();
		while (itr1.hasNext() && itr2.hasNext()) {
			list.add(new CoStream<X, Y>(itr1.next(), itr2.next()));
		}
		while (itr1.hasNext()) {
			list.add(new CoStream<X, Y>(itr1.next(), null));
		}
		while (itr2.hasNext()) {
			list.add(new CoStream<X, Y>(null, itr2.next()));
		}
		return list.stream();
	}
}

