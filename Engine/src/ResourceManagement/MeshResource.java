package ResourceManagement;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class MeshResource {

	private int vbo_id;
	private int ibo_id;
	private int vao_id;
	private int indices_length;
	private int reference_count;
	
	public MeshResource(int indices_length)
	{
		this.vao_id = GL30.glGenVertexArrays();
		this.vbo_id = GL15.glGenBuffers();
		this.ibo_id = GL15.glGenBuffers();
		this.indices_length = indices_length;
		this.reference_count = 1;
	}
	
	//when garbage collecting, delete these buffers
	@Override
	protected void finalize() throws Throwable 
	{
		super.finalize();
		GL15.glDeleteBuffers(vbo_id);
		GL15.glDeleteBuffers(ibo_id);
	}

	public void add_reference()
	{
		this.reference_count++;
	}
	
	public boolean remove_reference()
	{
		this.reference_count--;
		return this.reference_count == 0;
	}

	public int get_vao_id() {
		return vao_id;
	}
	
	public int get_vbo_id() {
		return vbo_id;
	}

	public int get_ibo_id() {
		return ibo_id;
	}

	public int get_reference_count() {
		return reference_count;
	}

	public int get_indices_length() {
		return indices_length;
	}
	
	
}
