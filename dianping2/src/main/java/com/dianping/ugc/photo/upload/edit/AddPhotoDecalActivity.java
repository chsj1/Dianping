package com.dianping.ugc.photo.upload.edit;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import java.util.ArrayList;

public class AddPhotoDecalActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final int MINVELOCITY = 0;
  private int horizontalMinDistance;
  private View lastClickView;
  private ArrayList<View> mCategoryViewList = new ArrayList();
  private GestureDetector mGestureDetector;
  private MApiRequest mGetdecalCategoryRequest;
  private StickyGridAdapter mGridAdapter;
  private StickyGridHeadersGridView mGridView;

  private void generateCategoryView(DPObject[] paramArrayOfDPObject)
  {
    LinearLayout localLinearLayout = (LinearLayout)findViewById(R.id.ugc_addpaster_group_view);
    int i = 0;
    while (i < paramArrayOfDPObject.length)
    {
      Object localObject = new LinearLayout.LayoutParams(0, -2);
      ((LinearLayout.LayoutParams)localObject).weight = 1.0F;
      View localView = LayoutInflater.from(this).inflate(R.layout.ugc_photo_add_paster_category_layout, null, false);
      localView.setLayoutParams((ViewGroup.LayoutParams)localObject);
      localObject = (TextView)localView.findViewById(R.id.ugc_photo_paster_category_name);
      ((TextView)localObject).setText(paramArrayOfDPObject[i].getString("CategoryName"));
      localLinearLayout.addView(localView);
      if (i == 0)
      {
        localView.findViewById(R.id.ugc_photo_paster_category_line).setVisibility(0);
        ((TextView)localObject).setTextColor(getResources().getColor(R.color.light_red));
        this.lastClickView = localView;
      }
      localView.setTag(Integer.valueOf(paramArrayOfDPObject[i].getInt("CategoryId")));
      this.mCategoryViewList.add(localView);
      localView.setOnClickListener(this);
      i += 1;
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void initView()
  {
    findViewById(R.id.ugc_photo_editcategory_title).setVisibility(0);
    findViewById(R.id.photo_editcategory_edit_ok).setVisibility(8);
    findViewById(R.id.photo_editcategory_edit_cancel).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        AddPhotoDecalActivity.this.finish();
      }
    });
    findViewById(R.id.photo_editcategory_title_mid).setVisibility(0);
    ((DPNetworkImageView)findViewById(R.id.photo_editcategory_title_mid)).setImageResource(R.drawable.ugc_photo_title_icon_paster);
    this.mGridView = ((StickyGridHeadersGridView)findViewById(R.id.ugc_select_paster_gridview));
    this.mGridView.setAreHeadersSticky(false);
    this.horizontalMinDistance = (getWindowManager().getDefaultDisplay().getWidth() / 5);
  }

  protected boolean needTitleBarShadow()
  {
    return false;
  }

  public void onClick(View paramView)
  {
    int i = Integer.parseInt(paramView.getTag().toString());
    if (i != Integer.parseInt(this.lastClickView.getTag().toString()))
    {
      ((TextView)this.lastClickView.findViewById(R.id.ugc_photo_paster_category_name)).setTextColor(getResources().getColor(R.color.white));
      this.lastClickView.findViewById(R.id.ugc_photo_paster_category_line).setVisibility(8);
      ((TextView)paramView.findViewById(R.id.ugc_photo_paster_category_name)).setTextColor(getResources().getColor(R.color.light_red));
      paramView.findViewById(R.id.ugc_photo_paster_category_line).setVisibility(0);
      this.lastClickView = paramView;
      this.mGridAdapter.setCategoryId(i);
      this.mGridAdapter.reset();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.ugc_photo_addpaster);
    initView();
    senddecalCategoryRequest();
  }

  protected void onDestroy()
  {
    if (this.mGetdecalCategoryRequest != null)
    {
      mapiService().abort(this.mGetdecalCategoryRequest, this, true);
      this.mGetdecalCategoryRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mGetdecalCategoryRequest)
      this.mGetdecalCategoryRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mGetdecalCategoryRequest)
    {
      this.mGetdecalCategoryRequest = null;
      if (paramMApiResponse != null)
        break label18;
    }
    label18: 
    do
    {
      return;
      paramMApiRequest = ((DPObject)paramMApiResponse.result()).getArray("List");
    }
    while ((paramMApiRequest == null) || (paramMApiRequest.length <= 0));
    findViewById(R.id.ugc_addpaster_group_bottom_line).setVisibility(0);
    generateCategoryView(paramMApiRequest);
    this.mGridAdapter = new StickyGridAdapter(this, paramMApiRequest[0].getInt("CategoryId"));
    this.mGridView.setAdapter(this.mGridAdapter);
    setEmptyView();
  }

  public void senddecalCategoryRequest()
  {
    this.mGetdecalCategoryRequest = mapiGet(this, Uri.parse("http://m.api.dianping.com/plaza/getdecalcategory.bin").buildUpon().toString(), CacheType.NORMAL);
    mapiService().exec(this.mGetdecalCategoryRequest, this);
  }

  public void setContentView(int paramInt)
  {
    super.setContentView(paramInt);
    ((FrameLayout)findViewById(16908290)).setBackgroundResource(0);
  }

  public void setEmptyView()
  {
    TextView localTextView = (TextView)findViewById(R.id.ugc_photo_paster_empty_view);
    localTextView.setText("暂时没有贴纸");
    localTextView.setGravity(17);
    localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_20));
    this.mGridView.setEmptyView(localTextView);
  }

  class StickyGridAdapter extends BasicLoadAdapter
    implements StickyGridHeadersSimpleAdapter
  {
    private int categoryId;
    private int gridViewItemHeight;
    private int gridViewItemWidth;

    public StickyGridAdapter(Context paramInt, int arg3)
    {
      super();
      int i;
      this.categoryId = i;
      this.gridViewItemWidth = ((AddPhotoDecalActivity.this.getWindowManager().getDefaultDisplay().getWidth() - ViewUtils.dip2px(paramInt, 15.0F) * 2 - ViewUtils.dip2px(paramInt, 10.0F) * 3) / 4);
      this.gridViewItemHeight = this.gridViewItemWidth;
    }

    protected void appendDataToList(DPObject[] paramArrayOfDPObject)
    {
      if (paramArrayOfDPObject != null)
      {
        int i = 0;
        while (i < paramArrayOfDPObject.length)
        {
          DPObject[] arrayOfDPObject = paramArrayOfDPObject[i].getArray("DecalList");
          if (arrayOfDPObject != null)
          {
            int k = arrayOfDPObject.length;
            int j = 0;
            while (j < k)
            {
              DPObject localDPObject = arrayOfDPObject[j];
              localDPObject = new DPObject().edit().putString("GroupName", paramArrayOfDPObject[i].getString("GroupName")).putInt("showInGridGroupId", paramArrayOfDPObject[i].getInt("GroupId")).putInt("GroupId", localDPObject.getInt("GroupId")).putString("SmallUrl", localDPObject.getString("SmallUrl")).putString("BigUrl", localDPObject.getString("BigUrl")).putInt("Id", localDPObject.getInt("Id")).generate();
              this.mData.add(localDPObject);
              j += 1;
            }
          }
          i += 1;
        }
      }
    }

    public MApiRequest createRequest(int paramInt)
    {
      return decalListTask(this.categoryId, paramInt);
    }

    protected MApiRequest decalListTask(int paramInt1, int paramInt2)
    {
      Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/plaza/getdecallist.bin").buildUpon();
      localBuilder.appendQueryParameter("categoryid", String.valueOf(paramInt1));
      localBuilder.appendQueryParameter("start", String.valueOf(paramInt2));
      localBuilder.appendQueryParameter("limit", "5");
      return AddPhotoDecalActivity.this.mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    }

    public int getCount()
    {
      if ((this.mIsEnd) && (this.mData.size() == 0))
        return 0;
      return super.getCount();
    }

    public long getHeaderId(int paramInt)
    {
      if ((this.mData != null) && (this.mData.size() > 0) && (paramInt < this.mData.size()))
        return ((DPObject)this.mData.get(paramInt)).getInt("showInGridGroupId");
      return 0L;
    }

    public View getHeaderView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = paramView;
      if (paramView == null)
        localView = LayoutInflater.from(AddPhotoDecalActivity.this).inflate(R.layout.ugc_photo_add_paster_headerview, paramViewGroup, false);
      if ((this.mData != null) && (this.mData.size() > 0))
        ((TextView)localView.findViewById(R.id.ugc_photo_paster_group_header_text)).setText(((DPObject)this.mData.get(paramInt)).getString("GroupName"));
      return localView;
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView == null) || (paramView.getId() != R.id.ugc_photo_paster_item_view))
      {
        paramView = LayoutInflater.from(AddPhotoDecalActivity.this).inflate(R.layout.ugc_photo_paster_item, paramViewGroup, false);
        paramView.setLayoutParams(new AbsListView.LayoutParams(this.gridViewItemWidth, this.gridViewItemHeight));
      }
      while (true)
      {
        ((DPNetworkImageView)paramView.findViewById(R.id.ugc_photo_paster_item_image)).setImage(paramDPObject.getString("SmallUrl"));
        paramView.setOnClickListener(new AddPhotoDecalActivity.StickyGridAdapter.1(this, paramDPObject));
        return paramView;
      }
    }

    public void setCategoryId(int paramInt)
    {
      this.categoryId = paramInt;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.upload.edit.AddPhotoDecalActivity
 * JD-Core Version:    0.6.0
 */