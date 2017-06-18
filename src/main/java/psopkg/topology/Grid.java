package psopkg.topology;

/**
 * Created by admin on 2017/6/11.
 */
public class Grid extends TopologyModel {
    public int sqrt;
    public Grid(int dim) {
        super(dim);
    }

    @Override
    public void generateTopology(){
        super.generateTopology();
        sqrt = (int)Math.floor(Math.sqrt(populationSize));
        for(int i = 0; i< populationSize; i++){
            topo.get(i).add(i-1<0?i-1+populationSize:(i-1)%populationSize);
            topo.get(i).add(i+1<0?i+1+populationSize:(i+1)%populationSize);
            topo.get(i).add(i-sqrt<0?i-sqrt+populationSize:(i-sqrt)%populationSize);
            topo.get(i).add(i+sqrt<0?i+sqrt+populationSize:(i+sqrt)%populationSize);
        }
    }
}
