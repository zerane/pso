package psopkg;

/**
 * Created by admin on 2017/6/6.
 */
class Particle{
    public double[] position;
    public double[] velocity;
    public int[] exemplar;
    public double fitnessValue;
    public Particle(int dim){
        position = new double[dim];
        velocity = new double[dim];
        exemplar = new int[dim];
        fitnessValue = Double.MAX_VALUE;
    }
    public Particle clone(){
        assert position.length==velocity.length;
        Particle ans = new Particle(position.length);
        System.arraycopy(position,0,ans.position,0,position.length);
        System.arraycopy(velocity,0,ans.velocity,0,velocity.length);
        System.arraycopy(exemplar,0,ans.exemplar,0, exemplar.length);
        ans.fitnessValue = fitnessValue;
        return ans;
    }
}
