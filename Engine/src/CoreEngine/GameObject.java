package CoreEngine;

import java.util.ArrayList;

import Components.GameComponent;
import MathUtil.Transform;
import Rendering.RenderingEngine;
import Shaders.Shader;
import Shaders.ShaderLoader;

public class GameObject {

	private ArrayList<GameObject> game_objects;
	private ArrayList<GameComponent> game_components;
	private Transform transform;
	
	public GameObject()
	{
		game_objects = new ArrayList<GameObject>();
		game_components = new ArrayList<GameComponent>();
		transform = new Transform();
	}
	
	public void add_object(GameObject object)
	{
		game_objects.add(object);
		object.get_transform().set_parent(transform);
	}
	
	public GameObject add_component(GameComponent component)
	{
		game_components.add(component);
		component.set_parent(this);
		return this;
	}
	
	public ArrayList<GameObject> getAllAttached()
	{
		ArrayList<GameObject> result = new ArrayList<GameObject>();

		for(GameObject child : game_objects)
			result.addAll(child.getAllAttached());

		result.add(this);
		return result;
	}
	
	public void inputAll(float delta)
	{
		input(delta);

		for(GameObject child : game_objects)
			child.inputAll(delta);
	}

	public void updateAll(float delta)
	{
		update(delta);

		for(GameObject child : game_objects)
			child.updateAll(delta);
	}

	public void renderAll(Shader shader, RenderingEngine renderingEngine)
	{
		render(shader, renderingEngine);

//		for(GameObject child : game_objects)
//			child.renderAll(shader, renderingEngine);
//		the above code was causing a runtime error when adding in new items to the game_objects list		
		for(int i = 0; i < game_objects.size(); i++)
			game_objects.get(i).renderAll(shader, renderingEngine);
	}
	
	public void input(float delta)
	{
		transform.update();
		
		for(GameComponent game_component: game_components)
			game_component.input(delta);
	}
	
	public void update(float delta)
	{
		for(GameComponent game_component: game_components)
			game_component.update(delta);
	}
	
	public void render(Shader shader, RenderingEngine rendering_engine)
	{
		for(GameComponent game_component: game_components)
			game_component.render(shader, rendering_engine);
	}
	
	public void add_to_rendering_engine(RenderingEngine rendering_engine)
	{
		for(GameObject game_object: game_objects)
			game_object.add_to_rendering_engine(rendering_engine);
		for(GameComponent game_component: game_components)
			game_component.add_to_rendering_engine(rendering_engine);
	}
	
	public Transform get_transform()
	{
		return this.transform;
	}
	
}
