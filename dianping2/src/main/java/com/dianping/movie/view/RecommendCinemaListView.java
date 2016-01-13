package com.dianping.movie.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecommendCinemaListView extends NovaLinearLayout
  implements View.OnClickListener
{
  private LinearLayout cinemaItemLayar;
  private List<DPObject> dpCinemaList = new ArrayList();
  private LayoutInflater inflater;
  private double lat;
  private double lng;

  public RecommendCinemaListView(Context paramContext)
  {
    this(paramContext, null);
  }

  public RecommendCinemaListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  private View createCiemaItem(DPObject paramDPObject, int paramInt)
  {
    CinemaListItem localCinemaListItem = (CinemaListItem)this.inflater.inflate(R.layout.movie_cinema_list_item, this.cinemaItemLayar, false);
    localCinemaListItem.setCinema(paramDPObject, this.lat, this.lng, 0);
    localCinemaListItem.setTag(paramDPObject);
    localCinemaListItem.setOnClickListener(this);
    localCinemaListItem.setGAString("hotcinema", "", paramInt);
    return localCinemaListItem;
  }

  private void init()
  {
    this.inflater = LayoutInflater.from(getContext());
    this.inflater.inflate(R.layout.recommend_cinema_list_view, this, true);
    this.cinemaItemLayar = ((LinearLayout)findViewById(R.id.cinema_item_layer));
    findViewById(R.id.more_cinema_layer).setOnClickListener(new RecommendCinemaListView.1(this));
    findViewById(R.id.allcinemas).setOnClickListener(new RecommendCinemaListView.2(this));
  }

  public void onClick(View paramView)
  {
    if (DPObjectUtils.isDPObjectof(paramView.getTag()))
    {
      paramView = (DPObject)paramView.getTag();
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + paramView.getInt("ID")));
      getContext().startActivity(paramView);
    }
  }

  public void setCinemas(DPObject[] paramArrayOfDPObject, double paramDouble1, double paramDouble2)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0));
    while (true)
    {
      return;
      this.cinemaItemLayar.removeAllViews();
      this.dpCinemaList.clear();
      this.dpCinemaList.addAll(Arrays.asList(paramArrayOfDPObject));
      this.lat = paramDouble1;
      this.lng = paramDouble2;
      int i = 0;
      while (i < paramArrayOfDPObject.length)
      {
        this.cinemaItemLayar.addView(createCiemaItem(paramArrayOfDPObject[i], i));
        i += 1;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.RecommendCinemaListView
 * JD-Core Version:    0.6.0
 */