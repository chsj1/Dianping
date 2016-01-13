package com.dianping.base.widget.loading;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.view.SurfaceHolder;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer extends GLSurfaceView
  implements GLSurfaceView.Renderer
{
  public Context mContext;
  public GLParticleSystem mParticleSystem;

  public GLRenderer(Context paramContext)
  {
    super(paramContext);
    requestFocus();
    setFocusableInTouchMode(true);
    this.mContext = paramContext;
    this.mParticleSystem = new GLParticleSystem();
    setZOrderOnTop(true);
    setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    getHolder().setFormat(-3);
    setRenderer(this);
  }

  public void onDrawFrame(GL10 paramGL10)
  {
    paramGL10.glClear(16640);
    paramGL10.glMatrixMode(5888);
    paramGL10.glLoadIdentity();
    GLU.gluLookAt(paramGL10, 0.0F, -10.0F, 15.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F);
    this.mParticleSystem.update();
    this.mParticleSystem.draw(paramGL10);
  }

  public void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2)
  {
    int i = paramInt2;
    if (paramInt2 == 0)
      i = 1;
    paramGL10.glViewport(0, 0, paramInt1, i);
    paramGL10.glMatrixMode(5889);
    paramGL10.glLoadIdentity();
    GLU.gluPerspective(paramGL10, 30.0F, paramInt1 / i, 1.0F, 100.0F);
  }

  public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig)
  {
    this.mParticleSystem.mTexture1 = this.mParticleSystem.mParticles[0].loadGLTexture(paramGL10, this.mContext);
    paramGL10.glHint(3152, 4354);
    paramGL10.glShadeModel(7425);
    paramGL10.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
    paramGL10.glClearDepthf(1.0F);
    paramGL10.glDisable(2929);
    paramGL10.glDisable(3024);
    paramGL10.glDisable(2896);
    paramGL10.glEnable(3042);
    paramGL10.glBlendFunc(770, 1);
    paramGL10.glEnable(2848);
    paramGL10.glHint(3154, 4354);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.loading.GLRenderer
 * JD-Core Version:    0.6.0
 */