import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats 
{
    private int trials;
    private double[] threshold;
    
    public PercolationStats(int n, int trials) // perform trials independent experiments on an n-by-n grid
    {   
        if (n <= 0)
            throw new IllegalArgumentException("Grid size out of range");
        if (trials <= 0)
            throw new IllegalArgumentException("value of trials out of range");
        this.trials = trials;
        threshold = new double[trials];
        for (int i = 0; i < trials; i++)
        {
            Percolation expModel = new Percolation(n);
            
            while (!expModel.percolates())
            {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                
                expModel.open(row, col);
            }
            threshold[i] = (double)expModel.numberOfOpenSites() / (double)(n*n);
        }
    }

    public double mean() // sample mean of percolation threshold
    {
        return StdStats.mean(threshold);
    }
    public double stddev() // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(threshold);
    }
    public double confidenceLo() // low  endpoint of 95% confidence interval
    {
        return mean() - 1.96*stddev() / Math.sqrt(trials);
    }
    public double confidenceHi() // high endpoint of 95% confidence interval
    {
        return mean() + 1.96*stddev() / Math.sqrt(trials);
    }
    
    public static void main(String[] args)
    {
        int n, trials;
        
        n = StdIn.readInt();
        trials = StdIn.readInt();
        PercolationStats client = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + client.mean());
        StdOut.println("stddev                  = " + client.stddev());
        StdOut.println("95% confidence interval = " + "[" + client.confidenceLo() + ", " 
                                                    + client.confidenceHi() + "]");
    }
}