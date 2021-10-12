package model;

import java.util.ArrayList;

public class Routine {
    private ArrayList<Exercise> exercises;
    private boolean inSession;
    private Exercise current;
    private int progress;

    private static final int WEIGHT_OF_BAR = 45;

    /* EFFECTS: Initializes a workout routine that is not in session with empty exercise list,
                no currently selected exercise, and progress set to 0.
     */
    public Routine() {
        exercises = new ArrayList<>();
        inSession = false;
        current = null;
        progress = 0;
    }

    // REQUIRES: Exercise should not be in the current routine.
    // MODIFIES: this
    // EFFECTS: Adds an exercise to the exercise list with indicated sets and reps.
    public void addExercise(Exercise e, int sets, int reps) {
        e.setSets(sets);
        e.setReps(reps);

        this.exercises.add(e);
    }

    // REQUIRES: Exercise must be in the list
    // MODIFIES: this
    // EFFECTS: Removes exercise from exercise list
    public void removeExercise(Exercise e) {
        this.exercises.remove(e);
    }

    // REQUIRES: must not be in session
    // MODIFIES: this
    // EFFECTS: Begins a session by setting inSession to true and first exercise as current.
    public void beginSession() {
        this.inSession = true;
        this.current = this.exercises.get(0);
    }

    // REQUIRES: weight is non-zero positive integer and must be in session
    // MODIFIES: this
    /* EFFECTS: adds weight based on type of equipment
                - if equipment is barbell: calculates total weight based on input plate and bar
                - if machine or dumbbell: sets weight normally
     */
    public void addWeightToCurrent(int weight) {
        if (current.getEquipment() == "Barbell") {
            int total = 2 * weight + WEIGHT_OF_BAR;
            current.setWeight(total);
        } else {
            current.setWeight(weight);
        }
    }

    // REQUIRES: must be in session
    // MODIFIES: this
    /* EFFECTS: Adds 1 current number of sets.
                - If The goal sets are met, move on to the next exercise in routine
                - If final set of workout, sets inSession to false and removes current
     */
    public void addSetToProgress() {
        if (progress == current.getSets() - 1) {
            current.setSetsCompleted(progress + 1);
            progress = 0;
            if (exercises.indexOf(current) == exercises.size() - 1) {
                current = null;
                inSession = false;
            } else {
                int indexOfCurrent = exercises.indexOf(current);
                current = exercises.get(indexOfCurrent + 1);
            }
        } else {
            progress++;
            current.setSetsCompleted(progress);
        }
    }

    // MODIFIES: this
    // EFFECTS: Moves on to the next exercise in routine. Completes workout if none left.
    public void skipExercise() {
        progress = 0;
        if (exercises.indexOf(current) == exercises.size() - 1) {
            current = null;
            inSession = false;
        } else {
            int indexOfCurrent = exercises.indexOf(current);
            current = exercises.get(indexOfCurrent + 1);
        }
    }

    // EFFECTS: Returns true if all the goal sets and reps have been met, false otherwise.
    public boolean hasMetAllGoals() {
        for (Exercise e : exercises) {
            if (!e.hasMetGoal()) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: Returns a string representation of the workout.
    public String viewWorkout() {
        String view = "";
        for (Exercise e : exercises) {
            view += e.viewExercise() + "\n";
        }
        if (hasMetAllGoals()) {
            view += "Met Goal: Yes";
        } else {
            view += "Met Goal: No";
        }

        return view;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public boolean getInSession() {
        return inSession;
    }
    public Exercise getCurrent() {
        return current;
    }

   public int getProgress() {
        return progress;
    }
}
