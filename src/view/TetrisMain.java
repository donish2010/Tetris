/*
 * TCSS 305 Autumn 2015
 * Assignment 6 - Tetris
 * 
 */

package view;

/**
 * Class that runs the tetris game with main method.
 * 
 * @author Dmitriy Onishchenko
 * @version 2 December 2015
 *
 */
public final class TetrisMain {
    
    /**
     * Make sure you cannot instantiate this class.
     */
    private TetrisMain() {
        // do nothingm
    }

    /**
     * Main method that instantiates a TetrisGUI and 
     * calls its set up components method.
     * 
     * @param theArg the string array
     */
    public static void main(final String[] theArg) {

        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                final TetrisGUI tetrisGame = new TetrisGUI();
                tetrisGame.setUpComponents();
            }
        });
    }
}
