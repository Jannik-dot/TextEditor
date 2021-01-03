package editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    JPanel window;
    JPanel controls;
    JTextArea text;
    JScrollPane scrollPane;
    JButton saveButton;
    JButton loadButton;
    JButton startSearch;
    JButton previous;
    JButton next;
    JCheckBox regex;
    JTextField searchField;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu menuSearch;
    JMenuItem save;
    JMenuItem load;
    JMenuItem exit;
    JMenuItem menuStartSearch;
    JMenuItem menuPreviousMatch;
    JMenuItem menuNextMatch;
    JMenuItem menuUseRegExp;
    ImageIcon saveIcon;
    ImageIcon loadIcon;
    ImageIcon nextIcon;
    ImageIcon previousIcon;
    ImageIcon startSearchIcon;
    JFileChooser fileChooser;
    Pattern pattern;
    Matcher matcher;
    ArrayList<Integer> matchStartInd;
    ArrayList<Integer> matchEndInd;
    int matchId;

    

    public TextEditor() {
        innit();
    }

    public void innit() {
        matchEndInd = new ArrayList<>();
        matchStartInd = new ArrayList<>();


        window = new JPanel();
        window.setLayout(new BorderLayout());
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
        fileChooser.setName("FileChooser");
        add(fileChooser);

        // Border-Layout: North
        controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));


        menuBar = new JMenuBar();

        // Menu for file functions
        fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");

        save = new JMenuItem("Save");
        save.setName("MenuSave");

        load = new JMenuItem("Load");
        load.setName("MenuOpen");

        exit = new JMenuItem("Exit");
        exit.setName("MenuExit");

        // Menu for search functions
        menuSearch = new JMenu("Search");
        menuSearch.setName("MenuSearch");

        menuStartSearch = new JMenuItem("Start search");
        menuStartSearch.setName("MenuStartSearch");

        menuPreviousMatch = new JMenuItem("Previous match");
        menuPreviousMatch.setName("MenuPreviousMatch");

        menuNextMatch = new JMenuItem("Next match");
        menuNextMatch.setName("MenuNextMatch");

        menuUseRegExp = new JMenuItem("Use regex");
        menuUseRegExp.setName("MenuUseRegExp");

        searchField = new JTextField();
        searchField.setName("SearchField");

        // Buttons for file functions
        saveIcon = new ImageIcon(".\\images\\save.png");
        saveButton = new JButton();
        saveButton.setName("SaveButton");
        saveButton.setIcon(saveIcon);

        loadIcon = new ImageIcon(".\\images\\folder.png");
        loadButton = new JButton();
        loadButton.setName("OpenButton");
        loadButton.setIcon(loadIcon);

        // Buttons for search options
        startSearchIcon = new ImageIcon(".\\images\\startSearch.png");
        startSearch = new JButton();
        startSearch.setName("StartSearchButton");
        startSearch.setIcon(startSearchIcon);

        previousIcon = new ImageIcon(".\\images\\previous.png");
        previous = new JButton();
        previous.setName("PreviousMatchButton");
        previous.setIcon(previousIcon);

        nextIcon = new ImageIcon(".\\images\\next.png");
        next = new JButton();
        next.setName("NextMatchButton");
        next.setIcon(nextIcon);

        regex = new JCheckBox("Use regex");
        regex.setName("UseRegExCheckbox");

        //action menus
        save.addActionListener(e -> saveFile());
        load.addActionListener(e -> loadFile());
        exit.addActionListener(e -> System.exit(1));
        menuStartSearch.addActionListener(e -> findText());
        menuPreviousMatch.addActionListener(e -> previousMatch());
        menuNextMatch.addActionListener(e -> nextMatch());
        menuUseRegExp.addActionListener(e -> {
            if(regex.isSelected()) {
                regex.setSelected(false);
            } else {
                regex.setSelected(true);
            }
        });

        add(menuBar, BorderLayout.NORTH);

        // File Menu:
        menuBar.add(fileMenu);
        fileMenu.add(save);
        fileMenu.add(load);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        // Search Menu:
        menuBar.add(menuSearch);
        menuSearch.add(menuStartSearch);
        menuSearch.add(menuPreviousMatch);
        menuSearch.add(menuNextMatch);
        menuSearch.add(menuUseRegExp);

        // action buttons
        saveButton.addActionListener(e -> saveFile());
        loadButton.addActionListener(e -> loadFile());
        startSearch.addActionListener(e -> findText());
        next.addActionListener(e -> nextMatch());
        previous.addActionListener(e -> previousMatch());

        controls.add(saveButton, BorderLayout.NORTH);
        controls.add(loadButton, BorderLayout.NORTH);
        controls.add(searchField, BorderLayout.NORTH);
        controls.add(startSearch, BorderLayout.NORTH);
        controls.add(previous, BorderLayout.NORTH);
        controls.add(next, BorderLayout.NORTH);
        controls.add(regex, BorderLayout.NORTH);


        // Border-Layout: Center
        text = new JTextArea();
        text.setName("TextArea");

        Insets insets = new Insets(10,10,10,10);
        text.setMargin(insets);

        scrollPane = new JScrollPane(text);
        scrollPane.setName("ScrollPane");
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        window.add(scrollPane, BorderLayout.CENTER);
        window.add(controls, BorderLayout.NORTH);
        add(window);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("TextEditor");
        setSize(600, 600);

        setVisible(true);
    }



    public void loadFile() {
        File file = new File("");
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            text.read(br, file);
        } catch (IOException ioException) {
            text.setText("");
        }
    }

    public void saveFile() {
        File file = new File("");
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }

        try (FileWriter writer = new FileWriter(file.getAbsolutePath(), false)) {
            file.createNewFile();
            writer.write(text.getText());

        } catch (IOException | NullPointerException ioException) {

            ioException.printStackTrace();
        }
    }


    public void findText() {
        matchEndInd = new ArrayList<>();
        matchStartInd = new ArrayList<>();
        if (regex.isSelected()) {
            pattern = Pattern.compile(searchField.getText());
        } else {
            pattern = Pattern.compile(searchField.getText(), Pattern.LITERAL);
        }

        matcher = pattern.matcher(text.getText());

        while (matcher.find()) {
            matchStartInd.add(matcher.start());
            matchEndInd.add(matcher.end());
        }

        matchId = 0;
        text.setCaretPosition(matchStartInd.get(matchId));
        text.select(matchStartInd.get(matchId), matchEndInd.get(matchId));
        text.grabFocus();

    }

    public void nextMatch() {
        matchId++;
        if (matchId + 1 > matchStartInd.size()) {
            matchId = 0;
        }
        text.setCaretPosition(matchStartInd.get(matchId));
        text.select(matchStartInd.get(matchId), matchEndInd.get(matchId));
        text.grabFocus();
    }

    public void previousMatch() {
        matchId--;
        if (matchId < 0) {
            matchId = matchStartInd.size() - 1;
        }
        text.setCaretPosition(matchStartInd.get(matchId));
        text.select(matchStartInd.get(matchId), matchEndInd.get(matchId));
        text.grabFocus();
        matcher.find(matchStartInd.get(matchId));
    }


}
