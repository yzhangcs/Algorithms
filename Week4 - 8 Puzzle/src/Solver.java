import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver 
{
    MinPQ<Node> pq = new MinPQ<Node>();
    Board initial;
    Node curNode;
    Node preNode;
    int moves;
    
    /**
     * find a solution to the initial board (using the A* algorithm)
     * 
     * @param initial
     */
    public Solver(Board initial)
    {
        this.initial = initial;
        moves = 0;
        pq.insert(new Node(initial, null, initial.manhattan()));
    }
    
    /**
     * is the initial board solvable?
     * 
     * @return
     */
    public boolean isSolvable()
    {
        Board twin = initial.twin();
        
        curNode = pq.delMin();
        while (!curNode.bd.isGoal())
        {
            for (Board nb : curNode.bd.neighbors())
                if (curNode.preNode == null || !nb.equals(curNode.preNode.bd))
                    pq.insert(new Node(nb, curNode, nb.manhattan() + moves));
            curNode = pq.delMin();
            moves++;
        }
        if (curNode.bd.isGoal()) return true;
        curNode = new Node(twin, null, twin.manhattan());
        moves = 0;
        while (!curNode.bd.isGoal())
        {
            for (Board nb : curNode.bd.neighbors())
                if (curNode.preNode == null || !nb.equals(curNode.preNode.bd))
                    pq.insert(new Node(nb, curNode, nb.manhattan() + moves));
            curNode = pq.delMin();
            moves++;
        }
        if (curNode.bd.isGoal()) return true;
        return false;
    }
    
    /**
     * min number of moves to solve initial board; -1 if unsolvable
     * 
     * @return
     */
    public int moves()
    {
        if (!isSolvable()) return -1;
        else return moves;
    }
    
    /**
     * sequence of boards in a shortest solution; null if unsolvable
     * 
     * @return
     */
    public Iterable<Board> solution()
    {
        if (!isSolvable()) return null;
        
        Stack<Board> boards = new Stack<Board>();
        
        while(curNode != null) 
        {
            boards.push(curNode.bd);
            curNode = curNode.preNode;
        }
        return boards;
    }
    
    private class Node implements Comparable<Node>
    {
        private Board bd;
        private Node preNode;
        int priority;
        
        public Node(Board bd, Node preNode, int priority)
        {
            this.bd = bd;
            this.preNode = preNode;
            this.priority = priority;
        }   
        public int compareTo(Node that)
        { return this.priority - that.priority; }
    }

    /**
     * solve a slider puzzle (given below)
     * 
     * @param args
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
