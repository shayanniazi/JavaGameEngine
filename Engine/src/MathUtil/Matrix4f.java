package MathUtil;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Matrix4f {

	private float[][] matrix4f;
	
	public Matrix4f()
	{
		this.matrix4f = new float[4][4];
	}
	
	public Matrix4f identity_matrix()
	{
		this.matrix4f[0][0] = 1; 		this.matrix4f[0][1] = 0;		this.matrix4f[0][2] = 0;		this.matrix4f[0][3] = 0;
		this.matrix4f[1][0] = 0; 		this.matrix4f[1][1] = 1;		this.matrix4f[1][2] = 0;		this.matrix4f[1][3] = 0;
		this.matrix4f[2][0] = 0; 		this.matrix4f[2][1] = 0;		this.matrix4f[2][2] = 1;		this.matrix4f[2][3] = 0;
		this.matrix4f[3][0] = 0; 		this.matrix4f[3][1] = 0;		this.matrix4f[3][2] = 0;		this.matrix4f[3][3] = 1;
		
		return this;
	}
	
	public void set_translation_matrix(float x, float y, float z)
	{
		this.matrix4f[0][0] = 1; 		this.matrix4f[0][1] = 0;		this.matrix4f[0][2] = 0;		this.matrix4f[0][3] = x;
		this.matrix4f[1][0] = 0; 		this.matrix4f[1][1] = 1;		this.matrix4f[1][2] = 0;		this.matrix4f[1][3] = y;
		this.matrix4f[2][0] = 0; 		this.matrix4f[2][1] = 0;		this.matrix4f[2][2] = 1;		this.matrix4f[2][3] = z;
		this.matrix4f[3][0] = 0; 		this.matrix4f[3][1] = 0;		this.matrix4f[3][2] = 0;		this.matrix4f[3][3] = 1;
	}
	
	public void set_scaling_matrix(float x, float y, float z)
	{
		this.matrix4f[0][0] = x; 		this.matrix4f[0][1] = 0;		this.matrix4f[0][2] = 0;		this.matrix4f[0][3] = 0;
		this.matrix4f[1][0] = 0; 		this.matrix4f[1][1] = y;		this.matrix4f[1][2] = 0;		this.matrix4f[1][3] = 0;
		this.matrix4f[2][0] = 0; 		this.matrix4f[2][1] = 0;		this.matrix4f[2][2] = z;		this.matrix4f[2][3] = 0;
		this.matrix4f[3][0] = 0; 		this.matrix4f[3][1] = 0;		this.matrix4f[3][2] = 0;		this.matrix4f[3][3] = 1;
	}
	
	public void set_rotation_matrix(float xAngle, float yAngle, float zAngle)
	{
		Matrix4f rx = new Matrix4f();
		Matrix4f ry = new Matrix4f();
		Matrix4f rz = new Matrix4f();
		Matrix4f result = new Matrix4f();
		
		//converting angle from degrees to radians then calculating cosine and sine functions
		xAngle = (float) Math.toRadians(xAngle); 		
		yAngle = (float) Math.toRadians(yAngle);		
		zAngle = (float) Math.toRadians(zAngle);

		float sinX = (float) Math.sin(xAngle);
		float sinY = (float) Math.sin(yAngle);
		float sinZ = (float) Math.sin(zAngle);
		
		float cosX = (float) Math.cos(xAngle);
		float cosY = (float) Math.cos(yAngle);
		float cosZ = (float) Math.cos(zAngle);
		
		
		rx.matrix4f[0][0] = 1; 			rx.matrix4f[0][1] = 0;			rx.matrix4f[0][2] = 0;			rx.matrix4f[0][3] = 0;
		rx.matrix4f[1][0] = 0; 			rx.matrix4f[1][1] = cosX;		rx.matrix4f[1][2] = -(sinX);	rx.matrix4f[1][3] = 0;
		rx.matrix4f[2][0] = 0; 			rx.matrix4f[2][1] = sinX;		rx.matrix4f[2][2] = cosX;		rx.matrix4f[2][3] = 0;
		rx.matrix4f[3][0] = 0; 			rx.matrix4f[3][1] = 0;			rx.matrix4f[3][2] = 0;			rx.matrix4f[3][3] = 1;
		
		ry.matrix4f[0][0] = cosY; 		ry.matrix4f[0][1] = 0;			ry.matrix4f[0][2] = sinY;		ry.matrix4f[0][3] = 0;
		ry.matrix4f[1][0] = 0; 			ry.matrix4f[1][1] = 1;			ry.matrix4f[1][2] = 0;			ry.matrix4f[1][3] = 0;
		ry.matrix4f[2][0] = -(sinY); 	ry.matrix4f[2][1] = 0;			ry.matrix4f[2][2] = cosY;		ry.matrix4f[2][3] = 0;
		ry.matrix4f[3][0] = 0; 			ry.matrix4f[3][1] = 0;			ry.matrix4f[3][2] = 0;			ry.matrix4f[3][3] = 1;

		rz.matrix4f[0][0] = cosZ; 		rz.matrix4f[0][1] = -(sinZ);	rz.matrix4f[0][2] = 0;			rz.matrix4f[0][3] = 0;
		rz.matrix4f[1][0] = sinZ; 		rz.matrix4f[1][1] = cosZ;		rz.matrix4f[1][2] = 0;			rz.matrix4f[1][3] = 0;
		rz.matrix4f[2][0] = 0; 			rz.matrix4f[2][1] = 0;			rz.matrix4f[2][2] = 1;			rz.matrix4f[2][3] = 0;
		rz.matrix4f[3][0] = 0; 			rz.matrix4f[3][1] = 0;			rz.matrix4f[3][2] = 0;			rz.matrix4f[3][3] = 1;

		result = rz.mul(ry.mul(rx));
		this.matrix4f = result.matrix4f;
	}
	
	public void set_perspective_projection_matrix(float fov, float aspect_ratio0, float zNear, float zFar)
	{
		
		float aspect_ratio = aspect_ratio0;
		float tan_fov_by2 = (float) Math.tan(fov/2);
		float zRange = zNear - zFar;
		
		this.matrix4f[0][0] = 1.0f/(tan_fov_by2 * aspect_ratio); 		this.matrix4f[0][1] = 0;					this.matrix4f[0][2] = 0;							this.matrix4f[0][3] = 0;
		this.matrix4f[1][0] = 0; 										this.matrix4f[1][1] = 1.0f/tan_fov_by2;		this.matrix4f[1][2] = 0;							this.matrix4f[1][3] = 0;
		this.matrix4f[2][0] = 0; 										this.matrix4f[2][1] = 0;					this.matrix4f[2][2] = (-zNear - zFar)/zRange;		this.matrix4f[2][3] = 2 * zFar * zNear / zRange;
		this.matrix4f[3][0] = 0; 										this.matrix4f[3][1] = 0;					this.matrix4f[3][2] = 1;							this.matrix4f[3][3] = 0;
	}
	
	public void set_orthographic_view(float left, float right, float bottom, float top, float near, float far)
	{
		float width = right - left;
		float height = top - bottom;
		float depth = far - near;

		this.matrix4f[0][0] = width; 		this.matrix4f[0][1] = 0;			this.matrix4f[0][2] = 9;			this.matrix4f[0][3] = left;
		this.matrix4f[1][0] = 0; 			this.matrix4f[1][1] = height;	    this.matrix4f[1][2] = 9;			this.matrix4f[1][3] = bottom;
		this.matrix4f[2][0] = 0;			this.matrix4f[2][1] = 0;			this.matrix4f[2][2] = depth;		this.matrix4f[2][3] = near;
		this.matrix4f[3][0] = 0; 			this.matrix4f[3][1] = 0;			this.matrix4f[3][2] = 0;			this.matrix4f[3][3] = 1;

	}
	
	public Matrix4f init_rotation(Vector3f forward, Vector3f up)
	{
		Vector3f f = forward;
		f = f.normalize();
		
		//getting right axis
		Vector3f r = up;
		r = r.normalize();
		r = r.cross_product(f);
		
		Vector3f u = f.cross_product(r);
		
		return init_rotation(f, u, r);
	}
	
	public Matrix4f init_rotation(Vector3f forward, Vector3f up, Vector3f right)
	{

		this.matrix4f[0][0] = right.getX(); 		this.matrix4f[0][1] = right.getY();			this.matrix4f[0][2] = right.getZ();			this.matrix4f[0][3] = 0;
		this.matrix4f[1][0] = up.getX(); 			this.matrix4f[1][1] = up.getY();			this.matrix4f[1][2] = up.getZ();			this.matrix4f[1][3] = 0;
		this.matrix4f[2][0] = forward.getX();		this.matrix4f[2][1] = forward.getY();		this.matrix4f[2][2] = forward.getZ();		this.matrix4f[2][3] = 0;
		this.matrix4f[3][0] = 0; 					this.matrix4f[3][1] = 0;					this.matrix4f[3][2] = 0;					this.matrix4f[3][3] = 1;
		
		return this;
	}

	public Matrix4f mul(Matrix4f m)
	{
		Matrix4f result = new Matrix4f();
		
		for(int row = 0; row < 4; row++)
		{
			for(int col = 0; col < 4; col++)
			{
				result.matrix4f[row][col] = this.matrix4f[row][0] * m.matrix4f[0][col] + 
								   			this.matrix4f[row][1] * m.matrix4f[1][col] +
								   			this.matrix4f[row][2] * m.matrix4f[2][col] +
								   			this.matrix4f[row][3] * m.matrix4f[3][col];
			}
		}
		return result;		
	}
	
	public Matrix4f add(Matrix4f m)
	{
		Matrix4f result = new Matrix4f();
		
		for(int row = 0; row < 4; row++)
		{
			for(int col = 0; col <4; col++)
			{
				result.matrix4f[row][col] = this.matrix4f[row][col] + m.matrix4f[row][col];
			}
		}
		
		return result;
	}
	
	public Matrix4f sub(Matrix4f m)
	{
		Matrix4f result = new Matrix4f();
		
		for(int row = 0; row < 4; row++)
		{
			for(int col = 0; col <4; col++)
			{
				result.matrix4f[row][col] = this.matrix4f[row][col] - m.matrix4f[row][col];
			}
		}
		
		return result;
	}
	
	public void set_matrix(float[][] matrix)
	{
		this.matrix4f = matrix;
	}
	
	
	public float get(int row, int col)
	{
		return matrix4f[row][col];
	}
	
	public void set(int row, int col, float value)
	{
		matrix4f[row][col] = value;
	}

	public Vector3f transform(Vector3f r)
	{
			return new Vector3f(this.matrix4f[0][0] * r.getX() + this.matrix4f[0][1] * r.getY() + this.matrix4f[0][2] * r.getZ() + this.matrix4f[0][3],
			            		this.matrix4f[1][0] * r.getX() + this.matrix4f[1][1] * r.getY() + this.matrix4f[1][2] * r.getZ() + this.matrix4f[1][3],
			            		this.matrix4f[2][0] * r.getX() + this.matrix4f[2][1] * r.getY() + this.matrix4f[2][2] * r.getZ() + this.matrix4f[2][3]);
	}

}
