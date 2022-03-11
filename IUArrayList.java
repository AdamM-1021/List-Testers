import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Array-based implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported. 
 * 
 * @author 
 *
 * @param <T> type to store
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int NOT_FOUND = -1;
	
	private T[] array;
	private int rear;
	private int modCount;
	
	/** Creates an empty list with default initial capacity */
	public IUArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	/** 
	 * Creates an empty list with the given initial capacity
	 * @param initialCapacity
	 */
	@SuppressWarnings("unchecked")
	public IUArrayList(int initialCapacity) {
		array = (T[])(new Object[initialCapacity]);
		rear = 0;
		modCount = 0;
	}
	
	/** Double the capacity of array */
	private void expandCapacity() {
		array = Arrays.copyOf(array, array.length*2);
	}
	
	private void expandIfNecessary() {
		if(rear >= (array.length - 1)) {
			expandCapacity();
		}
	}

	@Override
	public void addToFront(T element) {
		expandIfNecessary();
		rear++;
		for (int i = rear; i > 0; i--) {
			array[i] = array[i-1];
		}
		array[0] = element;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		expandIfNecessary();
		array[rear] = element;
		rear++;
		modCount++;
	}

	@Override
	public void add(T element) {
		expandIfNecessary();
		array[rear] = element;
		rear++;
		modCount++;
		
	}

	@Override
	public void addAfter(T element, T target) {

		int targetIndex = indexOf(target);
		
		if (targetIndex < 0) {
			throw new NoSuchElementException();
		}
		
		expandIfNecessary();
		
		for (int i = rear - 1; i > targetIndex; i--) {
			array[i+1] = array[i];
		}
		
		array[targetIndex + 1] = element;
		rear++;
		modCount++;
		
	}

	@Override
	public void add(int index, T element) {
		
		if(index >= 0 && index < (rear+1)) {
			expandIfNecessary();
			for(int i = rear - 1; i > index; i--) {
				array[i+1] = array[i];
			}
			
			array[index+1] = element;
			rear++;
			modCount++;
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public T removeFirst() {
		if(!isEmpty()) {
			rear--;
			modCount++;
			T val = array[0];
			for (int i=0; i<rear; i++) {
				array[i] = array[i+1];
			}
			array[rear+1] = null;
			return val;
		}
		else {
			throw new NoSuchElementException();
		}
	}
	

	@Override
	public T removeLast() {
		if(!isEmpty())
		{
			rear--;
			modCount++;
			T val = array[rear];
			array[rear] = null;
			return val;
		}
		else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public T remove(T element) {
		int index = indexOf(element);
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}
		
		T retVal = array[index];
		
		rear--;
		
		//shift elements
		for (int i = index; i < rear; i++) {
			array[i] = array[i+1];
		}
		array[rear] = null;
		modCount++;
		
		return retVal;
	}

	@Override
	public T remove(int index) { 
		if (index < 0 || index >= rear) {
			throw new IndexOutOfBoundsException();
		}
		
		T retVal = array[index];
		
		rear--;
		for (int i = index; i < rear; i++) {
			array[i] = array[i+1];
		}
		array[rear] = null;
		modCount++;
		
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= rear || isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		
		array[index] = element;
		modCount++;
	}

	@Override
	public T get(int index) {
		
		if(index >= rear||index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		if (index < rear && index >= 0)
		{
			return array[index];
		}
		else {
			throw new IndexOutOfBoundsException();
		}
		
	}

	@Override
	public int indexOf(T element) {
		int index = NOT_FOUND;
		
		if (!isEmpty()) {
			int i = 0;
			while (index == NOT_FOUND && i < rear) {
				if (element.equals(array[i])) {
					index = i;
				} else {
					i++;
				}
			}
		}
		
		return index;
	}

	@Override
	public T first() {
		if(!isEmpty()) {
			return array[0];
		}
		throw new NoSuchElementException();
	}

	@Override
	public T last() {
		if(!isEmpty()) {
			return array[rear-1];
		}
		throw new NoSuchElementException();
	}

	@Override
	public boolean contains(T target) {
		return (indexOf(target) != NOT_FOUND);
	}

	@Override
	public boolean isEmpty() {
		return (rear == 0);
	}

	@Override
	public int size() {
		return rear;
	}
	
	public String toString() {
		String str = "[";
		for (int i = 0; i < rear; i++)
		{
			str += array[i];
			if (i < rear-1) {
				str += ", ";
			}
		}
		str += "]";
		return str;
	}

	@Override
	public Iterator<T> iterator() {
		return new ALIterator<T>();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUArrayList */
	@SuppressWarnings("hiding")
	private class ALIterator<T> implements Iterator<T> {
		private int nextIndex;
		private int iterModCount;
		private boolean nextCalled;
		
		public ALIterator() {
			nextIndex = 0;
			iterModCount = modCount;
			nextCalled = false;
		}

		@Override
		public boolean hasNext() {
			if(iterModCount!= modCount) {
				throw new ConcurrentModificationException();
			}
			return nextIndex < rear;
		}

		@Override
		public T next() {
			if(iterModCount!= modCount) {
				throw new ConcurrentModificationException();
			}
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			@SuppressWarnings("unchecked")
			T val = (T)get(nextIndex);
			nextIndex++;
			nextCalled = true;
			return val;
			
		}
		
		@Override
		public void remove() {
			if(iterModCount!= modCount) {
				throw new ConcurrentModificationException();
			}
			if(nextCalled) {
				IUArrayList.this.remove(nextIndex-1);
				nextCalled = false;
				iterModCount++;
			}
			else {
				throw new IllegalStateException();
			}
		}
	}
}
