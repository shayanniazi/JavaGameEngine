package Model;

import java.util.ArrayList;

import MathUtil.Vector3f;
import Util.Util;

public class Model {

	private Vertex[] vertex_data;
	private int[] indices_data;
	private Mesh model_data;
	ArrayList<Vertex> vertices2 = new ArrayList<Vertex>();
	ArrayList<Float> vertices = new ArrayList<Float>();
	ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
	ArrayList<Integer> indices = new ArrayList<Integer>();

	
	public Model()
	{
		
	}

	public int[] get_indices()
	{
		this.indices_data = Util.arrayList_to_intArray(indices);
		return this.indices_data;
	}
	
	public Vertex[] get_vertices()
	{
		this.vertex_data = Util.vertexList_to_VertexArray(vertices2);
		return this.vertex_data;
	}
	
	
}
