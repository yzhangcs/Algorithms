import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board
{
    private int[][] blocks;
    private int rowOfBlankSquare, colOfBlankSquare;
    private int n;
    
    /**
     * construct a board from an n-by-n array of blocks
     * (where blocks[i][j] = block in row i, column j)
     * 
     * @param blocks
     */
    public Board(int[][] blocks)
    {
        if (blocks == null) throw new NullPointerException("Null blocks");
        
        n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; ++i)
        {
            for (int j = 0; j < n; ++j)
            {
                this.blocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) 
                {
                    rowOfBlankSquare = i;
                    colOfBlankSquare = j;
                }
            }
        }
    }
    
    /**
     * board n n
     * 
     * @return
     */
    public int n()
    {
        return n;
    }
    
    /**
     * number of blocks out of place
     * 
     * @return
     */
    public int hamming()
    {
        int hamming = 0;
        
        for (int i = 0; i < n; ++i) 
            for (int j = 0; j < n; ++j)
                if (blocks[i][j] != i * n + j + 1) hamming++;    
        return hamming;
    }
    
    /**
     * sum of Manhattan distances between blocks and goal
     * 
     * @return
     */
    public int manhattan()
    {
        int manhattan = 0;
        
        for (int i = 0; i < n; ++i) 
        {
            for (int j = 0; j < n; ++j)
            {
                int goalRow = (blocks[i][j] - 1) / n;
                int goalCol = (blocks[i][j] - 1) % n;
                
                if (goalRow != i || goalCol != j)
                    manhattan += Math.abs(goalRow - i) + Math.abs(goalCol - j);
            }
        }
        return manhattan;
    }
    
    /**
     * is this board the goal board?
     * 
     * @return
     */
    public boolean isGoal()
    {
        return this.hamming() == 0;
    }
    
    /**
     * a board that is obtained by exchanging any pair of blocks
     * 
     * @return
     */
    public Board twin()
    {
        int row1, col1;
        int row2, col2;
        
        row1 = col1 = 0;
        row2 = col2 = n - 1;
        if (blocks[row1][col1] == 0)      col1++;
        else if (blocks[row2][col2] == 0) col2--;   
        return new Board(getSwappedBlocks(row1, col1, row2, col2));
    }
    
    /**
     * does this board equal y?
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object y)
    {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        
        Board that = (Board) y;
        
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                if (this.blocks[i][j] != that.blocks[i][j])
                    return false;
        return true;
    }
    
    /**
     * all neighboring boards
     * 
     * @return
     */
    public Iterable<Board> neighbors()
    {
        Queue<Board> neighbors = new Queue<Board>();
        int[] xDiff = {-1, 1, 0, 0};
        int[] yDiff = {0, 0, -1, 1};
        int[][] swappedBlocks;
        
        for (int i = 0; i < 4; ++i)
        {
            int adjRow = rowOfBlankSquare + xDiff[i];
            int adjCol = colOfBlankSquare + yDiff[i];
            
            if (adjRow >= 0 && adjRow < n && adjCol >= 0 && adjCol < n)
            {
                swappedBlocks = getSwappedBlocks(rowOfBlankSquare, colOfBlankSquare,
                                                 adjRow, adjCol);
                neighbors.enqueue(new Board(swappedBlocks));
            }
        }
        return neighbors;
    }
    
    private int[][] getSwappedBlocks(int x1, int y1, int x2, int y2)
    {
        int[][] swappedBlocks = new int[n][n];
        int temp;
        
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                swappedBlocks[i][j] = blocks[i][j];
        temp = swappedBlocks[x1][y1];
        swappedBlocks[x1][y1] = swappedBlocks[x2][y2];
        swappedBlocks[x2][y2] = temp;
        return swappedBlocks;
    }
    
    /**
     * string representation of this board (in the output format specified below)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuilder board = new StringBuilder();
        board.append(n + "\n");
        for (int i = 0; i < n; i++) 
        {
            for (int j = 0; j < n; j++) 
                board.append(String.format("%2d ", blocks[i][j]));
            board.append("\n");
        }
        return board.toString();
    }
    
    /**
     * unit tests (not graded) 
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        Board as = new Board(new int[3][3]);
        Board sa = new Board(new int[3][3]);
        StdOut.print(as.equals(sa));
    }
}