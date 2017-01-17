/*
 * TCSS 305 Autumn 2015
 * Assignment 6 - Tetris
 * 
 */


package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import model.Board;
import sound.SoundPlayer;
import sun.applet.Main;

/**
 * The Gui for the tetris game.
 * 
 * @author Dmitriy Onishchenko
 * @version 2 December 2015
 *
 */
public class TetrisGUI extends JPanel implements Observer, PropertyChangeListener {
    
    
    /**
     * The game over song location.
     */
    private static final String SONG = "/game-over.mp3";
    
    /**
     * The level-up sound location.
     */
    private static final String LEVEL_UP = "/level-up.mp3";
    
    /**
     * The clear-line sound location.
     */
    private static final String CLEAR_LINE = "/clear-line.mp3";
    
    /**
     * The piece sound location.
     */
    private static final String PIECE = "/piece.mp3";
    
    
    
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * The number to divide initial delay for new delay.
     */
    private static final float TIMER_ADJUST = 1.17F;
    
    /**
     * Initial timer delay in milliseconds.
     */
    private static final int INITIAL_DELAY = 1000;
    
    /**
     * The border width for the borders.
     */
    private static final int BORDER_WIDTH = 5;
    
    /**
     * The RGB number for background color.
     */
    private static final int RGB = 92;
    
    /**
     * Playing board default width.
     */
    private static final int DEFAULT_BOARD_WIDTH = 10;
    
    /**
     * Playing board default height.
     */
    private static final int DEFAULT_BOARD_HEIGHT = 20;    
    
    /**
     * The class for playing sounds and songs.
     */
    private final SoundPlayer mySoundPlayer;    
    
    /**
     * The Tetris game board.
     */
    private final Board myBoard;
    
    /**
     * The timer for the game.
     */
    private final Timer myTimer;
    
    /**
     * The playing panel for the game.
     */
    private final PlayingPanel myPlayingPanel;
    
    /**
     * The next piece panel for the game.
     */
    private final NextPiecePanel myNextPiecePanel;
    
    /**
     * The statistics panel for the game.
     */
    private final StatisticsPanel myStatsPanel;
    
    /**
     * The current level of the game.
     */
    private int myLevel;  
    
    /**
     * A flag to mute the sound.
     */
    private boolean myMute;
    
    /**
     * Game paused status.
     */
    private Boolean myGamePaused = false;
    
    /**
     * Game ended flag.
     */
    private Boolean myGameEnded = true;
    
    /**
     * The width of the game board.
     */
    private int myBoardWidth = DEFAULT_BOARD_WIDTH;
    
    /**
     * The height of the game board.
     */
    private int myBoardHeight = DEFAULT_BOARD_HEIGHT;
    
    /**
     * The current delay of the Timer.
     */
    private float myDelay = INITIAL_DELAY;   
    
    
    
    
    /**
     * Constructor initialize fields.
     */
    public TetrisGUI() {
        super();   
        
        myBoard = new Board(myBoardWidth, myBoardHeight, null);       
        myPlayingPanel = new PlayingPanel();       
             
        myTimer = new Timer(INITIAL_DELAY, null);     
        myNextPiecePanel = new NextPiecePanel(myTimer); 
        
        myStatsPanel = new StatisticsPanel();        
        mySoundPlayer = new SoundPlayer();        

    }
    
