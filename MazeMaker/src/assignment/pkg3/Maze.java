package assignment.pkg3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Maze {

    private int numRows;
    private int numCols;
    private Room[][] room;
    boolean t;
    protected KeyboardMouse keyboardMouse;
    protected ArrayList<RandomMouse> randomMouses = new ArrayList<>();
    protected ArrayList<SmartMouse> smartMouses = new ArrayList<>();

    ;

    public Maze(int numRows, int numCols) {
        room = new Room[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                room[i][j] = new Room();
            }
        }
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public Room getRoom(int row, int col) {
        return room[row][col];
    }

    public int getNumRows() {
        return numRows - 1;
    }

    public int getNumCols() {
        return numCols - 1;
    }

    public boolean isOpen(int row, int col, Direction door) {
        return room[row][col].isDoorOpen(door);
    }

    public boolean hasOpenDoor(int row, int col) {
        return room[row][col].hasOpenDoor();
    }

    public void openDoor(int row, int col, Direction door) {
        room[row][col].openDoor(door);
        if (door == Direction.EAST) {
            if (isInsideMaze(row, col + 1)) {
                room[row][col + 1].openDoor(Direction.WEST);
            }
        }
        if (door == Direction.WEST) {
            if (isInsideMaze(row, col - 1)) {
                room[row][col - 1].openDoor(Direction.EAST);
            }
        }
        if (door == Direction.NORTH) {
            if (isInsideMaze(row - 1, col)) {
                room[row - 1][col].openDoor(Direction.SOUTH);
            }
        }
        if (door == Direction.SOUTH) {
            if (isInsideMaze(row + 1, col)) {
                room[row + 1][col].openDoor(Direction.NORTH);
            }
        }
    }

    public void printOpenDoors() {
        for (int j = 0; j < numRows; j++) {
            for (int u = 0; u < numCols; u++) {
                if (room[j][u].isDoorOpen(Direction.WEST)) {
                    System.out.println("Room [" + j + "," + u + "] > WEST Open!");
                }
                if (room[j][u].isDoorOpen(Direction.EAST)) {
                    System.out.println("Room [" + j + "," + u + "] > EAST Open!");
                }
                if (room[j][u].isDoorOpen(Direction.SOUTH)) {
                    System.out.println("Room [" + j + "," + u + "] > SOUTH Open!");
                }
                if (room[j][u].isDoorOpen(Direction.NORTH)) {
                    System.out.println("Room [" + j + "," + u + "] > NORTH Open!");
                }
            }
        }

    }

    public boolean isInsideMaze(int row, int col) {
        if (this.numRows > row && this.numCols > col && row >= 0 && col >= 0) {
            return true;
        }
        return false;
    }

    private int getRowLen() {
        return 0;
    }

    public void addKeyboardMouse(KeyboardMouse keyboardMouse) {
        this.keyboardMouse = keyboardMouse;
    }

    public void addRandomMouse(RandomMouse randomMouse) {
        this.randomMouses.add(randomMouse);
    }

    public void addSmartMouse(SmartMouse smartMouse) {
        this.smartMouses.add(smartMouse);
    }

    public void drawMaze(Graphics g, int worldWidth, int worldHeight) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double marginX = 10;
        double marginY = 10;

        double xSpaceLeft = worldWidth - marginX - marginX;
        double ySpaceLeft = worldHeight - marginY - marginY;

        double xPoint = marginX;//Start point on the x-axis
//        g2.setColor(Color.ORANGE);
//        g2.fill(new Ellipse2D.Double(xPoint, marginY, 10, 10));//x1
//        g2.fill(new Ellipse2D.Double(xSpaceLeft - 10, marginY, 10, 10));//x2

        double yPoint = marginY;
//        g2.setColor(Color.ORANGE);
//        g2.fill(new Ellipse2D.Double(yPoint, ySpaceLeft, 10, 10));//y1
//        g2.fill(new Ellipse2D.Double(xSpaceLeft - 10, ySpaceLeft, 10, 10));//y2

        double drawableSpaceY = ySpaceLeft - yPoint;

        double lineLengthY = drawableSpaceY / (numCols);
        double lineThickness = 1;

        double y2Point = yPoint + lineLengthY;
        double x2Point = xPoint + lineLengthY;
        g2.setColor(new Color(13, 13, 13));

        double lineXwidth = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (!room[i][j].isDoorOpen(Direction.WEST)) {
                    g2.draw(new Rectangle2D.Double(xPoint, yPoint, lineThickness, lineLengthY));
                }
                if (!room[i][j].isDoorOpen(Direction.EAST)) {
                    g2.draw(new Rectangle2D.Double(x2Point, yPoint, lineThickness, lineLengthY));
                }
                if (!room[i][j].isDoorOpen(Direction.NORTH)) {
                    g2.draw(new Rectangle2D.Double(xPoint, yPoint, lineLengthY, lineThickness));
                }
                if (!room[i][j].isDoorOpen(Direction.SOUTH)) {
                    g2.draw(new Rectangle2D.Double(xPoint, y2Point, lineLengthY, lineThickness));
                }
                xPoint += lineLengthY;
                x2Point += lineLengthY;
            }
            yPoint += lineLengthY;//Jump to next row
            y2Point += lineLengthY;
            // lineXwidth = xPoint;
            xPoint = marginY;
            x2Point = xPoint + lineLengthY;
        }

        if (!randomMouses.isEmpty()) {
            for (RandomMouse r : randomMouses) {
                r.drawMouse(g2, marginX, r.row, r.col, lineLengthY, Color.CYAN);
            }
        }

        if (!smartMouses.isEmpty()) {
            for (SmartMouse r : smartMouses) {
                r.drawMouse(g2, marginX, r.row, r.col, lineLengthY, new Color(255, 0, 102));
            }
        }
        if (keyboardMouse != null) {
            keyboardMouse.drawMouse(g2, marginX, keyboardMouse.row, keyboardMouse.col, lineLengthY, new Color(255, 51, 0));
        }
    }

    public static void main(String args[]) {
        Maze maze = new Maze(15, 15);
        maze.openDoor(4, 7, Direction.NORTH);
        maze.openDoor(4, 7, Direction.WEST);
        // System.out.println(""+maze.hasOpenDoor(4, 7));
        // System.out.println(""+maze.isInsideMaze(7, 17));
        System.out.println("" + maze.hasOpenDoor(14, 15));
        maze.printOpenDoors();
    }
}

