package ui;

import model.Equipment;
import model.Exercise;
import model.Routine;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
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

    /*
    EFFECTS: Constructor sets up all panes and buttons
     */
    public WorkoutTracker() {
        super();
        routine = new Routine();
        this.setTitle("Workout Tracker: [" + routine.getName() + "]");

        // TODO: for testing only
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
        JScrollPane viewRoutine = viewRoutine();
        JScrollPane viewTools = viewTools();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, viewRoutine,viewTools);
        splitPane.setDividerLocation(250);
        add(splitPane);
    }

    /*
    EFFECTS: Creates JTable representing current routine
     */
    private JScrollPane viewRoutine() {
        JTable table = new JTable(new RoutineTable(routine));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setOpaque(true);
        table.setFont(new Font("Arial",Font.PLAIN,16));

        makeEquipmentComboBox(table);

        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);

        return new JScrollPane(table);
    }

    /*
     EFFECTS: Creates combo box to edit Equipment column
     Inspired by: https://www.codejava.net/java-se/swing/how-to-create-jcombobox-cell-editor-for-jtable
     */
    private void makeEquipmentComboBox(JTable table) {
        TableColumn equipment = table.getColumnModel().getColumn(1);

        JComboBox comboBox = new JComboBox();
        comboBox.addItem(Equipment.BARBELL);
        comboBox.addItem(Equipment.DUMBBELL);
        comboBox.addItem(Equipment.MACHINE);
        comboBox.addItem(Equipment.NONE);

        equipment.setCellEditor((new DefaultCellEditor(comboBox)));
    }

    /*
    EFFECTS: Adds tools and buttons to bottom pane
     */
    private JScrollPane viewTools() {
        JTabbedPane tabbedPane = new JTabbedPane();

        modifyPanel(tabbedPane);
        sessionPanel(tabbedPane);

        return new JScrollPane(tabbedPane);
    }

    /*
    MODIFIES: tabbedPane
    EFFECTS: adds modify routine pane to tabbed pane
     */
    private void modifyPanel(JTabbedPane tabbedPane) {
        JPanel buttonPanel = modifyButtons();
        JList files = new JList(getFiles());

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
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(routine.getName());
        title.setFont(new Font("Arial", Font.BOLD,16));
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0,0,20,0);
        panel.add(title, c);

        JButton newButton = makeButton("new_icon.png", "NEW", c, 0, 1);
        newButton.setToolTipText("Create new routine");
        panel.add(newButton, c);

        JButton renameButton = makeButton("rename_icon.png","RENAME", c,0,2);
        renameButton.setToolTipText("Rename current routine");
        panel.add(renameButton, c);

        JButton saveButton = makeButton("save_icon.png", "SAVE", c, 0, 3);
        saveButton.setToolTipText("Save current routine");
        panel.add(saveButton, c);

        JButton loadButton = makeButton("open_icon.png","LOAD",c,0,4);
        loadButton.setToolTipText("Load selected routine from right");
        panel.add(loadButton, c);

        return panel;
    }

    /*
    REQUIRES: valid img in resources folder
    EFFECTS: creates new button
     */
    private JButton makeButton(String img, String name, GridBagConstraints c, int x, int y) {
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
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel("Panel 2");
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);

        ImageIcon icon = new ImageIcon(IMG_PATH + "barbel_icon.png");
        tabbedPane.addTab("Session", icon, panel, "Go to current session");
    }

    /*
    EFFECTS: starts application
     */
    public static void main(String[] args) {
        new WorkoutTracker();
    }
}
