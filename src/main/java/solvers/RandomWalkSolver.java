package solvers;

import parsers.Instance;

import java.util.Random;

public class RandomWalkSolver extends RandomSolver {
    public RandomWalkSolver(Instance instance) {
        super(instance);
        this.name = "rwalk";
    }

    public void solve(long time) {
        this.seenNum = 0;
        this.shuffle();
        Random random = new Random();
        long start = System.nanoTime();
        do {
            int i = random.nextInt(instance.getDimension());
            int j = random.nextInt(instance.getDimension());
            double improvement = this.getImprovement(i, j);
            this.swap(i, j);
            this.seenNum++;
            if (improvement > 0) {
                this.bestPermutation = this.permutation.clone();
            }
//            i++;
        } while (System.nanoTime() - start < time);
//        System.out.println(i);
        this.permutation = this.bestPermutation;
    }
}
