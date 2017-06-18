package psopkg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/6.
 */
public class CLPSO extends WPSO {
    public double[] pc;
    public int[] flag;
    public int m=7;
    public Topology policyFlag=Topology.Origin;
    public enum Topology{Origin,Globe,Star,Grid,Star_nop};
    public int degree=2;
    public int starCenter=0;
    public List<Integer> candidateTotal;

    @Override
    public void init(){
        pc = new double[populationSize];
        for(int i=0;i<populationSize;i++){
            pc[i] = 0.05+0.45*(Math.exp(10*(i)/(populationSize-1))-1)/(Math.exp(10)-1);
        }
        flag = new int[populationSize];
        super.init();
        for(int i=0;i<populationSize;i++){
            for(int j=0;j<dimensionCount;j++){
                particles[i].exemplar[j] = i;
            }
        }
        c1 = 1.49445;
        c2 = 1.49445;
        candidateTotal = new ArrayList<>();
        for(int i=0;i<populationSize;i++){
            candidateTotal.add(i);
        }
    }

    @Override
    public void selectExemplar(int i){
        for(int d=0;d<dimensionCount;d++){
            List<Integer> candidate = null;
            double rand = random.nextDouble();
            if(policyFlag == Topology.Star_nop){
                candidate = getCandidateStarNop(i);
                double fitness = Double.MAX_VALUE;
                for(int j=0;j<candidate.size();j++){
                    if(pbest[candidate.get(j)].fitnessValue<fitness){
                        particles[i].exemplar[d]=candidate.get(j);
                    }
                }
                continue;
            }
            if(rand<pc[i]){
                switch (policyFlag){
                    case Origin:
                        candidate = getCandidateOri(i);
                        break;
                    case Globe:
                        candidate = getCandidateGbest(i);
                        break;
                    case Star:
                        candidate = getCandidateStar(i);
                        break;
                    case Grid:
                        candidate = getCandidateGrid(i);
                        break;
                    default:
                        candidate = getCandidateOri(i);
                        break;
                }
                double fitness = Double.MAX_VALUE;
                for(int j=0;j<candidate.size();j++){
                    if(pbest[candidate.get(j)].fitnessValue<fitness){
                        particles[i].exemplar[d]=candidate.get(j);
                    }
                }
            }else{
                particles[i].exemplar[d] = i;
            }
        }
    }

    @Override
    public void iterate(){
        for(int i=0;i<populationSize;i++){
            if(flag[i]>=m){
                selectExemplar(i);
                flag[i] = 0;
            }
        }
        super.iterate();
    }

    @Override
    public void updateVelocity(){
        for (int i=0;i<populationSize;i++){
            for(int j=0;j<dimensionCount;j++){
                double rand = random.nextDouble();
                particles[i].velocity[j] = w*particles[i].velocity[j]
                        + c1*rand*(pbest[particles[i].exemplar[j]].position[j]-particles[i].position[j]);
            }
        }
    }

    @Override
    public void updateFitness(){
        for(int i=0;i<populationSize;i++){
            if(!checkBound(particles[i].position)){
                particles[i].fitnessValue = Double.MAX_VALUE;
            }else{
                particles[i].fitnessValue = benchmark.calculate(particles[i].position);
            }
        }
    }

    @Override
    public void updateBests(){
        for(int i=0;i<populationSize;i++){
            if(pbest[i]==null||particles[i].fitnessValue<pbest[i].fitnessValue){
                flag[i]=0;
                pbest[i] = particles[i].clone();
                if(gbest==null||pbest[i].fitnessValue<gbest.fitnessValue){
                    gbest = pbest[i].clone();
                }
            }else{
                flag[i]++;
            }
        }
        if(topology!=null){
            updateTopologyGbest();
        }
    }

    public List<Integer> getCandidateOri(int i){
        List<Integer> ans = new ArrayList<>();
        List<Integer> myCandidate = new ArrayList<>(candidateTotal);
        for(int j=0;j<degree;j++){
            if(topology==null){
                int idx = Math.abs(random.nextInt()%myCandidate.size());
                ans.add(myCandidate.get(idx));
                myCandidate.remove(idx);
            }else{
                ans.add(topology.topo.get(i).get(Math.abs(random.nextInt()%topology.topo.get(i).size())));
            }
        }

        return ans;
    }

    public List<Integer> getCandidateGrid(int i){
        List<Integer> ans = new ArrayList<>();

        int size = (int)Math.sqrt(populationSize);
        ans.add(i+1>=0?((i+1)%populationSize):(i+1+populationSize));
        ans.add(i-1>=0?((i-1)%populationSize):(i-1+populationSize));
        ans.add(i+size>=0?((i+size)%populationSize):(i+size+populationSize));
        ans.add(i-size>=0?((i-size)%populationSize):(i-size+populationSize));
        return ans;
    }

    public List<Integer> getCandidateStar(int i){
        List<Integer> ans = new ArrayList<>();
        ans.add(pickOne());
        ans.add(starCenter);
        return ans;
    }

    public List<Integer> getCandidateGbest(int i){
        List<Integer> ans = new ArrayList<>();
        int idx=0;
        double fitnessVal = Double.MAX_VALUE;
        for(int j=0;j<pbest.length;j++){
            if(pbest[j].fitnessValue<fitnessVal){
                fitnessVal = pbest[j].fitnessValue;
                idx = j;
            }
        }
        ans.add(idx);
        return ans;
    }

    public Integer pickOne(){
        return Math.abs(random.nextInt()%populationSize);
    }

    public List<Integer> getCandidateStarNop(int i){
        List<Integer> ans = new ArrayList<>();
        if(i==starCenter){
            for(int j=0;j<populationSize;j++){
                ans.add(j);
            }
        }else{
            ans.add(pickOne());
            ans.add(pickOne());
        }
        return ans;
    }
}
