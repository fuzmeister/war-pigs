package com.sevensoupcans.sfsoftware.warpigs.game;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

public class Sprite extends Thread {
	private static int spriteCount = 0;
	private static Graphics BUFFER;
	private static Component IMAGE_OBSERVER;	
    
	protected final Image IMAGE;
    
    private final int HEIGHT;
    private final int WIDTH;
    
	public int x;
    public int y;
    
    protected int srcX = 0;
    protected int srcY = 0;
    
    public boolean visible = false;
    
    public Sprite(Image img, int x, int y, int width, int height, Graphics g, Component io)
    {
    	super("Sprite #" + spriteCount++);
    	IMAGE = img;
    	BUFFER = g;
    	IMAGE_OBSERVER = io;
    	WIDTH = width;
    	HEIGHT = height;
    	
    	this.x = x;
    	this.y = y;
    	
    	visible = true;
    
    	start();
    }
    
    private void drawImage(int x, int y, int srcX, int srcY)
    {
    	BUFFER.drawImage(IMAGE, x, y, x + WIDTH, y + HEIGHT, srcX, srcY, srcX + WIDTH, srcY + HEIGHT, IMAGE_OBSERVER);    	
    }
    
    public void destroy()
    {
    	visible = false;
    }
    
	@Override
	public void run() {				
		System.out.println(BUFFER);
		System.out.println(this + " started");
		
		do
		{
			drawImage(x, y, srcX, srcY);         			
		} while(visible);
		
		System.out.println(this + " ended");
	}
	
    @Override
    public String toString()
    {
    	return getName();
    }
}
