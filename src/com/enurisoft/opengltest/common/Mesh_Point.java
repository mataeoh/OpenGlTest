package com.enurisoft.opengltest.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.enurisoft.opengltest.sub.SubRenderer;

public class Mesh_Point extends Mesh_Base
{
	// Draw
	@Override
	public void Draw(SubRenderer.SceneInfo sceneInfo, ShaderObject shader) {
		int handle = -1;
		
		// Pass in the position.
		handle = shader.getAttribute(Constants.attribPosition);
		GLES20.glVertexAttrib3f(handle, 0.0f, 0.0f, 0.0f);

		// Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(handle);  

		// Pass in the transformation matrix.
		handle = shader.getUniform(Constants.uniformMVPMatrix);
		Matrix.multiplyMM(mMVMatrix, 0, sceneInfo.mViewMatrix, 0, mModelMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, sceneInfo.mProjectionMatrix, 0, mMVMatrix, 0);
		GLES20.glUniformMatrix4fv(handle, 1, false, mMVPMatrix, 0);

		// Draw the point.
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
	}
}
