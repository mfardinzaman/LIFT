package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExerciseTest {
    private Exercise bentOverBarbellRow;
    private Exercise barbellBenchPress;

    @BeforeEach
    void runBefore() {
        bentOverBarbellRow = new Exercise("Bent Over Barbell Row", Equipment.Barbell);
        barbellBenchPress = new Exercise("Barbell Bench Press", Equipment.Barbell);
    }

    @Test
    void hasMetGoalNoSets() {
        bentOverBarbellRow.setSets(3);
        assertFalse(bentOverBarbellRow.hasMetGoal());
    }

    @Test
    void hasMetGoalPartialSet() {
        bentOverBarbellRow.setSets(3);
        bentOverBarbellRow.setSets(2);
        assertFalse(bentOverBarbellRow.hasMetGoal());
    }

    @Test
    void hasMetGoalCompleted() {
        barbellBenchPress.setSets(5);
        barbellBenchPress.setSetsCompleted(5);
        assertTrue(bentOverBarbellRow.hasMetGoal());
    }

    @Test
    void viewExerciseRow() {
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
    void viewExerciseBench() {
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
