package MathUtil;

import Camera.Camera2;
import Components.Camera;

public class Transform {

	private Vector3f position;
	private Vector3f scale;
	private Quaternion rotation;
	
	private Vector3f oldPos;
	private Quaternion oldRot;
	private Vector3f oldScale;
	
	private Transform parent;
	private Matrix4f parent_matrix;

	//NOTE: if you set the scale to 0, object will become invisible
	public Transform()
	{
		this.position = new Vector3f(0,0,0);
		this.scale = new Vector3f(1,1,1); 
		this.rotation = new Quaternion(0,0,0,1);
		this.parent_matrix = new Matrix4f().identity_matrix();
		
	}
	
	public Matrix4f get_transformation()
	{
		Matrix4f translation_matrix = new Matrix4f();
		Matrix4f rotation_matrix = new Matrix4f();
		Matrix4f scaling_matrix = new Matrix4f();
		
		translation_matrix.set_translation_matrix(this.position.getX(), this.position.getY(), this.position.getZ());
		//rotation_matrix.set_rotation_matrix(this.rotation.getX(), this.rotation.getY(), this.rotation.getZ());
		rotation_matrix = rotation.toRotationMatrix();
		scaling_matrix.set_scaling_matrix(this.scale.getX(), this.scale.getY(), this.scale.getZ());

		return get_parent_matrix().mul(translation_matrix.mul(rotation_matrix).mul(scaling_matrix));
	}
	
	public boolean hasChanged()
	{
		if(oldPos == null)
		{
			oldPos = new Vector3f(position);
			oldRot = new Quaternion(rotation);
			oldScale = new Vector3f(scale);
			return true;
		}

		if(parent != null && parent.hasChanged())
			return true;

		if(!position.equals(oldPos))
			return true;

		if(!rotation.equals(oldRot))
			return true;

		if(!scale.equals(oldScale))
			return true;

		return false;
	}
	
	private Matrix4f get_parent_matrix()
	{
		if(parent!=null && parent.hasChanged())
			this.parent_matrix = parent.get_transformation();
		
		return parent_matrix;
	}
	
	public Vector3f getTransformedPos()
	{
		return get_parent_matrix().transform(position);
	}

	public Quaternion getTransformedRot()
	{
		Quaternion parentRotation = new Quaternion(0,0,0,1);

		if(parent != null)
			parentRotation = parent.getTransformedRot();

		return parentRotation.Mul(rotation);
	}
	
	public void update()
	{
		if(oldPos != null)
		{
			oldPos = position;
			oldRot = rotation;
			oldScale = scale;
		}
		else
		{
			oldPos = position.add(1.0f);
			oldRot = rotation.mul(0.5f);
			oldScale = scale.add(1.0f);
		}
	}
	
	public void translate(float x, float y, float z)
	{
		this.position.setXYZ(x, y, z);
	}
	
	public Vector3f get_position()
	{
		return this.position;
	}
	
	public void rotate(Quaternion rotation)
	{
		this.rotation = rotation;
	}
	
	public void rotate(Vector3f axis, float angle)
	{
		rotation = new Quaternion(axis, angle).Mul(rotation).normalize();
	}
	
	public Quaternion get_rotation()
	{
		return this.rotation;
	}
	
	public void scale(float x, float y, float z)
	{
		this.scale.setXYZ(x, y, z);
	}

	public void translate(Vector3f position) {
		this.position = position;
	}

	public void set_parent(Transform transform) 
	{
		parent = transform;
	}
		
}