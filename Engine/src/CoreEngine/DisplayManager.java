package CoreEngine;
import java.awt.BorderLayout;
import java.awt.Canvas;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import Editor.GUI;

public class DisplayManager {

	private static int width, height;
	private boolean has_gui_display = false;
	private GUI gui;
	private Game game;
	
	public DisplayManager(int width, int height)
	{
		this.height = height;
		this.width = width;
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setResizable(true);
			
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void create_display(boolean enable_gui)
	{	
		has_gui_display = enable_gui;
		try 
		{	
			if(has_gui_display)
			{
				gui = new GUI();
				gui.init_gui();	
				Display.create();
				Mouse.create();
				Keyboard.create();
				GL11.glViewport(0, height/2, width/2, height/2); //default viewport for the graphics with GUI (editor)
			}
			else
			{
				Display.create(); 			
				Mouse.create();
				Keyboard.create();
			}

		}
		catch (LWJGLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void update()
	{
		if(Display.wasResized())
		{
			width = Display.getWidth();
			height = Display.getHeight();
			GL11.glViewport(0, 0, width, height);
		}
		if(has_gui_display)
			if(gui.was_resized())
			{
				width = gui.get_display().getWidth();
				height = gui.get_display().getHeight();
				GL11.glViewport(0, height/2, width/2, height/2);
			}
		Display.update(); 
	}
	
	public int get_refresh_rate()
	{
		return Display.getDesktopDisplayMode().getFrequency();
	}
	
	public void dispose()
	{
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}

	
	public void vsync(int FPS, boolean enable)
	{
		if(enable)
			Display.sync(FPS);
	}
	
	public static int get_width()
	{
		return width;
	}
	
	public static int get_height()
	{
		return height;
	}

	public static float get_aspect_ratio() {
		return ((float)width/(float)height);
	}
	
	public void set_game(Game game)
	{
		this.game = game;
		//this.gui.set_game(game);
	}
}
