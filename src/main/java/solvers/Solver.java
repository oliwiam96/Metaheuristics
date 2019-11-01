package solvers;

import parsers.Instance;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public abstract class Solver {
    protected Instance instance;
    protected int[] permutation;

    public Solver(Instance instance) {
        this.instance = instance;
        this.permutation = IntStream.range(0, instance.getDimension()).toArray();
    }

    protected final void swap(int firstIndex, int secondIndex) {
        int temp = permutation[firstIndex];
        permutation[firstIndex] = permutation[secondIndex];
        permutation[secondIndex] = temp;
    }

    protected final void shuffle() {
        int lastIndex = permutation.length;
        Random random = new Random();
        for (int j = 0; j < permutation.length; j++) {
            int randomInteger = random.nextInt(lastIndex);
            swap(randomInteger, lastIndex - 1);
            lastIndex -= 1;
        }
    }

    public abstract void solve();

    public final int getCost() {
        int cost = 0;
        for (int i = 0; i < permutation.length; i++) {
            int currentIndex = permutation[i];
            int nextIndex = permutation[(i + 1) % permutation.length];
            cost += instance.getEdges()[currentIndex][nextIndex];
        }
        return cost;
    }

    public final double getScore() {
        int cost = this.getCost();
        return (double) (cost - instance.getOptimalValue()) / instance.getOptimalValue();
    }

    public void printPermutation() {
        System.out.println(Arrays.toString(permutation));
    }
}