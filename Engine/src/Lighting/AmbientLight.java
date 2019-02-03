package Lighting;

public class AmbientLight {

	private float light_intensity;

	public AmbientLight()
	{
		
	}
	
	public float get_ambient_light() 
	{
		return light_intensity;
	}

	public void set_ambient_light(float light_intensity) 
	{
		this.light_intensity = light_intensity;
	}
	
}
