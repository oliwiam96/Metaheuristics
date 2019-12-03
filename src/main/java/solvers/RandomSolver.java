package solvers;

import parsers.Instance;


public class RandomSolver extends LocalSearchSolver {

    protected int[] bestPermutation;
    protected double bestScore = Double.MAX_VALUE;

    public RandomSolver(Instance instance) {
        super(instance);
        this.name = "random";
    }

    @Override
    protected void improvePermutation() {

    }

    @Override
    public void solve() {
        this.shuffle();
    }

    public void solve(long time) {
        this.seenNum = 0;
        long start = System.nanoTime();
        do {
            this.shuffle();
            this.seenNum++;
            double currentScore = this.getScore();
            if (currentScore < this.bestScore) {
                this.bestPermutation = this.permutation.clone();
                this.bestScore = currentScore;
            }
        } while (System.nanoTime() - start < time);
        this.permutation = this.bestPermutation;
    }
}
