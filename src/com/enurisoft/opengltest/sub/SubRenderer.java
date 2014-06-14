package com.enurisoft.opengltest.sub;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.lang.Math;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.enurisoft.opengltest.R;
import com.enurisoft.opengltest.common.Constants;
import com.enurisoft.opengltest.common.Mesh_Cube;
import com.enurisoft.opengltest.common.RawResourceReader;
import com.enurisoft.opengltest.common.ShaderManager;
import com.enurisoft.opengltest.common.ShaderObject;
import com.enurisoft.opengltest.common.TextureHelper;
import com.enurisoft.opengltest.common.*;

public class SubRenderer implements GLSurfaceView.Renderer 
{	
	// Used for debug logs.
	private static final String TAG = "Renderer";
	private final Context mActivityContext;
	
	// scene info
	public class SceneInfo {
		public float[] mViewMatrix = new float[16];
		public float[] mProjectionMatrix = new float[16];
		/** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
		public float[] mLightPosInEyeSpace = new float[4];
		public float[] mLightPosInWorldSpace = new float[4];
	}
	SceneInfo m_SceneInfo = new SceneInfo();
	
	// Mesh
	private Mesh_Cube mMeshCube;
	private Mesh_Cube mMeshPlane;
	
	// light
	Mesh_Point mPointLight;
	
	/** Temporary place to save the min and mag filter, in case the activity was restarted. */
	private int mQueuedMinFilter;
	private int mQueuedMagFilter;
	
	// These still work without volatile, but refreshes are not guaranteed to happen.					
	public volatile float mDeltaX;					
	public volatile float mDeltaY;
	
	public final ShaderManager m_ShaderMgr = ShaderManager.getInstance();
	public final TextureHelper m_TextureHelper = TextureHelper.getInstance();
	
	/**
	 * Initialize the model data.
	 */
	public SubRenderer(final Context activityContext)
	{	
		mActivityContext = activityContext;
	}
	
	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) 
	{
		// Set the background clear color to black.
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		
		// The below glEnable() call is a holdover from OpenGL ES 1, and is not needed in OpenGL ES 2.
		// Enable texture mapping
		// GLES20.glEnable(GLES20.GL_TEXTURE_2D);
			
		// Position the eye in front of the origin.
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = -0.5f;

		// We are looking toward the distance
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -5.0f;

		// Set our up vector. This is where our head would be pointing were we holding the camera.
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		// Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		Matrix.setLookAtM(m_SceneInfo.mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);		

		// init managers
		m_ShaderMgr.init(mActivityContext);
		m_TextureHelper.init(mActivityContext);
		
		// set filter
        if (mQueuedMinFilter != 0) setMinFilter(mQueuedMinFilter); 
        if (mQueuedMagFilter != 0) setMagFilter(mQueuedMagFilter);
        
		// mesh init
        mMeshCube = new Mesh_Cube(R.drawable.stone_wall_public_domain);
		m_ShaderMgr.useShader(0, mMeshCube);
		mMeshPlane = new Mesh_Cube(R.drawable.noisy_grass_public_domain);
		m_ShaderMgr.useShader(0, mMeshPlane);
		
		// point light
		mPointLight = new Mesh_Point();
		m_ShaderMgr.useShader(1, mPointLight);
	}	
	
		
	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) 
	{
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);

		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 1000.0f;
		
		Matrix.frustumM(m_SceneInfo.mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}	

	@Override
	public void onDrawFrame(GL10 glUnused) 
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);			        
                
        // Do a complete rotation every 10 seconds.
        float time = SystemClock.uptimeMillis() * 0.001f;
        float fDegrees = time * 0.1f *  360.0f;
		float fRadian = time * 0.5f * 3.141592f;
		    
		// update point light
		float fDist = 5.0f;
		mPointLight.BeginTransform();
		mPointLight.setPosition((float)Math.sin(fRadian)*fDist, 0.0f, (float)Math.cos(fRadian)*fDist);
		mPointLight.EndTranaform();

		//m_SceneInfo.mLightPosInWorldSpace = mPointLight.getPosition();
		System.arraycopy(m_SceneInfo.mLightPosInWorldSpace, 0, mPointLight.getPosition(), 0, 3);
		m_SceneInfo.mLightPosInWorldSpace[3] = 1.0f;
        Matrix.multiplyMV(m_SceneInfo.mLightPosInEyeSpace, 0, m_SceneInfo.mViewMatrix, 0, m_SceneInfo.mLightPosInWorldSpace, 0);
		
		// update cube
		mMeshCube.BeginTransform();
		mMeshCube.setPosition(0.0f, 0.8f, -5.5f);
		mMeshCube.setRotation(fDegrees, 0.0f, 0.0f);
		mMeshCube.rotationDelta(mDeltaY, mDeltaX, 0.0f);
		mMeshCube.EndTranaform();
		
		// update planr
		mMeshPlane.BeginTransform();
		mMeshPlane.setPosition(0.0f, -3.0f, 0.0f);
		mMeshPlane.setRotation(0.0f, fDegrees, 0.0f);
		mMeshPlane.setScale(25.0f, 1.0f, 25.0f);
		mMeshPlane.EndTranaform();
		
		// draw
		m_ShaderMgr.draw(m_SceneInfo);
        
		//
		mDeltaX = mDeltaY = 0.0f;
	}
	
	public void setMinFilter(final int filter) {
		m_TextureHelper.setMinFilter(filter);
	}
	
	public void setMagFilter(final int filter) {
		m_TextureHelper.setMagFilter(filter);
	}
}
