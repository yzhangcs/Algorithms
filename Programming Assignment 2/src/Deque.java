import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> 
{
    private int n;      // number of elements on queue
    private Node first; // beginning of queue
    private Node last;  // end of queue
    private Node head;  // sentinel node
    
    // helper linked list class
    private class Node
    {
        private Item item;
        private Node next;
        private Node last;
    }
    public Deque()                           // construct an empty deque
    {
        first = null;
        last  = null;
        head = new Node();
        head.next = null;
        n = 0;
    }
    public boolean isEmpty()                 // is the deque empty?
    {
        return head.next == null;
    }
    public int size()                        // return the number of items on the deque
    {
        return n;
    }
    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null) throw new NullPointerException("null item");
        Node oldFirst = first;
        
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.last = head;
        if (!isEmpty()) oldFirst.last = first;
        else            last = first;
        head.next = first;
        n++;
    }
    public void addLast(Item item)           // add the item to the end
    {
        if (item == null) throw new NullPointerException("null item");
        if (isEmpty()) addFirst(item);
        else
        {
            Node oldLast = last;
            
            last = new Node();
            last.item = item;
            oldLast.next = last;
            last.last = oldLast;
            last.next = null;
            n++;
        }
    }
    public Item removeFirst()                // remove and return the item from the front
    {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        
        Item item = first.item;        // save item to return
        
        first = first.next;
        head.next = first;            // delete first node
        if (isEmpty()) last = null;   // to avoid loitering
        else           first.last = head;
        n--;
        return item;                   // return the saved item
    }
    public Item removeLast()                 // remove and return the item from the end
    {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        
        Item item = last.item;
        
        last = last.last;
        last.next = null;
        if (isEmpty()) last = first;   // to avoid loitering
        n--;
        return item;
    }
    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new ListIterator();
    }
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
    public static void main(String[] args)   // unit testing (optional)
    {
        Deque<Float> deque = new Deque<Float>();
        In in = new In(args[0]);
        
        while (!in.isEmpty()) 
        {
            float item = in.readFloat();
            deque.addLast(item);
        }
        StdOut.println(deque.size());
        deque.addFirst((float) 1);
        StdOut.println(deque.removeLast());
        StdOut.println(deque.isEmpty());
        deque.addFirst((float) 4);
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.size());
        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());
    }
}