package Model;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import MathUtil.Vector3f;
import Model.OBJ.OBJModel;
import ResourceManagement.MeshResource;
import Util.Util;

public class Mesh {

	private static HashMap<String, MeshResource> loaded_resources = new HashMap<String, MeshResource>();
	private MeshResource resource;
	private String fileName;
	
	public Mesh(Vertex[] vertices, int[] indices, boolean compute_normals)
	{
		fileName = "";
		Load_Mesh(vertices, indices, compute_normals);
	}
	
	public Mesh(Model m)
	{
		Load_Mesh(m.get_vertices(), m.get_indices(), false);
	}
	
	public Mesh(String file_name)
	{
		MeshResource resource_ref = loaded_resources.get(file_name);
		fileName = file_name;
		
		if(resource_ref != null)
		{
			resource = resource_ref;
			resource.add_reference();
		}
		else
		{
			load_obj(file_name);
			loaded_resources.put(file_name, resource);
		}
	}
	
	private void Load_Mesh(Vertex[] vertices, int[] indices, boolean compute_normals)
	{
		
		resource = new MeshResource(indices.length);
		
		if(compute_normals)
			calcNormals(vertices, indices);
		
		//creating VAO
		GL30.glBindVertexArray(resource.get_vao_id());
		//creating VBO and IBO
//		this.VBO_ID = GL15.glGenBuffers();
//		this.IBO_ID = GL15.glGenBuffers();
		
		//bind VBO and place data inside VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, resource.get_vbo_id());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.create_float_buffer(vertices), GL15.GL_STATIC_DRAW);
		
		//bind IBO and place data inside IBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, resource.get_ibo_id());
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.create_int_buffer(indices), GL15.GL_STATIC_DRAW);
		
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vertex.size * 4, 0); //vertex data
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, Vertex.size * 4, 12);//texture data
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, Vertex.size * 4, 20);//normal data
		unbind_VAO_VBO_IBO();
	}
	
	public void load_obj(String file_name)
	{
		String[] splitArray = file_name.split("\\.");
		String ext = splitArray[splitArray.length - 1];

		if(!ext.equals("obj"))
		{
			System.err.println("Error: File format not supported for mesh data: " + ext);
			new Exception().printStackTrace();
			System.exit(1);
		}

		OBJModel test = new OBJModel("src/res/" + file_name);
		IndexedModel model = test.toIndexedModel();
		model.calcNormals();

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		for(int i = 0; i < model.getPositions().size(); i++)
		{
			vertices.add(new Vertex(model.getPositions().get(i),
					model.getTexCoords().get(i),
					model.getNormals().get(i)));
		}

		Load_Mesh(Util.vertexList_to_VertexArray(vertices), Util.arrayList_to_intArray(model.getIndices()), false);
	}
	
	@Override
	protected void finalize() throws Throwable 
	{
		super.finalize();
		if(resource.remove_reference() && !fileName.isEmpty())
			loaded_resources.remove(fileName);
	}
	
	private void unbind_VAO_VBO_IBO()
	{
		GL30.glBindVertexArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void draw_triangle()
	{
		
		GL30.glBindVertexArray(resource.get_vao_id()); //prepare this particular VAO for usage
		GL20.glEnableVertexAttribArray(0); //select which position you want from the VAO
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, resource.get_ibo_id());
		GL11.glDrawElements(GL11.GL_TRIANGLES, resource.get_indices_length(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0); //disable 0th indexed attribute of VAO
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		
		unbind_VAO_VBO_IBO();
		}
	
	public void draw_quad()
	{
		GL30.glBindVertexArray(resource.get_vao_id());
		GL20.glEnableVertexAttribArray(0);
		
		GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
	}
	
	public void draw_point()
	{
		GL30.glBindVertexArray(resource.get_vao_id());
		GL20.glEnableVertexAttribArray(0);
		
		GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
		
		GL30.glBindVertexArray(0);
		GL20.glDisableVertexAttribArray(0);
	}
	
	public void draw_line()
	{
		GL30.glBindVertexArray(resource.get_vao_id());
		GL20.glEnableVertexAttribArray(0);
		
		GL11.glDrawArrays(GL11.GL_LINES, 0, 2);
		//unbinding VAO
		GL30.glBindVertexArray(0);
		GL20.glDisableVertexAttribArray(0);
	}
	
	private void compute_normals(Vertex[] vertices, int[] indices)
	{		
		//Vector3f avgNormal;
		
		for(int i = 0; i < indices.length; i+=3)
		{
			int i0 = indices[i];
			int i1 = indices[i+1];
			int i2 = indices[i+2];
			
			Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
			Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());
			Vector3f normal = v1.cross_product(v2);
			normal = normal.normalize();
			
			vertices[i0].setNormal(normal);
			vertices[i1].setNormal(normal);
			vertices[i2].setNormal(normal);	
		}
		
		//avgNormal = compute_avg_normal(vertices, indices);
		
		//normalizing normals
		for(int i = 0; i < vertices.length; i++)
		{
			vertices[i].setNormal(vertices[i].getNormal().normalize());
		}
	}
	
	private Vector3f compute_avg_normal(Vertex[] vertices, int[] indices)
	{
		int noOfFaces = 0;
		Vector3f normalTotal = new Vector3f(0,0,0);
		Vector3f avgNormal = new Vector3f(0,0,0);
		
		for(int i = 0; i < indices.length; i+=3)
		{
			noOfFaces += 1;	
		}
		
		for(int i = 0; i < vertices.length; i++)
		{
			normalTotal = vertices[i].getNormal().add(normalTotal);
		}
		
		avgNormal = normalTotal.div((float)noOfFaces);
		avgNormal.normalize();
		
		return avgNormal;
	}
	
	private void calcNormals(Vertex[] vertices, int[] indices)
	{
		for(int i = 0; i < indices.length; i += 3)
		{
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
			Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());
			
			Vector3f normal = v1.cross_product(v2).normalize();
			
			vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
			vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
			vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
		}
		
		for(int i = 0; i < vertices.length; i++)
			vertices[i].setNormal(vertices[i].getNormal().normalize());
}
	
}
