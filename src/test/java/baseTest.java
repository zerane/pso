import org.junit.Test;
import psopkg.*;
import psopkg.benchmark.*;
import psopkg.topology.TopologyModel;
import tool.ChartDrawer;
import tool.ContentFactory;
import tool.TestData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/6.
 */
public class baseTest {
    @Test
    public void testPSO(){
        List<PSO> psos = new ArrayList<>();
        List<TestData> data;
        List<CLPSO.Topology> topologyModelList = ContentFactory.clpsoTopologyToTest();
        BenchmarkModel bm = new Rosenbrocks(10);

        for(int i=0;i<topologyModelList.size();i++){
            //System.out.println(psos.get(i).getClass().getSimpleName()+":");
            CLPSO pso = new CLPSO();
            pso.policyFlag = topologyModelList.get(i);
            psos.add(pso);
            run(pso,bm,null);
        }

        data = ContentFactory.psoToData(psos,ContentFactory.CATEGORY_CLPSOTOPOLOGY);

        ChartDrawer cd = new ChartDrawer();
        cd.draw(data);

        try {
            while (true){
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;
    }

    @Test
    public void testBenchMark(){
        int dims=10;
        List<BenchmarkModel> benchmarks = ContentFactory.benchmarkModels(dims);


        for(int i=0;i<benchmarks.size();i++){
            //System.out.println("f"+(i+1)+"\t"+benchmarks.get(i).getClass().toString());
            run(new OLPSO(),benchmarks.get(i),null);
        }
        return;
    }

    @Test
    public void testTopology(){
        int populationSize=40;
        List<TopologyModel> topologies = ContentFactory.topologyModels(populationSize);

        for(int i=0;i<topologies.size();i++){
            //System.out.println("f"+(i+1)+"\t"+topologies.get(i).getClass().toString());
            run(new CPSO(),new BenchmarkModel(10),topologies.get(i));
        }
    }

    @Test
    public void testSingleRun(){
        int dim = 30;
        PSO pso = new CLPSO();
        if(pso instanceof CLPSO){
            //((CLPSO)pso).policyFlag = CLPSO.Topology.Globe;
            ((CLPSO)pso).policyFlag = CLPSO.Topology.Globe;
            ((CLPSO)pso).degree = 2;
            //((CLPSO)pso).bindTopology(new Local(pso.populationSize));
        }
        if(pso instanceof WPSO){
//            ((WPSO)pso).fixedWeight = true;
//            ((WPSO)pso).w = 1.05;
        }

        pso.dimensionCount = dim;
        pso.single = true;
        run(pso,new Rastrigin(dim),null);
    }

    public void run(PSO pso, BenchmarkModel benchmarkModel, TopologyModel tm){
        pso.bindBenchmark(benchmarkModel);
        if(tm!=null){
            pso.bindTopology(tm);
        }
        pso.run();
        //System.out.println(pso.getAns());
    }

    @Test
    public void simpleTest(){
        BenchmarkModel bm = new Griewanks(10);
        double[] x = new double[10];
//        for(int i=0;i<10;i++){
//            x[i] = 420.96;
//        }
        System.out.println(bm.calculate(x));
        //System.out.println(420.96*Math.sin(Math.pow(Math.abs(420.96),0.5)));

    }

    @Test
    public void protectedTestSingleRun(){
        int dim = 30;
        PSO pso = new CLPSO();
        if(pso instanceof CLPSO){
            //((CLPSO)pso).policyFlag = CLPSO.Topology.Globe;
            ((CLPSO)pso).policyFlag = CLPSO.Topology.Origin;
            //((CLPSO)pso).degree = 2;
            //((CLPSO)pso).bindTopology(new Local(pso.populationSize));
        }
        if(pso instanceof WPSO){
//            ((WPSO)pso).fixedWeight = true;
//            ((WPSO)pso).w = 1.05;
        }

        pso.dimensionCount = dim;
        pso.single = true;
        run(pso,new Rastrigin(dim),null);
    }
}
