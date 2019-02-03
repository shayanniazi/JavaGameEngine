package CoreEngine;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import Input.Input;
import MathUtil.Matrix4f;
import MathUtil.Transform;
import MathUtil.Vector3f;
import Model.Mesh;
import Model.Model;
import Model.OBJLoader;
import Model.Vertex;
import Rendering.RenderingEngine;
import Shaders.ShaderLoader;

public class CoreEngine {
	
	private boolean is_running = false;
	private boolean render = false;
	private boolean fixed_time_step = false;
	private boolean vsync = false;
	
	private double DELTA_TIME;
	private double old_time, new_time;
	private double time_passed = 0;
	
	private double frame_rate;
	private int dynamic_frame_rate = (int) DELTA_TIME; //just initializing it to fixed time step delta for now
	private double FRAME_CAP; //holds the value of 1/60 but in milliseconds i.e 1000/frame_rate
	private int fixed_ticks = 0;
	private int dynamic_ticks = 0; 
	private double frame_counter;
	
	private DisplayManager display;
	private Game game;
	private RenderingEngine rendering_engine;
	
	private static final CoreEngine engine = new CoreEngine();
	
	public static CoreEngine get_engine()
	{
		return engine;
	}
	
	private CoreEngine() {}
	
	public void init_engine(int width, int height, boolean enable_gui)
	{
		display = new DisplayManager(width, height); 	
		display.create_display(enable_gui); 

		rendering_engine = new RenderingEngine();
	}
	
	//by default game will use dynamic update
	public void init_game(Game game, boolean enable_vsync)
	{
		this.vsync = enable_vsync;
		this.game = game; 
		this.game.init_game();
		this.is_running = true; 
		this.render = true;
		run_game();
	}
	
	public void init_game(Game game, boolean time_step, double frame_rate, boolean enable_vsync)
	{
		this.frame_rate = frame_rate;
		this.FRAME_CAP = 1.0/frame_rate; //since everything is computed in milliseconds, not seconds, otherwise would be 1/60
		this.fixed_time_step = time_step;
		this.game = game; 
		this.game.init_game();
		this.is_running = true; 
		this.render = true;
		
		if(time_step)
			display.vsync((int)frame_rate, enable_vsync);
		
		run_game();
	}
	
	//returns the time in seconds
	public float delta_time()
	{
		return (float) FRAME_CAP;
		//essentially converting back to 1/frame_rate i.e 1/60 in our case
	}
	
	public float dynamic_delta()
	{
		return (1/(float)dynamic_frame_rate);
	}
	
	public void stop_game()
	{
		is_running = false;
		render = false;
		display.dispose();
	}
	
	private void run_game()
	{
			display.set_game(this.game);
			old_time = Time.get_time_seconds();
			//game.init_game();
			//game loop
			while(is_running)
			{
				
				new_time = Time.get_time_seconds();
				DELTA_TIME = new_time - old_time; //amount of time it takes to complete 1 frame
				old_time = new_time;
				time_passed = time_passed + DELTA_TIME;
				frame_counter = frame_counter + DELTA_TIME;
				
				//insert dynamic update loop here if needed
				if(!fixed_time_step) 
				{
					game.input(dynamic_delta());
					Input.Update();
					game.update(dynamic_delta());
					time_passed = time_passed - FRAME_CAP; //will basically reset time_passed to 0
					dynamic_ticks++;
				}
				else
				{
					//fixed time step update loop (UPDATE LOOP - as fast as FRAME_CAP allows)
					while(time_passed >= FRAME_CAP)
					{
						//render = true;
						if(fixed_ticks > frame_rate)
							break;
					
						game.input(delta_time());
						Input.Update();
						game.update(delta_time());
						time_passed = time_passed - FRAME_CAP; //will basically reset time_passed to 0
						fixed_ticks++;
					
						if(Display.isCloseRequested())
						{
							stop_game();
						}
					}
				}
				
				//RENDER HERE (as fast as system will allow)
				if(render)
				{
					game.render(rendering_engine);;
					display.update();
					
					if(Display.isCloseRequested())
					{
						stop_game();
					}
				}
				
				//frame rate counter when it reaches 1000ms (1 second)
				if(frame_counter >= 1.0)
				{
					if(fixed_time_step)
					{
						System.out.println("FPS = " + fixed_ticks);
						frame_counter = 0;
						fixed_ticks = 0;
					}
					else //dynamic frame rate counter
					{
						System.out.println("FPS = " + dynamic_ticks);
						frame_counter = 0;
						dynamic_frame_rate = dynamic_ticks;
						dynamic_ticks = 0;
						
						if(vsync)
							display.vsync(dynamic_frame_rate, vsync);
					}
				}
			}
		}

}
	
