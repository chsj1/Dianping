package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class EducationPhotoLinkAgent extends ShopCellAgent
{
  private static final String CELL_PHOTO_LINK = "3500Photolink.50CommonLink";
  private static final String TAG = EducationPhotoLinkAgent.class.getSimpleName();
  int shopId;

  public EducationPhotoLinkAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initPhotoLinkCell()
  {
    View localView = this.res.inflate(getContext(), R.layout.shop_education_cell, getParentView(), false);
    ((NovaRelativeLayout)localView).setGAString("viewphoto", getGAExtra());
    ((TextView)localView.findViewById(R.id.cell_text)).setText("查看会员相册");
    localView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
        if (EducationPhotoLinkAgent.this.getShop() != null)
          paramView.putExtra("objShop", EducationPhotoLinkAgent.this.getShop());
        while (true)
        {
          EducationPhotoLinkAgent.this.getFragment().startActivity(paramView);
          return;
          if (EducationPhotoLinkAgent.this.shopId == 0)
            break;
          paramView.putExtra("shopId", EducationPhotoLinkAgent.this.shopId);
        }
        Toast.makeText(EducationPhotoLinkAgent.this.getContext(), "无法取得商户信息，请稍后再试。", 0).show();
      }
    });
    addCell("3500Photolink.50CommonLink", localView, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getShop();
    if (paramBundle == null)
    {
      Log.e(TAG, "Null shop data. Can not update shop info.");
      return;
    }
    this.shopId = paramBundle.getInt("ID");
    if (this.shopId <= 0)
    {
      Log.e(TAG, "Invalid shop id. Can not update shop info.");
      return;
    }
    initPhotoLinkCell();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.EducationPhotoLinkAgent
 * JD-Core Version:    0.6.0
 */