//
//int lineThickness = 2;//Line tickness.
//
//        int x = 10;//Start with 10 margin on x-axis.
//        int y = 10;
//
//        g2.setColor(Color.red);
//        int availableSpaceX = worldWidth - (2 * x);
//        int availableSpaceY = worldHeight - (2 * y);
//
//        g2.drawString("X1", x, y);
//        g2.drawString("X2=" + availableSpaceX, availableSpaceX, y);
//
//        g2.setColor(Color.BLUE);
//
//        int lineLengthX = 9;
//        int lineLengthY = 9;
//        g2.drawString("worldWith=" + worldWidth + ", X1=" + x + ", X2=" + availableSpaceX + ", X2-X1=" + (availableSpaceX) + ", rows*len=" + numRows * lineLengthX, x + 20, y);
//        g2.setColor(Color.MAGENTA);
//        g2.drawString("*", numRows * lineLengthX, y + 15);
//
//        int xLen = 0;
//
//        System.out.println("Line Len=" + lineLengthX);
//
//        int x2 = x + lineLengthX;//East.
//        int y2 = y + lineLengthY;
//
//        //  room[9][12].openDoor(Direction.EAST);
//        //room[0][12].openDoor(Direction.NORTH);
//        for (int i = 0; i < numRows; i++) {// Rows
//            for (int j = 0; j < numCols; j++) {
//                if (!room[i][j].isDoorOpen(Direction.WEST)) {
//                    g2.setColor(Color.BLACK);
//                    g2.fillRect(x, y, lineThickness, lineLengthY);
//                }
//                if (!room[i][j].isDoorOpen(Direction.EAST)) {
//                    g2.setColor(Color.BLACK);
//                    g2.fillRect(x2, y, lineThickness, lineLengthY);
//                }
//                if (!room[i][j].isDoorOpen(Direction.NORTH)) {//Draw top
//                    //  System.out.println("Drawing " + i + "," + "" + j);
//                    g2.setColor(Color.BLACK);
//                    g2.fillRect(x, y, lineLengthX, lineThickness);
//                }
//                if (!room[i][j].isDoorOpen(Direction.SOUTH)) {
//                    g2.setColor(Color.BLACK);
//                    g2.fillRect(x, y2, lineLengthX, lineThickness);
//                    // xLen += lineLengthX;
//                }
//
//                g2.setColor(Color.BLACK);
//                //  g2.drawString("(R:" + i + ", C:" + j + ")", x + 7, y + 15);
//                g2.setColor(Color.CYAN);
//              //  g2.drawString("*", x + 7, y + 15);
//                x += lineLengthX;
//                x2 += lineLengthX;
//            }
//
//            x = 10;
//            x2 = x + lineLengthX;
//            y += lineLengthY;
//            y2 = y + lineLengthY;
//        }
//        y=10;
//        drawMouse(g2,64*x,y*79);
//        // System.out.println("LinelenX="+lineLengthX+" , numRows="+numRows+", availableSpaceX"+availableSpaceX);
//        System.out.println("rows*width=" + numRows * lineLengthX);
//        System.out.println("XLEN=" + xLen / numCols);
