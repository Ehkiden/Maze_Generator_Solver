/*
Project:    Program 01
File:       Maze.java
Class:      CS 335
Author:     Jared Rigdon
Date:       11/2/2018

What doesnt work: the active speed slider, the stop button
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Maze extends JFrame {
    private mazeGen board;
    //private mazePanel panel;

    private JPanel boardView, sideView, genView, solView;
    private JScrollPane scrollPane;
    private Container c = getContentPane();

    private JButton solButt, genButt;

    private int row, col;
    public boolean genAnimation, solAnimation;
    private int currSpeed = 100;

    public Maze() {
        super("Maze Thing");

        //call the new panels
        sideView = new JPanel();
        boardView = new JPanel();
        genView = new JPanel();
        solView = new JPanel();

        //define gridlayout
        c.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        sideView.setLayout(new GridLayout(9,1));
        genView.setLayout(new GridLayout(1,2));
        solView.setLayout(new GridLayout(1,2));


        //solView
        solButt = new JButton("Solve");
        solButt.setEnabled(false);
        solButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solButt.setEnabled(false);
                if (solAnimation){
                    genButt.setEnabled(false);
                }
                board.mazeSolver(solAnimation);
            }
        });
        JCheckBox solCheckbox = new JCheckBox("Show Solver");
        solCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (solAnimation){
                    solAnimation=false;
                }
                else{
                    solAnimation=true;
                }
            }
        });
        solView.add(solButt);
        solView.add(solCheckbox);
        sideView.add(solView);

        //define the labels and sliders for the sideView
        //genView
        genButt = new JButton("Generate");
        genButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.createNewCells(row, col);
                //resetBoard();   //reset the board with the new constraints
                genButt.setEnabled(false);
                board.Maze_Generation(genAnimation);
                genButt.setEnabled(true);
                solButt.setEnabled(true);
            }
        });
        JCheckBox genCheckbox = new JCheckBox("Show Generation");
        genCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(genAnimation){
                    genAnimation=false;
                }
                else {
                    genAnimation=true;

                }
            }
        });
        genView.add(genButt);
        genView.add(genCheckbox);
        sideView.add(genView);

        //sliders and their labels
        //speed
        JLabel speedText = new JLabel("Speed: 100", JLabel.CENTER);
        JSlider speedSlide = new JSlider(JSlider.HORIZONTAL, 1,100,100);
        speedSlide.setMinorTickSpacing(10);
        speedSlide.setPaintTicks(true);

        speedSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currSpeed = ((JSlider)e.getSource()).getValue();
                speedText.setText("Speed: "+currSpeed);
                board.setSleepTime(1000/currSpeed);   //have this be updated automatically
            }
        });

        sideView.add(speedText);
        sideView.add(speedSlide);

        //rows
        JLabel rowText = new JLabel("Rows: 60", JLabel.CENTER);
        JSlider rowSlide = new JSlider(JSlider.HORIZONTAL, 10,60,60);
        rowSlide.setMinorTickSpacing(10);
        rowSlide.setPaintTicks(true);

        rowSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                row = ((JSlider)e.getSource()).getValue();
                rowText.setText("Rows: "+((JSlider)e.getSource()).getValue());
            }
        });

        sideView.add(rowText);
        sideView.add(rowSlide);

        //cols
        JLabel colText = new JLabel("Columns: 60", JLabel.CENTER);
        JSlider colSlide = new JSlider(JSlider.HORIZONTAL, 10,60,60);
        colSlide.setMinorTickSpacing(10);
        colSlide.setPaintTicks(true);

        colSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                col = ((JSlider)e.getSource()).getValue();
                colText.setText("Columns: "+((JSlider)e.getSource()).getValue());
            }
        });

        sideView.add(colText);
        sideView.add(colSlide);

        //stop button
        JButton stopButt = new JButton("Stop");
        sideView.add(stopButt);



        //****testing
        setupBoard(row, col);
        scrollPane = new JScrollPane(board);

        gc.gridx=0;
        gc.gridy=0;
        gc.weightx=0.9;
        gc.weighty=0;
        gc.fill=GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        c.add(scrollPane, gc);


        sideView.setPreferredSize(new Dimension(300,700));
        gc.gridx=1;
        gc.gridy=0;
        gc.weightx=0.1;
        gc.weighty=0;
        gc.fill=GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        c.add(sideView, gc);


        setSize(1000, 800);
        setVisible(true);
    }

    public void setupBoard(int rows, int cols){
        if (rows > 0){
            board = new mazeGen(rows, cols);
        }
        else{
            //assigns the size and bombCount to prevent issues
            row = 30;
            col = 30;
            board = new mazeGen(row, col);
        }
        board.revalidate();
        board.repaint();
    }

    public static void main(String args[]){
        Maze M = new Maze();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}