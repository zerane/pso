package psopkg.benchmark;

/**
 * Created by admin on 2017/7/11.
 */
public class ModifiedRastrigin2 extends BenchmarkModel{
    public ModifiedRastrigin2(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        double sum=0;
        for (int i=0;i<dimensionCount;i++){
            sum += -10*Math.cos(2*Math.PI*x[i])+10;
        }
        ans += sum;
        super.calculate(x);
        return ans;
    }

    @Override
    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 5.12;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 2;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -5.12;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -5.12;
        }
    }
}
