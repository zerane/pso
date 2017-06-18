package psopkg.benchmark;

/**
 * Created by admin on 2017/6/9.
 */
public class Griewanks extends BenchmarkModel {
    public Griewanks(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        double sum=0;
        for(int i=0;i<dimensionCount;i++){
            sum += x[i]*x[i]/4000;
        }
        ans += sum;
        sum=1;
        for(int i=0;i<dimensionCount;i++){
            sum *= Math.cos(x[i]/Math.pow(i+1,0.5));
        }
        ans += -sum;
        ans += 1;
        return ans;
    }

    @Override
    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 600;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 200;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -600;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -600;
        }
    }
}
