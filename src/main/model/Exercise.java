package model;

/* Represents an individual exercise with its name, number of goal sets and reps, the weight it was completed with in
   lb, the equipment used, and the number of sets actually completed
 */
public class Exercise {
    private String name;
    private int sets;
    private int reps;
    private int weight;
    private String equipment;
    private int setsCompleted;

    // REQUIRES: equipment must be barbell, dumbbell, machine, or none
    // EFFECTS: Creates a new exercise with the given name. Sets number of sets, reps, and weight to 0.
    public Exercise(String name, String equipment) {
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

    // EFFECTS: Returns a string representation of the exercise.
    public String viewExercise() {
        return this.name + "\t "
                + this.sets + "\t "
                + this.reps + "\t "
                + this.weight + "\t "
                + this.setsCompleted;
    }

    // EFFECTS: returns the name of the exercise
    public String getName() {
        return name;
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
    public String getEquipment() {
        return equipment;
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
}
