import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author 

 * @param <T> type to store
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size;
	private int modCount;
	
	/** Creates an empty list */
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		Node<T> node = new Node<T>(element);
		if (isEmpty()) {
			head = tail = node;
		}
		else {
			node.setNext(head);
			head.setPrev(node);
			head = node;
		}
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		Node<T> node = new Node<T>(element);
		if (isEmpty()) {
			head = tail = node;
		}
		else {
			tail.setNext(node);
			node.setPrev(tail);
			tail = node;
		}
		size++;
		modCount++;
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		boolean found = false;

		Node<T> current = head;
		Node<T> node = new Node<T>(element);
		
		while (current != null && !found) {
			if (target.equals(current.getElement())) {
				found = true;
			} else {

				current = current.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		
		if(size() == 1) {
			current.setNext(node);
			node.setPrev(current);
			tail = current.getNext();
		}
		else if(current == head) {
			node.setNext(head.getNext());
			node.getNext().setPrev(node);
			head.setNext(node);
			node.setPrev(head);
			
		}
		else if(current == tail) {
			tail.setNext(node);
			node.setPrev(tail);
			tail = node;
		}
		else {
			node.setNext(current.getNext());
			current.setNext(node);
			node.setPrev(current);
			node.getNext().setNext(node);
		}
		size++;
		modCount++;	
	}

	@Override
	public void add(int index, T element) {
		if (index >= 0 && index <= size()) {
			
			Node<T> current = head;
			Node<T> previous = null;
			Node<T> node = new Node<T>(element);
			for (int i = 0; i < index; i++) {
				previous = current;
				current = current.getNext();
			}
			
			
			if(size() == 0) {
				head = tail = node;
			}
			else if(current == head) {
				node.setNext(head);
				head.setPrev(node);
				head = node;
			}
			else if(current == tail) {
				node.setNext(tail);
				tail.setPrev(node);
				previous.setNext(node);
				node.setPrev(previous);
			}
			else if(current == null) {
				tail.setNext(node);
				node.setPrev(tail);
				tail = node;
			}
			else {
				previous.setNext(node);
				node.setNext(current);
				node.setPrev(previous);
				current.setPrev(node);
				
			}
			
			size++;
			modCount++;
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) {throw new NoSuchElementException();}
		T retVal = head.getElement();
		if (size == 1) {
			head = tail = null;
		}
		else {
			head = head.getNext();
			head.setPrev(null);
		}
		
		size--;
		modCount++;
		return retVal;
		
	}

	@Override
	public T removeLast() {
		if (isEmpty()) {throw new NoSuchElementException();}
		T retVal = tail.getElement();
		if (size == 1) {
			head = tail = null;
		}
		else {
			tail = tail.getPrev();
			tail.setNext(null);
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		boolean found = false;

		Node<T> current = head;
		
		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {

				current = current.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		if(size() == 1) {
			head = tail = null;
		}
		else if (current == head) {
			head = head.getNext();
			head.setPrev(null);
		}
		else if (current == tail) {
			tail = tail.getPrev();
			tail.setNext(null);
		}
		else {
			current.getPrev().setNext(current.getNext());
			current.getNext().setPrev(null);
		}
		size--;
		modCount++;
		return element;
	}

	@Override
	public T remove(int index) {
		Node<T> current = head;
		
		
		if (index >= 0 && index < size())
		{
			for(int i=0; i < index; i++)
			{
				current = current.getNext();
			}
			
			T retVal = current.getElement();
			
			if(size() == 1) {
				head = tail = null;
			}
			else if (current == head) {
				head = head.getNext();
				head.setPrev(null);
			}
			else if (current == tail) {
				tail = tail.getPrev();
				tail.setNext(null);
			}
			else {
				current.getPrev().setNext(current.getNext());
				current.getNext().setPrev(current.getPrev());
			}
			
			size--;
			modCount++;
			
			return retVal;
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public void set(int index, T element) {
		if(index >=0 && index < size()) {
			Node<T> current = head;
			
			for (int i=0; i<index; i++) {
				current = current.getNext();
			}
			current.setElement(element);
			modCount++;
		}
		else {
			throw new IndexOutOfBoundsException();
		}
		
	}

	@Override
	public T get(int index) {
		if(index >=0 && index < size()) {
			Node<T> current = head;
			
			for (int i=0; i<index; i++) {
				current = current.getNext();
			}
			return current.getElement();			
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public int indexOf(T element) {
		if (isEmpty()) {
			return -1;
		}
		
		boolean found = false;

		Node<T> current = head;
		int index = 0;
		
		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				current = current.getNext();
				index++;
			}
		}
		
		if (!found) {
			return -1;
		}
		
		return index;
	}

	@Override
	public T first() {
		if (isEmpty()) {throw new NoSuchElementException();}
		return head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty()) {throw new NoSuchElementException();}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		if (isEmpty()) {
			return false;
		}
		
		boolean found = false;
		Node<T> current = head;

		
		while (current != null && !found) {
			if (target.equals(current.getElement())) {
				return true;
			} else {

				current = current.getNext();
			}
		}
		return false;	
	}

	@Override
	public boolean isEmpty() {
		return (size() == 0);
	}

	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Returns a list in format [A, B, C]
	 * @return String representation of the list
	 */
	public String toString() {
		String str = "";
		Node<T> current = head;
		str += "[";
		while (current != null) {
			str += current.getElement();
			if (current != tail) {
				str += ", ";
			}
			current = current.getNext();
			
		}
		str += "]";
		return str;
	}

	@Override
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<T> {
		private Node<T> nextNode;
		private int index;
		private int iterModCount;
		private boolean nextCalled;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			iterModCount = modCount;
			nextCalled = false;
			index = 0;
			
		}

		@Override
		public boolean hasNext() {
			if (iterModCount != modCount ) {
				throw new ConcurrentModificationException();
			}
			if (size() == 0) {
				return false;
			}
			if (size() == 1 && index == 0) {
				return true;
			}
			else if (size() == 1 && index == 1) {
				return false;
			}
			else {
				return !(nextNode == null);
			}
	
		}

		@Override
		public T next() {
			if (iterModCount != modCount ) {
				throw new ConcurrentModificationException();
			}
			if (hasNext()) {
				T element = nextNode.getElement();
				index++;
				nextNode = nextNode.getNext();
				nextCalled = true;
				return element;
			}
			else {throw new NoSuchElementException();}
		}
		
		@Override
		public void remove() {
			if (iterModCount != modCount ) {
				throw new ConcurrentModificationException();
			}
			if (nextCalled) {
				IUDoubleLinkedList.this.remove(index-1);
				nextCalled = false;
				iterModCount++;
			}
			else {throw new IllegalStateException();}
			
		}
	}


	@Override
	public ListIterator<T> listIterator() {
		
		return new DLLIterator();
	}
	
	private class DLLIterator implements ListIterator<T> {
		
		private Node<T> nextNode;
		private Node<T> prevNode;
		private int iterModCount;
		private boolean nextCalled;
		private boolean prevCalled;
		private int index;
		
		public DLLIterator() {
			nextNode = head;
			prevNode = null;
			iterModCount = modCount;
			nextCalled = false;
			prevCalled = false;
			index = 0;
		}
		
		public DLLIterator(int index) {
			if (index > size || index < 0)
			{
				throw new IndexOutOfBoundsException();
			}
			
			nextNode = head;
			for (int i=0; i<index; i++) {
				prevNode = nextNode;
				nextNode = nextNode.getNext();
			}
			
			iterModCount = modCount;
			nextCalled = false;
			prevCalled = false;
			this.index = index;
		}

		@Override
		public boolean hasNext() {
			if (modCount != iterModCount) {throw new ConcurrentModificationException();}
			return (nextNode != null);
		}

		@Override
		public T next() {
			if (modCount != iterModCount) {throw new ConcurrentModificationException();}
			if (hasNext()) {
				T retval = nextNode.getElement();
				prevNode = nextNode;
				nextNode = nextNode.getNext();
				nextCalled = true;
				prevCalled = false;
				index++;
				return retval;
			}

			else {
				throw new NoSuchElementException();
			}
		}

		@Override
		public boolean hasPrevious() {
			if (modCount != iterModCount) {throw new ConcurrentModificationException();}
			return (prevNode != null);
		}

		@Override
		public T previous() {
			if (modCount != iterModCount) {throw new ConcurrentModificationException();}
			if (hasPrevious())
			{
				T retVal = prevNode.getElement();
				nextNode = prevNode;
				prevNode = prevNode.getPrev();
				prevCalled = true;
				nextCalled = false;
				index--;
				return retVal;
			}
			else {
				throw new NoSuchElementException();
			}
		}

		@Override
		public int nextIndex() {
		if (modCount != iterModCount) {throw new ConcurrentModificationException();}
			if (hasNext()) {
			return (index);
			}
			else {
				return size;
			}
		
		}

		@Override
		public int previousIndex() {
			if (modCount != iterModCount) {throw new ConcurrentModificationException();}
			if (hasPrevious()) {
				return (index-1);
			}
			else {
				return -1;
			}
		}

		@Override
		public void remove() {
			if (modCount != iterModCount) {throw new ConcurrentModificationException();}
			if (nextCalled) {
				IUDoubleLinkedList.this.remove(previousIndex());
				nextCalled = false;
				iterModCount++;
			}
			else if (prevCalled) {
				IUDoubleLinkedList.this.remove(index);
				prevCalled = false;
				iterModCount++;
			}
			else {
				throw new IllegalStateException();
			}
			
		}

		@Override
		public void set(T e) {
			if (modCount != iterModCount) {throw new ConcurrentModificationException();}
			if (nextCalled) {
				IUDoubleLinkedList.this.set(index-1, e);
				iterModCount++;
			}
			else if (prevCalled) {
				IUDoubleLinkedList.this.set(index, e);
				iterModCount++;
			}
			else {
				throw new IllegalStateException();
			}
			
		}

		@Override
		public void add(T e) {
			if (modCount != iterModCount) {throw new ConcurrentModificationException();}
			IUDoubleLinkedList.this.add(index, e);
			iterModCount++;
			nextCalled = false;
			prevCalled = false;
			index++;
		}
		
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLIterator(startingIndex);
	}
}
