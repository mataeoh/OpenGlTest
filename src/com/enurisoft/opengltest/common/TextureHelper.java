package com.enurisoft.opengltest.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import java.util.*;

public class TextureHelper
{
	private static final TextureHelper textureHelper = new TextureHelper();
	static Context m_Context;
	static HashMap<Integer, Integer> m_textureMap;
	
	private TextureHelper() {}
	public static TextureHelper getInstance() { return textureHelper; }
	public void init(final Context context) {
		m_Context = context;
		m_textureMap = new HashMap<Integer, Integer>();
	}
	
	public static int loadTexture(final int resourceId)
	{
		if(m_textureMap.containsKey(resourceId)) {
			return m_textureMap.get(resourceId);
		}
		
		final int[] textureHandle = new int[1];
		
		GLES20.glGenTextures(1, textureHandle, 0);
		
		if (textureHandle[0] != 0)
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;	// No pre-scaling

			// Read in the resource
			final Bitmap bitmap = BitmapFactory.decodeResource(m_Context.getResources(), resourceId, options);
						
			// Bind to the texture in OpenGL
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
			
			// Set filtering
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
			
			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			
			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();
			
			// gernerate mipmap
			GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			
			// add to hashmap
			m_textureMap.put(resourceId, textureHandle[0]);
		}
		
		if (textureHandle[0] == 0)
		{
			throw new RuntimeException("Error loading texture.");
		}
		
		return textureHandle[0];
	}
	
	public void setMinFilter(final int filter)
	{
		Set<Integer> keySet = m_textureMap.keySet();
		Iterator<Integer> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			int key = iterator.next();
			int value = m_textureMap.get(key);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, value);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, filter);
		}
	}

	public void setMagFilter(final int filter)
	{
		Set<Integer> keySet = m_textureMap.keySet();
		Iterator<Integer> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			int key = iterator.next();
			int value = m_textureMap.get(key);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, value);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, filter);
		}
	}
}
