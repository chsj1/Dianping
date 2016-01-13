package com.dianping.membercard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class MemberCardProductDetailActivity extends NovaActivity
{
  private DPObject card;
  private DPObject product;
  private TextView productDesc;
  private TextView productName;
  private TextView shopName;
  private TextView vipName;
  private TextView vipNumber;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.card_product_detail);
    setupView();
    paramBundle = getIntent();
    this.card = ((DPObject)paramBundle.getExtras().getParcelable("card"));
    this.product = ((DPObject)paramBundle.getExtras().getParcelable("product"));
    updateViewDetail();
  }

  public void setupView()
  {
    this.shopName = ((TextView)findViewById(R.id.shop_name));
    this.vipName = ((TextView)findViewById(R.id.vip_name));
    this.vipNumber = ((TextView)findViewById(R.id.vip_number));
    this.productName = ((TextView)findViewById(R.id.product_name));
    this.productDesc = ((TextView)findViewById(R.id.product_desc));
  }

  public void updateViewDetail()
  {
    setTitle(this.card.getString("Title"));
    this.shopName.setText(this.card.getString("Title"));
    this.vipName.setText(this.card.getString("UserName"));
    this.vipNumber.setText("No." + this.card.getString("MemberCardNO"));
    this.productName.setText(this.product.getString("ProductName").replace("|", ""));
    String str = this.product.getString("ProductDesc");
    if (!TextUtils.isEmpty(str))
      str.replaceAll("\r\n", "\n");
    this.productDesc.setText(str);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.MemberCardProductDetailActivity
 * JD-Core Version:    0.6.0
 */