package psopkg.topology;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/11.
 */
public class TopologyModel {
    public List<List<Integer>> topo;
    public int populationSize;
    public TopologyModel(int dim){
        populationSize = dim;
        generateTopology();
    }

    public void generateTopology(){
        topo = new ArrayList<>();
        for(int i = 0; i< populationSize; i++){
            topo.add(new ArrayList<>());
        }
    }
}
