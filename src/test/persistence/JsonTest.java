package persistence;

import model.Equipment;
import model.Exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkExercise(String name, int sets, int reps, Equipment equipment, Exercise exercise) {
        assertEquals(name, exercise.getName());
        assertEquals(sets, exercise.getSets());
        assertEquals(reps, exercise.getReps());
        assertEquals(equipment, exercise.getEquipment());
    }
}
