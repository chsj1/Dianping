package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import com.dianping.app.LabelIndicatorStrategy;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaFrameLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;

public class SchoolFlowersAgent extends ShopCellAgent
  implements RequestHandler
{
  private static final String CELL_SCHOOL_FLOWER = "0350Flower.01";
  private DPObject schoolFlowerInfo;
  private MApiRequest schoolFlowerReq;

  public SchoolFlowersAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createFlowerTabCell(DPObject paramDPObject)
  {
    View localView1 = this.res.inflate(getContext(), R.layout.shopinfo_edu_flower, getParentView(), false);
    TabHost localTabHost = (TabHost)localView1.findViewById(R.id.xiaohuaTH);
    localTabHost.setup();
    int j = paramDPObject.getInt("Count");
    paramDPObject = paramDPObject.getArray("BeautyPeopleModuleList");
    int i = 0;
    if (i < j)
    {
      View localView2 = paramDPObject[i];
      String str1 = localView2.getString("Title");
      Object localObject2 = localView2.getArray("BeautyPeopleList");
      int k = localView2.getInt("Count");
      Object localObject1 = localView2.getString("BeautyRankTitle");
      String str2 = localView2.getString("RankDescription");
      String str3 = localView2.getString("RankUrl");
      int m = localView2.getInt("PeopleModuleType");
      TextView localTextView1;
      TextView localTextView2;
      NovaRelativeLayout localNovaRelativeLayout;
      if (i == 0)
      {
        localView2 = this.res.inflate(getContext(), R.layout.shopinfo_edu_flower_item, getParentView(), false);
        fillItem(localView2, localObject2, k, m);
        localObject2 = (LinearLayout)localView1.findViewById(R.id.view1);
        localTextView1 = (TextView)((LinearLayout)localObject2).findViewById(R.id.beauty_rank_title);
        localTextView2 = (TextView)((LinearLayout)localObject2).findViewById(R.id.rank_description);
        localNovaRelativeLayout = (NovaRelativeLayout)((LinearLayout)localObject2).findViewById(R.id.rank_urlRL);
        localNovaRelativeLayout.setGAString("edu_schoolbeauty_more", m + "");
        localTextView1.setText((CharSequence)localObject1);
        localTextView2.setText(str2);
        localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(str3)
        {
          public void onClick(View paramView)
          {
            if (!TextUtils.isEmpty(this.val$rankUrl))
            {
              paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$rankUrl));
              SchoolFlowersAgent.this.getFragment().startActivity(paramView);
            }
          }
        });
        ((LinearLayout)localObject2).addView(localView2, 0);
        ((LinearLayout)localObject2).setVisibility(0);
        localObject1 = new LabelIndicatorStrategy(getContext(), str1, R.layout.school_flower_tab_indicator).createIndicatorView(localTabHost);
        ((NovaTextView)localObject1).setGAString("edu_schoolbeauty_tab", m + "");
        localTabHost.addTab(localTabHost.newTabSpec(str1).setIndicator((View)localObject1).setContent(R.id.view1));
      }
      label606: 
      do
        while (true)
        {
          i += 1;
          break;
          if (i != 1)
            break label606;
          localView2 = this.res.inflate(getContext(), R.layout.shopinfo_edu_flower_item, getParentView(), false);
          fillItem(localView2, localObject2, k, m);
          localObject2 = (LinearLayout)localView1.findViewById(R.id.view2);
          localTextView1 = (TextView)((LinearLayout)localObject2).findViewById(R.id.beauty_rank_title2);
          localTextView2 = (TextView)((LinearLayout)localObject2).findViewById(R.id.rank_description2);
          localNovaRelativeLayout = (NovaRelativeLayout)((LinearLayout)localObject2).findViewById(R.id.rank_urlRL2);
          localNovaRelativeLayout.setGAString("edu_schoolbeauty_more", m + "");
          localTextView1.setText((CharSequence)localObject1);
          localTextView2.setText(str2);
          localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(str3)
          {
            public void onClick(View paramView)
            {
              if (!TextUtils.isEmpty(this.val$rankUrl))
              {
                paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$rankUrl));
                SchoolFlowersAgent.this.getFragment().startActivity(paramView);
              }
            }
          });
          ((LinearLayout)localObject2).addView(localView2, 0);
          ((LinearLayout)localObject2).setVisibility(0);
          localObject1 = new LabelIndicatorStrategy(getContext(), str1, R.layout.school_flower_tab_indicator).createIndicatorView(localTabHost);
          ((NovaTextView)localObject1).setGAString("edu_schoolbeauty_tab", m + "");
          localTabHost.addTab(localTabHost.newTabSpec(str1).setIndicator((View)localObject1).setContent(R.id.view2));
        }
      while (i <= 1);
    }
    if (j == 1)
    {
      localTabHost.getTabWidget().getChildAt(0).setSelected(false);
      localTabHost.getTabWidget().getChildAt(0).setClickable(false);
      ((TextView)localTabHost.getTabWidget().getChildAt(0).findViewById(16908310)).setGravity(16);
    }
    return (View)(View)localView1;
  }

  private void fillItem(View paramView, DPObject[] paramArrayOfDPObject, int paramInt1, int paramInt2)
  {
    NetworkImageView localNetworkImageView1 = (NetworkImageView)paramView.findViewById(R.id.firstflowerpic);
    NetworkImageView localNetworkImageView2 = (NetworkImageView)paramView.findViewById(R.id.secondflowerpic);
    NetworkImageView localNetworkImageView3 = (NetworkImageView)paramView.findViewById(R.id.thirdflowerpic);
    ImageView localImageView1 = (ImageView)paramView.findViewById(R.id.firstflowerpic_cover_image);
    ImageView localImageView2 = (ImageView)paramView.findViewById(R.id.secondflowerpic_cover_image);
    ImageView localImageView3 = (ImageView)paramView.findViewById(R.id.thirdflowerpic_cover_image);
    NetworkImageView localNetworkImageView4 = (NetworkImageView)paramView.findViewById(R.id.firstflowerorder);
    NetworkImageView localNetworkImageView5 = (NetworkImageView)paramView.findViewById(R.id.secondflowerorder);
    NetworkImageView localNetworkImageView6 = (NetworkImageView)paramView.findViewById(R.id.thirdflowerorder);
    TextView localTextView1 = (TextView)paramView.findViewById(R.id.firstflowername);
    TextView localTextView2 = (TextView)paramView.findViewById(R.id.secondflowername);
    TextView localTextView3 = (TextView)paramView.findViewById(R.id.thirdflowername);
    LinearLayout localLinearLayout1 = (LinearLayout)paramView.findViewById(R.id.firstflowerheartcountLL);
    LinearLayout localLinearLayout2 = (LinearLayout)paramView.findViewById(R.id.secondflowerheartcountLL);
    LinearLayout localLinearLayout3 = (LinearLayout)paramView.findViewById(R.id.thirdflowerheartcountLL);
    NovaFrameLayout localNovaFrameLayout1 = (NovaFrameLayout)paramView.findViewById(R.id.firstflowerFL);
    NovaFrameLayout localNovaFrameLayout2 = (NovaFrameLayout)paramView.findViewById(R.id.secondflowerFL);
    NovaFrameLayout localNovaFrameLayout3 = (NovaFrameLayout)paramView.findViewById(R.id.thirdflowerFL);
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length <= 0))
      return;
    int i = ViewUtils.getScreenWidthPixels(getContext());
    int j = (int)(i * 0.6D);
    paramView = new FrameLayout.LayoutParams(j, j);
    paramView.rightMargin = (i * 8 / 640);
    localNetworkImageView1.setLayoutParams(paramView);
    paramView = new FrameLayout.LayoutParams(j, -2);
    paramView.gravity = 80;
    localImageView1.setLayoutParams(paramView);
    j = i * 188 / 640;
    paramView = new FrameLayout.LayoutParams(j, j);
    paramView.bottomMargin = (i * 8 / 640);
    localNetworkImageView2.setLayoutParams(paramView);
    paramView = new FrameLayout.LayoutParams(j, -2);
    paramView.gravity = 80;
    paramView.bottomMargin = (i * 8 / 640);
    localImageView2.setLayoutParams(paramView);
    localNetworkImageView3.setLayoutParams(new FrameLayout.LayoutParams(j, j));
    paramView = new FrameLayout.LayoutParams(j, -2);
    paramView.gravity = 80;
    localImageView3.setLayoutParams(paramView);
    i = 0;
    label423: Object localObject;
    String str;
    3 local3;
    if (i < paramInt1)
    {
      localObject = paramArrayOfDPObject[i];
      str = ((DPObject)localObject).getString("PicUrl");
      j = ((DPObject)localObject).getInt("PraiseCount");
      paramView = ((DPObject)localObject).getString("Name");
      local3 = new View.OnClickListener(((DPObject)localObject).getString("DetailLink"))
      {
        public void onClick(View paramView)
        {
          if (!TextUtils.isEmpty(this.val$detailLink))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$detailLink));
            SchoolFlowersAgent.this.getFragment().startActivity(paramView);
          }
        }
      };
      if (i != 0)
        break label663;
      if (j >= 0)
        break label564;
      localNetworkImageView1.setImage(str);
      localNetworkImageView4.setVisibility(8);
      localNovaFrameLayout1.setGAString("edu_schoolbeauty_add", paramInt2 + "", 0);
      localLinearLayout1.setVisibility(8);
      label542: localNovaFrameLayout1.setVisibility(0);
      localNovaFrameLayout1.setOnClickListener(local3);
    }
    label564: 
    do
    {
      i += 1;
      break label423;
      break;
      localNetworkImageView1.setImage(str);
      localImageView1.setVisibility(0);
      localObject = paramView;
      if (paramView != null)
      {
        localObject = paramView;
        if (paramView.length() >= 12)
          localObject = paramView.substring(0, 11) + "...";
      }
      localTextView1.setText((CharSequence)localObject);
      localNovaFrameLayout1.setGAString("edu_schoolbeauty", paramInt2 + "", 0);
      break label542;
      if (i == 1)
      {
        if (j < 0)
        {
          localNovaFrameLayout2.setGAString("edu_schoolbeauty_add", paramInt2 + "", 1);
          localNetworkImageView2.setImage(str);
          localNetworkImageView5.setVisibility(8);
          localLinearLayout2.setVisibility(8);
        }
        while (true)
        {
          localNovaFrameLayout2.setVisibility(0);
          localNovaFrameLayout2.setOnClickListener(local3);
          break;
          localNovaFrameLayout2.setGAString("edu_schoolbeauty", paramInt2 + "", 1);
          localObject = paramView;
          if (paramView != null)
          {
            localObject = paramView;
            if (paramView.length() >= 6)
              localObject = paramView.substring(0, 5) + "...";
          }
          localNetworkImageView2.setImage(str);
          localImageView2.setVisibility(0);
          localTextView2.setText((CharSequence)localObject);
        }
      }
      if (i != 2)
        continue;
      if (j < 0)
      {
        localNovaFrameLayout3.setGAString("edu_schoolbeauty_add", paramInt2 + "", 2);
        localNetworkImageView3.setImage(str);
        localNetworkImageView6.setVisibility(8);
        localLinearLayout3.setVisibility(8);
      }
      while (true)
      {
        localNovaFrameLayout3.setVisibility(0);
        localNovaFrameLayout3.setOnClickListener(local3);
        break;
        localNetworkImageView3.setImage(str);
        localImageView3.setVisibility(0);
        localObject = paramView;
        if (paramView != null)
        {
          localObject = paramView;
          if (paramView.length() >= 6)
            localObject = paramView.substring(0, 5) + "...";
        }
        localTextView3.setText((CharSequence)localObject);
        localNovaFrameLayout3.setGAString("edu_schoolbeauty", paramInt2 + "", 2);
      }
    }
    while (i <= 2);
    label663:
  }

  private void sendRequset()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/edu/schoolbeautyinfo.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.schoolFlowerReq = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.schoolFlowerReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if ((this.schoolFlowerInfo == null) || (this.schoolFlowerInfo.getInt("Count") <= 0) || (this.schoolFlowerInfo.getArray("BeautyPeopleModuleList") == null) || (this.schoolFlowerInfo.getArray("BeautyPeopleModuleList").length <= 0) || (this.schoolFlowerInfo.getArray("BeautyPeopleModuleList").length != this.schoolFlowerInfo.getInt("Count")))
      return;
    try
    {
      addCell("0350Flower.01", createFlowerTabCell(this.schoolFlowerInfo));
      return;
    }
    catch (java.lang.Exception paramBundle)
    {
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequset();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (this.schoolFlowerReq == paramRequest)
      this.schoolFlowerReq = null;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.schoolFlowerReq)
    {
      this.schoolFlowerReq = null;
      this.schoolFlowerInfo = ((DPObject)paramResponse.result());
      if (this.schoolFlowerInfo != null)
        dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.SchoolFlowersAgent
 * JD-Core Version:    0.6.0
 */