    /**
     * Method that sets everything up for this GUI including
     * adding things to screen and adding observers.
     * 
     */
    public void setUpComponents() {
        
        // first set up pre-load the sounds
        setUpSound();
        
        // create a panel to act as a border.
        final JPanel border = new JPanel();        
        border.add(myPlayingPanel);
        border.setBackground(Color.BLACK);
        
        add(border);
        add(createRightPanel()); 
        
        setUpTimer();
        addObserversPCListener();
        createJFrame();        
    }
    

    
    /**
     * 
     * {@inheritDoc}. 
     * 
     * If game is over prompt a message asking user if they would 
     * like to play a new game.
     */
    @Override
    public void update(final Observable theObserver, final Object theObject) {
        
        // when game ends
        if (myBoard.isGameOver()) { 

            final URL gameoverUrl = Main.class.getResource("/gameover.png");
            final Icon img = new ImageIcon(gameoverUrl);
            
            if (!myMute) {
                
                mySoundPlayer.play(SONG);
            }
            
            
            final int newGame = JOptionPane.showConfirmDialog(this, 
                                                        "", "Game Over!", 
                                                        JOptionPane.YES_NO_OPTION, 
                                                        JOptionPane.INFORMATION_MESSAGE, img);
            
                       
            if (newGame == JOptionPane.YES_OPTION) {
                
                myBoard.newGame(myBoardWidth,  myBoardHeight, null);
                myPlayingPanel.setMessageVisible(false);                
                myStatsPanel.resetScoringPanel(0);
                myTimer.setDelay(INITIAL_DELAY);                 
                myTimer.restart(); 
                mySoundPlayer.stopAll();
                
            } 
            
            myLevel = 0;
            myDelay = INITIAL_DELAY; 
        }
    }  
       
    
    /**
     *  
     * {@inheritDoc}.
     * 
     * When certain properties change update what needs to be updated.
     * 
     * @param theEvent the property change event
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
       
        
        if (TetrisMenuBar.PROPERTY_GRID.equals(theEvent.getPropertyName())) {    
                        
            myPlayingPanel.enableGrid((Boolean) theEvent.getNewValue());
            myPlayingPanel.repaint();
            
        } else if (TetrisMenuBar.PROPERTY_BOARDSIZE.equals(theEvent.getPropertyName())) {
            controlBoardSize(theEvent);          
            
        } else if (TetrisMenuBar.PROPERTY_LEVEL.equals(theEvent.getPropertyName())) {
            difficultyLevelAdjust(theEvent);            
            
        } else if (StatisticsPanel.PROPERTY_NEXT_LEVEL.equals(theEvent.getPropertyName())) {
            advanceLevel();        
            
        } else if (StatisticsPanel.PROPERTY_PIECE_DOWN.equals(theEvent.getPropertyName())) {
            
            if (!myMute) {
                mySoundPlayer.play(PIECE);
            }
            
        } else if (StatisticsPanel.PROPERTY_CLEAR_LINE.equals(theEvent.getPropertyName())) {
            
            if (!myMute) {                
                mySoundPlayer.play(CLEAR_LINE);
            }          
        }
    }
    
    /////////////////////////////////private helper methods////////////////////////////////////
    
    /**
     * Helper method for new board size.
     * 
     * @param theEvent the event
     */
    private void controlBoardSize(final PropertyChangeEvent theEvent) {
        
        final Dimension size = (Dimension) theEvent.getNewValue();
        
        myBoardWidth = (int) size.getWidth();
        myBoardHeight = (int) size.getHeight();
        
        if (myTimer.isRunning() && !myBoard.isGameOver()) {
            myBoard.newGame(myBoardWidth,  myBoardHeight, null);               
            myTimer.restart();

        }
    }
    
    /**
     * Helper method for adjusting the difficulty level.
     * 
     * @param theEvent the event changed
     */
    private void difficultyLevelAdjust(final PropertyChangeEvent theEvent) {
        
        if (myBoard.isGameOver() || myGameEnded) {
            
            final int level = (int) theEvent.getNewValue();                 
            myLevel = level;
            
            myDelay = INITIAL_DELAY;
            
            for (int i = 0; i < level; i++) {
                
                myDelay = myDelay / TIMER_ADJUST;                    
            }
            
            myTimer.setDelay((int) myDelay);
            myStatsPanel.setLevel(level);
        }        
    }
    
    /**
     * Helper method to adjust delay and play sound 
     * when level advances.
     */
    private void advanceLevel() {
        
        if (!myMute) {
            mySoundPlayer.play(LEVEL_UP);
        }
        
        myDelay = myDelay / TIMER_ADJUST;            
        myTimer.setDelay((int) myDelay);       
    }
    
