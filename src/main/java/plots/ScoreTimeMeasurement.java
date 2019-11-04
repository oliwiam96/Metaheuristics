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

        Solver[] solvers = {randomSolver,heuristicSolver,steepestSolver,greedySolver};

        for (int i = 0; i < solvers.length; i++) {
            String outputFileName = "./Results/Scores/"
                    + solvers[i].getName()+"_"
                    + instanceName
                    + ".csv";

            try (PrintWriter writer = new PrintWriter(new File(outputFileName))) {
                StringBuilder sb = new StringBuilder();

                int counter = 0;
                long startTime = System.currentTimeMillis();
                long endTime;

                do {
                    solvers[i].solve();
                    double score = solvers[i].getScore();
                    sb.append(score);
                    sb.append("\n");
                    counter += 1;
                    endTime = System.currentTimeMillis();
                } while (endTime - startTime < MAX_MILLIS || counter < MAX_COUNTER);

                writer.write(sb.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}


