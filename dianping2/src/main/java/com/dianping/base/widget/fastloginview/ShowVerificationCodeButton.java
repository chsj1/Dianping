package com.dianping.base.widget.fastloginview;

import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.ViewUtils;
import com.dianping.util.encrypt.Base64;
import com.dianping.v1.R.drawable;
import com.dianping.widget.view.NovaImageButton;

public class ShowVerificationCodeButton extends NovaImageButton
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private DPActivity mActivity;
  private VerficationCodeRefreshListener mListener;
  private MApiRequest mVerificationCode;
  public int mVerificationCodeLength = 0;
  public String ticket = null;

  public ShowVerificationCodeButton(DPActivity paramDPActivity, VerficationCodeRefreshListener paramVerficationCodeRefreshListener)
  {
    super(paramDPActivity);
    this.mActivity = paramDPActivity;
    this.mListener = paramVerficationCodeRefreshListener;
    setOnClickListener(this);
    setBackgroundResource(R.drawable.showver_line_bg);
    setScaleType(ImageView.ScaleType.FIT_XY);
    setGAString("verification_code");
    setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.dip2px(paramDPActivity, 120.0F), -1));
  }

  public void onClick(View paramView)
  {
    sendVerificationCodeRequest(0);
  }

  protected void onDetachedFromWindow()
  {
    if ((this.mVerificationCode != null) && (this.mActivity != null))
      this.mActivity.mapiService().abort(this.mVerificationCode, this, true);
    super.onDetachedFromWindow();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mVerificationCode == paramMApiRequest)
    {
      this.mVerificationCode = null;
      this.mVerificationCodeLength = 0;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mVerificationCode == paramMApiRequest)
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        paramMApiResponse = paramMApiRequest.getString("Image");
        this.ticket = paramMApiRequest.getString("Ticket");
        this.mVerificationCodeLength = paramMApiRequest.getInt("Piclen");
        if (TextUtils.isEmpty(paramMApiResponse));
      }
    try
    {
      paramMApiRequest = Base64.decode(paramMApiResponse);
      paramMApiRequest = BitmapFactory.decodeByteArray(paramMApiRequest, 0, paramMApiRequest.length);
      if ((paramMApiRequest != null) && (!TextUtils.isEmpty(this.ticket)))
      {
        setImageBitmap(paramMApiRequest);
        if (this.mListener != null)
          this.mListener.onVerficationCodeRefreshListener();
      }
      label112: this.mVerificationCode = null;
      return;
    }
    catch (java.lang.Throwable paramMApiRequest)
    {
      break label112;
    }
  }

  public void sendVerificationCodeRequest(int paramInt)
  {
    if (this.mVerificationCode != null)
      this.mActivity.mapiService().abort(this.mVerificationCode, this, true);
    this.mVerificationCode = BasicMApiRequest.mapiPost("http://m.api.dianping.com/mlogin/verifyimage.api", new String[] { "type", "1", "isfirst", String.valueOf(paramInt) });
    this.mActivity.mapiService().exec(this.mVerificationCode, this);
  }

  public static abstract interface VerficationCodeRefreshListener
  {
    public abstract void onVerficationCodeRefreshListener();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.fastloginview.ShowVerificationCodeButton
 * JD-Core Version:    0.6.0
 */