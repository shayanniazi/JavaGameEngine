#version 120


in vec2 textCoord0;

uniform sampler2D ourTexture;
uniform float R_ambient;

void main()
{
	vec4 totalLight = vec4(0,0,0,0);
	totalLight = totalLight + R_ambient;
	
    gl_FragColor = texture2D(ourTexture, textCoord0.xy) * totalLight;
}