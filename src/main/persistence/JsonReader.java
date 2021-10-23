package persistence;

import model.Equipment;
import model.Exercise;
import model.Routine;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads routine from JSON data stored in file
// Inspired by JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Routine from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Routine read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRoutine(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses routine from JSON object and returns it
    private Routine parseRoutine(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Routine r = new Routine(name);
        addExercises(r, jsonObject);
        return r;
    }

    // MODIFIES: r
    // EFFECTS: parses thingies from JSON object and adds them to routine
    private void addExercises(Routine r, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("exercises");
        for (Object json : jsonArray) {
            JSONObject nextExercise = (JSONObject) json;
            addExercise(r, nextExercise);
        }
    }

    // MODIFIES: r
    // EFFECTS: parses thingy from JSON object and adds it to routine
    private void addExercise(Routine r, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Equipment equipment = Equipment.valueOf(jsonObject.getString("equipment"));
        int sets = jsonObject.getInt("sets");
        int reps = jsonObject.getInt("reps");

        Exercise exercise = new Exercise(name, equipment);

        r.addExercise(exercise, sets, reps);
    }
}

