package parsers;

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
}
