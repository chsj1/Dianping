package com.dianping.main.find;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AdapterCellAgent.BasicCellAgentAdapter;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.TableHeader;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.UserProfileItem;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Arrays;

public class FindAroundAgent extends AdapterCellAgent
  implements CityConfig.SwitchListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String FIND_AROUND_AGENT_FIRST_SHOW = "FindAroundAgentHasShow";
  private static final String FIND_AROUND_AGENT_FORUM_FIRST_SHOW = "ForumHasShow";
  private static final String FIND_AROUND_AGENT_PLAZA_FIRST_SHOW = "PicPlazaHasShow";
  private static final String FIND_AROUND_TAG = "10findaround";
  private Adapter adapter;
  private ArrayList<ArrayList<DPObject>> aroundList = new ArrayList();
  private View cell;
  DPObject error;
  private MApiRequest hotRequest;
  private final LoadingErrorView.LoadRetry retryListener = new LoadingErrorView.LoadRetry()
  {
    public void loadRetry(View paramView)
    {
      FindAroundAgent.this.error = null;
      FindAroundAgent.this.reqHot();
      FindAroundAgent.this.dispatchAgentChanged(false);
    }
  };

  public FindAroundAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createHotView()
  {
    TableView localTableView = new TableView(getContext());
    Object localObject = new AbsListView.LayoutParams(-1, -2);
    localTableView.setBackgroundColor(getResources().getColor(R.color.common_bk_color));
    localTableView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    int i = 0;
    while (i < this.aroundList.size())
    {
      localObject = new TableHeader(getContext());
      ((TableHeader)localObject).setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 10.0F)));
      localTableView.addView((View)localObject);
      ArrayList localArrayList = (ArrayList)this.aroundList.get(i);
      int j = 0;
      while (j < localArrayList.size())
      {
        DPObject localDPObject2 = (DPObject)localArrayList.get(j);
        if (localDPObject2 == null)
        {
          j += 1;
          continue;
        }
        UserProfileItem localUserProfileItem = new UserProfileItem(getContext());
        localUserProfileItem.setGAString("explore_entrance", localDPObject2.getString("Title"));
        localUserProfileItem.setLayoutParams(new LinearLayout.LayoutParams(-1, getResources().getDimensionPixelSize(R.dimen.single_line_height)));
        localUserProfileItem.setGravity(16);
        localUserProfileItem.setPadding(ViewUtils.dip2px(getContext(), 19.0F), 0, ViewUtils.dip2px(getContext(), 15.0F), 0);
        localUserProfileItem.findViewById(R.id.item_title_text).setPadding(ViewUtils.dip2px(getContext(), 7.0F), 0, 0, 0);
        SharedPreferences localSharedPreferences = DPActivity.preferences(getContext());
        boolean bool1 = localSharedPreferences.getBoolean("FindAroundAgentHasShow", false);
        boolean bool2 = localSharedPreferences.getBoolean("ForumHasShow", false);
        boolean bool3 = localSharedPreferences.getBoolean("PicPlazaHasShow", true);
        String str = localDPObject2.getString("Title");
        DPObject localDPObject1 = (DPObject)localArrayList.get(j);
        if (((!"社区论坛".equals(str)) || (!bool2)) && (((!"看排行".equals(str)) && (!"排行榜".equals(str))) || (!bool1)))
        {
          localObject = localDPObject1;
          if ("图趣".equals(str))
          {
            localObject = localDPObject1;
            if (!bool3);
          }
        }
        else
        {
          localObject = localDPObject1.edit().putString("SubTitle", "").generate();
        }
        localUserProfileItem.setObject((DPObject)localObject);
        localTableView.addView(localUserProfileItem);
        if (("看排行".equals(str)) || ("排行榜".equals(str)))
          localUserProfileItem.setIconNewVisibility(bool1);
        while (true)
        {
          localUserProfileItem.setOnClickListener(new View.OnClickListener(localDPObject2, str, localSharedPreferences, localUserProfileItem)
          {
            public void onClick(View paramView)
            {
              paramView = this.val$itemObject.getString("Url");
              if (("看排行".equals(this.val$title)) || ("排行榜".equals(this.val$title)))
              {
                this.val$sharedPreferences.edit().putBoolean("FindAroundAgentHasShow", false).commit();
                this.val$item.setIconNewVisibility(false);
                this.val$item.setObject(this.val$itemObject);
              }
              while (true)
              {
                if (!TextUtils.isEmpty(paramView))
                {
                  paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
                  FindAroundAgent.this.getFragment().startActivity(paramView);
                }
                return;
                if ("社区论坛".equals(this.val$title))
                {
                  this.val$sharedPreferences.edit().putBoolean("ForumHasShow", false).commit();
                  this.val$item.setIconNewVisibility(false);
                  this.val$item.setObject(this.val$itemObject);
                  continue;
                }
                if (!"图趣".equals(this.val$title))
                  continue;
                this.val$sharedPreferences.edit().putBoolean("PicPlazaHasShow", false).apply();
                this.val$item.setIconNewVisibility(false);
                this.val$item.setObject(this.val$itemObject);
              }
            }
          });
          break;
          if ("社区论坛".equals(str))
          {
            localUserProfileItem.setIconNewVisibility(bool2);
            continue;
          }
          localUserProfileItem.setIconNewVisibility(false);
        }
      }
      i += 1;
    }
    return (View)localTableView;
  }

  private MApiRequest createReq()
  {
    String str1 = "";
    String str2 = "";
    Location localLocation = location();
    if (localLocation != null)
    {
      str1 = localLocation.latitude() + "";
      str2 = localLocation.longitude() + "";
    }
    return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/discovery/getdiscovermodule.bin").buildUpon().appendQueryParameter("mid", "HOTITEMS").appendQueryParameter("cityid", String.valueOf(cityId())).appendQueryParameter("lat", str1).appendQueryParameter("lng", str2).build().toString(), CacheType.NORMAL);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.cell = null;
    this.adapter.setHotItems(this.aroundList);
    this.adapter.notifyDataSetChanged();
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    if (paramCity1.id() != paramCity2.id())
    {
      int i = 0;
      while (i < this.aroundList.size())
      {
        ((ArrayList)this.aroundList.get(i)).clear();
        i += 1;
      }
      this.aroundList.clear();
      this.error = null;
      reqHot();
      dispatchAgentChanged(false);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getFragment().cityConfig().addListener(this);
    reqHot();
    this.adapter = new Adapter(null);
    addCell("10findaround", this.adapter);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.hotRequest != null)
    {
      getFragment().mapiService().abort(this.hotRequest, this, true);
      this.hotRequest = null;
    }
    getFragment().cityConfig().removeListener(this);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.hotRequest)
      if (!(paramMApiResponse.error() instanceof DPObject))
        break label44;
    label44: for (this.error = ((DPObject)paramMApiResponse.error()); ; this.error = new DPObject())
    {
      this.hotRequest = null;
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.hotRequest)
    {
      this.hotRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        if (paramMApiRequest.getArray("ItemLists") != null)
        {
          int j = paramMApiRequest.getArray("ItemLists").length;
          int i = 0;
          while (i < j)
          {
            paramMApiResponse = new ArrayList(Arrays.asList(paramMApiRequest.getArray("ItemLists")[i].getArray("Items")));
            this.aroundList.add(paramMApiResponse);
            i += 1;
          }
        }
        dispatchAgentChanged(false);
      }
    }
  }

  void reqHot()
  {
    if (getFragment() == null)
      return;
    if (this.hotRequest != null)
      getFragment().mapiService().abort(this.hotRequest, this, true);
    this.hotRequest = createReq();
    getFragment().mapiService().exec(this.hotRequest, this);
  }

  private class Adapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    private ArrayList<ArrayList<DPObject>> mAroundList;

    private Adapter()
    {
      super();
    }

    private void setHotItems(ArrayList<ArrayList<DPObject>> paramArrayList)
    {
      this.mAroundList = paramArrayList;
    }

    public int getCount()
    {
      if (((FindAroundAgent.this.hotRequest != null) && (this.mAroundList.size() == 0) && (FindAroundAgent.this.error == null)) || ((this.mAroundList.size() == 0) && (FindAroundAgent.this.error != null)) || (this.mAroundList.size() > 0))
        return 1;
      return 0;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((this.mAroundList == null) || (this.mAroundList.size() == 0))
      {
        NovaLinearLayout localNovaLinearLayout = new NovaLinearLayout(FindAroundAgent.this.getContext());
        Object localObject = new AbsListView.LayoutParams(-1, -2);
        localNovaLinearLayout.setOrientation(1);
        localNovaLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject);
        localNovaLinearLayout.addView(FindAroundAgent.this.createDividerBlock());
        localObject = null;
        if (FindAroundAgent.this.hotRequest != null)
          localObject = getLoadingView(paramViewGroup, paramView, ViewUtils.dip2px(FindAroundAgent.this.getContext(), 202.0F));
        while (true)
        {
          localNovaLinearLayout.addView((View)localObject);
          return localNovaLinearLayout;
          if (FindAroundAgent.this.error == null)
            continue;
          localObject = getFailedView("网络连接失败 点击重新加载", FindAroundAgent.this.retryListener, paramViewGroup, paramView, ViewUtils.dip2px(FindAroundAgent.this.getContext(), 202.0F));
        }
      }
      if (FindAroundAgent.this.cell == null)
        FindAroundAgent.access$502(FindAroundAgent.this, FindAroundAgent.this.createHotView());
      return (View)FindAroundAgent.this.cell;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.FindAroundAgent
 * JD-Core Version:    0.6.0
 */