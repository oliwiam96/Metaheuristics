package plots;

import parsers.Instance;
import solvers.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ScoreTimeMeasurement {

    private static final int MAX_MILLIS = 1000;
    private static final int MAX_COUNTER = 10;

    /***
     * Generate data to create scatter plot: initial score -> final score
     */
    public static void runSolvers(String instanceName) {
        Instance instance = new Instance(new File("Instances/"+instanceName));
        RandomSolver randomSolver = new RandomSolver(instance);
        HeuristicSolver heuristicSolver = new HeuristicSolver(instance);
        GreedySolver greedySolver = new GreedySolver(instance);
        SteepestSolver steepestSolver = new SteepestSolver(instance);

        Solver[] solvers = {heuristicSolver,steepestSolver,greedySolver};
        long greedyTime = 0;
        for (int i = 0; i < solvers.length; i++) {
            String outputFileName = "./Results/Scores/"
                    + solvers[i].getName()+"_"
                    + instance.getDimension()
                    + ".csv";

            try (PrintWriter writer = new PrintWriter(new File(outputFileName))) {
                StringBuilder sb = new StringBuilder();

                int counter = 0;
                long endTime;

                do {
                    long singleTime = System.nanoTime();
                    solvers[i].solve();
                    double score = solvers[i].getScore();
                    endTime = System.nanoTime();

                    sb.append(score);
                    sb.append(',');
                    sb.append(endTime-singleTime);
                    sb.append("\n");
                    if(solvers[i].getName()=="greedy"){
                        greedyTime = endTime-singleTime;
                    }
                    counter += 1;

                } while (counter < MAX_COUNTER);

                writer.write(sb.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // teraz liczę random z uwzględnieniem czasu wykonania greedy

        String outputFileName = "./Results/Scores/"
                + randomSolver.getName()+"_"
                + instance.getDimension()
                + ".csv";

        try (PrintWriter writer = new PrintWriter(new File(outputFileName))) {
            StringBuilder sb = new StringBuilder();

            int counter = 0;
            long endTime;

            do {
                long singleTime = System.nanoTime();

                randomSolver.solve(greedyTime);
                double score =randomSolver.getScore();
                endTime = System.nanoTime();

                sb.append(score);
                sb.append(',');
                sb.append(endTime-singleTime);
                sb.append("\n");
                counter += 1;

            } while (counter < MAX_COUNTER);

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}


