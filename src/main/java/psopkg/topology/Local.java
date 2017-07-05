package psopkg.topology;

/**
 * Created by admin on 2017/6/11.
 */
public class Local extends TopologyModel{
    public static int localSize=1;

    public Local(int dim) {
        super(dim);
    }

    @Override
    public void generateTopology(){
        super.generateTopology();
        for(int i = 0; i< populationSize; i++){
            for(int j=i-localSize;j<=i+localSize;j++){
                topo.get(i).add(j<0?j+ populationSize :j% populationSize);
            }
        }
    }
}
