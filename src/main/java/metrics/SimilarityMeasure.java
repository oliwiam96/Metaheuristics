package metrics;

import solvers.Solver;

import java.util.Arrays;

public class SimilarityMeasure {

    public static int getLevenshteinDistance(Solver firstSolution, Solver secondSolution) {
        int[] s = firstSolution.getPermutation();
        int[] t = secondSolution.getPermutation();

        return getLevenshteinDistance(s, t);
    }

    public static int getLevenshteinDistance(int[] s, int[] t) {
        t = getRolled(s, t);
        int n = s.length;

        int[][] d = new int[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            d[i][0] = i;
            d[0][i] = i;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = 1;
                if (s[i - 1] == t[j - 1]) {
                    cost = 0;
                }

                d[i][j] = minimum(
                        d[i - 1][j] + 1,
                        d[i][j - 1] + 1,
                        d[i - 1][j - 1] + cost
                );
            }
        }

        return d[n][n];
    }

    private static int minimum(int a, int b, int c) {
        return a < b ? Math.min(a, c) : Math.min(b, c);
    }

    // roll t so it starts with the same number as s
    private static int[] getRolled(int[] s, int[] t) {
        int firstNumber = s[0];
        int index = 0;
        for (int i = 0; i < t.length; i++) {
            if (t[i] == firstNumber) {
                index = i;
                break;
            }
        }

        int[] begging = Arrays.copyOfRange(t, index, t.length);
        int[] end = Arrays.copyOfRange(t, 0, index);

        t = Arrays.copyOf(begging, t.length);
        System.arraycopy(end, 0, t, begging.length, end.length);
        return t;
    }

    public static void main(String[] args) {
        int[] s = {1, 0, 0, 1, 1, 1, 0, 1}; //{1, 2, 3, 5, 4};
        //int t[] = {0, 0, 1, 1, 1, 0, 0, 1}; //{1, 2, 3, 7, 4};
        int[] t = {1, 1, 1, 0, 0, 1, 0, 0};

        System.out.println(getLevenshteinDistance(s, t));
    }
}
