package persistence;

import model.Event;
import model.EventLog;
import model.Routine;
import org.json.JSONObject;

import java.io.*;

/**
 * Represents a writer that writes JSON representation of routine to file
 * Inspired by JsonSerializationDemo
 */
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    /*
    EFFECTS: constructs writer to write to destination file
     */
    public JsonWriter() {
    }

    /*
    MODIFIES: this
    EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
             be opened for writing
     */
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    /*
    MODIFIES: this
    EFFECTS: writes JSON representation of routine to file
     */
    public void write(Routine r) {
        JSONObject json = r.toJson();
        saveToFile(json.toString(TAB));

        EventLog.getInstance().logEvent(new Event("Wrote to file " + destination));
    }

    /*
    MODIFIES: this
    EFFECTS: closes writer
     */
    public void close() {
        writer.close();
    }

    /*
    MODIFIES: this
    EFFECTS: writes string to file
     */
    private void saveToFile(String json) {
        writer.print(json);
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
