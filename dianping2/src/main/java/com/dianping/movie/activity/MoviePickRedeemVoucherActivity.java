package com.dianping.movie.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MoviePickRedeemVoucherActivity extends NovaListActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  protected static final String TAG = MoviePickRedeemVoucherActivity.class.getSimpleName();
  protected DiscountListAdapter adapter;
  private boolean canUseMultiRedeemVoucher;
  protected Button confirmRedeemButton;
  protected Context context;
  private View currentSelectRedeemView;
  private DPObject currentSelectRedeemVoucher;
  private PullToRefreshListView discountListPtrView;
  private ArrayList<DPObject> dpRedeemVoucherList;
  protected View headerView;
  protected int movieShowId;
  protected String orderId;
  protected EditText redeemEditText;
  protected View redeemHeaderLayer;
  protected int seatCount;
  protected MApiRequest validateRedeemVoucherRequest;

  private void checkRedeem(boolean paramBoolean)
  {
    if ((this.currentSelectRedeemView == null) || (this.currentSelectRedeemVoucher == null))
      return;
    if (paramBoolean)
    {
      this.dpRedeemVoucherList.add(this.currentSelectRedeemVoucher);
      ((ViewHolder)this.currentSelectRedeemView.getTag()).checkBox.setChecked(true);
      this.confirmRedeemButton.setText("确认使用");
      return;
    }
    removeRedeemVoucher(this.currentSelectRedeemVoucher);
    if (this.dpRedeemVoucherList.size() == 0)
      this.confirmRedeemButton.setText("不使用兑换券");
    ((ViewHolder)this.currentSelectRedeemView.getTag()).checkBox.setChecked(false);
  }

  private int getCurrentRedeemSeatCount()
  {
    int j;
    if (this.dpRedeemVoucherList.size() == 0)
      j = 0;
    int i;
    do
    {
      return j;
      i = 0;
      Iterator localIterator = this.dpRedeemVoucherList.iterator();
      while (localIterator.hasNext())
        i += ((DPObject)localIterator.next()).getInt("MaxSeatCount");
      j = i;
    }
    while (i <= this.seatCount);
    return this.seatCount;
  }

  private void removeRedeemVoucher(DPObject paramDPObject)
  {
    Iterator localIterator = this.dpRedeemVoucherList.iterator();
    while (localIterator.hasNext())
    {
      DPObject localDPObject = (DPObject)localIterator.next();
      if (localDPObject.getInt("ID") != paramDPObject.getInt("ID"))
        continue;
      this.dpRedeemVoucherList.remove(localDPObject);
    }
  }

  private void returnSelectedRedeem()
  {
    Intent localIntent = new Intent();
    if (this.dpRedeemVoucherList.size() > 0)
      localIntent.putParcelableArrayListExtra("redeemvouchers", this.dpRedeemVoucherList);
    localIntent.putExtra("redeemseatcount", getCurrentRedeemSeatCount());
    setResult(-1, localIntent);
    finish();
  }

  private boolean selectedRedeemVouchersContains(DPObject paramDPObject)
  {
    Iterator localIterator = this.dpRedeemVoucherList.iterator();
    while (localIterator.hasNext())
      if (((DPObject)localIterator.next()).getInt("ID") == paramDPObject.getInt("ID"))
        return true;
    return false;
  }

  private void validateRedeemVoucherCode(String paramString)
  {
    KeyboardUtils.hideKeyboard(this.discountListPtrView);
    if (TextUtils.isEmpty(paramString))
    {
      Toast.makeText(this, "请输入兑换密码", 0).show();
      return;
    }
    if ((!this.canUseMultiRedeemVoucher) && (this.dpRedeemVoucherList.size() > 0))
    {
      new AlertDialog.Builder(this).setTitle("提示").setMessage("很抱歉，暂不支持同时使用多张兑换券/码，请先取消选中的兑换券").setPositiveButton("确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      }).create().show();
      return;
    }
    if (this.validateRedeemVoucherRequest != null)
    {
      Log.i(TAG, "already requesting");
      return;
    }
    this.validateRedeemVoucherRequest = BasicMApiRequest.mapiPost("http://app.movie.dianping.com/rs/validateredeemcodemv.bin", new String[] { "token", accountService().token(), "redeemcode", paramString, "movieshowid", "" + this.movieShowId });
    mapiService().exec(this.validateRedeemVoucherRequest, this);
    showProgressDialog("正在验证兑换密码...");
  }

  public void onBackPressed()
  {
    returnSelectedRedeem();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040016");
    setContentView(R.layout.movie_pick_redeem_voucher_activity);
    this.context = this;
    this.confirmRedeemButton = ((Button)findViewById(R.id.confirm_redeem_button));
    this.confirmRedeemButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MoviePickRedeemVoucherActivity.this.returnSelectedRedeem();
      }
    });
    this.confirmRedeemButton.setText("不使用兑换券");
    this.discountListPtrView = ((PullToRefreshListView)findViewById(R.id.movie_discount_list));
    this.discountListPtrView.setHeaderDividersEnabled(false);
    this.discountListPtrView.setBackgroundResource(R.drawable.main_background);
    this.discountListPtrView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.discountListPtrView.setOnItemClickListener(this);
    this.headerView = getLayoutInflater().inflate(R.layout.movie_redeem_list_header, this.discountListPtrView, false);
    this.redeemHeaderLayer = this.headerView.findViewById(R.id.redeem_list_header_layer);
    this.discountListPtrView.addHeaderView(this.headerView, null, false);
    this.orderId = getIntent().getStringExtra("orderid");
    this.movieShowId = getIntParam("movieshowid");
    this.seatCount = getIntParam("seatcount");
    this.dpRedeemVoucherList = getIntent().getParcelableArrayListExtra("redeemvouchers");
    if (this.dpRedeemVoucherList == null)
      this.dpRedeemVoucherList = new ArrayList();
    this.canUseMultiRedeemVoucher = getBooleanParam("canusemultiredeemvoucher");
    if (this.orderId == null)
    {
      finish();
      return;
    }
    this.adapter = new DiscountListAdapter(this);
    this.discountListPtrView.setAdapter(this.adapter);
    if (this.dpRedeemVoucherList.size() > 0)
      this.confirmRedeemButton.setText("确认使用");
    while (true)
    {
      this.redeemEditText = ((EditText)this.redeemHeaderLayer.findViewById(R.id.redeem_voucher_code));
      this.redeemEditText.setOnKeyListener(new View.OnKeyListener()
      {
        public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
        {
          if (paramInt == 66)
          {
            MoviePickRedeemVoucherActivity.this.validateRedeemVoucherCode(MoviePickRedeemVoucherActivity.this.redeemEditText.getText().toString());
            KeyboardUtils.hideKeyboard(MoviePickRedeemVoucherActivity.this.discountListPtrView);
            return true;
          }
          return false;
        }
      });
      this.redeemHeaderLayer.findViewById(R.id.check_redeem_code_btn).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          MoviePickRedeemVoucherActivity.this.validateRedeemVoucherCode(MoviePickRedeemVoucherActivity.this.redeemEditText.getText().toString());
        }
      });
      return;
      this.confirmRedeemButton.setText("不使用兑换券");
    }
  }

  public void onDestroy()
  {
    if (this.validateRedeemVoucherRequest != null)
    {
      mapiService().abort(this.validateRedeemVoucherRequest, this, true);
      this.validateRedeemVoucherRequest = null;
    }
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    int i;
    if (isDPObjectof(paramAdapterView, "MovieRedeemVoucher"))
    {
      paramInt = getCurrentRedeemSeatCount();
      this.currentSelectRedeemView = paramView;
      this.currentSelectRedeemVoucher = ((DPObject)paramAdapterView);
      i = this.currentSelectRedeemVoucher.getInt("MaxSeatCount");
      if (selectedRedeemVouchersContains(this.currentSelectRedeemVoucher))
        checkRedeem(false);
    }
    else
    {
      return;
    }
    if ((!this.canUseMultiRedeemVoucher) && (this.dpRedeemVoucherList.size() > 0))
    {
      new AlertDialog.Builder(this).setTitle("提示").setMessage("很抱歉，暂不支持同时使用多张兑换券").setPositiveButton("确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      }).create().show();
      return;
    }
    if (paramInt == this.seatCount)
    {
      Toast.makeText(this, "亲，您选的兑换券足够兑换" + this.seatCount + "张票啦", 0).show();
      return;
    }
    if (paramInt + i == this.seatCount)
    {
      checkRedeem(true);
      return;
    }
    if (paramInt + i > this.seatCount)
    {
      paramAdapterView = String.format("您选择的兑换券共可兑换%d张票，但目前仅选择了%d个座位,确认还要使用这张吗？", new Object[] { Integer.valueOf(paramInt + i), Integer.valueOf(this.seatCount) });
      new AlertDialog.Builder(this).setTitle("提示").setMessage(paramAdapterView).setPositiveButton("确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          MoviePickRedeemVoucherActivity.this.checkRedeem(true);
        }
      }).setNegativeButton("取消", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      }).create().show();
      return;
    }
    checkRedeem(true);
  }

  public void onPause()
  {
    super.onPause();
    KeyboardUtils.hideKeyboard(this.discountListPtrView);
  }

  public void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
    if (this.validateRedeemVoucherRequest != null)
      this.validateRedeemVoucherRequest = null;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    paramMApiResponse = paramMApiResponse.message();
    paramMApiResponse = paramMApiResponse.title() + ":" + paramMApiResponse.content();
    if (this.validateRedeemVoucherRequest == paramMApiRequest)
    {
      this.validateRedeemVoucherRequest = null;
      Toast.makeText(this, paramMApiResponse, 0).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      if (this.validateRedeemVoucherRequest == paramMApiRequest)
      {
        this.validateRedeemVoucherRequest = null;
        if (paramMApiResponse != null)
        {
          if ((!selectedRedeemVouchersContains(paramMApiResponse)) && (getCurrentRedeemSeatCount() + paramMApiResponse.getInt("MaxSeatCount") <= this.seatCount))
            this.dpRedeemVoucherList.add(paramMApiResponse);
          this.adapter.addDPObjectAsFirt(paramMApiResponse);
        }
      }
    }
  }

  class DiscountListAdapter extends BasicLoadAdapter
  {
    private Set<Integer> redeemIdsForRedeemCode = new HashSet();

    public DiscountListAdapter(Context arg2)
    {
      super();
    }

    public void addDPObjectAsFirt(DPObject paramDPObject)
    {
      if (DPObjectUtils.isDPObjectof(paramDPObject))
      {
        if (this.redeemIdsForRedeemCode.add(Integer.valueOf(paramDPObject.getInt("ID"))))
        {
          MoviePickRedeemVoucherActivity.this.confirmRedeemButton.setText("确认使用");
          this.mData.add(0, paramDPObject);
          notifyDataSetChanged();
        }
      }
      else
        return;
      notifyDataSetChanged();
      Toast.makeText(MoviePickRedeemVoucherActivity.this.context, "已经验证并添加该兑换码啦", 0).show();
    }

    public MApiRequest createRequest(int paramInt)
    {
      Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/rs/redeemvoucherlistmv.bin?").buildUpon();
      localBuilder.appendQueryParameter("cityid", String.valueOf(MoviePickRedeemVoucherActivity.this.cityId()));
      localBuilder.appendQueryParameter("filterid", String.valueOf(1));
      localBuilder.appendQueryParameter("token", MoviePickRedeemVoucherActivity.this.accountService().token());
      localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
      localBuilder.appendQueryParameter("movieshowid", String.valueOf(MoviePickRedeemVoucherActivity.this.movieShowId));
      return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    }

    protected String emptyMessage()
    {
      return "没有满足使用条件的兑换券";
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (MoviePickRedeemVoucherActivity.this.isDPObjectof(paramDPObject, "MovieRedeemVoucher"))
      {
        Object localObject2 = null;
        Object localObject1 = localObject2;
        if (paramView != null)
        {
          localObject1 = localObject2;
          if (getItemViewType(paramInt) == 3)
            localObject1 = paramView;
        }
        if (localObject1 == null)
        {
          paramView = new MoviePickRedeemVoucherActivity.ViewHolder();
          localObject1 = LayoutInflater.from(MoviePickRedeemVoucherActivity.this).inflate(R.layout.movie_redeem_voucher_item, paramViewGroup, false);
          paramView.priceView = ((TextView)((View)localObject1).findViewById(R.id.movie_redeem_amount));
          paramView.titleView = ((TextView)((View)localObject1).findViewById(R.id.movie_redeem_title));
          paramView.subtitleView = ((TextView)((View)localObject1).findViewById(R.id.movie_redeem_subtitle));
          paramView.checkBox = ((CheckBox)((View)localObject1).findViewById(R.id.movie_redeem_check));
          ((View)localObject1).setTag(paramView);
        }
        while (true)
        {
          paramView.resetForRedeem();
          paramView.titleView.setText(paramDPObject.getString("Title"));
          paramView.subtitleView.setText(paramDPObject.getString("SubTitle"));
          paramView.priceView.setText("-￥" + PriceFormatUtils.formatPrice(paramDPObject.getString("RedeemAmount")));
          paramView.checkBox.setChecked(MoviePickRedeemVoucherActivity.this.selectedRedeemVouchersContains(paramDPObject));
          return localObject1;
          paramView = (MoviePickRedeemVoucherActivity.ViewHolder)((View)localObject1).getTag();
        }
      }
      return (View)null;
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
      if (((paramMApiResponse.result() instanceof DPObject)) && (MoviePickRedeemVoucherActivity.this.dpRedeemVoucherList.size() > 0) && (this.mNextStartIndex <= 25))
      {
        paramMApiRequest = MoviePickRedeemVoucherActivity.this.dpRedeemVoucherList.iterator();
        while (paramMApiRequest.hasNext())
        {
          paramMApiResponse = (DPObject)paramMApiRequest.next();
          if (TextUtils.isEmpty(paramMApiResponse.getString("RedeemVoucherCode")))
            continue;
          addDPObjectAsFirt(paramMApiResponse);
        }
      }
    }
  }

  public static final class ViewHolder
  {
    public CheckBox checkBox;
    public TextView extraView;
    public TextView priceView;
    public RadioButton radioButton;
    public TextView subtitleView;
    public TextView titleView;
    public TextView validView;

    public void resetForRedeem()
    {
      this.priceView.setVisibility(0);
      this.titleView.setVisibility(0);
      this.subtitleView.setVisibility(0);
      this.priceView.setText("");
      this.titleView.setText("");
      this.subtitleView.setText("");
      this.checkBox.setVisibility(0);
      this.checkBox.setChecked(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MoviePickRedeemVoucherActivity
 * JD-Core Version:    0.6.0
 */