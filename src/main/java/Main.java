import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    private static int MAX_COUNTER = 10;
    private static int MAX_MILLIS = 1000;

    private static void swap(int[] array, int firstIndex, int secondIndex) {
        int temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }

    private static int[] getPermutation(int N) {
        int[] indexes = IntStream.range(0, N).toArray();
        int lastIndex = N;
        Random random = new Random();
        for (int j = 0; j < N; j++) {
            int randomInteger = random.nextInt(lastIndex);
            swap(indexes, randomInteger, lastIndex - 1);
            lastIndex -= 1;
        }
        return indexes;
    }

    private static List<int[]> getPermutations(int N, int numberOfPermutations) {
        List<int[]> permutations = new ArrayList<>();
        for (int i = 0; i < numberOfPermutations; i++) {
            permutations.add(getPermutation(N));
        }
        return permutations;
    }

    private static void doAlgorithm() {
        int N = 10000000;
        List<int[]> permutations = getPermutations(N, 3);
    }

    private static double getCJU() {
        int counter = 0;
        long startTime = System.currentTimeMillis();
        long endTime;

        do {
            doAlgorithm();
            counter += 1;
            endTime = System.currentTimeMillis();
        } while (endTime - startTime < MAX_MILLIS && counter < MAX_COUNTER);

        return ((double) (endTime - startTime)) / counter;
    }

    public static int findMinIndex(int[] row, Set notVisited) {
        int min = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < row.length; i++) {
            if (row[i] < min && notVisited.contains(i)) {
                min = row[i];
                minIndex = i;
            }
        }
        notVisited.remove(minIndex);
        return minIndex;
    }

    public static int[] getHeuristicSolution(Instance instance) {
        int[] solution = new int[instance.getDimension()];
        solution[0] = 0;
        Set<Integer> notVisited = new HashSet<>();
        for (int i = 1; i < solution.length; i++) {
            notVisited.add(i);
        }
        for (int i = 1; i < solution.length; i++) {
            int nextCity = findMinIndex(instance.getEdges()[i - 1], notVisited);
            solution[i] = nextCity;
        }
        return solution;
    }

    public static int[] getGreedySolution(int[] permutation, Instance instance) {
        boolean swappedAtLeastOnce;
        do {
            swappedAtLeastOnce = false;
            for (int i = 0; i < instance.getDimension() - 1; i++) {
                for (int j = i + 1; j <= instance.getDimension() - 1; j++) {
                    if (instance.getImprovement(i, j, permutation) > 0) {
                        swap(permutation, i, j);
                        swappedAtLeastOnce = true;
                    }
                }
            }
        } while (swappedAtLeastOnce);
        return permutation;
    }

    public static int[] getSteepestSolution(int[] permutation, Instance instance) {
        boolean swappedAtLeastOnce;
        do {
            swappedAtLeastOnce = false;
            int bestFirstIndex = 0;
            int bestSecondIndex = 1;
            int bestImprovement = 0;

            for (int i = 0; i < instance.getDimension() - 1; i++) {
                for (int j = i + 1; j <= instance.getDimension() - 1; j++) {
                    int currentImprovement = instance.getImprovement(i, j, permutation);
                    if (currentImprovement > bestImprovement) {
                        bestFirstIndex = i;
                        bestSecondIndex = j;
                        bestImprovement = currentImprovement;
                    }
                }
            }

            if (bestImprovement > 0) {
                swap(permutation, bestFirstIndex, bestSecondIndex);
                swappedAtLeastOnce = true;
            }
        } while (swappedAtLeastOnce);
        return permutation;
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

    /***
     * Generate data to create scatter plot: initial score -> final score
     */
    public static void runInitialFinalTest(String instanceName) {
        Instance instance = new Instance(new File(instanceName));
        String outputFileName = "./Results/InitialFinalScore/"
                + instanceName.substring(12, instanceName.length() - 5)
                + ".csv";

        try (PrintWriter writer = new PrintWriter(new File(outputFileName))) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 300; i++) {
                int[] permutationOfIndexes = getPermutation(instance.getDimension());
                double initialScore = instance.getScore(permutationOfIndexes);
                sb.append(initialScore);
                System.out.println(initialScore);
                sb.append(", ");
                int[] greedy = getGreedySolution(permutationOfIndexes, instance);
                double finalScore = instance.getScore(greedy);
                sb.append(finalScore);
                sb.append("\n");
            }
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        runTests();
//        runInitialFinalTest("Instances/atex5.atsp");
        Instance instance = new Instance(new File("Instances/atex5.atsp"));
        int[] solution = getHeuristicSolution(instance);

        System.out.println(Arrays.toString(solution));
        System.out.println("Heuristic: " + instance.getCost(solution));
        System.out.println("Optimal: " + instance.getOptimalValue());
        System.out.println("CJU: " + getCJU());

        int[] greedy = getGreedySolution(getPermutation(instance.getDimension()), instance);
        System.out.println("Greedy: " + instance.getCost(greedy));

        int[] steepest = getSteepestSolution(getPermutation(instance.getDimension()), instance);
        System.out.println("Steepest: " + instance.getCost(steepest));

        int[] greedyWithH = getGreedySolution(solution, instance);
        System.out.println("Greedy with H: " + instance.getCost(greedyWithH));
    }
}
