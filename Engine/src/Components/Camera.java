package Components;

import CoreEngine.CoreEngine;
import CoreEngine.DisplayManager;
import Input.Input;
import MathUtil.Matrix4f;
import MathUtil.Quaternion;
import MathUtil.Vector2f;
import MathUtil.Vector3f;
import Rendering.RenderingEngine;

public class Camera extends GameComponent {
	
	public static final Vector3f yAxis = new Vector3f(0,1,0);

	private Matrix4f projection;
	private float fov, aspect, zNear, zFar;
	
	public Camera(float fov, float aspect, float zNear, float zFar)
	{
		this.fov = fov;
		this.aspect = aspect;
		this.zNear = zNear;
		this.zFar = zFar;
		this.projection = new Matrix4f();
		projection.set_perspective_projection_matrix(fov, aspect, zNear, zFar);
	}

	public Matrix4f get_perspective()
	{
		Matrix4f cameraRotation = get_transform().getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = new Matrix4f();
		Vector3f cameraPosition = get_transform().getTransformedPos().mul(-1);
		cameraTranslation.set_translation_matrix(cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ());

		return projection.mul(cameraRotation.mul(cameraTranslation));
	}

	@Override
	public void add_to_rendering_engine(RenderingEngine renderingEngine)
	{
		renderingEngine.add_camera(this);
	}
	
	@Override
	public void update(float delta)
	{
		this.projection.set_perspective_projection_matrix(fov, DisplayManager.get_aspect_ratio(), zNear, zFar);
	}
}

