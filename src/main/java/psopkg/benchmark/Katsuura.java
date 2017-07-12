package psopkg.benchmark;

/**
 * Created by admin on 2017/7/10.
 */
public class Katsuura extends BenchmarkModel {
    public Katsuura(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        super.calculate(x);
        double ans=0;
        double sum=1;
        double num;
        for (int i=0;i<dimensionCount;i++){
            num = 1;
            for(int j=1;j<=32;j++){
                num += Math.abs(Math.pow(2D,j)*x[i]-Math.round(Math.pow(2D,j)*x[i]))/Math.pow(2,j);
            }
            num = Math.pow(num,10D/Math.pow(dimensionCount,1.2));
            sum *=num;
        }
        ans += 10D/Math.pow(dimensionCount,2)*sum;
        ans -= 10D/Math.pow(dimensionCount,2);
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
