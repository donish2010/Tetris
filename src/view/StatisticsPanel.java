package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Block;
import model.Board;

/**
 * The score panel for tetris game. Includes score, lines cleared,
 * level and next level label.
 * 
 * @author Dmitriy Onishchenko
 * @version 11 December 2015
 *
 */
public class StatisticsPanel extends JPanel implements Observer {
    
    /**
     * Property change for advancing level.
     */
    public static final String PROPERTY_NEXT_LEVEL = "Next level";
    
    /**
     * Property change when piece is frozen.
     */
    public static final String PROPERTY_PIECE_DOWN = "Piece is down";
    
    /**
     * Property change when line is cleared.
     */
    public static final String PROPERTY_CLEAR_LINE = "Line cleared";    
    
    /**
     * Border width.
     */
    private static final int BORDER_WIDTH = 4;
    
    /**
     * Text Size.
     */
    private static final int TEXT_SIZE = 20;
    
    /**
     * Advance level every five lines.
     */
    private static final int ADVANCE_LEVEL = 8;
    
    /**
     * Generated Serial version ID.
     */
    private static final long serialVersionUID = -8197599436287522493L;
    
    /**
     * A String Filler.
     */
    private static final String SPACE = "  ";
    
    /**
     * The default size of the score panel.
     */
    private static final Dimension DEFAULT_SIZE = 
                    new Dimension(240, 200);

    /**
     * The score label.
     */
    private final JLabel myScoreLabel;
    
    /**
     * The level label.
     */
    private final JLabel myLevelLabel;
    
    /**
     * The lines cleared label.
     */
    private final JLabel myLinesLabel;
    
    /**
     * Level advances label.
     */
    private final JLabel myNextLevelLabel;    
    
    /**
     * The number of rows cleared.
     */
    private int myRows; 
    
    /**
     * The playing board.
     */
    private Board myBoard;
    
    /**
     * The current score of the game.
     */
    private int myScore = 0;
    
    /**
     * The current level of the game.
     */
    private int myLevel = 0;
    
    /**
     * The current number of lines cleared.
     */
    private int myLines;
    
    /**
     * The number of non empty blocks in game.
     */
    private int myNonEmpty; 
    
    /**
     * The counter when to advance to next level.
     */
    private int myAdvance = ADVANCE_LEVEL;
    

    
    
    /**
     * Constructor initialize fields.
     */
    public StatisticsPanel() {        
        super(new GridLayout(4, 1, 0, 6));
        
        myScoreLabel = createJLabelInt(myScore);
        myLevelLabel = createJLabelInt(myLevel);
        myLinesLabel = createJLabelInt(myLines);
        myNextLevelLabel = createJLabelInt(0);
        
        setPreferredSize(DEFAULT_SIZE);
        setBackground(Color.lightGray);
        setOpaque(true);      
        
        createLayout(); 
        
    }
    

    

    /**
     * {@inheritDoc}.
     * 
     * Update the score.
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        
        myBoard = (Board)  theObservable;
        
        // update the score
        updateScoringPanel(); 
        
        // total number of frozen rows in current game
        myRows = myBoard.getFrozenBlocks().size();
        
    }
    
    /**
     * A public method to reset the Scoring panel.
     * 
     * @param theLevel the level to reset the level to.
     */
    public void resetScoringPanel(final int theLevel) {
        myScore = 0;
        myLevel = theLevel;        
        myLines = 0;
        myAdvance = ADVANCE_LEVEL;
        
        update(myScoreLabel, myScore);
        update(myLevelLabel, myLevel);
        update(myLinesLabel, myLines);        
    }
    
    /**
     * Method to set the level.
     * 
     * @param theLevel the level to set it to
     */
    public void setLevel(final int theLevel) {
        
        myLevel = theLevel;
        update(myLevelLabel, theLevel);
    }
    
    /////////////////////////////// private helpers ///////////////////////////////////////
    
    /**
     * Add all panels to score panel.
     */
    private void createLayout() {
        
        add(createStatPanel("  Score:", myScoreLabel));
        add(createStatPanel("  Level:", myLevelLabel));
        add(createStatPanel("  Lines:", myLinesLabel));
        add(createStatPanel("  Next Level:", myNextLevelLabel));
        
    }
    
    
    /**
     * Helper method that creates the panel for each statistic.
     * 
     * @param theText the name
     * @param theLabel the label
     * @return JPanel a JPanel 
     */
    private JPanel createStatPanel(final String theText, final JLabel theLabel) {
        
        final JPanel stat = new JPanel(new GridLayout());
        
        stat.add(createJLabel(theText));
        stat.add(theLabel);
        stat.setBorder(BorderFactory.createLineBorder(Color.BLACK, BORDER_WIDTH));
        stat.setBackground(Color.LIGHT_GRAY);
        
        return stat;        
    }
    
