package psopkg.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2017/6/11.
 */
public class NCluster extends TopologyModel {
    public static int clusterCount=4;
    public Random random;
    public NCluster(int dim) {
        super(dim);
    }

    @Override
    public void generateTopology(){
        super.generateTopology();
        random = new Random();
        List<List<Integer>> clusters = generateClusters();

        for(int i=0;i<clusterCount;i++){
            List<Integer> currentCluster = clusters.get(i);
            for(int j=0;j<currentCluster.size();j++){
                for(int k=0;k<currentCluster.size();k++){
                    topo.get(currentCluster.get(j)).add(currentCluster.get(k));
                }
            }
        }

        for(int i=0;i<clusterCount;i++){
            for(int j=0;j<clusterCount;j++){
                List<Integer> clu1 = clusters.get(i);
                List<Integer> clu2 = clusters.get(i);
                int p1 = clu1.get(Math.abs(random.nextInt()%clu1.size()));
                int p2 = clu2.get(Math.abs(random.nextInt()%clu2.size()));
                topo.get(p1).add(p2);
                topo.get(p2).add(p1);
            }
        }
    }

    public List<List<Integer>> generateClusters(){
        List<List<Integer>> ans = new ArrayList<>();
        for(int i=0;i<clusterCount;i++){
            ans.add(new ArrayList<>());
        }
        for(int i=0;i<populationSize;i++){
            ans.get(i%clusterCount).add(i);
        }
        return ans;
    }
}
