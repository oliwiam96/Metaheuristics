package plots;

import parsers.Instance;
import solvers.GreedySolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class InitialFinalTest {
    /***
     * Generate data to create scatter plot: initial score -> final score
     */
    public static void run(String instanceName) {
        Instance instance = new Instance(new File(instanceName));
        String outputFileName = "./Results/InitialFinalScore/"
                + instanceName.substring(10, instanceName.length() - 5)
                + ".csv";

        try (PrintWriter writer = new PrintWriter(new File(outputFileName))) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 300; i++) {
                GreedySolver greedySolver = new GreedySolver(instance);
                greedySolver.solve();
                double initialScore = greedySolver.getInitialScore();
                sb.append(initialScore);
                sb.append(", ");
                double finalScore = greedySolver.getScore();
                sb.append(finalScore);
                sb.append("\n");
            }
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        run("Instances/PK66_10.atsp");
    }
}
