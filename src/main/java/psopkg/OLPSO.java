package psopkg;

/**
 * Created by admin on 2017/6/8.
 */
public class OLPSO extends WPSO{
    public int[][] OA;
    public int[] stagnated;
    public int G=5;
    int M;
    public Particle[] po;

    @Override
    public void init(){
        stagnated = new int[populationSize];
        initOA();
        super.init();
        po = new Particle[populationSize];
        for(int i=0;i<populationSize;i++){
            po[i] = particles[i].clone();
        }
    }

    @Override
    public void iterate(){
        if(FE<0){
            interrupted = true;
            return;
        }
        for(int i=0;i<populationSize;i++){
            if(stagnated[i]>=G){
                selectExemplar(i);
                stagnated[i] = 0;
            }
        }
        refreshWeight();
        super.iterate();
        c1 = 2;
    }

    @Override
    public void updateVelocity(int i){
        for(int j=0;j<dimensionCount;j++){
            double rand = random.nextDouble();
            particles[i].velocity[j] = w*particles[i].velocity[j]
                    + c1*rand*(po[i].position[j]-particles[i].position[j]);
        }
    }


    public void initOA(){
        OA = generateOA();
    }

    @Override
    public void selectExemplar(int i){
        Particle[] x = new Particle[M];
        Particle gbesti;
        if(topology==null){
            gbesti = gbest;
        }else{
            gbesti = topogbest[i];
        }
        double fitSum1;
        int count1;
        double fitSum2;
        int count2;
        Particle xb = null;
        Particle xp = new Particle(dimensionCount);
        double min=Double.MAX_VALUE;
        for(int j=0;j<M;j++){
            x[j] = new Particle(dimensionCount);
            for(int d=0;d<dimensionCount;d++){
                x[j].position[d] = OA[j][d]==1?pbest[i].position[d]:gbesti.position[d];
            }
            x[j].fitnessValue = benchmark.calculate(x[j].position);
            if(x[j].fitnessValue<min){
                min = x[j].fitnessValue;
                xb = x[j];
            }
        }
        for(int d=0;d<dimensionCount;d++){
            fitSum1=0;
            fitSum2=0;
            count1=0;
            count2=0;
            for(int m=0;m<M;m++){
                switch (OA[m][d]){
                    case 1:
                        fitSum1 += x[m].fitnessValue;
                        count1++;
                        break;
                    case 2:
                        fitSum2 += x[m].fitnessValue;
                        count2++;
                        break;
                    default:
                        assert false;
                        break;
                }
            }
            xp.position[d] = fitSum1/count1<fitSum2/count2?pbest[i].position[d]:gbesti.position[d];
        }
        xp.fitnessValue = benchmark.calculate(xp.position);
        po[i] = xp.fitnessValue<xb.fitnessValue?xp:xb;
    }

    @Override
    public void updateBests(int i){
        if(pbest[i]==null||particles[i].fitnessValue<pbest[i].fitnessValue){
            stagnated[i]=0;
            pbest[i] = particles[i].clone();
            if(gbest==null||pbest[i].fitnessValue<gbest.fitnessValue){
                gbest = pbest[i].clone();
            }
        }else{
            stagnated[i]++;
        }
        if(topology!=null){
            updateTopologyGbest(i);
        }
    }

    @Override
    public void updateFitness(int i){
        if(skipCal[i]){
            particles[i].fitnessValue = Double.MAX_VALUE;
        }else{
            particles[i].fitnessValue = benchmark.calculate(particles[i].position);
        }
    }

    public int[][] generateOA(){
        int[][] ans;
        M = (int)Math.round(Math.pow(2,Math.ceil(Math.log(dimensionCount+1)/Math.log(2))));
        int N = M-1;
        int u = (int)Math.round(Math.log(M)/Math.log(2));
        ans = new int[M][N];
        for(int a=0;a<M;a++){
            for(int k=1;k<=u;k++){
                int b = (int)Math.pow(2,k-1);
                ans[a][b-1] = (int)Math.floor(((double)a)/(Math.pow(2,u-k)))%2;
                for(int s=0;s<b;s++){
                    ans[a][b+s-1] = (ans[a][s]+ans[a][b-1])%2;
                }
            }
        }
        for(int i=0;i<M;i++){
            for (int j=0;j<N;j++){
                if(ans[i][j]==0){
                    ans[i][j]=1;
                } else if(ans[i][j]==1){
                    ans[i][j]=2;
                } else{
                    assert false;
                }
            }
        }
        return ans;
    }

}
