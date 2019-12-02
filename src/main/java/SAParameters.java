import parsers.Instance;
import solvers.SimulatedAnnealingSolver;

import java.io.File;

public class SAParameters {

    public void test()
    {
        Instance instance = new Instance(new File("Instances/code253.atsp"));

        int MAXV = Integer.MAX_VALUE;
        SimulatedAnnealingSolver test = new SimulatedAnnealingSolver(instance);
        double temperatures[] = {test.getCost(),test.getCost()/instance.getDimension(),test.getCost()/2};
        int size = instance.getDimension()*instance.getDimension();
        int max_iters[] = {size/2};
        int avg = 0;
        int ps[] = {(int)Math.sqrt(instance.getDimension()),6,7,8,9,10,11};
        int markovs[] = {instance.getDimension()*2,instance.getDimension()*instance.getDimension(),instance.getDimension()*instance.getDimension()*4};
        int execs = 20;

        for(int a=0;a<temperatures.length;a++)
            for(int b=0;b<max_iters.length;b++)
                for(int c=0;c<ps.length;c++)
                    for(int d=0;d<markovs.length;d++) {
                        for (int i = 0; i < execs; i++) {
                            SimulatedAnnealingSolver simulatedAnnealingSolver = new SimulatedAnnealingSolver(instance
                                    , temperatures[a], max_iters[b], markovs[d], ps[c]);
                            simulatedAnnealingSolver.solve();
                           // System.out.println("Simulated annealing: " + simulatedAnnealingSolver.getCost());
                            avg += simulatedAnnealingSolver.getCost();
                        }
                        avg /= execs;
                        if (avg < MAXV) {
                            MAXV = avg;
                            System.out.println(MAXV);
                            avg=0;
                            System.out.println("Temperature:" + a);
                            System.out.println("MAX_ITERS: " + b);
                            System.out.println("Markov: " + d);
                            System.out.println("P: "+ c);

                        }
                    }

    }
}
