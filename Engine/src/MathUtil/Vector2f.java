package MathUtil;

public class Vector2f 
{
	private float x;
	private float y;
	
	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public float Length()
	{
		return (float)Math.sqrt(x * x + y * y);
	}

	public float Max()
	{
		return Math.max(x, y);
	}

	public float Dot(Vector2f r)
	{
		return x * r.GetX() + y * r.GetY();
	}
	
	public Vector2f Normalized()
	{
		float length = Length();
		
		return new Vector2f(x / length, y / length);
	}

	public float Cross(Vector2f r)
	{
		return x * r.GetY() - y * r.GetX();
	}

	public Vector2f Lerp(Vector2f dest, float lerpFactor)
	{
		return dest.Sub(this).Mul(lerpFactor).Add(this);
	}

	public Vector2f Rotate(float angle)
	{
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		
		return new Vector2f((float)(x * cos - y * sin),(float)(x * sin + y * cos));
	}
	
	public Vector2f Add(Vector2f r)
	{
		return new Vector2f(x + r.GetX(), y + r.GetY());
	}
	
	public Vector2f Add(float r)
	{
		return new Vector2f(x + r, y + r);
	}
	
	public Vector2f Sub(Vector2f r)
	{
		return new Vector2f(x - r.GetX(), y - r.GetY());
	}
	
	public Vector2f Sub(float r)
	{
		return new Vector2f(x - r, y - r);
	}
	
	public Vector2f Mul(Vector2f r)
	{
		return new Vector2f(x * r.GetX(), y * r.GetY());
	}
	
	public Vector2f Mul(float r)
	{
		return new Vector2f(x * r, y * r);
	}
	
	public Vector2f Div(Vector2f r)
	{
		return new Vector2f(x / r.GetX(), y / r.GetY());
	}
	
	public Vector2f Div(float r)
	{
		return new Vector2f(x / r, y / r);
	}
	
	public Vector2f Abs()
	{
		return new Vector2f(Math.abs(x), Math.abs(y));
	}
	
	public String toString()
	{
		return "(" + x + " " + y + ")";
	}

	public Vector2f Set(float x, float y) 
	{
		this.x = x; this.y = y; 
		return this; 
	}
	public Vector2f Set(Vector2f r) 
	{
		Set(r.GetX(), r.GetY()); 
		return this; 
	}

	public float GetX()
	{
		return x;
	}

	public void SetX(float x)
	{
		this.x = x;
	}

	public float GetY()
	{
		return y;
	}

	public void SetY(float y)
	{
		this.y = y;
	}

	public boolean equals(Vector2f r)
	{
		return x == r.GetX() && y == r.GetY();
	}
}
