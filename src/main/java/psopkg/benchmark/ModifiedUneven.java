package psopkg.benchmark;

/**
 * Created by admin on 2017/7/10.
 */
public class ModifiedUneven extends BenchmarkModel {
    public ModifiedUneven(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double ans=0;
        super.calculate(x);

        for (int i=0;i<dimensionCount;i++){
            ans += 1-Math.exp(-2*Math.log(2)*Math.pow((x[i]-0.08)/0.854,2))*Math.pow(Math.sin(5*Math.PI*(Math.pow(Math.abs(x[i]),0.75)-0.05)),6);
        }

        return ans;
    }

    @Override
    public void initBound(){
        for(int i=0;i<dimensionCount;i++){
            upperBound[i] = 1;
        }
        for(int i=0;i<dimensionCount;i++){
            initUpperBound[i] = 1;
        }
        for(int i=0;i<dimensionCount;i++){
            lowerBound[i] = -1;
        }
        for(int i=0;i<dimensionCount;i++){
            initLowerBound[i] = -1;
        }
    }

}
