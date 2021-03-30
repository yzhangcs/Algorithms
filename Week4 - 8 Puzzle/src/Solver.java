import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code Solver} class provides solution for the 
 * 8-puzzle problem (and its natural generalizations) 
 * using the A* search algorithm. 
 *
 * @author zhangyu
 * @date 2017.3.29
 */
public class Solver 
{
    private SearchNode current; // current search node
    private boolean isSolvable;
    
    // the class implements the compareTo() method for MinPQ
    private class SearchNode implements Comparable<SearchNode>
    {
        private Board bd;
        private SearchNode previous;
        private int moves;
        private int priority; // cache the priority to avoid repeated calculations
        private boolean isInitParity; // is equal to initial parity
        
        // this constructor is used for initail node and its twin
        public SearchNode(Board bd, boolean isInitParity) 
        {
            if (bd == null) 
                throw new NullPointerException("Null Board");          
            this.bd = bd;
            this.moves = 0;
            this.priority = this.bd.manhattan() + this.moves;
            this.isInitParity = isInitParity;
        }   
        
        // for the later nodes
        public SearchNode(Board bd, SearchNode previous)
        {
            if (bd == null) 
                throw new NullPointerException("Null Board");
            if (previous == null) 
                throw new NullPointerException("Null SearchNode");   
            this.bd = bd;
            this.previous = previous;
            this.moves = previous.moves + 1;
            this.priority = this.bd.manhattan() + this.moves;
            this.isInitParity = previous.isInitParity;
        }   
        
        public int compareTo(SearchNode that)
        { 
            // When two search nodes have the same Manhattan priority, 
            // break ties by comparing the Manhattan distances of the two boards. 
            if (this.priority == that.priority) 
                return this.bd.manhattan() - that.bd.manhattan();
            else 
                return this.priority - that.priority; 
        }
    }

    
    /**
     * Find a solution to the initial board (using the A* algorithm).
     * 
     * @param initial the initial Board
     * @throws NullPointerException if initial Board is null
     */
    public Solver(Board initial)
    {
        if (initial == null) throw new NullPointerException("Null Board");  

        MinPQ<SearchNode> origPQ = new MinPQ<SearchNode>();
        
        // isInitParity of initial is true, and another is false.
        origPQ.insert(new SearchNode(initial, true)); // insert initial node and its twin
        origPQ.insert(new SearchNode(initial.twin(), false));
        while (true)
        {
            current = origPQ.delMin();
            if (current.bd.isGoal()) break;
            for (Board nb : current.bd.neighbors())
                if (current.previous == null || 
                    !nb.equals(current.previous.bd))
                    origPQ.insert(new SearchNode(nb, current));          
        } // only one of the two nodes can lead to the goal board
        isSolvable = current.isInitParity && current.bd.isGoal();
    }
    
    /**
     * Determines whether the initial board is solvable?
     * 
     * @return true if the initial board is solvable;
     *         false otherwise.
     */
    public boolean isSolvable()
    {
        return this.isSolvable;
    }
    
    /**
     * Returns min number of moves to solve initial board; -1 if unsolvable.
     * 
     * @return min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves()
    {
        if (!isSolvable()) return -1;
        else return current.moves;
    }
    
    /**
     * Returns sequence of boards in a shortest solution; null if unsolvable.
     * 
     * @return sequence of boards in a shortest solution;
     *         null if unsolvable.
     */
    public Iterable<Board> solution()
    {
        if (!isSolvable()) return null;
        
        Stack<Board> boards = new Stack<Board>();
        SearchNode node = current; // another reference
        
        while (node != null) 
        {
            boards.push(node.bd);
            node = node.previous;
        }
        return boards;
    }
    
    /**
     * Unit tests the {@code solver} data type to 
     * solve a slider puzzle (given below).
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
