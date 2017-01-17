/*
 * TCSS 305 Autumn 2015
 * Assignment 6 - Tetris
 * 
 */


package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Dmitriy Onishchenko 
 * @version 11 December 2015
 *
 */
public class TetrisMenuBar extends JMenuBar {
    
    /**
     * Property Change for grid status.
     */
    public static final String PROPERTY_GRID = "Grid";
    
    /**
     * Property Change for board size.
     */
    public static final String PROPERTY_BOARDSIZE = "Size";
    
    /**
     * Property Change different level selected.
     */
    public static final String PROPERTY_LEVEL = "Level Selected";
    
    /**
     * Generated version ID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Playing board default width.
     */
    private static final int DEFAULT_BOARD_WIDTH = 10;
    
    /**
     * Playing board default height.
     */
    private static final int DEFAULT_BOARD_HEIGHT = 20; 
    
    /**
     * The dimemsion of the game board.
     */
    private final Dimension myBoardDimensions;
    

    /**
     * The spinner for difficulty level controller.
     */
    private final JSpinner mySpinner;
    
    /**
     * The JFrame associated with the menu bar.
     */
    private final JFrame myFrame;
    
    /**
     * The new game menu item.
     */
    private final JMenuItem myNewGame;

    /** 
     * The end game menu item.
     */
    private final JMenuItem myEndGame;
    
    /**
     * The value of the spinner (level).
     */
    private int myLevel;
    

    
    /** 
     * Constructor initialize fields.
     * 
     * @param theFrame the JFrame associated with this Menu bar
     */
    public TetrisMenuBar(final JFrame theFrame) {
        super();
        
        myFrame = theFrame;        
        mySpinner = new JSpinner();          
        myBoardDimensions = new Dimension(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT);        
        myNewGame = new JMenuItem("New Game");
        myEndGame = new JMenuItem("End Game");
        
        buildMenuBar();        
    }
    
    /**
     * Method that returns the New Game Menu Item 
     * so that we could add an action listener.
     * 
     * @return myNewGame JMenuItem
     */
    public JMenuItem getNewGame() {
        return myNewGame;
    }
    
    /**
     * Method that returns the End Game Menu Item 
     * so that we could add an action listener.
     * 
     * @return myEndGame JMenuItem
     */
    public JMenuItem getEndGame() {
        return myEndGame;
    }
    
    ////////////////////////////////// helper methods //////////////////////////////////////

    /**
     * Method that adds menus items to menuBar.
     */
    private void buildMenuBar() {
        
        add(buildGameMenu());
        add(buildViewMenu()); 
        add(buildLevelMenu());
        add(buildHelpMenu()); 
        
    }
    
