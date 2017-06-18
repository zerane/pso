package psopkg.benchmark;

/**
 * Created by admin on 2017/6/9.
 */
public class Schwefel extends BenchmarkModel {
    public Schwefel(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        double sum=0;
        ans += 418.9829 * dimensionCount;
        for (int i=0;i<dimensionCount;i++){
            sum += x[i]*Math.sin(Math.pow(Math.abs(x[i]),0.5));
        }
        ans += sum;
        return ans;
    }

    @Override
    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 500;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 500;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -500;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -500;
        }
    }
}
