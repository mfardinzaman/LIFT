package ui;

import model.Equipment;
import model.Exercise;
import model.Routine;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

// Workout Tracker and Routine Manager GUI Application
public class WorkoutTracker extends JFrame {
    private Routine routine;

    // EFFECTS: Constructor sets up all panes and buttons
    public WorkoutTracker() {
        super();
        routine = new Routine();
        this.setTitle("Workout Tracker: [" + routine.getName() + "]");

        Exercise bentOverBarbellRow = new Exercise("Bent Over Barbell Row", Equipment.BARBELL);
        Exercise barbellBenchPress = new Exercise("Barbell Bench Press", Equipment.BARBELL);
        Exercise barbellSquat = new Exercise("Barbell Squat", Equipment.BARBELL);
        Exercise bicepCurl = new Exercise("Bicep Curl", Equipment.DUMBBELL);

        routine.addExercise(bentOverBarbellRow, 3, 5);
        routine.addExercise(barbellBenchPress, 3, 5);
        routine.addExercise(barbellSquat, 3, 5);
        routine.addExercise(bicepCurl, 5, 10);

        initSettings();
        initView();

        setVisible(true);
    }

    // EFFECTS: Initializes JFrame settings (window size, close operation, icon)
    private void initSettings() {
        this.setSize(750,1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Icons made by Good Ware: https://www.flaticon.com/authors/good-ware
        ImageIcon image = new ImageIcon("./resources/barbel.png");
        setIconImage(image.getImage());
    }

    // EFFECTS: Displays all panes in main window
    private void initView() {
        JPanel persistencePanel = new JPanel(new BorderLayout());
        persistencePanel.setBounds(0,0,750,10);
        add(persistencePanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBounds(0,10,750,990);
        add(mainPanel);

        JScrollPane viewRoutine = viewRoutine();
        JScrollPane viewTools = viewTools();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, viewRoutine,viewTools);
        splitPane.setDividerLocation(250);
        mainPanel.add(splitPane);
    }

    // EFFECTS: Creates JTable representing current routine
    private JScrollPane viewRoutine() {
        JTable table = new JTable(new RoutineTable(routine));
        makeEquipmentComboBox(table);
        table.setFillsViewportHeight(true);
        table.setOpaque(true);

        return new JScrollPane(table);
    }

    // EFFECTS: Creates combo box to edit Equipment column
    private void makeEquipmentComboBox(JTable table) {
        TableColumn equipment = table.getColumnModel().getColumn(1);

        JComboBox comboBox = new JComboBox();
        comboBox.addItem(Equipment.BARBELL);
        comboBox.addItem(Equipment.DUMBBELL);
        comboBox.addItem(Equipment.MACHINE);
        comboBox.addItem(Equipment.NONE);

        equipment.setCellEditor((new DefaultCellEditor(comboBox)));
    }

    // EFFECTS: Adds tools and buttons to bottom pane
    private JScrollPane viewTools() {
        return new JScrollPane();
    }

    // EFFECTS: starts application
    public static void main(String[] args) {
        new WorkoutTracker();
    }
}