    /**
     * A method that pre-loads the sound clips for faster play-back.
     */
    private void setUpSound() {
//
        mySoundPlayer.preLoad(SONG);
        mySoundPlayer.preLoad(LEVEL_UP);
        mySoundPlayer.preLoad(PIECE);
        mySoundPlayer.preLoad(CLEAR_LINE);    

    }
    
    
    /**
     * Helper method that sets up the timer action.
     */
    private void setUpTimer() {
       
        
        myTimer.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
               
                if (myBoard.isGameOver() || myGamePaused || myGameEnded) {
                    myTimer.stop();
                    
                } else {
                    myBoard.step();  
                    
                }
            }
        });   
        
        
        myTimer.setInitialDelay(0);
    }
    
    /**
     * Helper method to add observers to the playing board.
     */
    private void addObserversPCListener() {
        myBoard.addObserver(myPlayingPanel);
        myBoard.addObserver(myNextPiecePanel);
        myBoard.addObserver(myStatsPanel);
        myBoard.addObserver(this);   
        myStatsPanel.addPropertyChangeListener(this);
   
    }    
   
    
    /**
     * Helper method that builds the right panel.
     * @return JPanel the right panel
     */
    private JPanel createRightPanel() {
        
        final JPanel rightPanel = new JPanel(new BorderLayout());
        
        rightPanel.add(createNextPiecePanel(), BorderLayout.NORTH);       
        rightPanel.add(myStatsPanel, BorderLayout.CENTER);       
        rightPanel.add(createControls(), BorderLayout.SOUTH); 
        
        return rightPanel;
    }
    
    /**
     * Helper method to create the next piece panel with text.
     * 
     * @return JPanel the next piece panel.
     */
    private JPanel createNextPiecePanel() {
        
        final int textSize = 35;
        
        final JPanel next = new JPanel(new BorderLayout());
        final JLabel text = new JLabel("  Next");
        
        text.setOpaque(true);
        text.setBackground(Color.LIGHT_GRAY);
        text.setFont(new Font(Font.SERIF, Font.BOLD, textSize));

        next.add(text, BorderLayout.NORTH);
        next.add(myNextPiecePanel, BorderLayout.CENTER);
        
        next.setBorder(BorderFactory.createLineBorder(Color.BLACK, BORDER_WIDTH));
        
        return next;
    }
    
    
    /**
     * Helper method that creates a JLabel which
     * specifies what keys to use.
     * 
     * @return JLabel the controls description
     */
    private JLabel createControls() {
        
        final int textSize = 18;
        final int height = 224;
        
        final JLabel controls = new JLabel();
        
        controls.setText("<html><font color=#D2D2D2>"
                        + "Move left :&nbsp&nbsp&nbsp&nbsp Left <br>"
                        + "Move Right :&nbsp Right <br>"
                        + "Rotate :&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp Up <br> "
                        + "Move Down :&nbsp Down  <br>"
                        + "Hard Drop :&nbsp&nbsp&nbsp SpaceBar <br>"
                        + "Pause Game :&nbsp P <br>"
                        + "Mute Sound :&nbsp M </font></html>");
        
        controls.setHorizontalAlignment(JLabel.CENTER);        
        controls.setFont(new Font(Font.SERIF, Font.BOLD, textSize));
        controls.setOpaque(true);
        controls.setBorder(BorderFactory.createLineBorder(Color.BLACK, BORDER_WIDTH));
        
        controls.setPreferredSize(new Dimension(myNextPiecePanel.getWidth(), height));        
        controls.setBackground(new Color(RGB, RGB, RGB));
        
        return controls;
    }
    
    
    /**
     * Method that creates the JFrame and sets its content.
     */
    private void createJFrame() {
        
        final JFrame window = new JFrame();
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(this);        

        window.setJMenuBar(createMenuBar(window)); // helper method
        window.pack();
        window.setVisible(true);
        
        window.addKeyListener(new KeyListenerEvent());
     
        // make sure window not resize-able     
        window.setResizable(false);
        
    }
    
    
    /**
     * Helper method that creates and returns a JMenuBar.
     * 
     * @param theFrame the JFrame
     * @return JMenuBar the menu bar
     */
    private JMenuBar createMenuBar(final JFrame theFrame) {
        
        final TetrisMenuBar menuBar = new TetrisMenuBar(theFrame);

        // add the menu listener for all menus in menu bar
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            menuBar.getMenu(i).addMenuListener(new MenuListeners());
        }

        menuBar.getNewGame().addActionListener(new NewEndGameAction("New Game"));
        menuBar.getEndGame().addActionListener(new NewEndGameAction("End Game"));
        
        menuBar.addPropertyChangeListener(this);
        
        return menuBar;        
    }
    
    /**
     * Toggle the paused status.
     */
    private void togglePaused() {
        
        if (myGamePaused) {
            myGamePaused = false;            
            myPlayingPanel.setMessageVisible(false);
            myTimer.start();
            
        } else {
            myGamePaused = true;
            myPlayingPanel.setMessageVisible(true, "Game Paused");            
        }
    }
    
    
    
    
    
    ///////////////////////////////// inner classes /////////////////////////////////////
    
    

    /**
     * Keyboard listener class, adds key listeners to the game
     * for control.
     * 
     * @author Dmitriy Onishchenko
     * @version 1 December 2015
     *
     */
    private class KeyListenerEvent extends KeyAdapter { 
        
        
        /**
         * {@inheritDoc}.
         */
        @Override
        public void keyPressed(final KeyEvent theEvent) {            
            
            // get key code
            final int keyCode = theEvent.getKeyCode();                
            
            // if game is ended disable keys
            if (!myGameEnded) {                 
                disableEnableKeys(keyCode);          
            }
            
            // the mute button
            muteButton(keyCode);
            
            
        }
        
        /**
         * Helper method for key events.
         * 
         * @param theKeyCode the key code
         */
        private void disableEnableKeys(final int theKeyCode) {
            
            // if game is paused disable needed keys
            if (!myBoard.isGameOver() && !myGamePaused) {
                
                if (theKeyCode == KeyEvent.VK_UP) {
                    myBoard.rotate();                        
                    
                } else if (theKeyCode == KeyEvent.VK_DOWN) {                        
                    myBoard.moveDown();
                    
                } else if (theKeyCode == KeyEvent.VK_RIGHT) {                        
                    myBoard.moveRight();
                    
                } else if (theKeyCode == KeyEvent.VK_LEFT) {                        
                    myBoard.moveLeft();
                    
                } else if (theKeyCode == KeyEvent.VK_SPACE) {
                    myBoard.hardDrop();               
                }            
            } 
            
            if (theKeyCode == KeyEvent.VK_P) {
                togglePaused();
            } 
        }
        
        /**
         * Helper method for the mute button M.
         * 
         * @param theKeyCode the key code.
         */
        private void muteButton(final int theKeyCode) {
            
            if (theKeyCode == KeyEvent.VK_M) {

                if (myMute) {
                    myMute = false;

                } else {
                    myMute = true;
                    mySoundPlayer.stop(SONG);  

                }              
            }
        }
    } 
    
    
    
    
    
    
    
    /**
     * Abstract Action class for the New game and 
     * end game menu items.
     * 
     * @author Dmitriy Onishchenko
     * @version 4 December 2015
     *
     */
    private class NewEndGameAction extends AbstractAction {
        
        /**
         * Default serial version ID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Constructor initialize fields.
         * 
         * @param theText the name of the action
         */
        public NewEndGameAction(final String theText) {
            super(theText);  
                       
            final char keyCode = theText.charAt(0); // get the key code 

            putValue(MNEMONIC_KEY, KeyEvent.getExtendedKeyCodeForChar(keyCode));            
        }      
        
        
        
        /**
         * {@inheritDoc}.
         * 
         * Start or end a game.
         */
        @Override
        public void actionPerformed(final ActionEvent theEvent) {           
                     
            final String name = (String) this.getValue(NAME);
            
            if ("New Game".equals(name)) {                
                
                if (myGameEnded || myBoard.isGameOver()) {
                    
                    myBoard.newGame(myBoardWidth, myBoardHeight, null);
                    myGameEnded = false;
                    myPlayingPanel.setMessageVisible(false);
                    myStatsPanel.resetScoringPanel(myLevel);  
                    myTimer.restart();
                    mySoundPlayer.stopAll();
                    
                    if (!myMute) {
                        
                        mySoundPlayer.play(LEVEL_UP);
                    }                  
                }             
              
                // game over is selected
            } else {
                
                myPlayingPanel.setMessageVisible(true, "Game Ended");
                myDelay = INITIAL_DELAY;
                myLevel = 0;
                myGameEnded = true;               
                               
            }
        }
    }
    
    /**
     * A Class that listens for mouse movement on the menu Item.
     * When mouse action detected it pauses and unpauses the game.
     * 
     * @author Dmitriy Onishchenko
     * @version 11 December 2015
     *
     */
    private class MenuListeners implements MenuListener {

        /**
         * {@inheritDoc}.
         */
        @Override
        public void menuCanceled(final MenuEvent theEvent) {
            // no effect           
        }

        /**
         * {@inheritDoc}.
         * 
         * Toggle the pause status.
         */
        @Override
        public void menuDeselected(final MenuEvent theEvent) {
            
            if (myTimer.isRunning() || !myGameEnded || myBoard.isGameOver()) {
                togglePaused();
            } 
        }

        /**
         * {@inheritDoc}.
         * 
         * Toggle the pause status.
         */
        @Override
        public void menuSelected(final MenuEvent theEvent) {
            
            if (myTimer.isRunning() || myBoard.isGameOver()) {
                togglePaused();
            }
        }
    }

   
    
}
