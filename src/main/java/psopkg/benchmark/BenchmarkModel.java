package psopkg.benchmark;

import psopkg.PSO;

import java.io.*;

/**
 * Created by admin on 2017/6/6.
 */
public class BenchmarkModel {
    public double[] upperBound;
    public double[] lowerBound;
    public double[] initUpperBound;
    public double[] initLowerBound;
    public int dimensionCount;
    public double[][] M;
    public PSO pso;

    public BenchmarkModel(int dim){
        dimensionCount = dim;
        upperBound = new double[dimensionCount];
        lowerBound = new double[dimensionCount];
        initUpperBound = new double[dimensionCount];
        initLowerBound = new double[dimensionCount];
        initBound();
    }

    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 100;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 50;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -100;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -100;
        }
    }

    public double calculate(double[] x){
        trigger();
        double ans=0;
        return ans;
    }

    public void extractM(String filepath){
        File matFile = new File(filepath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(matFile));
            M = new double[dimensionCount][dimensionCount];
            String line;
            String[] data;
            int currentLine=0;
            while((line = br.readLine())!=null){
                data = line.trim().split("  | ");
                for(int i=0;i<dimensionCount;i++){
                    M[currentLine][i] = Double.parseDouble(data[i]);
                }
                currentLine++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void trigger(){
        if(pso==null){
            return;
        }
        pso.FE--;
        if(pso.FE%100==0){
            pso.sample.add(pso.getAns());
        }
        if(pso.FE%1000==0&&pso.single){
            System.out.println("\t"+pso.FE+": "+pso.getAns());
        }
        if(pso.FE<=0){
            pso.interrupted = true;
        }
    }
}
