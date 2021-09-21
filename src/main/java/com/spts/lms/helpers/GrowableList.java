package com.spts.lms.helpers;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GrowableList<T> implements List<T>, Serializable {
	
	private static final long serialVersionUID = 2763541004731656101L;
	// The list to decorate
	List<T> otherList;
	// Factory method to create a new instance of the element
	CreateInstanceFactory<T> instanceFactory;
	// Ignores the index when setting or adding at a particular index.
	// Causes entries to be added to the end
	Boolean ignoreIndex = false;
	// Condition to check if the List has grown
	Boolean hasGrown = false;
	/**
	 * Create a growable list backed by the List instance
	 * @param otherList	The list to decorate the growable behavior
	 */
	public GrowableList(List<T> otherList) {
		this(otherList, false);
	}
	/**
	 * Create a growable list backed by the List instance.
	 * The ignoreIndex will cause the elements to be added to the end of the list even when the index is provided.
	 * @param otherList		The list to decorate the growable behavior
	 * @param ignoreIndex	Flag to indicate whether index needs to be considered
	 */
	public GrowableList(List<T> otherList, Boolean ignoreIndex) {
		super();
		this.otherList = otherList;
		this.ignoreIndex = ignoreIndex;
	}

	public GrowableList(List<T> otherList, Boolean ignoreIndex, CreateInstanceFactory<T> instanceFactory) {
		this(otherList, false);
		this.instanceFactory = instanceFactory;
	}

	public boolean add(T e) {
		return otherList.add(e);
	}

	public void add(int index, T element) {
		if (ignoreIndex) {
			add(element);
		} else {
			growList(index);
			otherList.add(index, element);
		}
		

	}

	public boolean addAll(Collection<? extends T> c) {
		return otherList.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		if (ignoreIndex) {
			return addAll(c);
		} else {
			growList(index);
			return otherList.addAll(index, c);
		}
	}

	public void clear() {
		otherList.clear();

	}

	public boolean contains(Object o) {
		return otherList.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return otherList.containsAll(c);
	}

	public T get(int index) {
		growList(index);
		return otherList.get(index);
	}

	public int indexOf(Object o) {
		return otherList.indexOf(o);
	}

	public boolean isEmpty() {
		return otherList.isEmpty();
	}

	public Iterator<T> iterator() {
		trimToSizeonGrow();
		return otherList.iterator();
	}

	public int lastIndexOf(Object o) {
		return otherList.lastIndexOf(o);
	}

	public ListIterator<T> listIterator() {
		trimToSizeonGrow();
		return otherList.listIterator();
	}

	public ListIterator<T> listIterator(int index) {
		trimToSizeonGrow();
		return otherList.listIterator(index);
	}

	public boolean remove(Object o) {
		return otherList.remove(o);
	}

	public T remove(int index) {
		return otherList.remove(index);
	}

	public boolean removeAll(Collection<?> c) {
		return otherList.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return otherList.removeAll(c);
	}

	public T set(int index, T element) {
		if (ignoreIndex) {
			add(element);
			return element;
		} else {
			growList(index);
			return otherList.set(index, element);
		}
	}

	public int size() {
		return otherList.size();
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return otherList.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return otherList.toArray();
	}

	@SuppressWarnings("hiding")
	public <T> T[] toArray(T[] a) {
		return otherList.toArray(a);
	}
	/**
	 * Check if the index is Out Of Bound
	 * @param index	The index to check for
	 * @return
	 */
	private boolean isIndexOutOfBounds(int index) {
		return size() < index;
	}
	/**
	 * Grow the list to the index if it is more than the size
	 * @param index	The index to grow upto, Should not be negative
	 */
	private void growList(int index) {
		if (isIndexOutOfBounds(index)) {
			hasGrown = true;
			addAll(Collections.<T> nCopies(index - size() + 1, null));
		}
	}
	/**
	 * Remove the nulls from the list as a result of growing
	 */
	private void trimToSizeonGrow() {
		if(hasGrown) {
			removeAll(Collections.singleton(null));
			hasGrown = false;
		}
	}
	
	public GrowableList<T> trimToSize() {
			removeAll(Collections.singleton(null));
			return this;
	}
	
	private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.writeObject(otherList);
    }
	
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
		otherList = (List<T>) stream.readObject();
    }

}
