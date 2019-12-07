/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.pkg3;

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class KeyboardMouse extends Mouse {

    private JPanel drawPanel;

    public KeyboardMouse(Maze maze, int startRow, int startCol, JPanel drawPanel) {
        super(maze, 0, startRow, startCol);
        this.drawPanel = drawPanel;
        System.out.println("N=" + maze.isOpen(row, col, Direction.NORTH) + ", S=" + maze.isOpen(row, col, Direction.SOUTH) + ", E=" + maze.isOpen(row, col, Direction.EAST) + ", W=" + maze.isOpen(row, col, Direction.WEST));
    }

    @Override
    protected void move() {
        
    }



    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        if (!hasFoundExit()) {
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                if (maze.isOpen(row, col, Direction.NORTH)) {
                    row -= 1;
                }
            }

            if (key == KeyEvent.VK_A) {
                if (maze.isOpen(row, col, Direction.WEST)) {
                    col -= 1;
                }
            }

            if (key == KeyEvent.VK_D) {
                if (maze.isOpen(row, col, Direction.EAST)) {
                    col += 1;
                }
            }

            if (key == KeyEvent.VK_S) {
                if (maze.isOpen(row, col, Direction.SOUTH)) {
                    row += 1;
                }
            }
            System.out.println("R=" + maze.getNumRows() + ", C=" + maze.getNumCols() + " Current R=" + row + ", C=" + col);
            System.out.println("N=" + maze.isOpen(row, col, Direction.NORTH) + ", S=" + maze.isOpen(row, col, Direction.SOUTH) + ", E=" + maze.isOpen(row, col, Direction.EAST) + ", W=" + maze.isOpen(row, col, Direction.WEST));
        } else {
            maze.keyboardMouse = null;
        }
        drawPanel.repaint();
    }

    public static void main(String[] args) {

    }

}
