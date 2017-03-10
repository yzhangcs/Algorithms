import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * The {@code PercolationStats} class performs independent experiment, using 
 * the Percolation data structure. Then compute the mean and standard 
 * deviation of results.
 * <p>
 * It also provides method to compute confidence intervals for them.
 * 
 * @author zhangyu
 * @date 2017.2.27
 */
public class PercolationStats 
{
    private int trials;
    private double[] threshold;
    
    /**
     * perform trials independent experiments on an n-by-n grid
     * 
     * @param  n size of created grid
     * @param  trials times of experiments
     * @throws IllegalArgumentException unless
     *         both grid size and value of trials out of range
     */
    public PercolationStats(int n, int trials)
    {   
        if (n < 1) 
            throw new IllegalArgumentException("Grid size out of range");
        if (trials < 1) 
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

    /**
     * Returns the sample mean of percolation threshold.
     * 
     * @return the sample mean of percolation threshold
     */
    public double mean()
    {
        return StdStats.mean(threshold);
    }
    
    /**
     * Returns the sample standard deviation of percolation threshold.
     * 
     * @return the sample standard deviation of percolation threshold
     */
    public double stddev()
    {
        return StdStats.stddev(threshold);
    }
    
    /**
     * Returns the low endpoint of 95% confidence interval.
     * 
     * @return the low endpoint of 95% confidence interval.
     */
    public double confidenceLo()
    {
        return mean() - 1.96*stddev() / Math.sqrt(trials);
    }
    
    /**
     * Returns the high endpoint of 95% confidence interval.
     * 
     * @return the high endpoint of 95% confidence interval.
     */
    public double confidenceHi()
    {
        return mean() + 1.96*stddev() / Math.sqrt(trials);
    }
    
    /**
     * Unit tests the {@code PercolationStats} data type.
     *
     * @param args the command-line arguments
     */
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