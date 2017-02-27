import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * This class performs independent experiment, using the Percolation 
 * data structure. Then compute the mean and standard deviation of results.
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

    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(threshold);
    }
    
    // sample standard deviation of percolation threshold
    public double stddev()
    {
        return StdStats.stddev(threshold);
    }
    
    // low  endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean() - 1.96*stddev() / Math.sqrt(trials);
    }
    
    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean() + 1.96*stddev() / Math.sqrt(trials);
    }
    
    // test client (optional)
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