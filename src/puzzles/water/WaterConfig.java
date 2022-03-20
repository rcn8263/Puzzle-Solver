package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Single configuration of the water puzzle.
 */
public class WaterConfig implements Configuration {
    private int desiredAmount;
    private ArrayList<Integer> bucketCapacities;
    private ArrayList<Integer> bucketAmounts;

    public WaterConfig(int desiredAmount,
                       ArrayList<Integer> bucketCapacities,
                       ArrayList<Integer> bucketAmounts) {
        this.desiredAmount = desiredAmount;
        this.bucketCapacities = bucketCapacities;
        this.bucketAmounts = bucketAmounts;
    }

    /**
     * @return int that represents the amount of water you want a
     * bucket to have
     */
    public int getDesiredAmount() {
        return desiredAmount;
    }

    /**
     * @return an ArrayList of all bucket capacities
     */
    public ArrayList<Integer> getBucketCapacities() {
        return bucketCapacities;
    }

    /**
     * @return an ArrayList of all buckets with their current amount
     * of water
     */
    public ArrayList<Integer> getBucketAmounts() {
        return bucketAmounts;
    }

    /**
     * @return a copy ArrayList that contains the current amount of
     * water in each bucket
     */
    private ArrayList<Integer> copyBucketAmounts() {
        ArrayList<Integer> copy = new ArrayList<>();
        for (int i: bucketAmounts) {
            copy.add(i);
        }
        return copy;
    }

    /**
     * @return an ArrayList of all possible successor configurations for
     * the current configuration
     */
    @Override
    public Collection<Configuration> getSuccessors() {
        Collection<Configuration> successors = new ArrayList<>();

        //get the successors of filling each bucket by the lake
        for (int i = 0; i < bucketCapacities.size(); i++) {
            ArrayList<Integer> copy = copyBucketAmounts();
            copy.set(i, bucketCapacities.get(i));
            successors.add(new WaterConfig(desiredAmount,
                    bucketCapacities, copy));
        }

        //get the successors of dumping a bucket
        for (int i = 0; i < bucketCapacities.size(); i++) {
            ArrayList<Integer> copy = copyBucketAmounts();
            copy.set(i, 0);
            successors.add(new WaterConfig(desiredAmount,
                    bucketCapacities, copy));
        }

        //get the successors of pouring water from one bucket to another
        for (int i = 0; i < bucketCapacities.size(); i++) {
            for (int j = 0; j < bucketCapacities.size(); j++) {
                if (i != j && bucketAmounts.get(j) !=
                        bucketCapacities.get(j) &&
                        bucketAmounts.get(i) != 0) {
                    ArrayList<Integer> copy = copyBucketAmounts();

                    int amountToTransfer;
                    int amountLeftover;
                    if (bucketAmounts.get(i) <=
                        bucketCapacities.get(j) - bucketAmounts.get(j)) {
                        amountToTransfer = bucketAmounts.get(i);
                        amountLeftover = 0;
                    }
                    else {
                        amountToTransfer = bucketCapacities.get(j) -
                                bucketAmounts.get(j);
                        amountLeftover = bucketAmounts.get(i) - amountToTransfer;
                    }
                    copy.set(i, amountLeftover);
                    copy.set(j, copy.get(j) + amountToTransfer);

                    successors.add(new WaterConfig(desiredAmount,
                            bucketCapacities, copy));
                }
            }
        }

        return successors;
    }

    /**
     * @return true if the goal configuration for the water puzzle has
     * been reached
     */
    @Override
    public boolean isGoal() {
        for (int amount: bucketAmounts) {
            if (amount == desiredAmount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compares current config to the given config
     *
     * @param other config to compare to
     * @return true if both configurations are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof WaterConfig) {
            WaterConfig w = (WaterConfig) other;
            result = this.desiredAmount == w.desiredAmount &&
                    this.bucketCapacities.equals(w.bucketCapacities) &&
                    this.bucketAmounts.equals(w.bucketAmounts);
        }
        return result;
    }

    /**
     * @return integer representing the hashcode
     */
    @Override
    public int hashCode() {
        int hash = desiredAmount;
        for (int i: bucketCapacities) {
            hash += i;
        }
        for (int i: bucketAmounts) {
            hash += i;
        }
        return hash;
    }

    /**
     * @return String that represents the amount of water in each bucket
     */
    @Override
    public String toString() {
        return bucketAmounts.toString();
    }
}
