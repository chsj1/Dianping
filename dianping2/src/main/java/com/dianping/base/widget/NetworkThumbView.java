package com.dianping.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.Response;
import com.dianping.util.MemCache;
import com.dianping.widget.NetworkImageView;
import java.util.Map;

public class NetworkThumbView extends NetworkImageView
{
  private static final int MEM_CACHE_LIFETIME = -1;
  private static final int MEM_CACHE_SIZE = 4194304;
  private static final MemCache<String, Bitmap> memcache = new MemCache(-1L)
  {
    protected int sizeOf(Object paramObject)
    {
      if ((paramObject instanceof Bitmap))
      {
        paramObject = (Bitmap)paramObject;
        return paramObject.getHeight() * paramObject.getRowBytes();
      }
      return super.sizeOf(paramObject);
    }
  };

  public NetworkThumbView(Context paramContext)
  {
    super(paramContext);
  }

  public NetworkThumbView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public NetworkThumbView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public static Map<String, Bitmap> memcache()
  {
    return memcache;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if ((Boolean.FALSE == this.imageRetrieve) && (paramRequest == this.request))
      memcache().put(paramRequest.url(), getBitmapFromResponse(paramResponse));
    super.onRequestFinish(paramRequest, paramResponse);
  }

  public void setImage(String paramString)
  {
    Bitmap localBitmap = (Bitmap)memcache().get(paramString);
    if (localBitmap != null)
    {
      if (localBitmap.isRecycled())
      {
        memcache().remove(paramString);
        super.setImage(paramString);
        return;
      }
      setImageBitmap(localBitmap);
      this.url = paramString;
      this.imageRetrieve = Boolean.valueOf(true);
      return;
    }
    super.setImage(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.NetworkThumbView
 * JD-Core Version:    0.6.0
 */