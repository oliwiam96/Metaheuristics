package solvers;

import parsers.Instance;

import java.util.Random;

public class SimulatedAnnealingSolver extends LocalSearchSolver {

    public SimulatedAnnealingSolver(Instance instance) {
        super(instance);
        this.name = "annealing";
    }

    /**
     * TODO adjust temperature to be based on avg cost between edges (maybe in the initial permutation?)
     */
    @Override
    protected void improvePermutation() {
        Random random = new Random();
        double temperature = 1000 * instance.getDimension() * instance.getDimension();
        int iterationNumber = 0;
        int MAX_ITER = 1000 * instance.getDimension() * instance.getDimension();
        do {
//            int i = random.nextInt(instance.getDimension());
//            int j = random.nextInt(instance.getDimension());
            for (int i = 0; i < instance.getDimension() - 1; i++) {
                for (int j = i + 1; j <= instance.getDimension() - 1; j++) {
                    int improvement = this.getImprovement(i, j);
                    if (improvement > 0) {
                        // System.out.println("Swapping better " + improvement);
                        swap(i, j);
                    } else {
//                        System.out.println("p: " + Math.exp((double) improvement / temperature));
//                        System.out.println("x: " + (double) improvement / temperature);
//                        System.out.println("temp: " + temperature);
                        if (Math.exp((double) improvement / temperature) > random.nextDouble()) {
                            // System.out.println("Swapping worse " + improvement);
                            swap(i, j);
                        }
                    }
                    temperature *= 0.9999;
                    iterationNumber += 1;
                }
            }
        } while (iterationNumber <= MAX_ITER && temperature > 0.00001);
//        System.out.println(iterationNumber);
//        System.out.println(temperature);
    }
}
