package solvers;

import parsers.Instance;

import java.util.Random;

public class RandomWalkSolver extends RandomSolver {
    public RandomWalkSolver(Instance instance) {
        super(instance);
        this.name = "rwalk";
    }

    public void solve(long time)
    {
//        int i=0;
        this.shuffle();
        Random random = new Random();
        long start = System.nanoTime();
        do{
            int i = random.nextInt(instance.getDimension());
            int j = random.nextInt(instance.getDimension());
            this.swap(i,j);
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
