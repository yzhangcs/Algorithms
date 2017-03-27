import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver 
{
    private MinPQ<SearchNode> initPQ;
    private MinPQ<SearchNode> twinPQ;
    private SearchNode currSearchNode;
    private SearchNode twinSearchNode;
    
    /**
     * find a solution to the initial board (using the A* algorithm)
     * 
     * @param initial
     */
    public Solver(Board initial)
    {
        if (initial == null) throw new NullPointerException("Null Board");
        
        initPQ = new MinPQ<SearchNode>();
        twinPQ = new MinPQ<SearchNode>();
        currSearchNode = new SearchNode(initial, null);
        twinSearchNode = new SearchNode(initial.twin(), null);
    }
    
    /**
     * is the initial board solvable?
     * 
     * @return
     */
    public boolean isSolvable()
    {
        while (!currSearchNode.getBoard().isGoal() && 
               !twinSearchNode.getBoard().isGoal())
        {
            for (Board nb : currSearchNode.getBoard().neighbors())
                if (currSearchNode.getPreSearchNode() == null || 
                    !nb.equals(currSearchNode.getPreSearchNode().getBoard()))
                    initPQ.insert(new SearchNode(nb, currSearchNode));
            for (Board nb : twinSearchNode.getBoard().neighbors())
                if (twinSearchNode.getPreSearchNode() == null || 
                    !nb.equals(twinSearchNode.getPreSearchNode().getBoard()))
                    twinPQ.insert(new SearchNode(nb, twinSearchNode));
            currSearchNode = initPQ.delMin();
            twinSearchNode = twinPQ.delMin();
        }
        return currSearchNode.getBoard().isGoal();
    }
    
    /**
     * min number of moves to solve initial board; -1 if unsolvable
     * 
     * @return
     */
    public int moves()
    {
        if (!isSolvable()) return -1;
        else return currSearchNode.moves;
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
        
        while (currSearchNode != null) 
        {
            boards.push(currSearchNode.bd);
            currSearchNode = currSearchNode.previous;
        }
        return boards;
    }
    
    private class SearchNode implements Comparable<SearchNode>
    {
        private Board bd;
        private SearchNode previous;
        private int manhattan;
        private int moves;
        private int priority;
        
        public SearchNode(Board bd, SearchNode previous)
        {
            this.bd = bd;
            this.previous = previous;
            this.manhattan = this.bd.manhattan();
            this.moves = previous != null ? previous.moves + 1 : 0;
            this.priority = this.manhattan + this.moves;
        }   
        public int compareTo(SearchNode that)
        { return this.priority - that.priority; }
        public Board getBoard()
        { return this.bd; }
        public SearchNode getPreSearchNode()
        { return this.previous; }
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
