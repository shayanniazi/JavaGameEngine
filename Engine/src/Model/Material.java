package Model;

import java.util.HashMap;

import MathUtil.Vector3f;
import ResourceManagement.MappedValues;

public class Material extends MappedValues {

	private HashMap<String, Texture> texture_hashmap;


	public Material()
	{
		super();
		texture_hashmap = new HashMap<String, Texture>();
	}
	
	public void add_texture(String name, Texture texture)
	{
		texture_hashmap.put(name, texture);
	}
	
	public Texture get_texture(String name)
	{
		Texture result = texture_hashmap.get(name);
		
		if(result != null)
			return result;
		
		return new Texture("test.png");
	}
	
}
