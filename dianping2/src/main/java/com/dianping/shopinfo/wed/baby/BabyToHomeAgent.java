package com.dianping.shopinfo.wed.baby;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaFrameLayout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;

public class BabyToHomeAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  DPObject toHomeObject;
  MApiRequest toHomeRequest;

  public BabyToHomeAgent(Object paramObject)
  {
    super(paramObject);
    sendToHomeRequest();
  }

  void createToHomeView()
  {
    DPObject[] arrayOfDPObject = this.toHomeObject.getArray("ToHomePhotoDoList");
    if ((arrayOfDPObject == null) || (arrayOfDPObject.length == 0))
    {
      removeAllCells();
      return;
    }
    Object localObject2 = this.toHomeObject.getString("Title");
    Object localObject3 = this.toHomeObject.getString("TitlePrmo");
    Object localObject4 = this.toHomeObject.getString("TitleDesc");
    Object localObject1 = this.toHomeObject.getString("TitleLink");
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.baby_tohome_agent, getParentView(), false);
    ((NovaTextView)localView.findViewById(R.id.babytohome_title)).setText((CharSequence)localObject2);
    ((NovaTextView)localView.findViewById(R.id.babytohome_title_promo)).setText((CharSequence)localObject3);
    ((NovaTextView)localView.findViewById(R.id.babytohome_title_desc)).setText((CharSequence)localObject4);
    localObject2 = (NovaRelativeLayout)localView.findViewById(R.id.babytohome_layout_title);
    ((NovaRelativeLayout)localObject2).setGAString("babyservice_title");
    ((NovaRelativeLayout)localObject2).setOnClickListener(this);
    ((NovaRelativeLayout)localObject2).setTag(localObject1);
    int j = (int)((ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 40.0F)) / 3.0F);
    localObject1 = (NovaLinearLayout)localView.findViewById(R.id.babytohome_linearlayout_imageviews);
    localObject2 = new LinearLayout.LayoutParams(-2, -2);
    int i = 0;
    while (i < arrayOfDPObject.length)
    {
      localObject3 = (NovaFrameLayout)LayoutInflater.from(getContext()).inflate(R.layout.baby_tohome_item, getParentView(), false);
      localObject4 = arrayOfDPObject[i];
      NetworkImageView localNetworkImageView = (NetworkImageView)((NovaFrameLayout)localObject3).findViewById(R.id.babytohome_photo_item);
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)localNetworkImageView.getLayoutParams();
      int k = (int)(j * ((DPObject)localObject4).getInt("PicHeight") / ((DPObject)localObject4).getInt("PicWidth") * 1.0F);
      localLayoutParams.width = j;
      localLayoutParams.height = k;
      localNetworkImageView.setImage(((DPObject)localObject4).getString("PicPath"));
      ((NovaTextView)((NovaFrameLayout)localObject3).findViewById(R.id.babytohome_photo_item_text)).setText(((DPObject)localObject4).getString("UnitPrice"));
      ((NovaFrameLayout)localObject3).setOnClickListener(this);
      ((NovaFrameLayout)localObject3).setGAString("babyservice_content", "", i);
      ((NovaFrameLayout)localObject3).setTag(((DPObject)localObject4).getString("PicLink"));
      if (i > 0)
        ((LinearLayout.LayoutParams)localObject2).leftMargin = ViewUtils.dip2px(getContext(), 5.0F);
      ((NovaLinearLayout)localObject1).addView((View)localObject3, (ViewGroup.LayoutParams)localObject2);
      i += 1;
    }
    addCell("", localView);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.toHomeObject == null)
    {
      removeAllCells();
      return;
    }
    createToHomeView();
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.babytohome_layout_title)
    {
      paramView = (String)paramView.getTag();
      if (!TextUtils.isEmpty(paramView))
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramView)));
    }
    do
    {
      do
        return;
      while (!(paramView instanceof NovaFrameLayout));
      paramView = (String)paramView.getTag();
    }
    while (TextUtils.isEmpty(paramView));
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramView)));
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.toHomeRequest)
      this.toHomeRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.toHomeRequest)
    {
      this.toHomeRequest = null;
      this.toHomeObject = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
    }
  }

  void sendToHomeRequest()
  {
    if (this.toHomeRequest != null);
    do
      return;
    while (shopId() <= 0);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/tohomephotolist.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.toHomeRequest = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.toHomeRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.BabyToHomeAgent
 * JD-Core Version:    0.6.0
 */