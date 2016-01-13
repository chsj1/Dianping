package com.dianping.search.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.widget.CustomGridView;
import com.dianping.base.widget.CustomGridView.OnItemClickListener;
import com.dianping.search.shoplist.data.model.SearchRecAdGuideModel;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;

public class SearchRecAdGuideItem extends CustomGridView
  implements CustomGridView.OnItemClickListener
{
  private static final int ROW_NUMBER = 4;
  private BaseShopListDataSource mDataSource;

  public SearchRecAdGuideItem(Context paramContext)
  {
    super(paramContext);
    initView();
  }

  public SearchRecAdGuideItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }

  private void initView()
  {
    setAdapter(new Adapter());
    setOnItemClickListener(this);
    setNeedHideDivider(true);
  }

  public void onItemClick(CustomGridView paramCustomGridView, View paramView, int paramInt, long paramLong)
  {
    paramCustomGridView = (String)paramView.getTag();
    if (!TextUtils.isEmpty(paramCustomGridView))
    {
      paramView = Uri.parse("dianping://shoplist").buildUpon();
      paramView.appendQueryParameter("tab", String.valueOf(0));
      paramView.appendQueryParameter("keyword", paramCustomGridView);
      paramCustomGridView = new Intent("android.intent.action.VIEW", Uri.parse(paramView.toString()));
      getContext().startActivity(paramCustomGridView);
    }
  }

  public void setData(SearchRecAdGuideModel paramSearchRecAdGuideModel, BaseShopListDataSource paramBaseShopListDataSource)
  {
    this.mDataSource = paramBaseShopListDataSource;
    ((Adapter)getAdapter()).setData(paramSearchRecAdGuideModel);
  }

  public class Adapter extends BaseAdapter
  {
    private static final int MAX_TEXT_LENGTH = 5;
    private String[] guideKeywords;
    private int textViewWidth = (ViewUtils.getScreenWidthPixels(SearchRecAdGuideItem.this.getContext()) - ViewUtils.dip2px(SearchRecAdGuideItem.this.getContext(), 18.0F)) / 4 - ViewUtils.dip2px(SearchRecAdGuideItem.this.getContext(), 6.0F);

    public Adapter()
    {
    }

    public int getCount()
    {
      return this.guideKeywords.length;
    }

    public Object getItem(int paramInt)
    {
      return this.guideKeywords[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = (String)getItem(paramInt);
      if (paramInt % 4 == 0)
      {
        localObject = new TableRow(paramViewGroup.getContext());
        localNovaTextView = (NovaTextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_rec_ad_guide_word_view, (TableRow)localObject, false);
        localNovaTextView.setTag(paramView);
        if (paramView.length() > 5);
        for (paramViewGroup = paramView.substring(0, 5); ; paramViewGroup = paramView)
        {
          localNovaTextView.setText(paramViewGroup);
          paramViewGroup = (TableRow.LayoutParams)localNovaTextView.getLayoutParams();
          paramViewGroup.width = this.textViewWidth;
          localNovaTextView.setLayoutParams(paramViewGroup);
          localNovaTextView.setGAString("ad_guidewords");
          localNovaTextView.gaUserInfo.index = Integer.valueOf(paramInt);
          localNovaTextView.gaUserInfo.keyword = paramView;
          ((TableRow)localObject).addView(localNovaTextView);
          return localObject;
        }
      }
      Object localObject = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_rec_ad_guide_word_view, ((CustomGridView)paramViewGroup).getCurRow(), false);
      ((View)localObject).setTag(paramView);
      NovaTextView localNovaTextView = (NovaTextView)localObject;
      if (paramView.length() > 5);
      for (paramViewGroup = paramView.substring(0, 5); ; paramViewGroup = paramView)
      {
        localNovaTextView.setText(paramViewGroup);
        paramViewGroup = (TableRow.LayoutParams)((View)localObject).getLayoutParams();
        paramViewGroup.width = this.textViewWidth;
        ((View)localObject).setLayoutParams(paramViewGroup);
        ((NovaTextView)localObject).setGAString("ad_guidewords");
        ((NovaTextView)localObject).gaUserInfo.index = Integer.valueOf(paramInt);
        ((NovaTextView)localObject).gaUserInfo.keyword = paramView;
        return localObject;
      }
    }

    public void setData(SearchRecAdGuideModel paramSearchRecAdGuideModel)
    {
      this.guideKeywords = paramSearchRecAdGuideModel.guideKeywords;
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.SearchRecAdGuideItem
 * JD-Core Version:    0.6.0
 */