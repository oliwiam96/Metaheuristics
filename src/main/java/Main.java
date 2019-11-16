import parsers.Instance;
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
//        SimilarityTest.run("Instances/atex4.atsp");
//        RestartTest.run("Instances/code253.atsp");
//        InitialFinalTest.run("Instances/code253.atsp");
        Instance instance = new Instance(new File("Instances/PK131_2.atsp"));

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

        SimulatedAnnealingSolver simulatedAnnealingSolver = new SimulatedAnnealingSolver(instance);
        simulatedAnnealingSolver.solve();
        System.out.println("Simulated annealing: " + simulatedAnnealingSolver.getCost());

        TabuSolver tabuSolver = new TabuSolver(instance);
        tabuSolver.solve();
        System.out.println("Tabu: " + tabuSolver.getCost());


        // pomiar score dla wszystkich solverów i instancji
//        try (Stream<Path> paths = Files.walk(Paths.get("./Instances/"))) {
//            List<String> names = paths
//                    .filter(Files::isRegularFile)
//                    .map(x -> x.getFileName().toString())
//                    .collect(Collectors.toList());
//            for (String name : names
//            ) {
//                ScoreTimeMeasurement.runSolvers(name);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
