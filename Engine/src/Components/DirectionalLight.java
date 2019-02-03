package Components;

import MathUtil.Vector3f;
import Rendering.RenderingEngine;
import Shaders.Shader;
import Shaders.ShaderLoader;

public class DirectionalLight extends BaseLight {
	
	public DirectionalLight(Vector3f color, float intensity)
	{
		super(color, intensity);
		set_shader(new Shader("forward-directional"));
	}
	
	public Vector3f getDirection() {
		return super.get_transform().getTransformedRot().getForward();
	}

}
