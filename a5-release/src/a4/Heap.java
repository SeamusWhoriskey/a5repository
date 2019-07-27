package a4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class Heap<E, P> implements PriorityQueue<E, P> {
	// comparator for element priorities
	private Comparator<P> comp;
	
	// contains the queue's data and location (index) on the heap
	private ArrayList<E> arr;
	
	// stores queue data with an Element, containing its priority and index
	private HashMap<E, Element> map;
	
	/**
	 * Constructs a heap object
	 * @param c a comparator used to compare priorities
	 */
	public Heap(Comparator<P> c) {
		this.comp = c;
		arr = new ArrayList<E>();
		map = new HashMap<E, Element>();
	}
	
	/*
	 * Returns the comparator used for priorities
	 */
	@Override
	public Comparator<? super P> comparator() {
		return comp;
	}

	/*
	 * Returns the size of the heap, runs in O(1) time
	 */
	@Override
	public int size() {
		return arr.size();
	}

	/**
	 * Remove and return the largest element of this, according to comparator()
	 * Runs in O(log n) time.
	 * The heap is re-heaped after removing the element.
	 * @throws NoSuchElementException if this is empty 
	 */
	@Override
	public E poll() throws NoSuchElementException {
		if (size() == 0)
			throw new NoSuchElementException();
		E toBeReturned = arr.get(0);
		arr.set(0, arr.remove(size() - 1));
		map.remove(toBeReturned);
		elmAt(0).setIndex(0);
		heapifyDown(0);
		return toBeReturned;
	}

	/**
	 * Return the largest element of this, according to comparator().
	 * Runs in O(1) time.
	 * 
	 * @throws NoSuchElementException if this is empty.
	 */
	@Override
	public E peek() throws NoSuchElementException {
		if (size() == 0)
			throw new NoSuchElementException();
		return arr.get(0);
	}

	/**
	 * Add the element e with priority p to this.  Runs in O(log n + a) time,
	 * where a is the time it takes to append an element to an ArrayList of size
	 * n.
	 * e is added to both the ArrayList and the HashMap
	 * The heap is re-heaped after adding the data
	 * @throws IllegalArgumentException if this already contains an element that
	 *                                  is equal to e (according to .equals())
	 */
	@Override
	public void add(E e, P p) throws IllegalArgumentException {
		if (map.containsKey(e))
			throw new IllegalArgumentException();
		arr.add(e);
		map.put(e, new Element(p, size() - 1)); 
		heapifyUp(size() - 1);
	}

	/**
	 * Change the priority associated with e to p.
	 * To maintain the heap invariants, the heap is re-heaped after
	 * priority is changed.
	 * @throws NoSuchElementException if this does not contain e.
	 */
	@Override
	public void changePriority(E e, P p) throws NoSuchElementException {
		if (!map.containsKey(e))
			throw new NoSuchElementException();
		map.get(e).setPriority(p);
		heapifyDown(map.get(e).index());
		heapifyUp(map.get(e).index());
	}
	
	/**
	 * Recursively checks whether the heap is still a heap
	 * checkHeap(0) will check the whole heap
	 * @param i an index to start from on the heap
	 * @return whether the heap maintains invariants
	 */
	public boolean checkHeap(int i) {
		if (left(i) == -1)
			return true;
		if (right(i) == -1)
			return comp.compare(elmAt(i).priority(), elmAt(left(i)).priority()) > 0;
		return (comp.compare(elmAt(i).priority(), elmAt(right(i)).priority()) > 0 
			 && comp.compare(elmAt(i).priority(),  elmAt(left(i)).priority()) > 0 
			 && checkHeap(left(i)) && checkHeap(right(i)));
	}
	
	/*
	 * An element class that stores priority and index, used in the HashMap
	 */
	private class Element {
		P priority;
		int index;
		
		/**
		 * Creates a new element with priority p and index i
		 */
		Element(P p, int i) {
			priority = p;
			index = i;
		}
		
		/*
		 * Returns the priority of the element
		 */
		P priority() {
			return priority;
		}
		
		/*
		 * Returns the index of the element in the ArrayList and heap
		 */
		int index() {
			return index;
		}
		
		/*
		 * sets the index to i
		 */
		void setIndex(int i) {
			index = i;
		}
		
		/*
		 * sets the priority to p
		 */
		void setPriority(P p) {
			priority = p;
		}
	}
	
	/**
	 * Returns the index of the left subtree of the node i
	 * Returns -1 if the left subtree does not exist
	 */
	private int left(int i) {
		if (2 * i + 1 >= size())
			return -1;
		return 2 * i + 1;
	}
	
	/**
	 * Returns the index of the right subtree of the node i
	 * Returns -1 if the right subtree does not exist
	 */
	private int right(int i) {
		if (2 * i + 2 >= size())
			return -1;
		return 2 * i + 2;
	}

	/**
	 * Returns the index of the parent node of the node i
	 * Returns -1 if i is the root of the tree
	 */
	private int parent(int i) {
		if (i == 0)
			return -1;
		if (i % 2 == 0)
			return (i - 2) / 2;
		else
			return (i - 1) / 2;
	}
	
	/**
	 * Given an index, returns the Element associated with that index
	 */
	private Element elmAt(int i) {
		return map.get(arr.get(i));
	}
	
	/**
	 * Recursively heapifies the heap downwards
	 * @param i the index to start at
	 */
	private void heapifyDown(int i) {
		// base case: index i is a leaf of the heap
		if (left(i) != -1 && right(i) != -1) {
			// will swap with left child if left child is largest of the three
			if (comp.compare(elmAt(i).priority(), elmAt(left(i)).priority()) < 0 
			 && comp.compare(elmAt(left(i)).priority(), elmAt(right(i)).priority()) > 0) {
				swap(i, left(i));
				heapifyDown(left(i));
			}
			// will swap with right child if right child is largest of the three
			else if (comp.compare(elmAt(i).priority(), elmAt(right(i)).priority()) < 0 
				  && comp.compare(elmAt(right(i)).priority(), elmAt(left(i)).priority()) > 0) {
				swap(i, right(i));
				heapifyDown(right(i));
			}
		} else if (left(i) != -1 && right(i) == -1) {
			if (comp.compare(elmAt(i).priority(), elmAt(left(i)).priority()) < 0) {
				swap(i, left(i));
			}
		}
	}
	
	/**
	 * Recursively heapifies the heap upwards, similar to heapifyDown
	 * @param i the index to start at
	 */
	private void heapifyUp(int i) {
		if (i > 0) {
			if (comp.compare(elmAt(i).priority(), elmAt(parent(i)).priority()) > 0) {
				swap(i, parent(i));
				heapifyUp(parent(i));
			}
		}
	}
	
	/**
	 * Swaps the elements at index i and index j
	 * Applies the change to both the HashMap and the ArrayList
	 */
	private void swap(int i, int j) {
		int index = elmAt(i).index();
		elmAt(i).setIndex(elmAt(j).index());
		elmAt(j).setIndex(index);
		
		E temp = arr.get(i);
		arr.set(i, arr.get(j));
		arr.set(j, temp);
	}
	
	/**
	 * Glass-box tests for the heap. 
	 * As the private helper methods are used extensively
	 * by the add, poll, and changePriority methods, we only 
	 * test these three.
	 */
	public static class Tests {
		@Test
		void testAdd() {
			Heap<Integer, Integer> pq = new Heap<Integer, Integer>(Comparator.naturalOrder());
			pq.add(4, 2);
			assertEquals("[4]", pq.arr.toString());
			assertTrue(pq.map.containsKey(4));
			assertEquals(0, pq.map.get(4).index());
			assertEquals(2, pq.map.get(4).priority());
			pq.add(5, 3);
			assertEquals("[5, 4]", pq.arr.toString());
			assertTrue(pq.map.containsKey(5));
			assertEquals(0, pq.map.get(5).index());
			assertEquals(1, pq.map.get(4).index());
			assertEquals(2, pq.map.get(4).priority());
			assertEquals(3, pq.map.get(5).priority());
			pq.add(1, 4);
			assertEquals("[1, 4, 5]", pq.arr.toString());
			assertTrue(pq.map.containsKey(1));
			assertEquals(0, pq.map.get(1).index());
			assertEquals(1, pq.map.get(4).index());
			assertEquals(2, pq.map.get(5).index());
			assertEquals(4, pq.map.get(1).priority());
			assertEquals(2, pq.map.get(4).priority());
			assertEquals(3, pq.map.get(5).priority());
			pq.add(2, 5);
			assertEquals("[2, 1, 5, 4]", pq.arr.toString());
			assertTrue(pq.map.containsKey(2));
			assertEquals(0, pq.map.get(2).index());
			assertEquals(1, pq.map.get(1).index());
			assertEquals(3, pq.map.get(4).index());
			assertEquals(5, pq.map.get(2).priority());

			assertThrows(IllegalArgumentException.class, () -> pq.add(2, 5));
			
			assertTrue(pq.checkHeap(0));
		}
		
		@Test
		void testPoll() {
			Heap<Integer, Integer> pq = new Heap<Integer, Integer>(Comparator.naturalOrder());
			assertThrows(NoSuchElementException.class, () -> pq.peek());
			assertThrows(NoSuchElementException.class, () -> pq.poll());
			pq.add(4, 2);
			pq.add(5, 3);
			pq.add(1, 4);
			pq.add(2, 5);
			assertEquals(2, pq.peek());
			assertEquals(2, pq.poll());
			assertTrue(pq.checkHeap(0));
			assertFalse(pq.map.containsKey(2));
			assertEquals("[1, 4, 5]", pq.arr.toString());
			assertEquals(0, pq.map.get(1).index());
			assertEquals(1, pq.map.get(4).index());
			assertEquals(2, pq.map.get(5).index());
			assertEquals(4, pq.map.get(1).priority());
			assertEquals(2, pq.map.get(4).priority());
			assertEquals(3, pq.map.get(5).priority());
		}
		
		@Test
		void testChangePrio() {
			Heap<Integer, Integer> pq = new Heap<Integer, Integer>(Comparator.naturalOrder());
			assertThrows(NoSuchElementException.class, () -> pq.changePriority(4, 3));
			pq.add(4, 2);
			pq.add(5, 3);
			pq.add(1, 4);
			pq.add(2, 5);
			pq.changePriority(2, 0);
			assertEquals("[1, 4, 5, 2]", pq.arr.toString());
			assertEquals(0, pq.map.get(1).index());
			assertEquals(1, pq.map.get(4).index());
			assertEquals(2, pq.map.get(5).index());
			assertEquals(3, pq.map.get(2).index());
			assertEquals(0, pq.map.get(2).priority());
			assertTrue(pq.checkHeap(0));
			
			pq.changePriority(4, -1);
			assertEquals("[1, 2, 5, 4]", pq.arr.toString());
			assertEquals(0, pq.map.get(1).index());
			assertEquals(1, pq.map.get(2).index());
			assertEquals(2, pq.map.get(5).index());
			assertEquals(3, pq.map.get(4).index());
			assertEquals(-1, pq.map.get(4).priority());
			assertTrue(pq.checkHeap(0));
		}
	}
}
