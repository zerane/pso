package psopkg.benchmark;

/**
 * Created by admin on 2017/7/10.
 */
public class Scaffer extends BenchmarkModel {
    public Scaffer(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        super.calculate(x);
        double ans=0;
        double sum=0;
        for(int i=0;i<dimensionCount-1;i++){
            sum += g(x[i],x[i+1]);
        }
        sum += g(x[dimensionCount-1],x[0]);
        ans += sum;
        return ans;
    }

    public double g(double x,double y){
        double ans;
        ans = 0.5+(Math.pow(Math.sin(Math.sqrt(x*x+y*y)),2)-0.5)/Math.pow(1+0.001*(x*x+y*y),2);
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
