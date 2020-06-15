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

/** Tests the plip class
 *  @authr Kefan Zhang
 */

public class TestPlip {

    @Test
    public void testBasics() {
        Plip p = new Plip(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(99, 255, 76), p.color());
        p.move();
        assertEquals(1.85, p.energy(), 0.01);
        p.move();
        assertEquals(1.70, p.energy(), 0.01);
        p.stay();
        assertEquals(1.90, p.energy(), 0.01);
        p.stay();
        assertEquals(2.00, p.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        double e = 0.15;
        Plip p = new Plip(e);
        Plip offspring = p.replicate();
        assertEquals(e / 2, p.energy(), 0.01);
        assertEquals(e / 2, offspring.energy(), 0.01);
    }

    @Test
    public void testChoose() {

        // No empty adjacent spaces; stay.
        Plip p = new Plip(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        p = new Plip(1.2);
        HashMap<Direction, Occupant> topEmpty = new HashMap<Direction, Occupant>();
        topEmpty.put(Direction.TOP, new Empty());
        topEmpty.put(Direction.BOTTOM, new Impassible());
        topEmpty.put(Direction.LEFT, new Impassible());
        topEmpty.put(Direction.RIGHT, new Impassible());

        actual = p.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.REPLICATE, Direction.TOP);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        p = new Plip(1.2);
        HashMap<Direction, Occupant> allEmpty = new HashMap<Direction, Occupant>();
        allEmpty.put(Direction.TOP, new Empty());
        allEmpty.put(Direction.BOTTOM, new Empty());
        allEmpty.put(Direction.LEFT, new Empty());
        allEmpty.put(Direction.RIGHT, new Empty());

        actual = p.chooseAction(allEmpty);
        Action unexpected = new Action(Action.ActionType.STAY);

        assertNotEquals(unexpected, actual);


        // Energy < 1; stay.
        p = new Plip(.99);

        actual = p.chooseAction(allEmpty);
        expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // Energy < 1; stay.
        p = new Plip(.99);

        actual = p.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // We don't have Cloruses yet, so we can't test behavior for when they are nearby right now.
    }
    @Test
    public void testName() {
        Plip p = new Plip(1.5);
        assertEquals("plip", p.name());
    }

    @Test
    public void testEnergy() {
        double e = 1.5;
        Plip p = new Plip(e);
        p.stay();
        System.out.println("actual: " + p.energy() + " expected: " + (e + 0.2));
        p.move();
        System.out.println("actual: " + p.energy() + " expected: " + (e + 0.2 - 0.15));
        p.stay();
        p.stay();
        p.stay();
        System.out.println("actual: " + p.energy() + " expected: " + 2.0);
        Plip p1 = new Plip(0.1);
        p1.move();
        System.out.println("actual: " + p1.energy() + " expected: " + 0.0);
    }

    @Test
    public void testColor() {
        Plip p = new Plip(1.9);
        System.out.println("actual: " + p.color() + " expected: " + "g: " + ((int) p.energy() * 96 + 63));
        p.stay();
        System.out.println("actual: " + p.color() + " expected: " + "g: " + 255);

        Plip p1 = new Plip(0.1);
        System.out.println("actual: " + p1.color() + " expected: " + "g: " + ((int) p1.energy() * 96 + 63));
        p1.move();
        System.out.println("actual: " + p1.color() + " expected: " + "g: " + 63);




    }
}
