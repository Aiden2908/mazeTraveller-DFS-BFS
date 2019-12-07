package assignment.pkg3;

/**
 * A class that builds paths in a maze (with an exit on far left east wall, also
 * can be run in a thread for animated path building.. Need to ensure both Maze,
 * Direction and Room done properly.
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class MazeMaker {

    private static Random generator = new Random();
    private static int delay = 0;

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://raptor2.aut.ac.nz:3306/mazes";
    private static Connection conn = null;
    public static ArrayList<Maze> databaseMazes = new ArrayList<>();

    public static void createMazePathsInThread(Maze maze) {
        delay = 5;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int numRows = maze.getNumRows();
                int numCols = maze.getNumCols();
                int startRow = generator.nextInt(numRows);
                visitRoom(maze, startRow, 0);
                // randomly open one door along the eastern wall of maze
                int exitRow = generator.nextInt(numRows);
                maze.openDoor(exitRow, numCols - 1, Direction.EAST);
            }
        });
        t.start();
    }

    public static Maze loadMazeFromDatabase(String mazeName, String username, String password) {
        Maze maze = null;

        //TODO THIS FOR QUESTION 3!!!!
        return maze;
    }

    // prepares a maze of the specified direction that hsa a single
    // exit somewhere along the eastern wall
    public static void createMazePaths(Maze maze) {  // prepare a maze whose doors are all initially closed
        delay = 0;
        int numRows = maze.getNumRows();
        int numCols = maze.getNumCols();
        int startRow = generator.nextInt(numRows);
        visitRoom(maze, startRow, 0);
        // randomly open one door along the eastern wall of maze
        int exitRow = generator.nextInt(numRows);
        maze.openDoor(exitRow, numCols, Direction.EAST);
        // System.out.println("EXIT ROW ="+exitRow+" j="+(numCols-1));
    }

    public static void loadMazeFromDatabase() {
        try{
        
        connectToDatabase();
        }catch(Exception ex){
        
        }
    }

    private static void connectToDatabase() {
        String[] mazeNames = {"tiny", "small", "medium", "large", "enormous"};

        ArrayList<String> tinyData = new ArrayList<>();
        ArrayList<String> smallData = new ArrayList<>();
        ArrayList<String> mediumData = new ArrayList<>();
        ArrayList<String> largeData = new ArrayList<>();
        ArrayList<String> enormousData = new ArrayList<>();
        String[] data = new String[6];
        try {
            Class.forName(DRIVER);
            try {
                conn = DriverManager.getConnection(DB_URL, "student", "fpn871");
            } catch (SQLException ex) {
                //Logger.getLogger(MazeMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (conn != null) {
                for (int i = 0; i < mazeNames.length; i++) {
                    try {
                        String query = "select * from " + mazeNames[i];
                        // System.out.println("Success?");
                        Statement st = conn.createStatement();
                        ResultSet results = st.executeQuery(query);

                        while (results.next()) {
                            data[0] = Integer.toString(results.getInt("Row"));
                            data[1] = Integer.toString(results.getInt("Col"));
                            data[2] = results.getString("North");
                            data[3] = results.getString("East");
                            data[4] = results.getString("South");
                            data[5] = results.getString("West");
                            //System.out.println("Maze: " + mazeNames[i] + "," + Arrays.toString(data));
                            switch (i) {
                                case 0:
                                    tinyData.add(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[5]);
                                    break;
                                case 1:
                                    smallData.add(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[5]);
                                    break;
                                case 2:
                                    mediumData.add(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[5]);
                                    break;
                                case 3:
                                    largeData.add(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[5]);
                                    break;
                                case 4:
                                    enormousData.add(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[5]);
                                    break;
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("Failed to load maze" + mazeNames[i]);
                    }
                }
            }else{return;}
        } catch (ClassNotFoundException ex) {
           // Logger.getLogger(MazeMaker.class.getName()).log(Level.SEVERE, null, ex);
        }

        ArrayList<ArrayList<String>> mazes = new ArrayList<>();
        mazes.add(tinyData);
        mazes.add(smallData);
        mazes.add(mediumData);
        mazes.add(largeData);
        mazes.add(enormousData);

        for (int o = 0; o < mazes.size(); o++) {
            String[] tempArr;
            tempArr = mazes.get(o).get(mazes.get(o).size() - 1).split(",");
            //System.out.println("R="+tinyData.get(tinyData.size()-1).charAt(0));
            System.out.println("R=" + tempArr[0] + "," + "C=" + tempArr[1]);
            Maze maze = new Maze(Integer.parseInt(tempArr[0]) + 1, Integer.parseInt(tempArr[1]) + 1);
            // maze.openDoor(2-1, 4-1, Direction.WEST);
            for (int k = 0; k < mazes.get(o).size(); k++) {
                // System.out.println("i=" + k + ">" + tinyData.get(k));
                tempArr = mazes.get(o).get(k).split(",");
                int row = Integer.parseInt(tempArr[0]);
                int col = Integer.parseInt(tempArr[1]);

                if (tempArr[2].endsWith("Y")) {//2=NORTH
                    maze.openDoor(row, col, Direction.NORTH);
                    //   System.out.println("Row="+row+", Col="+col);
                }
                if (tempArr[3].endsWith("Y")) {//2=EAST
                    maze.openDoor(row, col, Direction.EAST);
                }
                if (tempArr[4].endsWith("Y")) {//2=SOUTH
                    maze.openDoor(row, col, Direction.SOUTH);
                }
                if (tempArr[5].endsWith("Y")) {//2=WEST
                    maze.openDoor(row, col, Direction.WEST);
                }
            }
            databaseMazes.add(maze);
        }
    }

    // recursive helper method which uses a depth first search of maze
    // opening doors as it moves from room to room
    private static void visitRoom(Maze maze, int row, int col) {  // randomize the order in which directions will be moved
        List<Direction> directionList = new ArrayList<Direction>(4);
        for (Direction direction : Direction.values()) {
            directionList.add(direction);
        }
        Collections.shuffle(directionList);
        Iterator<Direction> iterator = directionList.iterator();
        while (iterator.hasNext()) {
            Direction direction = iterator.next();
            // determine row and column of adjacent room
            int adjRow = row, adjCol = col;
            switch (direction) {
                case NORTH:
                    adjRow--;
                    break;
                case EAST:
                    adjCol++;
                    break;
                case SOUTH:
                    adjRow++;
                    break;
                case WEST:
                    adjCol--;
                    break;
            }

            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
            }
            // determine whether the adjacent room should be visited
            if (maze.isInsideMaze(adjRow, adjCol)
                    && !maze.hasOpenDoor(adjRow, adjCol)) {
                maze.openDoor(row, col, direction);
                visitRoom(maze, adjRow, adjCol);
            }
        }
    }

    public static void main(String[] args) {
//        Maze m = new Maze(10, 10);
//        System.out.println("" + m.isInsideMaze(9, 0));
//        MazeMaker.createMazePaths(m);
//        m.printOpenDoors();
        MazeMaker.loadMazeFromDatabase();
    }
}
