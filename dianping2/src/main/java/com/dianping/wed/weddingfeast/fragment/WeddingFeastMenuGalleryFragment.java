package com.dianping.wed.weddingfeast.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.HashMap;
import java.util.Map;

public class WeddingFeastMenuGalleryFragment extends NovaFragment
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  private static int WEDDING_REQ_STATUS_DONE;
  private static int WEDDING_REQ_STATUS_ERROR;
  private static int WEDDING_REQ_STATUS_INIT = 0;
  private static int WEDDING_REQ_STATUS_LOADING = 1;
  private static String screenInfo;
  private int albumFrameHeight;
  private int albumFrameWidth;
  TextView desc;
  private String[] desc_text;
  private String[] dishA;
  private String[] dishB;
  private String emptyMsg;
  private String errorMsg;
  View fragmentView = null;
  private boolean isEnd = false;
  private boolean isTaskRunning = false;
  private int mWeddingReqStatus = WEDDING_REQ_STATUS_INIT;
  private DPObject menu;
  private int menuId;
  private MApiRequest menuRequest;
  private Map<Integer, DPObject> menus = new HashMap();
  private int nextStartIndex = 0;
  View noDish;
  View oneDish;
  private int screenHeight;
  private int screenWidth;
  private int shopId;
  View twoDish;

  static
  {
    WEDDING_REQ_STATUS_DONE = 2;
    WEDDING_REQ_STATUS_ERROR = 3;
  }

  private void getMenuInfo()
  {
    if (this.menus.get(Integer.valueOf(this.menuId)) != null)
    {
      this.menu = ((DPObject)this.menus.get(Integer.valueOf(this.menuId)));
      updateMenuView();
      return;
    }
    if (this.menuRequest == null)
    {
      StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/wedding/getweddinghotelmenu.bin");
      localStringBuilder.append("?menuid=").append(this.menuId);
      this.menuRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    }
    mapiService().exec(this.menuRequest, this);
    this.mWeddingReqStatus = WEDDING_REQ_STATUS_LOADING;
  }

  private void setOneDish(String[] paramArrayOfString)
  {
    this.oneDish.setVisibility(0);
    this.twoDish.setVisibility(8);
    LinearLayout localLinearLayout = (LinearLayout)this.fragmentView.findViewById(R.id.dish_text);
    Object localObject = localLinearLayout.getLayoutParams();
    ((ViewGroup.LayoutParams)localObject).width = (this.screenWidth - 40);
    localLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject);
    localLinearLayout.removeAllViews();
    int i = 0;
    while (i < paramArrayOfString.length)
    {
      if (!TextUtils.isEmpty(paramArrayOfString[i]))
      {
        localObject = new TextView(getActivity());
        ((TextView)localObject).setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        ((TextView)localObject).setBackgroundResource(R.color.white);
        ((TextView)localObject).setSingleLine(true);
        ((TextView)localObject).setEllipsize(TextUtils.TruncateAt.END);
        ((TextView)localObject).setLineSpacing(10.0F, 1.0F);
        ((TextView)localObject).setTextSize(2, 15.0F);
        ((TextView)localObject).setText(paramArrayOfString[i]);
        localLinearLayout.addView((View)localObject);
      }
      i += 1;
    }
  }

  private void updateMenuView()
  {
    Object localObject1;
    int i;
    if ((this.desc_text != null) && (this.desc_text.length > 0))
    {
      localObject1 = "";
      i = 0;
      while (i < this.desc_text.length)
      {
        localObject1 = (String)localObject1 + this.desc_text[i] + "    ";
        i += 1;
      }
      this.desc.setText((CharSequence)localObject1);
    }
    Object localObject2;
    ViewGroup.LayoutParams localLayoutParams;
    while ((this.dishB != null) && (this.dishB.length > 0) && (this.dishA != null) && (this.dishA.length > 0))
    {
      this.twoDish.setVisibility(0);
      this.oneDish.setVisibility(8);
      localObject2 = (LinearLayout)this.fragmentView.findViewById(R.id.dishA_text);
      localObject1 = (LinearLayout)this.fragmentView.findViewById(R.id.dishB_text);
      localLayoutParams = ((LinearLayout)localObject2).getLayoutParams();
      localLayoutParams.width = (this.screenWidth / 2 - 1);
      ((LinearLayout)localObject2).removeAllViews();
      ((LinearLayout)localObject1).removeAllViews();
      i = 0;
      while (true)
        if (i < this.dishA.length)
        {
          if (!TextUtils.isEmpty(this.dishA[i]))
          {
            TextView localTextView = new TextView(getActivity());
            localTextView.setLayoutParams(localLayoutParams);
            localTextView.setGravity(17);
            localTextView.setBackgroundResource(R.color.white);
            localTextView.setSingleLine(true);
            localTextView.setEllipsize(TextUtils.TruncateAt.END);
            localTextView.setLineSpacing(10.0F, 1.0F);
            localTextView.setPadding(10, 0, 10, 0);
            localTextView.setTextSize(2, 15.0F);
            localTextView.setText(this.dishA[i]);
            ((LinearLayout)localObject2).addView(localTextView);
          }
          i += 1;
          continue;
          this.desc.setVisibility(8);
          break;
        }
      i = 0;
    }
    while (i < this.dishB.length)
    {
      if (!TextUtils.isEmpty(this.dishB[i]))
      {
        localObject2 = new TextView(getActivity());
        ((TextView)localObject2).setLayoutParams(localLayoutParams);
        ((TextView)localObject2).setGravity(17);
        ((TextView)localObject2).setBackgroundResource(R.color.white);
        ((TextView)localObject2).setSingleLine(true);
        ((TextView)localObject2).setEllipsize(TextUtils.TruncateAt.END);
        ((TextView)localObject2).setLineSpacing(10.0F, 1.0F);
        ((TextView)localObject2).setPadding(10, 0, 10, 0);
        ((TextView)localObject2).setTextSize(2, 15.0F);
        ((TextView)localObject2).setText(this.dishB[i]);
        ((LinearLayout)localObject1).addView((View)localObject2);
      }
      i += 1;
      continue;
      if ((this.dishA == null) || (this.dishA.length <= 0))
        break label459;
      setOneDish(this.dishA);
    }
    return;
    label459: if ((this.dishB != null) && (this.dishB.length > 0))
    {
      setOneDish(this.dishB);
      return;
    }
    this.oneDish.setVisibility(8);
    this.twoDish.setVisibility(8);
    this.noDish.setVisibility(0);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (TextUtils.isEmpty(screenInfo))
    {
      DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
      screenInfo = "screenwidth=" + localDisplayMetrics.widthPixels + "&screenheight=" + localDisplayMetrics.heightPixels + "&screendensity=" + localDisplayMetrics.density;
    }
    if (paramBundle != null)
      this.shopId = paramBundle.getInt("shopId");
    for (this.menuId = paramBundle.getInt("menuId"); ; this.menuId = paramBundle.getInt("MenuID"))
    {
      this.screenWidth = ViewUtils.getScreenWidthPixels(getActivity());
      this.screenHeight = ViewUtils.getScreenHeightPixels(getActivity());
      this.albumFrameWidth = (this.screenWidth * 45 / 100);
      this.albumFrameHeight = (this.albumFrameWidth * 300 / 280);
      return;
      paramBundle = getArguments();
      this.shopId = paramBundle.getInt("shopId");
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.fragmentView = paramLayoutInflater.inflate(R.layout.wedding_feast_menu_gallery, paramViewGroup, false);
    this.desc = ((TextView)this.fragmentView.findViewById(R.id.desc_text));
    this.noDish = this.fragmentView.findViewById(R.id.no_dish_view);
    this.oneDish = this.fragmentView.findViewById(R.id.one_dish_view);
    this.twoDish = this.fragmentView.findViewById(R.id.two_dish_view);
    getMenuInfo();
    return this.fragmentView;
  }

  public void onDetach()
  {
    super.onDetach();
    if (this.menuRequest != null)
      mapiService().abort(this.menuRequest, null, true);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.oneDish.setVisibility(8);
    this.twoDish.setVisibility(8);
    this.noDish.setVisibility(0);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.isTaskRunning = false;
    this.menu = ((DPObject)paramMApiResponse.result());
    this.desc_text = this.menu.getStringArray("Desc");
    this.dishA = this.menu.getStringArray("DishA");
    this.dishB = this.menu.getStringArray("DishB");
    this.menus.put(Integer.valueOf(this.menuId), this.menu);
    updateMenuView();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("shopId", this.shopId);
    paramBundle.putInt("menuId", this.menuId);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.weddingfeast.fragment.WeddingFeastMenuGalleryFragment
 * JD-Core Version:    0.6.0
 */