/*
Project:    Program 01
File:       mazeGen.java
Class:      CS 335
Author:     Jared Rigdon
Date:       11/2/2018

*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;
import java.awt.*;
import javax.swing.*;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.Timer;


public class mazeGen extends JPanel {
    private Cells cells[][];
    private int mazeRow, mazeCol;
    private ArrayList<Integer> unvistedNodesX = new ArrayList<>(4);
    private ArrayList<Integer> unvistedNodesY = new ArrayList<>(4);
    private int visited = 1;
    private int currX, currY,currSolX, currSolY;
    private double  perVisDouble, percentVis;
    private int defaultcell_width = 15;
    private int margin = 5;
    private boolean run;
    private int sleepTime = 100;

    //stack to store the nodes(could do points but oh well)
    private Stack<Integer> x = new Stack<>();
    private Stack<Integer> y = new Stack<>();
    private Stack<Integer> Sx = new Stack<>();
    private Stack<Integer> Sy = new Stack<>();


    public mazeGen(int Rows, int Cols) {
        cells = new Cells[Rows][Cols];
        mazeRow = Rows;
        mazeCol = Cols;

        //initialize a blank, empty array
        for (int i = 0; i < Rows; i++) {
            for (int j = 0; j < Cols; j++) {
                //set default values to the array
                Cells c = new Cells();
                c.setRowCol(i, j);
                c.setVisited(false);
                c.setSolVisited(false);
                c.setSolColor(Color.BLACK);    //set the solution color to white(same as background)
                c.createWall();
                //add to the cells array
                cells[i][j] = c;
            }
        }
        cells[0][0].setSolColor(Color.BLUE);
        cells[mazeRow-1][mazeCol-1].setSolColor(Color.RED);

       //Maze_Generation();
       //mazeSolver();
        revalidate();
        repaint();

    }


    //trying to just clear the thing and just adjust the sizes accordingly
    public void createNewCells(int Rows, int Cols){
        this.clearAll();
        cells = new Cells[Rows][Cols];
        mazeRow = Rows;
        mazeCol = Cols;

        //initialize a blank, empty array
        for (int i = 0; i < Rows; i++) {
            for (int j = 0; j < Cols; j++) {
                //set default values to the array
                Cells c = new Cells();
                c.setRowCol(i, j);
                c.setVisited(false);
                c.setSolVisited(false);
                c.setSolColor(Color.BLACK);    //set the solution color to white(same as background)
                c.createWall();
                //add to the cells array
                cells[i][j] = c;
            }
        }
        cells[0][0].setSolColor(Color.BLUE);
        cells[mazeRow-1][mazeCol-1].setSolColor(Color.RED);

        //Maze_Generation();
        //mazeSolver();
        revalidate();
        repaint();

    }

    public void setSleepTime(int passedSleep){ sleepTime = passedSleep;}


    public void Maze_Generation(boolean isAnimate) {


        //initial start position and conditions
        currX = 0;
        currY = 0;
        cells[0][0].setVisited(true);
        int totalSize = mazeRow * mazeCol;
        if (isAnimate){
            final Timer timer = new Timer(sleepTime, null);
            timer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DFS_Gen();  //should iterate through the code once
                    if(visited >= totalSize){timer.stop();} //kinda the same as the while loop

                }
            });
            timer.setRepeats(true);
            timer.start();
        }
        else{
            while (visited < totalSize) {
            DFS_Gen();
            }
        }


        visited = 1;
        x = new Stack<>();  //dump when finished
        y = new Stack<>();
    }

    //does one iteration
    public void DFS_Gen(){
        //starting node is 0,0 and ending is mazeRow,mazeCol
        //if there is a neighbor that has not been visited
        if (notVisited(currX, currY, mazeRow - 1, mazeCol - 1, 1)) {
            //pick a random node to visit
            int randNode = (int) (Math.random() * unvistedNodesY.size());

            //push the curr cell into the stack
            x.push(currX);
            y.push(currY);

            //remove the wall between the curr cell and the chosen cell
            cells[currX][currY].setConnected(unvistedNodesX.get(randNode), unvistedNodesY.get(randNode));
            cells[currX][currY].destroyWall(unvistedNodesX.get(randNode), unvistedNodesY.get(randNode));
            cells[unvistedNodesX.get(randNode)][unvistedNodesY.get(randNode)].setConnected(currX, currY);
            cells[unvistedNodesX.get(randNode)][unvistedNodesY.get(randNode)].destroyWall(currX, currY);

            //make the chosen cell the curr cell, mark as visited, and reset the arraylists
            currX = unvistedNodesX.get(randNode);
            currY = unvistedNodesY.get(randNode);
            cells[currX][currY].setVisited(true);
            resetArrayList();

            //increase the visited count
            visited++;
        } else {
            //pop a cell from the stack and make it the curr cell
            currX = x.pop();
            currY = y.pop();
        }
        resetArrayList();
        revalidate();
        repaint();
    }

    public void resetArrayList() {
        unvistedNodesX.clear();
        unvistedNodesY.clear();
        unvistedNodesX = new ArrayList<>(4);
        unvistedNodesY = new ArrayList<>(4);
    }


    //looks for any adjacient nodes that have not been visited and adds them to an arraylist. Then returns false if empty
    public boolean notVisited(int i, int j, int xmax, int ymax, int Type) {
        //use the type to determine if gen or sol
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if ((i + x >= 0) && (j + y >= 0) && (i + x <= xmax) && (j + y <= ymax)) {
                    if ((x == 0 && y != 0) || (y == 0 && x != 0)) {
                        if (Type == 1){
                            if (!cells[i + x][j + y].isVisited()) {
                                unvistedNodesX.add(i + x);
                                unvistedNodesY.add(j + y);
                            }
                        }
                        else{
                            //check if the solver has no visited the cell and it is connected to the current cell
                            if (!cells[i + x][j + y].isSolVisited() && (cells[currSolX][currSolY].isConnected(i+x, j+y))) {
                                unvistedNodesX.add(i + x);
                                unvistedNodesY.add(j + y);
                            }
                        }
                    }
                }
            }
        }
        unvistedNodesX.trimToSize();
        unvistedNodesY.trimToSize();
        if (unvistedNodesX.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    //maze solver
    public void mazeSolver(boolean isAnimate){
        currSolX = 0;
        currSolY = 0;

        int totalSize = mazeRow * mazeCol;

        //run the dfs solver until we reach the endpoint
        if (isAnimate){
            final Timer timer = new Timer(sleepTime, null);
            timer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cells[0][0].setSolColor(Color.BLUE);
                    cells[mazeRow-1][mazeCol-1].setSolColor(Color.RED);
                    DFS_Sol();  //should iterate through the code once
                    if((currSolX == (mazeRow - 1)) && (currSolY == (mazeCol - 1))){timer.stop();} //kinda the same as the while loop

                }
            });
            timer.setRepeats(true);
            timer.start();
        }
        else{
            while((currSolX != (mazeRow - 1)) || (currSolY != (mazeCol - 1))){
                DFS_Sol();
            }
        }
        cells[0][0].setSolColor(Color.BLUE);
        cells[mazeRow-1][mazeCol-1].setSolColor(Color.RED);
        Sx = new Stack<>(); //dump when empty
        Sy = new Stack<>();
    }

    public void DFS_Sol(){
        if (notVisited(currSolX, currSolY, mazeRow - 1, mazeCol - 1, 2)){
            int randNode = (int) (Math.random() * unvistedNodesY.size());

            Sx.push(currSolX);
            Sy.push(currSolY);

            cells[currSolX][currSolY].setSolColor(Color.YELLOW);
            currSolX = unvistedNodesX.get(randNode);
            currSolY = unvistedNodesY.get(randNode);
            cells[currSolX][currSolY].setSolVisited(true);

            resetArrayList();
        }
        else{
            cells[currSolX][currSolY].setSolColor(Color.GRAY);

            currSolX = Sx.pop();
            currSolY = Sy.pop();
            //before popping from the stack, set the currSol as gray
        }
        percentVis++;
        perVisDouble = (percentVis)/(mazeRow*mazeCol);

        resetArrayList();
        revalidate();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        setBackground(Color.BLACK);
        this.draw(g2);
        this.drawSol(g2);
        this.drawPercent(g2);
        g2.dispose();
    }

    /*
    So first should be to draw the panel with all walls intact, this can be done by determining the cell width and
     */
    //use this to draw the current route
    //pass in current cell and the next cell route
    public void drawSol(Graphics2D g){
        int cell_width;
        if(mazeRow > 40 || mazeCol > 40){
            cell_width = defaultcell_width/2;
        }
        else{
            cell_width = defaultcell_width;
        }
        //do a for loop that does the same as draw by in the solver: assign 1 var, a color. default is white(same as background)
        //then in the solver, change accouringly. then if animation, then call repaint after the change
        //basically use a for loop to create a square that is just 1 pixel smaller than the walls and fill the color of the thing
        for (int i = 0; i < (mazeRow); i++) {
            for (int j = 0; j < (mazeCol); j++) {
                g.setColor(cells[i][j].getSolColor());  //set the color

                g.fillRect(((j*cell_width))+margin+2,((i*cell_width))+margin+2,cell_width-3, cell_width-3);
            }
        }
    }

    public void draw(Graphics2D g) {
        int cell_width;
        if(mazeRow > 40 || mazeCol > 40){
            cell_width = defaultcell_width/2;
        }
        else{
            cell_width = defaultcell_width;
        }
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.WHITE);

        //sooooo... I accidentally got the rows and cols mixed up with the x's and y's. Just gonna flip these and it should all work out ok
        //draw the walls
        //could do a method that baically paints over the inital maze
        for (int i = 0; i < (mazeRow); i++) {
            for (int j = 0; j < (mazeCol); j++) {
                if (cells[i][j].getWall(0)) {    //north
                    g.drawLine(j * cell_width + margin, i * cell_width + margin, j * cell_width + margin, (i + 1) * cell_width + margin);
                }
                if(cells[i][j].getWall(1)){     //east
                    g.drawLine(j*cell_width+margin, (i+1)*cell_width+margin, (j+1)*cell_width+margin, (i+1)*cell_width+margin);
                }
                if(cells[i][j].getWall(2)){     //south
                    g.drawLine((j+1)*cell_width+margin, i*cell_width+margin, (j+1)*cell_width+margin, (i+1)*cell_width+margin);
                }
                if(cells[i][j].getWall(3)){     //west
                    g.drawLine(j*cell_width+margin, i*cell_width+margin, (j+1)*cell_width+margin, i*cell_width+margin);
                }
            }
        }
    }

    public void drawPercent(Graphics2D g){
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.WHITE);
        g.drawString("Percent Visited: "+Double.toString(perVisDouble), 10,650);
    }

    public void clearAll(){
        this.removeAll();
        setBackground(Color.WHITE);
        setOpaque(true);
        x = new Stack<>();  //dump when finished
        y = new Stack<>();
        Sx = new Stack<>(); //dump when empty
        Sy = new Stack<>();
        percentVis = 0;
    }
}
