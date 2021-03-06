package persistence;

import model.Equipment;
import model.Exercise;
import model.Routine;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest{
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader();
        reader.setSource("./data/noSuchFile.json");
        try {
            Routine r = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyRoutine() {
        JsonReader reader = new JsonReader();
        reader.setSource("./data/testEmptyRoutine.json");
        try {
            Routine r = reader.read();
            assertEquals("Empty Routine", r.getName());
            assertEquals(0, r.getExercises().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderRoutineA() {
        JsonReader reader = new JsonReader();
        reader.setSource("./data/testRoutineA.json");
        try {
            Routine r = reader.read();
            assertEquals("Test Routine A", r.getName());

            ArrayList<Exercise> exercises = r.getExercises();
            assertEquals(4, exercises.size());

            checkExercise("Bent Over Barbell Row", 3, 5, Equipment.BARBELL, exercises.get(0));
            checkExercise("Barbell Bench Press", 3, 5, Equipment.BARBELL, exercises.get(1));
            checkExercise("Barbell Squat", 3, 5, Equipment.BARBELL, exercises.get(2));
            checkExercise("Bicep Curl", 5, 10, Equipment.DUMBBELL, exercises.get(3));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderRoutineB() {
        JsonReader reader = new JsonReader();
        reader.setSource("./data/testRoutineB.json");
        try {
            Routine r = reader.read();
            assertEquals("Test Routine B", r.getName());

            ArrayList<Exercise> exercises = r.getExercises();
            assertEquals(5, exercises.size());

            checkExercise("Pullup", 3, 5, Equipment.NONE, exercises.get(0));
            checkExercise("Deadlift", 1, 5, Equipment.BARBELL, exercises.get(1));
            checkExercise("Overhead Barbell Press", 3, 5, Equipment.BARBELL, exercises.get(2));
            checkExercise("Incline Dumbbell Press", 5, 10, Equipment.DUMBBELL, exercises.get(3));
            checkExercise("Tricep Extension", 5, 10, Equipment.MACHINE, exercises.get(4));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
