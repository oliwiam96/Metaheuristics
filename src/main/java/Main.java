public class Main {
    private static int MAX_COUNTER = 10;
    private static int MAX_MILLIS = 1000;

    private static void doAlgorithm() {
        int N = 10;
        // TODO
    }

    private static double getCJU() {
        int counter = 0;
        long startTime = System.currentTimeMillis();
        long endTime;

        do {
            doAlgorithm();
            counter += 1;
            endTime = System.currentTimeMillis();
        } while (endTime - startTime < MAX_MILLIS || counter < MAX_COUNTER);

        return ((double) (endTime - startTime)) / counter;
    }

    public static void main(String[] args) {
        System.out.println("CJU: " + getCJU());
    }
}
