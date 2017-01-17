/*
 * TCSS 305 Autumn 2015
 * Assignment 6 - Tetris
 * 
 */

package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.Timer;

import model.AbstractPiece;
import model.Block;
import model.Board;
import model.Piece;

/**
 * This class shows the next piece that will
 * show up in the game.
 * 
 * @author Dmitriy Onishchenko
 * @version 11 December 2015
 *
 */
public class NextPiecePanel extends JPanel implements Observer {
    
    /**
     * Default generated serial ID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * RGB color value for background.
     */
    private static final int COLOR = 92;

    /**
     * The piece arc for rounded corners. 
     */
    private static final int ROUND_CORNERS = 20;
    
    /**
     * Playing board width.
     */
    private static final int BOARD_WIDTH = 10;
    
    /**
     * Playing board height.
     */
    private static final int BOARD_HEIGHT = 20;
    
    /**
     * The panel dimension.
     */
    private static final Dimension PANEL_DIMENSION =
                    new Dimension(240, 130);    
    
    /**
     * The timer used with the game.
     */
    private final Timer myTimer;
    
    /**
     * The tetris game board.
     */
    private Board myBoard;  
    
    

    /**
     * Constructor, instantiate fields.
     * 
     *  @param theTimer the timer used in game
     */
    public NextPiecePanel(final Timer theTimer) {
        super(); 
        
        myTimer = theTimer;
        
        // prevent null pointer exception
        myBoard = new Board(BOARD_WIDTH, BOARD_HEIGHT);        
        
        setPreferredSize(PANEL_DIMENSION);
        setBackground(new Color(COLOR, COLOR, COLOR));
        
    }


    
    /**
     * {@inheritDoc}.
     * 
     * Method that draws the next shape.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        
        super.paintComponent(theGraphics);
        
        final Graphics2D g2d = (Graphics2D) theGraphics;
        
        // for better graphics display
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON); 
        
        
        // prevent from drawing the piece when program starts
        if (myTimer.isRunning()) {
            drawNextShape(g2d);
            
        }
    }   
    
    /**
     * {@inheritDoc}.
     * 
     * Repaint this panel.
     * 
     * @param theObservable the Observable object
     * @param theObject the object that is changed.
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        
        myBoard = (Board) theObservable;    
        
        repaint();        
    }


    ///////////////////////////////// private helper //////////////////////////////////
    
    /**
     * Helper method to draw next shape.
     * 
     * @param theGraphics the graphics object
     */
    private void drawNextShape(final Graphics2D theGraphics) {
        
        final int strokeWidth = 2;          
        
        final int blockSize = getWidth() / 6;
        final int blockAdjustment = blockSize / 2  + blockSize;
        
        // space from top of panel to center pieces
        final int space = (getHeight() - (blockSize * 2)) / 2;
       
        final Piece nextPiece = myBoard.getNextPiece();        
 
        final int[][] nPCoor = ((AbstractPiece) nextPiece).getRotation();
        
        
        // loop through rotation coordinates and draw the shape
        for (int row = nPCoor.length - 1; row >= 0; row--) {                
            
            int xcor = 0;
            int ycor = 0; 
            
            for (int col = nPCoor[row].length - 1; col >= 0; col--) {                 
               
                // adjust the piece to make it center if it is an I or O piece
                if (((AbstractPiece) nextPiece).getBlock() == Block.I) {

                    xcor = (nPCoor[row][0] * blockSize) + blockSize;                    
                    ycor = (getHeight() / 2) - (blockSize / 2);

                } else if (((AbstractPiece) nextPiece).getBlock() == Block.O) {
                    
                    xcor = (nPCoor[row][0] * blockSize) + blockSize;
                    ycor = ((nPCoor[row][1] - 1) * blockSize) + space; 
                    
                } else {       
                                   
                    xcor = nPCoor[row][0] * blockSize + blockAdjustment;
                    ycor = ((nPCoor[row][1] - 1) * blockSize) + space;                  
                    
                    // flip the shape over to correct rotation
                    if (ycor + blockSize + space == getHeight()) {
                        ycor = ycor - blockSize;
                    } else {
                        ycor = ycor + blockSize;
                    }
                }
            }
          
            // draw the filled rectangles
            theGraphics.setPaint(chooseColor(nextPiece));
            theGraphics.fillRoundRect(xcor, ycor, blockSize, blockSize, 
                                      ROUND_CORNERS, ROUND_CORNERS);
            
            // draw the white border around the shapes
            theGraphics.setPaint(Color.WHITE);
            theGraphics.setStroke(new BasicStroke(strokeWidth));
            theGraphics.drawRoundRect(xcor, ycor , blockSize, blockSize, 
                                      ROUND_CORNERS, ROUND_CORNERS);            
   
        
        }
    }
    
    /**
     * Helper method to set the color of different blocks.
     * 
     * @param theNextPiece the next piece in game
     * @return Color the color to paint the next piece
     */
    private Color chooseColor(final Piece theNextPiece) {
        
        Color returnColor = Color.BLACK;
        
        switch (((AbstractPiece) theNextPiece).getBlock()) {
            
            case I : 
                returnColor = Color.BLUE;
                break;
            case J :
                returnColor = Color.RED;
                break;
            case L :
                returnColor = Color.GREEN;
                break;           
            case S :
                returnColor = Color.CYAN;
                break;
            case T :
                returnColor = Color.YELLOW;
                break;
            case Z :
                returnColor = Color.MAGENTA;
                break;
            default :
                break;
        }
        
        return returnColor;
    }

}
