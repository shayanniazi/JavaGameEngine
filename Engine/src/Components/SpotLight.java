package Components;

import MathUtil.Vector3f;
import Shaders.Shader;
import Shaders.ShaderLoader;

public class SpotLight extends PointLight
{
	private Vector3f direction;
	private float cutoff;
	
	public SpotLight(Vector3f color, float intensity, float constant, float linear, float exponent, float cutoff)
	{
		super(color, intensity, constant, linear, exponent);
		this.cutoff = cutoff;
		set_shader(new Shader("forward-spot"));
	}

	public Vector3f getDirection()
	{
		return get_transform().getTransformedRot().getForward();
	}
	
	public float getCutoff()
	{
		return cutoff;
	}
	public void setCutoff(float cutoff)
	{
		this.cutoff = cutoff;
	}
}