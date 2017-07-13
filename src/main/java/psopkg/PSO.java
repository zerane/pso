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
    public int dimensionCount=30;
    public static int populationSize=40;
    public Random random;
    public int currentIteration;
    public int totalGeneration=5000;
    public double c1=2;
    public double c2=2;
    public BenchmarkModel benchmark=new BenchmarkModel(dimensionCount);
    public TopologyModel topology;
    public int boundPolicy;//boundpolicy 1=restrict 2=skip
    public double[] vmax;
    public boolean single=false;
    public Particle[] topogbest;
    public List<Double> sample;
    public List<Double> convergeSample;
    public boolean interrupted = false;
    public boolean[] skipCal;
    public int FE=200000;

    public PSO(){
    }

    public void run(){
        init();
        System.out.println(this.getClass().getSimpleName()+"\t"+benchmark.getClass().getSimpleName()+"\t"+(topology==null?"":topology.getClass().getSimpleName()));
        System.out.println("\tBefore: "+getAns());
        for (currentIteration=0;;currentIteration++){
            if(interrupted){
                break;
            }
            iterate();
//            if(currentIteration%10==0){
//                sample.add(getAns());
//            }
//            if(currentIteration%100==0 && single){
//                System.out.println("\t"+currentIteration+": "+getAns());
//            }
        }
        System.out.println("\tAfter: "+getAns());
        sample.add(getAns());
        convergeSample.add(getInnerPopulationDistance());
    }

    public double getAns(){
        return gbest.fitnessValue;
    }

    public void init(){
        boundPolicy = 1;
        if(vmax==null){
            vmax = new double[dimensionCount];
            for(int i=0;i<dimensionCount;i++){
                vmax[i] = (upperBound[i]-lowerBound[i])*0.25;
            }
        }
        random = new Random();
        currentIteration=0;
        pbest = new Particle[populationSize];
        topogbest = new Particle[populationSize];
        sample = new LinkedList<>();
        convergeSample = new LinkedList<>();
        skipCal = new boolean[populationSize];
        initParticle();
    }

    public void initParticle(){
        particles = new Particle[populationSize];
        for(int i=0;i<populationSize;i++){
            particles[i] = new Particle(dimensionCount);
            for (int j=0;j<dimensionCount;j++){
                particles[i].position[j] = random.nextDouble()*(initUpperBound[j]-initLowerBound[j])+initLowerBound[j];
                //particles[i].velocity[j] = 0.25*random.nextDouble()*(initUpperBound[j]-initLowerBound[j])+initLowerBound[j];
            }
        }
        for(int i=0;i<populationSize;i++){
            updateFitness(i);
            updateBests(i);
        }

    }


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
        }
    }

    public void restrictVelocity(int i){
        for(int j=0;j<dimensionCount;j++){
            if(particles[i].velocity[j]<-vmax[j]){
                particles[i].velocity[j] = -vmax[j];
            }
            if(particles[i].velocity[j]>vmax[j]){
                particles[i].velocity[j] = vmax[j];
            }
        }
    }

    public void updatePosition(int i){
        skipCal[i] = false;
        for(int j=0;j<dimensionCount;j++){
            particles[i].position[j] = particles[i].position[j]
                    + particles[i].velocity[j];
            if(particles[i].position[j]<lowerBound[j]
                    ||particles[i].position[j]>upperBound[j]) {
                dealWithBound(particles[i].position,j,i);
            }
        }
    }

    public void dealWithBound(double[] position,int d,int i){
        switch (boundPolicy){
            case 1:
                if(position[d]>upperBound[d]){
                    position[d] = upperBound[d];
                }else{
                    position[d] = lowerBound[d];
                }
                break;
            case 2:
                skipCal[i] = true;
                break;
            default:
                System.out.println("Bound policy not selected.");
                break;
        }
    }

    public void updateFitness(int i){
        particles[i].fitnessValue = benchmark.calculate(particles[i].position);
    }

    public void updateBests(int i){
        if(pbest[i]==null||particles[i].fitnessValue<pbest[i].fitnessValue){
            pbest[i] = particles[i].clone();
            if(gbest==null||pbest[i].fitnessValue<gbest.fitnessValue){
                gbest = pbest[i].clone();
            }
        }
        if(topology!=null){
            updateTopologyGbest(i);
        }
    }

    public void updateTopologyGbest(int i){
        List<Integer> connected = topology.topo.get(i);
        for(int j=0;j<connected.size();j++){
            if(topogbest[i]==null||particles[connected.get(j)].fitnessValue<topogbest[i].fitnessValue){
                topogbest[i] = particles[connected.get(j)].clone();
            }
        }
    }


    public void selectExemplar(int i) {

    }

    public void iterate(){
        for(int i=0;i<populationSize;i++){
            updateVelocity(i);
            restrictVelocity(i);
            updatePosition(i);
            updateFitness(i);
            updateBests(i);
        }
    }

    public void bindBenchmark(BenchmarkModel bm){
        benchmark = bm;
        upperBound = bm.upperBound;
        lowerBound = bm.lowerBound;
        initLowerBound = bm.initLowerBound;
        initUpperBound = bm.initUpperBound;
        bm.pso = this;
    }

    public void bindTopology(TopologyModel tm){
        assert tm==null||tm.populationSize==populationSize;
        topology = tm;
    }

    public double getInnerPopulationDistance(){
        double ans=0;
        for(int i=0;i<populationSize;i++){
            for(int j=0;j<populationSize;j++){
                ans += distance(i,j);
            }
        }
        return ans;
    }
    public double distance(int i,int j){
        double ans=0;
        for(int d=0;d<dimensionCount;d++){
            ans+=Math.pow(particles[i].position[d]-particles[j].position[d],2);
        }
        ans = Math.sqrt(ans);
        return ans;
    }
}
