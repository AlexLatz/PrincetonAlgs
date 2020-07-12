import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private double[][] energyTo;
    public SeamCarver(final Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        energy = new double[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                energy[x][y] = energy(x, y);
            }
        }
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(final int x, final int y) {
        checkCoords(x, y);
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return 1000;
        return Math.sqrt(colorDiff(picture.getRGB(x + 1, y), picture.getRGB(x - 1, y))
                + colorDiff(picture.getRGB(x, y + 1), picture.getRGB(x, y - 1)));
    }

    public int[] findVerticalSeam() {
        energyTo = new double[width()][height()];
        final int[] seam = new int[height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (y == 0)
                    energyTo[x][y] = 0;
                else
                    energyTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                relax(x, y, x, y + 1);
                relax(x, y, x + 1, y + 1);
                relax(x, y, x - 1, y + 1);
            }
        }
        double min = Double.POSITIVE_INFINITY;
        if (height() > 1) {
            for (int x = 0; x < width(); x++) {
                if (energyTo[x][height() - 2] < min) {
                    min = energyTo[x][height() - 2];
                    seam[height() - 2] = x;
                }
            }
            seam[height() - 1] = seam[height() - 2];
        } else {
            seam[height() - 1] = 0; 
            return seam;
        }
        for (int y = height() - 2; y > 0; y--) {
            double minSeam = Double.POSITIVE_INFINITY;
            if (seam[y] - 1 > 0) {
                if (energyTo[seam[y] - 1][y - 1] < minSeam) {
                    minSeam = energyTo[seam[y] - 1][y - 1];
                    seam[y - 1] = seam[y] - 1;
                }
            }
            if (energyTo[seam[y]][y - 1] < minSeam) {
                minSeam = energyTo[seam[y]][y - 1];
                seam[y - 1] = seam[y];
            }
            if (seam[y] + 1 < width()) {
                if (energyTo[seam[y] + 1][y - 1] < minSeam) {
                    seam[y - 1] = seam[y] + 1;
                }
            }
        }
        return seam;
    }

    public int[] findHorizontalSeam() {
        transpose();
        final int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    public void removeVerticalSeam(final int[] seam) {
        if (seam == null || width() <= 1 || seam.length != height())
            throw new IllegalArgumentException();
        checkSeam(seam);
        final Picture newPic = new Picture(width() - 1, height());
        for (int y = 0; y < height(); y++) {
            int curRow = 0;
            for (int x = 0; x < width(); x++) {
                if (x != seam[y]) newPic.setRGB(curRow++, y, picture.getRGB(x, y));
            }
        }
        picture = newPic;
        energy = new double[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                energy[x][y] = energy(x, y);
            }
        }
    }

    public void removeHorizontalSeam(final int[] seam) {
        if (height() <= 1)
            throw new IllegalArgumentException();
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    private void checkSeam(final int[] seam) {
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException();
        }
    }

    private void checkCoords(final int x, final int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException();
    }


    private void transpose() {
        final Picture flipped = new Picture(height(), width());
        final double[][] newEnergy = new double[height()][width()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                flipped.setRGB(y, x, picture.getRGB(x, y));
                newEnergy[y][x] = energy[x][y];
            }
        }
        picture = flipped;
        energy = newEnergy;
    }

    private void relax(final int x, final int y, final int x2, final int y2) {
        if (x2 < 0 || y2 < 0 || x2 > width() - 1 || y2 > height() - 1)
            return;
        if (energyTo[x2][y2] > energyTo[x][y] + energy[x2][y2])
            energyTo[x2][y2] = energyTo[x][y] + energy[x2][y2];
    }

    private double colorDiff(final int c1, final int c2) {
        final int r = ((c1 >> 16) & 0xFF) - ((c2 >> 16) & 0xFF);
        final int g = ((c1 >> 8) & 0xFF) - ((c2 >> 8) & 0xFF);
        final int b = ((c1 >> 0) & 0xFF) - ((c2 >> 0) & 0xFF);
        return r*r + g*g + b*b;
    }
}