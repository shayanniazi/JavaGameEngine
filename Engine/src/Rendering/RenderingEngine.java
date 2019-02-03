package Rendering;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import Components.BaseLight;
import Components.Camera;
import Components.DirectionalLight;
import Components.PointLight;
import Components.SpotLight;
import CoreEngine.DisplayManager;
import CoreEngine.GameObject;
import MathUtil.Transform;
import MathUtil.Vector3f;
import Model.Material;
import ResourceManagement.MappedValues;
import Shaders.Shader;
import Shaders.ShaderLoader;

public class RenderingEngine extends MappedValues {

	private Camera main_camera;
	private Shader forwardAmbient;
	private HashMap<String, Integer> samplerMap;
	private BaseLight active_light;
	private ArrayList<BaseLight> lights;
	
	public RenderingEngine()
	{
		super();
		init_graphics();
		
		samplerMap = new HashMap<String, Integer>();
		samplerMap.put("ourTexture", 0);
		
		super.addFloat("ambient", 0.2f);
		forwardAmbient = new Shader("forward-ambient");
		lights = new ArrayList<BaseLight>();
	}
	
	public void init_graphics() 
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL32.GL_DEPTH_CLAMP);
		//GL11.glEnable(GL30.GL_FRAMEBUFFER_SRGB);
	}
	

	public void render(GameObject root)
	{
		clear_screen(); 
		lights.clear();
		root.add_to_rendering_engine(this); //all compponents/objects will have this rendering engine instance
		
		root.renderAll(forwardAmbient, this);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GL11.glDepthMask(false); 
		GL11.glDepthFunc(GL11.GL_EQUAL); //will only blend in lights in the visible pixels, not in pixels that are off screen/invisible
		
		for(BaseLight light : lights)
		{
			this.active_light = light;
			root.renderAll(light.get_shader(), this);
		}
		
		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
				
	}

	public void clear_screen()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void clear_color(float red, float green, float blue, float alpha)
	{
		GL11.glClearColor(red, green, blue, alpha);
	}
	
	public int getSamplerSlot(String samplerName)
	{
		return samplerMap.get(samplerName);
	}
	
	public Camera get_main_camera() {
		return main_camera;
	}

	public void set_main_camera(Camera main_camera) {
		this.main_camera = main_camera;
	}
	
	public void updateUniformStruct(Transform transform, Material material, Shader shader, String uniformName, String uniformType)
 	{
		throw new IllegalArgumentException(uniformType + " is not a supported type in RenderingEngine");
 	}

	public BaseLight getActive_light() {
		return active_light;
	}

	public void setActive_light(BaseLight active_light) {
		this.active_light = active_light;
	}
	
	public void add_light(BaseLight light)
	{
		this.lights.add(light);
	}

	public void add_camera(Camera camera) {
		this.main_camera = camera;
	}
	
}
