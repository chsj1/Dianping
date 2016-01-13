package com.dianping.base.widget.loading;

import com.dianping.v1.R.drawable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;
import javax.microedition.khronos.opengles.GL10;

public class GLParticleSystem
{
  public static final int PARTICLECOUNT = 25;
  public float GRAVITY = 10.0F;
  public float PSIZE = 0.6F;
  public int col;
  public int delay;
  public Random mGen = new Random();
  public ByteBuffer mIndexBuffer;
  public long mLastTime;
  public GLParticle[] mParticles = new GLParticle[25];
  public int mTexture1;
  public FloatBuffer mTextureBuffer;
  public FloatBuffer mVertexBuffer;

  public GLParticleSystem()
  {
    int i = 0;
    while (i < 25)
    {
      this.mParticles[i] = new GLParticle(0.0F, 0.0F, 0.0F, R.drawable.bubble);
      this.mParticles[i].life = -1.0F;
      i += 1;
    }
    this.mVertexBuffer = makeFloatBuffer(new float[] { -this.PSIZE, -this.PSIZE, 1.0F, this.PSIZE, -this.PSIZE, 1.0F, -this.PSIZE, this.PSIZE, 1.0F, this.PSIZE, this.PSIZE, 1.0F });
    this.mTextureBuffer = makeFloatBuffer(new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F });
    this.mIndexBuffer = makeByteBuffer(new byte[] { 0, 1, 3, 0, 3, 2 });
  }

  private void initParticle(int paramInt)
  {
    this.mParticles[paramInt].life = 1.0F;
    this.mParticles[paramInt].brightness = (this.mGen.nextFloat() / 600.0F + 0.0025F);
    this.mParticles[paramInt].pos.x = (float)(paramInt * 0.5D - 5.0D);
    this.mParticles[paramInt].pos.y = (this.mGen.nextFloat() * 8.0F - 12.0F);
    this.mParticles[paramInt].pos.z = 0.0F;
    this.mParticles[paramInt].vel.x = (this.mGen.nextFloat() * 2.0F - 1.0F);
    this.mParticles[paramInt].vel.z = (this.mGen.nextFloat() * 2.0F - 1.0F);
    this.mParticles[paramInt].vel.y = (this.mGen.nextFloat() * 7.0F + 1.0F);
  }

  public void draw(GL10 paramGL10)
  {
    paramGL10.glVertexPointer(3, 5126, 0, this.mVertexBuffer);
    paramGL10.glTexCoordPointer(2, 5126, 0, this.mTextureBuffer);
    paramGL10.glEnable(3553);
    paramGL10.glBindTexture(3553, this.mTexture1);
    paramGL10.glEnableClientState(32884);
    paramGL10.glEnableClientState(32888);
    paramGL10.glFrontFace(2305);
    int i = 0;
    while (i < 25)
    {
      paramGL10.glPushMatrix();
      paramGL10.glColor4f(this.mParticles[i].r, this.mParticles[i].g, this.mParticles[i].b, this.mParticles[i].life);
      paramGL10.glTranslatef(this.mParticles[i].pos.x, this.mParticles[i].pos.y, this.mParticles[i].pos.z);
      paramGL10.glDrawElements(4, this.mIndexBuffer.capacity(), 5121, this.mIndexBuffer);
      paramGL10.glPopMatrix();
      i += 1;
    }
    paramGL10.glDisable(3553);
    paramGL10.glDisableClientState(32884);
    paramGL10.glDisableClientState(32888);
    if (this.delay > 25)
    {
      this.col += 1;
      this.delay = 0;
      if (this.col > 11)
        this.col = 0;
    }
    this.delay += 1;
  }

  public ByteBuffer makeByteBuffer(byte[] paramArrayOfByte)
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocateDirect(paramArrayOfByte.length);
    localByteBuffer.order(ByteOrder.nativeOrder());
    localByteBuffer.put(paramArrayOfByte);
    localByteBuffer.position(0);
    return localByteBuffer;
  }

  public FloatBuffer makeFloatBuffer(float[] paramArrayOfFloat)
  {
    Object localObject = ByteBuffer.allocateDirect(paramArrayOfFloat.length * 4);
    ((ByteBuffer)localObject).order(ByteOrder.nativeOrder());
    localObject = ((ByteBuffer)localObject).asFloatBuffer();
    ((FloatBuffer)localObject).put(paramArrayOfFloat);
    ((FloatBuffer)localObject).position(0);
    return (FloatBuffer)localObject;
  }

  public void update()
  {
    long l = System.currentTimeMillis();
    float f = (float)(l - this.mLastTime) / 1000.0F;
    this.mLastTime = l;
    int i = 0;
    while (i < 25)
    {
      Object localObject = this.mParticles[i].vel;
      ((Vector3)localObject).y += this.GRAVITY * f;
      localObject = this.mParticles[i].pos;
      ((Vector3)localObject).x += this.mParticles[i].vel.x * f;
      localObject = this.mParticles[i].pos;
      ((Vector3)localObject).y += this.mParticles[i].vel.y * f;
      localObject = this.mParticles[i].pos;
      ((Vector3)localObject).z += this.mParticles[i].vel.z * f;
      localObject = this.mParticles[i];
      ((GLParticle)localObject).life -= this.mParticles[i].brightness;
      if (this.mParticles[i].life < 0.7F)
        initParticle(i);
      i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.loading.GLParticleSystem
 * JD-Core Version:    0.6.0
 */