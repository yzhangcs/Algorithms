import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation 
{
    private int size; // size of created grid
    private int[] status; // record the status of each site
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF gridWithoutVBottom;
    
    public Percolation(int n) // create n-by-n grid, with all sites blocked
    {
        if (n <= 0)
            throw new IllegalArgumentException("Grid size out of range");
        size = n;
        status = new int[n * n];
        for (int i = 0; i < n * n; i++) status[i] = 0;
        grid = new WeightedQuickUnionUF(n*n + 2);
        gridWithoutVBottom = new WeightedQuickUnionUF(n*n + 1);
    }
    public void open(int row, int col) // open site (row, col) if it is not open already
    {
        int idx = xyTo1D(row, col);
        
        validate(row, col);
        status[idx] = 1;
        if (1 == row) // connect to the virtual site
        {
            grid.union(idx, size * size);
            gridWithoutVBottom.union(idx, size * size);
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
                        gridWithoutVBottom.union(idx, adjPosIdx);
                    }
                }
            }
        }
    }
    public boolean isOpen(int row, int col) // is site (row, col) open?
    {
        validate(row, col);
        return status[xyTo1D(row, col)] == 1;
    }
    public boolean isFull(int row, int col) // is site (row, col) full?
    {
        validate(row, col);
        
        if (isOpen(row, col)) // only the open site can be full
            return gridWithoutVBottom.connected(xyTo1D(row, col), size * size);
        else 
            return false;
    }
    public int numberOfOpenSites() // number of open sites
    {
        int sum = 0;
        
        for (int i = 0; i < status.length; i++) sum += status[i];
        return sum;
    }
    public boolean percolates() // does the system percolate?
    {
        return grid.connected(size * size, size*size + 1);
    }
    private int xyTo1D(int row, int col) // map 2D coordinates to 1D coordinates
    {
        return (row - 1)*size + col - 1;
    }
    private void validate(int row, int col) // validate that (row, col) is valid
    {
        if (row < 1 || row > size) 
            throw new IndexOutOfBoundsException("row index " + row + " out of bounds");
        if (col < 1 || col > size) 
            throw new IndexOutOfBoundsException("col index " + col + " out of bounds");  
    }
    
    public static void main(String[] args)
    {
        Percolation client = new Percolation(3);
        while (true) 
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