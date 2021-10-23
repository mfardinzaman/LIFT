package persistence;

import org.json.JSONObject;

// Inspired by JsonSerialization Demo
public interface Writeable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
