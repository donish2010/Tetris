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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.AbstractPiece;
import model.Block;
import model.Board;
import model.Piece;
import sun.applet.Main;

/**
 * This class is the playing panel which is a drawing 
 * panel where the game takes place. It draws the current 
 * and frozen pieces.
 * 
 * @author Dmitriy Onishchenko
 * @version 11 December 2015
 *
 */
public class PlayingPanel extends JPanel implements Observer {   
    
    /**
     * RGB color value for background.
     */
    private static final int COLOR = 92;
    
    private static final Color GHOST_PIECE = 
                    new Color(224, 224, 224, 50);
    
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Arc width for rounded rectangle corners.
     */
    private static final int ROUND_CORNERS = 10;  
    
    /**
     * Playing board width.
     */
    private static final int BOARD_WIDTH = 10;
    
    /**
     * Playing board height.
     */
    private static final int BOARD_HEIGHT = 20;

    /**
     * The playing panel Dimension.
     */
    private static final Dimension PANEL_DIMENSION = new Dimension(300, 600);
    
    /**
     * A map with Blocks and their corresponding colors.
     */
    private final Map<Block, Color> myColors;
    
    /**
     * The tetris board for this panel.
     */
    private Board myBoard;  
    
    /**
     * The message when game is paused.
     */
    private JLabel myPaused;   

    
    /**
     * Grid status.
     */
    private Boolean myGridEnabled = false;
   
                   
    /**
     * Constructor instantiate fields.
     */
    public PlayingPanel() {
        
        super(new BorderLayout());
        
        // prevent null pointer exception
        myBoard = new Board(BOARD_WIDTH, BOARD_HEIGHT, null);
        myColors = new HashMap<>();      
        
        setPreferredSize(PANEL_DIMENSION);
        setBackground(new Color(COLOR, COLOR, COLOR));  
        
        loadMap();        
        createMessageLabel();        
    }
    
    /**
     * A public method that sets the message visibility to 
     * true or false and sets the text to game paused.
     * used mainly for pausing the game.
     * 
     * @param theStatus true or false
     * @param theMessage the message to display
     */
    public void setMessageVisible(final Boolean theStatus, final String theMessage) {
        
        myPaused.setText("<html><font color=#FF0000>" + theMessage + "...</font></html>");
        myPaused.setVisible(theStatus);
        myPaused.setIcon(null);
        
    }

    /**
     * {@inheritDoc}.
     * 
     * repaints the playing panel
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        
        myBoard = (Board) theObservable;
        repaint();
        
    }
    
    
    /**
     * Set the JLabel visibility status and 
     * remove icon.
     * 
     * @param theStatus the visibility status
     */
    public void setMessageVisible(final Boolean theStatus) {
        myPaused.setVisible(theStatus);
        myPaused.setIcon(null);
    }
    
    /**
     * Set visibility of grid.
     * 
     * @param theStatus the visibility 
     */
    public void enableGrid(final Boolean theStatus) {
        myGridEnabled = theStatus;
    }  

    
    /**
     * {@inheritDoc}.
     * 
     * Draws the current and frozen pieces and 
     * the grid if user choose so.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        
        
      //cast Graphics object to the "newer-ish" Graphics2D class.
        final Graphics2D g2d = (Graphics2D) theGraphics;

        // for better graphics display
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON); 
        
        g2d.setPaint(Color.BLACK);
        
        if (myGridEnabled) {
            drawGrid(g2d);     
        }
          
        // draw ghost piece
        if (!myPaused.isVisible()) {
            
            drawPiece(g2d, myBoard.getGhostboardCoordinates(), GHOST_PIECE);
        }
       
        
        // draw current piece
        drawPiece(g2d, ((AbstractPiece) myBoard.getCurrentPiece()).
                       getBoardCoordinates(), null);
        
        drawBoard(g2d);
        
        if (myBoard.isGameOver()) {
            myPaused.setText("<html><font color=#FF0000>Game Over...</font></html>");
            myPaused.setVisible(true);            
        }
    }
    
    ////////////////////////////// private helper /////////////////////////////
    
    
    
    private void drawPiece(final Graphics2D theGraphics, 
                           final int[][] theCoordinates, 
                           final Color theColor) {
        
        
        final int blockSize = getWidth() / myBoard.getWidth();
        
        
        for (int row = 0; row < theCoordinates.length; row++) {               
            
            int xcor = 0;
            int ycor = 0;
            
            for (int col = 0; col < theCoordinates[row].length; col++) {
                
                xcor = theCoordinates[row][0] * blockSize;
                ycor = getHeight() - (theCoordinates[row][1] * blockSize) - blockSize;
            }
            
            // draw the solid rectangle
            theGraphics.setPaint(theColor);
            
            
            if (theColor == null) {
                theGraphics.setPaint(chooseColor(myBoard.getCurrentPiece(), Block.EMPTY));
            } 
            
            theGraphics.fillRoundRect(xcor, ycor, blockSize, blockSize, 
                                      ROUND_CORNERS, ROUND_CORNERS);
            
            // draw the border around the rectangle          
            theGraphics.setPaint(Color.WHITE);
            theGraphics.drawRoundRect(xcor, ycor , blockSize, blockSize, 
                                      ROUND_CORNERS, ROUND_CORNERS);
        } 
    }
    
    
    
    /**
     * Helper method to load the map with key value pairs
     * of blocks and their colors.
     */
    private void loadMap() {
        
        myColors.put(Block.I, Color.BLUE);
        myColors.put(Block.J, Color.RED);
        myColors.put(Block.L, Color.GREEN);
        myColors.put(Block.O, Color.BLACK);
        myColors.put(Block.S, Color.CYAN);
        myColors.put(Block.T, Color.YELLOW);
        myColors.put(Block.Z, Color.MAGENTA);

    }
    