    /**
     * Create a JLabel for the numbers.
     * 
     * @param theNumber the value
     * @return JLabel a JLabel with the value
     */
    private JLabel createJLabelInt(final int theNumber) {
        
        final JLabel num = new JLabel(theNumber + SPACE);
        num.setHorizontalAlignment(JLabel.TRAILING);   
        
        num.setFont(new Font(Font.SANS_SERIF, Font.BOLD, TEXT_SIZE));
        
        return num;
    }
    
    /**
     * Create a JLabel for the text.
     * 
     * @param theText the name of the label
     * @return JLabel a label with text
     */
    private JLabel createJLabel(final String theText) {
        
        final JLabel label = new JLabel(theText); 
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, TEXT_SIZE));
        
        return label;
    }
    
    
    /**
     * Method that updates the complete scoring panel.
     */
    private void updateScoringPanel() {
        
        final int currentRows = myBoard.getFrozenBlocks().size();
        final int linesCleared = myRows - currentRows; 
        
        updateScorePiece();   
        updateScoreLinesCleared(linesCleared);
        updateLines(linesCleared);        
        updateLevel();
    }
    
    
    /**
     * Method that updates the score when the current piece is frozen.
     */
    private void updateScorePiece() {
        
        final int oldScore = myScore;
        int nonEmptyBlocks = 0;
        
        final List<Block[]> frozen = myBoard.getFrozenBlocks();      


        for (final Block[] blocks: frozen) {

            for (int i = 0; i < blocks.length; i++) {

                if (blocks[i] != Block.EMPTY) {                   
                    nonEmptyBlocks++;
                }               
            }
        }

        if (nonEmptyBlocks > myNonEmpty) {

            final int newScore = 10 * (myLevel + 1);

            myScore += newScore;
            update(myScoreLabel, myScore);
            
        }

        
        // keep track of non-empty blocks
        myNonEmpty = nonEmptyBlocks;
        firePropertyChange(PROPERTY_PIECE_DOWN, oldScore, myScore);
    }
    
    /**
     * Method that updates the level.
     */
    private void updateLevel() {
        
       
        final int oldLevel = myLevel;
        
        if (myLines >= myAdvance) {

            myAdvance += ADVANCE_LEVEL;
            myLevel++;
            update(myLevelLabel, myLevel);       

        }   

        update(myNextLevelLabel, myAdvance);        
        firePropertyChange(PROPERTY_NEXT_LEVEL, oldLevel, myLevel);
    }
    
    /**
     * Method that updates the score according to the number of lines cleared.
     * 
     * @param theLinesCleared the lines cleared 
     */
    private void updateScoreLinesCleared(final int theLinesCleared) {
        
        final int one = 1;
        final int two = 2;
        final int three = 3;
        final int four = 4;        
        
        final int oneLine = 50 * (myLevel + 1);
        final int twoLines = 150 * (myLevel + 1);
        final int threeLines = 250 * (myLevel + 1);
        final int fourLines = 1000 * (myLevel + 1);
        
        
        switch (theLinesCleared) {
            
            case one:
                myScore += oneLine;
                break;
            case two:
                myScore += twoLines;
                break;
            case three:
                myScore += threeLines;
                break;
            case four:
                myScore += fourLines;
                break;
            default :               
                break;
                
        }        
        
        update(myScoreLabel, myScore);
        
    }
    
    /**
     * Method that updates the number of lines cleared.
     * 
     * @param theLinesCleared the number of lines
     */
    private void updateLines(final int theLinesCleared) {
        
        final int oldLines = myLines;
        
        if (theLinesCleared > 0) {
            
            myLines += theLinesCleared;
            update(myLinesLabel, myLines);      
        }
        
        firePropertyChange(PROPERTY_CLEAR_LINE, oldLines, myLines);
    }
    
    
    
    /**
     * Helper method for setting the value of the labels.
     * 
     * @param theLabel the label to update
     * @param theNumber the value
     */
    private void update(final JLabel theLabel, final int theNumber) {
        theLabel.setText(theNumber + SPACE);
    }   

}
