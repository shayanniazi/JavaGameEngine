package Components;

import CoreEngine.GameObject;
import MathUtil.Transform;
import Rendering.RenderingEngine;
import Shaders.Shader;
import Shaders.ShaderLoader;

public abstract class GameComponent {
	private GameObject parent;
	
	public void input(float delta) {}
	public void update(float delta) {}
	public void render(Shader shader, RenderingEngine rendering_engine) {}
	
	public void add_to_rendering_engine(RenderingEngine rendering_engine)
	{
		
	}
	
	public void set_parent(GameObject parent)
	{
		this.parent = parent;
	}
	
	public Transform get_transform()
	{
		return this.parent.get_transform();
	}
}
