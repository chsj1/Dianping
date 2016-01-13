package com.dianping.base.widget.loading;

public final class Vector3
{
  public float x;
  public float y;
  public float z;

  public Vector3()
  {
  }

  public Vector3(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    set(paramFloat1, paramFloat2, paramFloat3);
  }

  public Vector3(Vector3 paramVector3)
  {
    set(paramVector3);
  }

  public final float length2()
  {
    return this.x * this.x + this.y * this.y + this.z * this.z;
  }

  public final void set(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    this.x = paramFloat1;
    this.y = paramFloat2;
    this.z = paramFloat3;
  }

  public final void set(Vector3 paramVector3)
  {
    this.x = paramVector3.x;
    this.y = paramVector3.y;
    this.z = paramVector3.z;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.loading.Vector3
 * JD-Core Version:    0.6.0
 */