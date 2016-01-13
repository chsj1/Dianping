package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MeasuredListView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;

public class ExploreCategoryItem extends NovaLinearLayout
{
  private int categoryId;
  private String categoryName;
  private NovaTextView categoryShowAllView;
  private NovaTextView categoryTitleView;
  private ImageView newImageView;
  private NovaRelativeLayout titleView;
  private MeasuredListView topicListView;

  public ExploreCategoryItem(Context paramContext)
  {
    super(paramContext);
  }

  public ExploreCategoryItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ExploreCategoryItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public int getCategoryId()
  {
    return this.categoryId;
  }

  public String getCategoryName()
  {
    return this.categoryName;
  }

  public NovaRelativeLayout getTitleView()
  {
    return this.titleView;
  }

  public MeasuredListView getTopicListView()
  {
    return this.topicListView;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.categoryTitleView = ((NovaTextView)findViewById(R.id.explore_category_title));
    this.categoryShowAllView = ((NovaTextView)findViewById(R.id.explore_category_show_all));
    this.topicListView = ((MeasuredListView)findViewById(R.id.explore_category_listview));
    this.newImageView = ((ImageView)findViewById(R.id.explore_category_new));
    this.titleView = ((NovaRelativeLayout)findViewById(R.id.explore_category_layout));
  }

  public void setCategoryInfo(DPObject paramDPObject)
  {
    this.categoryId = paramDPObject.getInt("CategoryId");
    this.categoryName = paramDPObject.getString("CategoryName");
    if (this.categoryTitleView != null)
      this.categoryTitleView.setText(this.categoryName);
    if (this.categoryShowAllView != null)
      this.categoryShowAllView.setText("查看全部");
  }

  public void setNewImageViewVisibility(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.newImageView.setVisibility(0);
      return;
    }
    this.newImageView.setVisibility(8);
  }

  public void setTitleOnClickListener(View.OnClickListener paramOnClickListener)
  {
    if ((this.titleView != null) && (paramOnClickListener != null))
      this.titleView.setOnClickListener(paramOnClickListener);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreCategoryItem
 * JD-Core Version:    0.6.0
 */