    /**
     * Helper method that builds the game menu.
     * 
     * @return JMenu the Game menu.
     */
    private JMenu buildGameMenu() {
        
        final JMenu game = new JMenu("Game"); 
        final JMenuItem exit = new JMenuItem("Exit");        
        
        // set mnemonics
        game.setMnemonic(KeyEvent.VK_G);
        myNewGame.setMnemonic(KeyEvent.VK_N);
        myEndGame.setMnemonic(KeyEvent.VK_E);
        exit.setMnemonic(KeyEvent.VK_X);
        
        // add action
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));

            }
        });
        
        
        game.add(myNewGame);
        game.addSeparator();
        game.add(myEndGame);
        game.addSeparator();
        game.add(exit);
        
        return game;        
    }
    
    /**
     * Helper method that builds the view menu.
     * 
     * @return JMenu the view menu
     */
    private JMenu buildViewMenu() {
        
        final JMenu view = new JMenu("View");
        final JCheckBoxMenuItem grid = new JCheckBoxMenuItem("Show Grid");   
        
        // set mnemonics
        view.setMnemonic(KeyEvent.VK_V);
        grid.setMnemonic(KeyEvent.VK_G);
        
        grid.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {

                final JCheckBoxMenuItem grid = (JCheckBoxMenuItem) theEvent.getSource();

                // enable grid status                
                firePropertyChange(PROPERTY_GRID, !grid.isSelected(), grid.isSelected());
            }
        });       
        
        view.add(grid);
        view.addSeparator();
        view.add(buildBoardSizeSub());
        
        
        return view;
    }
    
    /**
     * Helper method that creates a sub menu for the board size
     * selection.
     * 
     * @return JMenu board size menu
     */
    private JMenu buildBoardSizeSub() {
        
        final JMenu boardSize = new JMenu("Board Size");
        final ButtonGroup group = new ButtonGroup();        
       
        final JRadioButtonMenuItem r1 = new 
                        JRadioButtonMenuItem("10 X 20");        
        final JRadioButtonMenuItem r2 = new 
                        JRadioButtonMenuItem("15 X 30");
        final JRadioButtonMenuItem r3 = new 
                        JRadioButtonMenuItem("20 X 40");


        r1.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {                
                
                final Dimension newSize = new Dimension(10, 20);               
                
                firePropertyChange(PROPERTY_BOARDSIZE, myBoardDimensions, newSize);
                myBoardDimensions.setSize(newSize);
                
            }
        });
        
        r2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                final Dimension newSize = new Dimension(15, 30);               
                
                firePropertyChange(PROPERTY_BOARDSIZE, myBoardDimensions, newSize);
                myBoardDimensions.setSize(newSize);
 
            }
        });
        
        r3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                final Dimension newSize = new Dimension(20, 40);               
                
                firePropertyChange(PROPERTY_BOARDSIZE, myBoardDimensions, newSize);
                myBoardDimensions.setSize(newSize);
                
            }
        });       
        
        // add the buttons to a button group.
        group.add(r1);
        group.add(r2);
        group.add(r3);
        
        
        boardSize.add(r1);
        boardSize.add(r2);
        boardSize.add(r3);
        
        r1.setSelected(true);
        
        return boardSize;
        
    }
    
    
    /**
     * Helper method to build the difficulty level chooser menu.
     * 
     * @return JMenu the difficulty menu.
     */
    private JMenu buildLevelMenu() {
        
        final JMenu level = new JMenu("Difficulty");
        final JMenu difficulty = new JMenu("Choose Level");
        
        final SpinnerModel model = new SpinnerNumberModel(myLevel, 0, 20, 1);
        mySpinner.setModel(model);      
        
        mySpinner.addChangeListener(new ChangeListener() {           
            
            @Override
            public void stateChanged(final ChangeEvent theEvent) {               
                
                final JSpinner s = (JSpinner) theEvent.getSource();              
               
                
                firePropertyChange(PROPERTY_LEVEL, myLevel, s.getValue()); 
                myLevel = (int) s.getValue();
                
            }
        });
       
        
        difficulty.add(mySpinner);        
        level.add(difficulty);
        
        return level;
    }
    
    /**
     * Helper method that builds the about menu.
     * 
     * @return JMenu the about menu.
     */
    private JMenu buildHelpMenu() {
        
        final JMenu help = new JMenu("Help");
        final JMenuItem about = new JMenuItem("About");
        
        // set mnemonics
        help.setMnemonic(KeyEvent.VK_H);
        about.setMnemonic(KeyEvent.VK_A);
        
        about.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {

                JOptionPane.showMessageDialog(myFrame, "The objective of Tetris is "
                                + "\nto complete full solid lines "
                                + "\n(no gaps). When you make a "
                                + "\nline it disappears and all the "
                                + "\nblocks shift accordingly. "
                                + "\nIf you let the blocks reach the "
                                + "\ntop of the Tetris board, game ends. "
                                + "\nFor each piece you get 10*level points. "
                                + "\nFor one line you get 50*level points. "
                                + "\nFor two lines you get 150*level points. "
                                + "\nFor three lines you get 250*level points. "
                                + "\nFor four lines you get 1000*level points."
                                + "\nLevel advances after 8 cleared lines.");
            }
        });
        
        help.add(about);
        
        return help;
    }    
}