    /**
     * A Helper method that creates the Game over or 
     * game pause JLabel message.
     */
    private void createMessageLabel() {
        
        myPaused = new JLabel();
        final Font font = new Font(Font.SANS_SERIF, Font.BOLD, 30);       
        
        myPaused.setFont(font);
        myPaused.setHorizontalAlignment(JLabel.CENTER);
        add(myPaused, BorderLayout.CENTER);
        final URL logoUrl = Main.class.getResource("/tetris-logo.png");
        myPaused.setIcon(new ImageIcon(logoUrl));

        myPaused.setVisible(true);       
        
    }

    
//    /**
//     * Helper method that draws the current piece of the game.
//     * 
//     * @param theGraphics the graphics 2d object
//     */
//    private void drawCurrentPiece(final Graphics2D theGraphics) {
//        
//        final int blockSize = getWidth() / myBoard.getWidth();        
//        final Piece currentPiece = myBoard.getCurrentPiece();
// 
//        // retrieve coordinates of current piece
//        final int[][] cPCoor = ((AbstractPiece) currentPiece).getBoardCoordinates();
//
//        
//        for (int row = 0; row < cPCoor.length; row++) {                
//            
//            int xcor = 0;
//            int ycor = 0;
//            
//            for (int col = 0; col < cPCoor[row].length; col++) {
//                
//                xcor = cPCoor[row][0] * blockSize;
//                ycor = getHeight() - (cPCoor[row][1] * blockSize) - blockSize; 
//                
//            }
//            
//            // draw the solid rectangle
//            theGraphics.setPaint(chooseColor(currentPiece, Block.EMPTY));
//            theGraphics.fillRoundRect(xcor, ycor, blockSize, blockSize, 
//                                      ROUND_CORNERS, ROUND_CORNERS);
//            
//            // draw the border around the rectangle
//            theGraphics.setPaint(Color.WHITE);
//            theGraphics.drawRoundRect(xcor, ycor , blockSize, blockSize, 
//                                      ROUND_CORNERS, ROUND_CORNERS);
//        }            
//    }
    
    /**
     * Helper method that draws the frozen block of the board.
     * 
     * @param theGraphics the graphics 2d object
     */
    private void drawBoard(final Graphics2D theGraphics) {
        
        // determine the block size
        final int blockSize = getWidth() / myBoard.getWidth();
        
        // the y coordinate of the blocks
        int yCor = getHeight() - blockSize;
        
        // retrieve the frozen blocks        
        final List<Block[]> frozen = myBoard.getFrozenBlocks();       
        
        if (!frozen.isEmpty()) {          
            
            for (final Block[] blocks: frozen) {                
                for (int i = 0; i < blocks.length; i++) {
                    
                    // the x coordinate of the frozen block
                    final int xCor = i * blockSize;
                    
                    if (blocks[i] != Block.EMPTY) {
                        
                        theGraphics.setPaint(chooseColor(null, blocks[i]));
                        theGraphics.fillRoundRect(xCor, yCor, blockSize, blockSize, 
                                                  ROUND_CORNERS, ROUND_CORNERS);


                        theGraphics.setPaint(Color.WHITE);
                        theGraphics.drawRoundRect(xCor, yCor, blockSize, blockSize, 
                                                  ROUND_CORNERS, ROUND_CORNERS);
                    }
                }
                
                // the new y coordinate 
                yCor -= blockSize;
            }
        }
    }
    
    
    
    /**
     * Helper Method to draw the grid lines.
     * 
     * @param theGraphics the Graphics object
     */
    private void drawGrid(final Graphics theGraphics) {
        
        final int verticalSpace = getWidth() / myBoard.getWidth();
        final int horizontalSpace = getHeight() / myBoard.getHeight();
        
        // draw vertical lines 
        for (int i = verticalSpace; i <= getWidth(); i += verticalSpace) {
            theGraphics.drawLine(i, 0, i, getHeight());
            
        }
        
        // draw horizontal lines
        for (int i = horizontalSpace; i <= getHeight(); i += horizontalSpace) {
            theGraphics.drawLine(0, i, getWidth(), i);
        }            
    }
    
    
    /**
     * Helper method that chooses the correct color of the for the
     * frozen blocks and current piece.
     * 
     * @param theCurrentPiece the current piece
     * @param theBlock the block of frozen pieces
     * @return Color the color to paint the blocks
     */
    private Color chooseColor(final Piece theCurrentPiece, final Block theBlock) {        
        
        Color returnColor = Color.BLACK;

        Block cb;

        if (theCurrentPiece == null) {
            cb = Block.EMPTY;            
        } else {
            cb = ((AbstractPiece) theCurrentPiece).getBlock();
        }           

        if (!myBoard.isGameOver() && !myPaused.isVisible()) {           

            
            for (final Map.Entry<Block, Color> entry : myColors.entrySet()) {
                
                final Block b = entry.getKey();
                
                if (cb.equals(b) || theBlock.equals(b)) {
                    
                    // get corresponding color of block
                    returnColor = myColors.get(b);
                    break;
                }               
            }
        }
        
        return returnColor;
        
    }

}