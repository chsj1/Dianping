package com.dianping.imagemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.ImageView.ScaleType;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.image.ImageService;
import com.dianping.imagemanager.utils.LocalImageRequest;
import com.dianping.imagemanager.utils.LocalImageService;
import com.dianping.imagemanager.utils.NetworkImageRequest;
import com.dianping.imagemanager.utils.NetworkImageService;
import com.dianping.imagemanager.utils.RoundedDrawable;
import com.dianping.util.BitmapUtils;
import com.dianping.util.ViewUtils;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.styleable;
import com.dianping.widget.OnLoadChangeListener;
import com.dianping.widget.OnLoadingListener;
import com.dianping.widget.view.NovaImageView;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

public class DPNetworkImageView extends NovaImageView
  implements FullRequestHandle<Request, Response>, View.OnClickListener
{
  public static final int SIZE_ADAPTIVE = 0;
  private static final String TAG = "DPNetworkImageView";
  private static HashSet<String> clickToRequireSet = new HashSet();
  private static ImageService imageService = NetworkImageService.getInstance();
  private static LocalImageService localImageService = LocalImageService.getInstance();
  private final int DEFAULT_IMAGE_SIZE = ViewUtils.dip2px(getContext(), 76.0F);
  private boolean attached;
  private int borderStrokeColor = 201326592;
  private float borderStrokeWidth = 0.0F;
  private CacheType cacheType;
  private int containerHeight = 0;
  private int containerWidth = this.DEFAULT_IMAGE_SIZE;
  private float cornerRadius;
  private boolean forceDownload = true;
  private int imageId = 0;
  private DPNetworkImageView.ImageProcessor imageProcessor;
  private boolean isCircle = false;
  private boolean isCustomSized = false;
  private boolean isNetworkImage = false;
  private boolean isPlaceholder;
  private boolean[] isRounedCorner = new boolean[4];
  private boolean isSizeAdaptive = false;
  private boolean isSquare = false;
  private DPNetworkImageView.LoadState loadState = DPNetworkImageView.LoadState.IDLE;
  protected LocalImageRequest localImageRequest;
  private String mModule;
  private boolean needReload = true;
  private OnLoadChangeListener onLoadChangeListener;
  private OnLoadingListener onLoadingListener;
  public int placeholderClick;
  public int placeholderEmpty;
  public int placeholderError;
  public int placeholderLoading;
  public int placeholderReload;
  private ImageView.ScaleType placeholderScaleType = ImageView.ScaleType.CENTER_INSIDE;
  protected NetworkImageRequest request;
  private boolean requireBeforeAttach;
  private View.OnClickListener savedOnClickListener = null;
  private ImageView.ScaleType savedScaleType = null;
  private String url;

  public DPNetworkImageView(Context paramContext)
  {
    this(paramContext, null);
  }

  public DPNetworkImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public DPNetworkImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.NetworkImageView);
    this.requireBeforeAttach = localTypedArray.getBoolean(R.styleable.NetworkImageView_requireBeforeAttach, false);
    this.placeholderEmpty = localTypedArray.getResourceId(R.styleable.NetworkImageView_placeholderEmpty, 0);
    this.placeholderLoading = localTypedArray.getResourceId(R.styleable.NetworkImageView_placeholderLoading, 0);
    this.placeholderError = localTypedArray.getResourceId(R.styleable.NetworkImageView_placeholderError, 0);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DPNetworkImageView);
    this.placeholderClick = localTypedArray.getResourceId(R.styleable.DPNetworkImageView_placeholderClick, R.drawable.placeholder_click);
    this.placeholderReload = localTypedArray.getResourceId(R.styleable.DPNetworkImageView_placeholderReload, R.drawable.placeholder_reload);
    if ("centerCrop".equals(paramContext.getString(R.styleable.DPNetworkImageView_placeholderScaleType)));
    for (this.placeholderScaleType = ImageView.ScaleType.CENTER_CROP; ; this.placeholderScaleType = ImageView.ScaleType.CENTER_INSIDE)
    {
      setCornerRadius(paramContext.getDimension(R.styleable.DPNetworkImageView_cornerRadius, 0.0F));
      this.borderStrokeWidth = paramContext.getDimension(R.styleable.DPNetworkImageView_borderStrokeWidth, 0.0F);
      this.borderStrokeColor = paramContext.getColor(R.styleable.DPNetworkImageView_borderColor, 201326592);
      this.isCircle = paramContext.getBoolean(R.styleable.DPNetworkImageView_isCircle, false);
      this.isSquare = paramContext.getBoolean(R.styleable.DPNetworkImageView_isSquare, false);
      this.needReload = paramContext.getBoolean(R.styleable.DPNetworkImageView_needReload, true);
      this.forceDownload = paramContext.getBoolean(R.styleable.DPNetworkImageView_forceDownload, true);
      localTypedArray.recycle();
      paramContext.recycle();
      initView();
      return;
    }
  }

  private void initView()
  {
    super.setOnClickListener(this);
    getViewTreeObserver().addOnGlobalLayoutListener(new DPNetworkImageView.1(this));
  }

  private DPNetworkImageView loadImage(String paramString, CacheType paramCacheType, int paramInt)
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
      return this;
    while ((str != null) && (str.equals(this.url)));
    discard();
    this.loadState = DPNetworkImageView.LoadState.IDLE;
    this.url = str;
    this.cacheType = paramCacheType;
    this.imageId = paramInt;
    if ((str != null) && ((str.startsWith("http://")) || (str.startsWith("https://"))));
    for (boolean bool = true; ; bool = false)
    {
      this.isNetworkImage = bool;
      require();
      return this;
    }
  }

  private static float transUnit(int paramInt, float paramFloat)
  {
    return TypedValue.applyDimension(paramInt, paramFloat, DPApplication.instance().getResources().getDisplayMetrics());
  }

  protected boolean discard()
  {
    if ((this.url != null) && (this.loadState == DPNetworkImageView.LoadState.LOADING))
    {
      if ((this.isNetworkImage) && (this.request != null))
      {
        imageService.abort(this.request, this, true);
        this.request = null;
      }
      while (true)
      {
        this.loadState = DPNetworkImageView.LoadState.IDLE;
        return true;
        if ((this.isNetworkImage) || (this.localImageRequest == null))
          continue;
        localImageService.abort(this.localImageRequest, this, true);
        this.localImageRequest = null;
      }
    }
    return false;
  }

  protected Bitmap getBitmapFromResponse(Response paramResponse)
  {
    return (Bitmap)paramResponse.result();
  }

  public DPNetworkImageView.LoadState getLoadState()
  {
    return this.loadState;
  }

  public String getURL()
  {
    return this.url;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.attached = true;
    require();
  }

  public void onClick(View paramView)
  {
    long l;
    if (clickToRequireSet.contains(this.url))
    {
      if (this.request != null)
      {
        imageService.abort(this.request, this, true);
        this.request = null;
      }
      setPlaceHolder(this.placeholderLoading);
      paramView = this.url;
      if (this.cacheType == null)
      {
        l = CacheType.HALF_MONTH.getValidtime();
        this.request = new NetworkImageRequest(paramView, l);
        this.request.setImageModule(this.mModule);
        this.loadState = DPNetworkImageView.LoadState.LOADING;
        imageService.exec(this.request, this);
      }
    }
    do
    {
      return;
      l = this.cacheType.getValidtime();
      break;
    }
    while (this.savedOnClickListener == null);
    this.savedOnClickListener.onClick(paramView);
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.attached = false;
    discard();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    Object localObject = getDrawable();
    if ((localObject != null) && ((this.isCircle) || (this.cornerRadius > 0.0F) || (this.borderStrokeWidth > 0.0F)))
    {
      int i = getScrollX();
      int j = getScrollY();
      paramCanvas.clipRect(getPaddingLeft() + i, getPaddingTop() + j, getRight() + i - getLeft() - getPaddingRight(), getBottom() + j - getTop() - getPaddingBottom());
      paramCanvas.translate(getPaddingLeft(), getPaddingTop());
      localObject = (RoundedDrawable)RoundedDrawable.fromDrawable((Drawable)localObject);
      ((RoundedDrawable)localObject).setScaleType(getScaleType()).setBounds(0, 0, paramCanvas.getWidth() - getPaddingLeft() - getPaddingRight(), paramCanvas.getHeight() - getPaddingTop() - getPaddingBottom());
      if (this.isCircle)
        ((RoundedDrawable)localObject).setCircle(true);
      while (true)
      {
        if (this.borderStrokeWidth > 0.0F)
          ((RoundedDrawable)localObject).setBorderColor(this.borderStrokeColor).setBorderWidth(this.borderStrokeWidth);
        ((RoundedDrawable)localObject).draw(paramCanvas);
        return;
        if (this.cornerRadius <= 0.0F)
          continue;
        ((RoundedDrawable)localObject).setCornerRadius(this.cornerRadius);
        ((RoundedDrawable)localObject).setCorner(this.isRounedCorner[0], this.isRounedCorner[1], this.isRounedCorner[2], this.isRounedCorner[3]);
      }
    }
    super.onDraw(paramCanvas);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if (this.isSquare)
    {
      paramInt1 = getMeasuredWidth();
      setMeasuredDimension(paramInt1, paramInt1);
    }
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (this.loadState == DPNetworkImageView.LoadState.LOADING)
    {
      if (paramRequest != this.request)
        break label147;
      this.loadState = DPNetworkImageView.LoadState.FINISHED;
      if ((this.request.getRequestOption() != 2) && (this.request.getRequestOption() != 1))
        break label105;
      setPlaceHolder(this.placeholderClick);
      clickToRequireSet.add(this.url);
      this.request = null;
    }
    while (true)
    {
      if (this.onLoadingListener != null)
        this.onLoadingListener.onLoadingPause();
      if (this.onLoadChangeListener != null)
        this.onLoadChangeListener.onImageLoadFailed();
      return;
      label105: if (this.needReload)
      {
        clickToRequireSet.add(this.url);
        setPlaceHolder(this.placeholderReload);
        break;
      }
      setPlaceHolder(this.placeholderError);
      break;
      label147: if (paramRequest != this.localImageRequest)
        continue;
      this.loadState = DPNetworkImageView.LoadState.FINISHED;
      setPlaceHolder(this.placeholderError);
      this.localImageRequest = null;
    }
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    paramResponse = getBitmapFromResponse(paramResponse);
    if (this.onLoadingListener != null)
      this.onLoadingListener.onLoadingPause();
    if (this.onLoadChangeListener != null)
      this.onLoadChangeListener.onImageLoadSuccess(paramResponse);
    if (this.loadState == DPNetworkImageView.LoadState.LOADING)
    {
      if (paramRequest != this.request)
        break label86;
      this.loadState = DPNetworkImageView.LoadState.FINISHED;
      clickToRequireSet.remove(this.url);
      setImageBitmap(paramResponse);
      this.request = null;
    }
    label86: 
    do
      return;
    while (paramRequest != this.localImageRequest);
    this.loadState = DPNetworkImageView.LoadState.FINISHED;
    setImageBitmap(paramResponse);
    this.localImageRequest = null;
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

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.savedOnClickListener == null) && (!clickToRequireSet.contains(this.url)))
      return false;
    return super.onTouchEvent(paramMotionEvent);
  }

  protected boolean require()
  {
    if ((!this.attached) && (!this.requireBeforeAttach) && (this.isNetworkImage));
    do
      return false;
    while (this.loadState != DPNetworkImageView.LoadState.IDLE);
    if (this.url == null)
    {
      this.loadState = DPNetworkImageView.LoadState.FINISHED;
      setPlaceHolder(this.placeholderEmpty);
      return true;
    }
    this.loadState = DPNetworkImageView.LoadState.LOADING;
    if (this.isNetworkImage)
    {
      localObject = this.url;
      long l;
      int i;
      if (this.cacheType == null)
      {
        l = CacheType.HALF_MONTH.getValidtime();
        this.request = new NetworkImageRequest((String)localObject, l, 1);
        boolean bool = ((Boolean)imageService.execSync(this.request).result()).booleanValue();
        if ((!this.forceDownload) && (!DPActivity.preferences().getBoolean("isShowListImage", true)) && (!NetworkUtils.isWIFIConnection(DPApplication.instance())))
          break label244;
        i = 1;
        label163: if (!bool)
          break label265;
        localObject = this.url;
        if (this.cacheType != null)
          break label249;
        l = CacheType.HALF_MONTH.getValidtime();
        label187: if (i == 0)
          break label260;
        i = 0;
        label193: this.request = new NetworkImageRequest((String)localObject, l, i);
        this.request.setImageModule(this.mModule);
        imageService.exec(this.request, this);
      }
      while (true)
      {
        return true;
        l = this.cacheType.getValidtime();
        break;
        label244: i = 0;
        break label163;
        label249: l = this.cacheType.getValidtime();
        break label187;
        label260: i = 2;
        break label193;
        label265: if (i != 0)
        {
          setPlaceHolder(this.placeholderLoading);
          localObject = this.url;
          if (this.cacheType == null)
            l = CacheType.HALF_MONTH.getValidtime();
          while (true)
          {
            this.request = new NetworkImageRequest((String)localObject, l, 3);
            this.request.setImageModule(this.mModule);
            imageService.exec(this.request, this);
            break;
            l = this.cacheType.getValidtime();
          }
        }
        onRequestFailed(this.request, null);
      }
    }
    setPlaceHolder(this.placeholderLoading);
    if (this.containerHeight > 0);
    for (Object localObject = new LocalImageRequest(this.url, this.imageId, this.containerWidth, this.containerHeight); ; localObject = new LocalImageRequest(this.url, this.imageId, this.containerWidth))
    {
      this.localImageRequest = ((LocalImageRequest)localObject);
      localImageService.exec(this.localImageRequest, this);
      break;
    }
  }

  public DPNetworkImageView setBorderStrokeColor(int paramInt)
  {
    this.borderStrokeColor = paramInt;
    return this;
  }

  public DPNetworkImageView setBorderStrokeWidth(float paramFloat)
  {
    this.borderStrokeWidth = paramFloat;
    return this;
  }

  public DPNetworkImageView setBorderStrokeWidth(int paramInt, float paramFloat)
  {
    return setBorderStrokeWidth(transUnit(paramInt, paramFloat));
  }

  public DPNetworkImageView setCornerRadius(float paramFloat)
  {
    return setCornerRadius(paramFloat, true, true, true, true);
  }

  public DPNetworkImageView setCornerRadius(float paramFloat, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    this.cornerRadius = paramFloat;
    this.isRounedCorner[0] = paramBoolean1;
    this.isRounedCorner[1] = paramBoolean2;
    this.isRounedCorner[2] = paramBoolean3;
    this.isRounedCorner[3] = paramBoolean4;
    return this;
  }

  public DPNetworkImageView setCornerRadius(int paramInt, float paramFloat)
  {
    return setCornerRadius(transUnit(paramInt, paramFloat), true, true, true, true);
  }

  public DPNetworkImageView setCornerRadius(int paramInt, float paramFloat, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    return setCornerRadius(transUnit(paramInt, paramFloat), paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
  }

  public DPNetworkImageView setForceDownload(boolean paramBoolean)
  {
    this.forceDownload = paramBoolean;
    return this;
  }

  public DPNetworkImageView setImage(String paramString)
  {
    return loadImage(paramString, CacheType.HALF_MONTH, 0);
  }

  public DPNetworkImageView setImage(String paramString, int paramInt)
  {
    return loadImage(paramString, null, paramInt);
  }

  public DPNetworkImageView setImage(String paramString, CacheType paramCacheType)
  {
    return loadImage(paramString, paramCacheType, 0);
  }

  public void setImageBitmap(Bitmap paramBitmap)
  {
    if (this.savedScaleType != null)
      setScaleType(this.savedScaleType);
    if (this.url == null)
      this.loadState = DPNetworkImageView.LoadState.FINISHED;
    this.isPlaceholder = false;
    Bitmap localBitmap = paramBitmap;
    if (this.imageProcessor != null)
      localBitmap = this.imageProcessor.doPostProcess(paramBitmap);
    super.setImageBitmap(localBitmap);
  }

  public void setImageDrawable(Drawable paramDrawable)
  {
    if ((this.isPlaceholder) && (this.savedScaleType != null))
      setScaleType(this.savedScaleType);
    if (this.url == null)
      this.loadState = DPNetworkImageView.LoadState.FINISHED;
    this.isPlaceholder = false;
    int i;
    int j;
    int k;
    if ((!this.isSquare) && (this.isCustomSized) && (this.isSizeAdaptive))
    {
      if (this.containerHeight != 0)
        break label128;
      i = paramDrawable.getIntrinsicHeight();
      j = paramDrawable.getIntrinsicWidth();
      i = (this.containerWidth - getPaddingLeft() - getPaddingRight()) * i / j;
      j = getPaddingTop();
      k = getPaddingBottom();
      getLayoutParams().height = (i + j + k);
    }
    while (true)
    {
      super.setImageDrawable(paramDrawable);
      return;
      label128: if (this.containerWidth != 0)
        continue;
      i = paramDrawable.getIntrinsicHeight();
      j = paramDrawable.getIntrinsicWidth();
      i = (this.containerHeight - getPaddingTop() - getPaddingBottom()) * j / i;
      j = getPaddingLeft();
      k = getPaddingRight();
      getLayoutParams().width = (i + j + k);
    }
  }

  public DPNetworkImageView setImageModule(String paramString)
  {
    this.mModule = paramString;
    return this;
  }

  public DPNetworkImageView setImageProcessor(DPNetworkImageView.ImageProcessor paramImageProcessor)
  {
    this.imageProcessor = paramImageProcessor;
    return this;
  }

  public DPNetworkImageView setImageSize(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < -2) || (paramInt2 < -2))
      throw new IllegalArgumentException("invalid size!");
    if (getLayoutParams() == null)
      setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
    if (paramInt1 == 0)
    {
      if ((paramInt2 == 0) || (paramInt2 == -2) || (paramInt2 == -1))
        throw new IllegalArgumentException("invalid size!");
      this.isCustomSized = true;
      this.containerWidth = 0;
      this.containerHeight = paramInt2;
      getLayoutParams().height = this.containerHeight;
      this.isSizeAdaptive = true;
      return this;
    }
    if (paramInt2 == 0)
    {
      if ((paramInt1 == 0) || (paramInt1 == -2) || (paramInt1 == -1))
        throw new IllegalArgumentException("invalid size!");
      this.isCustomSized = true;
      this.containerWidth = paramInt1;
      this.containerHeight = 0;
      getLayoutParams().width = this.containerWidth;
      this.isSizeAdaptive = true;
      return this;
    }
    this.isCustomSized = true;
    this.containerWidth = paramInt1;
    this.containerHeight = paramInt2;
    getLayoutParams().width = this.containerWidth;
    getLayoutParams().height = this.containerHeight;
    this.isSizeAdaptive = false;
    return this;
  }

  public DPNetworkImageView setImageSize(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 > 0)
    {
      paramInt2 = (int)transUnit(paramInt1, paramInt2);
      if (paramInt3 <= 0)
        break label34;
    }
    label34: for (paramInt1 = (int)transUnit(paramInt1, paramInt3); ; paramInt1 = paramInt3)
    {
      return setImageSize(paramInt2, paramInt1);
      break;
    }
  }

  public DPNetworkImageView setImageWithAssetCache(String paramString1, String paramString2, CacheType paramCacheType)
  {
    Object localObject = null;
    try
    {
      paramString1 = DPApplication.instance().getAssets().open(paramString1);
      if (paramString1 != null)
        setImageBitmap(BitmapUtils.decodeSampledBitmapFromStream(Bitmap.Config.RGB_565, paramString1, ViewUtils.getScreenWidthPixels(DPApplication.instance()), ViewUtils.getScreenHeightPixels(DPApplication.instance())));
    }
    catch (IOException paramString1)
    {
      try
      {
        paramString1.close();
        return this;
        paramString1 = paramString1;
        paramString1.printStackTrace();
        paramString1 = localObject;
      }
      catch (IOException paramString1)
      {
        paramString1.printStackTrace();
        return this;
      }
    }
    return loadImage(paramString2, paramCacheType, 0);
  }

  public DPNetworkImageView setIsCircle(boolean paramBoolean)
  {
    this.isCircle = paramBoolean;
    return this;
  }

  public DPNetworkImageView setLoadChangeListener(OnLoadChangeListener paramOnLoadChangeListener)
  {
    this.onLoadChangeListener = paramOnLoadChangeListener;
    return this;
  }

  public DPNetworkImageView setLoadingListener(OnLoadingListener paramOnLoadingListener)
  {
    this.onLoadingListener = paramOnLoadingListener;
    return this;
  }

  public DPNetworkImageView setNeedReload(boolean paramBoolean)
  {
    this.needReload = paramBoolean;
    return this;
  }

  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    this.savedOnClickListener = paramOnClickListener;
  }

  protected DPNetworkImageView setPlaceHolder(int paramInt)
  {
    if (paramInt == 0);
    do
    {
      return this;
      super.setImageResource(paramInt);
    }
    while (this.isPlaceholder);
    if (this.savedScaleType == null)
      this.savedScaleType = getScaleType();
    setScaleType(this.placeholderScaleType);
    this.isPlaceholder = true;
    return this;
  }

  public DPNetworkImageView setPlaceHolderDrawable(Drawable paramDrawable)
  {
    super.setImageDrawable(paramDrawable);
    if ((paramDrawable != null) && (!this.isPlaceholder))
    {
      if (this.savedScaleType == null)
        this.savedScaleType = getScaleType();
      setScaleType(this.placeholderScaleType);
      this.isPlaceholder = true;
    }
    if ((paramDrawable instanceof AnimationDrawable))
      ((AnimationDrawable)paramDrawable).start();
    return this;
  }

  public DPNetworkImageView setPlaceholderScaleType(ImageView.ScaleType paramScaleType)
  {
    this.placeholderScaleType = paramScaleType;
    if (this.isPlaceholder)
      setScaleType(paramScaleType);
    return this;
  }

  public DPNetworkImageView setRequireBeforeAttach(boolean paramBoolean)
  {
    this.requireBeforeAttach = paramBoolean;
    return this;
  }

  public static enum CacheType
  {
    private long validtime;

    static
    {
      $VALUES = new CacheType[] { DAILY, HALF_MONTH, PERMANENT };
    }

    private CacheType(long paramLong)
    {
      this.validtime = paramLong;
    }

    public long getValidtime()
    {
      return this.validtime;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.imagemanager.DPNetworkImageView
 * JD-Core Version:    0.6.0
 */