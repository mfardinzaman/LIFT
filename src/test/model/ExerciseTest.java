package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExerciseTest {
    private Exercise bentOverBarbellRow;
    private Exercise barbellBenchPress;
    private Exercise pullup;
    private Exercise deadlift;

    @BeforeEach
    void runBefore() {
        bentOverBarbellRow = new Exercise("Bent Over Barbell Row", Equipment.BARBELL);
        barbellBenchPress = new Exercise("Barbell Bench Press", Equipment.BARBELL);
        pullup = new Exercise("Pullup", Equipment.NONE, 5, 10);
        deadlift = new Exercise("Deadlift", Equipment.BARBELL, 1, 5);
    }

    @Test
    void testSetName() {
        pullup.setName("Assisted Pullup");
        assertEquals("Assisted Pullup", pullup.getName());
    }

    @Test
    void testSetEquipment() {
        pullup.setEquipment(Equipment.MACHINE);
        assertEquals(Equipment.MACHINE, pullup.getEquipment());
    }

    @Test
    void testHasMetGoalNoSets() {
        bentOverBarbellRow.setSets(3);
        assertFalse(bentOverBarbellRow.hasMetGoal());
    }

    @Test
    void testHasMetGoalPartialSet() {
        bentOverBarbellRow.setSets(3);
        bentOverBarbellRow.setSets(2);
        assertFalse(bentOverBarbellRow.hasMetGoal());
    }

    @Test
    void testHasMetGoalCompleted() {
        barbellBenchPress.setSets(5);
        barbellBenchPress.setSetsCompleted(5);
        assertTrue(bentOverBarbellRow.hasMetGoal());
    }

    @Test
    void testViewExerciseRow() {
        String expected = "|   | 0 |"
                + String.format("%-25s|%-4s|%-4s|%-5s|%-3s|",
                " Bent Over Barbell Row",
                " 0",
                " 0",
                " 0",
                " 0");

        assertEquals(expected, bentOverBarbellRow.viewExercise());
    }

    @Test
    void testViewExerciseBench() {
        barbellBenchPress.setSets(3);
        barbellBenchPress.setReps(5);
        barbellBenchPress.setWeight(95);

        String expected = "|   | 0 |"
                + String.format("%-25s|%-4s|%-4s|%-5s|%-3s|",
                " Barbell Bench Press",
                " 3",
                " 5",
                " 95",
                " 0");

        assertEquals(expected, barbellBenchPress.viewExercise());
    }
}
