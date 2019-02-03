package Model;

import java.util.ArrayList;

import MathUtil.Vector2f;
import MathUtil.Vector3f;

public class Vertex {

	public static final int size = 8;
	private Vector3f pos;
	private Vector2f text_coord;
	private Vector3f normal;


	public Vertex(Vector3f vertex, Vector2f texture_coord, Vector3f normal)
	{
		this.pos = vertex;
		this.text_coord = texture_coord;
		this.normal = normal;
	}
	
	public Vertex(Vector3f vertex)
	{
		this.pos = vertex;
	}
	
	public Vertex(Vector3f vertex, Vector2f texture_coord)
	{
		this.pos = vertex;
		this.text_coord = texture_coord;
		this.normal = new Vector3f(0,0,0); //for now, doesn't have any normals
	}
	
	public Vector3f get_position_vector()
	{
		return this.pos;
	}
		
	public void set_position_vector(Vector3f v)
	{
		this.pos = v;
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public Vector2f getText_coord() {
		return text_coord;
	}

	public void setText_coord(Vector2f text_coord) {
		this.text_coord = text_coord;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
	
	
	
}
