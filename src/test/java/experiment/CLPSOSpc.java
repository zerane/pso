package experiment;

import org.junit.Test;
import psopkg.CLPSO;
import psopkg.PSO;
import psopkg.benchmark.BenchmarkModel;
import tool.ContentFactory;
import tool.TestData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/16.
 */
public class CLPSOSpc {
    public static int repeatTimes=30;
    public static int dimensionCount=30;

    @Test
    public void testDegree(){
        try {
            List<BenchmarkModel> bmList = new ArrayList<>();
            //bmList = ContentFactory.benchmarkModels(dimensionCount);
            bmList = ContentFactory.benchmarkModelSampling(dimensionCount);
            int degreeMax=40;

            for(int i=0;i<bmList.size();i++){
                //file
                File psoinbm = new File("./result/CLPSO/"+bmList.get(i).getClass().getSimpleName()+"_degree"+".csv");
                List<TestData> sampleTD = new ArrayList<>();
                BufferedWriter bw = new BufferedWriter(new FileWriter(psoinbm));
                String towrite = "";

                {
                    List<PSO> repeated = new ArrayList<>();
                    for (int m = 0; m < repeatTimes; m++) {
                        CLPSO pso = new CLPSO();
                        pso.degree = 2;
                        pso.bindBenchmark(bmList.get(i));
                        pso.run();
                        repeated.add(pso);
                    }
                    //TestData td = ContentFactory.psosToMeanData(repeated, ContentFactory.CATEGORY_DEGREE);
                    TestData td = ContentFactory.psosToMedianData(repeated, ContentFactory.CATEGORY_DEGREE);
                    sampleTD.add(td);
                }

                for(int j=8;j<=degreeMax;j+=8){
                    List<PSO> repeated = new ArrayList<>();
                    for(int m=0;m<repeatTimes;m++) {
                        CLPSO pso = new CLPSO();
                        pso.degree = j;
                        pso.bindBenchmark(bmList.get(i));
                        pso.run();
                        repeated.add(pso);
                    }
                    //TestData td = ContentFactory.psosToMeanData(repeated,ContentFactory.CATEGORY_DEGREE);
                    TestData td = ContentFactory.psosToMedianData(repeated, ContentFactory.CATEGORY_DEGREE);
                    sampleTD.add(td);
                }


                for(int m=0;m<sampleTD.size();m++){
                    towrite += sampleTD.get(m).category+",";
                }
                bw.write(towrite+"\n");
                for (int k=0;k<sampleTD.get(0).data.size();k++){
                    towrite="";
                    for(int m=0;m<sampleTD.size();m++){
                        towrite += sampleTD.get(m).data.get(k)+",";
                    }
                    bw.write(towrite+"\n");
                }
                System.out.println(towrite);

                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTopo(){
        try {
            List<BenchmarkModel> bmList = new ArrayList<>();
            //bmList = ContentFactory.benchmarkModels(dimensionCount);
            bmList = ContentFactory.benchmarkModelSampling(dimensionCount);
            //bmList = ContentFactory.benchmarkModelsSupple(dimensionCount);
            List<CLPSO.Topology> topologyList = ContentFactory.clpsoTopologyToTest();

            for(int i=0;i<bmList.size();i++){
                //file
                File psoinbm = new File("./result/CLPSO/supple/"+bmList.get(i).getClass().getSimpleName()+".csv");
                List<TestData> sampleTD = new ArrayList<>();
                BufferedWriter bw = new BufferedWriter(new FileWriter(psoinbm));
                String towrite = "";
                for(int j=0;j<topologyList.size();j++){
                    List<PSO> repeated = new ArrayList<>();
                    for(int m=0;m<repeatTimes;m++) {
                        CLPSO pso = new CLPSO();
                        pso.dimensionCount = dimensionCount;
                        pso.policyFlag = topologyList.get(j);
                        pso.bindBenchmark(bmList.get(i));
                        pso.run();
                        repeated.add(pso);
                    }
                    //TestData td = ContentFactory.psosToMeanData(repeated,ContentFactory.CATEGORY_CLPSOTOPOLOGY);
                    TestData td = ContentFactory.psosToMedianData(repeated,ContentFactory.CATEGORY_CLPSOTOPOLOGY);
                    sampleTD.add(td);

                }
                for(int m=0;m<sampleTD.size();m++){
                    towrite += sampleTD.get(m).category+",";
                }
                bw.write(towrite+"\n");
                for (int k=0;k<sampleTD.get(0).data.size();k++){
                    towrite="";
                    for(int m=0;m<sampleTD.size();m++){
                        towrite += sampleTD.get(m).data.get(k)+",";
                    }
                    bw.write(towrite+"\n");
                }

                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
