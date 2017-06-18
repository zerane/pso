import org.junit.Test;
import psopkg.CPSO;
import psopkg.OLPSO;
import psopkg.PSO;
import psopkg.benchmark.BenchmarkModel;
import psopkg.benchmark.Rosenbrocks;
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
        List<TopologyModel> topologyModelList = ContentFactory.topologyModels(PSO.populationSize);
        BenchmarkModel bm = new Rosenbrocks(10);

        for(int i=0;i<topologyModelList.size();i++){
            //System.out.println(psos.get(i).getClass().getSimpleName()+":");
            PSO pso = new OLPSO();
            psos.add(pso);
            run(pso,bm,topologyModelList.get(i));
        }

        data = ContentFactory.psoToData(psos,ContentFactory.CATEGORY_TOPOLOGY);

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
        int populationSize=20;
        List<TopologyModel> topologies = ContentFactory.topologyModels(populationSize);

        for(int i=0;i<topologies.size();i++){
            //System.out.println("f"+(i+1)+"\t"+topologies.get(i).getClass().toString());
            run(new CPSO(),new BenchmarkModel(10),topologies.get(i));
        }
    }

    @Test
    public void testSingleRun(){
        PSO pso = new OLPSO();
        pso.single = true;
        pso.bindTopology(null);
        run(pso,new BenchmarkModel(10),null);
    }

    public void run(PSO pso, BenchmarkModel benchmarkModel, TopologyModel tm){
        pso.bindBenchmark(benchmarkModel);
        if(tm!=null){
            pso.bindTopology(tm);
        }
        pso.run();
        //System.out.println(pso.getAns());
    }
}
