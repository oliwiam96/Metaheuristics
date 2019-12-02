package solvers;

import parsers.Instance;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class SimulatedAnnealingSolver extends LocalSearchSolver {

    private double temperature;
    private int MAX_ITER;
    private int L_MARKOV_CHAIN_LENGTH;
    private int P_NO_IMPROVEMENT;

    public SimulatedAnnealingSolver(Instance instance) {
        super(instance);
        this.name = "annealing";
    }

    public SimulatedAnnealingSolver(Instance instance, double temperature, int MAX_ITER, int L_MARKOV_CHAIN_LENGTH,
                                    int P_NO_IMPROVEMENT) {
        super(instance);
        this.name = "annealing";
        this.temperature = temperature;
        this.MAX_ITER = MAX_ITER;
        this.L_MARKOV_CHAIN_LENGTH = L_MARKOV_CHAIN_LENGTH;
        this.P_NO_IMPROVEMENT = P_NO_IMPROVEMENT;
    }

    /**
     * TODO adjust temperature to be based on avg cost between edges (maybe in the initial permutation?)
     */
    @Override
    protected void improvePermutation() {
        Random random = new Random();
        temperature = (this.getCost()/instance.getDimension())*2;
        int iterationNumber = 0;
        MAX_ITER = 2000 * instance.getDimension() * instance.getDimension();
        L_MARKOV_CHAIN_LENGTH = instance.getDimension()*instance.getDimension()*2;
        P_NO_IMPROVEMENT = 9;
        int lMarkov = 0;
//        try (PrintWriter writer = new PrintWriter(new File("test.csv"))) {
            StringBuilder sb = new StringBuilder();
            int noImprovementIterationNumber = 0;
            do {
                for (int i = 0; i < instance.getDimension() - 1; i++) {
                    for (int j = i + 1; j <= instance.getDimension() - 1; j++) {
                        int improvement = this.getImprovement(i, j);
                        if (improvement > 0) {
                            // System.out.println("Swapping better " + improvement);
                            swap(i, j);
                            noImprovementIterationNumber = 0;
                        } else {
//                        System.out.println("p: " + Math.exp((double) improvement / temperature));
//                        System.out.println("x: " + (double) improvement / temperature);
//                        System.out.println("temp: " + temperature);
                            if (Math.exp((double) improvement / temperature) > random.nextDouble()) {
                                // System.out.println("Swapping worse " + improvement);
                                swap(i, j);
                            }
                        }
                        noImprovementIterationNumber += 1;
                        iterationNumber += 1;
                        lMarkov += 1;
                        if (lMarkov == L_MARKOV_CHAIN_LENGTH) {
                            temperature *= 0.9;
                            lMarkov = 0;
                        }
                    }
                }
//            sb.append(this.getCost());
//                sb.append("\n");
            } while (temperature > 0.01 && noImprovementIterationNumber <= P_NO_IMPROVEMENT * L_MARKOV_CHAIN_LENGTH);
//        System.out.println(iterationNumber);
//        System.out.println(temperature);
//            writer.write(sb.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        }
    }
