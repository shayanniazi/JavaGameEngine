package MathUtil;

public class Quaternion {

	private float x;
	private float y;
	private float z;
	private float w;
	
	public Quaternion()
	{
		this(0,0,0,1); //setting to default rotation i.e unchanged/wont change/ will remain just as it was
	}
	
	public Quaternion (Vector3f axis, float angle)
	{
		float sin_half = (float) Math.sin(Math.toRadians(angle/2));
		float cos_half = (float) Math.cos(Math.toRadians(angle/2));
		
		this.x = axis.getX() * sin_half;
		this.y = axis.getY() * sin_half;
		this.z = axis.getZ() * sin_half; 
		this.w = cos_half;
		
	}
	
	public Quaternion(float x, float y, float z, float w) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Quaternion(Quaternion quaternion) 
	{
		this.x = quaternion.x;
		this.y = quaternion.y;
		this.z = quaternion.z;
		this.w = quaternion.w;
	}

	public float magnitude()
	{
		return (float) Math.sqrt(x*x + y*y + z*z + w*w);
	}
	
	public Quaternion normalize()
	{
		float magnitude = magnitude();
		float x = this.x/magnitude;
		float y = this.y/magnitude;
		float z = this.z/magnitude;
		float w = this.w/magnitude;
		
		return new Quaternion(x,y,z,w);
	}
	
	public Quaternion conjugate()
	{
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion Mul(Quaternion r)
	{
		float w_ = w * r.w - x * r.x - y * r.y - z * r.z;
		float x_ = x * r.w + w * r.x + y * r.z - z * r.y;
		float y_ = y * r.w + w * r.y + z * r.x - x * r.z;
		float z_ = z * r.w + w * r.z + x * r.y - y * r.x;
		
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion Mul(Vector3f r)
	{
		float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ =  w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ =  w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ =  w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion mul(float r)
	{
		return new Quaternion(x * r, y * r, z * r, w * r);
	}
/*	public Quaternion init_rotation(Vector3f axis, float angle)
	{
		float sin_half = (float) Math.sin(Math.toRadians(angle/2));
		float cos_half = (float) Math.cos(Math.toRadians(angle/2));
		
		this.x = axis.getX() * sin_half;
		this.y = axis.getY() * sin_half;
		this.z = axis.getZ() * sin_half; 
		this.w = cos_half;
		
		return this;
	}*/
	
	public Matrix4f toRotationMatrix()
	{
		Vector3f forward =  new Vector3f(2.0f * (x*z - w*y), 2.0f * (y*z + w*x), 1.0f - 2.0f * (x*x + y*y));
		Vector3f up = new Vector3f(2.0f * (x*y + w*z), 1.0f - 2.0f * (x*x + z*z), 2.0f * (y*z - w*x));
		Vector3f right = new Vector3f(1.0f - 2.0f * (y*y + z*z), 2.0f * (x*y - w*z), 2.0f * (x*z + w*y));
		
		return new Matrix4f().init_rotation(forward, up, right);
	}

	public Vector3f getForward()
	{
		return new Vector3f(0,0,1).rotate(this);
	}

	public Vector3f getBack()
	{
		return new Vector3f(0,0,-1).rotate(this);
	}

	public Vector3f getUp()
	{
		return new Vector3f(0,1,0).rotate(this);
	}

	public Vector3f getDown()
	{
		return new Vector3f(0,-1,0).rotate(this);
	}

	public Vector3f getRight()
	{
		return new Vector3f(1,0,0).rotate(this);
	}

	public Vector3f getLeft()
	{
		return new Vector3f(-1,0,0).rotate(this);
}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	
	public boolean equals(Quaternion r)
	{
		return x == r.getX() && y == r.getY() && z == r.getZ() && w == r.getW();
	}
	
}
