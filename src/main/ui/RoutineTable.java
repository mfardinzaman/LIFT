package ui;

import model.Equipment;
import model.Exercise;
import model.Routine;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class RoutineTable extends AbstractTableModel {
    private String[] columnNames;
    private ArrayList<Exercise> exercises;

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

    @Override
    public int getRowCount() {
        return exercises.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

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

    @Override
    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col < 5;
    }
}
