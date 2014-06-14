package com.enurisoft.opengltest.common;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.util.ArrayList;

import com.enurisoft.opengltest.R;
import com.enurisoft.opengltest.sub.SubRenderer;
import java.util.*;

public class ShaderManager
{
	private static final ShaderManager ShaderMgr = new ShaderManager();
	// Used for debug logs.
	private static final String TAG = "ShaderManager";
	private Context mActivityContext;
	
	private ArrayList m_ShaderList = new ArrayList();

	private ShaderManager() {}

	public void init(Context context) {
		mActivityContext = context;
		ShaderObject shader = new ShaderObject();
		// per pixel lighting shader
		shader.init(mActivityContext, 
			R.raw.per_pixel_vertex_shader_tex_and_light, 
			R.raw.per_pixel_fragment_shader_tex_and_light, 
			new String[] {Constants.attribPosition, Constants.attribNormal, Constants.attribTexcoord},
			new String[] {Constants.uniformMVPMatrix, Constants.uniformMVMatrix, Constants.uniformLightPos, Constants.uniformTexture});
		m_ShaderList.add(shader);
		
		// point shader
		shader = new ShaderObject();
		shader.init(mActivityContext, 
			R.raw.point_vertex_shader, 
			R.raw.point_fragment_shader, 
			new String[] {Constants.attribPosition},
			new String[] {Constants.uniformMVPMatrix});
		m_ShaderList.add(shader);
	}
	
	public void useShader(int shaderId, Mesh_Base mesh) {
		ShaderObject shader = (ShaderObject)m_ShaderList.get(shaderId);
		shader.addMesh(mesh);
	}

	public static ShaderManager getInstance() {
		return ShaderMgr;
	}
	
	public int getProgramHandle(int index) {
		ShaderObject shader = (ShaderObject)m_ShaderList.get(index);
		return shader.getProgramHandle();
	}
	
	public void draw(SubRenderer.SceneInfo sceneInfo) {
		for(int i=0; i<m_ShaderList.size(); ++i) {
			ShaderObject shader = (ShaderObject)m_ShaderList.get(i);
			shader.draw(sceneInfo);
		}
	}
	
	/** 
	 * Helper function to compile a shader.
	 * 
	 * @param shaderType The shader type.
	 * @param shaderSource The shader source code.
	 * @return An OpenGL handle to the shader.
	 */
	public static int compileShader(final int shaderType, final String shaderSource) 
	{
		int shaderHandle = GLES20.glCreateShader(shaderType);

		if (shaderHandle != 0) 
		{
			// Pass in the shader source.
			GLES20.glShaderSource(shaderHandle, shaderSource);

			// Compile the shader.
			GLES20.glCompileShader(shaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) 
			{
				Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
				GLES20.glDeleteShader(shaderHandle);
				shaderHandle = 0;
			}
		}

		if (shaderHandle == 0)
		{			
			throw new RuntimeException("Error creating shader.");
		}

		return shaderHandle;
	}

	/**
	 * Helper function to compile and link a program.
	 * 
	 * @param vertexShaderHandle An OpenGL handle to an already-compiled vertex shader.
	 * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
	 * @param attributes Attributes that need to be bound to the program.
	 * @return An OpenGL handle to the program.
	 */
	public static int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes) 
	{
		int programHandle = GLES20.glCreateProgram();

		if (programHandle != 0) 
		{
			// Bind the vertex shader to the program.
			GLES20.glAttachShader(programHandle, vertexShaderHandle);			

			// Bind the fragment shader to the program.
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);

			// Bind attributes
			if (attributes != null)
			{
				final int size = attributes.length;
				for (int i = 0; i < size; i++)
				{
					GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
				}						
			}

			// Link the two shaders together into a program.
			GLES20.glLinkProgram(programHandle);

			// Get the link status.
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

			// If the link failed, delete the program.
			if (linkStatus[0] == 0) 
			{				
				Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}

		if (programHandle == 0)
		{
			throw new RuntimeException("Error creating program.");
		}

		return programHandle;
	}
}

// ShaderObject class
class ShaderObject {
	public int m_nProgramHandle;
	public int m_nVertexShaderHandle;
	public int m_nPixelShaderHandle;
	
	ArrayList mMeshList = new ArrayList();
	
	public HashMap<String, Integer> m_nAttributeHandle = new HashMap<String, Integer>();
	public HashMap<String, Integer> m_nUniformHandle = new HashMap<String, Integer>();

	public void putAttrib(String key, int value) {
		m_nAttributeHandle.put(key, new Integer(value));
	}

	public int getAttribute(String key) {
		return m_nAttributeHandle.get(key).intValue();
	}

	public void putUniform(String key, int value) {
		m_nUniformHandle.put(key, new Integer(value));
	}

	public int getUniform(String key) {
		return m_nUniformHandle.get(key).intValue();
	}
	
	// init
	public void init(Context context, final int vertexShaderId, final int pixelShaderId, final String[] attributes, final String[] uniforms) {
		ShaderManager shaderMgr = ShaderManager.getInstance();
		final String vertexShader = RawResourceReader.readTextFileFromRawResource(context, vertexShaderId);   		
 		final String fragmentShader = RawResourceReader.readTextFileFromRawResource(context, pixelShaderId);
		m_nVertexShaderHandle = shaderMgr.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
		m_nPixelShaderHandle = shaderMgr.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);		
		m_nProgramHandle = shaderMgr.createAndLinkProgram(m_nVertexShaderHandle, m_nPixelShaderHandle, attributes);
		
		for(int i=0; i<attributes.length; ++i) {
			putAttrib(attributes[i], -1);
		}
		
		for(int i=0; i<uniforms.length; ++i) {
			putUniform(uniforms[i], -1);
		}
	}
	
	// Add Mesh
	public void addMesh(Mesh_Base mesh) {
		mMeshList.add(mesh);
	}
	
	// draw
	public void draw(SubRenderer.SceneInfo sceneInfo) {
		// Set shader program
        GLES20.glUseProgram(m_nProgramHandle);

        // Set program handles
		Iterator<String> iterator = m_nAttributeHandle.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String)iterator.next();
			putAttrib(key, GLES20.glGetAttribLocation(m_nProgramHandle, key));
		}
		iterator = m_nUniformHandle.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String)iterator.next();
			putUniform(key, GLES20.glGetUniformLocation(m_nProgramHandle, key));
		}                
        
		// draw
		for(int i=0; i<mMeshList.size(); ++i) {
			Mesh_Base mesh = (Mesh_Base)mMeshList.get(i);
			mesh.Draw(sceneInfo, this);
		}
	}
	
	// 
	public int getProgramHandle() { return m_nProgramHandle; }
}
