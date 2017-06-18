package psopkg;
import psopkg.benchmark.BenchmarkModel;
import psopkg.topology.TopologyModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2017/6/6.
 */
public class PSO {
    public double[] upperBound;
    public double[] lowerBound;
    public double[] initUpperBound;
    public double[] initLowerBound;
    public Particle[] particles;
    public Particle[] pbest;
    public Particle gbest;
    public int dimensionCount=10;
    public static int populationSize=40;
    public Random random;
    public int currentIteration;
    public int totalGeneration=5000;
    public double c1=2;
    public double c2=2;
    public BenchmarkModel benchmark=new BenchmarkModel(dimensionCount);
    public TopologyModel topology;
    public int boundPolicy=1;//boundpolicy 1=restrict
    public double[] vmax;
    public boolean single=false;
    public Particle[] topogbest;
    public List<Double> sample;
    public boolean interrupted = false;

    public PSO(){
    }

    public void run(){
        init();
        System.out.println(this.getClass().getSimpleName()+"\t"+benchmark.getClass().getSimpleName()+"\t"+(topology==null?"":topology.getClass().getSimpleName()));
        System.out.println("\tBefore: "+getAns());
        for(currentIteration=0;currentIteration<totalGeneration;currentIteration++){
            if(interrupted){
                break;
            }
            iterate();
            if(currentIteration%10==0){
                sample.add(getAns());
            }
            if(currentIteration%100==0 && single){
                System.out.println("\t"+currentIteration+": "+getAns());
            }
        }
        System.out.println("\tAfter: "+getAns());
        sample.add(getAns());
    }

    public double getAns(){
        return gbest.fitnessValue;
    }

    public void init(){
        if(vmax==null){
            vmax = new double[dimensionCount];
            for(int i=0;i<dimensionCount;i++){
                vmax[i] = (upperBound[i]-lowerBound[i])*0.2;
            }
        }
        random = new Random();
        currentIteration=0;
        pbest = new Particle[populationSize];
        topogbest = new Particle[populationSize];
        sample = new LinkedList<>();
        initParticle();
    }

    public void initParticle(){
        particles = new Particle[populationSize];
        for(int i=0;i<populationSize;i++){
            particles[i] = new Particle(dimensionCount);
            for (int j=0;j<dimensionCount;j++){
                particles[i].position[j] = random.nextDouble()*(initUpperBound[j]-initLowerBound[j])+initLowerBound[j];
                particles[i].velocity[j] = 0.5*random.nextDouble()*(initUpperBound[j]-initLowerBound[j])+initLowerBound[j];
            }
        }
        updateFitness();
        updateBests();
    }


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
            }
        }
    }

    public void restrictVelocity(){
        for (int i=0;i<populationSize;i++){
            for(int j=0;j<dimensionCount;j++){
                if(particles[i].velocity[j]<-vmax[j]){
                    particles[i].velocity[j] = -vmax[j];
                }
                if(particles[i].velocity[j]>vmax[j]){
                    particles[i].velocity[j] = vmax[j];
                }
            }
        }
    }

    public void updatePosition(){
        for(int i=0;i<populationSize;i++){
            for(int j=0;j<dimensionCount;j++){
                particles[i].position[j] = particles[i].position[j]
                        + particles[i].velocity[j];
                if(particles[i].position[j]<lowerBound[j]
                        ||particles[i].position[j]>upperBound[j]){
                    dealWithBound(particles[i].position,j);
                }
            }
        }
    }

    public void dealWithBound(double[] position,int idx){
        switch (boundPolicy){
            case 1:
                if(position[idx]>upperBound[idx]){
                    position[idx] = upperBound[idx];
                }else{
                    position[idx] = lowerBound[idx];
                }
                break;
            default:
                System.out.println("Bound policy not selected.");
                break;
        }
    }

    public boolean checkBound(double[] position){
        for(int i=0;i<dimensionCount;i++){
            if(position[i]<lowerBound[i]||position[i]>upperBound[i]){
                return false;
            }
        }
        return true;
    }

    public void updateFitness(){
        for(int i=0;i<populationSize;i++){
            particles[i].fitnessValue = benchmark.calculate(particles[i].position);
        }
    }

    public void updateBests(){
        for(int i=0;i<populationSize;i++){
            if(pbest[i]==null||particles[i].fitnessValue<pbest[i].fitnessValue){
                pbest[i] = particles[i].clone();
                if(gbest==null||pbest[i].fitnessValue<gbest.fitnessValue){
                    gbest = pbest[i].clone();
                }
            }
        }
        if(topology!=null){
            updateTopologyGbest();
        }
    }

    public void updateTopologyGbest(){
        for(int i=0;i<populationSize;i++){
            List<Integer> connected = topology.topo.get(i);
            for(int j=0;j<connected.size();j++){
                if(topogbest[i]==null||particles[connected.get(j)].fitnessValue<topogbest[i].fitnessValue){
                    topogbest[i] = particles[connected.get(j)].clone();
                }
            }
        }
    }


    public void selectExemplar(int i) {

    }

    public void iterate(){
        updateVelocity();
        restrictVelocity();
        updatePosition();
        updateFitness();
        updateBests();
    }

    public void bindBenchmark(BenchmarkModel bm){
        benchmark = bm;
        upperBound = bm.upperBound;
        lowerBound = bm.lowerBound;
        initLowerBound = bm.initLowerBound;
        initUpperBound = bm.initUpperBound;
    }

    public void bindTopology(TopologyModel tm){
        assert tm==null||tm.populationSize==populationSize;
        topology = tm;
    }
}
