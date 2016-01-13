package com.dianping.tuan.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.util.ImageUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.DateUtil;
import com.dianping.util.ViewUtils;
import com.dianping.util.encrypt.TOTP;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.google.zxing.WriterException;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;

public class TuanQRVerifyActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static int QUERY_TUAN_QRVERIFY_MESSAGE = 1;
  String barCodeString;
  View contentView;
  ImageView glowImageView;
  Handler handler;
  ImageView indicatorImageView;
  int indicatorPicHeight = 205;
  int indicatorPicPaddingTop = 67;
  Bitmap indicatorPicRes;
  int indicatorPicWidth = 205;
  Boolean isFirstTime;
  String key;
  View loadingView;
  String newTimeCodeString;
  int picHeight;
  int picWidth;
  ImageView qrImageView;
  protected final Handler queryDelayHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == TuanQRVerifyActivity.QUERY_TUAN_QRVERIFY_MESSAGE)
        TuanQRVerifyActivity.this.getTuanQRVerifyData();
    }
  };
  Runnable runnable;
  String timeCodeString;
  MApiRequest tuanQRVerifyReq;

  public void finish()
  {
    this.queryDelayHandler.removeMessages(QUERY_TUAN_QRVERIFY_MESSAGE);
    super.finish();
  }

  public void getTuanQRVerifyData()
  {
    String str;
    if (getAccount() == null)
    {
      str = "";
      if (!TextUtils.isEmpty(str))
        break label58;
      if (this.tuanQRVerifyReq != null)
      {
        mapiService().abort(this.tuanQRVerifyReq, this, true);
        this.tuanQRVerifyReq = null;
      }
    }
    label58: 
    do
    {
      return;
      str = accountService().token();
      break;
    }
    while (this.tuanQRVerifyReq != null);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://app.t.dianping.com/");
    localStringBuilder.append("barcodeauthgn.bin");
    localStringBuilder.append("?token=").append(str);
    this.tuanQRVerifyReq = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DAILY);
    mapiService().exec(this.tuanQRVerifyReq, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (isLogined())
    {
      setupView();
      return;
    }
    accountService().login(this);
  }

  protected void onDestroy()
  {
    if ((this.handler != null) && (this.runnable != null))
      this.handler.removeCallbacks(this.runnable);
    if (this.tuanQRVerifyReq != null)
    {
      mapiService().abort(this.tuanQRVerifyReq, this, true);
      this.tuanQRVerifyReq = null;
    }
    super.onDestroy();
  }

  public void onLoginCancel()
  {
    super.onLoginCancel();
    finish();
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    super.onLoginSuccess(paramAccountService);
    setupView();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.tuanQRVerifyReq)
    {
      this.tuanQRVerifyReq = null;
      this.queryDelayHandler.removeMessages(QUERY_TUAN_QRVERIFY_MESSAGE);
      paramMApiRequest = this.queryDelayHandler.obtainMessage(QUERY_TUAN_QRVERIFY_MESSAGE, null);
      this.queryDelayHandler.sendMessageDelayed(paramMApiRequest, 1000L);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.tuanQRVerifyReq)
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      this.tuanQRVerifyReq = null;
      if ((paramMApiRequest != null) && ((paramMApiRequest instanceof DPObject)) && (!TextUtils.isEmpty(paramMApiRequest.getString("Barcode"))) && (!TextUtils.isEmpty(paramMApiRequest.getString("AuthKey"))))
      {
        showLoading(Boolean.valueOf(false));
        this.key = paramMApiRequest.getString("AuthKey");
        this.barCodeString = paramMApiRequest.getString("Barcode");
        showContentAfterQuery();
      }
    }
    else
    {
      return;
    }
    this.queryDelayHandler.removeMessages(QUERY_TUAN_QRVERIFY_MESSAGE);
    paramMApiRequest = this.queryDelayHandler.obtainMessage(QUERY_TUAN_QRVERIFY_MESSAGE, null);
    this.queryDelayHandler.sendMessageDelayed(paramMApiRequest, 1000L);
  }

  protected void setupView()
  {
    setContentView(R.layout.activity_tuan_qrverify);
    this.contentView = findViewById(R.id.qrvarify_content);
    this.loadingView = findViewById(R.id.loading);
    this.qrImageView = ((ImageView)findViewById(R.id.img_qr));
    this.indicatorImageView = ((ImageView)findViewById(R.id.img_indicator));
    this.glowImageView = ((ImageView)findViewById(R.id.img_glow));
    if (Build.VERSION.SDK_INT < 11)
      this.glowImageView.setVisibility(8);
    this.isFirstTime = Boolean.valueOf(true);
    showContent();
  }

  protected void showContent()
  {
    showLoading(Boolean.valueOf(true));
    getTuanQRVerifyData();
  }

  protected void showContentAfterQuery()
  {
    if ((TextUtils.isEmpty(this.barCodeString)) || (TextUtils.isEmpty(this.key)))
      return;
    this.runnable = new Runnable()
    {
      public void run()
      {
        TuanQRVerifyActivity.this.timerUpdate();
        TuanQRVerifyActivity.this.handler.postDelayed(this, 1000L);
      }
    };
    if (this.handler != null)
      this.handler.removeCallbacks(this.runnable);
    this.handler = new Handler();
    timerUpdate();
    this.handler.postDelayed(this.runnable, 1000L);
  }

  protected void showLoading(Boolean paramBoolean)
  {
    if (paramBoolean.booleanValue())
    {
      this.contentView.setVisibility(4);
      this.loadingView.setVisibility(0);
      return;
    }
    this.contentView.setVisibility(0);
    this.loadingView.setVisibility(8);
  }

  protected void showQRCode(String paramString)
  {
    int i = ViewUtils.dip2px(this, 160.0F);
    int j = ViewUtils.dip2px(this, 160.0F);
    try
    {
      paramString = ImageUtils.encodeAsBitmap(paramString, i, j);
      this.qrImageView.setImageBitmap(paramString);
      return;
    }
    catch (WriterException paramString)
    {
      paramString.printStackTrace();
    }
  }

  protected void timerUpdate()
  {
    long l1 = DateUtil.currentTimeMillis();
    for (Object localObject = Long.toHexString(l1 / 1000L / 60L).toUpperCase(); ((String)localObject).length() < 16; localObject = "0" + (String)localObject);
    long l2 = l1 / 1000L % 60L;
    l1 = l2;
    if (l2 == 0L)
      l1 = 60L;
    this.newTimeCodeString = TOTP.generateTOTP(this.key, (String)localObject, "6");
    if (this.indicatorPicRes == null)
    {
      this.indicatorPicRes = BitmapFactory.decodeResource(getResources(), R.drawable.qrcode_bg_indicator);
      this.picWidth = this.indicatorPicRes.getWidth();
      this.picHeight = this.indicatorPicRes.getHeight();
    }
    int i = ViewUtils.dip2px(this, 8.0F);
    i = (int)((float)l1 / 60.0F * (this.picHeight - i * 2) + i);
    localObject = Bitmap.createBitmap(this.indicatorPicRes, 0, this.picHeight - i, this.picWidth, i);
    this.indicatorImageView.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap)localObject));
    i = (int)((float)l1 / 60.0F * (this.indicatorPicHeight - 16) + 8);
    int j = this.indicatorPicHeight;
    int k = this.indicatorPicPaddingTop;
    localObject = (FrameLayout.LayoutParams)this.indicatorImageView.getLayoutParams();
    ((FrameLayout.LayoutParams)localObject).topMargin = ViewUtils.dip2px(this, j + k - i);
    ((FrameLayout.LayoutParams)localObject).height = ViewUtils.dip2px(this, i);
    this.indicatorImageView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    if (l1 == 60L)
      if (Build.VERSION.SDK_INT < 11)
        if (!TextUtils.equals(this.newTimeCodeString, this.timeCodeString))
        {
          this.timeCodeString = this.newTimeCodeString;
          showQRCode(this.barCodeString + "@" + this.timeCodeString);
        }
    do
    {
      return;
      localObject = ObjectAnimator.ofFloat(this.glowImageView, "alpha", new float[] { 0.0F, 1.0F }).setDuration(500L);
      ((ObjectAnimator)localObject).addListener(new Animator.AnimatorListener()
      {
        public void onAnimationCancel(Animator paramAnimator)
        {
        }

        public void onAnimationEnd(Animator paramAnimator)
        {
          if (!TextUtils.equals(TuanQRVerifyActivity.this.newTimeCodeString, TuanQRVerifyActivity.this.timeCodeString))
          {
            TuanQRVerifyActivity.this.timeCodeString = TuanQRVerifyActivity.this.newTimeCodeString;
            TuanQRVerifyActivity.this.showQRCode(TuanQRVerifyActivity.this.barCodeString + "@" + TuanQRVerifyActivity.this.timeCodeString);
          }
          ObjectAnimator.ofFloat(TuanQRVerifyActivity.this.glowImageView, "alpha", new float[] { 1.0F, 0.0F }).setDuration(500L).start();
        }

        public void onAnimationRepeat(Animator paramAnimator)
        {
        }

        public void onAnimationStart(Animator paramAnimator)
        {
        }
      });
      ((ObjectAnimator)localObject).start();
      return;
    }
    while (TextUtils.equals(this.newTimeCodeString, this.timeCodeString));
    this.timeCodeString = this.newTimeCodeString;
    showQRCode(this.barCodeString + "@" + this.timeCodeString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanQRVerifyActivity
 * JD-Core Version:    0.6.0
 */