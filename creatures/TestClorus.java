package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the Clorus class
 *  @authr Kefan Zhang
 */

public class TestClorus {
    @Test
    public void testBasics() {
        Clorus c = new Clorus(.02);
        assertEquals(0.02, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(0, c.energy(), 0.01);
        c.stay();
        assertEquals(0.01, c.energy(), 0.01);
        c.stay();
        assertEquals(0.02, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        double e = 0.9;
        Clorus c = new Clorus(e);
        Clorus offspring = c.replicate();
        assertEquals(e / 2, c.energy(), 0.01);
        assertEquals(e / 2, offspring.energy(), 0.01);
    }

    @Test
    public void testAttack() {
        Plip p = new Plip(1.5);
        Clorus c = new Clorus(2);
        c.attack(p);
        assertEquals(3.5, c.energy(), 0.01);
    }

    @Test
    public void testChoose() {
        // rule 1 -> no empty neighbor: STAY
        double startingEnergy = 3;
        Clorus c = new Clorus(startingEnergy);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        // rule 2 -> seen but no empty: still STAY
        HashMap<Direction, Occupant> with3Plips = new HashMap<>();
        with3Plips.put(Direction.TOP, new Plip(1.5));
        with3Plips.put(Direction.BOTTOM, new Impassible());
        with3Plips.put(Direction.LEFT, new Impassible());
        with3Plips.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(with3Plips);
        expected = new Action(Action.ActionType.STAY);

        //expected = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(expected, actual);
        //assertEquals(startingEnergy, c.energy(), 0.01);

        // rule 2 -> with plip and empty: attack

        HashMap<Direction, Occupant> withPlips = new HashMap<>();
        withPlips.put(Direction.TOP, new Plip(1.5));
        withPlips.put(Direction.BOTTOM, new Empty());
        withPlips.put(Direction.LEFT, new Impassible());
        withPlips.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(withPlips);
        expected = new Action(Action.ActionType.ATTACK, Direction.TOP);

        //expected = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(expected, actual);
        //assertEquals(startingEnergy + 1.5, c.energy(), 0.01);

        // rule 3 -> energy >= 1, replicate
        HashMap<Direction, Occupant> emptyNoPlip = new HashMap<>();
        emptyNoPlip.put(Direction.TOP, new Impassible());
        emptyNoPlip.put(Direction.BOTTOM, new Empty());
        emptyNoPlip.put(Direction.LEFT, new Impassible());
        emptyNoPlip.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(emptyNoPlip);
        expected = new Action(Action.ActionType.REPLICATE, Direction.BOTTOM);

        HashMap<Direction, Occupant> manyEmpty = new HashMap<>();
        manyEmpty.put(Direction.TOP, new Empty());
        manyEmpty.put(Direction.BOTTOM, new Empty());
        manyEmpty.put(Direction.LEFT, new Empty());
        manyEmpty.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(manyEmpty);
        Action unexpected = new Action(Action.ActionType.STAY);

        assertNotEquals(unexpected, actual);

        // rule 4 -> move to empty if no enough energy
        Clorus cc = new Clorus(0.99);

        actual = cc.chooseAction(manyEmpty);
        assertNotEquals(unexpected, actual);

        actual = cc.chooseAction(emptyNoPlip);
        expected = new Action(Action.ActionType.MOVE, Direction.BOTTOM);

        assertEquals(expected, actual);

    }










}




