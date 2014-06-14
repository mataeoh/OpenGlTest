package com.enurisoft.opengltest.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.enurisoft.opengltest.R;
import com.enurisoft.opengltest.sub.SubRenderer;
import com.enurisoft.opengltest.common.Constants;
import com.enurisoft.opengltest.common.RawResourceReader;
import com.enurisoft.opengltest.common.ShaderManager;
import com.enurisoft.opengltest.common.ShaderObject;
import com.enurisoft.opengltest.common.TextureHelper;
import android.renderscript.*;
import android.mtp.*;

public class Mesh_Base
{
	protected float[] mModelMatrix = new float[16];
	protected float[] mMVMatrix = new float[16];
	protected float[] mMVPMatrix = new float[16];
	
	protected float[] mRotation = new float[16];
	protected float[] mRotationDelta = new float[16];
	protected float[] mRotationAcc = new float[16];
	protected float[] mTemporaryMatrix = new float[16];

	protected float[] m_fPosition = { 0.0f, 0.0f, 0.0f };
	protected float[] m_fRotation = { 0.0f, 0.0f, 0.0f };
	protected float[] m_fRotationAcc = { 0.0f, 0.0f, 0.0f };
	protected float[] m_fScale = { 1.0f, 1.0f, 1.0f };

	public Mesh_Base() {
		// Initialize the accumulated rotation matrix
        Matrix.setIdentityM(mRotationAcc, 0);
	}
	
	public void BeginTransform() {
		// Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);
	}
	
	public float[] getPosition() {
		return m_fPosition;
	}

	public void setPosition(float x, float y, float z) {
		m_fPosition[0] = x;
		m_fPosition[1] = y;
		m_fPosition[2] = z;
	}

	public void setRotation(float x, float y, float z) {
		m_fRotation[0] = x;
		m_fRotation[1] = y;
		m_fRotation[2] = z;
	}

	public void rotationDelta(float x, float y, float z) {
		m_fRotationAcc[0] = x;
		m_fRotationAcc[1] = y;
		m_fRotationAcc[2] = z;
	}

	public void setScale(float x, float y, float z) {
		m_fScale[0] = x;
		m_fScale[1] = y;
		m_fScale[2] = z;
	}

	public void EndTranaform() {
		// apply translation
		Matrix.translateM(mModelMatrix, 0, m_fPosition[0], m_fPosition[1], m_fPosition[2]);
		
		// apply scale
		Matrix.scaleM(mModelMatrix, 0, m_fScale[0], m_fScale[1], m_fScale[2]);	
		
		// apply rotation
		Matrix.setIdentityM(mRotation, 0);
    	if(m_fRotation[0] != 0.0f) Matrix.rotateM(mRotation, 0, m_fRotation[0], 1.0f, 0.0f, 0.0f);
		if(m_fRotation[1] != 0.0f) Matrix.rotateM(mRotation, 0, m_fRotation[1], 0.0f, 1.0f, 0.0f);
		if(m_fRotation[2] != 0.0f) Matrix.rotateM(mRotation, 0, m_fRotation[2], 0.0f, 0.0f, 1.0f);

		Matrix.setIdentityM(mRotationDelta, 0);
		if(m_fRotationAcc[0] != 0.0f) Matrix.rotateM(mRotationDelta, 0, m_fRotationAcc[0], 1.0f, 0.0f, 0.0f);
		if(m_fRotationAcc[1] != 0.0f) Matrix.rotateM(mRotationDelta, 0, m_fRotationAcc[1], 0.0f, 1.0f, 0.0f);
		if(m_fRotationAcc[2] != 0.0f) Matrix.rotateM(mRotationDelta, 0, m_fRotationAcc[2], 0.0f, 0.0f, 1.0f);

		Matrix.multiplyMM(mTemporaryMatrix, 0, mRotationDelta, 0, mRotationAcc, 0);
		System.arraycopy(mTemporaryMatrix, 0, mRotationAcc, 0, 16);

		Matrix.multiplyMM(mTemporaryMatrix, 0, mRotationAcc, 0, mRotation, 0);
		System.arraycopy(mTemporaryMatrix, 0, mRotation, 0, 16);

		Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mRotation, 0);
		System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16);
	}

	// Draw
	public void Draw(SubRenderer.SceneInfo sceneInfo, ShaderObject shader) {}
}
