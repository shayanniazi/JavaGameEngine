package Shaders;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import Components.BaseLight;
import Components.DirectionalLight;
import Components.PointLight;
import Components.SpotLight;
import MathUtil.Matrix4f;
import MathUtil.Transform;
import MathUtil.Vector3f;
import Model.Material;
import Rendering.RenderingEngine;
import ResourceManagement.ShaderResource;
import Util.Util;

//ClASS WORKING
/* 1) create a shader program
 * 2) load shaders from resources (txt files whatever) into class 
 * 3) create shader, give shader source (shader file), compile shader
 * 4) attatch the shader to the program, link the program (optionally validate program)
 *
 *HOW TO USE CLASS:
 *1) create shader object
 *2) create vertex and fragment shaders
 *3) add uniforms 
 *4) start shader in render loop
 */



public class ShaderLoader {

	private class GLSLStruct
	{
		public String name;
		public String type;
	}
	
	//private int programID;
	private static HashMap<String, ShaderResource> loadedShaders = new HashMap<String, ShaderResource>();
	private ShaderResource resource;
	private String fileName;
	
	
	public ShaderLoader() {}
	//constructor (creates shader program)
	public ShaderLoader(String file_name)
	{
		create_program(file_name);
	}
	
	public void create_program(String file_name)
	{

		this.fileName = file_name;
		//initialize uniforms hashmap
		ShaderResource oldResource = loadedShaders.get(fileName);
		
		if(oldResource != null)
		{
			resource = oldResource;
			resource.addReference();
		}
		else
		{
			resource = new ShaderResource();
			
			String vertex_code = this.load_shader_file(file_name + ".vs");
			String fragment_code = this.load_shader_file(file_name + ".fs");
			
			create_vertex_shader(vertex_code);
			create_fragment_shader(fragment_code);
			
			addAllAttributes(vertex_code);
			addAllUniforms(vertex_code);
			addAllUniforms(fragment_code);
		}
		
		}
	
	public void start_shader()
	{
		GL20.glUseProgram(resource.getProgram());
	}
	
	public void stop_shader()
	{
		GL20.glUseProgram(0);
	}
	
	private String load_shader_file(String file_name)
	{
		StringBuilder shader_code = new StringBuilder();
		String line = null;
		final String include_directive = "#include";
		try {
			BufferedReader shader_file_reader = new BufferedReader(new FileReader("src/Shaders/" + file_name));
			while((line = shader_file_reader.readLine()) != null)
			{
				if(line.startsWith(include_directive))
				{
					shader_code.append(load_shader_file(line.substring(include_directive.length() + 2, line.length() - 1)));
				}
				else
				{
					shader_code.append(line);
					shader_code.append('\n');
				}
			}
			shader_file_reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return shader_code.toString();
	}
	
	private int create_shader(int shader_type, String shader_code)
	{
		
		int shaderID = GL20.glCreateShader(shader_type);
		
		//if shader not created successfully
		if(shaderID == 0)
		{
			System.err.println("Error in creating shader of type " + shader_type);
			System.exit(1);
		}
		
		//uploads code into string 
		//String shader_code = load_shader_file(file_name);
		
		GL20.glShaderSource(shaderID, shader_code); //uploads code into shader of shaderID
		GL20.glCompileShader(shaderID); //compiles the shader, no shit Sherlock
		
		//gets shader status of compilation and 
		int shader_status = GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS);
		
		if(shader_status == GL11.GL_FALSE)
		{
			System.err.println(GL20.glGetShaderInfoLog(shaderID, 1024));
			System.exit(1);
		}
		
		return shaderID;
	}

	private void add_shader_to_program(int shaderID)
	{
		GL20.glAttachShader(resource.getProgram(), shaderID);
		GL20.glLinkProgram(resource.getProgram());
		GL20.glValidateProgram(shaderID);
	}
	
	private void create_vertex_shader(String shader_code)
	{
		int shaderID = create_shader(GL20.GL_VERTEX_SHADER, shader_code);
		add_shader_to_program(shaderID);
		GL20.glDeleteShader(shaderID);
	}
	
	private void create_geometry_shader(String shader_code)
	{
		int shaderID = create_shader(GL32.GL_GEOMETRY_SHADER, shader_code);
		add_shader_to_program(shaderID);
		GL20.glDeleteShader(shaderID);
	}
	
	private void create_fragment_shader(String shader_code)
	{
		int shaderID = create_shader(GL20.GL_FRAGMENT_SHADER, shader_code);
		add_shader_to_program(shaderID); //will attatch and link shader to program
		GL20.glDeleteShader(shaderID); //shader no longer needed since it is already linked to the program
	}

