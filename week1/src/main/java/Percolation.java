public class Percolation {
    private final int[] nums;
    private final int[] size;
    private final boolean[] open;
    private final int rowsize;
    private int count;

    public Percolation(final int n) {
        if (n < 1) throw new IllegalArgumentException();
        this.nums = new int[n * n + 2];
        this.size = new int[n * n + 2];
        this.open = new boolean[n * n + 2];
        this.rowsize = n;
        for (int i = 0; i < this.nums.length; i++) {
            this.nums[i] = i;
            this.size[i] = 1;
        }
        this.count = 0;
        this.open[0] = true;
        this.open[this.open.length - 1] = true;
    }

    private void union(final int p, final int q) {
        final int rootP = find(p);
        final int rootQ = find(q);
        if (rootP != rootQ) {
            if (this.size[rootP] > this.size[rootQ]) {
                this.nums[rootQ] = rootP;
                this.size[rootP] += this.size[rootQ];
            } else {
                this.nums[rootP] = rootQ;
                this.size[rootQ] += this.size[rootP];
            }
        }
    }

    private void unionIfOpen(final int n, final int n2) {
        if (this.open[n2]) {
            union(n, n2);
        }
    }

    private int convert(final int row, final int col) {
        if (row > this.rowsize || col > this.rowsize || row < 1 || col < 1) {
            throw new IllegalArgumentException();
        }
        return (row - 1) * this.rowsize + col;
    }

    public void open(final int row, final int col) {
        final int n = convert(row, col);
        if (!this.open[n]) {
            if (n < rowsize + 1) {
                if (n % rowsize == 1)
                    unionIfOpen(n, n + 1);
                else if (n % rowsize == 0)
                    unionIfOpen(n, n - 1);
                else {
                    unionIfOpen(n, n - 1);
                    unionIfOpen(n, n + 1);
                }
                union(n, 0);
                unionIfOpen(n, n + rowsize);
            } else if (n > rowsize * rowsize - rowsize) {
                if (n % rowsize == 1)
                    unionIfOpen(n, n + 1);
                else if (n % rowsize == 0)
                    unionIfOpen(n, n - 1);
                else {
                    unionIfOpen(n, n - 1);
                    unionIfOpen(n, n + 1);
                }
                union(n, this.nums.length - 1);
                unionIfOpen(n, n - rowsize);
            } else if (n % rowsize == 1) {
                unionIfOpen(n, n + 1);
                unionIfOpen(n, n + rowsize);
                unionIfOpen(n, n - rowsize);
            } else if (n % rowsize == 0) {
                unionIfOpen(n, n - 1);
                unionIfOpen(n, n + rowsize);
                unionIfOpen(n, n - rowsize);
            } else {
                unionIfOpen(n, n - 1);
                unionIfOpen(n, n + 1);
                unionIfOpen(n, n + rowsize);
                unionIfOpen(n, n - rowsize);
            }
            this.open[n] = true;
            this.count++;
        }
    }

    public boolean isOpen(final int row, final int col) {
        final int n = convert(row, col);
        return this.open[n];
    }

    public boolean isFull(final int row, final int col) {
        final int n = convert(row, col);
        return connected(n, 0);
    }

    private int find(final int p) {
        int i = this.nums[p];
        while (i != this.nums[i]) {
            this.nums[i] = this.nums[this.nums[i]];
            i = this.nums[i];
        }
        this.nums[p] = i;
        return i;
    }

    private boolean connected(final int p, final int q) {
        return find(p) == find(q);
    }
    public boolean percolates() {
        return connected(0, this.nums.length-1);
    }
    public int numberOfOpenSites() {
        return this.count;
    }
}