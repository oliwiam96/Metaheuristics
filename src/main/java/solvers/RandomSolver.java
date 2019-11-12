package solvers;

import parsers.Instance;


public class RandomSolver extends Solver {

    private int[] bestPermutation;
    private double bestScore = Double.MAX_VALUE;
    public RandomSolver(Instance instance) {
        super(instance);
        this.name = "random";
    }

    @Override
    public void solve() {
        this.shuffle();
    }

    public void solve(long time)
    {
//        int i=0;
        long start = System.nanoTime();
        do{
            this.shuffle();
            double currentScore = this.getScore();
            if(currentScore<this.bestScore)
            {
                this.bestPermutation = this.permutation.clone();
                this.bestScore = currentScore;
            }
//            i++;
        }while(System.nanoTime()-start<time);
//        System.out.println(i);
        this.permutation = this.bestPermutation;
    }
}
