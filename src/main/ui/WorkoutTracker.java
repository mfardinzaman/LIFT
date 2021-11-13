package ui;

import model.Equipment;
import model.Exercise;
import model.Routine;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JList files;

    /*
    EFFECTS: Constructor sets up all panes and buttons
     */
    public WorkoutTracker() {
        super();
        routine = new Routine();
        this.setTitle("Workout Tracker: [" + routine.getName() + "]");

        // TODO: delete - for testing only
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

    /*
    EFFECTS: Initializes JFrame settings (window size, close operation, icon)
     */
    private void initSettings() {
        jsonReader = new JsonReader();
        jsonWriter = new JsonWriter();

        this.setSize(750,750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        ImageIcon image = new ImageIcon(IMG_PATH + "barbel.png");
        setIconImage(image.getImage());
    }

    /*
    EFFECTS: Displays all panes in main window
     */
    private void initView() {
        JPanel routinePanel = routinePanel();
        JTabbedPane viewTools = viewTools();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, routinePanel,viewTools);
        splitPane.setDividerLocation(250);
        add(splitPane);
    }
    
    /*
    EFFECTS: creates panel containing routine and buttons
     */
    private JPanel routinePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(viewRoutine());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout());

        makeAddButton(buttonPanel);
        makeRemoveButton(buttonPanel);

        panel.add(buttonPanel);

        return panel;
    }

    private void makeAddButton(JPanel buttonPanel) {
        JButton addButton = makeButton("Add Exercise", "plus_icon.png");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                RoutineTable model = (RoutineTable) table.getModel();
                Exercise exercise = null;
                try {
                    exercise = getExerciseDetails();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Not a valid exercise","Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                if (exercise != null) {
                    model.insertRow(exercise);
                }
            }
        });
        buttonPanel.add(addButton);
    }

    private Exercise getExerciseDetails() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        GridBagConstraints c = new GridBagConstraints();

        JTextField nameField = new JTextField();
        JComboBox equipmentField = new JComboBox(Equipment.values());
        equipmentField.setSelectedItem(Equipment.NONE);
        JTextField setsField = new JTextField();
        JTextField repsField = new JTextField();

        newExercisePanel(panel, c, 0, nameField, "Exercise name:");
        newExercisePanel(panel, c, 1, equipmentField, "Equipment type:");
        newExercisePanel(panel, c, 2, setsField, "Number of sets:");
        newExercisePanel(panel, c, 3, repsField, "Number of reps:");

        int result = JOptionPane.showConfirmDialog(null, panel,"New Exercise",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            Equipment equipment = (Equipment) equipmentField.getSelectedItem();
            int sets = Integer.valueOf(setsField.getText());
            int reps = Integer.valueOf(repsField.getText());

            return new Exercise(name, equipment, sets, reps);
        } else {
            return null;
        }
    }

    private void newExercisePanel(JPanel panel, GridBagConstraints c, int y, JComponent component, String text) {
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
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RoutineTable model = (RoutineTable) table.getModel();
                model.removeRow(table.getSelectedRow());
                JOptionPane.showMessageDialog(null,
                        "Exercise deleted successfully",
                        "Exercise deleted",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(removeButton);
    }

    /*
    EFFECTS: Creates JTable representing current routine
     */
    private JScrollPane viewRoutine() {
        table = new JTable(new RoutineTable(routine));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setOpaque(true);
        table.setFont(new Font("Arial",Font.PLAIN,16));

        // Inspired by: https://www.codejava.net/java-se/swing/how-to-create-jcombobox-cell-editor-for-jtable
        TableColumn equipment = table.getColumnModel().getColumn(1);
        JComboBox comboBox = new JComboBox(Equipment.values());
        equipment.setCellEditor((new DefaultCellEditor(comboBox)));

        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);

        return new JScrollPane(table);
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
    MODIFIES: tabbedPane
    EFFECTS: adds modify routine pane to tabbed pane
     */
    private void modifyPanel(JTabbedPane tabbedPane) {
        JPanel buttonPanel = modifyButtons();
        files = new JList(getFiles());
        files.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonPanel,files);
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
    MODIFIES: tabbedPane
    EFFECTS: adds buttons to modify routine pane
     */
    private JPanel modifyButtons() {
        JPanel buttonPanel = new JPanel();
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(routine.getName());
        title.setFont(new Font("Arial", Font.BOLD,16));
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0,0,20,0);
        panel.add(title, c);

        addNewButton(buttonPanel, c, title);
        addRenameButton(buttonPanel, c, title);
        addSaveButton(buttonPanel, c);
        addLoadButton(buttonPanel, c, title);

        panel.add(buttonPanel);

        return panel;
    }

    private void addSaveButton(JPanel panel, GridBagConstraints c) {
        JButton saveButton = makeButton("SAVE", "save_icon.png", c, 0, 3);
        saveButton.setToolTipText("Save current routine");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String path = SAVE_PATH + routine.getName().replace(" ", "_") + ".json";
                jsonWriter.setDestination(path);

                try {
                    jsonWriter.open();
                    jsonWriter.write(routine);
                    jsonWriter.close();
                    JOptionPane.showMessageDialog(null,
                            "Saved " + routine.getName() + " to " + path,
                            "Routine saved", JOptionPane.INFORMATION_MESSAGE);

                    files.setListData(getFiles());
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "Unable to write to file: " + path,
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(saveButton, c);
    }

    /*
    MODIFIES: panel
    EFFECTS: creates load routine button and adds to panel
     */
    private void addLoadButton(JPanel panel, GridBagConstraints c, JLabel title) {
        JButton loadButton = makeButton("LOAD", "open_icon.png", c,0,4);
        loadButton.setToolTipText("Load selected routine from right");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String path = SAVE_PATH + (String) files.getSelectedValue();
                jsonReader.setSource(path);

                try {
                    routine = jsonReader.read();
                    RoutineTable model = (RoutineTable) table.getModel();
                    model.setExercises(routine);
                    title.setText(routine.getName());
                    WorkoutTracker.this.setTitle("Workout Tracker: [" + routine.getName() + "]");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Unable to read from file: " + path,
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(loadButton, c);
    }

    /*
    MODIFIES: panel
    EFFECTS: creates new routine button and adds to panel
     */
    private void addNewButton(JPanel panel, GridBagConstraints c, JLabel title) {
        JButton newButton = makeButton("NEW", "new_icon.png", c, 0, 1);
        newButton.setToolTipText("Create new routine");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = (String) JOptionPane.showInputDialog(null, "Exercise name:",
                        "Exercise name",JOptionPane.QUESTION_MESSAGE);

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
    EFFECTS: creates rename routine button and adds to panel
     */
    private void addRenameButton(JPanel panel, GridBagConstraints c, JLabel title) {
        JButton renameButton = makeButton("RENAME", "rename_icon.png", c,0,2);
        renameButton.setToolTipText("Rename current routine");
        renameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = (String) JOptionPane.showInputDialog(null, "Exercise name:",
                        "Exercise name",JOptionPane.QUESTION_MESSAGE);

                routine.setName(name);
                title.setText(name);
                WorkoutTracker.this.setTitle("Workout Tracker: [" + routine.getName() + "]");
            }
        });
        panel.add(renameButton, c);
    }

    /*
    REQUIRES: valid img in resources folder
    EFFECTS: creates new button
     */
    private JButton makeButton(String name, String img) {
        ImageIcon icon = new ImageIcon(IMG_PATH + img);

        JButton button = new JButton(name, icon);
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.TRAILING);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setIconTextGap(10);

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

        c.insets = new Insets(3,3,3,3);
        c.gridx = x;
        c.gridy = y;

        return button;
    }

    /*
    MODIFIES: tabbedPane
    EFFECTS: adds session controls to tabbed pane
     */
    private void sessionPanel(JTabbedPane tabbedPane) {
        JPanel panel = sessionTools();

        ImageIcon icon = new ImageIcon(IMG_PATH + "barbel_icon.png");
        tabbedPane.addTab("Session", icon, panel, "Go to current session");
    }

    private JPanel sessionTools() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JButton weightButton = makeButton("Set Weight", "weight_icon.png", c, 0, 0);
        weightButton.setToolTipText("Set weight for current exercise");
        panel.add(weightButton, c);

        JButton setButton = makeButton("Add set", "complete_icon.png", c, 0, 1);
        setButton.setToolTipText("Complete set for current exercise");
        panel.add(setButton, c);

        JButton skipButton = makeButton("Skip set", "cancel_icon.png", c, 0, 2);
        skipButton.setToolTipText("Skip exercise and move on to next exercise");
        panel.add(skipButton, c);

        return panel;
    }

    /*
    EFFECTS: starts application
     */
    public static void main(String[] args) {
        new WorkoutTracker();
    }
}
