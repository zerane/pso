package psopkg.benchmark;

/**
 * Created by admin on 2017/6/9.
 */
public class Ackley extends BenchmarkModel {
    public Ackley(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        double sum=0;
        for (int i=0;i<dimensionCount;i++){
            sum += x[i]*x[i];
        }
        ans += -20D*Math.exp(-0.2*Math.pow(1/dimensionCount*sum,0.5));
        sum=0;
        for(int i=0;i<dimensionCount;i++){
            sum += Math.cos(2*Math.PI*x[i]);
        }
        ans += -Math.exp(1D/dimensionCount*sum)+20+Math.E;
        return ans;
    }

    @Override
    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 32.768;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 16;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -32.768;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -32.768;
        }
    }
}
