/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.pkg3;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public abstract class Mouse implements Runnable {

    protected int row;
    protected int col;
    protected Maze maze;
    protected boolean stopRequested;
    protected int delay;
    private ArrayList<Ellipse2D.Double> trail=new ArrayList<>();
    //private ArrayList<g2.draw(new Rectangle2D.Double(xPoint, yPoint, lineThickness, lineLengthY));

    //  Mouse(maze:Maze, delay:int, startRow:int, startCol:int)+ getRow() : int + getCol() : int # move():void + run() : void + requestStop():void + drawMouse(g:Graphics, width:int,height,int):void;
    public Mouse(Maze maze, int delay, int startRow, int startCol) {
        this.maze = maze;
        this.delay = delay;
        this.row = startRow;
        this.col = startCol;
    }

    protected abstract void move();

    public int getRow() {
        return row;
    }

    protected boolean hasFoundExit() {
        if (col == maze.getNumCols() && maze.isOpen(row, col, Direction.EAST)) {
            System.out.println("EXITED MAZE");
            stopRequested=true;
            return true;
        }
        return false;
    }

    public int getCol() {
        return col;
    }

    @Override
    public void run() {
        if (!stopRequested) {
            move();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                //Logger.getLogger(Mouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class trailData{
        
    }
    public void drawMouse(Graphics2D g, double marginX, double row, double col, double lineLengthY, Color color) {
        row += 1;
        col += 1;
        g.setColor(color);
        g.fill(new Ellipse2D.Double((marginX + (col * lineLengthY) - (lineLengthY / 2)) - (lineLengthY/3)/2, (marginX + (row * lineLengthY) - (lineLengthY / 2)) - (lineLengthY/3)/2, lineLengthY/3, lineLengthY/3));
        trail.add(new Ellipse2D.Double((marginX + (col * lineLengthY) - (lineLengthY / 2)) - (lineLengthY/3)/2, (marginX + (row * lineLengthY) - (lineLengthY / 2)) - (lineLengthY/3)/2, lineLengthY/3, lineLengthY/3));
     
        for(int i =0;i<trail.size();i++){
            g.fill(trail.get(i));
        }
        
    }

}
