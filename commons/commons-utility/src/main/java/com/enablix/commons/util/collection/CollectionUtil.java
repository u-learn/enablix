package com.enablix.commons.util.collection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains utility methods for working with collections.
 */
public final class CollectionUtil {
	/**
	 * Checks if two collections do not contain the same elements.
	 * @param a The first collection.
	 * @param b The second collection.
	 * @return {@code true} if the collections do not contain the same elements.
	 */
	public static <K> boolean areNotSame(final Collection<K> a, final Collection<K> b) {
		return !areSame(a, b);
	}

	/**
	 * Checks if two collections contain the same elements.
	 * @param a The first collection.
	 * @param b The second collection.
	 * @return {@code true} if both collections contain the same elements.
	 */
	public static <K> boolean areSame(final Collection<K> a, final Collection<K> b) {
		return a != null && b != null && (a == b || a.size() == b.size() && a.containsAll(b));
	}


	/**
	 * Converts an array into List. If input array is null then it returns an empty list.
	 * @param elements Array of elements to be converted into {@link List}
	 * @return {@link List} of elements in array.
	 */
	@SafeVarargs
	public static <T> List<T> asList(final T... elements) {
		final List<T> result = new ArrayList<T>();
		if (isNotEmpty(elements)) {
			for (final T element : elements) {
				result.add(element);
			}
		}
		return result;
	}



	@SafeVarargs
	public static <T> Set<T> asSet(final T... elements) {
		final Set<T> result = new HashSet<T>();

		if (isNotEmpty(elements)) {
			for (final T element : elements) {
				result.add(element);
			}
		}
		return result;
	}

	/**
	 * Cleans null values from a collection.
	 * @param elements The collection to be cleaned.
	 * @return Elements after cleaning <code>null</code> values.
	 */
	public static <T extends Iterable<?>> T cleanNull(final T elements) {
		if (elements != null) {
			final Iterator<?> iterator = elements.iterator();
			while (iterator.hasNext()) {
				if (iterator.next() == null) {
					iterator.remove();
				}
			}
		}
		return elements;
	}





	/**
	 * Returns a {@link Collection} containing the intersection of the given {@link Collection}s.
	 * @param a the first collection, must not be null
	 * @param b the second collection, must not be null
	 * @return the intersection of the two collections
	 */
	public static <K> Collection<K> intersection(final Collection<K> a, final Collection<K> b) {
		return new HashSet<K>(subtract(a, subtract(a, b)));
	}

	/**
	 * Null-safe check if the specified collection is empty.
	 * <p>
	 * Null returns true.
	 * @param coll the collection to check, may be null
	 * @return true if empty or null
	 */
	public static <K> boolean isEmpty(final Collection<K> coll) {
		return coll == null || coll.isEmpty();
	}

	/**
	 * Null-safe check if the specified iterable is empty.
	 * <p>
	 * Null returns true.
	 *
	 * @param iterable
	 *            the collection to check, may be null
	 * @return true if empty and or null
	 */
	public static <K> boolean isEmpty(final Iterable<K> iterable) {
		return iterable == null || !iterable.iterator().hasNext();
	}

	/**
	 * Null-safe check if the specified array is empty.
	 * <p>
	 * Null returns true.
	 * @param elements the elements to check, may be null
	 * @return true if empty or null
	 */
	public static <K> boolean isEmpty(final K[] elements) {
		return !isNotEmpty(elements);
	}

	/**
	 * Null-safe check if the specified collection is not empty.
	 * <p>
	 * Null returns false.
	 * @param coll the collection to check, may be null
	 * @return true if not empty and not null
	 */
	public static <K> boolean isNotEmpty(final Collection<K> coll) {
		return !isEmpty(coll);
	}

	/**
	 * Null-safe check if the specified iterable is not empty.
	 * <p>
	 * Null returns false.
	 * 
	 * @param iterable
	 *            the collection to check, may be null
	 * @return true if not empty and not null
	 */
	public static <K> boolean isNotEmpty(final Iterable<K> iterable) {
		return !isEmpty(iterable);
	}

	/**
	 * Null-safe check if the specified array is not empty.
	 * <p>
	 * Null returns false.
	 * @param elements the elements to check, may be null
	 * @return true if not empty and not null
	 */
	public static <K> boolean isNotEmpty(final K[] elements) {
		return elements != null && elements.length > 0;
	}

	public static <E> Collection<E> nullSafe(final Collection<E> c) {
		return isNotEmpty(c) ? c : new LinkedList<E>();
	}

	public static <E> List<E> nullSafe(final List<E> c) {
		return isNotEmpty(c) ? c : new LinkedList<E>();
	}

	public static <E> Set<E> nullSafe(final Set<E> c) {
		return isNotEmpty(c) ? c : new HashSet<E>();
	}

	/**
	 * Returns a new {@link Collection} containing <tt><i>a</i> - <i>b</i></tt>.
	 * @param a the collection to subtract from, must not be null
	 * @param b the collection to subtract, must not be null
	 * @return a new collection with the results
	 */
	public static <K> Collection<K> subtract(final Collection<K> a, final Collection<K> b) {
		final List<K> result = new ArrayList<K>();

		if (isNotEmpty(a)) {
			result.addAll(a);

			if (isNotEmpty(b)) {
				result.removeAll(b);
			}
		}

		return result;
	}

	/**
	 * Converts a collection of specific type to an array of same type. It returns null if collection is null or empty or all elements in collection are null.
	 * @param c The collection which has to converted in to an array.
	 * @return The array containing all the elements of collection.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(final Collection<T> c) {
		if (isEmpty(c)) {
			return null;
		}

		final T firstNotNullValue = getFirstNotNull(c);
		if (firstNotNullValue == null) {
			return null;
		}

		return c.toArray((T[]) Array.newInstance(firstNotNullValue.getClass(), c.size()));
	}

	
	/**
	 * Gets the union of collections of same type. It return null in case all collections are null or empty.
	 * @param args The collection of same type whose union is required.
	 * @return The union of collections of same type.
	 */
	public static <T> Collection<T> union(@SuppressWarnings("unchecked") final Collection<T>... args) {
		final Collection<T> result = new HashSet<T>();

		if (args != null) {
			for (final Collection<T> t : args) {
				if (CollectionUtil.isNotEmpty(t)) {
					result.addAll(t);
				}
			}
		}

		return isNotEmpty(result) ? result : null;
	}

	/**
	 * Gets the first not null value in the collection.
	 * @param arg The input collection.
	 * @return The First not null value in collection if present else return null.
	 */
	private static <T> T getFirstNotNull(final Collection<T> arg) {
		for (final T t : arg) {
			if (t != null) {
				return t;
			}
		}

		return null;
	}

	public static interface ITransformer<K, V> {
		V transform(K in);
	}
	
	public static interface CollectionCreator<T extends Collection<V>, V> {
		T create();
	}
	
	public static <T extends Collection<V>, S extends Collection<K>, K, V> T 
			transform(S in, CollectionCreator<T, V> creator, ITransformer<K, V> tx) {
		T collection = creator.create();
		for (K inElem : in) {
			collection.add(tx.transform(inElem));
		}
		return collection;
	}
	
	public static boolean isNotEmpty(Map<?, ?> contentRecord) {
		return contentRecord != null && !contentRecord.isEmpty();
	}
	
	/**
	 * Prevent instantiation.
	 */
	private CollectionUtil() {
	}

	
}
