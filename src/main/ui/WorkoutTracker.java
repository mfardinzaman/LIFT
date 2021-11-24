package ui;

import model.Equipment;
import model.Exercise;
import model.Routine;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Workout Tracker and Routine Manager GUI Application
 * All icons by Freepik: https://www.flaticon.com/authors/freepik
  */
public class WorkoutTracker extends JFrame {
    private static final String SAVE_PATH = "./data/";
    private static final String IMG_PATH = "./resources/";

    private Routine routine;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JTable table;
    private JList<String> files;

    /*
    EFFECTS: Constructor sets up all panes and buttons
     */
    public WorkoutTracker() {
        super();
        routine = new Routine();
        this.setTitle("Workout Tracker: [" + routine.getName() + "]");

        initSettings();
        initView();

        setVisible(true);
    }

    /*
    EFFECTS: Initializes JFrame settings (window size, close operation, icon)
     */
    private void initSettings() {
        jsonReader = new JsonReader();
        jsonWriter = new JsonWriter();

        this.setSize(500,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        ImageIcon image = new ImageIcon(IMG_PATH + "barbel.png");
        setIconImage(image.getImage());
    }

    /*
    EFFECTS: Displays an error dialog box with indicate error message
     */
    private void errorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "ERROR",
                JOptionPane.ERROR_MESSAGE);
    }

    /*
    REQUIRES: valid img in resources folder
    EFFECTS: creates new generic button
     */
    private JButton makeButton(String name, String img) {
        ImageIcon icon = new ImageIcon(IMG_PATH + img);

        JButton button = new JButton(name, icon);
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.TRAILING);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setIconTextGap(10);
        button.setFocusable(false);

        return button;
    }

    /*
    REQUIRES: valid img in resources folder
    EFFECTS: creates new button for GridBagLayout
     */
    private JButton makeButton(String name, String img, GridBagConstraints c, int x, int y) {
        ImageIcon icon = new ImageIcon(IMG_PATH + img);

        JButton button = new JButton(name, icon);
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.TRAILING);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);
        button.setFocusable(false);

        c.insets = new Insets(3,3,3,3);
        c.gridx = x;
        c.gridy = y;

        return button;
    }

    /*
    MODIFIES: this
    EFFECTS: Displays all panes in main window
     */
    private void initView() {
        JPanel routinePanel = routinePanel();
        JTabbedPane viewTools = viewTools();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, routinePanel,viewTools);
        splitPane.setDividerLocation(200);
        add(splitPane);
    }
    
    /*
    EFFECTS: creates panel containing routine and buttons
     */
    private JPanel routinePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(viewRoutine());

        JPanel buttonPanel = new JPanel(new GridLayout());

        makeAddButton(buttonPanel);
        makeRemoveButton(buttonPanel);

        panel.add(buttonPanel);

        return panel;
    }

    /*
    MODIFIES: this
    EFFECTS: Creates JTable representing current routine
     */
    private JScrollPane viewRoutine() {
        createTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setOpaque(true);

        // Inspired by: https://www.codejava.net/java-se/swing/how-to-create-jcombobox-cell-editor-for-jtable
        TableColumn equipment = table.getColumnModel().getColumn(1);
        JComboBox<Equipment> comboBox = new JComboBox<>(Equipment.values());
        equipment.setCellEditor((new DefaultCellEditor(comboBox)));

        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);

        return new JScrollPane(table);
    }

    /*
    MODIFIES: this
    EFFECTS: creates JTable with custom renderer to display current exercise
    Inspired by: https://www.youtube.com/watch?v=iMBfneE2Ztg
     */
    private void createTable() {
        RoutineTable model = new RoutineTable(routine);
        table = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Exercise rowExercise = model.getExercises().get(row);
                Component c = super.prepareRenderer(r, row, col);

                if (rowExercise.equals(routine.getCurrent()) && isCellSelected(row, col)) {
                    c.setBackground(new Color(142, 219, 192));
                } else if (rowExercise.equals(routine.getCurrent())) {
                    c.setBackground(new Color(160, 219, 142));
                } else if (isCellSelected(row, col)) {
                    c.setBackground(new Color(184, 207, 229));
                } else {
                    c.setBackground(Color.WHITE);
                }

                return c;
            }
        };
    }

    /*
    MODIFIES: buttonPanel
    EFFECTS: creates add exercise button and adds to panel
     */
    private void makeAddButton(JPanel buttonPanel) {
        JButton addButton = makeButton("Add Exercise", "plus_icon.png");
        addButton.addActionListener(ae -> {
            RoutineTable model = (RoutineTable) table.getModel();
            Exercise exercise = null;
            try {
                exercise = getExerciseDetails();
            } catch (Exception e) {
                errorDialog("Not a valid exercise");
            }
            if (exercise != null) {
                model.insertRow(exercise);
            }
        });
        buttonPanel.add(addButton);
    }

    /*
    EFFECTS: creates new Exercise based on input from user and returns it
     */
    private Exercise getExerciseDetails() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        GridBagConstraints c = new GridBagConstraints();

        JTextField nameField = new JTextField();
        JComboBox<Equipment> equipmentField = new JComboBox<>(Equipment.values());
        equipmentField.setSelectedItem(Equipment.NONE);
        JTextField setsField = new JTextField();
        JTextField repsField = new JTextField();

        newInputArea(panel, c, 0, nameField, "Exercise name:");
        newInputArea(panel, c, 1, equipmentField, "Equipment type:");
        newInputArea(panel, c, 2, setsField, "Number of sets:");
        newInputArea(panel, c, 3, repsField, "Number of reps:");

        int result = JOptionPane.showConfirmDialog(null, panel,"New Exercise",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            Equipment equipment = (Equipment) equipmentField.getSelectedItem();
            int sets = Integer.parseInt(setsField.getText());
            int reps = Integer.parseInt(repsField.getText());

            return new Exercise(name, equipment, sets, reps);
        } else {
            return null;
        }
    }

    /*
    MODIFIES: panel
    EFFECTS: creates input area for multi-input dialog
     */
    private void newInputArea(JPanel panel, GridBagConstraints c, int y, JComponent component, String text) {
        c.gridx = 0;
        c.gridy = y;
        panel.add(new JLabel(text));

        c.gridx = 1;
        c.gridy = y;
        c.gridwidth = GridBagConstraints.REMAINDER;
        component.setPreferredSize(new Dimension(100, 25));
        panel.add(component, c);
    }

    /*
    MODIFIES: buttonPanel
    EFFECTS: creates a button to remove an exercise from the table
    Inspired by: https://www.tutorialspoint.com/how-can-we-remove-a-selected-row-from-a-jtable-in-java
     */
    private void makeRemoveButton(JPanel buttonPanel) {
        JButton removeButton = makeButton("Remove Exercise", "minus_icon.png");
        removeButton.addActionListener(ae -> {
            try {
                RoutineTable model = (RoutineTable) table.getModel();
                model.removeRow(table.getSelectedRow());
                JOptionPane.showMessageDialog(null,
                        "Exercise deleted successfully",
                        "Exercise Deleted",
                        JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e) {
                errorDialog("No exercise selected");
            } catch (ArrayIndexOutOfBoundsException e) {
                errorDialog("No exercises in routine");
            }
        });
        buttonPanel.add(removeButton);
    }

    /*
    EFFECTS: Adds tools and buttons to bottom pane
     */
    private JTabbedPane viewTools() {
        JTabbedPane tabbedPane = new JTabbedPane();

        modifyPanel(tabbedPane);
        sessionPanel(tabbedPane);

        return tabbedPane;
    }

    /*
    MODIFIES: this, tabbedPane
    EFFECTS: adds modify routine pane to tabbed pane
     */
    private void modifyPanel(JTabbedPane tabbedPane) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(routine.getName());
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0,0,20,0);
        panel.add(title, c);

        addNewButton(panel, c, title);
        addRenameButton(panel, c, title);
        addSaveButton(panel, c);
        addLoadButton(panel, c, title);

        files = new JList<>(getFiles());
        files.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel,files);
        splitPane.setDividerLocation(200);

        ImageIcon tabIcon = new ImageIcon(IMG_PATH + "setting_icon.png");
        tabbedPane.addTab("Modify", tabIcon, splitPane, "Modify current routine");
    }

    /*
    EFFECTS: retrieves files in ./data/ folder
     */
    private String[] getFiles() {
        String[] fileNames;

        File f = new File(SAVE_PATH);
        fileNames = f.list();

        return filterJson(fileNames);
    }

    /*
    EFFECTS: filters fileNames for only *.json
     */
    private String[] filterJson(String[] fileNames) {
        ArrayList<String> filterFileNames = new ArrayList<>();
        for (String fileName : fileNames) {
            if (fileName.endsWith(".json")) {
                filterFileNames.add(fileName);
            }
        }

        return filterFileNames.toArray(new String[0]);
    }

    /*
    MODIFIES: this, panel
    EFFECTS: creates button to start new routine and adds to panel
     */
    private void addNewButton(JPanel panel, GridBagConstraints c, JLabel title) {
        JButton newButton = makeButton("NEW", "new_icon.png", c, 0, 1);
        newButton.setToolTipText("Create new routine");
        newButton.addActionListener(ae -> {
            String name = JOptionPane.showInputDialog(null, "Routine name:",
                    "New Routine",JOptionPane.QUESTION_MESSAGE);

            if (name != null) {
                routine = new Routine(name);
                RoutineTable model = (RoutineTable) table.getModel();
                model.setExercises(routine);
                title.setText(name);
                WorkoutTracker.this.setTitle("Workout Tracker: [" + routine.getName() + "]");
            }
        });
        panel.add(newButton, c);
    }

    /*
    MODIFIES: panel
    EFFECTS: creates button to rename routine and adds to panel
     */
    private void addRenameButton(JPanel panel, GridBagConstraints c, JLabel title) {
        JButton renameButton = makeButton("RENAME", "rename_icon.png", c,0,2);
        renameButton.setToolTipText("Rename current routine");
        renameButton.addActionListener(ae -> {
            String name = JOptionPane.showInputDialog(null, "Routine name:",
                    "Rename Routine",JOptionPane.QUESTION_MESSAGE);

            if (name != null) {
                routine.setName(name);
                title.setText(name);
                WorkoutTracker.this.setTitle("Workout Tracker: [" + routine.getName() + "]");
            }
        });
        panel.add(renameButton, c);
    }

    /*
    MODIFIES: panel
    EFFECTS: creates button to save current routine and adds to panel
     */
    private void addSaveButton(JPanel panel, GridBagConstraints c) {
        JButton saveButton = makeButton("SAVE", "save_icon.png", c, 0, 3);
        saveButton.setToolTipText("Save current routine");
        saveButton.addActionListener(ae -> {
            String path = SAVE_PATH + routine.getName().replace(" ", "_") + ".json";
            jsonWriter.setDestination(path);

            try {
                jsonWriter.open();
                jsonWriter.write(routine);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null,
                        "Saved " + routine.getName() + " to " + path,
                        "Routine Saved", JOptionPane.INFORMATION_MESSAGE);

                files.setListData(getFiles());
            } catch (FileNotFoundException e) {
                errorDialog("Unable to write to file: " + path);
            }
        });
        panel.add(saveButton, c);
    }

    /*
    MODIFIES: panel
    EFFECTS: creates button to load routine and adds to panel
     */
    private void addLoadButton(JPanel panel, GridBagConstraints c, JLabel title) {
        JButton loadButton = makeButton("LOAD", "open_icon.png", c,0,4);
        loadButton.setToolTipText("Load selected routine from right");
        loadButton.addActionListener(ae -> {
            String path = SAVE_PATH + files.getSelectedValue();
            jsonReader.setSource(path);

            try {
                routine = jsonReader.read();
                RoutineTable model = (RoutineTable) table.getModel();
                model.setExercises(routine);
                title.setText(routine.getName());
                WorkoutTracker.this.setTitle("Workout Tracker: [" + routine.getName() + "]");
            } catch (IOException e) {
                errorDialog("Unable to read from file: " + path);
            }
        });
        panel.add(loadButton, c);
    }

    /*
    MODIFIES: tabbedPane
    EFFECTS: adds session controls to tabbed pane
     */
    private void sessionPanel(JTabbedPane tabbedPane) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        addBeginButton(panel, c);
        addWeightButton(panel, c);
        addSetButton(panel, c);
        addSkipButton(panel, c);

        ImageIcon icon = new ImageIcon(IMG_PATH + "barbel_icon.png");
        tabbedPane.addTab("Session", icon, panel, "Go to current session");
    }

    /*
    MODIFIES: panel
    EFFECTS: creates button to begin session and adds to panel
     */
    private void addBeginButton(JPanel panel, GridBagConstraints c) {
        JButton beginButton = makeButton("Begin session", "begin_icon.png", c, 0, 0);
        beginButton.setToolTipText("Begin a session");
        beginButton.addActionListener(ae -> {
            if (routine.getExercises().isEmpty()) {
                errorDialog("No exercises in routine");
            } else {
                routine.beginSession();
                table.repaint();
            }
        });
        panel.add(beginButton, c);
    }

    /*
    MODIFIES: panel
    EFFECTS: creates button to add weight to current exercise and adds to panel
     */
    private void addWeightButton(JPanel panel, GridBagConstraints c) {
        JButton weightButton = makeButton("Set weight", "weight_icon.png", c, 0, 1);
        weightButton.setToolTipText("Set weight for current exercise");
        weightButton.addActionListener(ae -> {
            if (routine.getExercises().isEmpty()) {
                errorDialog("No exercises in routine");
            } else if (routine.getCurrent() == null) {
                errorDialog("Session has not been started");
            } else {
                try {
                    String weight = JOptionPane.showInputDialog(null,
                                "NOTE: Input plate weight on one side for barbell exercises\nWeight (in lb):",
                                "Set Weight", JOptionPane.QUESTION_MESSAGE);

                    if (weight != null) {
                        routine.addWeightToCurrent(Integer.parseInt(weight));
                        table.repaint();
                    }
                } catch (Exception e) {
                    errorDialog("Not a valid weight");
                }
            }
        });
        panel.add(weightButton, c);
    }

    /*
    MODIFIES: panel
    EFFECTS: creates button to add set to current exercise and adds to panel
     */
    private void addSetButton(JPanel panel, GridBagConstraints c) {
        JButton setButton = makeButton("Add set", "complete_icon.png", c, 0, 2);
        setButton.setToolTipText("Complete set for current exercise");
        setButton.addActionListener(ae -> {
            if (routine.getExercises().isEmpty()) {
                errorDialog("No exercises in routine");
            } else if (routine.getCurrent() == null) {
                errorDialog("Session has not been started");
            } else {
                routine.addSetToProgress();
                table.repaint();
                if (routine.getCurrent() == null) {
                    goalsDialog();
                }
            }
        });
        panel.add(setButton, c);
    }

    /*
    MODIFIES: panel
    EFFECTS: creates button to skip set and adds to panel
     */
    private void addSkipButton(JPanel panel, GridBagConstraints c) {
        JButton skipButton = makeButton("Skip set", "cancel_icon.png", c, 0, 3);
        skipButton.setToolTipText("Skip exercise and move on to next exercise");
        skipButton.addActionListener(ae -> {
            if (routine.getExercises().isEmpty()) {
                errorDialog("No exercises in routine");
            } else if (routine.getCurrent() == null) {
                errorDialog("Session has not been started");
            } else {
                routine.skipExercise();
                table.repaint();
                if (routine.getCurrent() == null) {
                    goalsDialog();
                }
            }
        });
        panel.add(skipButton, c);
    }

    /*
    EFFECTS: displays dialog message indicating goal status after session
     */
    private void goalsDialog() {
        String message;

        if (routine.hasMetAllGoals()) {
            message = "You met all your goals!";
        } else {
            message = "You didn't meet your goals.\nRest up and better luck next time!";
        }

        JOptionPane.showMessageDialog(null, message, "Session Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /*
    EFFECTS: starts application
     */
    public static void main(String[] args) {
        new WorkoutTracker();
    }
}
