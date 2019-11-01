import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Instance {
    private String name;
    private int dimension;
    private int[][] edges;
    private int optimalValue;

    public Instance(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(" ");
                if (words[0].equals("NAME:")) {
                    this.name = words[1];
                }
                if (words[0].equals("DIMENSION:")) {
                    this.dimension = Integer.parseInt(words[1]);
                }
                if (words[0].equals("OPTIMAL:")) {
                    this.optimalValue = Integer.parseInt(words[1]);
                }
                if (line.startsWith("EDGE_WEIGHT_SECTION")) {
                    int[][] edgeMatrix = new int[this.dimension][this.dimension];
                    String row;
                    int i = 0;
                    row = br.readLine();
                    while ((row != null && !row.startsWith("EOF"))) {
                        String[] values = row.split("\t");
                        for (int j = 0; j < this.dimension; j++) {
                            edgeMatrix[i][j] = (int) Double.parseDouble(values[j]);
                        }
                        i++;
                        row = br.readLine();
                    }
                    this.edges = edgeMatrix;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public int getDimension() {
        return dimension;
    }

    public int[][] getEdges() {
        return edges;
    }

    public int getOptimalValue() {
        return optimalValue;
    }

    public int getCost(int[] solution) {
        int cost = 0;
        for (int i = 0; i < solution.length; i++) {
            int currentIndex = solution[i];
            int nextIndex = solution[(i + 1) % solution.length];
            cost += edges[currentIndex][nextIndex];
        }
        return cost;
    }

    public double getScore(int[] solutrion) {
        int cost = this.getCost(solutrion);
        return (double) (cost - this.getOptimalValue()) / this.getOptimalValue();
    }

    /***
     * If > 0 then swap is profitable.
     * @param firstIndex first index to swap
     * @param secondIndex second index to swap
     * @param solution permutation of indexes
     * @return difference between old cost and cost after swap.
     */
    public int getImprovement(int firstIndex, int secondIndex, int[] solution) {
        int costBeforeSwap = edges[solution[(firstIndex - 1 + dimension) % dimension]][solution[firstIndex]]
                + edges[solution[firstIndex]][solution[(firstIndex + 1) % dimension]]
                + edges[solution[(secondIndex - 1 + dimension) % dimension]][solution[secondIndex]]
                + edges[solution[secondIndex]][solution[(secondIndex + 1) % dimension]];

        int costAfterSwap = edges[solution[(firstIndex - 1 + dimension) % dimension]][solution[secondIndex]]
                + edges[solution[secondIndex]][solution[(firstIndex + 1) % dimension]]
                + edges[solution[(secondIndex - 1 + dimension) % dimension]][solution[firstIndex]]
                + edges[solution[firstIndex]][solution[(secondIndex + 1) % dimension]];

        return costBeforeSwap - costAfterSwap;
    }
}
