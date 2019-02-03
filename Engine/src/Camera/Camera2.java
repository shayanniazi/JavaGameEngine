package Camera;

import CoreEngine.CoreEngine;
import Input.Input;
import MathUtil.Vector3f;
import javafx.scene.transform.Transform;

public class Camera2 {
	
	private Vector3f world_up = new Vector3f(0f,1f,0f);
	private Vector3f position;
	private Vector3f target;
	private Vector3f direction;
	private Vector3f right;
	private Vector3f left;
	private Vector3f up;
	
	public Camera2()
	{
		target = new Vector3f(0f,0f,1f); //for now, we choose the scene origin
		position = new Vector3f(0f,0f,0f);
		up = new Vector3f(0f,1f,0f);
		init();
	}
	
	public void init()
	{
		set_direction();
		set_right();
		set_left();
		set_up();
	}
	
	public void move(Vector3f dir, float amount)
	{
		position = position.add(dir.mul(amount));
	}
	
//	public void input()
//	{
//		float movAmt = (float) (10*CoreEngine.delta_time());
//		float rotAmt = (float) (100*CoreEngine.delta_time());
//
//		if(Input.GetKey(Input.KEY_W))
//			move(direction, movAmt);
//		if(Input.GetKey(Input.KEY_S))
//			move(direction, -movAmt);
//		if(Input.GetKey(Input.KEY_A))
//			move(right, movAmt);
//		if(Input.GetKey(Input.KEY_D))
//			move(left, movAmt);
//		
//		if(Input.GetKey(Input.KEY_UP)) 
//			rotate_x(-rotAmt); 
//		if(Input.GetKey(Input.KEY_DOWN))
//			rotate_x(rotAmt);
//		if(Input.GetKey(Input.KEY_LEFT))
//			rotate_y(-rotAmt);
//		if(Input.GetKey(Input.KEY_RIGHT))
//			rotate_y(rotAmt);
//		
//	}
//	
	public void rotate_y(float angle)
	{
		Vector3f hAxis = world_up.cross_product(direction);
		hAxis = hAxis.normalize();
		
		direction.rotate(angle, world_up);
		direction = direction.normalize();
		
		up = direction.cross_product(hAxis);
		up = up.normalize();
	}
	
	public void rotate_x(float angle)
	{
		Vector3f hAxis = world_up.cross_product(direction);
		hAxis = hAxis.normalize();
		
		direction.rotate(angle, hAxis); 
		direction = direction.normalize();
		
		up = direction.cross_product(hAxis);
		up = up.normalize();
	}
	
	private void set_right()
	{
		this.right = direction.cross_product(up);
	}
	
	private void set_left()
	{
		this.left = up.cross_product(direction);
		left.normalize();
	}
	
	private void set_up()
	{
		this.up = direction.cross_product(this.left);
		up.normalize();
	}
	
	private void set_direction()
	{
		direction = position.sub(target);
		direction.normalize();
	}
	
	public void set_position(Vector3f pos)
	{
		this.position = pos;
	}
	
	public Vector3f get_position()
	{
		return this.position;
	}
	
	public Vector3f get_left()
	{
		return this.left;
	}
	
	public Vector3f get_direction()
	{
		return this.direction;
	}
	
	public Vector3f get_up()
	{
		return this.up;
	}
	
}
