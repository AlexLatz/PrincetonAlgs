import java.util.HashMap;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private HashMap<Integer, String> idToSyn;
    private HashMap<String, Bag<Integer>> synToIds;
    private Digraph graph;
    private SAP sap;
    public WordNet(final String synsets, final String hypernyms) {
        idToSyn = new HashMap<>();
        synToIds = new HashMap<>();
        processSynset(synsets);
        processHypernym(hypernyms);
        final DirectedCycle c = new DirectedCycle(graph);
        if (c.hasCycle())
            throw new IllegalArgumentException();
        sap = new SAP(graph);
        int rootNum = 0;
        for (int i = 0; i < graph.V(); i++)
            if (graph.outdegree(i) == 0)
                rootNum++;
        if (rootNum != 1)
            throw new IllegalArgumentException();
    }

    public Iterable<String> nouns() {
        return synToIds.keySet();
    }

    public boolean isNoun(final String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return synToIds.containsKey(word);
    }

    public int distance(final String nounA, final String nounB) {
        checkNouns(nounA, nounB);
        return sap.length(synToIds.get(nounA), synToIds.get(nounB));
    }

    public String sap(final String nounA, final String nounB) {
        checkNouns(nounA, nounB);
        return idToSyn.get(sap.ancestor(synToIds.get(nounA), synToIds.get(nounB)));
    }

    private void processSynset(final String synsets) {
        int count = 0;
        final In in = new In(synsets);
        while (in.hasNextLine()) {
            final String[] line = in.readLine().split(",");
            final String[] words = line[1].split(" ");
            final int num = Integer.parseInt(line[0]);
            idToSyn.put(num, line[1]);
            for (final String s : words) {
                if (synToIds.get(s) == null)
                    synToIds.put(s, new Bag<>());
                synToIds.get(s).add(num);
            }
            count++;
        }
        graph = new Digraph(count);
    }

    private void processHypernym(final String hypernyms) {
        final In in = new In(hypernyms);
        while (in.hasNextLine()) {
            final String[] line = in.readLine().split(",");
            final int num = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                graph.addEdge(num, Integer.parseInt(line[i]));
            }
        }
    }

    private void checkNouns(final String a, final String b) {
        if (a == null || b == null || !isNoun(a) || !isNoun(b)) throw new IllegalArgumentException();
    }
}