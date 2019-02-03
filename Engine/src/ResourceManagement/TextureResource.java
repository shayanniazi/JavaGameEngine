package ResourceManagement;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class TextureResource {

	private int id;
	private int reference_count;
	
	public TextureResource()
	{
		this.id = GL11.glGenTextures();
		this.reference_count = 1;
	}
	
	//when garbage collecting, delete these buffers
	@Override
	protected void finalize() throws Throwable 
	{
		GL15.glDeleteBuffers(id);
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

	public int get_id()
	{
		return this.id;
	}
	
	
	
}
