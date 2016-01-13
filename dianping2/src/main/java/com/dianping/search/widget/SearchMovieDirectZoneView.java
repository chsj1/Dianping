package com.dianping.search.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.dianping.search.shoplist.data.model.SearchDirectZoneModel;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;

public class SearchMovieDirectZoneView extends NovaLinearLayout
{
  private ArrayList<SearchDirectZoneModel> dpMovieOnInfoList = new ArrayList();
  private LinearLayout layerMovieGuide;

  public SearchMovieDirectZoneView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SearchMovieDirectZoneView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.layerMovieGuide = ((LinearLayout)findViewById(R.id.layer_movieguide));
  }

  public boolean sameData(ArrayList<SearchDirectZoneModel> paramArrayList)
  {
    if (paramArrayList.size() != this.dpMovieOnInfoList.size())
      return false;
    int i = 0;
    while (true)
    {
      if (i >= paramArrayList.size())
        break label200;
      SearchDirectZoneModel localSearchDirectZoneModel1 = (SearchDirectZoneModel)this.dpMovieOnInfoList.get(i);
      SearchDirectZoneModel localSearchDirectZoneModel2 = (SearchDirectZoneModel)paramArrayList.get(i);
      if (((localSearchDirectZoneModel1.title != null) && (localSearchDirectZoneModel2.title != null) && (!localSearchDirectZoneModel1.title.equals(localSearchDirectZoneModel2.title))) || ((localSearchDirectZoneModel1.picLabel != null) && (localSearchDirectZoneModel2.picLabel != null) && (!localSearchDirectZoneModel1.picLabel.equals(localSearchDirectZoneModel2.picLabel))) || ((localSearchDirectZoneModel1.picUrl != null) && (localSearchDirectZoneModel2.picUrl != null) && (!localSearchDirectZoneModel1.picUrl.equals(localSearchDirectZoneModel2.picUrl))) || ((localSearchDirectZoneModel1.mScore != null) && (localSearchDirectZoneModel2.mScore != null) && (!localSearchDirectZoneModel1.mScore.equals(localSearchDirectZoneModel2.mScore))) || ((localSearchDirectZoneModel1.mPrice != null) && (localSearchDirectZoneModel2.mPrice != null) && (!localSearchDirectZoneModel1.mPrice.equals(localSearchDirectZoneModel2.mPrice))))
        break;
      i += 1;
    }
    label200: return true;
  }

  public void setMovieGuide(ArrayList<SearchDirectZoneModel> paramArrayList)
  {
    if (sameData(paramArrayList));
    do
    {
      return;
      this.dpMovieOnInfoList = paramArrayList;
      this.layerMovieGuide.removeAllViews();
    }
    while ((this.dpMovieOnInfoList == null) || (this.dpMovieOnInfoList.isEmpty()));
    int i = 0;
    label40: Object localObject;
    if (i < this.dpMovieOnInfoList.size())
    {
      paramArrayList = (SearchDirectZoneModel)this.dpMovieOnInfoList.get(i);
      if (paramArrayList.displayType != 8)
        break label176;
      localObject = (SearchMovieDirectZoneAllMovie)LayoutInflater.from(getContext()).inflate(R.layout.search_movie_guide_allmovie, this.layerMovieGuide, false);
      ((SearchMovieDirectZoneAllMovie)localObject).setSearchMoreText(paramArrayList.title);
      ((SearchMovieDirectZoneAllMovie)localObject).setTag(Integer.valueOf(i));
      ((SearchMovieDirectZoneAllMovie)localObject).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          int i = Integer.parseInt(paramView.getTag().toString());
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(((SearchDirectZoneModel)SearchMovieDirectZoneView.this.dpMovieOnInfoList.get(i)).clickUrl));
          SearchMovieDirectZoneView.this.getContext().startActivity(paramView);
        }
      });
      ((SearchMovieDirectZoneAllMovie)localObject).setGAString("direct_zone");
      ((SearchMovieDirectZoneAllMovie)localObject).gaUserInfo.index = Integer.valueOf(i);
      ((SearchMovieDirectZoneAllMovie)localObject).gaUserInfo.keyword = paramArrayList.keyword;
      ((SearchMovieDirectZoneAllMovie)localObject).gaUserInfo.title = paramArrayList.title;
      this.layerMovieGuide.addView((View)localObject);
    }
    while (true)
    {
      i += 1;
      break label40;
      break;
      label176: localObject = (SearchMovieDirectZoneItem)LayoutInflater.from(getContext()).inflate(R.layout.search_movie_directzone_item, this.layerMovieGuide, false);
      ((SearchMovieDirectZoneItem)localObject).setMovieGuide(paramArrayList);
      ((SearchMovieDirectZoneItem)localObject).setTag(Integer.valueOf(i));
      ((SearchMovieDirectZoneItem)localObject).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          int i = Integer.parseInt(paramView.getTag().toString());
          paramView = ((SearchDirectZoneModel)SearchMovieDirectZoneView.this.dpMovieOnInfoList.get(i)).clickUrl;
          if (!TextUtils.isEmpty(paramView))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            SearchMovieDirectZoneView.this.getContext().startActivity(paramView);
          }
        }
      });
      ((SearchMovieDirectZoneItem)localObject).setGAString("direct_zone");
      ((SearchMovieDirectZoneItem)localObject).gaUserInfo.index = Integer.valueOf(i);
      ((SearchMovieDirectZoneItem)localObject).gaUserInfo.keyword = paramArrayList.keyword;
      ((SearchMovieDirectZoneItem)localObject).gaUserInfo.title = paramArrayList.title;
      this.layerMovieGuide.addView((View)localObject);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.SearchMovieDirectZoneView
 * JD-Core Version:    0.6.0
 */