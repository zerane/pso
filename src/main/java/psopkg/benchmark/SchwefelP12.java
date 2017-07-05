package psopkg.benchmark;

/**
 * Created by admin on 2017/6/19.
 */
public class SchwefelP12 extends BenchmarkModel {
    public SchwefelP12(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        double sum=0;

        for(int i=0;i<dimensionCount;i++){
            sum=0;
            for(int j=0;j<=i;j++){
                sum += x[j];
            }
            sum = Math.pow(sum,2);
            ans += sum;
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
