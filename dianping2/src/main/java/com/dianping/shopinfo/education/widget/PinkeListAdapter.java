package com.dianping.shopinfo.education.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.shopinfo.education.view.PinkeProgressBar;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetImageHandler;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;

public class PinkeListAdapter extends BaseAdapter
{
  private Context mContext = null;
  private LayoutInflater mInflater;
  public DPObject[] pinkeitems;

  public PinkeListAdapter(Context paramContext, DPObject[] paramArrayOfDPObject)
  {
    this.pinkeitems = paramArrayOfDPObject;
    this.mContext = paramContext;
    this.mInflater = LayoutInflater.from(this.mContext);
  }

  public int getCount()
  {
    return this.pinkeitems.length;
  }

  public Object getItem(int paramInt)
  {
    return this.pinkeitems[paramInt];
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null)
    {
      paramViewGroup = new PinkeItemView();
      paramView = this.mInflater.inflate(R.layout.shop_education_pinkeproductall_item, null);
      paramViewGroup.pinkeitemtitleTV = ((TextView)paramView.findViewById(R.id.pinkeitemtitleTV));
      paramViewGroup.pinkeitempicIV = ((NetworkImageView)paramView.findViewById(R.id.pinkeitempicIV));
      paramViewGroup.currentPriceTV = ((TextView)paramView.findViewById(R.id.currentPriceTV));
      paramViewGroup.pinkeProgressBar = ((PinkeProgressBar)paramView.findViewById(R.id.pinkeprogressBar));
      paramViewGroup.pinkesavedescriptionTV = ((TextView)paramView.findViewById(R.id.pinkesavedescriptionTV));
      paramViewGroup.savedPriceTV = ((TextView)paramView.findViewById(R.id.savedPriceTV));
      paramView.setTag(paramViewGroup);
    }
    while (true)
    {
      DPObject localDPObject = this.pinkeitems[paramInt];
      String str1 = localDPObject.getString("Name");
      String str2 = localDPObject.getString("PicUrl");
      int i = localDPObject.getInt("CurrentPrice");
      int j = localDPObject.getInt("Percent");
      String str3 = localDPObject.getString("SaveDescription");
      int k = localDPObject.getInt("SavePrice");
      ((NovaLinearLayout)paramView).setGAString("edu_pinke", str1, paramInt);
      if ((paramViewGroup.pinkeitemtitleTV != null) && (!TextUtils.isEmpty(str1)))
        paramViewGroup.pinkeitemtitleTV.setText(str1);
      if ((paramViewGroup.pinkeitempicIV != null) && (!TextUtils.isEmpty(str2)))
      {
        paramViewGroup.pinkeitempicIV.setImage(str2);
        paramViewGroup.pinkeitempicIV.setImageHandler(new NetImageHandler(paramViewGroup)
        {
          public void onFinish()
          {
            this.val$finalPinkeItemView.pinkeitempicIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
          }
        });
      }
      if ((paramViewGroup.currentPriceTV != null) && (i >= 0))
        paramViewGroup.currentPriceTV.setText(i + "");
      if ((paramViewGroup.pinkeProgressBar != null) && (j >= 0))
      {
        paramViewGroup.pinkeProgressBar.setCircleRadius(ViewUtils.dip2px(this.mContext, 4.0F));
        paramViewGroup.pinkeProgressBar.setProgress(j);
      }
      if ((paramViewGroup.pinkesavedescriptionTV != null) && (!TextUtils.isEmpty(str3)))
        paramViewGroup.pinkesavedescriptionTV.setText(str3);
      if ((paramViewGroup.savedPriceTV != null) && (k >= 0))
        paramViewGroup.savedPriceTV.setText(k + "");
      return paramView;
      paramViewGroup = (PinkeItemView)paramView.getTag();
    }
  }

  public final class PinkeItemView
  {
    public TextView currentPriceTV;
    public PinkeProgressBar pinkeProgressBar;
    public NetworkImageView pinkeitempicIV;
    public TextView pinkeitemtitleTV;
    public TextView pinkesavedescriptionTV;
    public TextView savedPriceTV;

    public PinkeItemView()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.widget.PinkeListAdapter
 * JD-Core Version:    0.6.0
 */