package com.dianping.movie.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.ImageUtils;
import com.dianping.movie.util.MovieUtil;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import com.google.zxing.WriterException;

public class MovieTicketCodeView extends NovaLinearLayout
{
  public static int FROM_MOVIETICKETDETAIL;
  public static int FROM_PURCHASERESULT = 1;
  private ImageView imgQRCode;
  private View qrImageLayer;
  private TextView tvQRCodeTips;
  private TextView tvTicketId;
  private TextView tvTicketMachinePosition;
  private TextView tvTicketNo;

  static
  {
    FROM_MOVIETICKETDETAIL = 2;
  }

  public MovieTicketCodeView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieTicketCodeView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  private void init()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.movie_ticketcode_view, this, true);
    this.imgQRCode = ((ImageView)findViewById(R.id.qacode_img));
    this.tvTicketId = ((TextView)findViewById(R.id.ticket_id_tv));
    this.tvTicketNo = ((TextView)findViewById(R.id.ticket_no_tv));
    this.tvTicketMachinePosition = ((TextView)findViewById(R.id.ticketmachine_position_tv));
    this.tvQRCodeTips = ((TextView)findViewById(R.id.qacode_tips_tv));
    this.qrImageLayer = findViewById(R.id.qr_image_layer);
  }

  private void setTicketCodeItem(TextView paramTextView, DPObject paramDPObject)
  {
    String str = paramDPObject.getString("Name");
    StringBuilder localStringBuilder = new StringBuilder(str);
    localStringBuilder.append("：");
    paramDPObject = paramDPObject.getString("ID");
    if ((TextUtils.isDigitsOnly(paramDPObject)) && (paramDPObject.length() >= 8))
      localStringBuilder.append(MovieUtil.speratedString(paramDPObject, 4, " "));
    while (true)
    {
      paramDPObject = new SpannableString(localStringBuilder);
      paramDPObject.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tuan_common_orange)), str.length() + 1, paramDPObject.length(), 33);
      paramTextView.setText(paramDPObject);
      paramTextView.setVisibility(0);
      return;
      localStringBuilder.append(paramDPObject);
    }
  }

  public void setTicketInfo(DPObject[] paramArrayOfDPObject, String paramString1, String paramString2, String paramString3)
  {
    this.imgQRCode.setVisibility(8);
    this.tvQRCodeTips.setVisibility(8);
    this.tvTicketNo.setVisibility(8);
    this.tvTicketId.setVisibility(8);
    this.tvTicketMachinePosition.setVisibility(8);
    this.qrImageLayer.setVisibility(8);
    if ((paramArrayOfDPObject != null) && (paramArrayOfDPObject.length >= 0))
    {
      setTicketCodeItem(this.tvTicketNo, paramArrayOfDPObject[0]);
      if (paramArrayOfDPObject.length > 1)
        setTicketCodeItem(this.tvTicketId, paramArrayOfDPObject[1]);
    }
    if (!TextUtils.isEmpty(paramString1))
    {
      paramArrayOfDPObject = new SpannableString("取票点：" + paramString1);
      paramArrayOfDPObject.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.deep_black)), 4, paramArrayOfDPObject.length(), 33);
      this.tvTicketMachinePosition.setText(paramArrayOfDPObject);
      this.tvTicketMachinePosition.setVisibility(0);
    }
    int i;
    int j;
    if (!TextUtils.isEmpty(paramString2))
    {
      i = ViewUtils.dip2px(getContext(), 200.0F);
      j = ViewUtils.dip2px(getContext(), 200.0F);
    }
    try
    {
      paramArrayOfDPObject = ImageUtils.encodeAsBitmap(paramString2, i, j);
      this.imgQRCode.setImageBitmap(paramArrayOfDPObject);
      this.imgQRCode.setVisibility(0);
      if (!TextUtils.isEmpty(paramString3))
      {
        this.tvQRCodeTips.setText(paramString3);
        this.tvQRCodeTips.setVisibility(0);
      }
      this.qrImageLayer.setVisibility(0);
      return;
    }
    catch (WriterException paramArrayOfDPObject)
    {
      paramArrayOfDPObject.printStackTrace();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.MovieTicketCodeView
 * JD-Core Version:    0.6.0
 */