import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * This class provides methods for calculating the Percolation.
 * Create only one WeightedQuickUnionUF object to solve the backwash problem.
 * 
 * @author zhangyu
 * @date 2017.2.27
 */
public class Percolation 
{
    private static final byte BLOCKED         = Byte.valueOf("0"); // 00000000
    private static final byte OPEN            = Byte.valueOf("1"); // 00000001
    private static final byte TOPCONNECTED    = Byte.valueOf("2"); // 00000010
    private static final byte BOTTOMCONNECTED = Byte.valueOf("4"); // 00000100
    private static final byte PERCOLATED      = Byte.valueOf("7"); // 00000111
    private int size; // size of created grid
    private byte[] state; // record the state of each site
    private int openSites; // number of open sites
    private boolean percolate = false;
    private WeightedQuickUnionUF grid;
    
    /**
     * Initializes one WeightedQuickUnionUF object, without 
     * any virtual site. Set state of each site blocked.
     * 
     * @param  n size of created grid
     * @throws IllegalArgumentException if n < 1
     */
    public Percolation(int n) // create n-by-n grid, with all sites blocked
    {
        if (n < 1) throw new IllegalArgumentException("Grid size out of range");
        size = n;
        state = new byte[n * n];
        for (int i = 0; i < n * n; i++) state[i] = BLOCKED;
        grid = new WeightedQuickUnionUF(n * n);
    }
    
    /**
     * open site (row, col) if it is not open already.
     * 
     * @param row abscissa of the grid 
     * @param col ordinate of the grid
     */
    public void open(int row, int col)
    {
        validate(row, col);
        
        int idx = xyTo1D(row, col);
        int[] xDiff = {-1, 1, 0, 0};
        int[] yDiff = {0, 0, -1, 1};
        
        // if the site has been opened, then return
        if (0 != (state[idx] & OPEN)) return;
        state[idx] = (byte)(state[idx] | OPEN);
        if (1 == row)    state[idx] = (byte)(state[idx] | TOPCONNECTED);
        if (size == row) state[idx] = (byte)(state[idx] | BOTTOMCONNECTED);
        
        for (int i = 0; i < 4; i++)
        {
            int adjX = row + xDiff[i];
            int adjY = col + yDiff[i];
            
            if (adjX > 0 && adjX <= size)
            {
                if (adjY > 0 && adjY <= size)
                {
                    int adjSiteIdx = xyTo1D(adjX, adjY); // index of the adjacent site
                    int rootASIdx = grid.find(adjSiteIdx);
                    
                    if (OPEN == (state[adjSiteIdx] & OPEN)) // if the adjacent site is open
                    {
                        state[idx] = (byte)(state[idx] | state[rootASIdx]);
                        grid.union(idx, adjSiteIdx); // union the two components
                    }
                }
            }
        }
        
        int rootIdx = grid.find(idx); // must get root index after union operation
        
        state[rootIdx] = (byte)(state[rootIdx] | state[idx]);
        if (PERCOLATED == (byte)(state[rootIdx] & PERCOLATED))
            percolate = true;
        openSites++;
    }
    
    /**
     * Determine whether the site (row, col) is open.
     * 
     * @param row abscissa of the grid 
     * @param col ordinate of the grid
     * @return true if the site (row, col) is open;
     *         false otherwise
     */
    public boolean isOpen(int row, int col)
    {
        validate(row, col);
        return OPEN == (state[xyTo1D(row, col)] & OPEN);
    }
    
    /**
     * Determine whether the site (row, col) is full.
     * 
     * @param row abscissa of the grid 
     * @param col ordinate of the grid
     * @return true if the site (row, col) is full;
     *         false otherwise
     */
    public boolean isFull(int row, int col)
    {
        validate(row, col);
        
        int rootIdx = grid.find(xyTo1D(row, col)); // root index of given site (row, col)
        return isOpen(row, col) && TOPCONNECTED == (state[rootIdx] & TOPCONNECTED);
    }
    
    /**
     * Returns the number of open sites.
     * 
     * @return the number of open sites
     */
    public int numberOfOpenSites()
    {
        return openSites;
    }

    /**
     * Determine whether the grid percolates.
     * 
     * @return true if the grid percolates.
     *         false otherwise
     */
    public boolean percolates()
    {
        return percolate;
    }
    
    /**
     * map 2D coordinates to 1D coordinates
     * 
     * @param row abscissa of the grid 
     * @param col ordinate of the grid
     * @return index converted from the 2D coordinates
     */
    private int xyTo1D(int row, int col)
    {
        return (row - 1)*size + col - 1;
    }
    
    // validate that (row, col) is valid
    private void validate(int row, int col)
    {
        if (row < 1 || row > size || col < 1 || col > size) 
            throw new IndexOutOfBoundsException("index: (" + row + ", " + col + ") are out of bounds");
    }
    
    // test client (optional)
    public static void main(String[] args)
    {
        int n = StdIn.readInt();
        Percolation client = new Percolation(n);
        while (!StdIn.isEmpty())
        {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            client.open(row, col);
            StdOut.println(client.isOpen(row, col));
            StdOut.println(client.isFull(row, col));
            StdOut.println(client.percolates());
        }
    }
}