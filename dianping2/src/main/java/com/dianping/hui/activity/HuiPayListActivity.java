package com.dianping.hui.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.Environment;
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
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class HuiPayListActivity extends NovaListActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  private static final int FLAG_LOGIN_MY_PAY_LIST = 20;
  private static final int FLAG_LOGIN_MY_TICKETS = 10;
  private static final DecimalFormat FMT = new DecimalFormat("0.00");
  private static final String TAG = "TAG_hui";
  private Adapter adapter;
  private View header;
  private DPObject lastOrder;
  private DPObject link;
  private int loginFlag;
  private MApiRequest request;

  private void loadNewPage()
  {
    this.adapter.errorMsg = null;
    this.adapter.notifyDataSetChanged();
    if (this.request != null)
      return;
    String str = accountService().token();
    ArrayList localArrayList = new ArrayList();
    if (!TextUtils.isEmpty(str))
      localArrayList.add(new BasicNameValuePair("token", str));
    localArrayList.add(new BasicNameValuePair("uuid", Environment.uuid()));
    localArrayList.add(new BasicNameValuePair("composestartindex", this.adapter.nextComposeStartIndex));
    localArrayList.add(new BasicNameValuePair("iscomposeend", this.adapter.isComposeEnd));
    if (this.lastOrder != null)
    {
      localArrayList.add(new BasicNameValuePair("lastorderserializedid", this.lastOrder.getString("SerializedId")));
      localArrayList.add(new BasicNameValuePair("lastordertime", this.lastOrder.getTime("Time") + ""));
    }
    str = URLEncodedUtils.format(localArrayList, "utf-8");
    this.request = BasicMApiRequest.mapiGet("http://hui.api.dianping.com/getmopayorder.bin?" + str, CacheType.DISABLED);
    mapiService().exec(this.request, this);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.adapter = new Adapter();
    this.listView.setAdapter(this.adapter);
    this.listView.setOnItemClickListener(this);
    paramBundle = new ColorDrawable(getResources().getColor(R.color.common_bk_color));
    this.listView.setDivider(paramBundle);
    this.listView.setDividerHeight(ViewUtils.dip2px(this, 20.0F));
    super.getTitleBar().setTitle("我的买单");
    super.getTitleBar().addRightViewItem("闪惠尊享券", "checkhuiticket", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        HuiPayListActivity.this.statisticsEvent("hui7", "hui7_mypay_viplist", "", 0);
        paramView = HuiPayListActivity.this.accountService().token();
        if (TextUtils.isEmpty(paramView))
        {
          HuiPayListActivity.access$002(HuiPayListActivity.this, 10);
          HuiPayListActivity.this.accountService().login(HuiPayListActivity.this);
          return;
        }
        paramView = Uri.encode("http://m.dianping.com/hui/ticket/usabletickets?dpshare=0&token=" + paramView);
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramView));
        HuiPayListActivity.this.startActivity(paramView);
      }
    });
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.request != null)
    {
      mapiService().abort(this.request, this, true);
      this.request = null;
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getAdapter().getItem(paramInt);
    if ((paramAdapterView instanceof DPObject))
    {
      paramAdapterView = (DPObject)paramAdapterView;
      statisticsEvent("profile5", "profile5_pay_item", "" + paramAdapterView.getString("SerializedID"), 0);
      paramView = Uri.parse("dianping://huipaydetail").buildUpon();
      paramView.appendQueryParameter("biztype", Integer.toString(paramAdapterView.getInt("BizType")));
      paramView.appendQueryParameter("serializedid", paramAdapterView.getString("SerializedId"));
      paramView.appendQueryParameter("ordertime", paramAdapterView.getTime("Time") + "");
      startActivity(new Intent("android.intent.action.VIEW", paramView.build()));
    }
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      Object localObject;
      switch (this.loginFlag)
      {
      default:
        localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://mopaylist"));
        ((Intent)localObject).setFlags(67108864);
        startActivity((Intent)localObject);
      case 10:
      case 20:
      }
      while (true)
      {
        return true;
        localObject = Uri.encode("http://m.dianping.com/hui/ticket/usabletickets?dpshare=0&token=" + accountService().token());
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + (String)localObject)));
        if (this.header != null)
          this.listView.removeHeaderView(this.header);
        this.adapter.reset();
        loadNewPage();
        continue;
        localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://mopaylist"));
        ((Intent)localObject).setFlags(67108864);
        startActivity((Intent)localObject);
      }
    }
    return false;
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    if (this.header != null)
      this.listView.removeHeaderView(this.header);
    this.adapter.reset();
    this.adapter.notifyDataSetChanged();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
    {
      this.request = null;
      if ((paramMApiResponse.message() == null) || (TextUtils.isEmpty(paramMApiResponse.message().content())))
        break label61;
    }
    label61: for (this.adapter.errorMsg = paramMApiResponse.message().content(); ; this.adapter.errorMsg = "服务器出错")
    {
      this.adapter.notifyDataSetChanged();
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        this.link = paramMApiRequest.getObject("AlertLoginLink");
        if ((this.link != null) && (this.adapter.nextComposeStartIndex.equals("0,0")))
        {
          this.listView.setAdapter(null);
          if (this.header != null)
            this.listView.removeHeaderView(this.header);
          this.header = LayoutInflater.from(this).inflate(R.layout.item_bind_phone, null, false);
          ((TextView)this.header.findViewById(R.id.title)).setText(this.link.getString("Name"));
          this.header.setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              if (TextUtils.isEmpty(HuiPayListActivity.this.link.getString("Url")))
              {
                Toast.makeText(HuiPayListActivity.this, "无法获取参数", 0).show();
                return;
              }
              HuiPayListActivity.access$002(HuiPayListActivity.this, 20);
              HuiPayListActivity.this.accountService().login(HuiPayListActivity.this);
            }
          });
          this.listView.addHeaderView(this.header);
          this.listView.setAdapter(this.adapter);
        }
        this.adapter.appendData(paramMApiRequest);
      }
      this.request = null;
    }
  }

  protected void setupView()
  {
    super.setContentView(R.layout.mobile_pay_list);
    super.getWindow().setBackgroundDrawable(null);
  }

  class Adapter extends BasicAdapter
  {
    String emptyMsg;
    String errorMsg;
    String isComposeEnd = "0,0";
    String nextComposeStartIndex = "0,0";
    ArrayList<DPObject> orderList = new ArrayList();

    Adapter()
    {
    }

    public void appendData(DPObject paramDPObject)
    {
      this.isComposeEnd = paramDPObject.getString("IsComposeEnd");
      this.nextComposeStartIndex = paramDPObject.getString("NextComposeStartIndex");
      this.emptyMsg = paramDPObject.getString("EmptyMsg");
      paramDPObject = paramDPObject.getArray("List");
      if (paramDPObject != null)
        Collections.addAll(this.orderList, paramDPObject);
      if ((this.orderList != null) && (this.orderList.size() > 1))
        HuiPayListActivity.access$402(HuiPayListActivity.this, (DPObject)this.orderList.get(this.orderList.size() - 1));
      notifyDataSetChanged();
    }

    public int getCount()
    {
      if ((!this.isComposeEnd.equals("1,1")) || (this.emptyMsg != null))
        return this.orderList.size() + 1;
      return this.orderList.size();
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.orderList.size())
        return this.orderList.get(paramInt);
      if (this.emptyMsg != null)
        return EMPTY;
      if (this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject1 = getItem(paramInt);
      if ((localObject1 instanceof DPObject))
      {
        localObject1 = (DPObject)localObject1;
        Object localObject2;
        View localView2;
        double d2;
        View localView1;
        if ((paramView == null) || (paramView.getTag() != this))
        {
          paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_mobile_pay, paramViewGroup, false);
          paramView.setTag(this);
          paramViewGroup = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(((DPObject)localObject1).getTime("Time")));
          ((TextView)paramView.findViewById(R.id.name)).setText(((DPObject)localObject1).getString("ShopName"));
          ((TextView)paramView.findViewById(R.id.time)).setText(paramViewGroup);
          ((TextView)paramView.findViewById(R.id.price)).setText("消费 ¥" + HuiPayListActivity.FMT.format(((DPObject)localObject1).getDouble("OriAmount")));
          paramViewGroup = ((DPObject)localObject1).getString("StatusMsg");
          ((TextView)paramView.findViewById(R.id.status)).setText(paramViewGroup);
          double d1 = ((DPObject)localObject1).getDouble("CurrentAmount");
          localObject2 = (TextView)paramView.findViewById(R.id.paid_price);
          localView2 = paramView.findViewById(R.id.price_divider1);
          d2 = ((DPObject)localObject1).getDouble("SaveAmount");
          paramViewGroup = (TextView)paramView.findViewById(R.id.saved_price);
          localView1 = paramView.findViewById(R.id.price_divider2);
          paramInt = ((DPObject)localObject1).getInt("Status");
          if ((paramInt == 0) || (paramInt == -1))
            break label451;
          if (d1 <= 0.0D)
            break label419;
          ((TextView)localObject2).setText("实付 ¥" + HuiPayListActivity.FMT.format(d1));
          ((TextView)localObject2).setVisibility(0);
          localView2.setVisibility(0);
        }
        while (true)
        {
          if (d2 <= 0.0D)
            break label436;
          localObject1 = "节省 ¥" + HuiPayListActivity.FMT.format(d2);
          localObject2 = new SpannableString((CharSequence)localObject1);
          ((SpannableString)localObject2).setSpan(new ForegroundColorSpan(HuiPayListActivity.this.getResources().getColor(R.color.light_red)), ((String)localObject1).indexOf("¥"), ((String)localObject1).length(), 18);
          paramViewGroup.setText((CharSequence)localObject2);
          paramViewGroup.setVisibility(0);
          localView1.setVisibility(0);
          return paramView;
          break;
          label419: ((TextView)localObject2).setVisibility(8);
          localView2.setVisibility(8);
        }
        label436: paramViewGroup.setVisibility(8);
        localView1.setVisibility(8);
        return paramView;
        label451: ((TextView)localObject2).setVisibility(8);
        localView2.setVisibility(8);
        paramViewGroup.setVisibility(8);
        localView1.setVisibility(8);
        return paramView;
      }
      if (localObject1 == LOADING)
      {
        HuiPayListActivity.this.loadNewPage();
        return getLoadingView(paramViewGroup, paramView);
      }
      if (localObject1 == EMPTY)
        return getEmptyView(this.emptyMsg, null, paramViewGroup, paramView);
      return (View)(View)getFailedView(this.errorMsg, new HuiPayListActivity.Adapter.1(this), paramViewGroup, paramView);
    }

    public void reset()
    {
      this.errorMsg = null;
      this.emptyMsg = null;
      this.orderList.clear();
      this.isComposeEnd = "0,0";
      this.nextComposeStartIndex = "0,0";
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiPayListActivity
 * JD-Core Version:    0.6.0
 */