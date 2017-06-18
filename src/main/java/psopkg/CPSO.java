package psopkg;

/**
 * Created by admin on 2017/6/8.
 */
public class CPSO extends WPSO {
    @Override
    public void updateVelocity(){
        for (int i=0;i<populationSize;i++){
            for(int j=0;j<dimensionCount;j++){
                double rand1 = random.nextDouble();
                double rand2 = random.nextDouble();
                particles[i].velocity[j] = particles[i].velocity[j]
                        + c1*rand1*(pbest[i].position[j]-particles[i].position[j]);
                if(topology==null){
                    particles[i].velocity[j] += c2*rand2*(gbest.position[j]-particles[i].position[j]);
                }else{
                    particles[i].velocity[j] += c2*rand2*(topogbest[i].position[j]-particles[i].position[j]);
                }
                particles[i].velocity[j] *= w;
            }
        }
    }
}
