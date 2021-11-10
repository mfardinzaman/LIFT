package model;

import org.json.JSONObject;
import persistence.Writeable;

/* Represents an individual exercise with its name, number of goal sets and reps, the weight it was completed with in
   lb, the equipment used, and the number of sets actually completed
 */
public class Exercise implements Writeable {
    private String name;
    private int sets;
    private int reps;
    private int weight;
    private Equipment equipment;
    private int setsCompleted;

    // REQUIRES: equipment must be barbell, dumbbell, machine, or none
    // EFFECTS: creates a new exercise with the given name and sets number of sets, reps, and weight to 0
    public Exercise(String name, Equipment equipment) {
        this.name = name;
        this.sets = 0;
        this.reps = 0;
        this.weight = 0;
        this.equipment = equipment;
        this.setsCompleted = 0;
    }

    // EFFECTS: returns true if number of sets completed is the same as the goal sets, false otherwise
    public boolean hasMetGoal() {
        return sets == setsCompleted; // stub
    }

//    // EFFECTS: returns a string representation of the exercise.
//    public String viewExercise() {
//        return this.name + "\t "
//                + this.sets + "\t "
//                + this.reps + "\t "
//                + this.weight + "\t "
//                + this.setsCompleted;
//    }

    // EFFECTS: returns a string representation of the exercise.
    public String viewExercise() {
        return "|   | 0 |"
                + String.format("%-25s|%-4s|%-4s|%-5s|%-3s|",
                " " + name,
                " " + sets,
                " " + reps,
                " " + weight,
                " " + setsCompleted);
    }

    // EFFECTS: returns the name of the exercise
    public String getName() {
        return name;
    }

    // EFFECTS: sets the name of the exercise
    public void setName(String name) {
        this.name = name;
    }

    // EFFECTS: returns the number of sets for the exercise
    public int getSets() {
        return sets;
    }

    // REQUIRES: sets must be non-zero positive integer
    // MODIFIES: this
    // EFFECTS: sets the number of sets
    public void setSets(int sets) {
        this.sets = sets;
    }

    // EFFECTS: returns the number of reps for the exercise
    public int getReps() {
        return reps;
    }

    // REQUIRES: reps must be non-zero positive integer
    // MODIFIES: this
    // EFFECTS: sets the number of reps
    public void setReps(int reps) {
        this.reps = reps;
    }

    // EFFECTS: returns the weight for the exercise
    public int getWeight() {
        return weight;
    }

    // REQUIRES: weight must be non-zero positive integer
    // MODIFIES: this
    // EFFECTS: sets the weight
    public void setWeight(int weight) {
        this.weight = weight;
    }

    // EFFECTS: returns equipment type
    public Equipment getEquipment() {
        return equipment;
    }

    // EFFECTS: sets equipment type
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    // EFFECTS: returns number of sets completed
    public int getSetsCompleted() {
        return setsCompleted;
    }

    // REQUIRES: completed must be non-zero positive integer
    // MODIFIES: this
    // EFFECTS: sets the number of sets completed
    public void setSetsCompleted(int setsCompleted) {
        this.setsCompleted = setsCompleted;
    }

    @Override
    // EFFECTS: returns this as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("sets", sets);
        json.put("reps", reps);
        json.put("equipment", equipment);

        return json;
    }

    public String[] toStringArray() {
        String[] exercise = {
                name,
                String.valueOf(equipment),
                String.valueOf(sets),
                String.valueOf(reps),
                String.valueOf(weight),
                String.valueOf(setsCompleted)
        };


        return exercise;
    }
}
