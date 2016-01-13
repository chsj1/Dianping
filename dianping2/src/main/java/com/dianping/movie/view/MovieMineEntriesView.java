package com.dianping.movie.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.zeus.utils.ViewUtils;
import java.util.Arrays;
import java.util.List;

public class MovieMineEntriesView extends NovaLinearLayout
  implements View.OnClickListener
{
  private static final int CELL_TYPE_WITH_BADGE = 1;
  private boolean isLocal = false;
  private MovieMineEntryItem itemWithBadge;
  private List<DPObject> localCells = Arrays.asList(new DPObject[] { new DPObject().edit().putInt("ID", R.drawable.my_movie_ticket_icon).putString("Title", "我的电影票").putString("CellData", "dianping://mymovieticket").generate(), new DPObject().edit().putInt("ID", R.drawable.my_movie_redeem_icon).putString("Title", "我的兑换券").putString("CellData", "dianping://web?url=http%3a%2f%2fm.dianping.com%2fmovie%2fh5%2fuserRedeemVouchers%3ffilter%3d1%26cityid%3d*%26token%3d!%26product%3d*").generate(), new DPObject().edit().putInt("ID", R.drawable.my_wish_movie_icon_normal).putString("Title", "收藏的影片").putString("CellData", "dianping://moviemycollection").generate(), new DPObject().edit().putInt("ID", R.drawable.my_movie_notice_icon).putString("Title", "收到的消息").putString("CellData", "dianping://web?url=http%3a%2f%2fm.dianping.com%2fmovie%2ffans%2fmessage%3ftoken%3d!%26version%3d*%26dpshare%3d0%26product%3d*").putInt("CellType", 1).generate() });

  public MovieMineEntriesView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieMineEntriesView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOrientation(1);
    setBackgroundResource(R.color.white);
    setPadding(ViewUtils.dip2px(getContext(), 15.0F), 0, 0, 0);
  }

  public MovieMineEntryItem createEntryItem(DPObject paramDPObject, int paramInt)
  {
    MovieMineEntryItem localMovieMineEntryItem = (MovieMineEntryItem)LayoutInflater.from(getContext()).inflate(R.layout.movie_mine_entry_item, this, false);
    localMovieMineEntryItem.setEntryContent(paramDPObject, this.isLocal);
    localMovieMineEntryItem.setGAString("myentriesitem", paramDPObject.getString("Title"), paramInt);
    return localMovieMineEntryItem;
  }

  public void onClick(View paramView)
  {
    if ((paramView instanceof MovieMineEntryItem))
    {
      DPObject localDPObject = ((MovieMineEntryItem)paramView).getCell();
      if (localDPObject.getInt("CellType") == 1)
        ((MovieMineEntryItem)paramView).updateBadge("");
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(localDPObject.getString("CellData")));
      getContext().startActivity(localIntent);
      ((DPActivity)getContext()).statisticsEvent("movie5", "movie5_mymmovie_entriesitem", paramView.getTag() + "|" + localDPObject.getString("CellData"), 0);
    }
  }

  public void setEntries(List<DPObject> paramList)
  {
    this.isLocal = false;
    removeAllViews();
    Object localObject;
    if (paramList != null)
    {
      localObject = paramList;
      if (paramList.size() != 0);
    }
    else
    {
      this.isLocal = true;
      localObject = this.localCells;
    }
    int i = 0;
    while (i < ((List)localObject).size())
    {
      paramList = createEntryItem((DPObject)((List)localObject).get(i), i);
      paramList.setOnClickListener(this);
      paramList.setTag(Integer.valueOf(i));
      if (((DPObject)((List)localObject).get(i)).getInt("CellType") == 1)
        this.itemWithBadge = paramList;
      addView(paramList);
      if (i < ((List)localObject).size() - 1)
      {
        paramList = LayoutInflater.from(getContext()).inflate(R.layout.movie_horizontal_divider, this, false);
        paramList.setPadding(0, 0, 0, 0);
        addView(paramList);
      }
      i += 1;
    }
  }

  public void updateBadge(String paramString)
  {
    if (this.itemWithBadge == null)
      return;
    this.itemWithBadge.updateBadge(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.MovieMineEntriesView
 * JD-Core Version:    0.6.0
 */