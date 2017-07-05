package experiment;

import org.junit.Test;
import psopkg.CLPSO;
import psopkg.PSO;
import psopkg.benchmark.BenchmarkModel;
import psopkg.topology.TopologyModel;
import tool.ContentFactory;
import tool.TestData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/13.
 */
public class ScopeTopology {
    public static int repeatTimes=30;
    public static int dimensionCount=30;

    @Test
    public void testScopeTopology(){
        try {

            List<BenchmarkModel> bmList = new ArrayList<>();
            //bmList = ContentFactory.benchmarkModels(dimensionCount);
            bmList = ContentFactory.benchmarkModelSampling(dimensionCount);
            List<PSO> psoList = ContentFactory.psos();
            psoList = new ArrayList<>();
            psoList.add(new CLPSO());
            List<TopologyModel> topologyList = ContentFactory.topologyModels(PSO.populationSize);
            List<Thread> threads = new ArrayList<>();

            for(int i=0;i<bmList.size();i++){
                for(int j=0;j<psoList.size();j++){
                    //file
                    File psoinbm = new File("./result/scopeTopology/"+bmList.get(i).getClass().getSimpleName()+"_"+
                            psoList.get(j).getClass().getSimpleName()+".csv");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(psoinbm));
                    String towrite = "";
                    List<TestData> sampleTD = new ArrayList<>();
                    for(int k=0;k<topologyList.size();k++){
                        List<PSO> repeated = new ArrayList<>();
                        for(int m=0;m<repeatTimes;m++){
                            ContentFactory.refreshPSO(psoList);
                            PSO pso = psoList.get(j);
                            pso.bindBenchmark(bmList.get(i));
                            pso.bindTopology(topologyList.get(k));
                            pso.run();
                            repeated.add(pso);
                        }
                        //TestData td = ContentFactory.psosToMeanData(repeated,ContentFactory.CATEGORY_TOPOLOGY);
                        TestData td = ContentFactory.psosToMedianData(repeated,ContentFactory.CATEGORY_TOPOLOGY);
                        sampleTD.add(td);
                    }
                    for(int m=0;m<sampleTD.size();m++){
                        towrite += sampleTD.get(m).category+",";
                    }
                    bw.write(towrite+"\n");

                    int maxsize=0;
                    for(int k=0;k<sampleTD.size();k++){
                        maxsize = sampleTD.get(k).data.size()>maxsize?sampleTD.get(k).data.size():maxsize;
                    }
                    for (int k=0;k<sampleTD.get(0).data.size();k++){
                        towrite="";
                        for(int m=0;m<sampleTD.size();m++){
                            if(sampleTD.get(m).data.size()>=k){
                                towrite += sampleTD.get(m).data.get(k);
                            }
                            towrite += ",";
                        }
                        bw.write(towrite+"\n");
                    }

                    bw.close();
                }
            }
//
//

//            for(BenchmarkModel bm: bmList){
//                for(PSO psoCla: psoList){
//                    //file
//                    Thread t = new Thread(()->{
//                        try {
//                            File psoinbm = new File("./result/scopeTopology/"+bm.getClass().getSimpleName()+"_"+
//                                    psoCla.getClass().getSimpleName()+".csv");
//                            BufferedWriter bw = new BufferedWriter(new FileWriter(psoinbm));
//                            String towrite = "";
//                            List<TestData> sampleTD = new ArrayList<>();
//                            for(int k=0;k<topologyList.size();k++){
//                                List<PSO> repeated = new ArrayList<>();
//                                for(int m=0;m<repeatTimes;m++){
//                                    PSO pso = psoCla.getClass().newInstance();
//                                    pso.bindBenchmark(bm);
//                                    pso.bindTopology(topologyList.get(k));
//                                    pso.run();
//                                    repeated.add(pso);
//                                }
//                                TestData td = ContentFactory.psosToMeanData(repeated,ContentFactory.CATEGORY_TOPOLOGY);
//                                sampleTD.add(td);
//                            }
//                            for(int m=0;m<sampleTD.size();m++){
//                                towrite += sampleTD.get(m).category+",";
//                            }
//                            bw.write(towrite+"\n");
//
//                            for (int k=0;k<sampleTD.get(0).data.size();k++){
//                                towrite="";
//                                for(int m=0;m<sampleTD.size();m++){
//                                    towrite += sampleTD.get(m).data.get(k)+",";
//                                }
//                                bw.write(towrite+"\n");
//                            }
//
//                            bw.close();
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    });
//                    t.start();
//                    threads.add(t);
//                }
//            }
//            for (Thread t: threads) {
//                t.join();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
