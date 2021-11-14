package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoutineTest {
    private Routine testRoutine;
    private Routine testRoutineA;
    private Routine testRoutineB;

    private Exercise bentOverBarbellRow;
    private Exercise barbellBenchPress;
    private Exercise barbellSquat;
    private Exercise bicepCurl;

    private Exercise pullup;
    private Exercise deadlift;
    private Exercise overheadBarbellPress;
    private Exercise inclineDumbbellPress;
    private Exercise tricepExtension;

    @BeforeEach
    void runBefore() {
        testRoutine = new Routine();
        testRoutineA = new Routine();
        testRoutineA.setName("Test Routine A");
        testRoutineB = new Routine("Test Routine B");

        bentOverBarbellRow = new Exercise("Bent Over Barbell Row", Equipment.BARBELL);
        barbellBenchPress = new Exercise("Barbell Bench Press", Equipment.BARBELL);
        barbellSquat = new Exercise("Barbell Squat", Equipment.BARBELL);
        bicepCurl = new Exercise("Bicep Curl", Equipment.DUMBBELL);

        pullup = new Exercise("Pullup", Equipment.NONE);
        deadlift = new Exercise("Deadlift", Equipment.BARBELL);
        overheadBarbellPress = new Exercise("Overhead Barbell Press", Equipment.BARBELL);
        inclineDumbbellPress = new Exercise("Incline Dumbbell Press", Equipment.DUMBBELL);
        tricepExtension = new Exercise("Tricep Extension", Equipment.MACHINE);

        testRoutineA.addExercise(bentOverBarbellRow, 3, 5);
        testRoutineA.addExercise(barbellBenchPress, 3, 5);
        testRoutineA.addExercise(barbellSquat, 3, 5);
        testRoutineA.addExercise(bicepCurl, 5, 10);

        testRoutineB.addExercise(pullup, 3, 5);
        testRoutineB.addExercise(deadlift, 1, 5);
        testRoutineB.addExercise(overheadBarbellPress, 3, 5);
        testRoutineB.addExercise(inclineDumbbellPress, 5, 10);
        testRoutineB.addExercise(tricepExtension, 5, 10);
    }

    @Test
    void addExerciseEmpty() {
        ArrayList<Exercise> exercises = testRoutine.getExercises();
        assertEquals(0, exercises.size());

        testRoutine.addExercise(bentOverBarbellRow, 3, 5);
        assertEquals(1, exercises.size());
        Exercise exercise0 = exercises.get(0);
        assertEquals("Bent Over Barbell Row", exercise0.getName());
        assertEquals(3, exercise0.getSets());
        assertEquals(5, exercise0.getReps());
    }

    @Test
    void addExerciseCompleteA() {
        testRoutine.addExercise(bentOverBarbellRow, 3, 5);
        ArrayList<Exercise> exercises = testRoutine.getExercises();
        assertEquals(1, exercises.size());
        Exercise exercise0 = exercises.get(0);
        assertEquals("Bent Over Barbell Row", exercise0.getName());
        assertEquals(3, exercise0.getSets());
        assertEquals(5, exercise0.getReps());

        testRoutine.addExercise(barbellBenchPress, 3, 5);
        testRoutine.addExercise(barbellSquat, 3, 5);
        testRoutine.addExercise(bicepCurl, 5, 10);

        assertEquals(4, testRoutine.getExercises().size());
        Exercise exercise3 = exercises.get(3);
        assertEquals("Bicep Curl", exercise3.getName());
        assertEquals(5, exercise3.getSets());
        assertEquals(10, exercise3.getReps());
    }

    @Test
    void removeExerciseLastAdded() {
        testRoutine.addExercise(bicepCurl, 3, 5);
        ArrayList<Exercise> exercises = testRoutine.getExercises();
        assertEquals(1, exercises.size());

        testRoutine.removeExercise(0);
        assertEquals(0, exercises.size());
    }

    @Test
    void removeExerciseAny() {
        ArrayList<Exercise> exercises = testRoutineB.getExercises();
        assertEquals(5, exercises.size());

        testRoutineB.removeExercise(2);
        assertEquals(4, exercises.size());
    }

    @Test
    void beginSessionRoutineA() {
        assertFalse(testRoutineA.getInSession());
        assertNull(testRoutineA.getCurrent());

        testRoutineA.beginSession();
        assertTrue(testRoutineA.getInSession());
        assertEquals("Bent Over Barbell Row", testRoutineA.getCurrent().getName());
    }

    @Test
    void beginSessionRoutineB() {
        assertFalse(testRoutineB.getInSession());
        assertNull(testRoutineB.getCurrent());

        testRoutineB.beginSession();
        assertTrue(testRoutineB.getInSession());
        assertEquals("Pullup", testRoutineB.getCurrent().getName());
    }

    @Test
    void endSessionRoutineA() {
        assertFalse(testRoutineA.getInSession());
        assertNull(testRoutineA.getCurrent());

        testRoutineA.beginSession();
        assertTrue(testRoutineA.getInSession());
        assertEquals("Bent Over Barbell Row", testRoutineA.getCurrent().getName());

        testRoutineA.endSession();
        assertFalse(testRoutineA.getInSession());
        assertNull(testRoutineA.getCurrent());
    }

    @Test
    void endSessionRoutineB() {
        assertFalse(testRoutineB.getInSession());
        assertNull(testRoutineB.getCurrent());

        testRoutineB.beginSession();
        assertTrue(testRoutineB.getInSession());
        assertEquals("Pullup", testRoutineB.getCurrent().getName());

        testRoutineB.endSession();
        assertFalse(testRoutineB.getInSession());
        assertNull(testRoutineB.getCurrent());
    }

    @Test
    void addWeightToCurrentBarbell() {
        testRoutineA.beginSession();
        Exercise current = testRoutineA.getCurrent();
        assertEquals(0, current.getWeight());

        testRoutineA.addWeightToCurrent(25);
        assertEquals(95, current.getWeight());

    }

    @Test
    void addWeightToCurrentDumbbell() {
        testRoutineA.beginSession();
        for (int i = 0; i < 9; i++) {
            testRoutineA.addSetToProgress();
        }
        Exercise current = testRoutineA.getCurrent();
        assertEquals(0, current.getWeight());

        testRoutineA.addWeightToCurrent(15);
        assertEquals(15, current.getWeight());

    }

    @Test
    void addWeightToCurrentMachine() {
        testRoutineB.beginSession();
        for (int i = 0; i < 12; i++) {
            testRoutineB.addSetToProgress();
        }
        Exercise current = testRoutineB.getCurrent();
        assertEquals(0, current.getWeight());

        testRoutineB.addWeightToCurrent(15);
        assertEquals(15, current.getWeight());

    }

    @Test
    void addSetToProgressIncompleteSet() {
        testRoutineA.beginSession();
        testRoutineA.addSetToProgress();

        assertEquals(1, testRoutineA.getProgress());
        assertEquals("Bent Over Barbell Row", testRoutineA.getCurrent().getName());
    }

    @Test
    void addSetToProgressCompleteSetNextExercise() {
        testRoutineA.beginSession();
        testRoutineA.addSetToProgress();
        testRoutineA.addSetToProgress();

        assertEquals(2, testRoutineA.getProgress());
        assertEquals("Bent Over Barbell Row", testRoutineA.getCurrent().getName());

        testRoutineA.addSetToProgress();
        Exercise previous = testRoutineA.getExercises().get(0);
        assertEquals(3, previous.getSetsCompleted());
        assertEquals(0, testRoutineA.getProgress());
        assertEquals("Barbell Bench Press", testRoutineA.getCurrent().getName());
    }

    @Test
    void addSetToProgressCompleteSetMultipleExercises() {
        ArrayList<Exercise> exercises = testRoutineB.getExercises();

        testRoutineB.beginSession();
        for (int i = 0; i < 4; i++) {
            testRoutineB.addSetToProgress();
        }
        assertEquals(3, exercises.get(0).getSetsCompleted());
        assertEquals(1, exercises.get(1).getSetsCompleted());
        assertEquals(0, testRoutineB.getProgress());
        assertEquals("Overhead Barbell Press", testRoutineB.getCurrent().getName());
    }

    @Test
    void addSetToProgressCompleteRoutine() {
        ArrayList<Exercise> exercises = testRoutineA.getExercises();

        testRoutineA.beginSession();
        for (int i = 0; i < 13; i++) {
            testRoutineA.addSetToProgress();
        }
        assertEquals(4, testRoutineA.getProgress());
        assertEquals("Bicep Curl", testRoutineA.getCurrent().getName());


        testRoutineA.addSetToProgress();
        assertFalse(testRoutineA.getInSession());
        assertNull(testRoutineA.getCurrent());
        assertEquals(0, testRoutineA.getProgress());
        assertEquals(5, exercises.get(3).getSetsCompleted());
    }

    @Test
    void skipExerciseWholeExercise() {
        ArrayList<Exercise> exercises = testRoutineA.getExercises();

        testRoutineA.beginSession();
        assertEquals(0, testRoutineA.getProgress());
        assertEquals("Bent Over Barbell Row", testRoutineA.getCurrent().getName());

        testRoutineA.skipExercise();
        assertEquals(0, exercises.get(0).getSetsCompleted());
        assertEquals(0, testRoutineA.getProgress());
        assertEquals("Barbell Bench Press", testRoutineA.getCurrent().getName());
    }

    @Test
    void skipExerciseOneSetComplete() {
        ArrayList<Exercise> exercises = testRoutineA.getExercises();

        testRoutineA.beginSession();
        testRoutineA.addSetToProgress();
        assertEquals(1, testRoutineA.getProgress());
        assertEquals("Bent Over Barbell Row", testRoutineA.getCurrent().getName());

        testRoutineA.skipExercise();
        assertEquals(1, exercises.get(0).getSetsCompleted());
        assertEquals(0, testRoutineA.getProgress());
        assertEquals("Barbell Bench Press", testRoutineA.getCurrent().getName());
    }

    @Test
    void skipExerciseSkipTwo() {
        ArrayList<Exercise> exercises = testRoutineB.getExercises();

        testRoutineB.beginSession();
        testRoutineB.addSetToProgress();
        testRoutineB.addSetToProgress();
        testRoutineB.skipExercise();
        testRoutineB.skipExercise();

        assertEquals(2, exercises.get(0).getSetsCompleted());
        assertEquals(0, exercises.get(1).getSetsCompleted());
        assertEquals(0, testRoutineB.getProgress());
        assertEquals("Overhead Barbell Press", testRoutineB.getCurrent().getName());
    }

    @Test
    void skipExerciseCompleteRoutine() {
        ArrayList<Exercise> exercises = testRoutineB.getExercises();

        testRoutineB.beginSession();
        for (int i = 0; i < 12; i++) {
            testRoutineB.addSetToProgress();
        }

        testRoutineB.skipExercise();
        assertEquals(0, exercises.get(4).getSetsCompleted());
        assertFalse(testRoutineB.getInSession());
        assertNull(testRoutineB.getCurrent());
        assertEquals(0, testRoutineB.getProgress());
    }

    @Test
    void hasMetGoalRoutineAEmpty() {
        assertFalse(testRoutineA.hasMetAllGoals());
    }

    @Test
    void hasMetGoalRoutineAPartial() {
        testRoutineA.beginSession();

        for (int i = 0; i < 3; i++) {
            testRoutineA.addWeightToCurrent((i + 1) * 25);
            for (int j = 0; j < 3; j++) {
                testRoutineA.addSetToProgress();
            }
        }

        assertFalse(testRoutineA.hasMetAllGoals());
    }

    @Test
    void hasMetGoalRoutineBComplete() {
        testRoutineB.beginSession();

        for (int i = 0; i < 3; i++) {
            testRoutineB.addSetToProgress();
        }
        testRoutineB.addWeightToCurrent(45);
        testRoutineB.addSetToProgress();
        testRoutineB.addWeightToCurrent(0);
        for (int i = 0; i < 3; i++) {
            testRoutineB.addSetToProgress();
        }
        testRoutineB.addWeightToCurrent(25);
        for (int i = 0; i < 5; i++) {
            testRoutineB.addSetToProgress();
        }
        testRoutineB.addWeightToCurrent(15);
        for (int i = 0; i < 5; i++) {
            testRoutineB.addSetToProgress();
        }

        assertTrue(testRoutineB.hasMetAllGoals());
    }

    @Test
    void viewWorkoutRoutineANotStarted() {
        assertEquals("Test Routine A\n"
                        + "|Cur|Num| Name                    |Sets|Reps| Wgt |Prg|\n"
                        + "=======================================================\n"
                        + "|   | 1 | Bent Over Barbell Row   | 3  | 5  | 0   | 0 |\n"
                        + "|   | 2 | Barbell Bench Press     | 3  | 5  | 0   | 0 |\n"
                        + "|   | 3 | Barbell Squat           | 3  | 5  | 0   | 0 |\n"
                        + "|   | 4 | Bicep Curl              | 5  | 10 | 0   | 0 |\n"
                        + "=======================================================\n"
                        + "Met Goal: No",
                testRoutineA.viewWorkout());
    }

    @Test
    void viewWorkoutRoutineAJustStarted() {
        testRoutineA.beginSession();

        assertEquals("Test Routine A\n"
                        + "|Cur|Num| Name                    |Sets|Reps| Wgt |Prg|\n"
                        + "=======================================================\n"
                        + "| * | 1 | Bent Over Barbell Row   | 3  | 5  | 0   | 0 |\n"
                        + "|   | 2 | Barbell Bench Press     | 3  | 5  | 0   | 0 |\n"
                        + "|   | 3 | Barbell Squat           | 3  | 5  | 0   | 0 |\n"
                        + "|   | 4 | Bicep Curl              | 5  | 10 | 0   | 0 |\n"
                        + "=======================================================\n"
                        + "Met Goal: No",
                testRoutineA.viewWorkout());
    }

    @Test
    void viewWorkoutRoutineAPartial() {
        testRoutineA.beginSession();

        for (int i = 0; i < 3; i++) {
            testRoutineA.addWeightToCurrent((i + 1) * 25);
            for (int j = 0; j < 3; j++) {
                testRoutineA.addSetToProgress();
            }
        }

        assertEquals("Test Routine A\n"
                        + "|Cur|Num| Name                    |Sets|Reps| Wgt |Prg|\n"
                        + "=======================================================\n"
                        + "|   | 1 | Bent Over Barbell Row   | 3  | 5  | 95  | 3 |\n"
                        + "|   | 2 | Barbell Bench Press     | 3  | 5  | 145 | 3 |\n"
                        + "|   | 3 | Barbell Squat           | 3  | 5  | 195 | 3 |\n"
                        + "| * | 4 | Bicep Curl              | 5  | 10 | 0   | 0 |\n"
                        + "=======================================================\n"
                        + "Met Goal: No",
                testRoutineA.viewWorkout());
    }

    @Test
    void viewWorkoutRoutineBPartial() {
        testRoutineB.beginSession();

        for (int i = 0; i < 3; i++) {
            testRoutineB.addSetToProgress();
        }
        testRoutineB.addWeightToCurrent(45);
        testRoutineB.addSetToProgress();
        testRoutineB.addWeightToCurrent(0);
        testRoutineB.addSetToProgress();

        assertEquals("Test Routine B\n"
                        + "|Cur|Num| Name                    |Sets|Reps| Wgt |Prg|\n"
                        + "=======================================================\n"
                        + "|   | 1 | Pullup                  | 3  | 5  | 0   | 3 |\n"
                        + "|   | 2 | Deadlift                | 1  | 5  | 135 | 1 |\n"
                        + "| * | 3 | Overhead Barbell Press  | 3  | 5  | 45  | 1 |\n"
                        + "|   | 4 | Incline Dumbbell Press  | 5  | 10 | 0   | 0 |\n"
                        + "|   | 5 | Tricep Extension        | 5  | 10 | 0   | 0 |\n"
                        + "=======================================================\n"
                        + "Met Goal: No",
                testRoutineB.viewWorkout());
    }

    @Test
    void viewWorkoutRoutineBComplete() {
        testRoutineB.beginSession();

        for (int i = 0; i < 3; i++) {
            testRoutineB.addSetToProgress();
        }
        testRoutineB.addWeightToCurrent(45);
        testRoutineB.addSetToProgress();
        testRoutineB.addWeightToCurrent(0);
        for (int i = 0; i < 3; i++) {
            testRoutineB.addSetToProgress();
        }
        testRoutineB.addWeightToCurrent(25);
        for (int i = 0; i < 5; i++) {
            testRoutineB.addSetToProgress();
        }
        testRoutineB.addWeightToCurrent(15);
        for (int i = 0; i < 5; i++) {
            testRoutineB.addSetToProgress();
        }

        assertEquals("Test Routine B\n"
                        + "|Cur|Num| Name                    |Sets|Reps| Wgt |Prg|\n"
                        + "=======================================================\n"
                        + "|   | 1 | Pullup                  | 3  | 5  | 0   | 3 |\n"
                        + "|   | 2 | Deadlift                | 1  | 5  | 135 | 1 |\n"
                        + "|   | 3 | Overhead Barbell Press  | 3  | 5  | 45  | 3 |\n"
                        + "|   | 4 | Incline Dumbbell Press  | 5  | 10 | 25  | 5 |\n"
                        + "|   | 5 | Tricep Extension        | 5  | 10 | 15  | 5 |\n"
                        + "=======================================================\n"
                        + "Met Goal: Yes",
                testRoutineB.viewWorkout());
    }
}