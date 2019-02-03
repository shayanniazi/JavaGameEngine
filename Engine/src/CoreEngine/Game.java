package CoreEngine;

import Rendering.RenderingEngine;

public abstract class Game {
	
	private GameObject root;

	public void init_game() 
	{
		
	}

	public void update(float delta) 
	{
		root.updateAll(delta);
	}
	public void input(float delta) 
	{
		root.inputAll(delta);
	}
	
	public void render(RenderingEngine rendering_engine)
	{
		rendering_engine.render(get_root());
	}
	
	public void add_object(GameObject object)
	{
		get_root().add_object(object);
	}
	
	private GameObject get_root()
	{
		if(root == null)
			root = new GameObject();
		
		return root;
	}
}