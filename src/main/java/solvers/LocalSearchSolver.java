package solvers;

import parsers.Instance;

public abstract class LocalSearchSolver extends HeuristicSolver {
    public LocalSearchSolver(Instance instance) {
        super(instance);
    }

    /***
     * If > 0 then swap is profitable.
     * @param firstIndex first index to swap
     * @param secondIndex second index to swap
     * @return difference between old cost and cost after swap.
     */
    protected final int getImprovement(int firstIndex, int secondIndex) {
        int[][] edges = instance.getEdges();
        int dimension = instance.getDimension();

        int costBeforeSwap = edges[permutation[(firstIndex - 1 + dimension) % dimension]][permutation[firstIndex]]
                + edges[permutation[firstIndex]][permutation[(firstIndex + 1) % dimension]]
                + edges[permutation[(secondIndex - 1 + dimension) % dimension]][permutation[secondIndex]]
                + edges[permutation[secondIndex]][permutation[(secondIndex + 1) % dimension]];

        int costAfterSwap = edges[permutation[(firstIndex - 1 + dimension) % dimension]][permutation[secondIndex]]
                + edges[permutation[secondIndex]][permutation[(firstIndex + 1) % dimension]]
                + edges[permutation[(secondIndex - 1 + dimension) % dimension]][permutation[firstIndex]]
                + edges[permutation[firstIndex]][permutation[(secondIndex + 1) % dimension]];

        return costBeforeSwap - costAfterSwap;
    }

    protected abstract void improvePermutation();

    @Override
    public void solve() {
        super.shuffle();
        improvePermutation();
    }

    public void solveStartingFromH() {
        this.generateHPermutation();
        improvePermutation();
    }

    public void solveStartingFromAntiH() {
        this.generateAntiHPermutation();
        improvePermutation();
    }
}