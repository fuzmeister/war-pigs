package com.sevensoupcans.sfsoftware.warpigs;
/*
 * Explosion.java
 *
 * Created on January 31, 2007, 8:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.Timer;

import com.sevensoupcans.sfsoftware.warpigs.game.Sprite;

/**
 * @author stthompson
 */
public class Explosion extends Sprite implements ActionListener {    
	
    /** Creates a new instance of Explosion */
    public Explosion(Image img, int x, int y, Graphics g, Component io) {      
    	super(img, x, y, 32, 32, g, io);
        
    	Timer animationTimer = new Timer(100, this);
        animationTimer.start();    
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
        if(visible == true) {
            srcX = srcX + 32;
            if(srcX > 224) {
                //frame = 0;
                visible = false;
            }
        }		
	}   
}
