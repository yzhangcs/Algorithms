import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * The {@code RandomizedQueue} class represents a randomized queue, which is
 * similar to a stack or queue, except that the item removed is chosen uniformly
 * at random from items in the data structure.
 * <p>
 * This implementation uses a resizing array, which double the underlying array
 * when it is full and halves the underlying array when it is one-quarter full.
 *
 * @author zhangyu
 * @date 2017.3.10
 */
public class RandomizedQueue<Item> implements Iterable<Item>
{
    private Item[] a;
    private int n;

    /**
     * Constructs an empty randomized queue.
     */
    public RandomizedQueue()
    {
        a = (Item[]) new Object[2];
        n = 0;
    }

    /**
     * Returns whether the randomized queue is empty.
     *
     * @return true if this randomized queue is empty;
     *         false otherwise
     */
    public boolean isEmpty()
    {
        return n == 0;
    }

    /**
     * Returns the number of items on the queue.
     *
     * @return the number of items on the queue
     */
    public int size()
    {
        return n;
    }

    // resize the underlying array
    private void resize(int capacity)
    {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++)
            temp[i] = a[i];
        a = temp;
    }

    /**
     * Adds the item to this queue.
     *
     * @param item the item to add
     * @throws NullPointerException if the item to be added is null
     */
    public void enqueue(Item item)
    {
        if (item == null) throw new NullPointerException("Null item");
        if (n == a.length) resize(2 * a.length); // double the size of array if necessary
        a[n++] = item;
    }

    /**
     * Removes and returns a random item
     *
     * @return a random item on this queue
     * @throws NoSuchElementException if the queue is empty
     */
    public Item dequeue()
    {
        if (isEmpty()) throw new NoSuchElementException("Empty randomized queue");

        int randomizedIdx = StdRandom.uniform(n);
        Item item;

        item = a[randomizedIdx];
        a[randomizedIdx] = a[n - 1]; // move the last item to the empty position
        a[--n] = null; // to avoid loitering
        // shrink the size of array if necessary
        if (n > 0 && n == a.length/4) resize(a.length/2);
        return item;
    }

    /**
     * Returns (but do not removes) a random item.
     *
     * @return a random item on this queue without removing it
     * @throws NoSuchElementException if the queue is empty
     */
    public Item sample()
    {
        if (isEmpty()) throw new NoSuchElementException("Empty randomized queue");

        int randomizedIdx = StdRandom.uniform(n);

        return a[randomizedIdx];
    }

    /**
     * Returns an independent iterator over items in random order.
     *
     * @return an independent iterator over items in random order
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Item> iterator()
    {
        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item>
    {
        private int[] randomIndices = StdRandom.permutation(n);
        private int i = 0;

        public boolean hasNext() { return i < n;                              }
        public void remove()     { throw new UnsupportedOperationException(); }

        public Item next()
        {
            if (!hasNext()) throw new NoSuchElementException();
            return a[randomIndices[i++]];
        }
    }

    /**
     * Unit tests the {@code RandomizedQueue} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        while (!StdIn.isEmpty())
        {
            String item = StdIn.readString();
            if (!item.equals("-"))
                rq.enqueue(item);
            else if (!rq.isEmpty())
                rq.dequeue();
            for (String str : rq)
                StdOut.print(str + " ");
            StdOut.println("\t" + rq.isEmpty());
        }
    }
}