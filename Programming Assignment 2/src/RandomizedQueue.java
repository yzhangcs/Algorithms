import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> 
{
    private Item[] a;         // array of items
    private int n;          // number of elements on queue
    
    public RandomizedQueue() // construct an empty randomized queue
    {
        a = (Item[]) new Object[2];
        n = 0;
    }
    public boolean isEmpty() // is the queue empty?
    {
        return n == 0;
    }
    public int size() // return the number of items on the queue
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
    public void enqueue(Item item) // add the item
    {
        // double size of array if necessary and recopy to front of array
        if (n == a.length) resize(2 * a.length);   // double size of array if necessary
        a[n++] = item;                        // add node
    }
    public Item dequeue() // remove and return a random item
    {
        int randomizedIdx = StdRandom.uniform(n);
        Item item;
        
        item = a[randomizedIdx];
        a[randomizedIdx] = a[n];
        a[n--] = null;
        return item;
    }
    public Item sample() // return (but do not remove) a random item
    {
        int randomizedIdx = StdRandom.uniform(n);
        
        return a[randomizedIdx];
    }
    public Iterator<Item> iterator() // return an independent iterator over items in random order
    {
        return new ListIterator();
    }
    private class ListIterator implements Iterator<Item>
    {
        private int[] randomIndices = StdRandom.permutation(n);
        private int i = 0;
        
        public boolean hasNext()  { return i < n;                              }
        public void remove()      { throw new UnsupportedOperationException(); }

        public Item next() 
        {
            if (!hasNext()) throw new NoSuchElementException();
            return a[randomIndices[i++]];
        }
    }
    public static void main(String[] args) // unit testing (optional)
    {
        
    }
}