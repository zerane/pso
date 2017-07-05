package psopkg.benchmark;

/**
 * Created by admin on 2017/6/9.
 */
public class GeneralizedPenalized extends BenchmarkModel {
    public GeneralizedPenalized(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        super.calculate(x);
        double ans=0;
        double sum=0;
        double[] y = new double[dimensionCount];
        for(int i=0;i<dimensionCount;i++){
            y[i] = 1D+1D/4*(x[i]+1);
        }
        sum += 10*Math.pow(Math.sin(Math.PI*y[1]),2);
        for (int i=0;i<dimensionCount-1;i++){
            sum += Math.pow(y[i]-1,2)*(1+10*Math.pow(Math.sin(Math.PI*y[i+1]),2));
        }
        sum += Math.pow(y[dimensionCount-1]-1,2);
        sum *= Math.PI/dimensionCount;
        ans += sum;
        sum=0;
        for(int i=0;i<dimensionCount;i++){
            sum += u(x[i],10,100,4);
        }
        ans += sum;
        return ans;
    }

    public double u(double xi,double a,double k,double m){
        double ans;
        if(xi>a){
            ans = k*Math.pow(xi-a,m);
        }else if(xi>=-a){
            ans = 0;
        }else{
            ans = k*Math.pow(-xi-a,m);
        }
        return ans;
    }

    @Override
    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 50;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 25;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -50;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -50;
        }
    }
}
