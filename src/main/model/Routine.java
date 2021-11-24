package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.util.ArrayList;

/**
 * A workout routine with its associated workout list, whether the user is currently in session, the current exercise
 * being done, and the current sets progress.
  */
public class Routine implements Writeable {
    private static final int WEIGHT_OF_BAR = 45;

    private String name;
    private ArrayList<Exercise> exercises;
    private boolean inSession;
    private Exercise current;
    private int progress;

    /*
    EFFECTS: initializes a workout routine that is not in session with empty exercise list, no currently selected
             exercise, and progress set to 0
     */
    public Routine() {
        this.name = "Default Routine";
        exercises = new ArrayList<>();
        inSession = false;
        current = null;
        progress = 0;
    }

    /*
    EFFECTS: initializes a workout routine that is not in session with empty exercise list, no currently selected
             exercise, and progress set to 0 with name set to name
     */
    public Routine(String name) {
        this.name = name;
        exercises = new ArrayList<>();
        inSession = false;
        current = null;
        progress = 0;
    }

    /*
    REQUIRES: Exercise should not be in the current routine.
    MODIFIES: this
    EFFECTS: adds an exercise to the exercise list with indicated sets and reps
     */
    public void addExercise(Exercise e, int sets, int reps) {
        e.setSets(sets);
        e.setReps(reps);

        exercises.add(e);

        EventLog.getInstance().logEvent(new Event(e.getName() + " added to " + this.name));
    }

    /*
    REQUIRES: Exercise must be in the list
    MODIFIES: this
    EFFECTS: removes exercise from exercise list
     */
    public void removeExercise(int index) {
        Exercise remExercise = exercises.get(index);

        exercises.remove(index);

        EventLog.getInstance().logEvent(new Event(remExercise.getName() + " removed from " + this.name));
    }

    /*
    REQUIRES: must not be in session
    MODIFIES: this
    EFFECTS: begins a session by setting inSession to true and first exercise as current
     */
    public void beginSession() {
        inSession = true;
        current = exercises.get(0);

        EventLog.getInstance().logEvent(new Event(this.name + "session initiated with " + current.getName()));
    }

    /*
    REQUIRES: must be in session
    MODIFIES: this
    EFFECTS: ends a session by setting inSession to false, current as null, and progress as 0
     */
    public void endSession() {
        inSession = false;
        current = null;
        progress = 0;

        EventLog.getInstance().logEvent(new Event(this.name + "session terminated"));
    }

    /*
    REQUIRES: weight is non-zero positive integer and must be in session
    MODIFIES: this
    EFFECTS: adds weight based on type of equipment:
             - if equipment is barbell -> calculates total weight based on input plate and bar
             - if machine or dumbbell -> sets weight normally
     */
    public void addWeightToCurrent(int weight) {
        if (current.getEquipment() == Equipment.BARBELL) {
            int total = 2 * weight + WEIGHT_OF_BAR;
            current.setWeight(total);
        } else {
            current.setWeight(weight);
        }
    }

    /*
    REQUIRES: must be in session
    MODIFIES: this
    EFFECTS: adds 1 to current number of sets
             - if The goal sets are met, move on to the next exercise in routine
             - if final set of workout, sets inSession to false and removes current
     */
    public void addSetToProgress() {
        EventLog.getInstance().logEvent(new Event(
                "One set of " + current.getName() + " completed in " + this.name));

        if (progress == current.getSets() - 1) {
            current.setSetsCompleted(progress + 1);
            progress = 0;
            if (exercises.indexOf(current) == exercises.size() - 1) {
                EventLog.getInstance().logEvent(new Event("All exercises in " + this.name + " completed"));
                endSession();
            } else {
                int indexOfCurrent = exercises.indexOf(current);
                Exercise prev = current;
                current = exercises.get(indexOfCurrent + 1);
                EventLog.getInstance().logEvent(new Event(
                        prev.getName() + " completed, " + current.getName() + " begun"));
            }
        } else {
            progress++;
            current.setSetsCompleted(progress);
        }
    }

    /*
    MODIFIES: this
    EFFECTS: moves on to the next exercise in routine or completes workout if none left
     */
    public void skipExercise() {
        EventLog.getInstance().logEvent(new Event(current.getName() + " skipped"));

        progress = 0;
        if (exercises.indexOf(current) == exercises.size() - 1) {
            EventLog.getInstance().logEvent(new Event("All exercises in " + this.name + " completed"));
            endSession();
        } else {
            int indexOfCurrent = exercises.indexOf(current);
            current = exercises.get(indexOfCurrent + 1);
        }
    }

    /*
    EFFECTS: returns true if all the goal sets and reps have been met, false otherwise
     */
    public boolean hasMetAllGoals() {
        for (Exercise e : exercises) {
            if (!e.hasMetGoal()) {
                return false;
            }
        }
        return true;
    }

    /*
    REQUIRES: this.exercises must be not empty
    EFFECTS: returns a string representation of the workout
     */
    public String viewWorkout() {
        StringBuilder view = new StringBuilder();
        view.append(generateHeader());

        int num = 1;
        for (Exercise e : exercises) {
            StringBuilder exercise = new StringBuilder(e.viewExercise());
            exercise.replace(6,7,String.valueOf(num));
            if (e == current) {
                exercise.replace(2,3, "*");
            }
            view.append(exercise + "\n");
            num++;
        }

        view.append(new String(new char[55]).replace("\0", "=") + "\n");
        view.append("Met Goal: ");
        if (hasMetAllGoals()) {
            view.append("Yes");
        } else {
            view.append("No");
        }

        return view.toString();
    }

    /*
    EFFECTS: generates header for Routine table
     */
    private String generateHeader() {
        return getName() + "\n"
                + "|Cur|Num|"
                + String.format("%-25s|%-4s|%-4s|%-5s|%-3s|",
                " Name",
                "Sets",
                "Reps",
                " Wgt ",
                "Prg") + "\n"
                + new String(new char[55]).replace("\0", "=") + "\n";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String prev = this.name;

        this.name = name;

        EventLog.getInstance().logEvent(new Event("Routine name changed from " + prev + " to " + name));
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

    @Override
    /*
    EFFECTS: returns this as a JSON object
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("exercises", exercisesToJson());

        return json;
    }

    /*
    EFFECTS: returns exercises in this routine as a JSON array
     */
    private JSONArray exercisesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Exercise e : exercises) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }
}
