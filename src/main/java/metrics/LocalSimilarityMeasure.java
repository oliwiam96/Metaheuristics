package metrics;

import javafx.util.Pair;
import solvers.Solver;

import java.util.HashSet;
import java.util.Set;

public class LocalSimilarityMeasure {
    public static double getSimilarity(Solver firstSolution, Solver secondSolution) {
        int[] s = firstSolution.getPermutation();
        int[] t = secondSolution.getPermutation();
        return getSimilarity(s, t);
    }

    public static double getSimilarity(int[] s, int[] t) {
        Set<Pair<Integer, Integer>> sPairs = new HashSet<>();
        Set<Pair<Integer, Integer>> tPairs = new HashSet<>();

        for (int i = 0; i < s.length; i++) {
            sPairs.add(new Pair<>(s[i], s[(i+1)%s.length]));
            tPairs.add(new Pair<>(t[i], t[(i + 1) % t.length]));
        }

        Set<Pair<Integer, Integer>> intersection = new HashSet<>(sPairs);
        intersection.retainAll(tPairs);

        return (double) intersection.size()/ s.length;
    }

    public static void main(String[] args) {
        int[] s = {1, 2, 3, 4, 5, 6}; //{1, 2, 3, 5, 4};
        int[] t = {1, 2, 4, 3, 5, 6};

        System.out.println(getSimilarity(s, t));
    }
}
