/*
Project:    Program 01
File:       Cells.java
Class:      CS 335
Author:     Jared Rigdon
Date:       11/2/2018
 */

import java.awt.*;
import java.util.ArrayList;

public class Cells {

    private int row, col;
    private boolean visited, solVisited;
    private Color solColor;

    private ArrayList<Integer> xConnect = new ArrayList<>(4);
    private ArrayList<Integer> yConnect = new ArrayList<>(4);

    private boolean[] walls;

    public Cells() {
        super();
        walls = new boolean[4];
    }

    //setup 4 walls{north, south, east, west}
    public void createWall(){
        walls[0]=true;  //north
        walls[1]=true;  //east
        walls[2]=true;  //south
        walls[3]=true;  //west
    }

    //recieves an int to determine which wall to destroy
    public void destroyWall(int x, int y){
        if ((row > x)){         //west
            walls[3]=false;
        }
        else if ((row < x)){   //east
            walls[1]=false;
        }
        else if ((col > y)){    //north
            walls[0]=false;
        }
        else{                   //south
            walls[2]=false;
        }
    }

    //recieves an int and returns the value of the wall
    public boolean getWall(int index){
        if (index == 0){         //west
            return walls[0];
        }
        else if (index == 1){   //east
            return walls[1];
        }
        else if (index == 2){    //north
            return walls[2];
        }
        else{                   //south
            return walls[3];
        }
    }

    //adds the x and y to the the array
    public void setConnected(int i, int j){
       xConnect.add(i);
       yConnect.add(j);
    }

    //check if the passed in cell is connected to the current cell
    public boolean isConnected(int i, int j){
        for (int x = 0; x < xConnect.size();x++){
            for (int y = 0; y < yConnect.size(); y++){
                if ((i == xConnect.get(x)) && (j == yConnect.get(y))){
                    return true;
                }
            }
        }
        return false;
    }

    //assign the bool val of it has been visited
    public void setVisited(boolean set){visited=set;}
    //check if this cell has been visited in gen
    public boolean isVisited(){return visited;}

    //sets the solution color, default white, current/correct solution path = yellow, backtracked path = gray
    public void setSolColor(Color setColor){solColor = setColor;}
    //returns the solution color
    public Color getSolColor(){return solColor;}

    //assign the bool val of it has been visited
    public void setSolVisited(boolean set){solVisited=set;}
    //check if this cell has been visited in gen
    public boolean isSolVisited(){return solVisited;}

    //assigns and retrieves the row and col location
    public void setRowCol(int i, int j){row=i; col=j;}
}
