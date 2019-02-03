package Components;

import MathUtil.Vector3f;
import Rendering.RenderingEngine;
import Shaders.Shader;
import Shaders.ShaderLoader;

public class BaseLight extends GameComponent{

	private Vector3f color;
	private float intensity;
	private Shader shader;
	
	public BaseLight(Vector3f color, float intensity)
	{
		this.color = color;
		this.intensity = intensity;
	}
	
	public BaseLight()
	{
		
	}

	@Override
	public void add_to_rendering_engine(RenderingEngine rendering_engine)
	{
		rendering_engine.add_light(this);
	}

	public void set_shader(Shader shader)
	{
		this.shader = shader;
	}
	
	public Shader get_shader() {
		return this.shader;
	}	

	public Vector3f getColor() {
		return color;
	}
	public void setColor(Vector3f color) {
		this.color = color;
	}
	public float getIntensity() {
		return intensity;
	}
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}	
}
