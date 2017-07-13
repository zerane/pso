package experiment;

import org.junit.Test;
import psopkg.PSO;
import psopkg.benchmark.BenchmarkModel;
import tool.ContentFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/12.
 */
public class WithoutTopology {
    public static int repeatTimes=30;
    public static int dimensionCount=30;

    @Test
    public void onAllBenchmarks(){
        List<BenchmarkModel> bmList = new ArrayList<>();
        //bmList.add(new BenchmarkModel(10));
        bmList = ContentFactory.benchmarkModels(dimensionCount);
        List<PSO> psoList = ContentFactory.psos();
        File outFile = new File("./result/noTopologyAllPSO.csv");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

            String[][] table = new String[psoList.size()+1][bmList.size()+1];
            for(int i=0;i<bmList.size();i++){
                table[0][i+1] = "f"+(i+1)+"("+bmList.get(i).getClass().getSimpleName()+")";
            }
            for(int i=0;i<psoList.size();i++){
                table[i+1][0] = psoList.get(i).getClass().getSimpleName();
            }

            for(int i=0;i<bmList.size();i++){
                double[] tempsum = new double[psoList.size()];
                double[] min = new double[psoList.size()];
                double[] max = new double[psoList.size()];
                for(int k=0;k<psoList.size();k++){
                    min[k] = Double.MAX_VALUE;
                    max[k] = -Double.MAX_VALUE;
                }
                for(int t=0;t<repeatTimes;t++){
                    psoList = ContentFactory.psos();
                    for(int j=0;j<psoList.size();j++){
                        PSO pso = psoList.get(j);
                        pso.bindBenchmark(bmList.get(i));
                        pso.run();
                        tempsum[j] += pso.getAns();
                        min[j] = pso.getAns()<min[j]?pso.getAns():min[j];
                        max[j] = pso.getAns()>max[j]?pso.getAns():max[j];
                        //table[j][i] = String.valueOf(pso.getAns());
                    }
                }
                for(int j=0;j<psoList.size();j++){
                    double mean = tempsum[j]/repeatTimes;
                    table[j+1][i+1] = String.valueOf(mean)+"$"+String.valueOf((max[j]-min[j])/2);
                }
            }

            for(int i=0;i<=bmList.size();i++){
                String towrite = table[0][i];
                for (int j=1;j<=psoList.size();j++){
                    towrite += ","+table[j][i];
                }
                towrite+="\n";
                bw.write(towrite);
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
