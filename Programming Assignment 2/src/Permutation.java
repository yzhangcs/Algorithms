import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation
{
    public static void main(String[] args)
    {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        int k = StdIn.readInt();
        
        while (!StdIn.isEmpty())
        {
            String item = StdIn.readString();
            queue.enqueue(item);
        }
        for (int i = 0; i < k; ++i) StdOut.println(queue.dequeue());
    }
}
