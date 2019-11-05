package plots;

import parsers.Instance;
import solvers.GreedySolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class RestartTest {
    /**
     * Generate data to create plot: number of restarts -> avg, best solution
     */
    public static void run(String instanceName) {
        Instance instance = new Instance(new File(instanceName));
        String outputFileName = "./Results/Restart/"
                + instanceName.substring(10, instanceName.length() - 5)
                + ".csv";

        try (PrintWriter writer = new PrintWriter(new File(outputFileName))) {
            StringBuilder sb = new StringBuilder();
            for (int i = 10; i <= 500; i += 10) {
                int minCost = Integer.MAX_VALUE;

                int sumOfCosts = 0;
                for (int j = 0; j < i; j++) {
                    GreedySolver greedySolver = new GreedySolver(instance);
                    greedySolver.solve();

                    int cost = greedySolver.getCost();
                    sumOfCosts += cost;
                    if (cost < minCost) {
                        minCost = cost;
                    }
                }
                double minScore = (double) (minCost - instance.getOptimalValue()) / instance.getOptimalValue();
                double averageScore = (double) ((double) sumOfCosts / i - instance.getOptimalValue()) / instance.getOptimalValue();

                sb.append(i); // number of restarts
                sb.append(", ");
                sb.append(averageScore);
                sb.append(", ");
                sb.append(minScore);
                sb.append("\n");
            }
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
