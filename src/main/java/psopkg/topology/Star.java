package psopkg.topology;

import java.util.Random;

/**
 * Created by admin on 2017/6/11.
 */
public class Star extends TopologyModel {
    public boolean randomCenter = false;
    public Star(int dim) {
        super(dim);
    }

    public void generateTopology(){
        super.generateTopology();
        int center = randomCenter?(Math.abs(new Random().nextInt())%populationSize):0;
        for(int i = 0; i< populationSize; i++){
            if(i==center){
                for(int j = 0; j< populationSize; j++){
                    topo.get(i).add(j);
                }
            }else{
                topo.get(i).add(center);
            }
        }
    }
}
