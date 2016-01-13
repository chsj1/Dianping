package com.dianping.main.find.pictureplaza;

import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TableRow;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.CustomGridView;
import com.dianping.base.widget.WrapHeightViewPager;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NavigationDot;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlazaHomeTopicAgent extends PlazaAdapterCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, ViewPager.OnPageChangeListener
{
  private static final String CELL_PLAZA_TOPIC = "0100plaza.02topic";
  private static final int TOPIC_NUM_PER_LINE = 4;
  private static final int TOPIC_NUM_PER_PAGE = 4;
  private final int CLICKRANGE = 5;
  private View cellView;
  private int currentPagePosition = 1;
  private NavigationDot mDotView;
  private SparseArray<CustomGridView> mPagerAdapterItemTags = new SparseArray();
  private List<DPObject> mTopicsList;
  private WrapHeightViewPager mTopicsVPager;
  private MApiRequest pTopicRequest;
  private int realPageNum = 0;
  private TopicPageAdapter tPageAdpater;
  private TopicAdapter topicAdapter;

  public PlazaHomeTopicAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void requestPlazaTopic()
  {
    if (this.pTopicRequest != null)
      getFragment().mapiService().abort(this.pTopicRequest, this, true);
    this.pTopicRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/plaza/getplazatopiclist.bin").buildUpon().build().toString(), CacheType.DISABLED);
    mapiService().exec(this.pTopicRequest, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestPlazaTopic();
    this.topicAdapter = new TopicAdapter(null);
    addCell("0100plaza.02topic", this.topicAdapter);
  }

  public void onDestroy()
  {
    super.onDestroy();
  }

  public void onPageScrollStateChanged(int paramInt)
  {
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
  }

  public void onPageSelected(int paramInt)
  {
    this.currentPagePosition = paramInt;
    if (paramInt == 0)
      this.currentPagePosition = this.realPageNum;
    while (true)
    {
      if (this.currentPagePosition != paramInt)
        this.mTopicsVPager.setCurrentItem(this.currentPagePosition, false);
      this.mDotView.setCurrentIndex(this.currentPagePosition - 1);
      return;
      if (paramInt != this.realPageNum + 1)
        continue;
      this.currentPagePosition = 1;
    }
  }

  protected void onRefresh()
  {
    super.onRefresh();
    requestPlazaTopic();
    onRefreshStart();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != null) && (this.pTopicRequest == paramMApiRequest))
    {
      this.pTopicRequest = null;
      onRefreshComplete();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != null) && (this.pTopicRequest == paramMApiRequest))
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
      {
        this.mTopicsList = Arrays.asList(paramMApiRequest.getArray("List"));
        this.mPagerAdapterItemTags.clear();
        this.topicAdapter.initView();
      }
      this.pTopicRequest = null;
      onRefreshComplete();
    }
  }

  private class TopicAdapter extends BasicAdapter
  {
    private TopicAdapter()
    {
    }

    public int getCount()
    {
      if ((PlazaHomeTopicAgent.this.mTopicsList == null) || (PlazaHomeTopicAgent.this.mTopicsList.size() == 0))
        return 0;
      return 1;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      return 5;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (PlazaHomeTopicAgent.this.cellView == null)
      {
        PlazaHomeTopicAgent.access$202(PlazaHomeTopicAgent.this, PlazaHomeTopicAgent.this.res.inflate(PlazaHomeTopicAgent.this.getContext(), R.layout.plaza_home_topic_layout, paramViewGroup, false));
        PlazaHomeTopicAgent.access$302(PlazaHomeTopicAgent.this, (WrapHeightViewPager)PlazaHomeTopicAgent.this.cellView.findViewById(R.id.topics_slide));
        PlazaHomeTopicAgent.access$402(PlazaHomeTopicAgent.this, (NavigationDot)PlazaHomeTopicAgent.this.cellView.findViewById(R.id.topics_navigation_dots));
        PlazaHomeTopicAgent.this.mTopicsVPager.setOffscreenPageLimit(5);
        PlazaHomeTopicAgent.this.mTopicsVPager.setOnPageChangeListener(PlazaHomeTopicAgent.this);
        PlazaHomeTopicAgent.access$502(PlazaHomeTopicAgent.this, new PlazaHomeTopicAgent.TopicPageAdapter(PlazaHomeTopicAgent.this, null));
        PlazaHomeTopicAgent.this.mTopicsVPager.setAdapter(PlazaHomeTopicAgent.this.tPageAdpater);
        PlazaHomeTopicAgent.this.mPagerAdapterItemTags.clear();
        PlazaHomeTopicAgent.this.mDotView.setDotNormalId(R.drawable.home_serve_dot);
        PlazaHomeTopicAgent.this.mDotView.setDotPressedId(R.drawable.home_serve_dot_pressed);
      }
      initView();
      return PlazaHomeTopicAgent.this.cellView;
    }

    public int getViewTypeCount()
    {
      return 1;
    }

    public void initView()
    {
      if (PlazaHomeTopicAgent.this.tPageAdpater == null)
        return;
      PlazaHomeTopicAgent.this.tPageAdpater.notifyDataSetChanged();
      if (PlazaHomeTopicAgent.this.tPageAdpater.getCount() == 1)
      {
        PlazaHomeTopicAgent.this.mDotView.setVisibility(8);
        return;
      }
      PlazaHomeTopicAgent.this.mDotView.setVisibility(0);
      PlazaHomeTopicAgent.this.mDotView.setTotalDot(PlazaHomeTopicAgent.this.tPageAdpater.getCount() - 2);
      PlazaHomeTopicAgent.this.mDotView.setCurrentIndex(0);
      PlazaHomeTopicAgent.this.mTopicsVPager.setCurrentItem(1, false);
    }
  }

  private class TopicItemAdapter extends BasicAdapter
  {
    PlazaTopicGridViewItem topicItem = null;
    private ArrayList<DPObject> topicObjects;

    private TopicItemAdapter()
    {
    }

    public int getCount()
    {
      if (this.topicObjects != null)
        return this.topicObjects.size();
      return 0;
    }

    public DPObject getItem(int paramInt)
    {
      if ((this.topicObjects != null) && (paramInt < this.topicObjects.size()))
        return (DPObject)this.topicObjects.get(paramInt);
      return null;
    }

    public long getItemId(int paramInt)
    {
      if ((this.topicObjects != null) && (paramInt < this.topicObjects.size()))
        return paramInt;
      return -1L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      this.topicItem = null;
      if (paramInt % 4 == 0)
      {
        paramView = new TableRow(PlazaHomeTopicAgent.this.getContext());
        this.topicItem = ((PlazaTopicGridViewItem)PlazaHomeTopicAgent.this.res.inflate(PlazaHomeTopicAgent.this.getContext(), R.layout.plaza_topic_gridview_item, (TableRow)paramView, false));
        ((TableRow)paramView).addView(this.topicItem);
      }
      while (true)
      {
        paramViewGroup = getItem(paramInt);
        this.topicItem.setTopicInfo(paramViewGroup);
        this.topicItem.setGAString("topic_navi");
        this.topicItem.setOnTouchListener(new View.OnTouchListener(paramViewGroup)
        {
          public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
          {
            paramView = (DPNetworkImageView)paramView.findViewById(16908294);
            int i = (int)paramMotionEvent.getX();
            int j = (int)paramMotionEvent.getY();
            switch (paramMotionEvent.getAction())
            {
            case 2:
            default:
            case 0:
            case 3:
            case 1:
            }
            while (true)
            {
              return true;
              paramView.setColorFilter(-7829368, PorterDuff.Mode.MULTIPLY);
              continue;
              paramView.setColorFilter(null);
              continue;
              paramView.setColorFilter(null);
              int k = (int)paramMotionEvent.getX();
              int m = (int)paramMotionEvent.getY();
              if ((Math.abs(k - i) >= 5) || (Math.abs(m - j) >= 5))
                continue;
              PlazaHomeTopicAgent.this.startActivity(this.val$object.getString("Url"));
              PlazaHomeTopicAgent.TopicItemAdapter.this.topicItem.gaUserInfo.biz_id = String.valueOf(this.val$object.getInt("TopicId"));
              GAHelper.instance().statisticsEvent(PlazaHomeTopicAgent.TopicItemAdapter.this.topicItem, "tap");
            }
          }
        });
        return paramView;
        this.topicItem = ((PlazaTopicGridViewItem)PlazaHomeTopicAgent.this.res.inflate(PlazaHomeTopicAgent.this.getContext(), R.layout.plaza_topic_gridview_item, ((CustomGridView)paramViewGroup).getCurRow(), false));
        paramView = this.topicItem;
      }
    }

    public void setTopicObjects(ArrayList<DPObject> paramArrayList)
    {
      this.topicObjects = paramArrayList;
    }
  }

  private class TopicPageAdapter extends PagerAdapter
  {
    private SparseArray<CustomGridView> gridviewArray = new SparseArray();

    private TopicPageAdapter()
    {
    }

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      paramViewGroup.removeView((View)paramObject);
      PlazaHomeTopicAgent.this.mPagerAdapterItemTags.remove(paramInt);
    }

    public int getCount()
    {
      if ((PlazaHomeTopicAgent.this.mTopicsList == null) || (PlazaHomeTopicAgent.this.mTopicsList.size() <= 4))
      {
        PlazaHomeTopicAgent.access$802(PlazaHomeTopicAgent.this, 1);
        return PlazaHomeTopicAgent.this.realPageNum;
      }
      PlazaHomeTopicAgent.access$802(PlazaHomeTopicAgent.this, (int)Math.ceil(PlazaHomeTopicAgent.this.mTopicsList.size() / 4.0F));
      return PlazaHomeTopicAgent.this.realPageNum + 2;
    }

    public int getItemPosition(Object paramObject)
    {
      int i = 0;
      while (i < PlazaHomeTopicAgent.this.mPagerAdapterItemTags.size())
      {
        if (((View)paramObject).findViewWithTag(PlazaHomeTopicAgent.this.mPagerAdapterItemTags.get(i)) != null)
          return -1;
        i += 1;
      }
      return -2;
    }

    public ArrayList<DPObject> getPageObjects(int paramInt)
    {
      Object localObject = null;
      if (PlazaHomeTopicAgent.this.mTopicsList == null)
        return localObject;
      int i = paramInt;
      if (getCount() != 1)
      {
        if (paramInt != 0)
          break label144;
        i = getCount() - 3;
      }
      while (true)
      {
        i *= 4;
        int j = i + 4 - 1;
        if (j != -1)
        {
          paramInt = j;
          if (j <= PlazaHomeTopicAgent.this.mTopicsList.size() - 1);
        }
        else
        {
          paramInt = PlazaHomeTopicAgent.this.mTopicsList.size() - 1;
        }
        if (i > paramInt)
          break;
        ArrayList localArrayList = new ArrayList();
        while (true)
        {
          localObject = localArrayList;
          if (i > paramInt)
            break;
          localArrayList.add(PlazaHomeTopicAgent.this.mTopicsList.get(i));
          i += 1;
        }
        label144: if (paramInt == getCount() - 1)
        {
          i = 0;
          continue;
        }
        i = paramInt - 1;
      }
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      CustomGridView localCustomGridView;
      if (this.gridviewArray.get(paramInt) == null)
      {
        localCustomGridView = (CustomGridView)PlazaHomeTopicAgent.this.res.inflate(PlazaHomeTopicAgent.this.getContext(), R.layout.plaza_topic_gridview, paramViewGroup, false);
        localCustomGridView.setNeedHideDivider(true);
        localCustomGridView.setStretchAllColumns(true);
        localCustomGridView.setFocusable(false);
        localCustomGridView.setAdapter(new PlazaHomeTopicAgent.TopicItemAdapter(PlazaHomeTopicAgent.this, null));
        this.gridviewArray.put(paramInt, localCustomGridView);
      }
      while (true)
      {
        PlazaHomeTopicAgent.TopicItemAdapter localTopicItemAdapter = (PlazaHomeTopicAgent.TopicItemAdapter)localCustomGridView.getAdapter();
        localTopicItemAdapter.setTopicObjects(getPageObjects(paramInt));
        localTopicItemAdapter.notifyDataSetChanged();
        paramViewGroup.addView(localCustomGridView);
        localCustomGridView.setTag(localCustomGridView);
        PlazaHomeTopicAgent.this.mPagerAdapterItemTags.put(paramInt, localCustomGridView);
        return localCustomGridView;
        localCustomGridView = (CustomGridView)this.gridviewArray.get(paramInt);
      }
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaHomeTopicAgent
 * JD-Core Version:    0.6.0
 */