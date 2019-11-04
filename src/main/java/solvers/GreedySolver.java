package solvers;

import parsers.Instance;

public class GreedySolver extends LocalSearchSolver {
    public GreedySolver(Instance instance) {
        super(instance);
        this.name = "greedy";
    }

    @Override
    protected void improvePermutation() {
        boolean swappedAtLeastOnce;
        do {
            swappedAtLeastOnce = false;
            for (int i = 0; i < instance.getDimension() - 1; i++) {
                for (int j = i + 1; j <= instance.getDimension() - 1; j++) {
                    if (this.getImprovement(i, j) > 0) {
                        swap(i, j);
                        swappedAtLeastOnce = true;
                    }
                }
            }
        } while (swappedAtLeastOnce);
    }
}
