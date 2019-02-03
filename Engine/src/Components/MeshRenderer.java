package Components;

import MathUtil.Transform;
import Model.Material;
import Model.Mesh;
import Rendering.RenderingEngine;
import Shaders.Shader;
import Shaders.ShaderLoader;

public class MeshRenderer extends GameComponent {

	private Mesh mesh;
	private Material material;
	
	public MeshRenderer(Mesh mesh, Material material)
	{
		this.mesh = mesh;
		this.material = material;
	}

	@Override
	public void render(Shader shader, RenderingEngine rendering_engine) {
		shader.bind();
		shader.updateUniforms(get_transform(), material, rendering_engine);
		mesh.draw_triangle();
	}

}
