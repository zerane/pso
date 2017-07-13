package psopkg;

/**
 * Created by admin on 2017/6/8.
 */
public class CPSO extends WPSO {

    @Override
    public void init(){
        super.init();
        fixedWeight = true;
        w=0.729844;
        c1=c2=2.01;
    }

    @Override
    public void updateVelocity(int i){
        for(int j=0;j<dimensionCount;j++){
            double rand1 = random.nextDouble();
            //double rand2 = random.nextDouble();
            particles[i].velocity[j] = particles[i].velocity[j]
                    + c1*rand1*(pbest[i].position[j]-particles[i].position[j]);
            if(topology==null){
                particles[i].velocity[j] += c2*rand1*(gbest.position[j]-particles[i].position[j]);
            }else{
                particles[i].velocity[j] += c2*rand1*(topogbest[i].position[j]-particles[i].position[j]);
            }
            particles[i].velocity[j] *= w;
        }
    }
}
