package com.dianping.shopinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.MeasuredListView;
import com.dianping.base.widget.TableHeader;
import com.dianping.base.widget.TitleBar;
import com.dianping.shopinfo.utils.FacilitiesGridAdapter;
import com.dianping.shopinfo.widget.ShopInfoGridView;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaGridView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopDetailsActivity extends NovaActivity
{
  private static final int NUM_COLUMNS = 3;
  private ArrayList<DPObject> mFacilitiesList = new ArrayList();
  private NovaGridView mGridView;
  private DPObject mShop;
  private DPObject shopExtra;
  private DPObject shopExtraInfo = new DPObject();

  private void setupView(String paramString)
  {
    if (this.shopExtraInfo == null)
    {
      localObject1 = "商户信息";
      setTitle((CharSequence)localObject1);
      super.setContentView(R.layout.shopinfo_details_default_layout);
      localObject1 = (ShopInfoGridView)findViewById(R.id.facility);
      localObject2 = (TableHeader)findViewById(R.id.base_facility_header);
      if ((this.mFacilitiesList == null) || (this.mFacilitiesList.size() <= 0))
        break label157;
      ((TableHeader)localObject2).setVisibility(0);
      ((ShopInfoGridView)localObject1).setVisibility(0);
      ((ShopInfoGridView)localObject1).setVerticalSpacing(20);
      ((ShopInfoGridView)localObject1).setIsDisplayDivider(false);
      ((ShopInfoGridView)localObject1).setAdapter(new FacilitiesGridAdapter(getApplicationContext(), this.mFacilitiesList));
    }
    while (true)
    {
      if (this.shopExtraInfo == null)
        break label344;
      localObject2 = this.shopExtraInfo.getArray("ShopExtraInfos");
      if (localObject2 != null)
        break label172;
      return;
      if (TextUtils.isEmpty(this.shopExtraInfo.getString("ShopExtraInfosTitle")))
      {
        localObject1 = "商户信息";
        break;
      }
      localObject1 = this.shopExtraInfo.getString("ShopExtraInfosTitle");
      break;
      label157: ((ShopInfoGridView)localObject1).setVisibility(8);
      ((TableHeader)localObject2).setVisibility(8);
    }
    label172: Object localObject1 = new ArrayList();
    int i = 0;
    Object localObject3;
    while (i < localObject2.length)
    {
      localObject3 = new HashMap();
      ((HashMap)localObject3).put("Name", localObject2[i].getString("Name"));
      ((HashMap)localObject3).put("Value", localObject2[i].getString("Value"));
      ((List)localObject1).add(localObject3);
      i += 1;
    }
    Object localObject2 = (MeasuredListView)findViewById(R.id.id_list);
    int j;
    if ((localObject1 != null) && (((List)localObject1).size() > 0))
    {
      ((MeasuredListView)localObject2).setVisibility(0);
      localObject3 = getApplicationContext();
      i = R.layout.shopinfo_detail_feature_item;
      j = R.id.title;
      int k = R.id.sub_title;
      ((MeasuredListView)localObject2).setAdapter(new SimpleAdapter((Context)localObject3, (List)localObject1, i, new String[] { "Name", "Value" }, new int[] { j, k }));
    }
    while (true)
    {
      label344: localObject1 = findViewById(R.id.business_certifications_header);
      localObject2 = findViewById(R.id.fl_certification);
      localObject3 = (TextView)findViewById(R.id.business_certifications);
      if ((this.shopExtra == null) || (!this.shopExtra.getBoolean("CertificationsShow")))
        break label596;
      if (!"common_default".equalsIgnoreCase(paramString))
        break label583;
      this.mGridView = ((NovaGridView)findViewById(R.id.license));
      this.mGridView.setNumColumns(3);
      paramString = this.shopExtra.getArray("Certifications");
      if ((paramString == null) || (paramString.length <= 0))
        break;
      ArrayList localArrayList = new ArrayList();
      j = paramString.length;
      i = 0;
      while (true)
        if (i < j)
        {
          Object localObject4 = paramString[i];
          localArrayList.add(new DPObject().edit().putString("Url", localObject4.getString("BigIcon")).generate());
          i += 1;
          continue;
          ((MeasuredListView)localObject2).setVisibility(8);
          break;
        }
      this.mGridView.setAdapter(new CertificationsAdapter(paramString));
      this.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(localArrayList)
      {
        public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showcheckinphoto"));
          paramAdapterView.putExtra("position", paramInt);
          paramAdapterView.putParcelableArrayListExtra("pageList", this.val$photos);
          ShopDetailsActivity.this.startActivity(paramAdapterView);
        }
      });
      ((TextView)localObject3).setVisibility(8);
    }
    while (true)
    {
      ((View)localObject2).setVisibility(0);
      ((View)localObject1).setVisibility(0);
      return;
      ((TextView)localObject3).setVisibility(0);
    }
    label583: ((View)localObject2).setVisibility(8);
    ((View)localObject1).setVisibility(8);
    return;
    label596: ((View)localObject2).setVisibility(8);
    ((View)localObject1).setVisibility(8);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 6);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mShop = ((DPObject)getIntent().getParcelableExtra("shop"));
    this.shopExtra = ((DPObject)getIntent().getParcelableExtra("shopextra"));
    if (this.shopExtra != null)
      this.shopExtraInfo = this.shopExtra.getObject("ShopExtraInfo");
    this.mFacilitiesList = getIntent().getParcelableArrayListExtra("featurelist");
    if (this.mShop != null)
    {
      setSubtitle(this.mShop.getString("Name") + this.mShop.getString("BranchName"));
      if (this.mShop.getObject("ClientShopStyle") != null)
        break label140;
    }
    label140: for (paramBundle = null; ; paramBundle = this.mShop.getObject("ClientShopStyle").getString("ShopView"))
    {
      setupView(paramBundle);
      return;
    }
  }

  class CertificationsAdapter extends BaseAdapter
  {
    private List<DPObject> infoList = new ArrayList();

    public CertificationsAdapter(DPObject[] arg2)
    {
      this.infoList.clear();
      Object localObject;
      if ((localObject != null) && (localObject.length > 0))
      {
        int j = localObject.length;
        int i = 0;
        while (i < j)
        {
          this$1 = localObject[i];
          this.infoList.add(ShopDetailsActivity.this);
          i += 1;
        }
      }
    }

    public int getCount()
    {
      return this.infoList.size();
    }

    public Object getItem(int paramInt)
    {
      return this.infoList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject;
      if (paramView == null)
      {
        paramView = LayoutInflater.from(ShopDetailsActivity.this.getApplicationContext()).inflate(R.layout.shopinfo_certification_item, null);
        paramViewGroup = new ShopDetailsActivity.ViewHolder(ShopDetailsActivity.this);
        paramViewGroup.mIcon = ((NetworkImageView)paramView.findViewById(R.id.image));
        localObject = paramViewGroup.mIcon.getLayoutParams();
        int i = (ViewUtils.getScreenWidthPixels(ShopDetailsActivity.this.getApplicationContext()) - ShopDetailsActivity.this.mGridView.getPaddingLeft() - ShopDetailsActivity.this.mGridView.getPaddingRight() - ViewUtils.dip2px(ShopDetailsActivity.this.getApplicationContext(), 15.0F) * 2) / 3;
        ((ViewGroup.LayoutParams)localObject).width = i;
        ((ViewGroup.LayoutParams)localObject).height = i;
        paramViewGroup.mIcon.setLayoutParams((ViewGroup.LayoutParams)localObject);
        paramViewGroup.mTitle = ((TextView)paramView.findViewById(R.id.title));
        paramView.setTag(paramViewGroup);
      }
      while (true)
      {
        localObject = (DPObject)this.infoList.get(paramInt);
        if ((localObject != null) && (!TextUtils.isEmpty(((DPObject)localObject).getString("Icon"))))
          break;
        return null;
        paramViewGroup = (ShopDetailsActivity.ViewHolder)paramView.getTag();
      }
      paramViewGroup.mIcon.setImage(((DPObject)localObject).getString("Icon"));
      paramViewGroup.mTitle.setText(((DPObject)localObject).getString("Title"));
      return (View)paramView;
    }
  }

  class ViewHolder
  {
    public NetworkImageView mIcon;
    public TextView mTitle;

    ViewHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.activity.ShopDetailsActivity
 * JD-Core Version:    0.6.0
 */