package com.dianping.movie.activity;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.movie.view.MovieOnInfoItem;
import com.dianping.util.CollectionUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyWishMovieActivity extends NovaListActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  private static final int LIMIT = 25;
  private static final String TAG_CANCEL = "C";
  private static final String TAG_DELETE = "B";
  private static final String TAG_EDIT = "A";
  private MovieWishListAdapter adapter;
  private Button cancelButton;
  private MApiRequest delWishRequest;
  private Button deleteButton;
  private View emptyViewForWishMovie;
  private boolean isEditMode;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("movie:wish_movie_modified"))
        MyWishMovieActivity.this.adapter.reset();
    }
  };
  private ArrayList<String> movieIds = new ArrayList();

  private void delWishMovies()
  {
    if (this.movieIds.size() == 0)
    {
      showAlertDialog("提示", "请至少选择一项！");
      return;
    }
    new AlertDialog.Builder(this).setMessage("确定删除选择的影片？").setPositiveButton("确认", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if (MyWishMovieActivity.this.delWishRequest != null)
        {
          MyWishMovieActivity.this.mapiService().abort(MyWishMovieActivity.this.delWishRequest, MyWishMovieActivity.this, true);
          MyWishMovieActivity.access$502(MyWishMovieActivity.this, null);
        }
        paramDialogInterface = new StringBuilder();
        paramDialogInterface.append("http://app.movie.dianping.com/");
        paramDialogInterface.append("removewishmoviemv.bin?");
        MyWishMovieActivity.access$502(MyWishMovieActivity.this, BasicMApiRequest.mapiPost(paramDialogInterface.toString(), new String[] { "token", MyWishMovieActivity.this.accountService().token(), "movieids", CollectionUtils.list2Str(MyWishMovieActivity.access$400(MyWishMovieActivity.this), ",") + "" }));
        MyWishMovieActivity.this.mapiService().exec(MyWishMovieActivity.this.delWishRequest, MyWishMovieActivity.this);
        MyWishMovieActivity.this.showProgressDialog("删除中...");
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MyWishMovieActivity.this.adapter.reset();
        paramDialogInterface.cancel();
      }
    }).show();
  }

  private void toggleTitleButton()
  {
    if (this.adapter.getDataList().size() == 0)
    {
      getTitleBar().removeAllRightViewItem();
      return;
    }
    getTitleBar().removeAllRightViewItem();
    if (this.isEditMode)
    {
      getTitleBar().addRightViewItem(this.deleteButton, "B", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          MyWishMovieActivity.this.delWishMovies();
        }
      });
      getTitleBar().addRightViewItem(this.cancelButton, "C", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          MyWishMovieActivity.access$202(MyWishMovieActivity.this, false);
          MyWishMovieActivity.this.toggleTitleButton();
          MyWishMovieActivity.this.movieIds.clear();
          MyWishMovieActivity.this.adapter.notifyDataSetChanged();
        }
      });
      return;
    }
    getTitleBar().addRightViewItem("编辑", "A", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MyWishMovieActivity.access$202(MyWishMovieActivity.this, true);
        MyWishMovieActivity.this.toggleTitleButton();
        MyWishMovieActivity.this.adapter.notifyDataSetChanged();
      }
    });
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040012");
    setTitle("我收藏的影片");
    this.deleteButton = new Button(this);
    paramBundle = new LinearLayout.LayoutParams(-2, -1);
    this.deleteButton.setLayoutParams(paramBundle);
    this.deleteButton.setGravity(17);
    this.deleteButton.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
    this.deleteButton.setTextSize(0, getResources().getDimension(R.dimen.text_size_15));
    this.deleteButton.setBackgroundResource(R.color.transparent);
    this.deleteButton.setPadding(ViewUtils.dip2px(this, 15.0F), 0, ViewUtils.dip2px(this, 15.0F), 0);
    this.deleteButton.setText("删除");
    this.cancelButton = new Button(this);
    this.cancelButton.setLayoutParams(paramBundle);
    this.cancelButton.setGravity(17);
    this.cancelButton.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
    this.cancelButton.setTextSize(0, getResources().getDimension(R.dimen.text_size_15));
    this.cancelButton.setBackgroundResource(R.drawable.ic_titlebar_btn_bg_line);
    this.cancelButton.setPadding(ViewUtils.dip2px(this, 15.0F), 0, ViewUtils.dip2px(this, 5.0F), 0);
    this.cancelButton.setText("取消");
    this.adapter = new MovieWishListAdapter(this);
    if (this.listView.getAdapter() != null)
      this.adapter.reset();
    while (true)
    {
      this.listView.setOnItemClickListener(this);
      this.emptyViewForWishMovie = LayoutInflater.from(this).inflate(R.layout.movie_empty_imgtxtbtn_view, null);
      paramBundle = (TextView)this.emptyViewForWishMovie.findViewById(R.id.text);
      paramBundle.setText("您还没有收藏过的影片哦");
      paramBundle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.empty_page_nothing), null, null, null);
      paramBundle = (Button)this.emptyViewForWishMovie.findViewById(R.id.add_btn);
      paramBundle.setVisibility(0);
      paramBundle.setText("去看热门影片");
      paramBundle.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW");
          paramView.setData(Uri.parse("dianping://movielist"));
          MyWishMovieActivity.this.startActivity(paramView);
        }
      });
      paramBundle = new IntentFilter("movie:wish_movie_modified");
      registerReceiver(this.mReceiver, paramBundle);
      return;
      this.listView.setAdapter(this.adapter);
    }
  }

  public void onDestroy()
  {
    this.adapter.cancelLoad();
    if (this.delWishRequest != null)
    {
      mapiService().abort(this.delWishRequest, this, true);
      this.delWishRequest = null;
    }
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    this.movieIds = null;
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if (isDPObjectof(paramAdapterView, "MovieOnInfo"))
    {
      paramAdapterView = (DPObject)paramAdapterView;
      if (!this.isEditMode)
      {
        paramAdapterView = paramAdapterView.getObject("Movie");
        startActivity("dianping://moviedetail?movieid=" + paramAdapterView.getInt("ID"));
      }
    }
    else
    {
      return;
    }
    if (!this.movieIds.remove(paramAdapterView.getObject("Movie").getInt("ID") + ""))
      this.movieIds.add(paramAdapterView.getObject("Movie").getInt("ID") + "");
    this.adapter.notifyDataSetChanged();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.delWishRequest == paramMApiRequest)
    {
      this.delWishRequest = null;
      dismissDialog();
      paramMApiRequest = paramMApiResponse.message();
      Toast.makeText(this, paramMApiRequest.title() + ":" + paramMApiRequest.content(), 0).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.delWishRequest == paramMApiRequest)
      try
      {
        this.delWishRequest = null;
        dismissDialog();
        if (((DPObject)paramMApiResponse.result()).getInt("Flag") == 1);
        for (paramMApiRequest = "删除成功"; ; paramMApiRequest = "删除失败")
        {
          Toast.makeText(this, paramMApiRequest, 0).show();
          this.adapter.removeWishMovies(this.movieIds);
          this.movieIds.clear();
          return;
        }
      }
      catch (Exception paramMApiRequest)
      {
      }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.adapter.onRestoreInstanceState(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    this.adapter.onSaveInstanceState(paramBundle);
    super.onSaveInstanceState(paramBundle);
  }

  class MovieWishListAdapter extends BasicLoadAdapter
  {
    public MovieWishListAdapter(Context arg2)
    {
      super();
    }

    public MApiRequest createRequest(int paramInt)
    {
      Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/wishmovielistmv.bin?").buildUpon();
      localBuilder.appendQueryParameter("token", MyWishMovieActivity.this.accountService().token());
      localBuilder.appendQueryParameter("cityid", String.valueOf(MyWishMovieActivity.this.cityId()));
      localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
      localBuilder.appendQueryParameter("limit", String.valueOf(25));
      return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (getItem(paramInt) == EMPTY)
        return MyWishMovieActivity.this.emptyViewForWishMovie;
      return super.getView(paramInt, paramView, paramViewGroup);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject1 = null;
      Object localObject2 = null;
      if (MyWishMovieActivity.this.isDPObjectof(paramDPObject, "MovieOnInfo"))
      {
        localObject1 = localObject2;
        if (paramView != null)
          localObject1 = (MovieOnInfoItem)paramView;
        paramView = (View)localObject1;
        if (localObject1 == null)
          paramView = (MovieOnInfoItem)MyWishMovieActivity.this.getLayoutInflater().inflate(R.layout.movie_on_info_item, paramViewGroup, false);
        paramView.setMovieOnInfo(paramDPObject, 1, paramInt);
        paramView.setEditable(MyWishMovieActivity.this.isEditMode);
        paramDPObject = paramDPObject.getObject("Movie").getInt("ID") + "";
        paramView.setChecked(MyWishMovieActivity.this.movieIds.contains(paramDPObject));
        localObject1 = paramView;
      }
      return (View)localObject1;
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
      MyWishMovieActivity.this.toggleTitleButton();
    }

    public void removeWishMovies(List<String> paramList)
    {
      if ((paramList == null) || (paramList.size() == 0))
        return;
      paramList = paramList.iterator();
      label105: 
      while (paramList.hasNext())
      {
        String str = (String)paramList.next();
        Iterator localIterator = this.mData.iterator();
        while (true)
          while (true)
          {
            if (!localIterator.hasNext())
              break label105;
            DPObject localDPObject = (DPObject)localIterator.next();
            try
            {
              if (Integer.parseInt(str) != localDPObject.getObject("Movie").getInt("ID"))
                continue;
              MyWishMovieActivity.this.adapter.remove(localDPObject);
            }
            catch (Exception localException)
            {
            }
          }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MyWishMovieActivity
 * JD-Core Version:    0.6.0
 */