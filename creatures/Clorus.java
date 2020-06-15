package creatures;

import huglife.*;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Random;

public class Clorus extends Creature {
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;


    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    @Override
    public void move() {
        energy = Math.min(0, energy - 0.03);
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public Clorus replicate() {
        Clorus c = new Clorus(energy / 2);
        energy /= 2;
        return c;
    }

    @Override
    public void stay() {
        energy += 0.01;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Random rand = new Random();

        for (Direction d : neighbors.keySet()) {
            if ((neighbors.get(d) instanceof Empty)) emptyNeighbors.add(d);
        }

        // rule 1
        if (emptyNeighbors.isEmpty()) return new Action(Action.ActionType.STAY);

        // rule 2
        Deque<Direction> plipsAround = new ArrayDeque<>();
        for (Direction d : neighbors.keySet()) {
            if (neighbors.get(d).name().equals("plip")) plipsAround.add(d);
        }

        if (!plipsAround.isEmpty()) {
            //Random rand = new Random();
            int unluckyOneIndex = rand.nextInt(plipsAround.size());
            for (Direction d : plipsAround) {
                if (unluckyOneIndex > 0) unluckyOneIndex--;
                else {
                    return new Action(Action.ActionType.ATTACK, d);
                }
            }
        }

        // rule 3
        if (energy >= 1) {
            //Random rand = new Random();
            int randDirIndex = rand.nextInt(emptyNeighbors.size());
            for (Direction d : emptyNeighbors) {
                if (randDirIndex > 0) randDirIndex--;
                else {
                    return new Action(Action.ActionType.REPLICATE, d);
                }
            }
        }

        // rule 4

        int moveToIndex = rand.nextInt(emptyNeighbors.size());
        for (Direction d : emptyNeighbors) {
            if (moveToIndex > 0) moveToIndex--;
            else {
                return new Action(Action.ActionType.MOVE, d);
            }
        }
        return null;
    }

    @Override
    public Color color() {
        return color(r, g, b);
    }
}
