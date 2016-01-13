package com.dianping.main.favorite;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.adapter.MulDeleListAdapter;
import com.dianping.base.app.NovaTabBaseFragment;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.DealListItem;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.CollectionUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class FavoriteTuanFragment extends NovaTabBaseFragment
  implements AdapterView.OnItemClickListener
{
  Adapter deallistAdapter = new Adapter(null);
  MApiRequest delFavRequest;
  private TextView emptyTV;
  MApiRequest favoriteDealsRequest;
  private View mContentView;
  private ViewGroup mEmptyView;
  View mFailedView;
  private ListView mListView;
  private View mLoadingView;
  int mUserId = 0;
  ArrayList<String> removedids = new ArrayList();

  private View showFailedView(String paramString, boolean paramBoolean, LoadingErrorView.LoadRetry paramLoadRetry)
  {
    int j = 0;
    Object localObject = this.mEmptyView;
    if (paramBoolean)
    {
      i = 0;
      ((ViewGroup)localObject).setVisibility(i);
      localObject = this.mContentView;
      if (!paramBoolean)
        break label93;
    }
    label93: for (int i = 8; ; i = 0)
    {
      ((View)localObject).setVisibility(i);
      int k = this.mEmptyView.getChildCount();
      i = 0;
      while (i < k)
      {
        this.mEmptyView.getChildAt(i).setVisibility(8);
        i += 1;
      }
      i = 8;
      break;
    }
    if (this.mFailedView == null)
    {
      this.mFailedView = getActivity().getLayoutInflater().inflate(R.layout.error_item, null, false);
      this.mEmptyView.addView(this.mFailedView);
    }
    ((TextView)this.mFailedView.findViewById(16908308)).setText(paramString);
    ((LoadingErrorView)this.mFailedView).setCallBack(paramLoadRetry);
    paramString = this.mFailedView;
    if (paramBoolean);
    for (i = j; ; i = 8)
    {
      paramString.setVisibility(i);
      return this.mFailedView;
    }
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    ((FavoriteBaseActivity)getActivity()).setActivityEditable(false, this);
    if (paramBundle != null)
    {
      this.removedids = paramBundle.getStringArrayList("removedids");
      this.mUserId = paramBundle.getInt("userid");
    }
    while (true)
    {
      this.mListView.setAdapter(this.deallistAdapter);
      if (paramBundle != null)
        this.deallistAdapter.onRestoreInstanceState(paramBundle);
      if (this.deallistAdapter != null)
      {
        this.deallistAdapter.loadNewPage();
        showLoadingView(true);
      }
      return;
      Object localObject = getActivity().getIntent();
      if (((Intent)localObject).getData() != null)
      {
        localObject = ((Intent)localObject).getData().getQueryParameter("userid");
        try
        {
          this.mUserId = Integer.parseInt((String)localObject);
        }
        catch (NumberFormatException localNumberFormatException)
        {
          this.mUserId = 0;
        }
        continue;
      }
      this.mUserId = localNumberFormatException.getIntExtra("userId", 0);
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.favorite_shop_layout, paramViewGroup, false);
    this.mEmptyView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.empty));
    this.mContentView = paramLayoutInflater.findViewById(R.id.content);
    this.mListView = ((ListView)paramLayoutInflater.findViewById(R.id.list));
    this.mListView.setOnItemClickListener(this);
    paramLayoutInflater.findViewById(R.id.filterBar).setVisibility(8);
    return paramLayoutInflater;
  }

  public void onDeleteButtonClicked()
  {
    Object localObject = this.deallistAdapter.getDeleteList();
    this.deallistAdapter.deleteDeals((ArrayList)localObject);
    localObject = ((ArrayList)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      DPObject localDPObject = (DPObject)((Iterator)localObject).next();
      this.removedids.add(String.valueOf(localDPObject.getInt("ID")));
    }
    this.deallistAdapter.delFavoriteDeals(this.removedids);
    ((FavoriteBaseActivity)getActivity()).setActivityIsEdit(false);
    if ((this.deallistAdapter.shownDeals == null) || (this.deallistAdapter.shownDeals.isEmpty()))
    {
      if (this.mUserId > 0);
      for (localObject = "TA还没收藏过团购，真可惜……"; ; localObject = "您还没有收藏过团购哦")
      {
        showEmptyView((String)localObject);
        return;
      }
    }
    ((FavoriteBaseActivity)getActivity()).setActivityEditable(true, this);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.favoriteDealsRequest != null)
    {
      mapiService().abort(this.favoriteDealsRequest, this.deallistAdapter.handler, true);
      this.favoriteDealsRequest = null;
    }
    if (this.delFavRequest != null)
    {
      mapiService().abort(this.delFavRequest, this.deallistAdapter.handler, true);
      this.delFavRequest = null;
    }
  }

  public void onEditModeChanged(boolean paramBoolean)
  {
    if (this.deallistAdapter != null)
    {
      ((FavoriteBaseActivity)getActivity()).setButtonView(this.deallistAdapter.getCheckedSize());
      this.deallistAdapter.resetCheckList();
      this.deallistAdapter.setIsEdit(paramBoolean);
    }
  }

  public void onImageSwitchChanged()
  {
    if (this.deallistAdapter != null)
      this.deallistAdapter.notifyDataSetChanged();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.deallistAdapter.getItem(paramInt);
    if (((paramAdapterView instanceof DPObject)) && (((DPObject)paramAdapterView).isClass("Deal")))
    {
      if (((FavoriteBaseActivity)getActivity()).isEdit())
      {
        this.deallistAdapter.itemBeChecked(paramInt);
        ((FavoriteBaseActivity)getActivity()).setButtonView(this.deallistAdapter.getCheckedSize());
      }
    }
    else
      return;
    paramAdapterView = (DPObject)paramAdapterView;
    if (!this.deallistAdapter.isEdit())
    {
      if (paramAdapterView.getInt("DealType") == 5)
      {
        paramAdapterView = paramAdapterView.getString("Link");
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + URLEncoder.encode(paramAdapterView))));
      }
      while (true)
      {
        statisticsEvent("tuan5", "tuan5_myfavo_item", "", 0);
        return;
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
        paramView.putExtra("deal", paramAdapterView);
        startActivity(paramView);
      }
    }
    this.deallistAdapter.notifyDataSetChanged();
  }

  public void onRefreshComplete(boolean paramBoolean)
  {
    showLoadingView(false);
    if (!paramBoolean)
    {
      this.mFailedView = showFailedView("网络不给力哦，请稍后再试", true, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          FavoriteTuanFragment.this.mFailedView.setVisibility(8);
          FavoriteTuanFragment.this.deallistAdapter.loadNewPage();
        }
      });
      return;
    }
    if (this.deallistAdapter.shownDeals.size() == 0)
    {
      if (this.mUserId > 0);
      for (String str = "TA还没收藏过团购，真可惜……"; ; str = "您还没有收藏过团购哦")
      {
        showEmptyView(str);
        return;
      }
    }
    this.mContentView.setVisibility(0);
    this.mEmptyView.setVisibility(8);
    this.mListView.setAdapter(this.deallistAdapter);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    this.deallistAdapter.onSaveInstanceState(paramBundle);
    paramBundle.putStringArrayList("removedids", this.removedids);
    paramBundle.putInt("userid", this.mUserId);
    super.onSaveInstanceState(paramBundle);
  }

  void showEmptyView(String paramString)
  {
    this.mEmptyView.setVisibility(0);
    this.mContentView.setVisibility(8);
    int j = this.mEmptyView.getChildCount();
    int i = 0;
    while (i < j)
    {
      this.mEmptyView.getChildAt(i).setVisibility(8);
      i += 1;
    }
    if (this.emptyTV == null)
    {
      this.emptyTV = ((TextView)getActivity().getLayoutInflater().inflate(R.layout.simple_list_item_18, null, false));
      this.emptyTV.setMovementMethod(LinkMovementMethod.getInstance());
      Drawable localDrawable = getResources().getDrawable(R.drawable.empty_page_nothing);
      localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
      this.emptyTV.setCompoundDrawablePadding(8);
      this.emptyTV.setCompoundDrawables(localDrawable, null, null, null);
      this.mEmptyView.addView(this.emptyTV);
    }
    if (!TextUtils.isEmpty(paramString))
      this.emptyTV.setText(Html.fromHtml(paramString));
    this.emptyTV.setVisibility(0);
    ((FavoriteBaseActivity)getActivity()).setActivityEditable(false, this);
  }

  void showLoadingView(boolean paramBoolean)
  {
    int j = 0;
    Object localObject = this.mEmptyView;
    if (paramBoolean)
    {
      i = 0;
      ((ViewGroup)localObject).setVisibility(i);
      localObject = this.mContentView;
      if (!paramBoolean)
        break label79;
    }
    label79: for (int i = 8; ; i = 0)
    {
      ((View)localObject).setVisibility(i);
      int k = this.mEmptyView.getChildCount();
      i = 0;
      while (i < k)
      {
        this.mEmptyView.getChildAt(i).setVisibility(8);
        i += 1;
      }
      i = 8;
      break;
    }
    if (this.mLoadingView == null)
    {
      this.mLoadingView = getActivity().getLayoutInflater().inflate(R.layout.loading_item_fullscreen, null, false);
      this.mEmptyView.addView(this.mLoadingView);
    }
    localObject = this.mLoadingView;
    if (paramBoolean);
    for (i = j; ; i = 8)
    {
      ((View)localObject).setVisibility(i);
      return;
    }
  }

  private class Adapter extends MulDeleListAdapter
  {
    private static final String CHECKED_SIZE_KEY = "checkedSize";
    private static final String CHECK_LIST_KEY = "checkList";
    private static final String DEALS_KEY = "deals";
    private static final String EMPTY_MESSAGE_KEY = "emptyMsg";
    private static final String ERROR_MESSAGE_KEY = "errorMsg";
    private static final String IS_END_KEY = "isEnd";
    private static final String NEXT_START_INDEX_KEY = "nextStartIndex";
    private static final String REMOVED_IDS_KEY = "removedIds";
    private ArrayList<DPObject> deals = new ArrayList();
    private String emptyMsg;
    private String errorMsg;
    private RequestHandler<MApiRequest, MApiResponse> handler = new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (paramMApiRequest == FavoriteTuanFragment.this.favoriteDealsRequest)
        {
          FavoriteTuanFragment.this.favoriteDealsRequest = null;
          FavoriteTuanFragment.Adapter.this.setError(paramMApiResponse.message().toString());
          if (FavoriteTuanFragment.this.deallistAdapter.getDataList().isEmpty())
            FavoriteTuanFragment.this.onRefreshComplete(false);
        }
        do
        {
          return;
          FavoriteTuanFragment.this.onRefreshComplete(true);
          return;
        }
        while (paramMApiRequest != FavoriteTuanFragment.this.delFavRequest);
        FavoriteTuanFragment.this.delFavRequest = null;
        FavoriteTuanFragment.Adapter.this.setError(paramMApiResponse.message().toString());
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (paramMApiRequest == FavoriteTuanFragment.this.favoriteDealsRequest)
        {
          FavoriteTuanFragment.this.favoriteDealsRequest = null;
          FavoriteTuanFragment.this.showLoadingView(false);
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest.isClass("DealList"))
          {
            if ((paramMApiRequest == null) || (paramMApiRequest.getArray("List") == null) || (paramMApiRequest.getArray("List").length <= 0))
              break label111;
            FavoriteTuanFragment.Adapter.this.appendDeals(paramMApiRequest);
            ((FavoriteBaseActivity)FavoriteTuanFragment.this.getActivity()).setActivityEditable(true, FavoriteTuanFragment.this);
          }
        }
        label111: 
        do
        {
          return;
          paramMApiResponse = FavoriteTuanFragment.this;
          if (FavoriteTuanFragment.this.mUserId > 0);
          for (paramMApiRequest = "TA还没收藏过团购，真可惜……"; ; paramMApiRequest = "您还没有收藏过团购哦")
          {
            paramMApiResponse.showEmptyView(paramMApiRequest);
            return;
          }
        }
        while (paramMApiRequest != FavoriteTuanFragment.this.delFavRequest);
        try
        {
          FragmentActivity localFragmentActivity = FavoriteTuanFragment.this.getActivity();
          if (((DPObject)paramMApiResponse.result()).getInt("Flag") == 0);
          for (paramMApiRequest = "删除成功"; ; paramMApiRequest = "删除失败")
          {
            Toast.makeText(localFragmentActivity, paramMApiRequest, 0).show();
            FavoriteTuanFragment.this.delFavRequest = null;
            FavoriteTuanFragment.this.removedids.clear();
            return;
          }
        }
        catch (java.lang.Exception paramMApiRequest)
        {
        }
      }

      public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
      {
      }

      public void onRequestStart(MApiRequest paramMApiRequest)
      {
      }
    };
    private boolean isEnd;
    private int nextStartIndex;
    ArrayList<DPObject> shownDeals = this.dataList;

    private Adapter()
    {
    }

    public void appendDeals(DPObject paramDPObject)
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
      if (arrayOfDPObject != null)
      {
        this.emptyMsg = paramDPObject.getString("EmptyMsg");
        int i = arrayOfDPObject.length;
        this.nextStartIndex += i;
        this.isEnd = paramDPObject.getBoolean("IsEnd");
        this.deals.addAll(Arrays.asList(arrayOfDPObject));
        refreshShownDealList(true, false);
        appendCheckList(arrayOfDPObject.length);
        notifyDataSetChanged();
      }
    }

    public boolean areAllItemsEnabled()
    {
      return true;
    }

    void dealList(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://app.t.dianping.com/");
      localStringBuilder.append("favoritedeallistgn.bin");
      localStringBuilder.append("?token=").append(FavoriteTuanFragment.this.accountService().token());
      if (this.dataList.isEmpty())
        localStringBuilder.append("&start=").append(0);
      while (true)
      {
        if (FavoriteTuanFragment.this.location() != null)
        {
          localStringBuilder.append("&lat=").append(FavoriteTuanFragment.this.location().latitude());
          localStringBuilder.append("&lng=").append(FavoriteTuanFragment.this.location().longitude());
        }
        FavoriteTuanFragment.this.favoriteDealsRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
        FavoriteTuanFragment.this.mapiService().exec(FavoriteTuanFragment.this.favoriteDealsRequest, this.handler);
        return;
        localStringBuilder.append("&start=").append(FavoriteTuanFragment.this.deallistAdapter.getCount());
      }
    }

    void delFavoriteDeals(ArrayList<String> paramArrayList)
    {
      if (FavoriteTuanFragment.this.delFavRequest != null)
        FavoriteTuanFragment.this.mapiService().abort(FavoriteTuanFragment.this.delFavRequest, null, true);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://app.t.dianping.com/");
      localStringBuilder.append("delfavoritedealgn.bin?");
      FavoriteTuanFragment.this.delFavRequest = BasicMApiRequest.mapiPost(localStringBuilder.toString(), new String[] { "token", FavoriteTuanFragment.this.accountService().token(), "groupid", CollectionUtils.list2Str(paramArrayList, ",") + "" });
      FavoriteTuanFragment.this.mapiService().exec(FavoriteTuanFragment.this.delFavRequest, this.handler);
    }

    public void deleteDeals(ArrayList<DPObject> paramArrayList)
    {
      Iterator localIterator1 = paramArrayList.iterator();
      while (localIterator1.hasNext())
      {
        DPObject localDPObject = (DPObject)localIterator1.next();
        Object localObject = null;
        Iterator localIterator2 = this.shownDeals.iterator();
        do
        {
          paramArrayList = localObject;
          if (!localIterator2.hasNext())
            break;
          paramArrayList = (DPObject)localIterator2.next();
        }
        while (paramArrayList.getInt("ID") != localDPObject.getInt("ID"));
        if (paramArrayList == null)
          continue;
        this.shownDeals.remove(paramArrayList);
        this.deals.remove(paramArrayList);
      }
      resetCheckList();
      notifyDataSetChanged();
    }

    public int getCount()
    {
      if (this.isEnd)
        return this.shownDeals.size();
      return this.shownDeals.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.shownDeals.size())
        return this.shownDeals.get(paramInt);
      if (this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if (((localObject instanceof DPObject)) && (((DPObject)localObject).isClass("Deal")))
        return ((DPObject)localObject).getInt("ID");
      if (localObject == LOADING)
        return -paramInt;
      return -2147483648L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      boolean bool = true;
      Object localObject = getItem(paramInt);
      if (((localObject instanceof DPObject)) && (((DPObject)localObject).isClass("Deal")))
      {
        DPObject localDPObject = (DPObject)localObject;
        paramInt = getChecked(paramInt);
        if ((paramView instanceof DealListItem))
        {
          paramView = (DealListItem)paramView;
          localObject = paramView;
          if (paramView == null)
            localObject = (DealListItem)FavoriteTuanFragment.this.getActivity().getLayoutInflater().inflate(R.layout.deal_list_item, paramViewGroup, false);
          double d1 = 0.0D;
          double d2 = 0.0D;
          if (FavoriteTuanFragment.this.location() != null)
          {
            d1 = FavoriteTuanFragment.this.location().latitude();
            d2 = FavoriteTuanFragment.this.location().longitude();
          }
          ((DealListItem)localObject).setDeal(localDPObject, d1, d2, NovaConfigUtils.isShowImageInMobileNetwork(), 1);
          ((DealListItem)localObject).setEditable(this.isEdit);
          if (paramInt <= 0)
            break label170;
        }
        while (true)
        {
          ((DealListItem)localObject).setChecked(bool);
          return localObject;
          paramView = null;
          break;
          label170: bool = false;
        }
      }
      if (localObject == LOADING)
      {
        if (this.errorMsg == null)
          loadNewPage();
        return getLoadingView(paramViewGroup, paramView);
      }
      return (View)getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          FavoriteTuanFragment.Adapter.this.loadNewPage();
        }
      }
      , paramViewGroup, paramView);
    }

    public int getViewTypeCount()
    {
      return 1;
    }

    public boolean hasStableIds()
    {
      return true;
    }

    boolean isDeleted(String paramString)
    {
      return FavoriteTuanFragment.this.removedids.contains(paramString);
    }

    public boolean loadNewPage()
    {
      if (this.isEnd);
      do
        return false;
      while (FavoriteTuanFragment.this.favoriteDealsRequest != null);
      this.errorMsg = null;
      dealList(this.nextStartIndex);
      return true;
    }

    public void onRestoreInstanceState(Bundle paramBundle)
    {
      this.deals = paramBundle.getParcelableArrayList("deals");
      this.isEnd = paramBundle.getBoolean("isEnd");
      this.errorMsg = paramBundle.getString("errorMsg");
      this.emptyMsg = paramBundle.getString("emptyMsg");
      this.nextStartIndex = paramBundle.getInt("nextStartIndex");
      this.checkedSize = paramBundle.getInt("checkedSize");
      this.checkList = paramBundle.getIntegerArrayList("checkList");
      FavoriteTuanFragment.this.removedids = paramBundle.getStringArrayList("removedIds");
      refreshShownDealList(false, true);
      notifyDataSetChanged();
    }

    public void onSaveInstanceState(Bundle paramBundle)
    {
      paramBundle.putParcelableArrayList("deals", this.deals);
      paramBundle.putBoolean("isEnd", this.isEnd);
      paramBundle.putString("errorMsg", this.errorMsg);
      paramBundle.putString("emptyMsg", this.emptyMsg);
      paramBundle.putInt("nextStartIndex", this.nextStartIndex);
      paramBundle.putInt("checkedSize", this.checkedSize);
      paramBundle.putIntegerArrayList("checkList", this.checkList);
      paramBundle.putStringArrayList("removedIds", FavoriteTuanFragment.this.removedids);
    }

    void refreshShownDealList(boolean paramBoolean1, boolean paramBoolean2)
    {
      this.shownDeals.clear();
      Iterator localIterator = this.deals.iterator();
      while (localIterator.hasNext())
      {
        DPObject localDPObject = (DPObject)localIterator.next();
        if (isDeleted(String.valueOf(localDPObject.getInt("ID"))))
          continue;
        this.shownDeals.add(localDPObject);
      }
      if (paramBoolean2)
        resetCheckList();
      if (paramBoolean1)
        notifyDataSetChanged();
    }

    public void setError(String paramString)
    {
      this.errorMsg = paramString;
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.favorite.FavoriteTuanFragment
 * JD-Core Version:    0.6.0
 */