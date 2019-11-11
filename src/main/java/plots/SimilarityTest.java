package plots;

import metrics.SimilarityMeasure;
import parsers.Instance;
import solvers.GreedySolver;
import solvers.Solver;
import solvers.SteepestSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SimilarityTest {
    private static final int NUMBER_OF_POINTS = 300;

    /***
     * Generate data to create scatter plot: score -> similarity
     */
    public static void run(String instanceName) {
        Instance instance = new Instance(new File(instanceName));
        String outputFileName = "./Results/Similarity/"
                + instanceName.substring(10, instanceName.length() - 5)
                + ".csv";

        List<GreedySolver> greedySolutions = new ArrayList<>();
        List<SteepestSolver> steepestSolutions = new ArrayList<>();
        Solver bestSolution = null;

        try (PrintWriter writer = new PrintWriter(new File(outputFileName))) {
            for (int i = 0; i < NUMBER_OF_POINTS; i++) {
                GreedySolver greedySolver = new GreedySolver(instance);
                SteepestSolver steepestSolver = new SteepestSolver(instance);
                greedySolver.solve();
                greedySolutions.add(greedySolver);
                steepestSolver.solve();
                steepestSolutions.add(steepestSolver);

                if (bestSolution == null) {
                    bestSolution = greedySolver;
                }

                if (bestSolution.getCost() > greedySolver.getCost()) {
                    bestSolution = greedySolver;
                }

                if (bestSolution.getCost() > steepestSolver.getCost()) {
                    bestSolution = steepestSolver;
                }

            }
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < NUMBER_OF_POINTS; i++) {
                int greedyDistance = SimilarityMeasure.getLevenshteinDistance(greedySolutions.get(i), bestSolution);
                double greedyScore = greedySolutions.get(i).getScore();

                int steepestDistance = SimilarityMeasure.getLevenshteinDistance(steepestSolutions.get(i), bestSolution);
                double steepestScore = steepestSolutions.get(i).getScore();

                sb.append(greedyDistance);
                sb.append(", ");
                sb.append(greedyScore);
                sb.append(", ");
                sb.append(steepestDistance);
                sb.append(", ");
                sb.append(steepestScore);
                sb.append("\n");
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
