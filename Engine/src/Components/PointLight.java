package Components;

import MathUtil.Vector3f;
import Rendering.RenderingEngine;
import Shaders.Shader;
import Shaders.ShaderLoader;

public class PointLight extends BaseLight {

	private static final int COLOR_DEPTH = 256;
	private float constant;
	private float linear;
	private float exponent;
	private float range;
	
	public PointLight(Vector3f color, float intensity, float constant, float linear, float exponent)
	{
		super(color, intensity);
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
		
		set_range();

		set_shader(new Shader("forward-point"));
	}
	
	private void set_range()
	{
		float exp = exponent;
		float lin = linear;
		float con = constant - COLOR_DEPTH * getIntensity() * getColor().max();

		this.range = (float)((-lin + Math.sqrt(lin * lin - 4 * exp * con))/(2 * exp));
	}

	public float get_range()
	{
		return this.range;
	}
	
	public float getConstant() {
		return constant;
	}

	public void setConstant(float constant) {
		this.constant = constant;
	}

	public float getLinear() {
		return linear;
	}

	public void setLinear(float linear) {
		this.linear = linear;
	}

	public float getExponent() {
		return exponent;
	}

	public void setExponent(float exponent) {
		this.exponent = exponent;
	}
}
