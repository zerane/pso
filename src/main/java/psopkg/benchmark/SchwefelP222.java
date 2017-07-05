package psopkg.benchmark;

/**
 * Created by admin on 2017/6/9.
 */
public class SchwefelP222 extends BenchmarkModel {
    public SchwefelP222(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        double sum=0;
        for (int i=0;i<dimensionCount;i++){
            sum += Math.abs(x[i]);
        }
        ans += sum;
        sum=1;
        for(int i=0;i<dimensionCount;i++){
            sum *= Math.abs(x[i]);
        }
        ans += sum;
        super.calculate(x);
        return ans;
    }

    @Override
    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 10;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 5;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -10;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -10;
        }
    }

}
