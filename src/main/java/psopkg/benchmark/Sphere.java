package psopkg.benchmark;

/**
 * Created by admin on 2017/6/8.
 */
public class Sphere extends BenchmarkModel {
    public Sphere(int dim){
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        for (int i=0;i<dimensionCount;i++){
            ans += Math.pow(x[i],2);
        }
        return ans;
    }

    @Override
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
}
