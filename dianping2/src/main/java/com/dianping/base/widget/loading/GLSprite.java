package com.dianping.base.widget.loading;

public class GLSprite extends Renderable
{
  public int mResourceId;
  public int mTextureName;
  private int[] textures = new int[1];

  public GLSprite(int paramInt)
  {
    this.mResourceId = paramInt;
  }

  // ERROR //
  public int loadGLTexture(javax.microedition.khronos.opengles.GL10 paramGL10, android.content.Context paramContext)
  {
    // Byte code:
    //   0: aload_2
    //   1: ifnull +124 -> 125
    //   4: aload_1
    //   5: ifnull +120 -> 125
    //   8: aload_2
    //   9: invokevirtual 29	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   12: aload_0
    //   13: getfield 18	com/dianping/base/widget/loading/GLSprite:mResourceId	I
    //   16: invokevirtual 35	android/content/res/Resources:openRawResource	(I)Ljava/io/InputStream;
    //   19: astore_2
    //   20: aload_2
    //   21: invokestatic 41	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;)Landroid/graphics/Bitmap;
    //   24: astore_3
    //   25: aload_2
    //   26: invokevirtual 46	java/io/InputStream:close	()V
    //   29: aload_1
    //   30: iconst_1
    //   31: aload_0
    //   32: getfield 16	com/dianping/base/widget/loading/GLSprite:textures	[I
    //   35: iconst_0
    //   36: invokeinterface 52 4 0
    //   41: aload_1
    //   42: sipush 3553
    //   45: aload_0
    //   46: getfield 16	com/dianping/base/widget/loading/GLSprite:textures	[I
    //   49: iconst_0
    //   50: iaload
    //   51: invokeinterface 56 3 0
    //   56: aload_1
    //   57: sipush 3553
    //   60: sipush 10241
    //   63: ldc 57
    //   65: invokeinterface 61 4 0
    //   70: aload_1
    //   71: sipush 3553
    //   74: sipush 10240
    //   77: ldc 62
    //   79: invokeinterface 61 4 0
    //   84: aload_1
    //   85: sipush 3553
    //   88: sipush 10242
    //   91: ldc 63
    //   93: invokeinterface 61 4 0
    //   98: aload_1
    //   99: sipush 3553
    //   102: sipush 10243
    //   105: ldc 63
    //   107: invokeinterface 61 4 0
    //   112: sipush 3553
    //   115: iconst_0
    //   116: aload_3
    //   117: iconst_0
    //   118: invokestatic 69	android/opengl/GLUtils:texImage2D	(IILandroid/graphics/Bitmap;I)V
    //   121: aload_3
    //   122: invokevirtual 74	android/graphics/Bitmap:recycle	()V
    //   125: aload_0
    //   126: aload_0
    //   127: getfield 16	com/dianping/base/widget/loading/GLSprite:textures	[I
    //   130: iconst_0
    //   131: iaload
    //   132: putfield 76	com/dianping/base/widget/loading/GLSprite:mTextureName	I
    //   135: aload_0
    //   136: getfield 76	com/dianping/base/widget/loading/GLSprite:mTextureName	I
    //   139: ireturn
    //   140: astore_1
    //   141: aload_2
    //   142: invokevirtual 46	java/io/InputStream:close	()V
    //   145: aload_1
    //   146: athrow
    //   147: astore_2
    //   148: goto -119 -> 29
    //   151: astore_2
    //   152: goto -7 -> 145
    //
    // Exception table:
    //   from	to	target	type
    //   20	25	140	finally
    //   25	29	147	java/io/IOException
    //   141	145	151	java/io/IOException
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.loading.GLSprite
 * JD-Core Version:    0.6.0
 */