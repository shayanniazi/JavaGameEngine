package Util;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import MathUtil.Matrix4f;
import MathUtil.Vector3f;
import Model.Vertex;

public class Util {

	public static FloatBuffer create_float_buffer(Matrix4f m)
	{
		FloatBuffer matrix_buffer = BufferUtils.createFloatBuffer(4*4);
		
		for(int row = 0; row < 4; row++)
		{
			for(int col = 0; col < 4; col++)
			{
				matrix_buffer.put(m.get(row, col));
			}
		}
		matrix_buffer.flip();
		
		return matrix_buffer;
	}
	
	public static IntBuffer create_int_buffer(int[] indices)
	{
		IntBuffer int_buffer = BufferUtils.createIntBuffer(indices.length);
		int_buffer.put(indices);
		int_buffer.flip();
		return int_buffer;
	}
	
	public static FloatBuffer create_float_buffer(float[] vertices)
	{
		FloatBuffer vertex_buffer = BufferUtils.createFloatBuffer(vertices.length);
		vertex_buffer.put(vertices);
		vertex_buffer.flip();
		return vertex_buffer;
	}
	
	public static ByteBuffer create_byte_buffer(BufferedImage img)
	{
		ByteBuffer buffer = BufferUtils.createByteBuffer(img.getHeight() * img.getWidth() * 4);
		int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());

		boolean hasAlpha = img.getColorModel().hasAlpha();

		for(int y = 0; y < img.getHeight(); y++)
		{
			for(int x = 0; x < img.getWidth(); x++)
			{
				int pixel = pixels[y * img.getWidth() + x];

				buffer.put((byte)((pixel >> 16) & 0xFF));
				buffer.put((byte)((pixel >> 8) & 0xFF));
				buffer.put((byte)((pixel) & 0xFF));
				if(hasAlpha)
					buffer.put((byte)((pixel >> 24) & 0xFF));
				else
					buffer.put((byte)(0xFF));
			}
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static ByteBuffer createByteBuffer(int size)
	{
		return BufferUtils.createByteBuffer(size);
	}
	
	public static String[] remove_empty_strings(String[] data)
	{
		ArrayList<String> parsed_data = new ArrayList<String>();
		String[] result;
		
		for(int i=0; i<data.length;i++)
		{
			if(!(data[i].equals("")))
				parsed_data.add(data[i]);	
		}
		
		result = new String[parsed_data.size()];
		parsed_data.toArray(result);
		
		return result;	
	}
	
	public static float[] floatList_to_floatArray(ArrayList<Float> list)
	{
		float[] float_data = new float[list.size()];
		Float[] Float_data = new Float[list.size()];
		
		Float_data = list.toArray(Float_data);
		
		for(int i = 0; i < float_data.length; i++)
		{
			float_data[i] = Float_data[i].floatValue();
		}
		
		return float_data;
	}
	
	public static int[] arrayList_to_intArray(ArrayList<Integer> list)
	{
		Integer[] Integer_data = new Integer[list.size()];
		int[] int_data = new int[Integer_data.length];
		
		Integer_data = list.toArray(Integer_data);
		
		for(int i = 0; i < Integer_data.length; i++)
		{
			int_data[i] = Integer_data[i].intValue();
		}
		
		return int_data;
		
	}

	public static FloatBuffer create_float_buffer(Vertex[] vertices)
	{
		FloatBuffer vertex_buffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.size);
		
		for(int i = 0; i < vertices.length; i++)
		{
			vertex_buffer.put(vertices[i].get_position_vector().getX());
			vertex_buffer.put(vertices[i].get_position_vector().getY());
			vertex_buffer.put(vertices[i].get_position_vector().getZ());
			vertex_buffer.put(vertices[i].getText_coord().GetX());
			vertex_buffer.put(vertices[i].getText_coord().GetY());
			vertex_buffer.put(vertices[i].getNormal().getX());
			vertex_buffer.put(vertices[i].getNormal().getY());
			vertex_buffer.put(vertices[i].getNormal().getZ());
		}
		
		vertex_buffer.flip();
		
		return vertex_buffer;
		
	}

	public static Vertex[] vertexList_to_VertexArray(ArrayList<Vertex> vertex_list)
	{
		Vertex[] vertex_array = new Vertex[vertex_list.size()];
		vertex_list.toArray(vertex_array);
		
		return vertex_array;
	}
	
}
