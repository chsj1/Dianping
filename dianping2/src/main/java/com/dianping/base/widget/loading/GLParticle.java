package com.dianping.base.widget.loading;

public class GLParticle extends GLSprite
{
  public float b = 1.0F;
  public float brightness;
  public float g = 1.0F;
  public float life;
  public float r = 1.0F;

  public GLParticle(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt)
  {
    super(paramInt);
    this.pos.x = paramFloat1;
    this.pos.y = paramFloat2;
    this.pos.z = paramFloat3;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.loading.GLParticle
 * JD-Core Version:    0.6.0
 */