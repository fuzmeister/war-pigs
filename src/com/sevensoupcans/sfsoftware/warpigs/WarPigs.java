package com.sevensoupcans.sfsoftware.warpigs;
/**
 *
 * @author sthompson
 */

import java.awt.event.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import javax.swing.*;

import com.sevensoupcans.sfsoftware.warpigs.game.Tile;

public class WarPigs extends JFrame implements MouseListener, ActionListener { 	

	private static final long serialVersionUID = 1L;

	// Declaration and initialization of some important variables
	final public int PLAY_FIELD_X 					= 32;
	final public int PLAY_FIELD_Y 					= 64;
    final private Timer MAIN_TIMER 					= new Timer(15, this);
    final private Timer ANIMATION_TIMER 			= new Timer(500, this);	
    final private HashMap<String, Image> SPRITES 	= loadGraphics();
    
    private Graphics theGraphicsBuffer;  // This thing is mad important!
      
    public int GameState = 0;
    private Tile[][] PlayField = new Tile[7][7];
    private Tile[][] HumanField = new Tile[7][7];    
    //private Explosion BoomBoom = new Explosion();

    boolean AIHit;
    int AIx;
    int AIy;
    
    boolean FlashGameOverShips = false;
    
    public JTextField xGuess = new JTextField(1);
    public JTextField yGuess = new JTextField(1);
    public JButton jbAttack = new JButton("Attack!");
    public JLabel jlPrompt = new JLabel("Phase 1");
    public JLabel jlRemaining = new JLabel("Remaining Enemies: ");
    public JLabel jxPrompt = new JLabel("X:");
    public JLabel jyPrompt = new JLabel("Y:");     
    
    int missionNumber = 1;
    int numberEnemyShips = 3;
    int numberPlayerShips = 3;
    
    public int iPigFrame = 0;
    public boolean UpMissileVisible = false;
    public int UpMissileY = -67;
    public int DownMissileY = 640;
    public int UpMissileX = 0;
    public int DownMissileX = 0;  
    public boolean DownMissileVisible = false;    
    public double MissileAccel = 10;
    public int MissileDestY;    
    
    public JPanel augh = new JPanel();
    
    int TitleCounter = 0;
    int DrakeY = 640;
    
    private String DrakeSpeak = "";
    private int DrakeSpeechBubble = 0;

