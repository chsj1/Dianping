package com.dianping.movie.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.movie.view.MovieDiscountPayOptionView;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;

public class MoviePickReductionActivity extends NovaListActivity
  implements AdapterView.OnItemClickListener
{
  protected static final String TAG = MoviePickReductionActivity.class.getSimpleName();
  protected Adapter adapter;
  protected int discountId = 0;
  protected DPObject dpCurrentReductionDiscount;
  protected int movieShowId;
  protected String orderId;
  protected int seatCount;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040015");
    this.orderId = getIntent().getStringExtra("orderid");
    this.dpCurrentReductionDiscount = ((DPObject)getIntent().getParcelableExtra("reductiondiscount"));
    this.movieShowId = getIntParam("movieshowid");
    this.seatCount = getIntParam("seatcount");
    if (this.orderId == null)
    {
      finish();
      return;
    }
    if (this.dpCurrentReductionDiscount != null)
      this.discountId = this.dpCurrentReductionDiscount.getInt("DiscountID");
    this.adapter = new Adapter(this);
    this.listView.setAdapter(this.adapter);
    this.listView.setOnItemClickListener(this);
  }

  public void onDestroy()
  {
    super.onDestroy();
  }

  protected void onDiscountSelected(DPObject paramDPObject)
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("reductiondiscount", paramDPObject);
    setResult(-1, localIntent);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if (((paramAdapterView instanceof DPObject)) && (!((DPObject)paramAdapterView).getBoolean("DiscountUnavailable")))
    {
      paramInt = ((DPObject)paramAdapterView).getInt("DiscountID");
      if (paramInt == this.discountId)
      {
        this.discountId = 0;
        onDiscountSelected(null);
        this.adapter.notifyDataSetChanged();
      }
    }
    else
    {
      return;
    }
    this.discountId = paramInt;
    onDiscountSelected((DPObject)paramAdapterView);
    finish();
  }

  public void onPause()
  {
    super.onPause();
    KeyboardUtils.hideKeyboard(this.listView);
  }

  public void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
  }

  class Adapter extends BasicLoadAdapter
  {
    private Context context;

    public Adapter(Context arg2)
    {
      super();
      this.context = localContext;
    }

    public MApiRequest createRequest(int paramInt)
    {
      Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/rs/getmoviediscountpayoptionmv.bin?").buildUpon();
      localBuilder.appendQueryParameter("orderid", MoviePickReductionActivity.this.orderId);
      localBuilder.appendQueryParameter("token", MoviePickReductionActivity.this.accountService().token());
      localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
      localBuilder.appendQueryParameter("movieshowid", String.valueOf(MoviePickReductionActivity.this.movieShowId));
      localBuilder.appendQueryParameter("seatnum", String.valueOf(MoviePickReductionActivity.this.seatCount));
      localBuilder.appendQueryParameter("discountid", String.valueOf(MoviePickReductionActivity.this.discountId));
      return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    }

    protected String emptyMessage()
    {
      return "无可用电影活动优惠";
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (MoviePickReductionActivity.this.isDPObjectof(paramDPObject, "MovieDiscountPayOption"))
      {
        if ((paramView != null) && ((paramView instanceof MovieDiscountPayOptionView)))
        {
          paramView = (MovieDiscountPayOptionView)paramView;
          paramView.setMovieDiscountPayOption(paramDPObject);
          paramView.setTag(paramDPObject);
          if (MoviePickReductionActivity.this.discountId != paramDPObject.getInt("DiscountID"))
            break label144;
        }
        label144: for (boolean bool = true; ; bool = false)
        {
          paramView.setChecked(bool);
          if ((bool) && (paramDPObject.getBoolean("DiscountUnavailable")))
            MoviePickReductionActivity.this.onDiscountSelected(null);
          paramView.setBackgroundResource(R.drawable.table_view_item);
          paramView.setIsActive(paramDPObject.getBoolean("DiscountUnavailable"));
          return paramView;
          paramView = new MovieDiscountPayOptionView(MoviePickReductionActivity.this);
          paramView.setPadding(ViewUtils.dip2px(this.context, 5.0F), 0, ViewUtils.dip2px(this.context, 5.0F), 0);
          break;
        }
      }
      return null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MoviePickReductionActivity
 * JD-Core Version:    0.6.0
 */