import parsers.Instance;
import plots.InitialFinalTest;
import solvers.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    private static int MAX_COUNTER = 10;
    private static int MAX_MILLIS = 1000;


    private static double getCJU(Solver solver) {
        int counter = 0;
        long startTime = System.currentTimeMillis();
        long endTime;

        do {
            solver.solve();
            counter += 1;
            endTime = System.currentTimeMillis();
        } while (endTime - startTime < MAX_MILLIS && counter < MAX_COUNTER);

        return ((double) (endTime - startTime)) / counter;
    }


    public static void runTest(Path pathToTestFile) {
        System.out.println(pathToTestFile);

    }

    public static void runTests() {
        try (Stream<Path> paths = Files.walk(Paths.get("./Instances/"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(Main::runTest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        runTests();
        InitialFinalTest.run("Instances/dc188.atsp");
        Instance instance = new Instance(new File("Instances/atex1.atsp"));
        System.out.println("Optimal: " + instance.getOptimalValue());

        RandomSolver randomSolver = new RandomSolver(instance);
        randomSolver.solve();
        System.out.println("Random: " + randomSolver.getCost());

        HeuristicSolver heuristicSolver = new HeuristicSolver(instance);
        heuristicSolver.solve();
        System.out.println("Heuristic: " + heuristicSolver.getCost());

        GreedySolver greedySolver = new GreedySolver(instance);
        greedySolver.solve();
        System.out.println("Greedy: " + greedySolver.getCost());
        greedySolver.solveStartingFromH();
        System.out.println("Greedy with H: " + greedySolver.getCost());
        greedySolver.solveStartingFromAntiH();
        System.out.println("Greedy with antiH: " + greedySolver.getCost());

        SteepestSolver steepestSolver = new SteepestSolver(instance);
        steepestSolver.solve();
        System.out.println("Steepest: " + steepestSolver.getCost());
    }
}
