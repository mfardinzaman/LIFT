package model;

import java.util.ArrayList;

/* A workout routine with its associated workout list, whether the user is currently in session, the current exercise
*  being done, and the current sets progress.
*/
public class Routine {
    private ArrayList<Exercise> exercises;
    private boolean inSession;
    private Exercise current;
    private int progress;

    private static final int WEIGHT_OF_BAR = 45;

    // EFFECTS: initializes a workout routine that is not in session with empty exercise list,
    //          no currently selected exercise, and progress set to 0
    public Routine() {
        exercises = new ArrayList<>();
        inSession = false;
        current = null;
        progress = 0;
    }

    // REQUIRES: Exercise should not be in the current routine.
    // MODIFIES: this
    // EFFECTS: adds an exercise to the exercise list with indicated sets and reps
    public void addExercise(Exercise e, int sets, int reps) {
        e.setSets(sets);
        e.setReps(reps);

        exercises.add(e);
    }

    // REQUIRES: Exercise must be in the list
    // MODIFIES: this
    // EFFECTS: removes exercise from exercise list
    public void removeExercise(Exercise e) {
        exercises.remove(e);
    }

    // EFFECTS: returns the index of exercise of given name in exercises,
    //          returns -1 if not in list
    public int indexOfExercise(String name) {
        for (Exercise e : exercises) {
            if (name.equals(e.getName())) {
                return exercises.indexOf(e);
            }
        }
        return -1;
    }

    // REQUIRES: must not be in session
    // MODIFIES: this
    // EFFECTS: begins a session by setting inSession to true and first exercise as current
    public void beginSession() {
        inSession = true;
        current = exercises.get(0);
    }

    // REQUIRES: must be in session
    // MODIFIES: this
    // EFFECTS: ends a session by setting inSession to false, current as null, and progress as 0
    public void endSession() {
        inSession = false;
        current = null;
        progress = 0;
    }

    // REQUIRES: weight is non-zero positive integer and must be in session
    // MODIFIES: this
    // EFFECTS: adds weight based on type of equipment:
    //          - if equipment is barbell -> calculates total weight based on input plate and bar
    //          - if machine or dumbbell -> sets weight normally
    public void addWeightToCurrent(int weight) {
        if (current.getEquipment().equals("Barbell")) {
            int total = 2 * weight + WEIGHT_OF_BAR;
            current.setWeight(total);
        } else {
            current.setWeight(weight);
        }
    }

    // REQUIRES: must be in session
    // MODIFIES: this
    // EFFECTS: adds 1 to current number of sets
    //          - if The goal sets are met, move on to the next exercise in routine
     //         - if final set of workout, sets inSession to false and removes current
    public void addSetToProgress() {
        if (progress == current.getSets() - 1) {
            current.setSetsCompleted(progress + 1);
            progress = 0;
            if (exercises.indexOf(current) == exercises.size() - 1) {
                endSession();
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
    // EFFECTS: moves on to the next exercise in routine or completes workout if none left
    public void skipExercise() {
        progress = 0;
        if (exercises.indexOf(current) == exercises.size() - 1) {
            endSession();
        } else {
            int indexOfCurrent = exercises.indexOf(current);
            current = exercises.get(indexOfCurrent + 1);
        }
    }

    // EFFECTS: returns true if all the goal sets and reps have been met, false otherwise
    public boolean hasMetAllGoals() {
        for (Exercise e : exercises) {
            if (!e.hasMetGoal()) {
                return false;
            }
        }
        return true;
    }

    // REQUIRES: this.exercises must be not empty
    // EFFECTS: returns a string representation of the workout
    public String viewWorkout() {
        String view = "";
        for (Exercise e : exercises) {
            if (e == current) {
                view += "* ";
            }
            view += e.viewExercise() + "\n";
        }

        view += "Met Goal: ";
        if (hasMetAllGoals()) {
            view += "Yes";
        } else {
            view += "No";
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
