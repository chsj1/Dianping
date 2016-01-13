package com.dianping.movie.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

public class MovieTrailerListActivity extends NovaActivity
  implements AdapterView.OnItemClickListener
{
  private MovieTrailerAdapter mAdapter;
  private int movieId = 0;
  private PullToRefreshListView ptrlv;

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("预告片");
    setContentView(R.layout.movie_trailer_list_layout);
    this.movieId = getIntParam("movieid");
    if (this.movieId == 0)
      finish();
    this.ptrlv = ((PullToRefreshListView)findViewById(R.id.trailerlist_ptr));
    this.ptrlv.setPullRefreshEnable(1);
    this.ptrlv.setPullLoadEnable(0);
    this.ptrlv.setOnItemClickListener(this);
    this.mAdapter = new MovieTrailerAdapter(this);
    this.ptrlv.setAdapter(this.mAdapter);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if (DPObjectUtils.isDPObjectof(paramAdapterView, "MovieTrailer"))
    {
      paramView = new Intent(this, MovieVideoActivity.class);
      paramView.putExtra("VIDEO_URL", ((DPObject)paramAdapterView).getString("SourceUrl"));
      startActivity(paramView);
    }
  }

  private class MovieTrailerAdapter extends BasicLoadAdapter
  {
    private Context context;

    public MovieTrailerAdapter(Context arg2)
    {
      super();
      this.context = localContext;
    }

    public MApiRequest createRequest(int paramInt)
    {
      Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/movietrailerlistmv.bin?").buildUpon();
      localBuilder.appendQueryParameter("movieid", String.valueOf(MovieTrailerListActivity.this.movieId));
      localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
      localBuilder.appendQueryParameter("limit", String.valueOf(25));
      return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.NORMAL);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = paramView;
      if (paramView == null)
      {
        localView = MovieTrailerListActivity.this.getLayoutInflater().inflate(R.layout.movie_trailer_item_layout, paramViewGroup, false);
        paramView = localView.getLayoutParams();
        paramView.height = (ViewUtils.getScreenWidthPixels(this.context) / 16 * 9);
        localView.setLayoutParams(paramView);
      }
      paramView = (MovieTrailerListActivity.MovieTrailerAdapter.ViewHolder)localView.getTag();
      if (paramView == null)
      {
        paramView = new MovieTrailerListActivity.MovieTrailerAdapter.ViewHolder(this);
        paramView.backGroundImageView = ((NetworkImageView)localView.findViewById(R.id.bg_imageview));
        paramView.descTextView = ((TextView)localView.findViewById(R.id.desc_textview));
        if (TextUtils.isEmpty(paramDPObject.getString("ImageUrl")))
          break label154;
        paramView.backGroundImageView.setImage(paramDPObject.getString("ImageUrl"));
      }
      while (true)
      {
        paramView.descTextView.setText(paramDPObject.getString("Desc"));
        return localView;
        paramView.reset();
        break;
        label154: paramView.backGroundImageView.setBackgroundColor(-16777216);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieTrailerListActivity
 * JD-Core Version:    0.6.0
 */