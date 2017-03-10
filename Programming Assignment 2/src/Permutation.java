import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code Permutation} class implements a client that takes a command-line 
 * integer k; reads in a sequence of strings from standard input; and prints 
 * exactly k of them, uniformly at random. 
 * Each item from the sequence is printed at most once.
 *
 * @author zhangyu
 * @date 2017.3.10
 */
public class Permutation
{
    /**
     * Unit tests the {@code Permutation} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);
        
        while (!StdIn.isEmpty())
        {
            String item = StdIn.readString();
            rq.enqueue(item);
        }
        for (int i = 0; i < k; ++i) StdOut.println(rq.dequeue());
    }
}
