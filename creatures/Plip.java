package creatures;

import edu.princeton.cs.algs4.StdRandom;
import huglife.*;

import javax.naming.directory.DirContext;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Random;

/**
 * An implementation of a motile pacifist photosynthesizer.
 *
 * @author Josh Hug
 */
public class Plip extends Creature {

    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;

    private final double lostEnergy = 0.15;

    private final double gainEnergy = 0.2;

    private final double maxEnergy = 2.0;

    private final double minEnergy = 0.0;
    /**
     * creates plip with energy equal to E.
     */
    public Plip(double e) {
        super("plip");
        r = 99;
        g = 0;
        b = 76;
        energy = e;
    }

    /**
     * creates a plip with energy equal to 1.
     */
    public Plip() {
        this(1);
    }

    /**
     * Should return a color with red = 99, blue = 76, and green that varies
     * linearly based on the energy of the Plip. If the plip has zero energy,
     * it should have a green value of 63. If it has max energy, it should
     * have a green value of 255. The green value should vary with energy
     * linearly in between these two extremes. It's not absolutely vital
     * that you get this exactly correct.
     */
    public Color color() {
        g = energy == minEnergy ? 63 : energy == maxEnergy ? 255 : (int) energy * 96 + 63;
        return color(r, g, b);
    }

    /**
     * Do nothing with C, Plips are pacifists.
     */
    public void attack(Creature c) {
        // do nothing.
    }

    /**
     * Plips should lose 0.15 units of energy when moving. If you want to
     * to avoid the magic number warning, you'll need to make a
     * private static final variable. This is not required for this lab.
     */
    public void move() {
        energy = Math.max(minEnergy, energy - lostEnergy);
    }


    /**
     * Plips gain 0.2 energy when staying due to photosynthesis.
     */
    public void stay() {
        energy = Math.min(maxEnergy, energy + gainEnergy);
    }

    /**
     * Plips and their offspring each get 50% of the energy, with none
     * lost to the process. Now that's efficiency! Returns a baby
     * Plip.
     */
    public Plip replicate() {
        Plip offspring = new Plip(energy / 2);
        energy /= 2;
        return offspring;
    }

    /**
     * Plips take exactly the following actions based on NEIGHBORS:
     * 1. If no empty adjacent spaces, STAY.
     * 2. Otherwise, if energy >= 1, REPLICATE towards an empty direction
     * chosen at random.
     * 3. Otherwise, if any Cloruses, MOVE with 50% probability,
     * towards an empty direction chosen at random.
     * 4. Otherwise, if nothing else, STAY
     * <p>
     * Returns an object of type Action. See Action.java for the
     * scoop on how Actions work. See SampleCreature.chooseAction()
     * for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        // Rule 1
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        boolean anyClorus = false;
        Random rand = new Random();

        for (Direction d : neighbors.keySet()) {
            if ((neighbors.get(d) instanceof Empty)) emptyNeighbors.add(d);
        }

        if (emptyNeighbors.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        }

        // Rule 2
        // HINT: randomEntry(emptyNeighbors)
        if (energy >= 1.0) {
            //Random rand = new Random();
            int randDirIndex = rand.nextInt(emptyNeighbors.size());
            for (Direction d : emptyNeighbors) {
                if (randDirIndex > 0) randDirIndex--;
                else {
                    return new Action(Action.ActionType.REPLICATE, d);
                }
            }

        }

        // Rule 3
        for (Direction d : neighbors.keySet()) {
            if (neighbors.get(d).name().equals("clorus")) {
                anyClorus = true;
                break;
            }
        }

        if (anyClorus) {
            //Random rand = new Random();
            int decision = rand.nextInt(2);
            if (decision == 0) {
                int randDirIndex = rand.nextInt(emptyNeighbors.size());
                for (Direction d : emptyNeighbors) {
                    if (randDirIndex > 0) randDirIndex--;
                    else {
                        return new Action(Action.ActionType.MOVE, d);
                    }
                }
            }
        }


        // Rule 4
        return new Action(Action.ActionType.STAY);
    }
}
