package ui;

import model.Equipment;
import model.Exercise;
import model.Routine;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;

/* Workout tracker application
   Inspired by TellerApp
 */
public class WorkoutTracker {
    private static final String DEFAULT_PATH = "./data/";

    private Routine routine;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

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
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        routine = new Routine();
        jsonWriter = new JsonWriter();
        jsonReader = new JsonReader();
    }

    // EFFECTS: displays home menu of options to user
    private void displayHomeMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tm -> Modify routine");
        System.out.println("\tv -> View routine");
        System.out.println("\ts -> Go to session");
        System.out.println("\tq -> Quit");
        System.out.print("Make your selection: ");
    }

    // EFFECTS: process user command in the home menu
    private void processHomeCommand(String command) {
        switch (command) {
            case "m":
                doModify();
                break;
            case "v":
                doView();
                break;
            case "s":
                doSession();
                break;
            default:
                System.out.println("\nNot a valid selection");
                break;
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
        System.out.println("\tn -> New routine");
        System.out.println("\tl -> Load routine");
        System.out.println("\ts -> Save routine");
        System.out.println("\tc -> Change routine name");
        System.out.println("\ta -> Add new exercise");
        System.out.println("\tr -> Remove exercise");
        System.out.println("\tb -> Back");
        System.out.print("Make your selection: ");
    }

    // EFFECTS: process user command in the modify menu
    private void processModifyCommand(String command) {
        switch (command) {
            case "n":
                newRoutine();
                break;
            case "l":
                loadRoutine();
                break;
            case "s":
                saveRoutine();
                break;
            case "c":
                changeName();
                break;
            case "a":
                addNewExercise();
                break;
            case "r":
                removeExercise();
                break;
            default:
                System.out.println("\nNot a valid selection");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new empty routine with the input name
    private void newRoutine() {
        System.out.print("\nRoutine Name: ");
        String name = input.next();

        routine = new Routine();
        routine.setName(name);
    }

    // MODIFIES: this
    // EFFECTS: displays available routines and loads routine from file
    private void loadRoutine() {
        ArrayList<String> fileNames = getFiles();
        int index = 1;
        System.out.println("\nAvailable Routines:");
        for (String fileName : fileNames) {
            System.out.println(index + " -> " + fileName);
            index++;
        }
        System.out.print("Choose file number to load: ");

        int choice = input.nextInt() - 1;
        String path = DEFAULT_PATH + fileNames.get(choice);
        jsonReader.setSource(path);

        try {
            routine = jsonReader.read();
            System.out.println("Loaded " + routine.getName() + " from " + path);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + path);
        }
    }

    // EFFECTS: retrieves files in ./data/ folder
    private ArrayList<String> getFiles() {
        String[] fileNames;

        File f = new File(DEFAULT_PATH);
        fileNames = f.list();

        return filterJson(fileNames);
    }

    // EFFECTS: filters fileNames for only *.json
    private ArrayList<String> filterJson(String[] fileNames) {
        ArrayList<String> filterFileNames = new ArrayList<>();
        for (String fileName : fileNames) {
            if (fileName.endsWith(".json")) {
                filterFileNames.add(fileName);
            }
        }

        return filterFileNames;
    }

    // EFFECTS: saves routine to file
    private void saveRoutine() {
        String path = DEFAULT_PATH + routine.getName().replace(" ", "_") + ".json";
        jsonWriter.setDestination(path);

        try {
            jsonWriter.open();
            jsonWriter.write(routine);
            jsonWriter.close();
            System.out.println("Saved " + routine.getName() + " to " + path);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + path);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets name of current routine
    private void changeName() {
        System.out.println("\nCurrent Name: " + routine.getName());
        System.out.print("New Name (press b to cancel): ");
        String name = input.next();

        if (!name.equals("b")) {
            routine.setName(name);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds new exercise to exercises in routine
    private void addNewExercise() {
        System.out.print("\nExercise Name: ");
        String name = input.next();

        System.out.println("Select from:");
        System.out.println("\tb -> Barbell");
        System.out.println("\td -> Dumbbell");
        System.out.println("\tm -> Machine");
        System.out.println("\tn -> None");
        System.out.print("Equipment: ");
        Equipment equipment = convertToEquipment(input.next().toLowerCase());
        if (equipment == null) {
            System.out.println("\nNot a valid selection");
        } else {
            Exercise newExercise = new Exercise(name, equipment);

            System.out.print("Sets: ");
            int sets = input.nextInt();
            System.out.print("Reps: ");
            int reps = input.nextInt();
            routine.addExercise(newExercise, sets, reps);
        }
    }

    // EFFECTS: converts string input to Equipment
    private Equipment convertToEquipment(String equipment) {
        switch (equipment) {
            case "b":
                return Equipment.Barbell;
            case "d":
                return Equipment.Dumbbell;
            case "m":
                return  Equipment.Machine;
            case "n":
                return Equipment.None;
            default:
                return null;
        }
    }

    // MODIFIES: this
    // EFFECTS: removes exercise already in exercises
    private void removeExercise() {
        if (routine.getExercises().isEmpty()) {
            System.out.println("\nNo exercises in routine!");
        } else {
            System.out.println("\n" + routine.viewWorkout());
            System.out.print("Exercise Number: ");
            int index = input.nextInt() - 1;
            try {
                String name = routine.getExercises().get(index).getName();
                routine.removeExercise(index);
                System.out.println(name + " removed");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Not a valid selection");
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
    private void doSession() {
        if (routine.getExercises().isEmpty()) {
            System.out.println("\nNo exercises in routine!");
        } else {
            boolean keepGoing = true;
            String command;

            while (keepGoing) {
                if (!routine.getInSession()) {
                    routine.beginSession();
                }
                displaySessionMenu();
                command = input.next();
                command = command.toLowerCase();

                if (command.equals("b")) {
                    keepGoing = false;
                } else {
                    processSessionCommand(command);
                    if (!routine.getInSession()) {
                        System.out.println("Workout complete!");
                        keepGoing = false;
                    }
                }
            }
        }
    }

    // EFFECTS: displays begin session menu of options to user
    private void displaySessionMenu() {
        System.out.println("\n" + routine.viewWorkout());
        System.out.println("Select from:");
        System.out.println("\tw -> Set weight of exercise");
        System.out.println("\ta -> Add set");
        System.out.println("\ts -> Skip exercise");
        System.out.println("\tv -> View routine");
        System.out.println("\tb -> Back");
        System.out.print("Make your selection: ");
    }

    // EFFECTS: process user command in the begin session menu
    private void processSessionCommand(String command) {
        switch (command) {
            case "w":
                doSetWeight();
                break;
            case "a":
                doAddSet();
                break;
            case "s":
                doSkipExercise();
                break;
            case "v":
                doView();
                break;
            default:
                System.out.println("\nNot a valid selection");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: sets weight for current exercise
    private void doSetWeight() {
        System.out.println("\nNOTE: Input plate weight on one side for barbell exercises");
        System.out.print("Weight (in lb): ");
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
