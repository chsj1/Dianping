package com.dianping.ugc.review;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.ugc.review.view.ShopTagItem;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class RecommendDishChooseActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  protected Adapter adapter;
  private AlertDialog dialog;
  private String dialogTitle;
  protected LinkedList<String> listAll = new LinkedList();
  protected LinkedList<String> listChoose;
  protected ProgressDialog loadingDialog;
  protected MApiService mapi;
  protected String orderId;
  protected MApiRequest requestDish;
  protected String shopId;

  private View createAddView()
  {
    return getLayoutInflater().inflate(R.layout.shop_tag_item_add, null, false);
  }

  protected AlertDialog createAddTagDialog()
  {
    LinearLayout localLinearLayout = new LinearLayout(this);
    Object localObject = new LinearLayout.LayoutParams(-1, -2);
    localLinearLayout.setPadding(ViewUtils.dip2px(this, 10.0F), ViewUtils.dip2px(this, 10.0F), ViewUtils.dip2px(this, 10.0F), ViewUtils.dip2px(this, 10.0F));
    localLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject);
    localObject = new EditText(this);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
    ((EditText)localObject).setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
    ((EditText)localObject).setLayoutParams(localLayoutParams);
    localLinearLayout.addView((View)localObject);
    return (AlertDialog)new AlertDialog.Builder(this).setTitle(this.dialogTitle).setView(localLinearLayout).setPositiveButton("确定", new DialogInterface.OnClickListener((EditText)localObject)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        Object localObject = this.val$textEntryView.getText().toString().trim();
        if (TextUtils.isEmpty((CharSequence)localObject))
          return;
        if (RecommendDishChooseActivity.this.listAll.contains(localObject))
          if (!RecommendDishChooseActivity.this.listChoose.contains(localObject))
          {
            RecommendDishChooseActivity.this.listChoose.add(localObject);
            RecommendDishChooseActivity.this.adapter.notifyDataSetChanged();
          }
        while (true)
        {
          this.val$textEntryView.setText("");
          try
          {
            localObject = paramDialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            ((Field)localObject).setAccessible(true);
            ((Field)localObject).set(paramDialogInterface, Boolean.valueOf(true));
            return;
          }
          catch (java.lang.Exception paramDialogInterface)
          {
            return;
          }
          RecommendDishChooseActivity.this.listAll.add(localObject);
          RecommendDishChooseActivity.this.listChoose.add(localObject);
          RecommendDishChooseActivity.this.adapter.notifyDataSetChanged();
        }
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener((EditText)localObject)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        this.val$textEntryView.setText("");
        try
        {
          Field localField = paramDialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
          localField.setAccessible(true);
          localField.set(paramDialogInterface, Boolean.valueOf(true));
          return;
        }
        catch (java.lang.Exception paramDialogInterface)
        {
        }
      }
    }).create();
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent();
    this.shopId = paramBundle.getStringExtra("shopId");
    if (TextUtils.isEmpty(this.shopId))
      this.shopId = paramBundle.getData().getQueryParameter("shopId");
    this.orderId = paramBundle.getStringExtra("orderid");
    if (TextUtils.isEmpty(this.orderId))
      this.orderId = paramBundle.getData().getQueryParameter("orderid");
    ArrayList localArrayList = paramBundle.getStringArrayListExtra("dishes");
    if ((TextUtils.isEmpty(this.shopId)) && (TextUtils.isEmpty(this.orderId)))
    {
      Toast.makeText(this, "无法获得商户ID或团单ID", 0).show();
      finish();
    }
    super.setContentView(R.layout.recommend_dish_chose_layout);
    GridView localGridView = (GridView)findViewById(R.id.grid);
    localGridView.setOnItemClickListener(this);
    this.adapter = new Adapter();
    localGridView.setAdapter(this.adapter);
    super.setTitle(paramBundle.getStringExtra("title"));
    this.dialogTitle = paramBundle.getStringExtra("dialogTitle");
    if (this.dialogTitle == null)
      this.dialogTitle = "请输入菜名";
    super.getTitleBar().addRightViewItem("确定", null, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent();
        paramView.putStringArrayListExtra("dishes", new ArrayList(RecommendDishChooseActivity.this.listChoose));
        RecommendDishChooseActivity.this.setResult(-1, paramView);
        RecommendDishChooseActivity.this.statisticsEvent("addreview5", "addreview5_dish_submit", "", 0);
        RecommendDishChooseActivity.this.finish();
      }
    });
    this.listChoose = new LinkedList();
    if (localArrayList != null)
    {
      paramBundle = localArrayList.iterator();
      while (paramBundle.hasNext())
        this.listChoose.add(paramBundle.next());
    }
    this.loadingDialog = new ProgressDialog(this);
    this.loadingDialog.setMessage("正在加载...");
    this.loadingDialog.show();
    requestShopDish(this.shopId, this.orderId);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if ((this.mapi != null) && (this.requestDish != null))
      this.mapi.abort(this.requestDish, this, true);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    statisticsEvent("addreview5", "addreview5_dish_select", "", 0);
    if (paramInt == this.listAll.size())
    {
      if (this.dialog == null)
        this.dialog = createAddTagDialog();
      this.dialog.show();
      return;
    }
    paramAdapterView = (ShopTagItem)paramView;
    paramView = paramAdapterView.getName();
    if (paramAdapterView.isChecked())
    {
      this.listChoose.remove(paramView);
      paramAdapterView.setChecked(false);
      return;
    }
    this.listChoose.add(paramView);
    paramAdapterView.setChecked(true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.loadingDialog != null)
      this.loadingDialog.dismiss();
    if (paramMApiRequest == this.requestDish)
      this.requestDish = null;
    Toast.makeText(this, paramMApiResponse.message().content(), 0).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (!(paramMApiResponse.result() instanceof DPObject))
      return;
    if (this.loadingDialog != null)
      this.loadingDialog.dismiss();
    if (paramMApiRequest == this.requestDish)
    {
      paramMApiRequest = ((DPObject)paramMApiResponse.result()).getArray("List");
      this.listAll = new LinkedList();
      int i = 0;
      while (i < paramMApiRequest.length)
      {
        paramMApiResponse = paramMApiRequest[i].getString("Name");
        this.listAll.add(paramMApiResponse);
        i += 1;
      }
      paramMApiRequest = this.listChoose.iterator();
      while (paramMApiRequest.hasNext())
      {
        paramMApiResponse = (String)paramMApiRequest.next();
        if (this.listAll.contains(paramMApiResponse))
          continue;
        this.listAll.add(paramMApiResponse);
      }
    }
    this.requestDish = null;
    this.adapter.notifyDataSetChanged();
  }

  protected void requestShopDish(String paramString1, String paramString2)
  {
    this.mapi = ((MApiService)getService("mapi"));
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/commontags.bin").buildUpon();
    String str = paramString1;
    if (paramString1 == null)
      str = "0";
    localBuilder.appendQueryParameter("shopid", str);
    paramString1 = paramString2;
    if (paramString2 == null)
      paramString1 = "0";
    localBuilder.appendQueryParameter("orderid", paramString1);
    this.requestDish = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.NORMAL);
    this.mapi.exec(this.requestDish, this);
  }

  protected class Adapter extends BasicAdapter
  {
    private final Object ADD = new Object();

    protected Adapter()
    {
    }

    public int getCount()
    {
      if (RecommendDishChooseActivity.this.listAll == null)
        return 1;
      return RecommendDishChooseActivity.this.listAll.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramInt == RecommendDishChooseActivity.this.listAll.size())
        return RecommendDishChooseActivity.this.createAddView();
      View localView = paramView;
      if (localView != null)
      {
        paramView = localView;
        if ((localView instanceof ShopTagItem));
      }
      else
      {
        paramView = RecommendDishChooseActivity.this.getLayoutInflater().inflate(R.layout.shop_tag_item, paramViewGroup, false);
      }
      paramViewGroup = (String)RecommendDishChooseActivity.this.listAll.get(paramInt);
      ((ShopTagItem)paramView).setChecked(RecommendDishChooseActivity.this.listChoose.contains(paramViewGroup));
      ((ShopTagItem)paramView).setName(paramViewGroup);
      return paramView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.RecommendDishChooseActivity
 * JD-Core Version:    0.6.0
 */