package psopkg.topology;

/**
 * Created by admin on 2017/6/11.
 */
public class FullyConnected extends TopologyModel {
    public FullyConnected(int dim) {
        super(dim);
    }

    @Override
    public void generateTopology(){
        super.generateTopology();
        for(int i = 0; i< populationSize; i++){
            for(int j = 0; j< populationSize; j++){
                topo.get(i).add(j);
            }
        }
    }
}
