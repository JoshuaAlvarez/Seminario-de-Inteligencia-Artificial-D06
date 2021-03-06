package HandsOn8;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSwarmOptimization {

    private static double[] gBest; //Global optimum position 
    private static double gBestFitness = Double.MAX_VALUE; //Fitness of the global optimum position
    private static int populationSize = 50; //Number of particles
    private static int maxIter = 400; //Maximun number of iterations
    private static int c1 = 2; //Acceleration factors
    private static int c2 = 2;
    private static double w = 0.9; //Inertia weight
    private static List<Particle> swarm = new ArrayList<>(); //Create particle swarm

//  Initialize population
    public static void initialParticles() {
        for (int i = 0; i < populationSize; i++) {
            Particle particle = new Particle();
            particle.initialX();
            particle.initialV();
            particle.fitness = particle.calculateFitness();
            swarm.add(particle);
        }
    }

//  Update Global best
    public static void updateGbest() {
        double fitness = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < populationSize; i++) {
            if (swarm.get(i).fitness < fitness) {
                index = i;
                fitness = swarm.get(i).fitness;
            }
        }
        if (fitness < gBestFitness) {
            gBest = swarm.get(index).pBest.clone();
            gBestFitness = fitness;
        }
    }
    
//  Update the velocity of each particle
    public static void updateSwarmVelocity() {
        swarm.forEach((particle) -> {
            for (int i = 0; i < particle.dimension; i++) {
                double v = w * particle.V[i] + c1 * rand() * (particle.pBest[i] - particle.x[i]) + c2 * rand() * (gBest[i] - particle.x[i]);
                if (v > particle.Vmax)
                    v = particle.Vmax;
                else if (v < -particle.Vmax)
                    v = -particle.Vmax;
                particle.V[i] = v;//Updating Vi
            }
        });
    }
    
//  Update the position and pBest of each particle
    public static void updateSwarmPosition() {
        swarm.stream().map((particle) -> {
            for (int i = 0; i < particle.dimension; i++) {
                particle.x[i] = particle.x[i] + particle.V[i];
            }
            return particle;
        }).forEachOrdered((particle) -> {
            double newFitness = particle.calculateFitness(); //New fitness
            //If the new fitness value is smaller than the original, update fitness and pBest
            if (newFitness < particle.fitness) {
                particle.pBest = particle.x.clone();
                particle.fitness = newFitness;
            }
        });
    }

    public static void PSOprocess() {
        initialParticles();
        updateGbest();
        int i = 0;
        while(i++<maxIter) {
            updateSwarmVelocity();
            updateSwarmPosition();
            updateGbest();
            System.out.println(i+".gbest:("+gBest[0]+","+gBest[1]+")  fitness="+gBestFitness);
        }
    }

//  Random parameters within [0,1]
    public static double rand() {
        return new Random().nextDouble();
    }

    public static void main(String[] args) {
        System.out.println("Particle Swarm Optimization");
        System.out.println("Estimating solution for SLR equation y = B0 + B1*x");
        PSOprocess();
    }

}
