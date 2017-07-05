package psopkg.benchmark;

/**
 * Created by admin on 2017/6/9.
 */
public class NoncontinuousRastrigin extends Rastrigin {
    public NoncontinuousRastrigin(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        double[] y = new double[dimensionCount];

        for(int i=0;i<dimensionCount;i++){
            y[i] = Math.abs(x[i])<0.5?x[i]:Math.round(2*x[i])/2;
        }
        return super.calculate(y);
    }
}
