package com.dianping.shopinfo.beauty.hair;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BeautyCouponAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String BEAUTY_SHOP_BASIC_INFO = "beautyShopBasicInfo";
  private static final String CELL_BEAUTY_COUPON = "0200Basic.33Coupon";
  private DPObject couponInfos;
  private List<View> couponViews = new ArrayList();
  MApiRequest gaincouponReq;
  MApiRequest getcouponinfoReq;
  Dialog popup;

  public BeautyCouponAgent(Object paramObject)
  {
    super(paramObject);
  }

  private Dialog buildDialog(Map<String, Object> paramMap, String paramString)
  {
    if (this.popup == null)
    {
      this.popup = new Dialog(getContext());
      this.popup.requestWindowFeature(1);
      this.popup.setContentView(R.layout.shopinfo_beauty_coupon_popup);
      this.popup.getWindow().setBackgroundDrawableResource(R.color.transparent);
      this.popup.getWindow().setLayout(-1, -2);
      this.popup.getWindow().setGravity(1);
      this.popup.setCanceledOnTouchOutside(true);
      this.popup.setOnCancelListener(new DialogInterface.OnCancelListener(paramString)
      {
        public void onCancel(DialogInterface paramDialogInterface)
        {
          paramDialogInterface = BeautyCouponAgent.this.couponViews.iterator();
          while (paramDialogInterface.hasNext())
          {
            View localView = (View)paramDialogInterface.next();
            TextView localTextView = (TextView)localView.findViewById(R.id.coupon_id);
            if (!this.val$couponId.equals(localTextView.getText().toString()))
              continue;
            localView.setClickable(true);
            BeautyCouponAgent.this.couponViews.remove(localView);
          }
        }
      });
    }
    LinearLayout localLinearLayout = (LinearLayout)this.popup.findViewById(R.id.content);
    if (paramMap != null)
    {
      displayDialogContent(localLinearLayout, paramMap);
      localLinearLayout.setVisibility(0);
    }
    while (true)
    {
      paramMap = (Button)this.popup.findViewById(R.id.button);
      paramMap.setClickable(true);
      paramMap.setOnClickListener(new View.OnClickListener(paramString)
      {
        public void onClick(View paramView)
        {
          BeautyCouponAgent.this.popup.dismiss();
          paramView = BeautyCouponAgent.this.couponViews.iterator();
          while (paramView.hasNext())
          {
            View localView = (View)paramView.next();
            TextView localTextView = (TextView)localView.findViewById(R.id.coupon_id);
            if (!this.val$couponId.equals(localTextView.getText().toString()))
              continue;
            ((TextView)localView.findViewById(R.id.coupon_getted_before)).setVisibility(4);
            ((ImageView)localView.findViewById(R.id.coupon_getted)).setVisibility(0);
            localView.setClickable(false);
            BeautyCouponAgent.this.couponViews.remove(localView);
          }
        }
      });
      return this.popup;
      localLinearLayout.setVisibility(8);
    }
  }

  private void displayDialogContent(LinearLayout paramLinearLayout, Map<String, Object> paramMap)
  {
    paramLinearLayout.findViewById(R.id.content_coupon_type).setVisibility(8);
    paramLinearLayout.findViewById(R.id.content_rules).setVisibility(8);
    paramLinearLayout.findViewById(R.id.content_valid_time).setVisibility(8);
    if ((paramMap.get("title") != null) && (paramMap.get("title").toString().trim().length() != 0))
    {
      ((TextView)paramLinearLayout.findViewById(R.id.coupon_type)).setText(paramMap.get("title").toString());
      paramLinearLayout.findViewById(R.id.content_coupon_type).setVisibility(0);
    }
    if ((paramMap.get("rule") != null) && (paramMap.get("rule").toString().trim().length() != 0))
    {
      ((TextView)paramLinearLayout.findViewById(R.id.rules)).setText(paramMap.get("rule").toString());
      paramLinearLayout.findViewById(R.id.content_rules).setVisibility(0);
    }
    if ((paramMap.get("deadLine") != null) && (paramMap.get("deadLine").toString().trim().length() != 0))
    {
      ((TextView)paramLinearLayout.findViewById(R.id.valid_time)).setText(paramMap.get("deadLine").toString());
      paramLinearLayout.findViewById(R.id.content_valid_time).setVisibility(0);
    }
  }

  private LinearLayout getCoupleCouponView(View paramView1, View paramView2, int paramInt)
  {
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    localLinearLayout.setOrientation(0);
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localLinearLayout.addView(paramView1);
    if (paramView2 == null)
    {
      localLinearLayout.addView((RelativeLayout)getCouponEmptyView(getContext(), paramInt));
      return localLinearLayout;
    }
    localLinearLayout.addView(paramView2);
    return localLinearLayout;
  }

  private View getCouponEmptyView(Context paramContext, int paramInt)
  {
    if (paramInt > 2)
      return LayoutInflater.from(paramContext).inflate(R.layout.shopinfo_beauty_coupon_empty_below, getParentView(), false);
    return LayoutInflater.from(paramContext).inflate(R.layout.shopinfo_beauty_coupon_empty, getParentView(), false);
  }

  private String getCouponIdByUrl(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      return "";
    paramString = paramString.split("\\?");
    if ((paramString == null) || (paramString.length < 2))
      return "";
    paramString = paramString[1].split("=");
    if ((paramString == null) || (paramString.length < 2))
      return "";
    return paramString[1];
  }

  private View getCouponView(Context paramContext, Map<String, Object> paramMap, int paramInt)
  {
    if (paramInt > 1)
    {
      paramContext = LayoutInflater.from(paramContext).inflate(R.layout.shopinfo_beauty_coupon_below, getParentView(), false);
      ((TextView)paramContext.findViewById(R.id.coupon_id)).setText(paramMap.get("id").toString());
      if (((Integer)paramMap.get("type")).intValue() != 0)
        break label241;
      ((TextView)paramContext.findViewById(R.id.coupon_getted_before)).setText(paramMap.get("context").toString());
      ((ImageView)paramContext.findViewById(R.id.coupon_getted)).setVisibility(8);
      paramContext.setOnClickListener(new View.OnClickListener(paramMap, paramContext)
      {
        public void onClick(View paramView)
        {
          BeautyCouponAgent.this.sendRequest(BeautyCouponAgent.this, this.val$info.get("id").toString());
          this.val$finalCouponView.setClickable(false);
          BeautyCouponAgent.this.couponViews.add(this.val$finalCouponView);
        }
      });
    }
    while (true)
    {
      ((TextView)paramContext.findViewById(R.id.coupon_money)).setText(paramMap.get("money").toString());
      ((TextView)paramContext.findViewById(R.id.coupon_topic)).setText(paramMap.get("topic").toString());
      ((TextView)paramContext.findViewById(R.id.coupon_desc)).setText(paramMap.get("desc").toString());
      paramMap = new GAUserInfo();
      paramMap.index = Integer.valueOf(paramInt);
      ((NovaRelativeLayout)paramContext).setGAString("shopinfo_beauty_coupon", paramMap);
      return paramContext;
      paramContext = LayoutInflater.from(paramContext).inflate(R.layout.shopinfo_beauty_coupon, getParentView(), false);
      break;
      label241: ((TextView)paramContext.findViewById(R.id.coupon_getted_before)).setText("");
    }
  }

  private void resumeCouponClickable()
  {
    Iterator localIterator = this.couponViews.iterator();
    while (localIterator.hasNext())
      ((View)localIterator.next()).setClickable(true);
    this.couponViews.clear();
  }

  private void sendRequest()
  {
    StringBuilder localStringBuilder = new StringBuilder("http://mapi.dianping.com/beauty/getcouponinfo.bin?");
    localStringBuilder.append("shopid=").append(shopId());
    this.getcouponinfoReq = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.getcouponinfoReq, this);
  }

  private void sendRequest(RequestHandler paramRequestHandler, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder("http://mapi.dianping.com/beauty/gaincoupon.bin?");
    localStringBuilder.append("id=").append(paramString);
    this.gaincouponReq = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.gaincouponReq, paramRequestHandler);
  }

  private Map<String, Object> transform(DPObject paramDPObject)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("type", Integer.valueOf(paramDPObject.getInt("Type")));
    localHashMap.put("context", paramDPObject.getString("Context"));
    localHashMap.put("money", paramDPObject.getString("Money"));
    localHashMap.put("desc", paramDPObject.getString("Desc"));
    localHashMap.put("topic", paramDPObject.getString("Topic"));
    localHashMap.put("id", paramDPObject.getString("Id"));
    return localHashMap;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (getShop() == null);
    ArrayList localArrayList;
    Object localObject;
    do
    {
      do
      {
        do
        {
          return;
          removeAllCells();
        }
        while ((getFragment() == null) || (this.couponInfos == null));
        paramBundle = this.couponInfos.getArray("CouponInfos");
      }
      while (paramBundle == null);
      localArrayList = new ArrayList();
      int j = paramBundle.length;
      i = 0;
      while (i < j)
      {
        localObject = paramBundle[i];
        if (localObject != null)
          localArrayList.add(transform((DPObject)localObject));
        i += 1;
      }
    }
    while (paramBundle.length == 0);
    LinearLayout localLinearLayout = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_beauty_coupon_comb, getParentView(), false);
    paramBundle = null;
    int i = 0;
    if (i < localArrayList.size())
    {
      localObject = (Map)localArrayList.get(i);
      localObject = (NovaRelativeLayout)getCouponView(getContext(), (Map)localObject, i);
      if (i % 2 == 1)
        localLinearLayout.addView(getCoupleCouponView(paramBundle, (View)localObject, localArrayList.size()));
      for (paramBundle = null; ; paramBundle = (Bundle)localObject)
      {
        i += 1;
        break;
      }
    }
    if (paramBundle != null)
      localLinearLayout.addView(getCoupleCouponView(paramBundle, null, localArrayList.size()));
    addCell("0200Basic.33Coupon", localLinearLayout, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onDestroy()
  {
    if (this.getcouponinfoReq != null)
    {
      getFragment().mapiService().abort(this.getcouponinfoReq, this, true);
      this.getcouponinfoReq = null;
    }
    if (this.gaincouponReq != null)
    {
      getFragment().mapiService().abort(this.gaincouponReq, this, true);
      this.gaincouponReq = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.getcouponinfoReq == paramMApiRequest)
      this.getcouponinfoReq = null;
    if (this.gaincouponReq == paramMApiRequest)
      this.gaincouponReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getcouponinfoReq)
    {
      this.couponInfos = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
    }
    Object localObject;
    while (true)
    {
      return;
      continue;
      continue;
      break label215;
      if (paramMApiRequest != this.gaincouponReq)
        continue;
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      if (paramMApiResponse == null)
        continue;
      localObject = new HashMap();
      ((Map)localObject).put("title", paramMApiResponse.getString("Title"));
      ((Map)localObject).put("rule", paramMApiResponse.getString("Rule"));
      ((Map)localObject).put("deadLine", paramMApiResponse.getString("Deadline"));
      ((Map)localObject).put("result", Integer.valueOf(paramMApiResponse.getInt("Result")));
      if (((Integer)((Map)localObject).get("result")).intValue() == 0)
      {
        accountService().login(new LoginResultListener()
        {
          public void onLoginCancel(AccountService paramAccountService)
          {
            BeautyCouponAgent.this.resumeCouponClickable();
          }

          public void onLoginSuccess(AccountService paramAccountService)
          {
            BeautyCouponAgent.this.resumeCouponClickable();
          }
        });
        return;
      }
      if (((Integer)((Map)localObject).get("result")).intValue() != 2)
        break;
      Toast.makeText(getContext(), "领取失败", 0).show();
      paramMApiRequest = getCouponIdByUrl(paramMApiRequest.url());
      paramMApiResponse = this.couponViews.iterator();
      label215: if (!paramMApiResponse.hasNext())
        continue;
      localObject = (View)paramMApiResponse.next();
      if (!paramMApiRequest.equals(((TextView)((View)localObject).findViewById(R.id.coupon_id)).getText().toString()))
        continue;
      ((View)localObject).setClickable(true);
      this.couponViews.remove(localObject);
      return;
    }
    buildDialog((Map)localObject, getCouponIdByUrl(paramMApiRequest.url()));
    this.popup.show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.beauty.hair.BeautyCouponAgent
 * JD-Core Version:    0.6.0
 */