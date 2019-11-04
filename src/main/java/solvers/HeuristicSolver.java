package solvers;

import parsers.Instance;

import java.util.HashSet;
import java.util.Set;

public class HeuristicSolver extends Solver {
    public HeuristicSolver(Instance instance) {
        super(instance);
        this.name = "heuristic";
    }

    private int findMinIndex(int[] row, Set notVisited) {
        int min = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < row.length; i++) {
            if (row[i] < min && notVisited.contains(i)) {
                min = row[i];
                minIndex = i;
            }
        }

        notVisited.remove(minIndex);
        return minIndex;
    }

    private int findMaxIndex(int[] row, Set notVisited) {
        int max = Integer.MIN_VALUE;
        int maxIndex = 0;
        for (int i = 0; i < row.length; i++) {
            if (row[i] > max && notVisited.contains(i)) {
                max = row[i];
                maxIndex = i;
            }
        }

        notVisited.remove(maxIndex);
        return maxIndex;
    }

    private void generateHeuristicPermutation(boolean antiH) {
        permutation[0] = 0;
        Set<Integer> notVisited = new HashSet<>();
        for (int i = 1; i < permutation.length; i++) {
            notVisited.add(i);
        }
        for (int i = 1; i < permutation.length; i++) {
            int nextCity;
            if (antiH) {
                nextCity = findMaxIndex(instance.getEdges()[permutation[i - 1]], notVisited);
            } else {
                nextCity = findMinIndex(instance.getEdges()[permutation[i - 1]], notVisited);
            }

            permutation[i] = nextCity;
        }
    }

    protected final void generateHPermutation() {
        generateHeuristicPermutation(false);
    }

    protected final void generateAntiHPermutation() {
        generateHeuristicPermutation(true);
    }

    @Override
    public void solve() {
        generateHPermutation();
    }
}
