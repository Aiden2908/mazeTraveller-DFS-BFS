package assignment.pkg3;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class RandomMouse extends Mouse {

    private JPanel drawPanel;
    private int lastVisitedRow = -1, lastVisitedCol = -1;
    private final Random random = new Random();

    public RandomMouse(Maze maze, int startRow, int startCol, int delay, JPanel drawPanel) {
        super(maze, delay, startRow, startCol);
        this.drawPanel = drawPanel;
        System.out.println("N=" + maze.isOpen(row, col, Direction.NORTH) + ", S=" + maze.isOpen(row, col, Direction.SOUTH) + ", E=" + maze.isOpen(row, col, Direction.EAST) + ", W=" + maze.isOpen(row, col, Direction.WEST));
    }

    @Override
    protected void move() {
        if (!stopRequested) {
            if (!hasFoundExit()) {
                if (maze.isOpen(row, col, Direction.EAST)) {
                    System.out.println("Mv E");
                    col += 1;
                } else {
                    int result = random.nextInt(3);
                    System.out.println("Rand=" + result);
                    if (result == 0 && maze.isOpen(row, col, Direction.WEST) && lastVisitedCol != (col - 1)) {
                        System.out.println("Mv W");
                        col -= 1;
                    } else if (result == 1 && maze.isOpen(row, col, Direction.NORTH) && lastVisitedRow != (row - 1)) {
                        System.out.println("Mv N");
                        row -= 1;
                    } else if (result == 2 && maze.isOpen(row, col, Direction.SOUTH) && lastVisitedRow != (row - 1)) {
                        System.out.println("Mv S");
                        row += 1;
                    }
                }
            } else {
                stopRequested = true;
                maze.randomMouses = null;
            }
            lastVisitedCol = col;
            lastVisitedRow = row;
            drawPanel.repaint();
        }
    }

    @Override
    public void run() {
        while (!stopRequested) {
            move();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                // Logger.getLogger(RandomMouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
