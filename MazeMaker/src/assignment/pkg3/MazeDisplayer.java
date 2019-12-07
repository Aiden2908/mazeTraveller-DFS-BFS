package assignment.pkg3;

/**
 * *****
 * *******************************************************************************
 * A simple class that can be used to display a MAZE
 * *************************************************************************************
 */
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import static java.awt.Toolkit.getDefaultToolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MazeDisplayer extends JPanel implements ActionListener {

    private Maze maze;
    private DrawPanel drawPanel;
    private JButton performMazeAlg;
    private JButton loadMazeButton;
    private Timer timer;
    private Random random = new Random();
    private boolean isMazesLoading = true;
    private final Dimension screenSize = getDefaultToolkit().getScreenSize();
    private final int WINDOW_WIDTH = ((screenSize.width / 2) - screenSize.width / 40), WINDOW_HEIGHT = ((screenSize.height / 2) + screenSize.width / 6);
    int i = 0;
    private RacePanel racePanel;
    private String strLoadingStatus;
    private SettingsPanel settingsPanel;

    public MazeDisplayer() {
        super(new BorderLayout());
        // maze = new Maze(MAZE_SIZE,MAZE_SIZE);
        // MazeMaker.createMazePaths(maze);

        timer = new Timer(15, this);
        //create the mouses
        JPanel southPanel = new JPanel();
        loadMazeButton = new JButton("Load Maze from DB");
        loadMazeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isMazesLoading = true;
                drawPanel.repaint();
                // maze = MazeMaker.databaseMazes.get(i % 5);
                System.out.println("i=" + i % 5);
                i++;
                //maze.openValidator();
                //maze.printOpenDoors();
                // drawPanel.repaint();
            }

        });
        southPanel.add(loadMazeButton);

        performMazeAlg = new JButton("Build Paths");
        performMazeAlg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked");
            }
        });
        southPanel.add(performMazeAlg);

        //add(southPanel, BorderLayout.SOUTH);
        drawPanel = new DrawPanel();
        settingsPanel = new SettingsPanel();
        drawPanel.add(settingsPanel, BorderLayout.SOUTH);
        add(drawPanel, BorderLayout.CENTER);
    }

    private class DrawPanel extends JPanel {

        public DrawPanel() {
            super();
            this.setLayout(new BorderLayout());
            super.setBackground(Color.white);
            super.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        }

        //draws the maze and draws the different mouses in the maze
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (maze != null) {
                maze.drawMaze(g, getWidth(), getHeight());
            }
            if (isMazesLoading) {
                g.setColor(Color.GREEN);
                g.drawString(strLoadingStatus, 10, 20);
            }

        }
    }

    public class SettingsPanel extends JPanel {

        private final String[] mazeChoices = {"Tiny Maze", "Small Maze", "Medium Maze", "Large Maze", "Enormous Maze"};
        private final JComboBox<String> cbDBMazes = new JComboBox<>(mazeChoices);
        private final String[] mouseChoices = {"Random AI Mouse", "Depth Search Mouse"};
        private final JComboBox<String> cbMouse = new JComboBox<>(mouseChoices);
        private SliderPanel sliderPanel = new SliderPanel();
        private ButtonPanel buttonPanel = new ButtonPanel();
        private JSlider jslRows, jslCols;

        public SettingsPanel() {
            this.setBackground(Color.WHITE);
            this.setPreferredSize(new Dimension(WINDOW_WIDTH, 165));
            this.setLayout(new BorderLayout());

            this.add(sliderPanel, BorderLayout.NORTH);
            this.add(buttonPanel, BorderLayout.SOUTH);

            Thread loadMazesThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        strLoadingStatus = "Loading...";
                        System.out.println("" + isMazesLoading);
                        MazeMaker.loadMazeFromDatabase();
                        maze = MazeMaker.databaseMazes.get(4);
                        drawPanel.repaint();
                        cbDBMazes.setSelectedIndex(4);
                        isMazesLoading = false;
                        System.out.println("" + isMazesLoading);
                        jslRows.setValue(maze.getNumRows());
                        jslCols.setValue(MazeMaker.databaseMazes.get(4).getNumCols());
                    } catch (Exception ex) {
                        strLoadingStatus = "Failed to load datasbase mazes";
                    }
                }
            });
            loadMazesThread.start();
        }

        private class SliderPanel extends JPanel {

            public SliderPanel() {
                this.setBackground(Color.red);
                this.setPreferredSize(new Dimension(WINDOW_WIDTH, 124));
                this.setLayout(new BorderLayout());
                JLabel lb = new JLabel();

                jslRows = new JSlider(JSlider.HORIZONTAL, 1, 65, 1);
                //jslRows.setBackground(blueTheme[1]);
                jslRows.setForeground(Color.BLACK);
                jslRows.setBorder(BorderFactory.createTitledBorder(null, "Number of Rows", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, new Font(lb.getName(), Font.PLAIN, 14), Color.BLACK));
                jslRows.setMajorTickSpacing(1);
                jslRows.setPaintLabels(true);
                jslRows.setFont(new Font("", Font.PLAIN, 8));
                jslRows.setPaintTicks(true);
                jslRows.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        removeCurrentMouses();
                        maze = createNewMaze(jslRows.getValue(), jslCols.getValue());
                        drawPanel.repaint();
                    }

                });
                this.add(jslRows, BorderLayout.NORTH);
                System.out.println("" + jslRows.getValue());
                jslCols = new JSlider(JSlider.HORIZONTAL, 1, 80, 1);
                //jslRows.setBackground(blueTheme[1]);
                jslCols.setForeground(Color.BLACK);
                jslCols.setBorder(BorderFactory.createTitledBorder(null, "Number of Columns", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, new Font("", Font.PLAIN, 14), Color.BLACK));
                jslCols.setMajorTickSpacing(1);
                jslCols.setFont(new Font("", Font.PLAIN, 8));
                jslCols.setPaintLabels(true);
                jslCols.setPaintTicks(true);
                jslCols.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        removeCurrentMouses();
                        System.out.println("" + jslRows.getValue());
                        maze = createNewMaze(jslRows.getValue(), jslCols.getValue());
                        drawPanel.repaint();
                    }
                });

                this.add(jslCols, BorderLayout.SOUTH);
            }
        }

        private class ButtonPanel extends JPanel {

//            private final JComboBox<String> cbCurrentMouse = new JComboBox<>(currentMouseList);
            private JButton btnKeyboardMouse;
            private JButton btnRecreateMaze;
            private JButton btnAddAIMouse;
            private JButton btnRace;
            private KeyboardMouse keyboardMouse;
            private boolean isKeyboardMouseEnabled, showRaceSettings;

            public ButtonPanel() {
                this.setBackground(Color.BLUE);
                this.setPreferredSize(new Dimension(WINDOW_WIDTH, 45));
                this.setLayout(new GridLayout());
                racePanel = new RacePanel();
                //  racePanel.setVisible(false);

                cbDBMazes.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!MazeMaker.databaseMazes.isEmpty()) {
                            removeCurrentMouses();
                            switch (cbDBMazes.getSelectedIndex()) {
                                case 0:
                                    maze = MazeMaker.databaseMazes.get(0);
                                    break;
                                case 1:
                                    maze = MazeMaker.databaseMazes.get(1);
                                    break;
                                case 2:
                                    maze = MazeMaker.databaseMazes.get(2);
                                    break;
                                case 3:
                                    maze = MazeMaker.databaseMazes.get(3);
                                    break;
                                case 4:
                                    maze = MazeMaker.databaseMazes.get(4);
                                    break;
                            }
                            jslRows.setValue(maze.getNumRows());
                            jslCols.setValue(MazeMaker.databaseMazes.get(cbDBMazes.getSelectedIndex()).getNumCols());
                            drawPanel.repaint();
                        }
                    }
                });
                this.add(cbDBMazes);

                cbMouse.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                this.add(cbMouse);

                btnAddAIMouse = new JButton("Add AI Mouse");
                btnAddAIMouse.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // keyboardMouse=new KeyboardMouse(maze,1,1,drawPanel);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (cbMouse.getSelectedIndex() == 0) {
                                    RandomMouse randMouse = new RandomMouse(maze, 1, 0, 10, drawPanel);
                                    maze.addRandomMouse(randMouse);
                                    drawPanel.repaint();
                                    try {
                                        while (!randMouse.stopRequested) {
                                            randMouse.move();
                                            Thread.sleep(50);
                                        }
                                    } catch (InterruptedException ex) {
                                        //  Logger.getLogger(MazeDisplayer.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else if (cbMouse.getSelectedIndex() == 1) {
                                    SmartMouse smartMouse = new SmartMouse(maze, 0, 0, 0, drawPanel);
                                    maze.addSmartMouse(smartMouse);
                                    drawPanel.repaint();
                                    try {
                                        while (true) {
                                            smartMouse.move();
                                            Thread.sleep(50);
                                        }
                                    } catch (InterruptedException ex) {
                                        //  Logger.getLogger(MazeDisplayer.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }

                        }).start();
                    }

                });
                this.add(btnAddAIMouse);
                btnKeyboardMouse = new JButton("Enable Keyboard Mouse");
                btnKeyboardMouse.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // keyboardMouse=new KeyboardMouse(maze,1,1,drawPanel);
                        maze.addKeyboardMouse(new KeyboardMouse(maze, 0, 0, drawPanel));
                        drawPanel.repaint();
                    }

                });
                btnKeyboardMouse.addKeyListener(new KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (maze.keyboardMouse != null) {
                            maze.keyboardMouse.keyPressed(e);
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }

                });

                btnRace = new JButton("Race");
                btnRace.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeCurrentMouses();
                        settingsPanel.sliderPanel.setVisible(false);
                        racePanel.setVisible(true);
                        settingsPanel.add(racePanel, BorderLayout.NORTH);
                    }

                });
                this.add(btnRace);

                btnKeyboardMouse.setFocusable(true);
                this.add(btnKeyboardMouse);

                btnRecreateMaze = new JButton("Recreate Maze");
                btnRecreateMaze.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeCurrentMouses();
                        maze = createNewMaze(jslRows.getValue(), jslCols.getValue());
                        drawPanel.repaint();
                    }

                });
                this.add(btnRecreateMaze);

            }
        }

        private Maze createNewMaze(int row, int col) {
            try {
                removeCurrentMouses();
                Maze tempMaze;
                tempMaze = new Maze(row, col);
                MazeMaker.createMazePaths(tempMaze);
                return tempMaze;
            } catch (Exception ex) {
                return null;
            }
        }

        private void removeCurrentMouses() {
            for (RandomMouse r : maze.randomMouses) {
                r.stopRequested = true;
            }

            for (SmartMouse s : maze.smartMouses) {
                s.stopRequested = true;
            }
        }
    }

    private class RacePanel extends JPanel {

        private JSpinner spnNumofRandomMouse;
        private JSpinner spnRandomMouseSpeed;
        private JSpinner spnNumofSmartMouse;
        private JSpinner spnSmartMouseSpeed;
        private JCheckBox chkbEnableKeyboardMouse;
        private JButton btnStart;
        private RacePanel racePanel = this;

        public RacePanel() {
            this.setBackground(Color.red);
            this.setPreferredSize(new Dimension(WINDOW_WIDTH, 50));
            this.setLayout(new GridLayout());

            spnNumofRandomMouse = new JSpinner();
            spnNumofRandomMouse.setValue(1);
            spnNumofRandomMouse.setBorder(BorderFactory.createTitledBorder(null, "No. Random-mouses", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("", Font.PLAIN, 12), Color.BLACK));
            this.add(spnNumofRandomMouse);

            spnRandomMouseSpeed = new JSpinner();
            spnRandomMouseSpeed.setValue(100);
            spnRandomMouseSpeed.setBorder(BorderFactory.createTitledBorder(null, "Rand-mouse speed", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("", Font.PLAIN, 12), Color.BLACK));
            this.add(spnRandomMouseSpeed);

            spnNumofSmartMouse = new JSpinner();
            spnNumofSmartMouse.setValue(1);
            spnNumofSmartMouse.setBorder(BorderFactory.createTitledBorder(null, "No. Smart-mouses", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("", Font.PLAIN, 12), Color.BLACK));
            this.add(spnNumofSmartMouse);

            spnSmartMouseSpeed = new JSpinner();
            spnSmartMouseSpeed.setValue(100);
            spnSmartMouseSpeed.setBorder(BorderFactory.createTitledBorder(null, "Smart-mouse speed", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("", Font.PLAIN, 12), Color.BLACK));
            this.add(spnSmartMouseSpeed);

            chkbEnableKeyboardMouse = new JCheckBox(" Keybord-mouse");
            chkbEnableKeyboardMouse.setSelected(true);
            // chkbEnableKeyboardMouse.setBorder(BorderFactory.createTitledBorder(null, "Enable Keybord-mouse", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, new Font("", Font.PLAIN, 14), Color.BLACK));
            this.add(chkbEnableKeyboardMouse);

            btnStart = new JButton("Start");
            btnStart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (maze != null) {
                        for (RandomMouse r : maze.randomMouses) {
                            r.stopRequested = true;
                        }

                        for (SmartMouse s : maze.smartMouses) {
                            s.stopRequested = true;
                        }
                        ArrayList<Thread> threads = new ArrayList<>();

                        int numRandomMouses = Integer.parseInt(spnNumofRandomMouse.getValue().toString());
                        int randMouseSpeed = Integer.parseInt(spnRandomMouseSpeed.getValue().toString());
                        int numSmartMouses = Integer.parseInt(spnNumofSmartMouse.getValue().toString());
                        int smartMouseSpeed = Integer.parseInt(spnSmartMouseSpeed.getValue().toString());

                        for (int i = 0; i < numRandomMouses; i++) {
                            int randomRow = random.nextInt(maze.getNumRows());
                            RandomMouse randMouse = new RandomMouse(maze, randomRow, 0, randMouseSpeed, drawPanel);
                            maze.addRandomMouse(randMouse);
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (true) {
                                        try {
                                            randMouse.move();
                                            Thread.sleep(randMouseSpeed);
                                        } catch (InterruptedException ex) {
                                            //  Logger.getLogger(MazeDisplayer.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }

                            });
                            threads.add(t);
                        }

                        for (int i = 0; i < numSmartMouses; i++) {
                            int randomRow = random.nextInt(maze.getNumRows());
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    SmartMouse smartMouse = new SmartMouse(maze, randomRow, 0, smartMouseSpeed, drawPanel);
                                    maze.addSmartMouse(smartMouse);
                                    //drawPanel.repaint();
                                    try {
                                        while (true) {
                                            smartMouse.move();
                                            Thread.sleep(smartMouseSpeed);
                                        }
                                    } catch (InterruptedException ex) {
                                        //  Logger.getLogger(MazeDisplayer.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                            });
                            threads.add(t);
                        }

                        for (Thread t : threads) {
                            t.start();
                        }

                        if (chkbEnableKeyboardMouse.isSelected()) {
                            int randomRow = random.nextInt(maze.getNumRows());
                            maze.addKeyboardMouse(new KeyboardMouse(maze, randomRow, 0, drawPanel));
                        }

                        racePanel.setVisible(false);
                        settingsPanel.sliderPanel.setVisible(true);
                        settingsPanel.add(settingsPanel.sliderPanel, BorderLayout.NORTH);

                    }
                }

            });
            this.add(btnStart);

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        drawPanel.repaint();
    }
    //request keyboard focus

    //main method to test this game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Maker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MazeDisplayer());
        frame.pack();
        // position the frame in the middle of the screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize();
        frame.setLocation((screenDimension.width - frameDimension.width) / 2,
                (screenDimension.height - frameDimension.height) / 2);
        frame.setVisible(true);

    }
}
