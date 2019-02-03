package Model;

import java.io.BufferedReader;
import java.io.FileReader;

import MathUtil.Vector3f;
import Util.Util;

public class OBJLoader {

	public static Model load(String filename)
	{
		String line = null;
		Model model = new Model();
		
		try {
			BufferedReader OBJ_reader = new BufferedReader(new FileReader("src/Model/" + filename));
			while((line = OBJ_reader.readLine()) != null)
			{
				String[] tokens = line.split(" ");
				tokens = Util.remove_empty_strings(tokens);
				
				//for comments or empty line
				if(line.startsWith("#") || tokens.length == 0)
				{
					continue;
				}
				//for vertex data
				else if(line.startsWith("v"))
				{	
					model.vertices2.add(new Vertex(new Vector3f(Float.valueOf(tokens[1]),
																Float.valueOf(tokens[2]),
																Float.valueOf(tokens[3])
															   )));
					
					model.vertices.add(Float.valueOf(tokens[1]));
					model.vertices.add(Float.valueOf(tokens[2]));
					model.vertices.add(Float.valueOf(tokens[3]));

				}
				//for vertex normal data
				else if(line.startsWith("vn"))
				{
					model.normals.add(new Vector3f(Float.valueOf(tokens[1]),
													Float.valueOf(tokens[2]),
													Float.valueOf(tokens[3])
												   ));
				}
				//finds indices data of OBJ face data, and does -1 to convert to engines way of
				//defining indices (engines indices start with 0, whereas OBJ's with 1)
				else if(line.startsWith("f"))
				{
					model.indices.add(Integer.parseInt(tokens[1].split("/")[0]) - 1);
					model.indices.add(Integer.parseInt(tokens[2].split("/")[0]) - 1);
					model.indices.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
					//for basic triangulation (triangulating models if not triangles)
					if(tokens.length > 4)
					{
						model.indices.add(Integer.parseInt(tokens[1].split("/")[0]) - 1);
						model.indices.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
						model.indices.add(Integer.parseInt(tokens[4].split("/")[0]) - 1);
					}
				}
				
			}
			
			OBJ_reader.close();

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return model;
	}
	
}
