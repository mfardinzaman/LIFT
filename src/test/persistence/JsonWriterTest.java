package persistence;

import model.Equipment;
import model.Exercise;
import model.Routine;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest{
    @Test
    void testWriterInvalidFile() {
        try {
            Routine r = new Routine("Empty Routine");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyRoutine() {
        try {
            Routine r = new Routine("Empty Routine");
            JsonWriter writer = new JsonWriter("./data/testEmptyRoutine.json");
            writer.open();
            writer.write(r);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmptyRoutine.json");
            r = reader.read();
            assertEquals("Empty Routine", r.getName());
            assertEquals(0, r.getExercises().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testWriterRoutineA() {
        try {
            Routine r = new Routine("Test Routine A");
            Exercise bentOverBarbellRow = new Exercise("Bent Over Barbell Row", Equipment.Barbell);
            Exercise barbellBenchPress = new Exercise("Barbell Bench Press", Equipment.Barbell);
            Exercise barbellSquat = new Exercise("Barbell Squat", Equipment.Barbell);
            Exercise bicepCurl = new Exercise("Bicep Curl", Equipment.Dumbbell);
            r.addExercise(bentOverBarbellRow, 3, 5);
            r.addExercise(barbellBenchPress, 3, 5);
            r.addExercise(barbellSquat, 3, 5);
            r.addExercise(bicepCurl, 5, 10);

            JsonWriter writer = new JsonWriter("./data/testRoutineA.json");
            writer.open();
            writer.write(r);
            writer.close();

            JsonReader reader = new JsonReader("./data/testRoutineA.json");
            r = reader.read();
            assertEquals("Test Routine A", r.getName());

            ArrayList<Exercise> exercises = r.getExercises();
            assertEquals(4, exercises.size());

            checkExercise("Bent Over Barbell Row", 3, 5, Equipment.Barbell, exercises.get(0));
            checkExercise("Barbell Bench Press", 3, 5, Equipment.Barbell, exercises.get(1));
            checkExercise("Barbell Squat", 3, 5, Equipment.Barbell, exercises.get(2));
            checkExercise("Bicep Curl", 5, 10, Equipment.Dumbbell, exercises.get(3));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testWriterRoutineB() {
        try {
            Routine r = new Routine("Test Routine B");
            Exercise pullup = new Exercise("Pullup", Equipment.None);
            Exercise deadlift = new Exercise("Deadlift", Equipment.Barbell);
            Exercise overheadBarbellPress = new Exercise("Overhead Barbell Press", Equipment.Barbell);
            Exercise inclineDumbbellPress = new Exercise("Incline Dumbbell Press", Equipment.Dumbbell);
            Exercise tricepExtension = new Exercise("Tricep Extension", Equipment.Machine);
            r.addExercise(pullup, 3, 5);
            r.addExercise(deadlift, 1, 5);
            r.addExercise(overheadBarbellPress, 3, 5);
            r.addExercise(inclineDumbbellPress, 5, 10);
            r.addExercise(tricepExtension, 5, 10);

            JsonWriter writer = new JsonWriter("./data/testRoutineB.json");
            writer.open();
            writer.write(r);
            writer.close();

            JsonReader reader = new JsonReader("./data/testRoutineB.json");
            r = reader.read();
            assertEquals("Test Routine B", r.getName());

            ArrayList<Exercise> exercises = r.getExercises();
            assertEquals(5, exercises.size());

            checkExercise("Pullup", 3, 5, Equipment.None, exercises.get(0));
            checkExercise("Deadlift", 1, 5, Equipment.Barbell, exercises.get(1));
            checkExercise("Overhead Barbell Press", 3, 5, Equipment.Barbell, exercises.get(2));
            checkExercise("Incline Dumbbell Press", 5, 10, Equipment.Dumbbell, exercises.get(3));
            checkExercise("Tricep Extension", 5, 10, Equipment.Machine, exercises.get(4));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
