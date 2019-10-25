import java.io.*;

public class Instance {
    private String name;
    private int dimension;
    private int[][] edges;
    private int optimalValue;

    public Instance(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                String words[] = line.split(" ");
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
                    while ((row = br.readLine()) != null) {
                        String[] values = row.split("\t");
                        for (int j = 0; j < this.dimension; j++) {
                            edgeMatrix[i][j] = Integer.valueOf(values[j]);
                        }
                        i++;
                    }
                    this.edges = edgeMatrix;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
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
