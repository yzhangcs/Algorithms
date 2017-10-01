import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code Deque} class represents a double-ended queue or deque, which is
 * a generalization of a stack and a queue that supports adding and removing
 * items from either the front or the back of the data structure.
 * <p>
 * This implementation uses a double-linked list with a non-static nested class for
 * linked-list nodes.
 *
 * @author zhangyu
 * @date 2017.3.10
 */
public class Deque<Item> implements Iterable<Item>
{
    private int n;
    private Node first;
    private Node last;
    private Node sentinel;

    // helper linked list class
    private class Node
    {
        private Item item;
        private Node next;
        private Node prev;
    }

    /**
     * Initializes an empty deque.
     */
    public Deque()
    {
        first = null;
        last  = first;
        sentinel = new Node();
        sentinel.next = first;
        n = 0;
    }

    /**
     * Returns whether the deque is empty.
     *
     * @return true if this deque is empty; false otherwise
     */
    public boolean isEmpty()
    {
        return sentinel.next == null;
    }

    /**
     * Returns the number of items on this deque.
     *
     * @return the number of items on this deque
     */
    public int size()
    {
        return n;
    }

    /**
     * Add the item to the front of the deque.
     *
     * @param item the item to add
     * @throws NullPointerException if the item to be added is null
     */
    public void addFirst(Item item)
    {
        if (item == null) throw new NullPointerException("Null item");
        Node oldFirst = first;

        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = sentinel;
        if (!isEmpty()) oldFirst.prev = first;
        else            last = first;
        sentinel.next = first;
        n++;
    }

    /**
     * Add the item to the end of the deque.
     *
     * @param item the item to add
     * @throws NullPointerException if the item to be added is null
     */
    public void addLast(Item item)
    {
        if (item == null) throw new NullPointerException("Null item");
        if (isEmpty()) addFirst(item);
        else
        {
            Node oldLast = last;

            last = new Node();
            last.item = item;
            oldLast.next = last;
            last.prev = oldLast;
            last.next = null;
            n++;
        }
    }

    /**
     * Remove and return the item from the front of the deque.
     *
     * @return the item on the front of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    public Item removeFirst()
    {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");

        Item item = first.item;     // save item to return

        first = first.next;
        sentinel.next = first;          // delete first node
        if (isEmpty()) last = null; // to avoid loitering
        else           first.prev = sentinel;
        n--;
        return item;                // return the saved item
    }

    /**
     * Remove and return the item from the end of the deque.
     *
     * @return the item on the end of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    public Item removeLast()
    {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");

        Item item = last.item;

        last = last.prev;
        last.next = null;
        if (isEmpty()) // to avoid loitering
        {
            first = null;
            last = first;
        }
        n--;
        return item;
    }

    /**
     * Returns an iterator over items in order from front to end.
     *
     * @return an iterator over items in order from front to end
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Item> iterator()
    {
        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item>
    {
        private Node current = first;

        public boolean hasNext() { return current != null;                    }
        public void remove()     { throw new UnsupportedOperationException(); }
        public Item next()
        {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = current.item;

            current = current.next;
            return item;
        }
    }

    /**
     * Unit tests the {@code Deque} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        Deque<String> deque = new Deque<String>();

        while (!StdIn.isEmpty())
        {
            String item = StdIn.readString();
            if (!item.equals("-"))
                deque.addLast(item);
            else if (!deque.isEmpty())
                deque.removeFirst();
            for (String str : deque)
                StdOut.print(str + " ");
            StdOut.println("\t" + deque.isEmpty());
        }
    }
}