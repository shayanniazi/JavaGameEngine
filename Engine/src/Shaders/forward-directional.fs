#version 120
#include "lighting.glh"

varying vec3 normal0;
varying vec3 worldPos0;
varying vec2 texCoord0;

uniform DirectionalLight R_directionalLight;
uniform sampler2D ourTexture;

void main()
{
	vec3 normal = normalize(normal0);
	gl_FragColor = texture2D(ourTexture, texCoord0.xy) * calcDirectionalLight(R_directionalLight, normal, worldPos0);
}