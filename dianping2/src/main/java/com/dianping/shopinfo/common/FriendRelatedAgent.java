package com.dianping.shopinfo.common;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.FriendRelatedCell;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;

public class FriendRelatedAgent extends ShopCellAgent
  implements RequestHandler
{
  private static final String CELL_RELATED = "2950friend.relate";
  public static final int SHOP_CELL_WEEK_TIME = 604800000;
  private static final String spIgnoreButtonTimeString = "shopinfo_friends_related_ignore_button_time";
  private static final String spIgnoreTimeString = "shopinfo_friends_related_ignore_time";
  private DPObject friendsInfo;
  private int ignoreButtonTime;
  private MApiRequest request;
  private boolean showFlag;

  public FriendRelatedAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createDishCell(DPObject paramDPObject)
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    Object localObject = (NovaRelativeLayout)localShopinfoCommonCell.findViewById(R.id.title_layout);
    localShopinfoCommonCell.findViewById(R.id.middle_divder_line).setVisibility(8);
    ((NovaRelativeLayout)localObject).setVisibility(8);
    localObject = (FriendRelatedCell)LayoutInflater.from(getContext()).inflate(R.layout.friend_related_cell_in_shopinfo, null, false);
    ((FriendRelatedCell)localObject).setInfo(paramDPObject);
    paramDPObject = (NovaTextView)((FriendRelatedCell)localObject).findViewById(R.id.ignore_tv);
    paramDPObject.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        FriendRelatedAgent.access$002(FriendRelatedAgent.this, false);
        paramView = DPActivity.preferences();
        paramView.edit().putInt("shopinfo_friends_related_ignore_button_time", FriendRelatedAgent.this.ignoreButtonTime + 1).commit();
        paramView.edit().putLong("shopinfo_friends_related_ignore_time", System.currentTimeMillis()).commit();
        FriendRelatedAgent.this.dispatchAgentChanged(false);
      }
    });
    paramDPObject.setGAString("FriendsIgnore");
    localShopinfoCommonCell.addContent((View)localObject, false, null);
    return (View)localShopinfoCommonCell;
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/mshop/friendguide.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(shopId()));
    this.request = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public boolean checkShouldShowAgent()
  {
    SharedPreferences localSharedPreferences = DPActivity.preferences();
    this.ignoreButtonTime = localSharedPreferences.getInt("shopinfo_friends_related_ignore_button_time", 0);
    long l = localSharedPreferences.getLong("shopinfo_friends_related_ignore_time", 0L);
    if (l == 0L);
    do
    {
      return true;
      if (this.ignoreButtonTime >= 3)
        return false;
    }
    while (System.currentTimeMillis() - l >= 604800000L);
    return false;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (!isLogined());
    do
      return;
    while ((this.friendsInfo == null) || (TextUtils.isEmpty(this.friendsInfo.getString("Title"))) || (!this.showFlag));
    addCell("2950friend.relate", createDishCell(this.friendsInfo), "FriendsGuide", 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!isLogined())
      return;
    this.showFlag = checkShouldShowAgent();
    sendRequest();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (this.request == paramRequest)
      this.request = null;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (this.request == paramRequest)
    {
      this.request = null;
      this.friendsInfo = ((DPObject)paramResponse.result());
      if ((this.friendsInfo != null) && (this.showFlag))
        dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.FriendRelatedAgent
 * JD-Core Version:    0.6.0
 */