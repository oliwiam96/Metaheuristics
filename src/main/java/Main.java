import java.io.File;
import java.util.*;
import java.util.stream.IntStream;

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
        int firstIndex = 0;
        int secondIndex = 1;
        boolean stop = false;
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < instance.getDimension(); j++) {
                if (instance.getImprovement(firstIndex, secondIndex, permutation) > 0) {
                    swap(permutation, firstIndex, secondIndex);
                }
                secondIndex += 1;
                secondIndex = secondIndex % instance.getDimension();
            }
            firstIndex += 1;
            firstIndex = firstIndex % instance.getDimension();

        }
        return permutation;
    }

    public static void main(String[] args) {
        Instance instance = new Instance(new File("Instances/PK66_10.atsp"));
        int[] solution = getHeuristicSolution(instance);

        System.out.println(Arrays.toString(solution));
        System.out.println("Heuristic: " + instance.getCost(solution));
        System.out.println("Optimal: " + instance.getOptimalValue());
        System.out.println("CJU: " + getCJU());

        int[] greedy = getGreedySolution(getPermutation(instance.getDimension()), instance);
        System.out.println("Greedy: " + instance.getCost(greedy));
        System.out.println(Arrays.toString(greedy));


        int[] greedyWithH = getGreedySolution(solution, instance);
        System.out.println("Greedy with H: " + instance.getCost(greedyWithH));
    }
}
