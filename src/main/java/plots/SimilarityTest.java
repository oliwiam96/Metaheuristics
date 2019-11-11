package plots;

import metrics.SimilarityMeasure;
import parsers.Instance;
import solvers.GreedySolver;
import solvers.HeuristicSolver;
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

    public static void runFromH(String instanceName) {
        runFrom(instanceName, true);
    }

    public static void runFromAntyH(String instanceName) {
        runFrom(instanceName, false);
    }

    private static void runFrom(String instanceName, boolean H) {
        Instance instance = new Instance(new File(instanceName));
        String outputFileName = "./Results/Similarity/" + "from" + (H ? "H" : "AntiH")  + "_"
                + instanceName.substring(10, instanceName.length() - 5)
                + ".csv";

        List<GreedySolver> greedySolutions = new ArrayList<>();
        GreedySolver greedyFromHSolver = new GreedySolver(instance);
        if (H) {
            greedyFromHSolver.solveStartingFromH();
        } else {
            greedyFromHSolver.solveStartingFromAntiH();
        }
        Solver bestSolution = greedyFromHSolver;

        try (PrintWriter writer = new PrintWriter(new File(outputFileName))) {
            for (int i = 0; i < NUMBER_OF_POINTS; i++) {
                GreedySolver greedySolver = new GreedySolver(instance);
                greedySolver.solve();
                greedySolutions.add(greedySolver);

            }
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < NUMBER_OF_POINTS; i++) {
                int greedyDistance = SimilarityMeasure.getLevenshteinDistance(greedySolutions.get(i), bestSolution);
                double greedyScore = greedySolutions.get(i).getScore();

                sb.append(greedyDistance);
                sb.append(", ");
                sb.append(greedyScore);
                sb.append("\n");
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void runOnlyH(String instanceName) {
        Instance instance = new Instance(new File(instanceName));
        String outputFileName = "./Results/Similarity/" + "onlyH" + "_"
                + instanceName.substring(10, instanceName.length() - 5)
                + ".csv";

        List<GreedySolver> greedySolutions = new ArrayList<>();
        HeuristicSolver heuristicSolver = new HeuristicSolver(instance);
        heuristicSolver.solve();
        Solver bestSolution = heuristicSolver;

        try (PrintWriter writer = new PrintWriter(new File(outputFileName))) {
            for (int i = 0; i < NUMBER_OF_POINTS; i++) {
                GreedySolver greedySolver = new GreedySolver(instance);
                greedySolver.solve();
                greedySolutions.add(greedySolver);

            }
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < NUMBER_OF_POINTS; i++) {
                int greedyDistance = SimilarityMeasure.getLevenshteinDistance(greedySolutions.get(i), bestSolution);
                double greedyScore = greedySolutions.get(i).getScore();

                sb.append(greedyDistance);
                sb.append(", ");
                sb.append(greedyScore);
                sb.append("\n");
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SimilarityTest.runFromH("Instances/code253.atsp");
        SimilarityTest.runFromH("Instances/atex1.atsp");
        SimilarityTest.runFromAntyH("Instances/atex1.atsp");
        SimilarityTest.runOnlyH("Instances/atex1.atsp");
    }
}
