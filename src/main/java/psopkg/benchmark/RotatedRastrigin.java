package psopkg.benchmark;

/**
 * Created by admin on 2017/6/9.
 */
public class RotatedRastrigin extends Rastrigin {
    public RotatedRastrigin(int dim) {
        super(dim);
    }

    @Override
    public double calculate(double[] x){
        extractM("./extdata/rastrigin_M_D"+dimensionCount+".txt");
        double[] y = new double[dimensionCount];

        for (int i=0;i<dimensionCount;i++){
            for(int j=0;j<dimensionCount;j++){
                y[i] += x[j]*M[i][j];
            }
        }

        return super.calculate(y);
    }
}
