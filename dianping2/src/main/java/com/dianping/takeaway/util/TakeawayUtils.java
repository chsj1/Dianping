package com.dianping.takeaway.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dianping.util.telephone.ContactUtils;
import com.dianping.v1.R.layout;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TakeawayUtils
{
  public static final int EVENT_TAKEAWAY_ORDER_UPDATED = 0;
  public static DecimalFormat PRICE_DF = new DecimalFormat("#.##");

  public static String bigDecimalTrailingZerosToString(BigDecimal paramBigDecimal)
  {
    return paramBigDecimal.stripTrailingZeros().toPlainString();
  }

  public static void callCancelOrderPhone(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    if (!TextUtils.isEmpty(paramString1))
    {
      localArrayList1.add("联系餐厅");
      localArrayList2.add(paramString1);
    }
    if ((!TextUtils.isEmpty(paramString3)) && (!TextUtils.isEmpty(paramString4)))
    {
      localArrayList1.add(paramString3);
      localArrayList2.add(paramString4);
    }
    if (!TextUtils.isEmpty(paramString2))
    {
      localArrayList1.add("联系大众点评客服");
      localArrayList2.add(paramString2);
    }
    callPhones(paramContext, localArrayList2, new DialogAdapter(paramContext, localArrayList1), paramString5);
  }

  public static void callPhones(Context paramContext, List<String> paramList, DialogAdapter paramDialogAdapter, String paramString)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
    if (!TextUtils.isEmpty(paramString))
      localBuilder.setTitle(paramString);
    localBuilder.setAdapter(paramDialogAdapter, new DialogInterface.OnClickListener(paramList, paramContext)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if ((paramInt < this.val$phoneNos.size()) && (!TextUtils.isEmpty((CharSequence)this.val$phoneNos.get(paramInt))))
          ContactUtils.dial(this.val$context, (String)this.val$phoneNos.get(paramInt));
      }
    });
    paramContext = localBuilder.create();
    paramContext.setCanceledOnTouchOutside(true);
    paramContext.show();
  }

  public static void callShopPhones(Context paramContext, String[] paramArrayOfString)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
      return;
    ArrayList localArrayList = new ArrayList();
    int j = paramArrayOfString.length;
    int i = 0;
    while (i < j)
    {
      String str = paramArrayOfString[i];
      localArrayList.add("拨打电话: " + str);
      i += 1;
    }
    callPhones(paramContext, Arrays.asList(paramArrayOfString), new DialogAdapter(paramContext, localArrayList), "联系商户");
  }

  static class DialogAdapter extends BaseAdapter
  {
    private Context context;
    private List<String> dataList;

    DialogAdapter(Context paramContext, List<String> paramList)
    {
      this.context = paramContext;
      this.dataList = paramList;
    }

    public int getCount()
    {
      return this.dataList.size();
    }

    public Object getItem(int paramInt)
    {
      return this.dataList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = LayoutInflater.from(this.context).inflate(R.layout.dialog_items, null);
      ((TextView)paramViewGroup).setText((CharSequence)this.dataList.get(paramInt));
      return paramViewGroup;
    }
  }

  public class broadcast
  {
    public static final String UPDATE_ADDRESS_LIST = "com.dianping.takeaway.UPDATE_ADDRESS_LIST";
    public static final String UPDATE_ORDER = "com.dianping.takeaway.UPDATE_ORDER";

    public broadcast()
    {
    }
  }

  public class message
  {
    public static final String DELIVERY_ADDRESS_DELETED = "DELIVERY_ADDRESS_DELETED";
    public static final String DELIVERY_LOAD_ORDER_SUCCESS = "DELIVERY_LOAD_ORDER_SUCCESS";

    public message()
    {
    }
  }

  public class request
  {
    public static final String ADDRESS_LIST = "http://waimai.api.dianping.com/getaddresslist.ta";
    public static final String ADDRESS_SUGGEST_REQUEST = "http://waimai.api.dianping.com/addresssuggest.ta?";
    public static final String ADDRESS_VALIDATE_REQUEST = "http://waimai.api.dianping.com/parseaddress.ta?";
    public static final String ADD_REVIEW_REQUEST = "http://waimai.api.dianping.com/review.ta";
    public static final String CANCEL_ORDER_REQUEST = "http://waimai.api.dianping.com/cancelorder.ta";
    public static final String CHANGE_USERCONTACT_REQUEST = "http://waimai.api.dianping.com/changeusercontact.ta";
    public static final String CONFIRM_ORDER_REQUEST = "http://waimai.api.dianping.com/confirmorderinfo.ta";
    public static final String CONFIRM_RECEIPT_REQUEST = "http://waimai.api.dianping.com/arrived.ta";
    public static final String CREATE_ORDER_REQUEST = "http://waimai.api.dianping.com/createorder.ta";
    public static final String DISH_LIST_REQUEST = "http://waimai.api.dianping.com/dishlist.ta?";
    public static final String ELEME_COUPON_REQUEST = "http://waimai.api.dianping.com/getelemecoupon.ta";
    public static final String ELEME_VERIFY_PHONE_REQUEST = "http://waimai.api.dianping.com/thirdpartyverifyphone.ta";
    public static final String EVALUATION_DISH_LIST_REQUEST = "http://waimai.api.dianping.com/reviewconfirm.ta";
    public static final String LOCATE_REQUEST = "http://waimai.api.dianping.com/getlocation.ta?";
    public static final String MAXTIME_REQUEST = "http://waimai.api.dianping.com/maxarrivedtime.ta";
    public static final String MY_TAKEAWAY_REQUEST = "http://waimai.api.dianping.com/mytakeaway.ta?";
    public static final String NEARBY_SHOP_LIST_REQUEST = "http://waimai.api.dianping.com/nearbyshoplist.ta?";
    public static final String ORDER_DETAIL_REQUEST = "http://waimai.api.dianping.com/orderdetail.ta";
    public static final String POI_SUGGEST_REQUEST = "http://waimai.api.dianping.com/getsuggestpoilist.ta";
    public static final String REQUEST_BASE = "http://waimai.api.dianping.com/";
    public static final String SEND_ELEME_VERIFY_CODE_REQUEST = "http://waimai.api.dianping.com/thirdpartysendverifycode.ta";
    public static final String SEND_VERIFY_CODE_REQUEST = "http://waimai.api.dianping.com/sendverifycode.ta";
    public static final String SHOP_COMMENTS_REQUEST = "http://waimai.api.dianping.com/commentlist.ta";
    public static final String SHOP_DETAIL_REQUEST = "http://waimai.api.dianping.com/shopinfo.ta";
    public static final String SHOP_LIST_REQUEST = "http://waimai.api.dianping.com/shoplist.ta?";
    public static final String SHOP_SEARCH_URL = "http://waimai.api.dianping.com/shopsuggest.ta?";
    public static final String SINGLE_TAKWAWAY_REQUEST = "http://waimai.api.dianping.com/takeawayrefresh.ta?";
    public static final String SUBMIT_OLD_ORDER_REQUEST = "http://waimai.api.dianping.com/submitoldorder.ta";
    public static final String SUGGEST_URL = "http://waimai.api.dianping.com/myuncompleteorder.ta";
    public static final String SUITABLE_ADDRESS = "http://waimai.api.dianping.com/getsuitableaddress.ta?";
    public static final String USER_BIND_PHONE_REQUEST = "http://waimai.api.dianping.com/getuserphone.ta";
    public static final String VERIFY_ELEME_CODE = "http://waimai.api.dianping.com/verifyelemecode.ta";
    public static final String VERIFY_PHONE_REQUEST = "http://waimai.api.dianping.com/verifyphone.ta";

    public request()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.util.TakeawayUtils
 * JD-Core Version:    0.6.0
 */