package Editor;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import CoreEngine.DisplayManager;
import CoreEngine.Game;
import CoreEngine.GameObject;
import Game.TestGame2;

public class GUI {

	private static JFrame display;
	private static Canvas main_canvas;
	private JButton b;
	private static int height, width;
	private Game game;
	
	public void init_gui()
	{
		display = new JFrame();
		main_canvas = new Canvas();
		display.setSize(DisplayManager.get_width(), DisplayManager.get_height());
		display.setLocationRelativeTo(null);
		display.setResizable(true);
		
		generate_menu_bar();
		generate_canvas();
		button();
		
		try {
			Display.setParent(main_canvas);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		display.setDefaultCloseOperation(display.EXIT_ON_CLOSE);
		display.setVisible(true);
	}
	
	private void generate_menu_bar()
	{
	      JMenuBar menubar = new JMenuBar();
	      JMenu menu = new JMenu("menu");
	      JMenuItem size = new JMenuItem("item");
	      menu.add(size);
	      menubar.add(menu);
	      display.setJMenuBar(menubar);
	}
	
	private void generate_canvas()
	{
	      main_canvas.setSize(DisplayManager.get_width(), DisplayManager.get_height());     
	      main_canvas.setFocusable(true);                                
	      main_canvas.setIgnoreRepaint(true);                         
	      display.getContentPane().add(main_canvas, BorderLayout.CENTER); 
	}
	
	private void button()
	{
		b = new JButton("CLICK ME ASSHOLE");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				game.add_object(new GameObject());;
			}
		});
		display.add(b, BorderLayout.SOUTH);
	}

	public void dispose_resources()
	{
		display.dispose(); 
	}
	
	public boolean was_resized()
	{
		if(display.getWidth() != DisplayManager.get_width() || display.getHeight() != DisplayManager.get_height())
			return true;
		return false;
	}
	
	public JFrame get_display()
	{
		return display;
	}
	
	public void set_game(Game game)
	{
		this.game = game;
	}
}
