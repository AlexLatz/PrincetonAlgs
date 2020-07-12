import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet net;
    public Outcast(final WordNet wordnet) {
        net = wordnet;
    }

    public String outcast(final String[] nouns) {
        int max = 0;
        int index = 0;
        for (int i = 0; i < nouns.length; i++) {
            int sum = 0;
            for (final String noun : nouns) {
                sum += net.distance(nouns[i], noun);
            }
            if (sum > max) {
                max = sum;
                index = i;
            }
        }
        return nouns[index];
    }

    public static void main(final String[] args) {
        final WordNet wordnet = new WordNet(args[0], args[1]);
        final Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            final In in = new In(args[t]);
            final String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}