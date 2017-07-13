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
    public enum Topology{Origin,Globe,Star,Grid,Local};
    public int degree=2;
    public int starCenter;
    public List<Integer> candidateTotal;
    public int clusters = 4;
    public boolean mixed = false;

    @Override
    public void init(){
        starCenter = populationSize-1;
        pc = new double[populationSize];
        for(int i=0;i<populationSize;i++){
            pc[i] = 0.05+0.45*(Math.exp(10*(i)/(populationSize-1))-1)/(Math.exp(10)-1);
            //pc[i] = 1;
        }
        flag = new int[populationSize];
        super.init();
        for(int i=0;i<populationSize;i++){
            for(int j=0;j<dimensionCount;j++){
                particles[i].exemplar[j] = i;
            }
        }
        c1 = 1.49445;
        candidateTotal = new ArrayList<>();
        for(int i=0;i<populationSize;i++){
            candidateTotal.add(i);
        }
        boundPolicy = 2;
    }

    @Override
    public void selectExemplar(int i){
        for(int d=0;d<dimensionCount;d++){
            List<Integer> candidate = null;
            double rand = random.nextDouble();
//            if(policyFlag == Topology.Local){
//                candidate = getCandidateStarNop(i);
//                double fitness = Double.MAX_VALUE;
//                for(int j=0;j<candidate.size();j++){
//                    if(pbest[candidate.get(j)].fitnessValue<fitness){
//                        particles[i].exemplar[d]=candidate.get(j);
//                        fitness = particles[i].exemplar[d];
//                    }
//                }
//                continue;
//            }
//            if(policyFlag==Topology.Local){
//                if(i==starCenter){
//                    candidate = getCandidateGbest(i);
//                    particles[i].exemplar[d]=candidate.get(0);
//                    continue;
//                }
//            }

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
                    case Local:
                        candidate = getCandidateStarNop(i);
                        break;
                    default:
                        candidate = getCandidateOri(i);
                        break;
                }

                double fitness = Double.MAX_VALUE;
                for(int j=0;j<candidate.size();j++){
                    if(pbest[candidate.get(j)].fitnessValue<fitness){
                        particles[i].exemplar[d] = candidate.get(j);
                        fitness = pbest[candidate.get(j)].fitnessValue;
                    }
                }
                int a=0;
            }else{
                particles[i].exemplar[d] = i;
            }
        }
        boolean ownFlag = true;
        for(int j=0;j<dimensionCount;j++){
            if(particles[i].exemplar[j] != i){
                ownFlag = false;
            }
        }
        if(ownFlag){
            int another = i;
            while (another==i) {
                another = pickOne();
            }
            //particles[i].exemplar[Math.abs(random.nextInt()%dimensionCount)] = another;
        }
        if(mixed){
            policyFlag = policyFlag==Topology.Origin?Topology.Globe:policyFlag;
            policyFlag = policyFlag==Topology.Globe?Topology.Star:policyFlag;
            policyFlag = policyFlag==Topology.Star?Topology.Grid:policyFlag;
            policyFlag = policyFlag==Topology.Grid?Topology.Local:policyFlag;
            policyFlag = policyFlag==Topology.Local?Topology.Origin:policyFlag;
        }
    }

//    @Override
//    public void iterate(){
//        for(int i=0;i<populationSize;i++){
//            if(flag[i]>=m){
//                selectExemplar(i);
//                flag[i] = 0;
//            }
//        }
//        super.iterate();
//    }

    @Override
    public void updateVelocity(int i){
        if(flag[i]>=m){
            selectExemplar(i);
            flag[i] = 0;
        }
        for(int j=0;j<dimensionCount;j++){
            double low = 0D;
            double high = 1D;
            double rand = random.nextDouble();
            rand  = rand*(high-low)+low;
//            DecimalFormat df = new DecimalFormat("0.0");
//            rand = Double.parseDouble(df.format(rand));
            //rand = 1;
            particles[i].velocity[j] = w*particles[i].velocity[j]
                    + c1*rand*(pbest[particles[i].exemplar[j]].position[j]-particles[i].position[j]);
        }
    }

    @Override
    public void updateFitness(int i){
        if(skipCal[i]){
            //particles[i].fitnessValue = Double.MAX_VALUE;
        }else{
            particles[i].fitnessValue = benchmark.calculate(particles[i].position);
        }
    }

    @Override
    public void updateBests(int i){
        if(skipCal[i]){
            return;
        }
        if(pbest[i]==null||particles[i].fitnessValue<pbest[i].fitnessValue){
            flag[i]=0;
            pbest[i] = particles[i].clone();
            if(gbest==null||pbest[i].fitnessValue<gbest.fitnessValue){
                gbest = pbest[i].clone();
            }
        }else{
            flag[i]++;
        }
        if(topology!=null){
            updateTopologyGbest(i);
        }
    }

    public List<Integer> getCandidateOri(int i){
        List<Integer> ans = new ArrayList<>();
        List<Integer> myCandidate;
        if (topology==null){
            myCandidate = new ArrayList<>(candidateTotal);
        }else{
            myCandidate = new ArrayList<>(topology.topo.get(i));
        }
        //myCandidate.remove(i);
        for(int j=0;j<degree;j++){
            if(myCandidate.size()==0){
                System.out.println("00000");
            }
            int idx = Math.abs(random.nextInt()%myCandidate.size());
            ans.add(myCandidate.get(idx));
            myCandidate.remove(idx);
        }
        return ans;
    }

    public List<Integer> getCandidateGrid(int i){
        List<Integer> ans = new ArrayList<>();
        List<Integer> realAns = new ArrayList<>();

        int size = (int)Math.sqrt(populationSize);
        ans.add(i+1>=0?((i+1)%populationSize):(i+1+populationSize));
        ans.add(i-1>=0?((i-1)%populationSize):(i-1+populationSize));
        ans.add(i+size>=0?((i+size)%populationSize):(i+size+populationSize));
        ans.add(i-size>=0?((i-size)%populationSize):(i-size+populationSize));
        ans.add(i);
        realAns.add(ans.get(pickOne(ans.size())));
        return realAns;
    }

    public List<Integer> getCandidateStar(int i){
        List<Integer> ans = new ArrayList<>();
        List<Integer> realAns = new ArrayList<>();
        if(i==starCenter){
            ans.add(pickOne());
        }else{
            ans.add(starCenter);
            ans.add(i);
        }

        realAns.add(ans.get(pickOne(ans.size())));
        return realAns;
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

    public Integer pickOne(int size){
        return Math.abs(random.nextInt()%size);
    }

    public List<Integer> getCandidateStarNop(int i){
        List<Integer> ans = new ArrayList<>();
        List<Integer> realAns = new ArrayList<>();

        //int size = (int)Math.sqrt(populationSize);
        ans.add(i+1>=0?((i+1)%populationSize):(i+1+populationSize));
        ans.add(i-1>=0?((i-1)%populationSize):(i-1+populationSize));
        ans.add(i);
//        ans.add(i+size>=0?((i+size)%populationSize):(i+size+populationSize));
//        ans.add(i-size>=0?((i-size)%populationSize):(i-size+populationSize));
        realAns.add(ans.get(pickOne(ans.size())));

        return realAns;
    }
}
