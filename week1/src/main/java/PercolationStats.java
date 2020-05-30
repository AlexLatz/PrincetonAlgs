import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] stats;
    private final double mean;
    private final double stddev;
    private final double CONFIDENCE_95 = 1.96;
    public PercolationStats(final int n, final int trials) {
        if (n < 1 || trials < 1)
            throw new IllegalArgumentException();
        this.stats = new double[trials];
        for (int i = 0; i < trials; i++) {
            final Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            stats[i] = (double) percolation.numberOfOpenSites() / (double) (n * n);
        }
        this.mean = StdStats.mean(stats);
        this.stddev = StdStats.stddev(stats);
    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.stddev;
    }

    public double confidenceLo() {
        return this.mean - (CONFIDENCE_95 * this.stddev / Math.sqrt((double) stats.length));
    }

    public double confidenceHi() {
        return this.mean + (CONFIDENCE_95 * this.stddev / Math.sqrt((double) stats.length));
    }

    public static void main(final String[] args) {
        final PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}