package com.dianping.shopinfo.clothes.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class ClothesBrandGoodsItemView extends NovaLinearLayout
{
  private TextView descView;
  private TextView nameView;
  public NetworkThumbView picView;

  public ClothesBrandGoodsItemView(Context paramContext)
  {
    super(paramContext);
  }

  public ClothesBrandGoodsItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ClothesBrandGoodsItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public static ClothesBrandGoodsItemView createView(Context paramContext, ViewGroup paramViewGroup)
  {
    return (ClothesBrandGoodsItemView)LayoutInflater.from(paramContext).inflate(R.layout.shopinfo_clothes_brandgoods_item, paramViewGroup, false);
  }

  public void init(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (!TextUtils.isEmpty(paramString1))
      this.picView.setImage(paramString1);
    if (!TextUtils.isEmpty(paramString2))
      this.nameView.setText(paramString2);
    if (!TextUtils.isEmpty(paramString3))
      this.descView.setText(paramString3);
    if (!TextUtils.isEmpty(paramString4))
      setOnClickListener(new View.OnClickListener(paramString4)
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$url));
          ClothesBrandGoodsItemView.this.getContext().startActivity(paramView);
        }
      });
    setGAString("branditem", paramString2);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.picView = ((NetworkThumbView)findViewById(R.id.goods_pic));
    this.nameView = ((TextView)findViewById(R.id.goods_name));
    this.descView = ((TextView)findViewById(R.id.goods_desc));
    int i = (ViewUtils.getScreenWidthPixels(getContext()) - 100) / 3;
    this.picView.getLayoutParams().width = i;
    this.picView.getLayoutParams().height = i;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.clothes.view.ClothesBrandGoodsItemView
 * JD-Core Version:    0.6.0
 */