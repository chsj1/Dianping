package com.dianping.ugc.review.list.ui;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.ugc.review.ReviewShop;
import com.dianping.base.ugc.review.fragment.ReviewSearchFragment;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import java.util.ArrayList;

public class ReviewListActivity extends NovaActivity
  implements ReviewShop, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int REQUEST_CODE_BRANCHES = 100;
  private static final String SCHEME_HOTEL = "hotelreview";
  private static final String SCHEME_SHOP = "review";
  private static final String TAG = "ReviewListActivity";
  private static final String TYPE_DEAL = "1";
  private static final String TYPE_SHOP = "0";
  private boolean enableSearch = true;
  private MApiRequest getBranchesRequest;
  private int[] mBranchIds;
  private String[] mBranchNames;
  private ArrayList<DPObject> mBranches = new ArrayList();
  private String mDealId;
  private int mFilterId;
  private int mHotelLabelId;
  private String mKeyword;
  private ReviewListFragment mListView;
  private String mSelectedReviewId;
  private int mShopId;
  private String mShopName;
  private int mTabIndex = 0;
  private String mType = "0";

  private void initViews(Bundle paramBundle)
  {
    setContentView(R.layout.ugc_reviewlist_layout);
    if ("0".equals(this.mType))
      if ("hotelreview".equals(getIntent().getData().getHost()))
      {
        setTitle(R.string.ugc_review);
        if (this.enableSearch)
          getTitleBar().addRightViewItem("search", R.drawable.navibar_icon_search, new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              ReviewListActivity.this.hideTitleBar();
              ReviewListActivity.this.statisticsEvent("shopinfo5", "shopinfo5_review_search", "", 0);
              ReviewSearchFragment.newInstance(ReviewListActivity.this).setOnSearchFragmentListener(new ReviewListActivity.2.1(this));
            }
          });
        label72: if (paramBundle != null)
          break label284;
        this.mListView = new ReviewListFragment();
        paramBundle = getSupportFragmentManager().beginTransaction();
        paramBundle.add(R.id.reviewlist_content, this.mListView, "ReviewListFragment");
        paramBundle.commit();
      }
    while (true)
    {
      this.mListView.setShopId(this.mShopId);
      this.mListView.setDealId(this.mDealId);
      this.mListView.setSelectedReviewId(this.mSelectedReviewId);
      this.mListView.setKeyword(this.mKeyword);
      this.mListView.setFilterId(this.mFilterId);
      if (this.mFilterId == 900)
        this.mListView.setKeyword("好友点评");
      this.mListView.setHotelLabelId(this.mHotelLabelId);
      return;
      ShopListTabView localShopListTabView = (ShopListTabView)LayoutInflater.from(this).inflate(R.layout.shoplist_tab_layout, null);
      localShopListTabView.setTabChangeListener(new ShopListTabView.TabChangeListener()
      {
        public void onTabChanged(int paramInt)
        {
          if (paramInt == 0)
          {
            ReviewListActivity.this.mListView.setFilterId(800);
            ReviewListActivity.this.mListView.setKeyword(null);
            ReviewListActivity.this.mListView.setNeedFilter(true);
            ReviewListActivity.this.mListView.reset();
          }
          do
            return;
          while (paramInt != 1);
          ReviewListActivity.this.mListView.setFilterId(1000);
          ReviewListActivity.this.mListView.setKeyword(null);
          ReviewListActivity.this.mListView.setNeedFilter(false);
          ReviewListActivity.this.mListView.reset();
        }
      });
      localShopListTabView.setLeftTitleText(getString(R.string.ugc_review));
      localShopListTabView.setRightTitleText(getString(R.string.ugc_review_with_photo));
      localShopListTabView.setCurIndex(this.mTabIndex);
      getTitleBar().setCustomContentView(localShopListTabView);
      break;
      if (!"1".equals(this.mType))
        break label72;
      break label72;
      label284: this.mListView = ((ReviewListFragment)getSupportFragmentManager().findFragmentByTag("ReviewListFragment"));
    }
  }

  private void processParams(Bundle paramBundle)
  {
    this.mType = getStringParam("type");
    Object localObject;
    if (this.mType != null)
      localObject = this.mType;
    while (true)
    {
      this.mType = ((String)localObject);
      this.mShopName = getStringParam("shopname");
      this.mDealId = getStringParam("dealid");
      localObject = getStringParam("id");
      String str = getStringParam("bestshopid");
      if (str != null);
      try
      {
        for (i = Integer.parseInt(str); ; i = Integer.parseInt((String)localObject))
        {
          this.mShopId = i;
          this.mSelectedReviewId = getStringParam("selectedreviewid");
          this.mFilterId = getIntParam("tagtype", -1);
          this.mKeyword = getStringParam("selecttagname");
          if (this.mFilterId == -1)
          {
            if (!"review".equals(getIntent().getData().getHost()))
              break label311;
            i = 800;
            this.mFilterId = getIntParam("filterid", i);
          }
          this.mHotelLabelId = getIntParam("labelid");
          localObject = (DPObject)getIntent().getParcelableExtra("shop");
          if ((localObject != null) && (((DPObject)localObject).getBoolean("IsForeignShop")))
            this.enableSearch = false;
          Log.d("ReviewListActivity", "type=" + this.mType + " shop name=" + this.mShopName + " shop id=" + this.mShopId + " deal id=" + this.mDealId + " filter id=" + this.mFilterId);
          if (paramBundle != null)
            break label317;
          return;
          localObject = "0";
          break;
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        while (true)
        {
          localNumberFormatException.printStackTrace();
          continue;
          label311: int i = 0;
        }
        label317: this.mTabIndex = paramBundle.getInt("tabindex");
        this.mBranchIds = paramBundle.getIntArray("branchids");
        this.mBranchNames = paramBundle.getStringArray("branchnames");
        this.mBranches = paramBundle.getParcelableArrayList("branches");
      }
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 100) && (paramInt2 == -1))
    {
      paramInt2 = paramIntent.getIntExtra("selectShopId", this.mShopId);
      if (paramInt2 != this.mShopId)
      {
        this.mShopId = paramInt2;
        paramInt1 = 0;
      }
    }
    while (true)
    {
      if (paramInt1 < this.mBranchIds.length)
      {
        if (this.mBranchIds[paramInt1] == paramInt2)
          getTitleBar().setTitle(this.mBranchNames[paramInt1]);
      }
      else
      {
        this.mListView.setShopId(this.mShopId);
        this.mListView.setNeedFilter(true);
        this.mListView.reset();
        return;
      }
      paramInt1 += 1;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    processParams(paramBundle);
    initViews(paramBundle);
    if ("1".equals(this.mType))
    {
      paramBundle = Uri.parse("http://app.t.dianping.com/reviewlistgn.bin").buildUpon();
      paramBundle.appendQueryParameter("dealid", this.mDealId);
      if (location() != null)
      {
        paramBundle.appendQueryParameter("lat", String.valueOf(location().latitude()));
        paramBundle.appendQueryParameter("lng", String.valueOf(location().longitude()));
      }
      this.getBranchesRequest = BasicMApiRequest.mapiGet(paramBundle.build().toString(), CacheType.DISABLED);
      mapiService().exec(this.getBranchesRequest, this);
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.getBranchesRequest != null)
    {
      mapiService().abort(this.getBranchesRequest, this, false);
      this.getBranchesRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getBranchesRequest)
      this.getBranchesRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getBranchesRequest)
    {
      this.getBranchesRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        this.mBranchIds = paramMApiRequest.getIntArray("IdList");
        this.mBranchNames = paramMApiRequest.getStringArray("NameList");
        if ((this.mBranchIds != null) && (this.mBranchNames != null))
          i = 0;
      }
    }
    while (true)
    {
      if (i < this.mBranchIds.length)
      {
        if (this.mShopId == this.mBranchIds[i])
          getTitleBar().setTitle(this.mBranchNames[i]);
      }
      else
      {
        if (this.mBranchIds.length != 1)
          break;
        getTitleBar().removeAllRightViewItem();
        return;
      }
      i += 1;
    }
    int i = 0;
    while (i < this.mBranchNames.length)
    {
      this.mBranches.add(new DPObject("Shop").edit().putInt("id", this.mBranchIds[i]).putString("name", this.mBranchNames[i]).generate());
      i += 1;
    }
    getTitleBar().addRightViewItem(getString(R.string.ugc_change_branch), "branch", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://reviewshop"));
        Bundle localBundle = new Bundle();
        localBundle.putInt("selectShopId", ReviewListActivity.this.shopId());
        localBundle.putParcelableArrayList("shopList", ReviewListActivity.this.mBranches);
        paramView.putExtras(localBundle);
        ReviewListActivity.this.startActivityForResult(paramView, 100);
      }
    });
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("tabindex", this.mTabIndex);
    paramBundle.putIntArray("branchids", this.mBranchIds);
    paramBundle.putStringArray("branchnames", this.mBranchNames);
    paramBundle.putParcelableArrayList("branches", this.mBranches);
  }

  public DPObject shop()
  {
    return null;
  }

  public int shopId()
  {
    return this.mShopId;
  }

  public String shopName()
  {
    return this.mShopName;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.list.ui.ReviewListActivity
 * JD-Core Version:    0.6.0
 */