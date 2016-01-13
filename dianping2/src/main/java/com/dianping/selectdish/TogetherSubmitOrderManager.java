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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TogetherSubmitOrderManager
  implements FullRequestHandle<MApiRequest, MApiResponse>
{
  private static MApiRequest mSubmitRequest;
  private TogetherCartManager mCartManager = TogetherCartManager.getInstance();
  private Context mContext;
  private ProgressDialog mSubmitDialog;
  private TogetherSubmitOrderInfo mTogetherSubmitOrderInfo;

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
        paramMApiResponse.getInt("OldOrderId");
        paramMApiRequest = paramMApiResponse.getArray("UpdatedDishInfos");
        paramMApiResponse = paramMApiResponse.getArray("UpdatedGiftInfos");
        if ((i != 200) && (i != 250))
          break label394;
        if ((i == 250) || (!this.mTogetherSubmitOrderInfo.supportPrePay))
          break label341;
        paramMApiRequest = Uri.parse("dianping://selectdishorderresult").buildUpon();
        paramMApiRequest.appendQueryParameter("orderid", String.valueOf(j));
        paramMApiRequest.appendQueryParameter("querytype", "20");
        paramMApiResponse = Uri.parse("dianping://huiunifiedcashier").buildUpon();
        paramMApiResponse.appendQueryParameter("successurl", paramMApiRequest.build().toString());
        paramMApiResponse.appendQueryParameter("failureurl", paramMApiRequest.build().toString());
        paramMApiResponse.appendQueryParameter("bizorderid", String.valueOf(j));
        paramMApiResponse.appendQueryParameter("bizordertype", "20");
        paramMApiResponse.appendQueryParameter("shopname", this.mTogetherSubmitOrderInfo.shopName);
        paramMApiResponse.appendQueryParameter("shopid", String.valueOf(this.mTogetherSubmitOrderInfo.shopId));
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
      label341: paramMApiRequest = Uri.parse("dianping://selectdishorderresult").buildUpon();
      paramMApiRequest.appendQueryParameter("orderid", String.valueOf(j));
      paramMApiRequest.appendQueryParameter("querytype", "20");
      paramMApiRequest = new Intent("android.intent.action.VIEW", paramMApiRequest.build());
      this.mContext.startActivity(paramMApiRequest);
      return;
      label394: AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.mContext);
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
              break label563;
          }
          label563: for (i = paramMApiResponse.length; ; i = 0)
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
          TogetherCartManager.getInstance().correctAll(localObject1, localObject2);
        }
        localBuilder.setPositiveButton(R.string.sd_return_to_cart, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            if ((TogetherSubmitOrderManager.this.mContext instanceof SelectDishesCaputreActivity))
              ((SelectDishesCaputreActivity)TogetherSubmitOrderManager.this.mContext).finish();
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
              TogetherSubmitOrderManager.this.mTogetherSubmitOrderInfo.tableId = null;
              TogetherSubmitOrderManager.this.mTogetherSubmitOrderInfo.needRetypeTableId = true;
              TogetherSubmitOrderManager.this.submit(TogetherSubmitOrderManager.this.mContext, TogetherSubmitOrderManager.this.mTogetherSubmitOrderInfo);
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
            if ((TogetherSubmitOrderManager.this.mContext instanceof SelectDishesCaputreActivity))
              ((Activity)TogetherSubmitOrderManager.this.mContext).finish();
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
    Object localObject1 = new JSONArray();
    JSONArray localJSONArray1 = new JSONArray();
    Object localObject2;
    JSONObject localJSONObject2;
    JSONArray localJSONArray2;
    try
    {
      localJSONObject1.put("dishInfos", localObject1);
      localJSONObject1.put("giftInfos", localJSONArray1);
      localObject2 = this.mCartManager.getAllDishesinTotalDish().iterator();
      if (((Iterator)localObject2).hasNext())
      {
        Object localObject3 = (CartItem)((Iterator)localObject2).next();
        localJSONObject2 = new JSONObject();
        localJSONObject2.put("dishId", ((CartItem)localObject3).dishInfo.dishId);
        localJSONObject2.put("dishName", ((CartItem)localObject3).dishInfo.name);
        localJSONObject2.put("originPrice", ((CartItem)localObject3).dishInfo.oldPriceForSubmit);
        localJSONObject2.put("price", ((CartItem)localObject3).dishInfo.currentPrice);
        localJSONObject2.put("count", ((CartItem)localObject3).getItemCount());
        localJSONObject2.put("dishType", ((CartItem)localObject3).dishInfo.dishType);
        if (((CartItem)localObject3).dishInfo.dishType == 1)
        {
          localJSONArray2 = new JSONArray();
          localObject3 = ((CartItem)localObject3).dishInfo.setItems.iterator();
          while (((Iterator)localObject3).hasNext())
          {
            SetItem localSetItem = (SetItem)((Iterator)localObject3).next();
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
      TogetherSubmitOrderInfo localTogetherSubmitOrderInfo = new TogetherSubmitOrderInfo();
      localTogetherSubmitOrderInfo.shopName = this.mCartManager.getShopName();
      localTogetherSubmitOrderInfo.shopId = this.mCartManager.getShopId();
      localTogetherSubmitOrderInfo.tableId = this.mCartManager.tableId;
      localTogetherSubmitOrderInfo.roomId = this.mCartManager.roomId;
      localTogetherSubmitOrderInfo.supportPrePay = this.mCartManager.supportPrepay;
      localTogetherSubmitOrderInfo.supportTable = this.mCartManager.supportTable;
      localTogetherSubmitOrderInfo.total = String.valueOf(this.mCartManager.getTotalPrice());
      localTogetherSubmitOrderInfo.oldtotal = String.valueOf(this.mCartManager.getTotalOriginPrice());
      localTogetherSubmitOrderInfo.cartInfo = localJSONObject1.toString();
      submit(paramContext, localTogetherSubmitOrderInfo);
      return;
      localJSONObject2.put("packageDishInfos", localJSONArray2);
      ((JSONArray)localObject1).put(localJSONObject2);
      break;
      localObject1 = this.mCartManager.getAllFreeDishes().iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (CartFreeItem)((Iterator)localObject1).next();
        if ((!((CartFreeItem)localObject2).use) || (((CartFreeItem)localObject2).soldout))
          continue;
        localJSONObject2 = new JSONObject();
        localJSONObject2.put("dishId", ((CartFreeItem)localObject2).giftInfo.dishId);
        localJSONObject2.put("dishName", ((CartFreeItem)localObject2).giftInfo.name);
        localJSONObject2.put("giftId", ((CartFreeItem)localObject2).giftInfo.giftId);
        localJSONObject2.put("actId", ((CartFreeItem)localObject2).giftInfo.activityId);
        localTogetherSubmitOrderInfo.put(localJSONObject2);
      }
    }
  }

  public void submit(Context paramContext, TogetherSubmitOrderInfo paramTogetherSubmitOrderInfo)
  {
    this.mContext = paramContext;
    this.mTogetherSubmitOrderInfo = paramTogetherSubmitOrderInfo;
    if (this.mTogetherSubmitOrderInfo.supportTable)
    {
      if (this.mTogetherSubmitOrderInfo.needRetypeTableId)
      {
        this.mTogetherSubmitOrderInfo.needRetypeTableId = false;
        if (!(paramContext instanceof SelectDishesCaputreActivity))
        {
          paramContext = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishscan?istogethersd=1"));
          this.mContext.startActivity(paramContext);
        }
        return;
      }
      if (this.mTogetherSubmitOrderInfo.tableId == null)
      {
        if ((paramContext instanceof SelectDishesCaputreActivity))
        {
          paramTogetherSubmitOrderInfo = new AlertDialog.Builder(paramContext);
          paramTogetherSubmitOrderInfo.setTitle(R.string.sd_input_table);
          EditText localEditText = new EditText(paramContext);
          localEditText.setGravity(16);
          localEditText.setHint(R.string.sd_input_table_hint);
          localEditText.setBackgroundDrawable(null);
          localEditText.setLines(1);
          localEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(16) });
          localEditText.setFocusable(true);
          localEditText.setFocusableInTouchMode(true);
          localEditText.requestFocus();
          paramTogetherSubmitOrderInfo.setView(localEditText);
          paramTogetherSubmitOrderInfo.setNegativeButton(R.string.cancel, null);
          paramTogetherSubmitOrderInfo.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(localEditText)
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              if (!TextUtils.isEmpty(this.val$input.getText().toString()))
              {
                TogetherSubmitOrderManager.this.mTogetherSubmitOrderInfo.tableId = this.val$input.getText().toString().trim();
                TogetherSubmitOrderManager.this.submit(TogetherSubmitOrderManager.this.mContext, TogetherSubmitOrderManager.this.mTogetherSubmitOrderInfo);
              }
            }
          });
          paramTogetherSubmitOrderInfo = paramTogetherSubmitOrderInfo.create();
          paramTogetherSubmitOrderInfo.setOnDismissListener(new DialogInterface.OnDismissListener(paramContext)
          {
            public void onDismiss(DialogInterface paramDialogInterface)
            {
              ((SelectDishesCaputreActivity)this.val$context).setScanClosed(false);
            }
          });
          paramTogetherSubmitOrderInfo.getWindow().clearFlags(131080);
          paramTogetherSubmitOrderInfo.getWindow().setSoftInputMode(4);
          paramTogetherSubmitOrderInfo.show();
          return;
        }
        paramContext = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishscan?istogethersd=1"));
        this.mContext.startActivity(paramContext);
        return;
      }
    }
    abort();
    paramContext = Uri.parse("http://m.api.dianping.com/orderdish/postorder.bin").buildUpon();
    paramTogetherSubmitOrderInfo = new ArrayList();
    paramTogetherSubmitOrderInfo.add("callid");
    paramTogetherSubmitOrderInfo.add(UUID.randomUUID().toString());
    if (this.mTogetherSubmitOrderInfo.supportTable)
    {
      paramTogetherSubmitOrderInfo.add("tableid");
      paramTogetherSubmitOrderInfo.add(this.mTogetherSubmitOrderInfo.tableId);
    }
    paramTogetherSubmitOrderInfo.add("roomid");
    paramTogetherSubmitOrderInfo.add(String.valueOf(this.mTogetherSubmitOrderInfo.roomId));
    paramTogetherSubmitOrderInfo.add("total");
    paramTogetherSubmitOrderInfo.add(this.mTogetherSubmitOrderInfo.total);
    paramTogetherSubmitOrderInfo.add("orgtotal");
    paramTogetherSubmitOrderInfo.add(this.mTogetherSubmitOrderInfo.oldtotal);
    paramTogetherSubmitOrderInfo.add("cartinfo");
    paramTogetherSubmitOrderInfo.add(this.mTogetherSubmitOrderInfo.cartInfo);
    paramTogetherSubmitOrderInfo.add("shopid");
    paramTogetherSubmitOrderInfo.add(String.valueOf(this.mTogetherSubmitOrderInfo.shopId));
    paramTogetherSubmitOrderInfo.add("shopname");
    paramTogetherSubmitOrderInfo.add(this.mTogetherSubmitOrderInfo.shopName);
    mSubmitRequest = BasicMApiRequest.mapiPost(paramContext.toString(), (String[])paramTogetherSubmitOrderInfo.toArray(new String[paramTogetherSubmitOrderInfo.size()]));
    DPApplication.instance().mapiService().exec(mSubmitRequest, this);
  }

  public static class TogetherSubmitOrderInfo
  {
    public String cartInfo;
    public boolean needRetypeTableId = false;
    public String oldtotal;
    public int roomId;
    public int shopId;
    public String shopName;
    public boolean supportPrePay;
    public boolean supportTable;
    public String tableId;
    public String total;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.TogetherSubmitOrderManager
 * JD-Core Version:    0.6.0
 */