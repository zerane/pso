package psopkg.benchmark;

/**
 * Created by admin on 2017/7/10.
 */
public class ModifiedModTrap extends BenchmarkModel {
    public ModifiedModTrap(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        super.calculate(x);

        for (int i=0;i<dimensionCount;i++){
            ans += Math.abs(x[i]*(x[i]%2-1))+Math.abs(x[i])/10;
        }

        return ans;
    }

    @Override
    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 100;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 100;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -100;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -100;
        }
    }
}
