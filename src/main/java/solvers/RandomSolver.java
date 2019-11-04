package solvers;

import parsers.Instance;

public class RandomSolver extends Solver {
    public RandomSolver(Instance instance) {
        super(instance);
        this.name = "random";
    }

    @Override
    public void solve() {
        this.shuffle();
    }
}
