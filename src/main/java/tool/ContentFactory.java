package tool;

import psopkg.*;
import psopkg.benchmark.*;
import psopkg.topology.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/6/12.
 */
public class ContentFactory {
    public static final int CATEGORY_ALG=-1;
    public static final int CATEGORY_BENCHMARK=-2;
    public static final int CATEGORY_TOPOLOGY=-3;
    public static final int CATEGORY_DEGREE=-4;
    public static final int CATEGORY_CLPSOTOPOLOGY=-5;

    public static List<BenchmarkModel> benchmarkModels(int dims){
        List<BenchmarkModel> benchmarks = new ArrayList<>();
        benchmarks.add(new Sphere(dims));
        benchmarks.add(new SchwefelP222(dims));
        benchmarks.add(new Rosenbrocks(dims));

        benchmarks.add(new Ackley(dims));
        benchmarks.add(new Griewanks(dims));
        benchmarks.add(new Weierstrass(dims));
        benchmarks.add(new Rastrigin(dims));
        benchmarks.add(new NoncontinuousRastrigin(dims));
        benchmarks.add(new Schwefel(dims));
        benchmarks.add(new GeneralizedPenalized(dims));

        benchmarks.add(new RotatedAckley(dims));
        benchmarks.add(new RotatedGriewanks(dims));
        benchmarks.add(new RotatedWeierstrass(dims));
        benchmarks.add(new RotatedRastrigin(dims));
        return benchmarks;
    }

    public static List<BenchmarkModel> benchmarkModelSampling(int dims){
        List<BenchmarkModel> benchmarks = new ArrayList<>();
        //benchmarks.add(new Sphere(dims));
//        benchmarks.add(new SchwefelP222(dims));
//        benchmarks.add(new Rosenbrocks(dims));

        //benchmarks.add(new Ackley(dims));
        //benchmarks.add(new Griewanks(dims));
        //benchmarks.add(new ModifiedGriewanks(dims));
//        //benchmarks.add(new Weierstrass(dims));
//        benchmarks.add(new Rastrigin(dims));
//        benchmarks.add(new NoncontinuousRastrigin(dims));
//        benchmarks.add(new Schwefel(dims));
//        benchmarks.add(new GeneralizedPenalized(dims));

//        benchmarks.add(new RotatedAckley(dims));
        benchmarks.add(new RotatedGriewanks(dims));
//        benchmarks.add(new RotatedWeierstrass(dims));
        //benchmarks.add(new RotatedRastrigin(dims));
        return benchmarks;
    }

    public static List<BenchmarkModel> benchmarkModelsSupple(int dims){
        List<BenchmarkModel> benchmarks = new ArrayList<>();
//        benchmarks.add(new ModifiedRastrigin(dims));
//        benchmarks.add(new ModifiedRastrigin2(dims));
        benchmarks.add(new ModifiedRastrigin3(dims));
        //benchmarks.add(new ModifiedGriewanks(dims));
        return benchmarks;
    }

    public static List<PSO> psos(){
        List<PSO> psos = new ArrayList<>();
        psos.add(new WPSO());
        psos.add(new CPSO());
        psos.add(new CLPSO());
        psos.add(new OLPSO());
        return psos;
    }

    public static List<PSO> clpsos(){
        List<PSO> psos = new ArrayList<>();
        List<CLPSO.Topology> clpsotopo = ContentFactory.clpsoTopologyToTest();
        for (CLPSO.Topology topo : clpsotopo) {
            CLPSO cp = new CLPSO();
            cp.policyFlag = topo;
            psos.add(cp);
        }
        return psos;
    }

