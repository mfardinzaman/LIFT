package model;

import org.json.JSONObject;
import persistence.Writeable;

/**
 * Represents an individual exercise with its name, number of goal sets and reps, the weight it was completed with in
 * lb, the equipment used, and the number of sets actually completed
  */
public class Exercise implements Writeable {
    private String name;
    private int sets;
    private int reps;
    private int weight;
    private Equipment equipment;
    private int setsCompleted;

    /*
     REQUIRES: equipment must be barbell, dumbbell, machine, or none
     EFFECTS: creates a new exercise with the given name and sets number of sets, reps, and weight to 0
     */
    public Exercise(String name, Equipment equipment) {
        this.name = name;
        this.sets = 0;
        this.reps = 0;
        this.weight = 0;
        this.equipment = equipment;
        this.setsCompleted = 0;
    }

    /*
     REQUIRES: equipment must be barbell, dumbbell, machine, or none
               reps and sets must be > 0
     EFFECTS: creates a new exercise with the given name, sets, and reps, and sets weight to 0
     */
    public Exercise(String name, Equipment equipment, int sets, int reps) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = 0;
        this.equipment = equipment;
        this.setsCompleted = 0;
    }

    /*
    EFFECTS: returns true if number of sets completed is the same as the goal sets, false otherwise
     */
    public boolean hasMetGoal() {
        return sets == setsCompleted; // stub
    }

    /*
    EFFECTS: returns a string representation of the exercise.
     */
    public String viewExercise() {
        return "|   | 0 |"
                + String.format("%-25s|%-4s|%-4s|%-5s|%-3s|",
                " " + name,
                " " + sets,
                " " + reps,
                " " + weight,
                " " + setsCompleted);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String prev = this.name;
        this.name = name;
        EventLog.getInstance().logEvent(new Event("Changed exercise name from " + prev + " to " + name));
    }

    public int getSets() {
        return sets;
    }

    /*
     REQUIRES: sets must be non-zero positive integer
     MODIFIES: this
     EFFECTS: sets the number of sets
     */
    public void setSets(int sets) {
        String prev = Integer.toString(this.sets);
        this.sets = sets;
        EventLog.getInstance().logEvent(new Event("Changed " + this.name + " sets from "
                + prev
                + " to "
                + sets));
    }

    public int getReps() {
        return reps;
    }

    /*
    REQUIRES: reps must be non-zero positive integer
    MODIFIES: this
    EFFECTS: sets the number of reps
     */
    public void setReps(int reps) {
        String prev = Integer.toString(this.reps);
        this.reps = reps;
        EventLog.getInstance().logEvent(new Event("Changed " + this.name + " reps from "
                + prev
                + " to "
                + reps));
    }

    public int getWeight() {
        return weight;
    }

    /*
    REQUIRES: weight must be non-zero positive integer
    MODIFIES: this
    EFFECTS: sets the weight
     */
    public void setWeight(int weight) {
        String prev = Integer.toString(this.weight);
        this.weight = weight;
        EventLog.getInstance().logEvent(new Event("Changed " + this.name + " weight from "
                + prev
                + " to "
                + weight));
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        Equipment prev = this.equipment;
        this.equipment = equipment;
        EventLog.getInstance().logEvent(new Event("Changed " + this.name + " equipment from "
                + prev
                + " to "
                + equipment));
    }

    public int getSetsCompleted() {
        return setsCompleted;
    }

    /*
    REQUIRES: completed must be non-zero positive integer
    MODIFIES: this
    EFFECTS: sets the number of sets completed
     */
    public void setSetsCompleted(int setsCompleted) {
        this.setsCompleted = setsCompleted;
    }

    @Override
    /*
    EFFECTS: returns this as a JSON object
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("sets", sets);
        json.put("reps", reps);
        json.put("equipment", equipment);

        return json;
    }
}
