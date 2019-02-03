package MathUtil;

public class Vector3f {

	private float x, y, z;
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getZ()
	{
		return z;
	}
	
	public void setXYZ(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f set(Vector3f v) 
	{
		this.x = v.x; 
		this.y = v.y;
		this.z = v.z;
		
		return this;
	}
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x; 
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector3f v) 
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public Vector3f add(Vector3f v)
	{
		float x_ = x + v.x;
		float y_ = y + v.y;
		float z_ = z + v.z;
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f add(float f)
	{
		return new Vector3f(x+f, y+f, z+f);
	}

	public Vector3f sub(Vector3f v)
	{
		float x_ = x + (-v.x);
		float y_ = y + (-v.y);
		float z_ = z + (-v.z);
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f mul(float scalar)
	{
		float x_ = x * scalar;
		float y_ = y * scalar;
		float z_ = z * scalar;
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f Mul(Vector3f r)
	{
		return new Vector3f(x * r.x, y * r.y, z * r.z);
	}

	public Vector3f div(float val)
	{
		return new Vector3f(x/val, y/val, z/val);
	}
	
	public float magnitude()
	{
		float magnitude = (float) Math.sqrt((x*x) + (y*y) + (z*z));
		return magnitude;
	}
	
	//a normalized vector is one where the new components magnitude amounts to exactly 1. normalized components calculated by:
	//vectorComponent/magnitude
	public Vector3f normalize()
	{
		float magnitude = magnitude();
		
		float x_ = x/magnitude;
		float y_ = y/magnitude;
		float z_ = z/magnitude;
		
		return new Vector3f(x_, y_, z_);
	}
	
	//essentially multiplying component wise and adding them up
	public float dot_product(Vector3f v)
	{
		return ((x*v.x) + (y*v.y) + (z*v.z));
	}
	
	public Vector3f cross_product(Vector3f v)
	{
		float x_, y_, z_;
		
		x_ = (y*v.z - z*v.y);
		y_ = (z*v.x - x*v.z);
		z_ = (x*v.y - y*v.x);
		
		return new Vector3f(x_, y_, z_);
		
	}
	
	public Vector3f negate()
	{
		x = -1 * x;
		y = -1 * y;
		z = -1 * z;
		
		return this;
	}

	public Vector3f rotate(float angle, Vector3f axis) 
	{
		
		Quaternion rotation = new Quaternion(axis, angle);
		
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.Mul(this).Mul(conjugate);
		
		x = w.getX();
		y = w.getY();
		z = w.getZ();
		
		return this;
	}
	
 	public Vector3f rotate(Quaternion rotation)
 	{
 		Quaternion conjugate = rotation.conjugate();
 
 		Quaternion w = rotation.Mul(this).Mul(conjugate);
 
 		return new Vector3f(w.getX(), w.getY(), w.getZ());
 	}
	
	public Vector3f lerp(Vector3f dest, float lerpFactor)
	{
		return dest.sub(this).mul(lerpFactor).add(this);
	}
	
	public Vector3f interpolation(Vector3f vector, float lerp_factor)
	{
		return this.mul(lerp_factor).add(vector.mul(1.0f - lerp_factor));
	}
	
	public float max()
	{
		return Math.max(x, Math.max(y, z));
	}
	
	public boolean equals(Vector3f r)
	{
		return x == r.getX() && y == r.getY() && z == r.getZ();
	}
	
}
