package psopkg.benchmark;

/**
 * Created by admin on 2017/6/19.
 */
public class Step extends BenchmarkModel {
    public Step(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        for(int i=0;i<dimensionCount;i++){
            double num=0;
            num = Math.pow(Math.floor(x[i]+0.5),2);
            ans += num;
        }
        super.calculate(x);
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
