package psopkg.benchmark;

/**
 * Created by admin on 2017/6/8.
 */
public class Rosenbrocks extends BenchmarkModel {
    public Rosenbrocks(int dim){
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        for (int i=0;i<dimensionCount-1;i++){
            ans += 100*(Math.pow(Math.pow(x[i],2)-x[i+1],2)+Math.pow(x[i]-1,2));
        }
        super.calculate(x);
        return ans;
    }

    @Override
    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 2.048;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 2.048;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -2.048;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -2.048;
        }
    }
}
