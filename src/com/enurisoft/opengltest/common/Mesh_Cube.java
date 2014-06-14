package com.enurisoft.opengltest.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.enurisoft.opengltest.sub.SubRenderer;

public class Mesh_Cube extends Mesh_Base
{
	private FloatBuffer mPositionBuffer;
	private FloatBuffer mNormalBuffer;
	private FloatBuffer mTexCoordBuffer;
	
	private int mTextureDataHandle;
	
	public Mesh_Cube() {
		initData();
	}

	public Mesh_Cube(final int textureID) {
		// initData
		initData();
		// load texture
		mTextureDataHandle = TextureHelper.getInstance().loadTexture(textureID);
	}
	
	public void initData() {
		// Positions
		final float[] PositionData =
		{
			// Front face
			-1.0f, 1.0f, 1.0f,				
			-1.0f, -1.0f, 1.0f,
			1.0f, 1.0f, 1.0f, 
			-1.0f, -1.0f, 1.0f, 				
			1.0f, -1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,

			// Right face
			1.0f, 1.0f, 1.0f,				
			1.0f, -1.0f, 1.0f,
			1.0f, 1.0f, -1.0f,
			1.0f, -1.0f, 1.0f,				
			1.0f, -1.0f, -1.0f,
			1.0f, 1.0f, -1.0f,

			// Back face
			1.0f, 1.0f, -1.0f,				
			1.0f, -1.0f, -1.0f,
			-1.0f, 1.0f, -1.0f,
			1.0f, -1.0f, -1.0f,				
			-1.0f, -1.0f, -1.0f,
			-1.0f, 1.0f, -1.0f,

			// Left face
			-1.0f, 1.0f, -1.0f,				
			-1.0f, -1.0f, -1.0f,
			-1.0f, 1.0f, 1.0f, 
			-1.0f, -1.0f, -1.0f,				
			-1.0f, -1.0f, 1.0f, 
			-1.0f, 1.0f, 1.0f, 

			// Top face
			-1.0f, 1.0f, -1.0f,				
			-1.0f, 1.0f, 1.0f, 
			1.0f, 1.0f, -1.0f, 
			-1.0f, 1.0f, 1.0f, 				
			1.0f, 1.0f, 1.0f, 
			1.0f, 1.0f, -1.0f,

			// Bottom face
			1.0f, -1.0f, -1.0f,				
			1.0f, -1.0f, 1.0f, 
			-1.0f, -1.0f, -1.0f,
			1.0f, -1.0f, 1.0f, 				
			-1.0f, -1.0f, 1.0f,
			-1.0f, -1.0f, -1.0f,
			};				

		// Nomals
		final float[] NormalData =
		{												
		// Front face
			0.0f, 0.0f, 1.0f,				
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,				
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,

			// Right face 
			1.0f, 0.0f, 0.0f,				
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,				
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,

			// Back face 
			0.0f, 0.0f, -1.0f,				
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,				
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,

			// Left face 
			-1.0f, 0.0f, 0.0f,				
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,				
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,

			// Top face 
			0.0f, 1.0f, 0.0f,			
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,				
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,

			// Bottom face 
			0.0f, -1.0f, 0.0f,			
			0.0f, -1.0f, 0.0f,
			0.0f, -1.0f, 0.0f,
			0.0f, -1.0f, 0.0f,				
			0.0f, -1.0f, 0.0f,
			0.0f, -1.0f, 0.0f
			};

		// Texture Coordinate
		final float[] TexCoordData =
		{												
		// Front face
			0.0f, 0.0f, 				
			0.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f,				

			// Right face 
			0.0f, 0.0f, 				
			0.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f,	

			// Back face 
			0.0f, 0.0f, 				
			0.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f,	

			// Left face 
			0.0f, 0.0f, 				
			0.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f,	

			// Top face 
			0.0f, 0.0f, 				
			0.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f,	

			// Bottom face 
			0.0f, 0.0f, 				
			0.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f
			};

		// Initialize the buffers.
		mPositionBuffer = ByteBuffer.allocateDirect(PositionData.length * Constants.BytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mPositionBuffer.put(PositionData).position(0);				

		mNormalBuffer = ByteBuffer.allocateDirect(NormalData.length * Constants.BytesPerFloat)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mNormalBuffer.put(NormalData).position(0);

		mTexCoordBuffer = ByteBuffer.allocateDirect(TexCoordData.length * Constants.BytesPerFloat)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTexCoordBuffer.put(TexCoordData).position(0);
	}
	
	// Draw
	@Override
	public void Draw(SubRenderer.SceneInfo sceneInfo, ShaderObject shader)
	{
		int handle = 0;
		
        // Draw a cube.
    	// Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
		handle = shader.getUniform(Constants.uniformTexture);
        GLES20.glUniform1i(handle, 0);
		
		// bind light
		handle = shader.getUniform(Constants.uniformLightPos);
		GLES20.glUniform3f(handle, 0.0f, 10.0f, 0.0f);

        // texture coordinate
		handle = shader.getAttribute(Constants.attribTexcoord);
        mTexCoordBuffer.position(0);
        GLES20.glVertexAttribPointer(handle, Constants.TextureCoordinateDataSize, GLES20.GL_FLOAT, false, 0, mTexCoordBuffer);
		GLES20.glEnableVertexAttribArray(handle);                       
		
		// position
		handle = shader.getAttribute(Constants.attribPosition);
		mPositionBuffer.position(0);		
        GLES20.glVertexAttribPointer(handle, Constants.PositionDataSize, GLES20.GL_FLOAT, false, 0, mPositionBuffer);
        GLES20.glEnableVertexAttribArray(handle);                       
		
		// normal
		handle = shader.getAttribute(Constants.attribNormal);
        mNormalBuffer.position(0);
        GLES20.glVertexAttribPointer(handle, Constants.NormalDataSize, GLES20.GL_FLOAT, false, 0, mNormalBuffer);
        GLES20.glEnableVertexAttribArray(handle);                
		
		// model view
		handle = shader.getUniform(Constants.uniformMVMatrix);
        Matrix.multiplyMM(mMVMatrix, 0, sceneInfo.mViewMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(handle, 1, false, mMVMatrix, 0);
		
		// projection matrix
		handle = shader.getUniform(Constants.uniformMVPMatrix);
        Matrix.multiplyMM(mMVPMatrix, 0, sceneInfo.mProjectionMatrix, 0, mMVMatrix, 0);
        GLES20.glUniformMatrix4fv(handle, 1, false, mMVPMatrix, 0);
		
		// draw
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
	}
}
