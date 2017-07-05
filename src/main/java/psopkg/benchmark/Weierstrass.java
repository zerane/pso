package psopkg.benchmark;

/**
 * Created by admin on 2017/6/9.
 */
public class Weierstrass extends BenchmarkModel {
    public Weierstrass(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        double sum=0;
        double a=0.5;
        double b=3;
        double kmax=20;
        for(int i=0;i<dimensionCount;i++){
            for(int k=0;k<kmax;k++){
                sum += Math.pow(a,k)*Math.cos(2*Math.PI*Math.pow(b,k)*(x[i]+0.5));
            }
        }
        ans += sum;
        sum=0;
        for(int k=0;k<kmax;k++){
            sum += Math.pow(a,k)*Math.cos(2*Math.PI*Math.pow(b,k)*0.5);
        }
        ans += -dimensionCount*sum;
        super.calculate(x);
        return ans;
    }

    @Override
    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 0.5;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 0.2;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -0.5;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -0.5;
        }
    }
}