    public WarPigs() {
        
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                PlayField[i][j] = new Tile(PLAY_FIELD_X + (32 * i),  PLAY_FIELD_Y + (32 * j));
                HumanField[i][j] = new Tile(PLAY_FIELD_X + (32 * i), 288 + PLAY_FIELD_Y + (32 * j));
            }
        }
        
        MAIN_TIMER.start();
        ANIMATION_TIMER.start();
    
        
        generateShip(numberPlayerShips, PlayField, 1);
        generateShip(numberEnemyShips, HumanField, 5);
        DrakeY = 640;
        
        xGuess.setLocation(PLAY_FIELD_X + 318, PLAY_FIELD_Y + 72);
        xGuess.setSize(64,22); 
        
        yGuess.setLocation(PLAY_FIELD_X + 318, PLAY_FIELD_Y + 102);
        yGuess.setSize(64,22);
        
        jlRemaining.setLocation(PLAY_FIELD_X + 254, PLAY_FIELD_Y + 190);
        jlRemaining.setSize(128,22);
        
        jbAttack.setLocation(PLAY_FIELD_X + 254, PLAY_FIELD_Y + 140);
        jbAttack.setSize (128,32);
        jbAttack.addActionListener(this);
        
        jlPrompt.setLocation(PLAY_FIELD_X + 330, 57);
        jlPrompt.setSize(256,32);

        jxPrompt.setLocation(PLAY_FIELD_X + 300, 130);
        jxPrompt.setSize(64,32);
        
        jyPrompt.setLocation(PLAY_FIELD_X + 300, 160);
        jyPrompt.setSize(64,32);  
        
        addMouseListener(this);
        
        this.setIconImage(SPRITES.get("title"));
        this.setTitle("War Pigs - Developed by Steve Thompson");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(445, 608);        
        this.setResizable(false);
        
        repaint();
        setVisible(true);
    }
    
    public void generateShip(int HowMany, Tile[][] Tileset, int ShipValue) {
        int MisterRandomNumber = (int) (Math.random() * 5) + 1;
        int MissRandomNumber = (int) (Math.random() * 5) + 1;
        int AnotherRandomNumber = (int) (Math.random() * 2);
        boolean Success;
        int i = 0;
        
        do {
            
            do{
                Success = false;
                
                if(AnotherRandomNumber == 0) {
                    if(Tileset[MisterRandomNumber + 1][MissRandomNumber].status == 0 && Tileset[MisterRandomNumber][MissRandomNumber].status == 0 && Tileset[MisterRandomNumber - 1][MissRandomNumber].status == 0) {
                        Tileset[MisterRandomNumber + 1][MissRandomNumber].status = ShipValue;
                        Tileset[MisterRandomNumber][MissRandomNumber].status = 2;
                        Tileset[MisterRandomNumber - 1][MissRandomNumber].status = ShipValue;
                        
                        Success = true;
                    }
                    MisterRandomNumber = (int) (Math.random() * 5) + 1;
                    MissRandomNumber = (int) (Math.random() * 5) + 1;
                    AnotherRandomNumber = (int) (Math.random() * 2);
                }
                
                else {
                    if(Tileset[MisterRandomNumber][MissRandomNumber+1].status == 0 && Tileset[MisterRandomNumber][MissRandomNumber].status == 0 && Tileset[MisterRandomNumber][MissRandomNumber-1].status == 0) {
                        Tileset[MisterRandomNumber][MissRandomNumber + 1].status = ShipValue;
                        Tileset[MisterRandomNumber][MissRandomNumber].status = 3;
                        Tileset[MisterRandomNumber][MissRandomNumber - 1].status = ShipValue;     
 
                        Success = true;
                    }
                    MisterRandomNumber = (int) (Math.random() * 5) + 1;
                    MissRandomNumber = (int) (Math.random() * 5) + 1;
                    AnotherRandomNumber = (int) (Math.random() * 2);                    
                }
            } while(Success == false);
            
            i++;
            
        } while(i < HowMany);
    }
    
    public void BeginGame() {
        add(jxPrompt);
        add(jyPrompt);
        add(xGuess);
        add(yGuess);
        add(jbAttack);
        add(jlRemaining);
        add(jlPrompt);

        add(augh);

        jxPrompt.setEnabled(true);
        jyPrompt.setEnabled(true);
        jbAttack.setEnabled(true);
        
        GameState = 1;
    
    }
    
    public int GetShipCount(Tile[][] TileSet) {
        int iCount = 0;
        
        for(int i = 1; i < 6; i++) {
            for(int j = 1; j < 6; j++) {
                if(TileSet[i][j].status == 2 && TileSet[i + 1][j].status == 5 && TileSet[i - 1][j].status == 5) {
                    if(TileSet[i][j].checkedAlready == false || TileSet[i + 1][j].checkedAlready == false || TileSet[i - 1][j].checkedAlready == false) {
                        iCount++;
                    }
                } 
                if(TileSet[i][j].status == 2 && TileSet[i + 1][j].status == 1 && TileSet[i - 1][j].status == 1) {
                    if(TileSet[i][j].checkedAlready == false || TileSet[i + 1][j].checkedAlready == false || TileSet[i - 1][j].checkedAlready == false) {
                        iCount++;
                    }
                }   
                if(TileSet[i][j].status == 3 && TileSet[i][j + 1].status == 5 && TileSet[i][j - 1].status == 5) {
                    if(TileSet[i][j].checkedAlready == false || TileSet[i][j + 1].checkedAlready == false || TileSet[i][j - 1].checkedAlready ==false) {
                        iCount++;
                    }
                }  
                if(TileSet[i][j].status == 3 && TileSet[i][j + 1].status == 1 && TileSet[i][j - 1].status == 1) {
                    if(TileSet[i][j].checkedAlready == false || TileSet[i][j + 1].checkedAlready == false || TileSet[i][j - 1].checkedAlready ==false) {
                        iCount++;
                    }
                }  
            }
        }
        
        return iCount;
    }
    
    public void paint(Graphics g) {
        // We need a buffer and buffer image to store
        // what is drawn so that the graphics do not flash.
        
    	Image b = createImage(445, 608);
        theGraphicsBuffer = b.getGraphics();
        
        switch(GameState) {
            case 0:
                super.paint(theGraphicsBuffer);
                
                theGraphicsBuffer.setColor(Color.white);
                theGraphicsBuffer.fillRect(0,0,445,664);                
                drawImage("title", 150, 170, theGraphicsBuffer);                
                drawImage("titlet",94, 298, theGraphicsBuffer);
                
                theGraphicsBuffer.setColor(Color.black);
                theGraphicsBuffer.drawString("EPISODE I: SWINES AT SEA",145,400);                
                theGraphicsBuffer.drawString("BATTLESHIP PROGRAM BY S. THOMPSON", 92, 565);
                theGraphicsBuffer.drawString("GRAPHICS BY PEOPLE I DON'T KNOW", 105, 580);
                
                jxPrompt.setEnabled(false);
                jyPrompt.setEnabled(false);
                jbAttack.setEnabled(false);                

                break;
            case 1:
                theGraphicsBuffer.setColor(Color.white);
                theGraphicsBuffer.fillRect(0,0,445,664);
              
                super.paint(theGraphicsBuffer);
              
                //drawHUD();
                
                // First pass takes care of the basics - water tiles mostly.
                for(int i = 0; i < 7; i++) {
                    for(int j = 0; j < 7; j++) {

                        if(j == 6) {
                            theGraphicsBuffer.setColor(Color.BLACK);
                            theGraphicsBuffer.drawString("" + i, PLAY_FIELD_X + 12 + (i * 32), PLAY_FIELD_Y  - 8);
                            theGraphicsBuffer.drawString("" + i, PLAY_FIELD_X + 12 + (i * 32), (9*32) + PLAY_FIELD_Y  - 8);
                        }                
                        if(i == 0) {
                            theGraphicsBuffer.setColor(Color.BLACK);
                            theGraphicsBuffer.drawString("" + j, 16, PLAY_FIELD_Y + 20 + (j * 32));
                            theGraphicsBuffer.drawString("" + j, 16, (9 * 32) + PLAY_FIELD_Y + 20 + (j * 32));
                        }
                        
                        drawImage("water", PlayField[i][j].x, PlayField[i][j].y, 31, 31, theGraphicsBuffer);
                        drawImage("water", HumanField[i][j].x, HumanField[i][j].y, 31, 31, theGraphicsBuffer);
                                                
                        PlayField[i][j].paintComponent(theGraphicsBuffer);
                        HumanField[i][j].paintComponent(theGraphicsBuffer);
                        
                    }
                }
                
                // Second pass takes care of ship graphics
                for(int i = 0; i < 7; i++) {
                    for(int j = 0; j < 7; j++) {                
                        if(HumanField[i][j].status == 2) {                              
                            drawImage("h_boat", HumanField[i][j].x - 32, HumanField[i][j].y, 96, 32, theGraphicsBuffer);
                        }
                        if(HumanField[i][j].status == 3) {                               
                            drawImage("v_boat", HumanField[i][j].x, HumanField[i][j].y - 32, 32, 96, theGraphicsBuffer);
                        }
                        
                    }
                }

                // The following displays enemy ships if they have been destroyed.
                for(int i = 1; i < 6; i++) {
                    for(int j = 1; j < 6; j++) {                
                        if(PlayField[i][j].status == 2) {
                            if(PlayField[i][j].checkedAlready == true && PlayField[i + 1][j].checkedAlready == true && PlayField[i - 1][j].checkedAlready == true) {                                
                            	drawImage("h_boat", PlayField[i][j].x - 32, PlayField[i][j].y, 96, 32, theGraphicsBuffer);
                            }
                        }
                            
                        if(PlayField[i][j].status == 3) {
                            if(PlayField[i][j].checkedAlready == true && PlayField[i][j + 1].checkedAlready == true && PlayField[i][j - 1].checkedAlready == true) {                                   
                                drawImage("v_boat", PlayField[i][j].x, PlayField[i][j].y - 32, 32, 96,theGraphicsBuffer);
                            }
                        }
                        
                    }
                } 
                
                // The following displays ship graphics if the player got game over!
                if(GetShipCount(HumanField) <= 0 && FlashGameOverShips == true) {
                    for(int i = 1; i < 6; i++) {
                        for(int j = 1; j < 6; j++) {                
                            if(PlayField[i][j].status == 2) {
                                drawImage("h_boat", PlayField[i][j].x - 32, PlayField[i][j].y, 96, 32, theGraphicsBuffer);
                            }

                            if(PlayField[i][j].status == 3) {
                                drawImage("v_boat", PlayField[i][j].x, PlayField[i][j].y - 32, 32, 96, theGraphicsBuffer);
                            }

                        }
                    } 
                }
                
                // Third pass takes care of things overlaying the tiles such as hit or miss icons
                for(int i = 0; i < 7; i++) {
                    for(int j = 0; j < 7; j++) {
                        if(HumanField[i][j].checkedAlready == true) {
                            if(HumanField[i][j].status == 0) {
                                drawImage("miss", HumanField[i][j].x, HumanField[i][j].y, theGraphicsBuffer);
                            }
                            else {                                  
                                drawImage("hit", HumanField[i][j].x, HumanField[i][j].y, theGraphicsBuffer);
                            }
                        }
                        if(PlayField[i][j].checkedAlready == true) {
                            if(PlayField[i][j].status == 0) {
                                drawImage("miss", PlayField[i][j].x, PlayField[i][j].y, theGraphicsBuffer);
                            }
                            else {                             
                                drawImage("hit", PlayField[i][j].x, PlayField[i][j].y, theGraphicsBuffer);
                            }
                        }                        
                    }   
                }
                
                /*if(BoomBoom.visible == true) {                    
                    drawImage("explosion", BoomBoom.x, BoomBoom.y, 32, 32, BoomBoom.frame, 0, theGraphicsBuffer); 
                }*/

                jlRemaining.setText("Enemies In Range");                
                for(int q = 0; q < GetShipCount(PlayField); q++) {
                    if(q > 3) {
                        drawImage("umissile", 300 + ((q - 4) * 32), PLAY_FIELD_Y + 285, 32, 32, iPigFrame, 0, theGraphicsBuffer);
                    	//theGraphicsBuffer.drawImage(imgUpMissile, 300 + ((q - 4) * 32), PLAY_FIELD_Y + 285, 332 + ((q - 4) * 32), PLAY_FIELD_Y + 317, iPigFrame, 0, iPigFrame + 32, 32, this);                         
                    }
                    else {
                    	drawImage("umissile", 300 + (q * 32), PLAY_FIELD_Y + 250, 32, 32, iPigFrame, 0, theGraphicsBuffer);
                    	//theGraphicsBuffer.drawImage(imgUpMissile, 300 + (q * 32), PLAY_FIELD_Y + 250, 332 + (q * 32), PLAY_FIELD_Y + 282, iPigFrame, 0, iPigFrame + 32, 32, this);    
                    }
                }    
                
                drawImage("titlet", 273, 34, 172, 64, theGraphicsBuffer);
                
                if(GetShipCount(PlayField) == 0) {
                    DrakeSpeak = "Click Me!";
                    DrakeSpeechBubble = 0;
                }
                
                 if(GetShipCount(HumanField) == 0) {
                    DrakeSpeak = "Game Over!";
                    DrakeSpeechBubble = 0;
                }

                if(!DrakeSpeak.equals("")) {         
                    drawImage("speech", 265, 371, 97, 79, theGraphicsBuffer);
                    // Auto Adjust the Speech's X to how long the String var is...
                    theGraphicsBuffer.setColor(Color.black);       
                    theGraphicsBuffer.drawString(DrakeSpeak, 313 - ((DrakeSpeak.length() * 5) / 2), 408);
                }

                drawImage("drake", 327, DrakeY, theGraphicsBuffer); 
                
                int handY = 0;                           
                // Hand logic
                if(UpMissileY < 576) {
                    if(UpMissileY <= -67) {
                        handY = -67;
                    }
                    else {
                        handY = 576;
                    }
                } 
                else {
                    handY = UpMissileY;
                }     
                
                drawImage("hand", UpMissileX, handY, 32, 32, 0, 0, theGraphicsBuffer);                
                drawImage("umissile", UpMissileX, UpMissileY, 32, 32,iPigFrame, 0, theGraphicsBuffer);
                
                // More Hand Logic
                if(DownMissileY > 32) {
                    if(DownMissileY >= 640) {
                        handY = 640;
                    }
                    else {
                        handY = 32;                        
                    }
                } 
                else {
                    handY = DownMissileY;
                }                 
                drawImage("hand", DownMissileX, handY, 32, 32, 32, 0, theGraphicsBuffer);
               drawImage("dmissile", DownMissileX, DownMissileY, 32, 32, iPigFrame, 32, theGraphicsBuffer);
                break;
        }
                
        // Draw the buffer image to the actual image... confusing, no?
        g.drawImage(b, 0, 0, this);
        
    }
    
    public void resetGame() {
        if(GetShipCount(HumanField) > 0) {
            missionNumber++;

            numberEnemyShips++;
            if(numberEnemyShips > 8) {
                numberEnemyShips = 8;
            }

            numberPlayerShips--;
            if(numberPlayerShips < 1) {
                numberPlayerShips = 1;
            }              
        }
        else {
            missionNumber = 1;
            numberEnemyShips = 3;
            numberPlayerShips = 3;
        }
        
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                PlayField[i][j] = new Tile(PLAY_FIELD_X + (32 * i),  PLAY_FIELD_Y + (32 * j));
                HumanField[i][j] = new Tile(PLAY_FIELD_X + (32 * i), (9 * 32) + PLAY_FIELD_Y + (32 * j));
            }
        }
        
        jlPrompt.setText("Phase " + missionNumber);
        
        generateShip(numberEnemyShips, PlayField, 1);
        generateShip(numberPlayerShips, HumanField, 5);  
        
        DrakeY = 640;
        DrakeSpeak = "";
        repaint();
    }
    
    public void drawHUD() {
        for(int j = 0; j < 640; j = j + 32) {
            for(int i = 0; i < (8*32); i = i + 32) {
            	drawImage("water", i, j, 31, 31, theGraphicsBuffer);
            }
        }
        for(int j = 0; j < 640; j = j + 32) {
            /*for(int i = (9*32); i < 445; i = i + 32) {
            	drawImage("grass", i, j, theGraphicsBuffer);  
            }*/
            drawImage("shore", 256, j, 31, 31, theGraphicsBuffer);           
        }
    }
    
    public void DoEnemyAI() {
        int pX = 0;
        int pY = 0;
        boolean AIChecked = false;       
        
        // Only make a move if you have ships left ;)
        if(GetShipCount(PlayField) > 0) {  
            // Simple enemy AI using conditionals
            if(AIHit == true) {
                if(AIx > 0 && AIChecked == false) {
                    if(HumanField[AIx - 1][AIy].checkedAlready == false) {
                        pX = AIx - 1;
                        pY = AIy;
                        AIHit = true;
                        AIChecked = true;
                    }
                }
                if(AIx < 6 && AIChecked == false) {
                    if(HumanField[AIx + 1][AIy].checkedAlready == false) {
                        pX = AIx + 1;
                        pY = AIy;
                        AIHit = true;
                        AIChecked = true;
                    }
                }
                if(AIy > 0 && AIChecked == false) {
                    if(HumanField[AIx][AIy - 1].checkedAlready == false) {
                        pX = AIx;
                        pY = AIy - 1;
                        AIHit = true;
                        AIChecked = true;
                    }
                }    
                if(AIx < 6 && AIChecked == false) {
                    if(HumanField[AIx][AIy + 1].checkedAlready == false) {
                        pX = AIx;
                        pY = AIy + 1;
                        AIHit = true;
                        AIChecked = true;
                    }
                }   
                
            }
            if(AIChecked == false) {
                do {
                    // Random selection
                    pX = (int) (Math.random() * 7);
                    pY = (int) (Math.random() * 7);
                    
                    AIHit = false; 

                } while(HumanField[pX][pY].checkedAlready == true);
            }
            
            
            DownMissileY = -67;
            DownMissileX = HumanField[pX][pY].x;

            // The actual attack.
            if(HumanField[pX][pY].checkStatus() > 0) {
                // A hit of some sort :D
                MissileDestY = HumanField[pX][pY].y;
                AIHit = true;
                AIx = pX;
                AIy = pY;
            }
            else {
                MissileDestY = 640;
            }  
        }
    }
    
    public void doAttack() {
            // Be sure to convert your strings to numerals ;)
            int pX = 0;
            int pY = 0;
            
            try{
                pX = Integer.parseInt(xGuess.getText().trim());
                pY = Integer.parseInt(yGuess.getText().trim());
            }
            catch (ArrayIndexOutOfBoundsException obe) {
                System.out.println("ArrayIndexOutOfBoundsException: " + obe);
            }
            catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe);
            }
            
            
            UpMissileY = 640;
            UpMissileX = PlayField[pX][pY].x;
            UpMissileVisible = true;
            
            if(PlayField[pX][pY].checkStatus() > 0) {
                // A hit of some sort :D
                DrakeSpeak = "It's a Hit!";
                DrakeSpeechBubble = 0;
                MissileDestY = PlayField[pX][pY].y;
                if(PlayField[pX][pY].checkStatus() == 2) {
                    DrakeSpeak = "Got One!!"; 
                    DrakeSpeechBubble = 0;
                }
                
                if(PlayField[pX][pY].checkStatus() == 3) {
                    DrakeSpeak = "Cool!!";
                    DrakeSpeechBubble = 0;
                }
            }
            if(PlayField[pX][pY].checkStatus() == 0) {
                MissileDestY = -67;
                DrakeSpeak = "No Good...";
                DrakeSpeechBubble = 0;
            }

            
            
            xGuess.setText("");
            yGuess.setText("");             
            
            repaint();        
    }
    
    public void mouseClicked(MouseEvent e) {
        int MouseX = e.getX();
        int MouseY = e.getY();
        
        if(GetShipCount(PlayField) == 0 || GetShipCount(HumanField) == 0 ) {
            if(MouseX > 327 && MouseX < 445 && MouseY > 480 && MouseY < 640) {
                resetGame();
            }
        }
        else {
            for(int i = 0; i < 7; i++) {
                for(int j = 0; j < 7; j++) {
                    if(MouseX > PlayField[i][j].x && MouseX < (PlayField[i][j].x + 32) && MouseY > PlayField[i][j].y && MouseY < (PlayField[i][j].y + 32)) {
                        if(DownMissileY >= 480 && UpMissileY <= 0) {
                            xGuess.setText("" + i);
                            yGuess.setText("" + j);                        
                            
                            doAttack();
                        }
                    }
                    if(MouseX > HumanField[i][j].x && MouseX < (HumanField[i][j].x + 32) && MouseY > HumanField[i][j].y && MouseY < (HumanField[i][j].y + 32)) {
                        System.out.println("" + i + ", " + j);
                    }
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
     * 
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     * @param srcX
     * @param srcY
     * @param g
     */
    private void drawImage(String image, int x, int y, int width, int height, int srcX, int srcY, Graphics g)
    {
    	drawImage(image, x, y, SPRITES, width, height, srcX, srcY, g);	
    }

    /**
     * 
     * @param image
     * @param x
     * @param y
     * @param h
     * @param width
     * @param height
     * @param srcX
     * @param srcY
     * @param g
     */
    private void drawImage(String image, int x, int y, HashMap<String, Image> h, int width, int height, int srcX, int srcY, Graphics g)
    {
    	Object o = h.get(image);
    	if(o instanceof Image)
    	{
    		Image temp = (Image) o;
    		g.drawImage(temp, x, y, x + width, y + height, srcX, srcY, srcX + width, srcY + height, this);
    	}     	
    }    
    
    /**
     * 
     * @param image
     * @param x
     * @param y
     * @param g
     */
    private void drawImage(String image, int x, int y, Graphics g) {    	
    	drawImage(image, x, y, SPRITES, g);	
    }  
    
    /**
     * 
     * @param image
     * @param x
     * @param y
     * @param h
     * @param g
     */
    private void drawImage(String image, int x, int y, HashMap<String, Image> h,Graphics g) {    	
    	Object o = h.get(image);
    	if(o instanceof Image)
    	{
    		Image temp = (Image) o;
    		g.drawImage(temp, x, y, this);
    	} 
    }    
    
    /**
     * 
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     * @param g
     */
    private void drawImage(String image, int x, int y, int width, int height, Graphics g) {    	
    	drawImage(image, x, y, width, height, SPRITES, g);	
    }  
    
    /**
     * 
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     * @param h
     * @param g
     */
    private void drawImage(String image, int x, int y, int width, int height, HashMap<String, Image> h, Graphics g) {    	
    	Object o = h.get(image);
    	if(o instanceof Image)
    	{
    		Image temp = (Image) o;
    		g.drawImage(temp, x, y, width, height, this);
    	}   	
    }

    /**
     * Loads graphics from a provided list of files
     * 
     * @return HashMap<String, Image>	Collection of loaded images that can be referenced with a unique key.
     */
    private HashMap<String, Image> loadGraphics() {
    	
    	HashMap<String, Image> graphics = new HashMap<String, Image>();
       	String[] files = {"water.gif", "explosion.gif", "dmissile.gif", "umissile.gif", 
       						"h_boat.gif", "drake.gif", "speech.gif", "grass.gif", "v_boat.gif", 
       						"miss.gif", "hand.gif", "shore.gif", "hit.gif", "title.gif", "titlet.gif"};
       		    
    	for (String file : files)
		{
    		// Retrieve the file resource
    		URL spriteLocation = getClass().getResource(file);
    		Image temp = getToolkit().getImage(spriteLocation);
    		
    		// Store the graphic in our collection
    		String cleanName = file.substring(0, file.lastIndexOf('.'));
    		graphics.put(cleanName, temp);
		}
    	
    	System.out.println(graphics);
    	return graphics;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// Clicking the attack button triggers doAttack()
		if(e.getSource() == jbAttack)
		{
			if(DownMissileY >= 480 &&  UpMissileY <= 0) {
            	doAttack();
        	}
		}
		else if(e.getSource() == MAIN_TIMER)
		{
            if(GameState == 0) {
                TitleCounter = TitleCounter + 1;
                if(TitleCounter >= 160) {
                    BeginGame();
                }
            }
            else {
                if(DrakeSpeak.equals("") == false) {
                    DrakeSpeechBubble = DrakeSpeechBubble + 1;   
                    if(DrakeSpeechBubble >= 100) {
                        DrakeSpeak = "";
                        DrakeSpeechBubble = 0;
                    }
                }   
                if(UpMissileVisible == true) {
                    if(UpMissileY - MissileDestY < 10) {
                        new Explosion(SPRITES.get("explosion"), UpMissileX, UpMissileY, theGraphicsBuffer, this);
                        
                    	/*BoomBoom.visible = true;
                        BoomBoom.x = UpMissileX;
                        BoomBoom.y = UpMissileY;*/
                        UpMissileY = -67;
                        UpMissileVisible = false;
                    }
                    
                    if(UpMissileY > -67) {
                        UpMissileY = UpMissileY - (int) MissileAccel;                
                        MissileAccel = MissileAccel + 0.75;
                    }
                    else {
                        MissileAccel = 5;
                        UpMissileY = -67;
                        UpMissileVisible = false;
                        DownMissileVisible = true;
                        DoEnemyAI();
                    }
                }
                
                if(DownMissileVisible == true) {
                    if(MissileDestY - DownMissileY < 10) {
                        // WTF!?
                    	new Explosion(SPRITES.get("explosion"), DownMissileX, DownMissileY, theGraphicsBuffer, this);                        
                    	/*BoomBoom.visible = true;
                        BoomBoom.x = DownMissileX;
                        BoomBoom.y = DownMissileY;*/
                        DownMissileY = 640;
                        DownMissileVisible = false;
                    }                    
                    
                    if(DownMissileY < 640) {
                        DownMissileY = DownMissileY + (int) MissileAccel;                
                        MissileAccel = MissileAccel + 0.75;                
                    }
                    else {
                        MissileAccel = 5;
                        DownMissileY = 640;
                        DownMissileVisible = false;                  
                    }
                }
                if(DrakeY > 448) {
                    DrakeY = DrakeY - 5;                
                }
            }
            repaint();			
		}
		else if(e.getSource() == ANIMATION_TIMER)
		{
            FlashGameOverShips = !(FlashGameOverShips);
            
            if(iPigFrame == 0) {
                iPigFrame = 32;
            }
            else {
                iPigFrame = 0;
            }  
            
            repaint();			
		}
	}
	
    public static void main(String[] args) {
        new WarPigs();
    }	
}

