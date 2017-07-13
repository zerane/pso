package psopkg;

/**
 * Created by admin on 2017/6/6.
 */
public class WPSO extends PSO {
    public double w;
    public double w0;
    public double w1;
    public boolean fixedWeight = false;

    @Override
    public void init(){
        super.init();
        w0=0.9;
        w1=0.4;
        boundPolicy = 1;
    }

    public void refreshWeight(){
        w = w0-(w0-w1)*currentIteration/totalGeneration;
    }

    @Override
    public void updateVelocity(int i){
        for(int j=0;j<dimensionCount;j++){
            double rand1 = random.nextDouble();
            double rand2 = random.nextDouble();
            particles[i].velocity[j] = w*particles[i].velocity[j]
                    + c1*rand1*(pbest[i].position[j]-particles[i].position[j]);
            if(topology==null){
                particles[i].velocity[j] += c2*rand2*(gbest.position[j]-particles[i].position[j]);
            }else{
                particles[i].velocity[j] += c2*rand2*(topogbest[i].position[j]-particles[i].position[j]);
            }
        }
    }

    @Override
    public void iterate(){
        if(!fixedWeight){
            refreshWeight();
        }
        super.iterate();
    }
}
