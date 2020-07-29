/*
 * Tile.java
 *
 * Created on January 29, 2007, 9:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author stthompson
 */
package com.sevensoupcans.sfsoftware.warpigs.game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Tile extends JPanel {

	private static final long serialVersionUID = 1L;
	public int x;
    public int y;
    public int status;
    public boolean checkedAlready;
    
    public Tile(int iX, int iY) {
        checkedAlready = false;
        status = 0;
        x = iX;
        y = iY;
                
    }

    public int checkStatus() {
        if(checkedAlready == false) {
            checkedAlready = true;
        }
        
        return status;
    }
    
    public void paintComponent(Graphics g) {        
        //graphicsBuffer.drawImage(imgDefault, x, y, this);
        
        g.setColor(Color.WHITE);
        //g.fillRect(x, y, 32, 32);        
        
        if(checkedAlready == true) {
            switch(status) {
                case 5:
                    g.setColor(Color.DARK_GRAY);
                    //g.fillRect(x, y, 32, 32);
                    
                    break;
                case 1:
                    g.setColor(Color.RED);
                    //g.fillRect(x+1, y+1, 31, 31);
                    break;
            }   
        }
        else {
            g.setColor(Color.BLUE);
        }
        
    }

    public void actionPerformed(ActionEvent e) {
    }
    
    public boolean isOpaque() {
        return true;
    }


}
