import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * The {@code Percolation} class provides methods for calculating the Percolation. 
 * Create two WeightedQuickUnionUF objects to solve the backwash problem.
 * 
 * @author zhangyu
 * @date 2017.2.27
 */
public class Percolation 
{
    private int size; // size of created grid
    private int[] status; // record the status of each site
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF gridNoVBottom;
    
    /**
     * Initializes two WeightedQuickUnionUF object. One has two 
     * virtual sites, and the other one has only one top site. 
     * Set state of each site blocked.
     * 
     * @param  n size of created grid
     * @throws IllegalArgumentException if n < 1
     */
    public Percolation(int n)
    {
        if (n < 1) throw new IllegalArgumentException("Grid size out of range");
        size = n;
        status = new int[n * n];
        for (int i = 0; i < n * n; i++) status[i] = 0;
        grid = new WeightedQuickUnionUF(n*n + 2);
        gridNoVBottom = new WeightedQuickUnionUF(n*n + 1);
    }
    
    /**
     * open site (row, col) if it is not open already.
     * 
     * @param row abscissa of the grid 
     * @param col ordinate of the grid
     */
    public void open(int row, int col)
    {
        int idx = xyTo1D(row, col);
        
        validate(row, col);
        status[idx] = 1;
        if (1 == row) // connect to the virtual site
        {
            grid.union(idx, size * size);
            gridNoVBottom.union(idx, size * size);
        }
        if (size == row) grid.union(idx, size*size + 1);
        
        int[] xDiff = {-1, 1, 0, 0};
        int[] yDiff = {0, 0, -1, 1};
        
        for (int i = 0; i < 4; i++)
        {
            int adjX = row + xDiff[i];
            int adjY = col + yDiff[i];
            
            if (adjX > 0 && adjX <= size)
            {
                if (adjY > 0 && adjY <= size)
                {
                    int adjPosIdx = xyTo1D(adjX, adjY);
                    if (1 == status[adjPosIdx])
                    {
                        grid.union(idx, adjPosIdx);
                        gridNoVBottom.union(idx, adjPosIdx);
                    }
                }
            }
        }
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
        return status[xyTo1D(row, col)] == 1;
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
        return isOpen(row, col) && gridNoVBottom.connected(xyTo1D(row, col), size * size);
    }
    
    /**
     * Returns the number of open sites.
     * 
     * @return the number of open sites
     */
    public int numberOfOpenSites()
    {
        int sum = 0;
        
        for (int i = 0; i < status.length; i++) sum += status[i];
        return sum;
    }

    /**
     * Determine whether the grid percolates.
     * 
     * @return true if the grid percolates.
     *         false otherwise
     */
    public boolean percolates()
    {
        return grid.connected(size * size, size*size + 1);
    }
    
    //map 2D coordinates to 1D coordinates 
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
  
    /**
     * Unit tests the {@code Percolation} data type.
     *
     * @param args the command-line arguments
     */
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
