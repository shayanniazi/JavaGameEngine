package Game;


import Components.Camera;
import Components.DirectionalLight;
import Components.FreeLook;
import Components.FreeMove;
import Components.MeshRenderer;
import Components.PointLight;
import Components.SpotLight;
import CoreEngine.DisplayManager;
import CoreEngine.Game;
import CoreEngine.GameObject;
import MathUtil.Quaternion;
import MathUtil.Vector2f;
import MathUtil.Vector3f;
import Model.Material;
import Model.Mesh;
import Model.Texture;
import Model.Vertex;
import Input.Input;

public class TestGame2 extends Game {

	//change these variables
	private Mesh mesh, mesh2, mesh3;
	private Material material, material2;
	
	
    Vertex[] vertices2 = new Vertex[] { 
    		new Vertex( new Vector3f( -1.0f, -1.0f, 0.5773f ),new Vector2f( 0.0f, 0.0f ) ),                                                
    		new Vertex( new Vector3f( 0.0f, -1.0f, -1.15475f ), new Vector2f( 0.5f, 0.0f ) ),    
    		new Vertex( new Vector3f( 1.0f, -1.0f, 0.5773f ),new Vector2f( 1.0f, 0 ) ),   
    		new Vertex( new Vector3f( 0.0f, 1.0f, 0.0f ), new Vector2f( 0.5f, 1.0f ) ) };        
    int[] indices2 = new int[] {
    		0, 3, 1,               
    		1, 3, 2,                
    		2, 3, 0,               
    		1, 2, 0 };
	
	float fieldDepth = 10.0f;
	float fieldWidth = 10.0f;
	
	Vertex[] vertices = new Vertex[] { 	new Vertex( new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
										new Vertex( new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
										new Vertex( new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
										new Vertex( new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};
	
	int indices[] = { 0, 1, 2,
					  2, 1, 3};
	

	public TestGame2()
	{

	}
	
	public void init_game()
	{
		//model = OBJLoader.load("cube.obj");
	//	camera = new Camera();
		GameObject obj = new GameObject();
		GameObject directional_light = new GameObject();
		GameObject point_light = new GameObject();
		GameObject spot_light = new GameObject();
		GameObject camera = new GameObject();

		mesh = new Mesh(vertices, indices, true);
		mesh2 = new Mesh(vertices2, indices2, true);
		mesh3 = new Mesh("capsule.obj");
		material = new Material(); material.add_texture("ourTexture", new Texture("jpgBrick.jpg"));
		material2 = new Material(); material2.add_texture("ourTexture", new Texture("test.png"));
		material.addFloat("specularIntensity", 2f);
		material.addFloat("specularPower", 32f);
		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);
		
		obj.add_component(meshRenderer);
		obj.get_transform().translate(0, -1, 5);
		directional_light.add_component(new DirectionalLight(new Vector3f(1,1,1), 0.4f)); 
		point_light.add_component(new PointLight(new Vector3f(0,1,0), 0.4f, 0,0,1));
		
		spot_light.add_component(
				 new SpotLight(new Vector3f(0,1,1), 0.4f,
							0,0,0.1f, 0.7f));
		camera.add_component(new Camera((float)Math.toRadians(70.0f), DisplayManager.get_aspect_ratio(), 0.01f, 1000.0f));		 
		
		spot_light.get_transform().translate(-5, 0, 5);
		spot_light.get_transform().rotate(new Vector3f(0,1,0), -90);
		directional_light.get_transform().rotate(new Vector3f(1,0,0), -45);
		
		GameObject testMesh1 = new GameObject().add_component(new MeshRenderer(mesh2, material2));
		GameObject testMesh2 = new GameObject().add_component(new MeshRenderer(mesh2, material2));
		GameObject testMesh3 = new GameObject().add_component(new MeshRenderer(mesh3, material));
		
		testMesh3.get_transform().translate(0,2,10);
		testMesh1.get_transform().translate(0, 2, 0);
		testMesh1.get_transform().rotate(new Vector3f(0,1,0), 45f);
		testMesh2.get_transform().translate(0, 0, 5);
		testMesh1.add_object(testMesh2);
		testMesh2.add_object(camera.add_component(new FreeLook(3f)).add_component(new FreeMove(5f)));
		testMesh3.add_component(new FreeMove(5f, Input.KEY_UP, Input.KEY_DOWN, Input.KEY_LEFT, Input.KEY_RIGHT));
		
		add_object(testMesh1);
		add_object(testMesh3);
		add_object(new GameObject().add_component(new MeshRenderer(new Mesh("capsule.obj"), material)));
		add_object(obj);
		add_object(directional_light);
		add_object(point_light);
		add_object(spot_light);
		System.out.println(testMesh1.getClass().getName());
	}
}