	private HashMap<String, ArrayList<GLSLStruct>> findUniformStructs(String shaderText)
	{
		HashMap<String, ArrayList<GLSLStruct>> result = new HashMap<String, ArrayList<GLSLStruct>>();

		final String STRUCT_KEYWORD = "struct";
		int structStartLocation = shaderText.indexOf(STRUCT_KEYWORD);
		while(structStartLocation != -1)
		{
			int nameBegin = structStartLocation + STRUCT_KEYWORD.length() + 1;
			int braceBegin = shaderText.indexOf("{", nameBegin);
			int braceEnd = shaderText.indexOf("}", braceBegin);

			String structName = shaderText.substring(nameBegin, braceBegin).trim();
			ArrayList<GLSLStruct> glslStructs = new ArrayList<GLSLStruct>();

			int componentSemicolonPos = shaderText.indexOf(";", braceBegin);
			while(componentSemicolonPos != -1 && componentSemicolonPos < braceEnd)
			{
				int componentNameStart = componentSemicolonPos;

				while(!Character.isWhitespace(shaderText.charAt(componentNameStart - 1)))
					componentNameStart--;

				int componentTypeEnd = componentNameStart - 1;
				int componentTypeStart = componentTypeEnd;

				while(!Character.isWhitespace(shaderText.charAt(componentTypeStart - 1)))
					componentTypeStart--;

				String componentName = shaderText.substring(componentNameStart, componentSemicolonPos);
				String componentType = shaderText.substring(componentTypeStart, componentTypeEnd);

				GLSLStruct glslStruct = new GLSLStruct();
				glslStruct.name = componentName;
				glslStruct.type = componentType;

				glslStructs.add(glslStruct);

				componentSemicolonPos = shaderText.indexOf(";", componentSemicolonPos + 1);
			}

			result.put(structName, glslStructs);

			structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
		}

		return result;
	}
	
	private void addUniform(String uniformName, String uniformType, HashMap<String, ArrayList<GLSLStruct>> structs)
	{
		boolean addThis = true;
		ArrayList<GLSLStruct> structComponents = structs.get(uniformType);

		if(structComponents != null)
		{
			addThis = false;
			for(GLSLStruct struct : structComponents)
			{
				addUniform(uniformName + "." + struct.name, struct.type, structs);
			}
		}

		if(!addThis)
			return;
			
		int uniformLocation = GL20.glGetUniformLocation(resource.getProgram(), uniformName);
	}
	
