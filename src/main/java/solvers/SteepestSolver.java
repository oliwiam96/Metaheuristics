package solvers;

import parsers.Instance;

public class SteepestSolver extends LocalSearchSolver {
    public SteepestSolver(Instance instance) {
        super(instance);
        this.name = "steepest";
    }

    @Override
    protected void improvePermutation() {
        boolean swappedAtLeastOnce;
        do {
            swappedAtLeastOnce = false;
            int bestFirstIndex = 0;
            int bestSecondIndex = 1;
            int bestImprovement = 0;

            for (int i = 0; i < instance.getDimension() - 1; i++) {
                for (int j = i + 1; j <= instance.getDimension() - 1; j++) {
                    int currentImprovement = getImprovement(i, j);
                    if (currentImprovement > bestImprovement) {
                        bestFirstIndex = i;
                        bestSecondIndex = j;
                        bestImprovement = currentImprovement;
                    }
                }
            }

            if (bestImprovement > 0) {
                swap(bestFirstIndex, bestSecondIndex);
                swappedAtLeastOnce = true;
            }
        } while (swappedAtLeastOnce);
    }
}
