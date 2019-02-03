package Model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

import ResourceManagement.TextureResource;
import Util.Util;

public class Texture {

	
	private HashMap<String, TextureResource> loaded_textures = new HashMap<String, TextureResource>();
	private TextureResource resource;
	private String fileName;
	
	public Texture(String file_name)
	{
		TextureResource resource_ref = loaded_textures.get(file_name);
		
		if(resource_ref!=null)
		{
			this.resource = resource_ref;
			resource_ref.add_reference();
		}
		else
		{
			resource = load_texture(file_name);
			loaded_textures.put(file_name, resource);
		}
	}
	
		protected void finalize()
		{
			if(resource.remove_reference() && !fileName.isEmpty())
			
				loaded_textures.remove(fileName);
		}
		
	public void bind()
	{
		create_texture(0);
	}
		
	public void create_texture(int samplerSlot)
	{
		assert(samplerSlot >= 0 && samplerSlot <= 31);
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + samplerSlot);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, resource.get_id());
	}
	
	//public static Texture load_texture(String file_name)
	public static TextureResource load_texture(String file_name)
	{
		String[] parsed_file_name = file_name.split("\\.");
		String file_extension = parsed_file_name[parsed_file_name.length - 1];

		try
		{
			BufferedImage image = ImageIO.read(new File("src/res/textures/" + file_name));

			ByteBuffer buffer = Util.create_byte_buffer(image);
			boolean hasAlpha = image.getColorModel().hasAlpha();

			TextureResource resource = new TextureResource();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, resource.get_id());

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

			return resource;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
}