	private void addAllUniforms(String shaderText)
	{
		HashMap<String, ArrayList<GLSLStruct>> structs = findUniformStructs(shaderText);

		final String UNIFORM_KEYWORD = "uniform";
		int uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD);
		while(uniformStartLocation != -1)
		{
			int begin = uniformStartLocation + UNIFORM_KEYWORD.length() + 1;
			int end = shaderText.indexOf(";", begin);

			String uniformLine = shaderText.substring(begin, end);

			int whiteSpacePos = uniformLine.indexOf(' ');
			String uniformName = uniformLine.substring(whiteSpacePos + 1, uniformLine.length());
			String uniformType = uniformLine.substring(0, whiteSpacePos);

			addUniform(uniformName, uniformType, structs);

			uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
		}
	}
	
	private void addAllAttributes(String shaderText)
	{
		final String ATTRIBUTE_KEYWORD = "attribute";
		int attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD);
		int attribNumber = 0;
		while(attributeStartLocation != -1)
		{
			int begin = attributeStartLocation + ATTRIBUTE_KEYWORD.length() + 1;
			int end = shaderText.indexOf(";", begin);

			String attributeLine = shaderText.substring(begin, end);
			String attributeName = attributeLine.substring(attributeLine.indexOf(' ') + 1, attributeLine.length());

			set_attrib_location(attributeName, attribNumber);
			attribNumber++;

			attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
		}
}
	
	private void set_attrib_location(String uniform, int location)
	{
		GL20.glBindAttribLocation(resource.getProgram(), location, uniform);
	}
	

	public void setUniformi(String uniformName, int value)
	{
		GL20.glUniform1i(resource.getUniforms().get(uniformName), value);
}
	
	public void set_uniform1f(String uniform_name, float uniform_value)
	{
		//setting uniform to the shader program
		GL20.glUniform1f(resource.getUniforms().get(uniform_name), uniform_value);
	}

	public void set_uniform2f(String uniform_name, float v0, float v1)
	{
		//setting uniform to the shader program
		GL20.glUniform2f(resource.getUniforms().get(uniform_name), v0, v1);
	}

	public void set_uniform3f(String uniform_name, Vector3f v)
	{
		//setting uniform to the shader program
		GL20.glUniform3f(resource.getUniforms().get(uniform_name), v.getX(), v.getY(), v.getZ());
	}

	public void set_uniform4f(String uniform_name, float v0, float v1, float v2, float v3)
	{
		//setting uniform to the shader program
		GL20.glUniform4f(resource.getUniforms().get(uniform_name), v0, v1, v2, v3);
	}
	
	public void set_uniform_matrix4f(String uniform_name, Matrix4f m)
	{		
		
		GL20.glUniformMatrix4(resource.getUniforms().get(uniform_name), true, Util.create_float_buffer(m));
	}
	
	public void update_uniforms(Transform transform, Material material, RenderingEngine rendering_engine)
	{
		Matrix4f worldMatrix = transform.get_transformation();
		Matrix4f MVPMatrix = rendering_engine.get_main_camera().get_perspective().mul(worldMatrix);

		for(int i = 0; i < resource.getUniformNames().size(); i++)
		{
			String uniformName = resource.getUniformNames().get(i);
			String uniformType = resource.getUniformTypes().get(i);

			if(uniformType.equals("sampler2D"))
			{
				int samplerSlot = rendering_engine.getSamplerSlot(uniformName);
				//material.get_texture(uniformName).create_texture(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			}
			else if(uniformName.startsWith("T_"))
			{
				if(uniformName.equals("T_MVP"))
					set_uniform_matrix4f(uniformName, MVPMatrix);
				else if(uniformName.equals("T_model"))
					set_uniform_matrix4f(uniformName, worldMatrix);
				else
					throw new IllegalArgumentException(uniformName + " is not a valid component of Transform");
			}
			else if(uniformName.startsWith("R_"))
			{
				String unprefixedUniformName = uniformName.substring(2);
				if(uniformType.equals("vec3"))
					set_uniform3f(uniformName, rendering_engine.getVector3f(unprefixedUniformName));
				else if(uniformType.equals("float"))
					set_uniform1f(uniformName, rendering_engine.getFloat(unprefixedUniformName));
				else if(uniformType.equals("DirectionalLight"))
					setUniformDirectionalLight(uniformName, (DirectionalLight)rendering_engine.getActive_light());
				else if(uniformType.equals("PointLight"))
					setUniformPointLight(uniformName, (PointLight)rendering_engine.getActive_light());
				else if(uniformType.equals("SpotLight"))
					setUniformSpotLight(uniformName, (SpotLight)rendering_engine.getActive_light());
				//else
					//rendering_engine.updateUniformStruct(transform, material, this, uniformName, uniformType);
			}
			else if(uniformName.startsWith("C_"))
			{
				if(uniformName.equals("C_eyePos"))
					set_uniform3f(uniformName, rendering_engine.get_main_camera().get_transform().getTransformedPos());
				else
					throw new IllegalArgumentException(uniformName + " is not a valid component of Camera");
			}
			else
			{
				if(uniformType.equals("vec3"))
					set_uniform3f(uniformName, material.getVector3f(uniformName));
				else if(uniformType.equals("float"))
					set_uniform1f(uniformName, material.getFloat(uniformName));
				else
					throw new IllegalArgumentException(uniformType + " is not a supported type in Material");
			}
		}
	}
	
	public void setUniformBaseLight(String uniformName, BaseLight baseLight)
	{
		set_uniform3f(uniformName + ".color", baseLight.getColor());
		set_uniform1f(uniformName + ".intensity", baseLight.getIntensity());
	}

	public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight)
	{
		setUniformBaseLight(uniformName + ".base", directionalLight);
		set_uniform3f(uniformName + ".direction", directionalLight.getDirection());
	}

	public void setUniformPointLight(String uniformName, PointLight pointLight)
	{
		setUniformBaseLight(uniformName + ".base", pointLight);
		set_uniform1f(uniformName + ".atten.constant", pointLight.getConstant());
		set_uniform1f(uniformName + ".atten.linear", pointLight.getLinear());
		set_uniform1f(uniformName + ".atten.exponent", pointLight.getExponent());
		set_uniform3f(uniformName + ".position", pointLight.get_transform().getTransformedPos());
		set_uniform1f(uniformName + ".range", pointLight.get_range());
	}

	public void setUniformSpotLight(String uniformName, SpotLight spotLight)
	{
		setUniformPointLight(uniformName + ".pointLight", spotLight);
		set_uniform3f(uniformName + ".direction", spotLight.getDirection());
		set_uniform1f(uniformName + ".cutoff", spotLight.getCutoff());
	}
	
	
	
}