    public static void refreshPSO(List<PSO> psos){
        try {
            for(int i=0;i<psos.size();i++){
                if (psos.get(i).sample!=null){
                    psos.set(i,psos.get(i).getClass().newInstance());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static List<TopologyModel> topologyModels(int populationSize){
        List<TopologyModel> topologies = new ArrayList<>();
        topologies.add(new FullyConnected(populationSize));
        topologies.add(new Local(populationSize));
        topologies.add(new Grid(populationSize));
        topologies.add(new Star(populationSize));
        topologies.add(new NCluster(populationSize));
        return topologies;
    }

    public static List<TestData> psoToData(List<PSO> psos,int category){
        List<TestData> data = new ArrayList<>();
        for(int i=0;i<psos.size();i++){
            TestData td = new TestData();
            td.data = psos.get(i).sample;
            switch (category){
                case CATEGORY_ALG:
                    td.category = psos.get(i).getClass().getSimpleName();
                    break;
                case CATEGORY_BENCHMARK:
                    td.category = psos.get(i).benchmark.getClass().getSimpleName();
                    break;
                case CATEGORY_TOPOLOGY:
                    td.category = psos.get(i).topology.getClass().getSimpleName();
                    break;
                case CATEGORY_DEGREE:
                    if(psos.get(i) instanceof CLPSO){
                        td.category = String.valueOf((((CLPSO) psos.get(i)).degree));
                    }else{
                        td.category = "";
                    }
                    break;
                case CATEGORY_CLPSOTOPOLOGY:
                    td.category = String.valueOf((((CLPSO) psos.get(i)).policyFlag));
                    break;
                default:
                    td.category = String.valueOf(category);
                    break;
            }
            data.add(td);
        }

        return data;
    }

    public static TestData psosToMeanData(List<PSO> psos,int category){
        TestData td = new TestData();
        switch (category){
            case CATEGORY_ALG:
                td.category = psos.get(0).getClass().getSimpleName();
                break;
            case CATEGORY_BENCHMARK:
                td.category = psos.get(0).benchmark.getClass().getSimpleName();
                break;
            case CATEGORY_TOPOLOGY:
                td.category = psos.get(0).topology.getClass().getSimpleName();
                break;
            case CATEGORY_DEGREE:
                if(psos.get(0) instanceof CLPSO){
                    td.category = String.valueOf((((CLPSO) psos.get(0)).degree));
                }else{
                    td.category = "";
                }
                break;
            case CATEGORY_CLPSOTOPOLOGY:
                td.category = ((CLPSO)psos.get(0)).policyFlag.toString();
                break;
            default:
                break;
        }

        List<Double> sample = new ArrayList<>();
        for(int i=0;i<psos.get(0).sample.size();i++){
            double sum=0;
            int count=0;
            for(int j=0;j<psos.size();j++){
                if(psos.get(j).sample.size()<=j){
                    break;
                }
                sum += psos.get(j).sample.get(i);
                count++;
            }
            sample.add(sum/count);
        }
        td.data = sample;

        return td;
    }

    public static TestData psosToMedianData(List<PSO> psos,int category){
        TestData td = new TestData();
        switch (category){
            case CATEGORY_ALG:
                td.category = psos.get(0).getClass().getSimpleName();
                break;
            case CATEGORY_BENCHMARK:
                td.category = psos.get(0).benchmark.getClass().getSimpleName();
                break;
            case CATEGORY_TOPOLOGY:
                td.category = psos.get(0).topology.getClass().getSimpleName();
                break;
            case CATEGORY_DEGREE:
                if(psos.get(0) instanceof CLPSO){
                    td.category = String.valueOf((((CLPSO) psos.get(0)).degree));
                }else{
                    td.category = "";
                }
                break;
            case CATEGORY_CLPSOTOPOLOGY:
                td.category = ((CLPSO)psos.get(0)).policyFlag.toString();
                break;
            default:
                break;
        }

        List<Double> sample = new ArrayList<>();
        int count = psos.size();
        for(int i=0;i<psos.get(0).sample.size();i++){
            double[] record = new double[count];
            for(int j=0;j<psos.size();j++){
                if(psos.get(j).sample.size()<=i){
                    break;
                }

                record[j] = psos.get(j).sample.get(i);
                //sum += psos.get(j).sample.get(i);
            }
            Arrays.sort(record);
            sample.add(record[count/2]);
        }
        td.data = sample;

        return td;
    }

    public static TestData psosToMedianConverge(List<PSO> psos,int category){
        TestData td = new TestData();
        switch (category){
            case CATEGORY_ALG:
                td.category = psos.get(0).getClass().getSimpleName();
                break;
            case CATEGORY_BENCHMARK:
                td.category = psos.get(0).benchmark.getClass().getSimpleName();
                break;
            case CATEGORY_TOPOLOGY:
                td.category = psos.get(0).topology.getClass().getSimpleName();
                break;
            case CATEGORY_DEGREE:
                if(psos.get(0) instanceof CLPSO){
                    td.category = String.valueOf((((CLPSO) psos.get(0)).degree));
                }else{
                    td.category = "";
                }
                break;
            case CATEGORY_CLPSOTOPOLOGY:
                td.category = ((CLPSO)psos.get(0)).policyFlag.toString();
                break;
            default:
                break;
        }

        List<Double> sample = new ArrayList<>();
        int count = psos.size();
        for(int i=0;i<psos.get(0).convergeSample.size();i++){
            double[] record = new double[count];
            for(int j=0;j<psos.size();j++){
                if(psos.get(j).convergeSample.size()<=i){
                    break;
                }

                record[j] = psos.get(j).convergeSample.get(i);
                //sum += psos.get(j).sample.get(i);
            }
            Arrays.sort(record);
            sample.add(record[count/2]);
        }
        td.data = sample;

        return td;
    }

    public static List<CLPSO.Topology> clpsoTopologyToTest(){
        List<CLPSO.Topology> ans = new ArrayList<>();

        ans.add(CLPSO.Topology.Origin);
        ans.add(CLPSO.Topology.Globe);
        ans.add(CLPSO.Topology.Star);
        ans.add(CLPSO.Topology.Grid);
        ans.add(CLPSO.Topology.Local);

        return ans;
    }
}
