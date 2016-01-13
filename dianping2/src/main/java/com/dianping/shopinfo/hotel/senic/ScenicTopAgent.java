package com.dianping.shopinfo.hotel.senic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.speed.SpeedMonitorHelper;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.shopinfo.activity.ShopInfoActivity;
import com.dianping.shopinfo.common.TopAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.shopinfo.widget.TourShopInfoHeaderView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class ScenicTopAgent extends TopAgent
{
  protected final View.OnClickListener iconClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      DPObject localDPObject = ScenicTopAgent.this.getShop();
      if (localDPObject == null);
      label309: label325: 
      while (true)
      {
        return;
        if ((localDPObject.contains("PicCount")) && (localDPObject.getInt("PicCount") == 0) && (TextUtils.isEmpty(localDPObject.getString("DefaultPic"))))
        {
          UploadPhotoUtil.uploadShopPhoto(ScenicTopAgent.this.getContext(), ScenicTopAgent.this.getShop());
          return;
        }
        paramView = "dianping://shopphoto";
        if (ScenicTopAgent.this.isWeddingType())
          paramView = "dianping://weddinghotelphoto";
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
        paramView.putExtra("objShop", localDPObject);
        if (ScenicTopAgent.this.getSharedObject("WeddingHotelExtra") != null)
          paramView.putExtra("extraWeddingShop", (DPObject)ScenicTopAgent.this.getSharedObject("WeddingHotelExtra"));
        boolean bool;
        if ((localDPObject.getInt("Status") == 1) || (localDPObject.getInt("Status") == 4))
        {
          bool = false;
          paramView.putExtra("enableUpload", bool);
          ScenicTopAgent.this.getFragment().startActivity(paramView);
          paramView = new ArrayList();
          paramView.add(new BasicNameValuePair("shopid", String.valueOf(localDPObject.getInt("ID"))));
          if (!ScenicTopAgent.this.isTravelType())
            break label309;
          ScenicTopAgent.this.statisticsEvent("shopinfo5", "shopinfo5_viewphoto2", "", 0, paramView);
        }
        while (true)
        {
          if (!ScenicTopAgent.this.isWeddingType())
            break label325;
          paramView = new ArrayList();
          paramView.add(new BasicNameValuePair("shopid", ScenicTopAgent.this.shopId() + ""));
          ScenicTopAgent.this.statisticsEvent("shopinfow", "shopinfow_topphoto", "", 0, paramView);
          return;
          bool = true;
          break;
          ScenicTopAgent.this.statisticsEvent("shopinfo5", "shopinfo5_viewphoto", "", 0, paramView);
        }
      }
    }
  };

  public ScenicTopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initHeaderView()
  {
    if (this.topView == null)
    {
      if (getShop() != null)
        this.topView = ((ShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.scenic_shop_top, getParentView(), false));
    }
    else
      return;
    this.topView = ((TourShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_panel_tour, getParentView(), false));
    NetworkImageView localNetworkImageView = (NetworkImageView)this.topView.findViewById(R.id.shop_panel_collect_icon);
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)localNetworkImageView.getLayoutParams();
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    ((DPActivity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
    localLayoutParams.height = (int)(localDisplayMetrics.widthPixels * 0.5620000000000001D);
    localNetworkImageView.setLayoutParams(localLayoutParams);
  }

  private void modifyHeaderView()
  {
    View localView = this.topView.findViewById(R.id.score_text_layout);
    this.topView.findViewById(R.id.text2_renjun_value).setVisibility(8);
    TextView localTextView = (TextView)this.topView.findViewById(R.id.text2_shop_score1);
    Object localObject = (TextView)this.topView.findViewById(R.id.text2_vote_total);
    int i = getShop().getInt("VoteTotal");
    ((TextView)localObject).setText(i + "条点评");
    String str = getShop().getString("StarGrade");
    if (!TextUtils.isEmpty(str))
    {
      if ((!str.equalsIgnoreCase("3A景区")) && (!str.equalsIgnoreCase("4A景区")))
      {
        localObject = str;
        if (!str.equalsIgnoreCase("5A景区"));
      }
      else
      {
        localObject = "国家" + str;
      }
      localView.setVisibility(0);
      localTextView.setText((CharSequence)localObject);
      localTextView.setPadding(0, 0, ViewUtils.dip2px(getContext(), 7.0F), 0);
    }
    while (true)
    {
      localObject = (TextView)this.topView.findViewById(R.id.text2_renjun_value);
      str = getShop().getString("PriceText");
      if ((!TextUtils.isEmpty(str)) && (!str.equalsIgnoreCase("￥-/人")))
        break;
      ((TextView)localObject).setVisibility(8);
      return;
      localView.setVisibility(8);
    }
    ((TextView)localObject).setVisibility(0);
    ((TextView)localObject).setText(str);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    paramBundle = getShop();
    removeAllCells();
    if ((paramBundle == null) && (!isWeddingType()))
      break label20;
    label20: 
    do
      return;
    while (getFragment() == null);
    initHeaderView();
    if (getShopStatus() == 0)
      this.topView.setShop(paramBundle, 0);
    while (true)
    {
      modifyHeaderView();
      this.topView.setIconClickListen(this.iconClickListener);
      addCell("0200Basic.05Info", this.topView);
      if (!(getContext() instanceof ShopInfoActivity))
        break;
      ((ShopInfoActivity)getContext()).getSpeedMonitorHelper().setResponseTime(1, System.currentTimeMillis());
      return;
      this.topView.setShop(paramBundle);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.senic.ScenicTopAgent
 * JD-Core Version:    0.6.0
 */