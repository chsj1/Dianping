package com.dianping.selectdish;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.view.Window;
import android.widget.EditText;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.model.GiftInfo;
import com.dianping.selectdish.model.SetItem;
import com.dianping.selectdish.ui.SelectDishesCaputreActivity;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubmitOrderManager
  implements FullRequestHandle<MApiRequest, MApiResponse>
{
  private static MApiRequest mSubmitRequest;
  private NewCartManager mCartManager = NewCartManager.getInstance();
  private Context mContext;
  private ProgressDialog mSubmitDialog;
  private SubmitOrderInfo mSubmitOrderInfo;

  private void abort()
  {
    if (mSubmitRequest != null)
    {
      DPApplication.instance().mapiService().abort(mSubmitRequest, this, true);
      mSubmitRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == mSubmitRequest)
    {
      mSubmitRequest = null;
      if ((this.mSubmitDialog != null) && (this.mSubmitDialog.isShowing()))
        this.mSubmitDialog.dismiss();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    int i;
    Object localObject1;
    Object localObject2;
    int j;
    int k;
    if (paramMApiRequest == mSubmitRequest)
    {
      mSubmitRequest = null;
      if ((this.mSubmitDialog != null) && (this.mSubmitDialog.isShowing()))
        this.mSubmitDialog.dismiss();
      paramMApiRequest = paramMApiResponse.result();
      if ((paramMApiRequest instanceof DPObject))
      {
        paramMApiResponse = (DPObject)paramMApiRequest;
        i = paramMApiResponse.getInt("Code");
        localObject1 = paramMApiResponse.getString("Title");
        localObject2 = paramMApiResponse.getString("Msg");
        j = paramMApiResponse.getInt("OrderId");
        double d1 = paramMApiResponse.getDouble("NoHuiPay");
        double d2 = paramMApiResponse.getDouble("HuiPay");
        k = paramMApiResponse.getInt("OldOrderId");
        paramMApiRequest = paramMApiResponse.getArray("UpdatedDishInfos");
        paramMApiResponse = paramMApiResponse.getArray("UpdatedGiftInfos");
        if ((i != 200) && (i != 250))
          break label395;
        if ((i == 250) || (!this.mSubmitOrderInfo.supportPrePay))
          break label342;
        paramMApiRequest = Uri.parse("dianping://selectdishorderresult").buildUpon();
        paramMApiRequest.appendQueryParameter("orderid", String.valueOf(j));
        paramMApiRequest.appendQueryParameter("querytype", "20");
        paramMApiResponse = Uri.parse("dianping://huiunifiedcashier").buildUpon();
        paramMApiResponse.appendQueryParameter("successurl", paramMApiRequest.build().toString());
        paramMApiResponse.appendQueryParameter("failureurl", paramMApiRequest.build().toString());
        paramMApiResponse.appendQueryParameter("bizorderid", String.valueOf(j));
        paramMApiResponse.appendQueryParameter("bizordertype", "20");
        paramMApiResponse.appendQueryParameter("shopname", this.mSubmitOrderInfo.shopName);
        paramMApiResponse.appendQueryParameter("shopid", String.valueOf(this.mSubmitOrderInfo.shopId));
        paramMApiResponse.appendQueryParameter("amount", String.valueOf(d1 + d2));
        paramMApiResponse.appendQueryParameter("nodiscountamount", String.valueOf(d1));
        paramMApiResponse.appendQueryParameter("config", "{supportVoucher:1}");
        paramMApiResponse.appendQueryParameter("amountlocked", "1");
        paramMApiRequest = new Intent("android.intent.action.VIEW", paramMApiResponse.build());
        this.mContext.startActivity(paramMApiRequest);
      }
    }
    while (true)
    {
      return;
      label342: paramMApiRequest = Uri.parse("dianping://selectdishorderresult").buildUpon();
      paramMApiRequest.appendQueryParameter("orderid", String.valueOf(j));
      paramMApiRequest.appendQueryParameter("querytype", "20");
      paramMApiRequest = new Intent("android.intent.action.VIEW", paramMApiRequest.build());
      this.mContext.startActivity(paramMApiRequest);
      return;
      label395: NewCartManager.getInstance().storeCart();
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.mContext);
      localBuilder.setTitle((CharSequence)localObject1);
      localBuilder.setMessage((CharSequence)localObject2);
      localBuilder.setCancelable(false);
      if (i == 100)
      {
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(new Intent("com.dianping.selectdish.resetmenu"));
        if ((paramMApiRequest != null) || (paramMApiResponse != null))
        {
          if (paramMApiRequest != null)
          {
            i = paramMApiRequest.length;
            localObject1 = new CartItem[i];
            if (paramMApiResponse == null)
              break label570;
          }
          label570: for (i = paramMApiResponse.length; ; i = 0)
          {
            localObject2 = new CartFreeItem[i];
            i = 0;
            while (i < localObject1.length)
            {
              DishInfo localDishInfo = new DishInfo(paramMApiRequest[i]);
              localDishInfo.saleTime = paramMApiRequest[i].getString("PackageDishSaleTime");
              localObject1[i] = new CartItem(localDishInfo);
              i += 1;
            }
            i = 0;
            break;
          }
          i = 0;
          if (i < localObject2.length)
          {
            localObject2[i] = new CartFreeItem(new GiftInfo(paramMApiResponse[i]));
            j = paramMApiResponse[i].getInt("Type");
            if (j == 10)
              localObject2[i].soldout = true;
            while (true)
            {
              i += 1;
              break;
              if (j != 30)
                continue;
              localObject2[i].expired = true;
            }
          }
          NewCartManager.getInstance().correctAll(localObject1, localObject2);
        }
        localBuilder.setPositiveButton(R.string.sd_return_to_cart, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            if ((SubmitOrderManager.this.mContext instanceof SelectDishesCaputreActivity))
              ((SelectDishesCaputreActivity)SubmitOrderManager.this.mContext).finish();
          }
        });
      }
      while (localBuilder != null)
      {
        localBuilder.show();
        return;
        if (i == 120)
        {
          localBuilder.setTitle(R.string.sd_submitorder_tableid_error);
          localBuilder.setMessage(R.string.sd_submitorder_tableid_error_hint);
          localBuilder.setNegativeButton(R.string.cancel, null);
          localBuilder.setPositiveButton(R.string.sd_retype_tableid, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              SubmitOrderManager.this.mSubmitOrderInfo.tableId = null;
              SubmitOrderManager.this.mSubmitOrderInfo.needRetypeTableId = true;
              SubmitOrderManager.this.submit(SubmitOrderManager.this.mContext, SubmitOrderManager.this.mSubmitOrderInfo);
            }
          });
          continue;
        }
        if (i == 121)
        {
          localBuilder.setTitle(R.string.sd_submitorder_order_exist);
          localBuilder.setMessage(R.string.sd_submitorder_order_exist_hint);
          localBuilder.setPositiveButton(R.string.sd_add_more_dishes, new DialogInterface.OnClickListener(k)
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              GAHelper.instance().contextStatisticsEvent(SubmitOrderManager.this.mContext, "menuorder_tableconfirm_add", null, "tap");
              SubmitOrderManager.this.mSubmitOrderInfo.orderId = String.valueOf(this.val$oldid);
              SubmitOrderManager.this.submit(SubmitOrderManager.this.mContext, SubmitOrderManager.this.mSubmitOrderInfo);
            }
          });
          localBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              if ((SubmitOrderManager.this.mContext instanceof Activity))
                ((Activity)SubmitOrderManager.this.mContext).finish();
            }
          });
          continue;
        }
        if (i == 122)
        {
          localBuilder.setTitle(R.string.sd_submitorder_close);
          localBuilder.setMessage(R.string.sd_submitorder_close_hint);
          localBuilder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
            }
          });
          continue;
        }
        localBuilder.setMessage(R.string.sd_submitorder_error);
        localBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            if ((SubmitOrderManager.this.mContext instanceof SelectDishesCaputreActivity))
              ((Activity)SubmitOrderManager.this.mContext).finish();
          }
        });
      }
    }
  }

  public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
  {
  }

  public void onRequestStart(MApiRequest paramMApiRequest)
  {
    if (paramMApiRequest == mSubmitRequest)
    {
      if ((this.mSubmitDialog != null) && (this.mSubmitDialog.isShowing()))
        this.mSubmitDialog.dismiss();
      this.mSubmitDialog = new ProgressDialog(this.mContext);
      this.mSubmitDialog.setMessage(this.mContext.getString(R.string.sd_loading_submiting));
      this.mSubmitDialog.setCancelable(false);
      this.mSubmitDialog.show();
    }
  }

  public void submit(Context paramContext)
  {
    JSONObject localJSONObject1 = new JSONObject();
    Object localObject2 = new JSONArray();
    JSONArray localJSONArray1 = new JSONArray();
    Object localObject3;
    JSONObject localJSONObject2;
    JSONArray localJSONArray2;
    try
    {
      localJSONObject1.put("dishInfos", localObject2);
      localJSONObject1.put("giftInfos", localJSONArray1);
      localObject3 = this.mCartManager.getAllDishes().iterator();
      if (((Iterator)localObject3).hasNext())
      {
        Object localObject4 = (CartItem)((Iterator)localObject3).next();
        localJSONObject2 = new JSONObject();
        localJSONObject2.put("dishId", ((CartItem)localObject4).dishInfo.dishId);
        localJSONObject2.put("dishName", ((CartItem)localObject4).dishInfo.name);
        localJSONObject2.put("originPrice", ((CartItem)localObject4).dishInfo.oldPriceForSubmit);
        localJSONObject2.put("price", ((CartItem)localObject4).dishInfo.currentPrice);
        localJSONObject2.put("count", ((CartItem)localObject4).getItemCount());
        localJSONObject2.put("dishType", ((CartItem)localObject4).dishInfo.dishType);
        if (((CartItem)localObject4).dishInfo.dishType == 1)
        {
          localJSONArray2 = new JSONArray();
          localObject4 = ((CartItem)localObject4).dishInfo.setItems.iterator();
          while (((Iterator)localObject4).hasNext())
          {
            SetItem localSetItem = (SetItem)((Iterator)localObject4).next();
            JSONObject localJSONObject3 = new JSONObject();
            localJSONObject3.put("dishId", localSetItem.id);
            localJSONObject3.put("dishName", localSetItem.name);
            localJSONObject3.put("price", localSetItem.originPrice);
            localJSONObject3.put("count", localSetItem.count);
            localJSONObject3.put("unit", localSetItem.unit);
            localJSONArray2.put(localJSONObject3);
          }
        }
      }
    }
    catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
    while (true)
    {
      Object localObject1 = new SubmitOrderInfo();
      ((SubmitOrderInfo)localObject1).shopName = this.mCartManager.getShopName();
      ((SubmitOrderInfo)localObject1).shopId = this.mCartManager.getShopId();
      ((SubmitOrderInfo)localObject1).tableId = this.mCartManager.tableId;
      ((SubmitOrderInfo)localObject1).orderId = this.mCartManager.orderId;
      ((SubmitOrderInfo)localObject1).supportPrePay = this.mCartManager.supportPrepay;
      ((SubmitOrderInfo)localObject1).supportTable = this.mCartManager.supportTable;
      ((SubmitOrderInfo)localObject1).total = String.valueOf(this.mCartManager.getTotalPrice());
      ((SubmitOrderInfo)localObject1).oldtotal = String.valueOf(this.mCartManager.getTotalOriginPrice());
      ((SubmitOrderInfo)localObject1).cartInfo = localJSONObject1.toString();
      submit(paramContext, (SubmitOrderInfo)localObject1);
      return;
      localJSONObject2.put("packageDishInfos", localJSONArray2);
      ((JSONArray)localObject2).put(localJSONObject2);
      break;
      localObject2 = this.mCartManager.getAllFreeDishes().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (CartFreeItem)((Iterator)localObject2).next();
        if ((!((CartFreeItem)localObject3).use) || (((CartFreeItem)localObject3).soldout))
          continue;
        localJSONObject2 = new JSONObject();
        localJSONObject2.put("dishId", ((CartFreeItem)localObject3).giftInfo.dishId);
        localJSONObject2.put("dishName", ((CartFreeItem)localObject3).giftInfo.name);
        localJSONObject2.put("giftId", ((CartFreeItem)localObject3).giftInfo.giftId);
        localJSONObject2.put("actId", ((CartFreeItem)localObject3).giftInfo.activityId);
        ((JSONArray)localObject1).put(localJSONObject2);
      }
      if (this.mCartManager.groupOnDealId == 0)
        continue;
      localObject1 = new JSONObject();
      ((JSONObject)localObject1).put("dealId", this.mCartManager.groupOnDealId);
      ((JSONObject)localObject1).put("orderId", this.mCartManager.groupOnOrderId);
      localJSONObject1.put("groupOnInfo", localObject1);
    }
  }

  public void submit(Context paramContext, SubmitOrderInfo paramSubmitOrderInfo)
  {
    this.mContext = paramContext;
    this.mSubmitOrderInfo = paramSubmitOrderInfo;
    if (this.mSubmitOrderInfo.supportTable)
    {
      if (this.mSubmitOrderInfo.needRetypeTableId)
      {
        this.mSubmitOrderInfo.needRetypeTableId = false;
        if (!(paramContext instanceof SelectDishesCaputreActivity))
        {
          paramContext = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishscan"));
          this.mContext.startActivity(paramContext);
        }
        return;
      }
      if (this.mSubmitOrderInfo.tableId == null)
      {
        if ((paramContext instanceof SelectDishesCaputreActivity))
        {
          paramSubmitOrderInfo = new AlertDialog.Builder(paramContext);
          paramSubmitOrderInfo.setTitle(R.string.sd_input_table);
          EditText localEditText = new EditText(paramContext);
          localEditText.setGravity(16);
          localEditText.setHint(R.string.sd_input_table_hint);
          localEditText.setBackgroundDrawable(null);
          localEditText.setLines(1);
          localEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(16) });
          localEditText.setFocusable(true);
          localEditText.setFocusableInTouchMode(true);
          localEditText.requestFocus();
          paramSubmitOrderInfo.setView(localEditText);
          paramSubmitOrderInfo.setNegativeButton(R.string.cancel, null);
          paramSubmitOrderInfo.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(localEditText)
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              if (!TextUtils.isEmpty(this.val$input.getText().toString()))
              {
                SubmitOrderManager.this.mSubmitOrderInfo.tableId = this.val$input.getText().toString().trim();
                SubmitOrderManager.this.submit(SubmitOrderManager.this.mContext, SubmitOrderManager.this.mSubmitOrderInfo);
              }
            }
          });
          paramSubmitOrderInfo = paramSubmitOrderInfo.create();
          paramSubmitOrderInfo.setOnDismissListener(new DialogInterface.OnDismissListener(paramContext)
          {
            public void onDismiss(DialogInterface paramDialogInterface)
            {
              ((SelectDishesCaputreActivity)this.val$context).setScanClosed(false);
            }
          });
          paramSubmitOrderInfo.getWindow().clearFlags(131080);
          paramSubmitOrderInfo.getWindow().setSoftInputMode(4);
          paramSubmitOrderInfo.show();
          return;
        }
        paramContext = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishscan"));
        this.mContext.startActivity(paramContext);
        return;
      }
    }
    abort();
    paramContext = Uri.parse("http://m.api.dianping.com/orderdish/postorder.bin").buildUpon();
    paramSubmitOrderInfo = new ArrayList();
    paramSubmitOrderInfo.add("callid");
    paramSubmitOrderInfo.add(UUID.randomUUID().toString());
    if (this.mSubmitOrderInfo.supportTable)
    {
      paramSubmitOrderInfo.add("tableid");
      paramSubmitOrderInfo.add(this.mSubmitOrderInfo.tableId);
    }
    paramSubmitOrderInfo.add("orderid");
    paramSubmitOrderInfo.add(this.mSubmitOrderInfo.orderId);
    paramSubmitOrderInfo.add("total");
    paramSubmitOrderInfo.add(this.mSubmitOrderInfo.total);
    paramSubmitOrderInfo.add("orgtotal");
    paramSubmitOrderInfo.add(this.mSubmitOrderInfo.oldtotal);
    paramSubmitOrderInfo.add("cartinfo");
    paramSubmitOrderInfo.add(this.mSubmitOrderInfo.cartInfo);
    paramSubmitOrderInfo.add("shopid");
    paramSubmitOrderInfo.add(String.valueOf(this.mSubmitOrderInfo.shopId));
    paramSubmitOrderInfo.add("shopname");
    paramSubmitOrderInfo.add(this.mSubmitOrderInfo.shopName);
    mSubmitRequest = BasicMApiRequest.mapiPost(paramContext.toString(), (String[])paramSubmitOrderInfo.toArray(new String[paramSubmitOrderInfo.size()]));
    DPApplication.instance().mapiService().exec(mSubmitRequest, this);
  }

  public static class SubmitOrderInfo
  {
    public String cartInfo;
    public boolean needRetypeTableId = false;
    public String oldtotal;
    public String orderId;
    public int shopId;
    public String shopName;
    public boolean supportPrePay;
    public boolean supportTable;
    public String tableId;
    public String total;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.SubmitOrderManager
 * JD-Core Version:    0.6.0
 */