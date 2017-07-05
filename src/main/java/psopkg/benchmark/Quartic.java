package psopkg.benchmark;

import java.util.Random;

/**
 * Created by admin on 2017/6/19.
 */
public class Quartic extends BenchmarkModel {
    public Quartic(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        super.calculate(x);
        double ans=0;
        for(int i=0;i<dimensionCount;i++){
            double num=0;
            num = (i+1)*Math.pow(x[i],4);
            ans += num;
        }
        ans += new Random().nextDouble();
        return ans;
    }

    @Override
    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 1.28;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 1.28;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -1.28;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -1.28;
        }
    }
}
