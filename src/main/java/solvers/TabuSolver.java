package solvers;

import parsers.Instance;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TabuSolver extends LocalSearchSolver {
    private int bestCost;
    protected int[] bestPermutation;
    private int currentCost;
    private int[][] tabuMatrix;
    private int MAX_TABU_ITER; // how many iterations element is "tabu"
    private int k; // max tabu list length
    private List<Element> masterList = new ArrayList<>();
    private int threshold = -1000; // when master list should be rebuilt
    private int MAX_NO_IMPROVEMENT;

    public TabuSolver(Instance instance) {
        super(instance);
        this.name = "tabu";
        tabuMatrix = new int[instance.getDimension()][instance.getDimension()];
        k = instance.getDimension() / 10;
        MAX_TABU_ITER = instance.getDimension() / 4;
        MAX_NO_IMPROVEMENT = instance.getDimension() * 16;
    }

    public class Element {
        int firstIndex;
        int secondIndex;
        int improvement;
        int tabuFactor;

        public Element(int firstIndex, int secondIndex, int improvement) {
            this.firstIndex = firstIndex;
            this.secondIndex = secondIndex;
            this.improvement = improvement;
        }

        public int getImprovement() {
            return improvement;
        }
    }

    private void sortMasterList() {
        masterList.sort(Comparator.comparing(Element::getImprovement).reversed());
    }

    private void buildMasterList() {
        masterList = new ArrayList<>();
        for (int i = 0; i < instance.getDimension() - 1; i++) {
            for (int j = i + 1; j <= instance.getDimension() - 1; j++) {
                Element element = new Element(i, j, getImprovement(i, j));
                this.seenNum++;
                masterList.add(element);
            }
        }

        sortMasterList();
        updateMasterListTabuFactors();
        masterList = masterList.stream().filter(this::isNotTabu).collect(Collectors.toList());
        masterList = masterList.subList(0, k);
    }

    private void updateTabuFactor(Element element) {
        int i = permutation[element.firstIndex];
        int j = permutation[element.secondIndex];

        if (i < j) {
            // swap so that i is always bigger than j
            int temp = j;
            j = i;
            i = temp;
        }

        element.tabuFactor = tabuMatrix[i][j];
    }

    private void updateMasterListTabuFactors() {
        masterList.forEach(this::updateTabuFactor);
    }

    private boolean isNotTabu(Element element) {
        if (element.tabuFactor == 0) {
            return true;
        } else if (currentCost - element.improvement < bestCost) { // global criteria aspiration
            return true;
        } else {
            return false;
        }
    }

    private Element getBestElement() {
        // update costs in master list
        for (Element element : masterList) {
            element.improvement = getImprovement(element.firstIndex, element.secondIndex);
        }

        sortMasterList();
        updateMasterListTabuFactors();
        List<Element> acceptedMasterList = masterList.stream().filter(this::isNotTabu).collect(Collectors.toList());

        if (acceptedMasterList.size() > 0 && acceptedMasterList.get(0).improvement > threshold) {
            return acceptedMasterList.get(0);
        } else {
            buildMasterList();
            return masterList.get(0);
        }
    }

    private void updateTabuMatrix(Element element) {
        for (int i = 0; i < tabuMatrix.length; i++) {
            for (int j = 0; j < tabuMatrix.length; j++) {
                if (tabuMatrix[i][j] > 0) {
                    tabuMatrix[i][j] -= 1;
                }
            }
        }

        int i = permutation[element.firstIndex];
        int j = permutation[element.secondIndex];

        if (i < j) {
            // swap so that i is always bigger than j
            int temp = j;
            j = i;
            i = temp;
        }
        tabuMatrix[i][j] = MAX_TABU_ITER;
    }


    @Override
    protected void improvePermutation() {
        int noImprovementIterationNumber = 0;

        this.currentCost = getInitialCost();
        this.bestCost = currentCost;
        buildMasterList();
        Element previousElement = null;
        threshold = (-3) * this.getCost() / instance.getDimension();

        do {
            Element element = getBestElement();
            masterList.remove(element);
            updateTabuMatrix(element);

            currentCost -= this.getImprovement(element.firstIndex, element.secondIndex);
            swap(element.firstIndex, element.secondIndex);
            if (currentCost < bestCost) {
                bestCost = currentCost;
                bestPermutation = Arrays.copyOf(permutation, permutation.length);
                noImprovementIterationNumber = 0;
            } else {
                noImprovementIterationNumber += 1;
            }

        } while (noImprovementIterationNumber < MAX_NO_IMPROVEMENT);

        this.permutation = bestPermutation;
    }
}
