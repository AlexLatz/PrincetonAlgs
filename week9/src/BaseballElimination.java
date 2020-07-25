import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final HashMap<String, Integer> teamNames;
    private final HashMap<Integer, String> reverse;
    private final HashMap<String, ArrayList<String>> certificates;
    private final int[] wins;
    private final int[] losses;
    private final int[] totalRemain;
    private final int[][] remaining;
    private final boolean[] eliminated;

    public BaseballElimination(final String filename) {
        final In in = new In(filename);
        teamNames = new HashMap<>();
        certificates = new HashMap<>();
        reverse = new HashMap<>();
        final int size = in.readInt();
        wins = new int[size];
        losses = new int[size];
        totalRemain = new int[size];
        remaining = new int[size][size];
        eliminated = new boolean[size];
        for (int i = 0; i < size; i++) {
            final String team = in.readString();
            teamNames.put(team, i);
            reverse.put(i, team);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            totalRemain[i] = in.readInt();
            for (int j = 0; j < size; j++) {
                remaining[i][j] = in.readInt();
            }
        }
        trivialEliminate();
        eliminate();
    }

    public int numberOfTeams() {
        return wins.length;
    }

    public Iterable<String> teams() {
        return teamNames.keySet();
    }

    public int wins(final String team) {
        checkTeam(team);
        return wins[teamNames.get(team)];
    }

    public int losses(final String team) {
        checkTeam(team);
        return losses[teamNames.get(team)];
    }

    public int remaining(final String team) {
        checkTeam(team);
        return totalRemain[teamNames.get(team)];
    }

    public int against(final String team1, final String team2) {
        checkTeam(team1);
        checkTeam(team2);
        return remaining[teamNames.get(team1)][teamNames.get(team2)];
    }

    public boolean isEliminated(final String team) {
        checkTeam(team);
        return eliminated[teamNames.get(team)];
    }

    public Iterable<String> certificateOfElimination(final String team) {
        checkTeam(team);
        return certificates.get(team);
    }

    private void checkTeam(final String team) {
        if (team == null || !teamNames.containsKey(team)) throw new IllegalArgumentException();
    }

    private void trivialEliminate() {
        int bestTeam = 0;
        int bestWins = 0;
        for (int i = 0; i < numberOfTeams(); i++) {
            if (wins[i] > bestWins) {
                bestTeam = i;
                bestWins = wins[bestTeam];
            }
        }
        for (int t = 0; t < numberOfTeams(); t++) {
            if (wins[t] + totalRemain[t] < bestWins) {
                eliminated[t] = true;
                certificates.put(reverse.get(t), new ArrayList<>());
                certificates.get(reverse.get(t)).add(reverse.get(bestTeam));
            }
        }
    }

    private void eliminate() {
        for (int i = 0; i < numberOfTeams(); i++) {
            if (!eliminated[i]) {
                final ArrayList<int[]> vertGame = new ArrayList<>();
                for (int j = 0; j < numberOfTeams(); j++) {
                    if (j == i) continue;
                    for (int k = j + 1; k < numberOfTeams(); k++) {
                        if (k == i) continue;
                        if (remaining[j][k] != 0) {
                            vertGame.add(new int[]{j, k});
                        }
                    }
                }
                final FlowNetwork flow = new FlowNetwork(vertGame.size() + numberOfTeams() + 1);
                for (int v = 0; v < vertGame.size(); v++) {
                    flow.addEdge(new FlowEdge(vertGame.size() + numberOfTeams() - 1, v, remaining[vertGame.get(v)[0]][vertGame.get(v)[1]]));
                    if (vertGame.get(v)[0] >= i) vertGame.get(v)[0]--;
                    if (vertGame.get(v)[1] >= i) vertGame.get(v)[1]--;
                    flow.addEdge(new FlowEdge(v, vertGame.get(v)[0] + vertGame.size(), Double.POSITIVE_INFINITY));
                    flow.addEdge(new FlowEdge(v, vertGame.get(v)[1] + vertGame.size(), Double.POSITIVE_INFINITY));
                }
                final int wR = wins[i] + totalRemain[i];
                for (int t = vertGame.size(); t < vertGame.size() + numberOfTeams() - 1; t++) {
                    int convTeam = t - vertGame.size();
                    if (convTeam >= i) convTeam++;
                    flow.addEdge(new FlowEdge(t, vertGame.size() + numberOfTeams(), wR - wins[convTeam]));
                }
                final FordFulkerson ff = new FordFulkerson(flow, vertGame.size() + numberOfTeams() - 1, vertGame.size() + numberOfTeams());
                for (final FlowEdge e : flow.adj(vertGame.size() + numberOfTeams() - 1)) {
                    if (e.flow() != e.capacity()) {
                        eliminated[i] = true;
                    }
                }
                if (eliminated[i]) {
                    certificates.put(reverse.get(i), new ArrayList<String>());
                    for (int t = vertGame.size(); t < vertGame.size() + numberOfTeams() - 1; t++) {
                        int team = t - vertGame.size();
                        if (team >= i) team++;
                        if (ff.inCut(t)) certificates.get(reverse.get(i)).add(reverse.get(team));
                    }
                }
            }
        }
    }

    public static void main(final String[] args) {
        final BaseballElimination division = new BaseballElimination(args[0]);
        for (final String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (final String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}