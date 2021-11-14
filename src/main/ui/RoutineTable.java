package ui;

import com.sun.jmx.remote.internal.RMIExporter;
import model.Equipment;
import model.Exercise;
import model.Routine;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Custom TableModel for Routine viewer
 * Inspired by: https://www.codejava.net/java-se/swing/how-to-create-jcombobox-cell-editor-for-jtable
 *              https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TableDemoProject/src/components/TableDemo.java
  */
public class RoutineTable extends AbstractTableModel {
    private String[] columnNames;
    private ArrayList<Exercise> exercises;

    // EFFECTS: sets column headers and exercises to use in table
    public RoutineTable(Routine routine) {
        super();
        this.columnNames = new String[] {
                "Name",
                "Equipment",
                "Sets",
                "Reps",
                "Weight",
                "Progress"
        };
        this.exercises = routine.getExercises();
    }

    public void setExercises(Routine routine) {
        this.exercises = routine.getExercises();
        fireTableDataChanged();
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    /*
    EFFECTS: returns number of rows
     */
    @Override
    public int getRowCount() {
        return exercises.size();
    }

    /*
    EFFECTS: returns number of columns
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /*
    REQUIRES: col in [0, columnNames.length)
    EFFECTS: returns name of column header
     */
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /*
    REQUIRES: value of type String, Equipment, or int
              row in [0, exercises.size())
              col in [0, columnNames.length)
    MODIFIES: this
    EFFECTS: sets value at specified cell (row, col)
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        Exercise exercise = exercises.get(row);

        switch (col) {
            case 0:
                exercise.setName((String) value);
                break;
            case 1:
                exercise.setEquipment((Equipment) value);
                break;
            case 2:
                exercise.setSets((int) value);
                break;
            case 3:
                exercise.setReps(((int) value));
                break;
            case 4:
                exercise.setWeight((int) value);
                break;
            case 5:
                exercise.setSetsCompleted((int) value);
                break;
        }

        fireTableCellUpdated(row, col);
    }

    /*
    REQUIRES: row in [0, exercises.size())
              col in [0, columnNames.length)
    EFFECTS: returns value at indicated cell
     */
    @Override
    public Object getValueAt(int row, int col) {
        Object returnValue = null;
        Exercise exercise = exercises.get(row);

        switch (col) {
            case 0:
                returnValue = exercise.getName();
                break;
            case 1:
                returnValue = exercise.getEquipment();
                break;
            case 2:
                returnValue = exercise.getSets();
                break;
            case 3:
                returnValue = exercise.getReps();
                break;
            case 4:
                returnValue = exercise.getWeight();
                break;
            case 5:
                returnValue = exercise.getSetsCompleted();
                break;
        }

        return returnValue;
    }

    /*
    REQUIRES: column in [0, columnNames.length)
    EFFECTS: returns Class of indicated column
     */
    @Override
    public Class<?> getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }

    /*
    REQUIRES: row in [0, exercises.size())
              col in [0, columnNames.length)
    EFFECTS: sets progress and weight columns to uneditable
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return col < 4;
    }

    /*
    REQUIRES: exercises list must not be empty
    MODIFIES: this
    EFFECTS: removes the selected exercise
     */
    public void removeRow(int row) {
        exercises.remove(row);
        fireTableRowsDeleted(row, row);
    }

    /*
    MODIFIES: this
    EFFECTS: adds new exercise to end of row
     */
    public void insertRow(Exercise exercise) {
        exercises.add(exercise);
        fireTableRowsDeleted(exercises.size() - 1, exercises.size() - 1);
    }
}
