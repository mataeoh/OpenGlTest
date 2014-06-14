package com.enurisoft.opengltest.common;

public class Constants
{
	/** How many bytes per float. */
	public static final int BytesPerFloat = 4;	

	/** Size of the position data in elements. */
	public static final int PositionDataSize = 3;	

	/** Size of the normal data in elements. */
	public static final int NormalDataSize = 3;

	/** Size of the texture coordinate data in elements. */
	public static final int TextureCoordinateDataSize = 2;
	
	public static final String attribPosition = "a_Position";
	public static final String attribNormal = "a_Normal";
	public static final String attribTexcoord = "a_TexCoordinate";
	public static final String uniformMVPMatrix = "u_MVPMatrix";
	public static final String uniformMVMatrix = "u_MVMatrix";
	public static final String uniformLightPos = "u_LightPos";
	public static final String uniformTexture = "u_Texture";
}
