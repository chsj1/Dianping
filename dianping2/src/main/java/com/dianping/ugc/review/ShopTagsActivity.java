package com.dianping.ugc.review;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.ugc.review.view.ShopTagItem;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShopTagsActivity extends NovaActivity
  implements AdapterView.OnItemClickListener, View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private Adapter adapter;
  private AlertDialog dialog;
  private String orderID;
  private MApiRequest request;
  private String shopID;
  private List<String> shopTags;
  private List<Tag> tags;

  private AlertDialog createAddTagDialog()
  {
    LinearLayout localLinearLayout = new LinearLayout(this);
    Object localObject = new LinearLayout.LayoutParams(-1, -2);
    localLinearLayout.setPadding(ViewUtils.dip2px(this, 10.0F), ViewUtils.dip2px(this, 10.0F), ViewUtils.dip2px(this, 10.0F), ViewUtils.dip2px(this, 10.0F));
    localLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject);
    localObject = new EditText(this);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
    ((EditText)localObject).setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });
    ((EditText)localObject).setLayoutParams(localLayoutParams);
    localLinearLayout.addView((View)localObject);
    return (AlertDialog)new AlertDialog.Builder(this).setTitle("请输入一个特色标签").setView(localLinearLayout).setPositiveButton("确定", new DialogInterface.OnClickListener((EditText)localObject)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        Object localObject = this.val$textEntryView.getText().toString().trim();
        if (TextUtils.isEmpty((CharSequence)localObject));
        try
        {
          localObject = paramDialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
          ((Field)localObject).setAccessible(true);
          ((Field)localObject).set(paramDialogInterface, Boolean.valueOf(false));
          label48: this.val$textEntryView.setError(Html.fromHtml("<font color='#666664'>标签不能为空</font>"));
          return;
          ShopTagsActivity.this.tags.add(new ShopTagsActivity.Tag(ShopTagsActivity.this, (String)localObject, true));
          ShopTagsActivity.this.adapter.notifyDataSetChanged();
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
        }
        catch (java.lang.Exception paramDialogInterface)
        {
          break label48;
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

  private View createEmptyView()
  {
    ProgressBar localProgressBar = new ProgressBar(this);
    FrameLayout localFrameLayout = new FrameLayout(this);
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-1, -1);
    localLayoutParams.gravity = 17;
    localFrameLayout.addView(localProgressBar, localLayoutParams);
    return localFrameLayout;
  }

  private void loadData()
  {
    if (this.request != null)
      mapiService().abort(this.request, this, true);
    this.request = BasicMApiRequest.mapiGet("http://m.api.dianping.com/commontags.bin?shopid=" + this.shopID + "&orderid=" + this.orderID, CacheType.NORMAL);
    mapiService().exec(this.request, this);
  }

  private void submit()
  {
    if (this.tags == null)
      return;
    Object localObject1 = new StringBuilder();
    Object localObject2 = this.tags.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      Tag localTag = (Tag)((Iterator)localObject2).next();
      if (!localTag.checked)
        continue;
      ((StringBuilder)localObject1).append("、");
      ((StringBuilder)localObject1).append(localTag.name);
    }
    localObject1 = ((StringBuilder)localObject1).toString().replaceFirst("、", "");
    localObject2 = new Intent();
    ((Intent)localObject2).putExtra("shoptags", (String)localObject1);
    setResult(-1, (Intent)localObject2);
    finish();
  }

  public void onClick(View paramView)
  {
    submit();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.shop_tags);
    this.shopID = getIntent().getData().getQueryParameter("shopid");
    this.orderID = getIntent().getData().getQueryParameter("orderid");
    paramBundle = getIntent().getData().getQueryParameter("shoptags").split("、");
    int j = paramBundle.length;
    int i = 0;
    while (i < j)
    {
      Object localObject = paramBundle[i];
      if (this.shopTags == null)
        this.shopTags = new ArrayList();
      this.shopTags.add(localObject);
      i += 1;
    }
    paramBundle = (GridView)findViewById(R.id.grid);
    paramBundle.setOnItemClickListener(this);
    this.adapter = new Adapter();
    paramBundle.setEmptyView(createEmptyView());
    paramBundle.setAdapter(this.adapter);
    loadData();
    super.setTitleButton("提交", this);
  }

  protected void onDestroy()
  {
    if (this.request != null)
      mapiService().abort(this.request, this, true);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if (paramAdapterView == this.adapter.ADD)
    {
      if (this.dialog == null)
        this.dialog = createAddTagDialog();
      this.dialog.show();
      return;
    }
    paramView = (Tag)paramAdapterView;
    if (!((Tag)paramAdapterView).checked);
    for (boolean bool = true; ; bool = false)
    {
      paramView.setChecked(bool);
      this.adapter.notifyDataSetChanged();
      return;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.tags = new ArrayList();
    this.adapter.notifyDataSetChanged();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiRequest = ((DPObject)paramMApiResponse.result()).getString("Name");
      this.tags = new ArrayList();
      if (!TextUtils.isEmpty(paramMApiRequest))
      {
        paramMApiRequest = paramMApiRequest.split("、");
        int k = paramMApiRequest.length;
        int i = 0;
        if (i < k)
        {
          paramMApiResponse = paramMApiRequest[i];
          if (TextUtils.isEmpty(paramMApiResponse));
          while (true)
          {
            i += 1;
            break;
            boolean bool = false;
            int j = 0;
            while (j < this.shopTags.size())
            {
              if (paramMApiResponse.equals((String)this.shopTags.get(j)))
              {
                bool = true;
                this.shopTags.remove(j);
              }
              j += 1;
            }
            paramMApiResponse = new Tag(paramMApiResponse, bool);
            this.tags.add(paramMApiResponse);
          }
        }
      }
      paramMApiRequest = this.shopTags.iterator();
      while (paramMApiRequest.hasNext())
      {
        paramMApiResponse = (String)paramMApiRequest.next();
        if (TextUtils.isEmpty(paramMApiResponse))
          continue;
        paramMApiResponse = new Tag(paramMApiResponse, true);
        this.tags.add(paramMApiResponse);
      }
    }
    this.adapter.notifyDataSetChanged();
  }

  class Adapter extends BasicAdapter
  {
    private final Object ADD = new Object();

    Adapter()
    {
    }

    public int getCount()
    {
      if (ShopTagsActivity.this.tags == null)
        return 0;
      return ShopTagsActivity.this.tags.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < ShopTagsActivity.this.tags.size())
        return ShopTagsActivity.this.tags.get(paramInt);
      return this.ADD;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = paramView;
      if (localView != null)
      {
        paramView = localView;
        if ((localView instanceof ShopTagItem));
      }
      else
      {
        paramView = ShopTagsActivity.this.getLayoutInflater().inflate(R.layout.shop_tag_item, paramViewGroup, false);
      }
      paramViewGroup = getItem(paramInt);
      if (paramViewGroup == this.ADD)
      {
        ((ShopTagItem)paramView).setData(null, false);
        return paramView;
      }
      ((ShopTagItem)paramView).setData(((ShopTagsActivity.Tag)paramViewGroup).name, ((ShopTagsActivity.Tag)paramViewGroup).checked);
      return paramView;
    }
  }

  class Tag
  {
    boolean checked;
    String name;

    public Tag(String paramBoolean, boolean arg3)
    {
      this.name = paramBoolean;
      boolean bool;
      this.checked = bool;
    }

    public void setChecked(boolean paramBoolean)
    {
      this.checked = paramBoolean;
    }

    public void setName(String paramString)
    {
      this.name = paramString;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.ShopTagsActivity
 * JD-Core Version:    0.6.0
 */