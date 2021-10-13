package ui;

import model.Exercise;
import model.Routine;

import java.util.Scanner;

// Workout tracker application inspired by Teller application
public class WorkoutTracker {
    private Routine routine;
    private Scanner input;

    // EFFECTS: runs the tracker app
    public WorkoutTracker() {
        runTracker();
    }

    // MODIFIES: this
    // EFFECTS: processes user input in the home menu
    private void runTracker() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayHomeMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                if (routine.getInSession()) {
                    routine.endSession();
                }
                keepGoing = false;
            } else {
                processHomeCommand(command);
            }
        }

        if (routine.hasMetAllGoals()) {
            System.out.println("\nYou met all your goals! Have a great day!");
        } else {
            System.out.println("\nRest up and better luck next time!");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes routine
    private void init() {
        routine = new Routine();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: displays home menu of options to user
    private void displayHomeMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tm -> Modify routine");
        System.out.println("\tv -> View routine");
        System.out.println("\tb -> Begin session");
        System.out.println("\tq -> Quit");
        System.out.print("Make your selection: ");
    }

    // EFFECTS: process user command in the home menu
    private void processHomeCommand(String command) {
        if (command.equals("m")) {
            doModify();
        } else if (command.equals("v")) {
            doView();
        } else if (command.equals("b")) {
            doBegin();
        } else {
            System.out.println("\nNot a valid selection");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input in modify menu
    private void doModify() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayModifyMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepGoing = false;
            } else {
                processModifyCommand(command);
            }
        }
    }

    // EFFECTS: displays modify menu of options to user
    private void displayModifyMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Add new exercise");
        System.out.println("\tr -> Remove exercise");
        System.out.println("\tb -> Back");
        System.out.print("Make your selection: ");
    }

    // EFFECTS: process user command in the modify menu
    private void processModifyCommand(String command) {
        if (command.equals("a")) {
            addNewExercise();
        } else if (command.equals("r")) {
            removeExercise();
        } else {
            System.out.println("\nNot a valid selection");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds new exercise to exercises in routine
    private void addNewExercise() {
        System.out.print("\nName: ");
        String name = input.next();
        System.out.print("Equipment: ");
        String equipment = input.next();
        Exercise newExercise = new Exercise(name, equipment);

        System.out.print("Sets: ");
        int sets = input.nextInt();
        System.out.print("Reps: ");
        int reps = input.nextInt();
        routine.addExercise(newExercise, sets, reps);
    }

    // MODIFIES: this
    // EFFECTS: removes exercise already in exercises
    private void removeExercise() {
        if (routine.getExercises().isEmpty()) {
            System.out.println("\nNo exercises in routine!");
        } else {
            System.out.print("\nName: ");
            String name = input.next();
            int indexOfExercise = routine.indexOfExercise(name);

            if (indexOfExercise == -1) {
                System.out.println("Exercise does not exist");
            } else {
                Exercise exerciseToRemove = routine.getExercises().get(indexOfExercise);
                routine.removeExercise(exerciseToRemove);
                System.out.println(name + " removed");
            }
        }
    }

    // EFFECTS: displays workout routine to user
    private void doView() {
        if (routine.getExercises().isEmpty()) {
            System.out.println("\nNo exercises in routine!");
        } else {
            System.out.println(routine.viewWorkout());
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input in begin session menu
    private void doBegin() {
        if (routine.getExercises().isEmpty()) {
            System.out.println("\nNo exercises in routine!");
        } else {
            boolean keepGoing = true;
            String command;
            routine.beginSession();

            while (keepGoing) {
                displayBeginMenu();
                if (routine.getCurrent() == null) {
                    keepGoing = false;
                } else {
                    command = input.next();
                    command = command.toLowerCase();

                    if (command.equals("b")) {
                        keepGoing = false;
                    } else {
                        processBeginCommand(command);
                    }
                }
            }
        }
    }

    // EFFECTS: displays begin session menu of options to user
    private void displayBeginMenu() {
        if (routine.getCurrent() == null) {
            System.out.println("Workout complete!");
            System.out.println(routine.viewWorkout());
        } else {
            System.out.println("\nCurrent exercise: " + routine.getCurrent().viewExercise());
            System.out.println("Select from:");
            System.out.println("\tw -> Set weight of exercise");
            System.out.println("\ta -> Add set");
            System.out.println("\ts -> Skip exercise");
            System.out.println("\tv -> View routine");
            System.out.println("\tb -> Back");
            System.out.print("Make your selection: ");
        }
    }

    // EFFECTS: process user command in the begin session menu
    private void processBeginCommand(String command) {
        if (command.equals("w")) {
            doSetWeight();
        } else if (command.equals("a")) {
            doAddSet();
        } else if (command.equals("s")) {
            doSkipExercise();
        } else if (command.equals("v")) {
            doView();
        } else {
            System.out.println("\nNot a valid selection");
        }
    }

    // MODIFIES: this
    // EFFECTS: sets weight for current exercise
    private void doSetWeight() {
        System.out.print("\nWeight (in lb): ");
        int weight = input.nextInt();
        routine.addWeightToCurrent(weight);
    }

    // MODIFIES: this
    // EFFECTS: adds set to progress
    private void doAddSet() {
        routine.addSetToProgress();
    }

    // MODIFIES: this
    // EFFECTS: skips exercise in routine
    private void doSkipExercise() {
        routine.skipExercise();
    }
}
