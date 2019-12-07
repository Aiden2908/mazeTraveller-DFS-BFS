package assignment.pkg3;

import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class SmartMouse extends Mouse {

    private ArrayList<Coordinates> visitedList = new ArrayList<>();
    private JPanel drawPanel;
    private int startRow, startCol;
    private int backI;

    public SmartMouse(Maze maze, int startRow, int startCol, int delay, JPanel drawPanel) {
        super(maze, delay, startRow, startCol);
        this.drawPanel = drawPanel;
//        this.startRow = startRow;
//        this.startCol = startCol;
        visitedList.add(new Coordinates(startRow, startCol));
    }

    @Override
    protected void move() {
        if (!stopRequested) {
            defaultSearch();
            System.out.println("Get last added:" + visitedList.get(visitedList.size() - 1).toString());
        }
    }

    private void defaultSearch() {
        if (!hasFoundExit()) {//Order: E,N,S,W

            if (maze.isOpen(row, col, Direction.EAST)) {
                //   System.out.println("Avilable Path East");
                if (!hasCoordinateInList(row, col + 1)) {
                    col += 1;
                    System.out.println("Moved EAST");
                    Coordinates coordinates = new Coordinates(row, col);
                    visitedList.add(coordinates);
                    System.out.println("Added coord> " + coordinates);
                    backI = visitedList.size() - 2;
                    drawPanel.repaint();
                    return;
                }
            }
            if (maze.isOpen(row, col, Direction.NORTH)) {
                //    System.out.println("Avilable Path North");
                if (!hasCoordinateInList(row - 1, col)) {
                    row -= 1;
                    System.out.println("Moved NORTH");
                    Coordinates coordinates = new Coordinates(row, col);
                    visitedList.add(coordinates);
                    System.out.println("Added coord> " + coordinates);
                    backI = visitedList.size() - 2;
                    drawPanel.repaint();
                    return;

                }
            }
            if (maze.isOpen(row, col, Direction.SOUTH)) {
                // System.out.println("Avilable Path South");
                if (!hasCoordinateInList(row + 1, col)) {
                    row += 1;
                    System.out.println("Moved SOUTH");
                    Coordinates coordinates = new Coordinates(row, col);
                    visitedList.add(coordinates);
                    System.out.println("Added coord> " + coordinates);
                    backI = visitedList.size() - 2;
                    drawPanel.repaint();
                    return;
                }
            }
            if (maze.isOpen(row, col, Direction.WEST)) {
                //  System.out.println("Avilable Path West");
                if (!hasCoordinateInList(row, col - 1)) {
                    col -= 1;
                    System.out.println("Moved WEST");
                    Coordinates coordinates = new Coordinates(row, col);
                    visitedList.add(coordinates);
                    System.out.println("Added coord> " + coordinates);
                    backI = visitedList.size() - 2;
                    drawPanel.repaint();
                    return;
                }
            }
            row = visitedList.get(backI).row;
            col = visitedList.get(backI).col;
            Coordinates coordinates = new Coordinates(row, col);
            visitedList.add(coordinates);
            backI -= 1;
            System.out.println("No where to go");
            //defaultSearch();
            drawPanel.repaint();
        }
    }

    public Coordinates getLastVisited() {
        return visitedList.get(visitedList.size() - 1);
    }

    private boolean hasCoordinateInList(int row, int col) {
        for (int i = 0; i < visitedList.size(); i++) {
            if (visitedList.get(i).col == col && visitedList.get(i).row == row) {
                return true;
            }
        }
        return false;
    }

//    private boolean dfs(int row, int col) {
//        if(maze.isInsideMaze(row, col)){
//        Room currentRoom = maze.getRoom(row, col);
//        if (hasFoundExit()) {
//            drawPanel.repaint();
//            return true;
//        }
//        if (dfs(row-1, col)) {
//            col-=1;
//            drawPanel.repaint();
//            return true;
//        }
//        if (dfs(row, col + 1)) {
//            col += 1;
//            drawPanel.repaint();
//            return true;
//        }
//        if (dfs(row, col - 1)) {
//            col -= 1;
//            drawPanel.repaint();
//            return true;
//        }
//        if (dfs(row+1, col)) {
//            col +=1;
//            drawPanel.repaint();
//            return true;
//        }
//        }
//        return false;
//    }
    private class Coordinates {

        protected int row;
        protected int col;

        public Coordinates(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return "(" + row + "," + col + ")";
        }
    }

}
