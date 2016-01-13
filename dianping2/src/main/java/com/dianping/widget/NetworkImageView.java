package com.dianping.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;
import com.dianping.app.DPApplication;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.image.ImageService;
import com.dianping.dataservice.image.impl.ImageRequest;
import com.dianping.util.BitmapUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.styleable;
import com.dianping.widget.view.NovaImageView;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;

public class NetworkImageView extends NovaImageView
  implements FullRequestHandle<Request, Response>
{
  private static final int CORE_POOL_SIZE;
  private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
  private static final int KEEP_ALIVE = 1;
  private static final int MAXIMUM_POOL_SIZE;
  private static final Executor SAFE_THREAD_POOL_EXECUTOR;
  private final int DEFAULT_IMAGE_SIZE = ViewUtils.dip2px(getContext(), 76.0F);
  private boolean attached;
  protected boolean currentPlaceholder;
  protected int direction = 0;
  private NetImageHandler imageHandler;
  public Boolean imageRetrieve;
  private ImageService imageService;
  private int imageSize = this.DEFAULT_IMAGE_SIZE;
  private boolean isBottomRound = false;
  private boolean isBounds = false;
  private boolean isCorner = false;
  public boolean isPhoto;
  private boolean isTopRound = false;
  Task localImageTask;
  private String mModule;
  private int mRadius = 3;
  OnLoadChangeListener onLoadChangeListener;
  OnLoadingListener onLoadingListener;
  public int placeholderEmpty;
  public int placeholderError;
  public int placeholderLoading;
  private ImageView.ScaleType placeholderScaleType = ImageView.ScaleType.CENTER_INSIDE;
  protected ImageRequest request;
  public boolean requireBeforeAttach;
  protected ImageView.ScaleType savedScaleType = null;
  protected String url;

  static
  {
    CORE_POOL_SIZE = CPU_COUNT + 1;
    MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    SAFE_THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue(128), new ThreadPoolExecutor.DiscardOldestPolicy());
  }

  public NetworkImageView(Context paramContext)
  {
    this(paramContext, null);
  }

  public NetworkImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public NetworkImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.NetworkImageView);
    this.requireBeforeAttach = paramContext.getBoolean(R.styleable.NetworkImageView_requireBeforeAttach, false);
    this.placeholderEmpty = paramContext.getResourceId(R.styleable.NetworkImageView_placeholderEmpty, 0);
    this.placeholderLoading = paramContext.getResourceId(R.styleable.NetworkImageView_placeholderLoading, 0);
    this.placeholderError = paramContext.getResourceId(R.styleable.NetworkImageView_placeholderError, 0);
    if ("centerCrop".equals(paramContext.getString(R.styleable.NetworkImageView_placeholderStyleType)));
    for (this.placeholderScaleType = ImageView.ScaleType.CENTER_CROP; ; this.placeholderScaleType = ImageView.ScaleType.CENTER_INSIDE)
    {
      this.isCorner = paramContext.getBoolean(R.styleable.NetworkImageView_isCorner, false);
      this.mRadius = paramContext.getInt(R.styleable.NetworkImageView_radius, 3);
      this.isTopRound = paramContext.getBoolean(R.styleable.NetworkImageView_isTopRound, false);
      this.isBottomRound = paramContext.getBoolean(R.styleable.NetworkImageView_isBottomRound, false);
      this.isBounds = paramContext.getBoolean(R.styleable.NetworkImageView_isBounds, false);
      paramContext.recycle();
      return;
    }
  }

  private Bitmap getRoundedCornerBitmap(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
    {
      paramBitmap = null;
      return paramBitmap;
    }
    int m = paramBitmap.getWidth();
    int k = paramBitmap.getHeight();
    Bitmap localBitmap = Bitmap.createBitmap(m, k, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    Paint localPaint = new Paint(1);
    Rect localRect = new Rect(0, 0, m, k);
    Object localObject = new RectF(localRect);
    localCanvas.drawARGB(0, 0, 0, 0);
    localPaint.setColor(-12434878);
    localCanvas.drawRoundRect((RectF)localObject, this.mRadius, this.mRadius, localPaint);
    if ((this.isTopRound) || (this.isBottomRound))
      if (!this.isTopRound)
        break label281;
    label281: for (int i = this.mRadius; ; i = 0)
    {
      int j = k;
      if (this.isBottomRound)
        j = k - this.mRadius;
      localObject = new Rect(0, i, m, j);
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
      localCanvas.drawRect((Rect)localObject, localPaint);
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
      localCanvas.drawBitmap(paramBitmap, localRect, localRect, localPaint);
      paramBitmap = localBitmap;
      if (!this.isBounds)
        break;
      localPaint.setStyle(Paint.Style.STROKE);
      localPaint.setStrokeWidth(1.0F);
      localPaint.setColor(201326592);
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
      localCanvas.drawRect(localRect, localPaint);
      return localBitmap;
    }
  }

  protected boolean discard()
  {
    if ((this.url != null) && (Boolean.FALSE.equals(this.imageRetrieve)))
    {
      if (this.url.startsWith("http://"))
        if (this.request != null)
        {
          imageService().abort(this.request, this, true);
          this.request = null;
        }
      while (true)
      {
        this.imageRetrieve = null;
        return true;
        if (this.localImageTask == null)
          continue;
        this.localImageTask.cancel(true);
        this.localImageTask = null;
      }
    }
    return false;
  }

  protected Bitmap getBitmapFromResponse(Response paramResponse)
  {
    return (Bitmap)paramResponse.result();
  }

  public NetImageHandler getImageHandler()
  {
    return this.imageHandler;
  }

  protected ImageService imageService()
  {
    monitorenter;
    try
    {
      if (this.imageService == null)
        this.imageService = ((ImageService)DPApplication.instance().getService("image"));
      return this.imageService;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.attached = true;
    require();
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.attached = false;
    discard();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (this.onLoadingListener != null)
      this.onLoadingListener.onLoadingPause();
    if (this.onLoadChangeListener != null)
      this.onLoadChangeListener.onImageLoadFailed();
    if ((Boolean.FALSE == this.imageRetrieve) && (paramRequest == this.request))
    {
      setPlaceHolder(this.placeholderError);
      this.imageRetrieve = Boolean.valueOf(true);
      this.request = null;
    }
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    paramResponse = getBitmapFromResponse(paramResponse);
    if (this.onLoadingListener != null)
      this.onLoadingListener.onLoadingPause();
    if (this.onLoadChangeListener != null)
      this.onLoadChangeListener.onImageLoadSuccess(paramResponse);
    if ((Boolean.FALSE == this.imageRetrieve) && (paramRequest == this.request))
    {
      setImageBitmap(paramResponse);
      this.imageRetrieve = Boolean.valueOf(true);
      this.request = null;
    }
  }

  public void onRequestProgress(Request paramRequest, int paramInt1, int paramInt2)
  {
    if (this.onLoadingListener != null)
      this.onLoadingListener.onLoadingProgress(paramInt1, paramInt2);
  }

  public void onRequestStart(Request paramRequest)
  {
    if (this.onLoadingListener != null)
      this.onLoadingListener.onLoadingResume();
    if (this.onLoadChangeListener != null)
      this.onLoadChangeListener.onImageLoadStart();
  }

  protected void onWindowVisibilityChanged(int paramInt)
  {
    super.onWindowVisibilityChanged(paramInt);
    if (paramInt == 0)
    {
      this.attached = true;
      require();
      return;
    }
    this.attached = false;
    discard();
  }

  protected boolean require()
  {
    if ((!this.attached) && (!this.requireBeforeAttach))
      return false;
    if (this.url == null)
    {
      setPlaceHolder(this.placeholderEmpty);
      this.imageRetrieve = Boolean.valueOf(true);
      return true;
    }
    if (this.imageRetrieve == null)
    {
      setPlaceHolder(this.placeholderLoading);
      int i;
      if ((this.url.startsWith("http://")) || (this.url.startsWith("https://")))
      {
        String str = this.url;
        if (this.isPhoto)
        {
          i = 2;
          this.request = new ImageRequest(str, i);
          this.request.setImageModule(this.mModule);
          imageService().exec(this.request, this);
        }
      }
      while (true)
      {
        this.imageRetrieve = Boolean.valueOf(false);
        return true;
        i = 1;
        break;
        this.localImageTask = new Task();
        if (Build.VERSION.SDK_INT >= 11)
        {
          this.localImageTask.executeOnExecutor(SAFE_THREAD_POOL_EXECUTOR, new Void[0]);
          continue;
        }
        this.localImageTask.execute(new Void[0]);
      }
    }
    return false;
  }

  public void setAttached(boolean paramBoolean)
  {
    this.attached = paramBoolean;
  }

  public void setDirection(int paramInt)
  {
    this.direction = paramInt;
  }

  public void setDrawable(Drawable paramDrawable, boolean paramBoolean)
  {
    if (this.currentPlaceholder != paramBoolean)
    {
      if (!paramBoolean)
        break label64;
      if (this.savedScaleType == null)
        this.savedScaleType = getScaleType();
      setScaleType(this.placeholderScaleType);
    }
    while (true)
    {
      this.currentPlaceholder = paramBoolean;
      setImageDrawable(paramDrawable);
      if ((paramBoolean) && ((paramDrawable instanceof AnimationDrawable)))
        ((AnimationDrawable)paramDrawable).start();
      return;
      label64: if (this.savedScaleType == null)
        continue;
      setScaleType(this.savedScaleType);
    }
  }

  public void setImage(String paramString)
  {
    String str = paramString;
    if (paramString != null)
    {
      str = paramString;
      if (paramString.length() == 0)
        str = null;
    }
    if ((str == null) && (this.url == null));
    do
    {
      do
        return;
      while ((str != null) && (str.equals(this.url)));
      this.direction = 0;
      discard();
      this.imageRetrieve = null;
      this.url = str;
      require();
    }
    while (this.imageRetrieve != null);
    setImageDrawable(null);
  }

  public void setImage(String paramString, int paramInt)
  {
    String str = paramString;
    if (paramString != null)
    {
      str = paramString;
      if (paramString.length() == 0)
        str = null;
    }
    if ((str == null) && (this.url == null));
    do
    {
      do
        return;
      while ((str != null) && (str.equals(this.url)) && (paramInt == this.direction));
      this.direction = paramInt;
      discard();
      this.imageRetrieve = null;
      this.url = str;
      require();
    }
    while (this.imageRetrieve != null);
    setImageDrawable(null);
  }

  public void setImageBitmap(Bitmap paramBitmap)
  {
    if (this.savedScaleType != null)
      setScaleType(this.savedScaleType);
    this.currentPlaceholder = false;
    Bitmap localBitmap = paramBitmap;
    if (this.isCorner)
      localBitmap = getRoundedCornerBitmap(paramBitmap);
    super.setImageBitmap(localBitmap);
    if (this.imageHandler != null)
      this.imageHandler.onFinish();
  }

  public void setImageHandler(NetImageHandler paramNetImageHandler)
  {
    this.imageHandler = paramNetImageHandler;
  }

  public void setImageModule(String paramString)
  {
    this.mModule = paramString;
  }

  public void setImageSize(int paramInt)
  {
    this.imageSize = paramInt;
  }

  public void setIsCorner(boolean paramBoolean)
  {
    this.isCorner = paramBoolean;
  }

  public void setLoadChangeListener(OnLoadChangeListener paramOnLoadChangeListener)
  {
    this.onLoadChangeListener = paramOnLoadChangeListener;
  }

  public void setLoadingListener(OnLoadingListener paramOnLoadingListener)
  {
    this.onLoadingListener = paramOnLoadingListener;
  }

  public void setLocalBitmap(Bitmap paramBitmap)
  {
    setImageBitmap(paramBitmap);
    this.url = "local_bitmap";
    this.imageRetrieve = Boolean.valueOf(true);
  }

  public void setLocalDrawable(Drawable paramDrawable)
  {
    setDrawable(paramDrawable, false);
    this.url = "local_drawable";
    this.imageRetrieve = Boolean.valueOf(true);
  }

  protected void setPlaceHolder(int paramInt)
  {
    if (!this.currentPlaceholder)
    {
      if (this.savedScaleType == null)
        this.savedScaleType = getScaleType();
      setScaleType(this.placeholderScaleType);
    }
    this.currentPlaceholder = true;
    super.setImageResource(paramInt);
  }

  public void setRoundPixels(int paramInt)
  {
    this.mRadius = paramInt;
  }

  class Task extends AsyncTask<Void, Void, Bitmap>
  {
    Task()
    {
    }

    protected Bitmap doInBackground(Void[] paramArrayOfVoid)
    {
      Bitmap localBitmap = null;
      paramArrayOfVoid = localBitmap;
      Object localObject;
      try
      {
        if (NetworkImageView.this.direction == 0)
        {
          paramArrayOfVoid = localBitmap;
          NetworkImageView.this.direction = BitmapUtils.readPictureDegree(NetworkImageView.this.url);
        }
        paramArrayOfVoid = localBitmap;
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        paramArrayOfVoid = localBitmap;
        localOptions.inJustDecodeBounds = true;
        paramArrayOfVoid = localBitmap;
        BitmapFactory.decodeFile(NetworkImageView.this.url, localOptions);
        paramArrayOfVoid = localBitmap;
        if (localOptions.outWidth > localOptions.outHeight)
          paramArrayOfVoid = localBitmap;
        for (int i = localOptions.outWidth; ; i = localOptions.outHeight)
        {
          paramArrayOfVoid = localBitmap;
          i /= NetworkImageView.this.imageSize;
          paramArrayOfVoid = localBitmap;
          localOptions.inJustDecodeBounds = false;
          paramArrayOfVoid = localBitmap;
          localOptions.inSampleSize = (i + 1);
          paramArrayOfVoid = localBitmap;
          localBitmap = BitmapFactory.decodeFile(NetworkImageView.this.url, localOptions);
          paramArrayOfVoid = localBitmap;
          localObject = localBitmap;
          if (NetworkImageView.this.direction == 0)
            break;
          paramArrayOfVoid = localBitmap;
          localObject = new Matrix();
          paramArrayOfVoid = localBitmap;
          ((Matrix)localObject).setRotate(NetworkImageView.this.direction, localOptions.outWidth / 2.0F, localOptions.outHeight / 2.0F);
          paramArrayOfVoid = localBitmap;
          return Bitmap.createBitmap(localBitmap, 0, 0, localOptions.outWidth, localOptions.outHeight, (Matrix)localObject, false);
          paramArrayOfVoid = localBitmap;
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        localObject = paramArrayOfVoid;
      }
      return (Bitmap)localObject;
    }

    protected void onPostExecute(Bitmap paramBitmap)
    {
      if (NetworkImageView.this.onLoadingListener != null)
        NetworkImageView.this.onLoadingListener.onLoadingPause();
      if (paramBitmap == null)
      {
        if (NetworkImageView.this.onLoadChangeListener != null)
          NetworkImageView.this.onLoadChangeListener.onImageLoadFailed();
        if ((Boolean.FALSE == NetworkImageView.this.imageRetrieve) && (NetworkImageView.this.localImageTask == this))
        {
          NetworkImageView.this.setPlaceHolder(NetworkImageView.this.placeholderError);
          NetworkImageView.this.imageRetrieve = Boolean.valueOf(true);
          NetworkImageView.this.localImageTask = null;
        }
      }
      do
      {
        return;
        if (NetworkImageView.this.onLoadChangeListener == null)
          continue;
        NetworkImageView.this.onLoadChangeListener.onImageLoadSuccess(paramBitmap);
      }
      while ((Boolean.FALSE != NetworkImageView.this.imageRetrieve) || (NetworkImageView.this.localImageTask != this));
      NetworkImageView.this.setImageBitmap(paramBitmap);
      NetworkImageView.this.imageRetrieve = Boolean.valueOf(true);
      NetworkImageView.this.localImageTask = null;
    }

    protected void onPreExecute()
    {
      if (NetworkImageView.this.onLoadingListener != null)
        NetworkImageView.this.onLoadingListener.onLoadingResume();
      if (NetworkImageView.this.onLoadChangeListener != null)
        NetworkImageView.this.onLoadChangeListener.onImageLoadStart();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.NetworkImageView
 * JD-Core Version:    0.6.0
 */