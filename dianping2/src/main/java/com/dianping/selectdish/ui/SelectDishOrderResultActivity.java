package com.dianping.selectdish.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.widget.TableView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.entity.SelectDishOrderResultDataSource;
import com.dianping.selectdish.entity.SelectDishOrderResultDataSource.ReviewStatus;
import com.dianping.selectdish.entity.SelectDishOrderResultDataSource.SelectDishOrderResultDataLoaderListener;
import com.dianping.selectdish.view.SelectDishAlertDialog;
import com.dianping.selectdish.view.SelectDishAlertDialog.OnDialogClickListener;
import com.dianping.util.CrashReportHelper;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectDishOrderResultActivity extends NovaActivity
  implements SelectDishOrderResultDataSource.SelectDishOrderResultDataLoaderListener, View.OnClickListener
{
  private static final String MAPI_DELETE_ORDER_URL = "http://m.api.dianping.com/orderdish/deleteorder.bin";
  public static DecimalFormat PRICE_DF = new DecimalFormat("#.##");
  NovaButton addReviewButton;
  LinearLayout addReviewLayout;
  TextView addReviewStatus;
  TextView addReviewTip;
  TextView addReviewTitle;
  ImageView barCodeView;
  Context context;
  private SelectDishOrderResultDataSource dataSource;
  private MApiRequest deleteOrderRequest = null;
  private GAUserInfo gaUserInfo = new GAUserInfo();
  RelativeLayout gotoDetailLayout;
  RelativeLayout gotoGroupOnLayout;
  LinearLayout groupOnCode;
  LinearLayout groupOnLayout;
  TextView groupOnName;
  NovaButton leftButton;
  TextView leftButtonSubTitle;
  LinearLayout mainInfoLayout;
  TextView mainTitle;
  private final RequestHandler<MApiRequest, MApiResponse> mapiRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      SelectDishOrderResultActivity.access$402(SelectDishOrderResultActivity.this, null);
      SelectDishOrderResultActivity.this.dismissDialog();
      paramMApiRequest = null;
      if (paramMApiResponse.message() != null)
        paramMApiRequest = paramMApiResponse.message().content();
      SelectDishOrderResultActivity localSelectDishOrderResultActivity = SelectDishOrderResultActivity.this;
      paramMApiResponse = paramMApiRequest;
      if (TextUtils.isEmpty(paramMApiRequest))
        paramMApiResponse = "订单编辑失败";
      localSelectDishOrderResultActivity.showToast(paramMApiResponse);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      SelectDishOrderResultActivity.access$402(SelectDishOrderResultActivity.this, null);
      SelectDishOrderResultActivity.this.dismissDialog();
      paramMApiRequest = paramMApiResponse.result();
      if ((paramMApiRequest != null) && ((paramMApiRequest instanceof DPObject)))
      {
        paramMApiRequest = (DPObject)paramMApiRequest;
        paramMApiResponse = paramMApiRequest.getString("Message");
        if (paramMApiRequest.getInt("Code") == 1)
        {
          SelectDishOrderResultActivity.this.openMenuViewForEdit();
          SelectDishOrderResultActivity.this.finish();
          return;
        }
        SelectDishOrderResultActivity localSelectDishOrderResultActivity = SelectDishOrderResultActivity.this;
        paramMApiRequest = paramMApiResponse;
        if (TextUtils.isEmpty(paramMApiResponse))
          paramMApiRequest = "订单编辑失败";
        localSelectDishOrderResultActivity.showToast(paramMApiRequest);
        return;
      }
      SelectDishOrderResultActivity.this.showToast("订单编辑失败");
    }
  };
  NovaButton middleButton;
  TextView middleButtonSubTitle;
  FrameLayout oneButtonLayout;
  TableView orderDetail;
  LinearLayout orderDetailLayout;
  ImageView orderResultImageView;
  NovaButton rightButton;
  TextView rightButtonSubTitle;
  FrameLayout selectDishOrderResultError;
  public PullToRefreshScrollView selectDishOrderResultLoadedLayout;
  View selectDishOrderResultLoadingLayout;
  float skuQuantityWidth = 0.0F;
  float skusPriceWidth = 0.0F;
  LinearLayout subInfoLayout;
  View subInfoLine;
  TextView subTitle;
  LinearLayout twoButtonLayout;

  private void addGroupOnCodeView(DPObject[] paramArrayOfDPObject, ViewGroup paramViewGroup, int paramInt)
  {
    if (paramArrayOfDPObject != null)
    {
      int j = paramArrayOfDPObject.length;
      int i = 0;
      if (i < j)
      {
        View localView = getLayoutInflater().inflate(R.layout.selectdish_orderresult_groupon_item, null);
        Object localObject = (TextView)localView.findViewById(R.id.selectdish_groupon_code);
        TextView localTextView = (TextView)localView.findViewById(R.id.selectdish_groupon_status);
        ((TextView)localObject).setText(paramArrayOfDPObject[i].getString("BarCode"));
        if (paramInt == 1)
        {
          ((TextView)localObject).setTextColor(getResources().getColor(R.color.light_red));
          localTextView.setText("可使用");
          localTextView.setTextColor(getResources().getColor(R.color.light_red));
        }
        while (true)
        {
          localObject = new LinearLayout.LayoutParams(-2, -2);
          ((LinearLayout.LayoutParams)localObject).setMargins(0, 0, 0, ViewUtils.dip2px(this.context, 5.0F));
          paramViewGroup.addView(localView, (ViewGroup.LayoutParams)localObject);
          i += 1;
          break;
          if (paramInt == 2)
          {
            ((TextView)localObject).setTextColor(getResources().getColor(R.color.font_gray));
            ((TextView)localObject).getPaint().setFlags(16);
            localTextView.setText("已使用");
            localTextView.setTextColor(getResources().getColor(R.color.font_gray));
            continue;
          }
          if (paramInt != 3)
            continue;
          ((TextView)localObject).setTextColor(getResources().getColor(R.color.font_gray));
          ((TextView)localObject).getPaint().setFlags(16);
          localTextView.setText("已失效");
          localTextView.setTextColor(getResources().getColor(R.color.font_gray));
        }
      }
    }
  }

  private void exit()
  {
    Object localObject = getWebUriFromSchemaHistory();
    if (TextUtils.isEmpty((CharSequence)localObject))
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishmenu"));
      ((Intent)localObject).putExtra("close", true);
      ((Intent)localObject).putExtra("shopid", this.dataSource.shopId);
      ((Intent)localObject).putExtra("orderid", this.dataSource.orderId);
      ((Intent)localObject).putExtra("tablenum", this.dataSource.tableNum);
    }
    while (true)
    {
      ((Intent)localObject).setFlags(67108864);
      startActivity((Intent)localObject);
      finish();
      return;
      localObject = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
    }
  }

  private String getWebUriFromSchemaHistory()
  {
    try
    {
      Iterator localIterator = CrashReportHelper.listSchema.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if ((str.startsWith("dianping://web")) && (str.contains("newmyorders")))
          return str;
        if (str.startsWith("dianping://selectdishmenu"))
          return "";
      }
      return "";
    }
    catch (Exception localException)
    {
    }
    return "";
  }

  private void initGroupOnInfo()
  {
    if (this.dataSource.groupOnInfo != null)
    {
      this.groupOnName.setText(this.dataSource.groupOnInfo.getString("GroupOnName"));
      DPObject[] arrayOfDPObject1 = this.dataSource.groupOnInfo.getArray("UnusedGroupOnReceipts");
      DPObject[] arrayOfDPObject2 = this.dataSource.groupOnInfo.getArray("UsedGroupOnReceipts");
      DPObject[] arrayOfDPObject3 = this.dataSource.groupOnInfo.getArray("InvalidGroupOnReceipts");
      this.groupOnCode.removeAllViews();
      addGroupOnCodeView(arrayOfDPObject1, this.groupOnCode, 1);
      addGroupOnCodeView(arrayOfDPObject2, this.groupOnCode, 2);
      addGroupOnCodeView(arrayOfDPObject3, this.groupOnCode, 3);
      this.gotoGroupOnLayout.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          GAHelper.instance().contextStatisticsEvent(SelectDishOrderResultActivity.this.context, "checkdealdetail", SelectDishOrderResultActivity.this.gaUserInfo, "tap");
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(SelectDishOrderResultActivity.this.dataSource.groupOnInfo.getString("Schema")));
          SelectDishOrderResultActivity.this.startActivity(paramView);
        }
      });
      this.groupOnLayout.setVisibility(0);
      return;
    }
    this.groupOnLayout.setVisibility(8);
  }

  private void initInfo()
  {
    float f1 = getResources().getDimension(R.dimen.text_size_15);
    float f2 = getResources().getDimension(R.dimen.text_size_13);
    float f3 = ViewUtils.dip2px(this.context, 12.0F);
    float f4 = getTextWidth("四字宽度", f1);
    this.mainInfoLayout.removeAllViews();
    this.subInfoLayout.removeAllViews();
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
    int k = getResources().getColor(R.color.deep_gray);
    int j = getResources().getColor(R.color.light_gray);
    int i;
    if (this.dataSource.mainInfos != null)
    {
      i = 0;
      while (i < this.dataSource.mainInfos.length)
      {
        this.mainInfoLayout.addView(createInfoView(f4, f3, this.dataSource.mainInfos[i].getString("Key"), this.dataSource.mainInfos[i].getString("Value"), f1, k), localLayoutParams);
        i += 1;
      }
    }
    if (this.dataSource.subInfos != null)
    {
      i = 0;
      while (i < this.dataSource.subInfos.length)
      {
        this.subInfoLayout.addView(createInfoView(f4, f3, this.dataSource.subInfos[i].getString("Key"), this.dataSource.subInfos[i].getString("Value"), f2, j), localLayoutParams);
        i += 1;
      }
      this.subInfoLine.setVisibility(0);
    }
    while (this.dataSource.showOrderDetailFlag == 1)
    {
      this.gotoDetailLayout.setVisibility(0);
      this.gotoDetailLayout.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (SelectDishOrderResultActivity.this.dataSource.status == 20)
            GAHelper.instance().contextStatisticsEvent(SelectDishOrderResultActivity.this.context, "succeed_detail", SelectDishOrderResultActivity.this.gaUserInfo, "tap");
          while (true)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=http%3A%2F%2Fm.dianping.com%2Fhobbit%2Fnewmyorderdetail%3Ftoken%3D!%26viewid%3d" + SelectDishOrderResultActivity.this.dataSource.viewId));
            paramView.setFlags(67108864);
            SelectDishOrderResultActivity.this.startActivity(paramView);
            return;
            if (SelectDishOrderResultActivity.this.dataSource.status != 30)
              continue;
            GAHelper.instance().contextStatisticsEvent(SelectDishOrderResultActivity.this.context, "failure_detail", SelectDishOrderResultActivity.this.gaUserInfo, "tap");
          }
        }
      });
      return;
      this.subInfoLine.setVisibility(8);
      this.subInfoLayout.setVisibility(8);
    }
    this.gotoDetailLayout.setVisibility(8);
  }

  private void initOrder()
  {
    float f3 = getResources().getDimension(R.dimen.text_size_15);
    if (this.dataSource.orderEntries == null)
    {
      this.orderDetailLayout.setVisibility(8);
      return;
    }
    int i = 0;
    if (i < this.dataSource.orderEntries.length)
    {
      String str1 = this.dataSource.orderEntries[i].getInt("SkuQuantity") + "";
      String str2 = new BigDecimal(PRICE_DF.format(this.dataSource.orderEntries[i].getDouble("SkusPrice"))).toString();
      float f1 = getTextWidth(str1 + "份", f3);
      float f2 = getTextWidth("¥" + str2, f3);
      if (f1 > this.skuQuantityWidth)
      {
        label181: this.skuQuantityWidth = f1;
        if (f2 <= this.skusPriceWidth)
          break label219;
        f1 = f2;
      }
      while (true)
      {
        this.skusPriceWidth = f1;
        i += 1;
        break;
        f1 = this.skuQuantityWidth;
        break label181;
        label219: f1 = this.skusPriceWidth;
      }
    }
    this.skuQuantityWidth += 1.0F;
    this.skusPriceWidth += 1.0F;
    this.orderDetail.setAdapter(new SelectDishOrderDetailAdapter());
    this.orderDetailLayout.setVisibility(0);
  }

  private void initReviewAndButton()
  {
    if (this.dataSource.reviewStatus == SelectDishOrderResultDataSource.ReviewStatus.OPEN)
    {
      ViewUtils.setVisibilityAndContent(this.addReviewTitle, this.dataSource.reviewInfo.getString("Title"));
      ViewUtils.setVisibilityAndContent(this.addReviewTip, this.dataSource.reviewInfo.getString("Tip"));
      this.addReviewStatus.setVisibility(4);
      this.addReviewButton.setOnClickListener(this);
      this.addReviewButton.setGAString("order_finished_comment", this.gaUserInfo);
      this.addReviewButton.setVisibility(0);
      this.addReviewLayout.setVisibility(0);
      if ((this.dataSource.buttonList != null) && (this.dataSource.buttonList[0] != null) && (this.dataSource.buttonList[1] != null))
      {
        if (!this.dataSource.buttonList[0].equals(this.dataSource.buttonList[1]))
          break label357;
        this.oneButtonLayout.setVisibility(0);
        this.twoButtonLayout.setVisibility(8);
        if ((!this.dataSource.buttonList[0].equals("2")) && (!this.dataSource.buttonList[0].equals("3")))
          break label334;
      }
    }
    label334: for (this.middleButton.getLayoutParams().height = ViewUtils.dip2px(this.context, 44.0F); ; this.middleButton.getLayoutParams().height = ViewUtils.dip2px(this.context, 35.0F))
    {
      setButton(this.middleButton, this.middleButtonSubTitle, this.dataSource.buttonList[0]);
      return;
      if (this.dataSource.reviewStatus == SelectDishOrderResultDataSource.ReviewStatus.CLOSED)
      {
        ViewUtils.setVisibilityAndContent(this.addReviewTitle, this.dataSource.reviewInfo.getString("Title"));
        this.addReviewTip.setVisibility(8);
        this.addReviewStatus.setVisibility(0);
        this.addReviewButton.setVisibility(8);
        this.addReviewLayout.setVisibility(0);
        break;
      }
      this.addReviewLayout.setVisibility(8);
      break;
    }
    label357: this.oneButtonLayout.setVisibility(8);
    this.twoButtonLayout.setVisibility(0);
    if ((this.dataSource.buttonList[0].equals("2")) || (this.dataSource.buttonList[1].equals("2")) || (this.dataSource.buttonList[0].equals("3")) || (this.dataSource.buttonList[1].equals("3")))
      this.leftButton.getLayoutParams().height = ViewUtils.dip2px(this.context, 44.0F);
    for (this.rightButton.getLayoutParams().height = ViewUtils.dip2px(this.context, 44.0F); ; this.rightButton.getLayoutParams().height = ViewUtils.dip2px(this.context, 35.0F))
    {
      setButton(this.leftButton, this.leftButtonSubTitle, this.dataSource.buttonList[0]);
      setButton(this.rightButton, this.rightButtonSubTitle, this.dataSource.buttonList[1]);
      return;
      this.leftButton.getLayoutParams().height = ViewUtils.dip2px(this.context, 35.0F);
    }
  }

  private void initTitle()
  {
    float f1;
    Object localObject1;
    Object localObject2;
    int i;
    label268: label316: label383: int j;
    label639: int k;
    switch (this.dataSource.status)
    {
    default:
      super.setTitle(this.dataSource.barTitle);
      float f3 = getResources().getDimension(R.dimen.text_size_20);
      float f4 = getResources().getDimension(R.dimen.text_size_30);
      float f5 = getResources().getDimension(R.dimen.text_size_11);
      f1 = 0.0F;
      if (!TextUtils.isEmpty(this.dataSource.viewBarCodeNo))
      {
        float f2 = ViewUtils.getScreenWidthPixels(this.context) - 60 - 48 - 20 - getTextWidth(this.dataSource.mainTitle, f3) - getTextWidth(this.dataSource.viewBarCodeNo, f4) - getTextWidth("  " + this.dataSource.mainComment, f5);
        f1 = f2;
        if (f2 < 0.0F)
        {
          localObject1 = new StringBuilder();
          localObject2 = this.dataSource;
          ((SelectDishOrderResultDataSource)localObject2).mainTitle += "\n";
          f1 = f2;
        }
      }
      i = 0;
      localObject2 = new StringBuilder().append("");
      if (TextUtils.isEmpty(this.dataSource.mainTitle))
        break;
      localObject1 = this.dataSource.mainTitle;
      localObject1 = (String)localObject1;
      localObject2 = new StringBuilder().append((String)localObject1);
      if (!TextUtils.isEmpty(this.dataSource.viewBarCodeNo))
      {
        localObject1 = this.dataSource.viewBarCodeNo;
        localObject1 = (String)localObject1;
        localObject2 = new StringBuilder().append((String)localObject1);
        if (TextUtils.isEmpty(this.dataSource.mainComment))
          break label943;
        localObject1 = "  " + this.dataSource.mainComment;
        localObject1 = new SpannableString((String)localObject1);
        if (!TextUtils.isEmpty(this.dataSource.mainTitle))
        {
          ((SpannableString)localObject1).setSpan(new AbsoluteSizeSpan((int)f3), 0, this.dataSource.mainTitle.length(), 33);
          i = 0 + this.dataSource.mainTitle.length();
        }
        j = i;
        if (!TextUtils.isEmpty(this.dataSource.viewBarCodeNo))
        {
          ((SpannableString)localObject1).setSpan(new AbsoluteSizeSpan((int)f4), i, this.dataSource.viewBarCodeNo.length() + i, 33);
          j = i + this.dataSource.viewBarCodeNo.length();
        }
        if (!TextUtils.isEmpty(this.dataSource.mainComment))
        {
          ((SpannableString)localObject1).setSpan(new AbsoluteSizeSpan((int)f5), j, j + 2 + this.dataSource.mainComment.length(), 33);
          ((SpannableString)localObject1).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.hotel_shoplist_gray)), j, j + 2 + this.dataSource.mainComment.length(), 33);
        }
        this.mainTitle.setText((CharSequence)localObject1);
        if (!TextUtils.isEmpty(this.dataSource.subTitle))
          break label951;
        this.subTitle.setVisibility(8);
        if (this.dataSource.barCodeFlag != 1)
          break label976;
        i = 1;
        label653: if (TextUtils.isEmpty(this.dataSource.barCodeNo))
          break label982;
        j = 1;
        label669: if ((i & j) == 0)
          break label1206;
        i = ViewUtils.getScreenWidthPixels(this.context);
        j = ViewUtils.dip2px(this.context, 30.0F);
        k = ViewUtils.dip2px(this.context, 50.0F);
      }
    case 20:
    case 30:
    case 40:
    }
    while (true)
    {
      int n;
      int i1;
      try
      {
        localObject2 = new MultiFormatWriter().encode(this.dataSource.barCodeNo, BarcodeFormat.CODE_128, i - j, k);
        n = ((BitMatrix)localObject2).getWidth();
        i1 = ((BitMatrix)localObject2).getHeight();
        k = 0;
        m = 0;
        j = 0;
        i = k;
        if (j >= n)
          break label1324;
        if (!((BitMatrix)localObject2).get(j, 0))
          continue;
        i = j;
        break label1324;
        k = m;
        if (j < 0)
          continue;
        if (!((BitMatrix)localObject2).get(j, 0))
          continue;
        k = j;
        int i2 = k - i + 1;
        localObject1 = new int[i2 * i1];
        j = 0;
        break label1333;
        if (m > k)
          continue;
        if (!((BitMatrix)localObject2).get(m, j))
          continue;
        n = -16777216;
        localObject1[(j * i2 + m - i)] = n;
        m += 1;
        continue;
        this.orderResultImageView.setImageResource(R.drawable.icon_review_success);
        break;
        this.orderResultImageView.setImageResource(R.drawable.order_home_icon_error);
        break;
        this.orderResultImageView.setImageResource(R.drawable.order_home_icon_loading);
        break;
        localObject1 = "";
        break label268;
        localObject1 = "";
        break label316;
        label943: localObject1 = "";
        break label383;
        label951: this.subTitle.setText(this.dataSource.subTitle);
        this.subTitle.setVisibility(0);
        break label639;
        label976: i = 0;
        break label653;
        label982: j = 0;
        break label669;
        j += 1;
        continue;
        j -= 1;
        continue;
        n = -1;
        continue;
        j += 1;
        break label1333;
        localObject2 = Bitmap.createBitmap(i2, i1, Bitmap.Config.ARGB_8888);
        ((Bitmap)localObject2).setPixels(localObject1, 0, i2, 0, 0, i2, i1);
        this.barCodeView.setImageBitmap((Bitmap)localObject2);
        this.barCodeView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.barCodeView.setVisibility(0);
        if ((TextUtils.isEmpty(this.dataSource.viewBarCodeNo)) || (f1 < 0.0F))
          break label1218;
        localObject1 = (RelativeLayout.LayoutParams)this.mainTitle.getLayoutParams();
        ((RelativeLayout.LayoutParams)localObject1).setMargins(ViewUtils.dip2px(this.context, 10.0F), ViewUtils.dip2px(this.context, 10.0F), ViewUtils.dip2px(this.context, 15.0F), 0);
        this.mainTitle.setLayoutParams((ViewGroup.LayoutParams)localObject1);
        localObject1 = (RelativeLayout.LayoutParams)this.orderResultImageView.getLayoutParams();
        ((RelativeLayout.LayoutParams)localObject1).setMargins(0, ViewUtils.dip2px(this.context, 20.0F), 0, 0);
        this.orderResultImageView.setLayoutParams((ViewGroup.LayoutParams)localObject1);
        return;
      }
      catch (WriterException localWriterException)
      {
        this.barCodeView.setVisibility(8);
        continue;
      }
      label1206: this.barCodeView.setVisibility(8);
      continue;
      label1218: RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)this.mainTitle.getLayoutParams();
      localLayoutParams.setMargins(ViewUtils.dip2px(this.context, 10.0F), ViewUtils.dip2px(this.context, 20.0F), ViewUtils.dip2px(this.context, 15.0F), 0);
      this.mainTitle.setLayoutParams(localLayoutParams);
      localLayoutParams = (RelativeLayout.LayoutParams)this.orderResultImageView.getLayoutParams();
      localLayoutParams.setMargins(0, ViewUtils.dip2px(this.context, 20.0F), 0, ViewUtils.dip2px(this.context, 10.0F));
      this.orderResultImageView.setLayoutParams(localLayoutParams);
      return;
      label1324: j = n - 1;
      continue;
      label1333: if (j >= i1)
        continue;
      int m = i;
    }
  }

  private void initView()
  {
    this.selectDishOrderResultLoadingLayout = super.findViewById(R.id.selectdish_orderresult_loading_layout);
    this.selectDishOrderResultLoadedLayout = ((PullToRefreshScrollView)super.findViewById(R.id.selectdish_orderresult_loaded_layout));
    this.selectDishOrderResultError = ((FrameLayout)super.findViewById(R.id.selectdish_orderresult_error));
    this.orderResultImageView = ((ImageView)super.findViewById(R.id.orderresult_imageview));
    this.mainTitle = ((TextView)super.findViewById(R.id.main_title));
    this.subTitle = ((TextView)super.findViewById(R.id.sub_title));
    this.barCodeView = ((ImageView)super.findViewById(R.id.barcode_imageview));
    this.mainInfoLayout = ((LinearLayout)super.findViewById(R.id.selectdish_orderresult_maininfo));
    this.subInfoLayout = ((LinearLayout)super.findViewById(R.id.selectdish_orderresult_subinfo));
    this.subInfoLine = super.findViewById(R.id.subinfo_line);
    this.gotoDetailLayout = ((RelativeLayout)super.findViewById(R.id.goto_orderdetail_layout));
    this.orderDetailLayout = ((LinearLayout)super.findViewById(R.id.selectdish_orderresult_menu));
    this.orderDetail = ((TableView)super.findViewById(R.id.selectdish_orderresult_menu_tableview));
    this.groupOnLayout = ((LinearLayout)super.findViewById(R.id.selectdish_orderresult_groupon));
    this.groupOnName = ((TextView)super.findViewById(R.id.selectdish_groupon_name_content));
    this.groupOnCode = ((LinearLayout)super.findViewById(R.id.selectdish_groupon_code_content));
    this.gotoGroupOnLayout = ((RelativeLayout)super.findViewById(R.id.goto_groupon_orderdetail));
    this.addReviewLayout = ((LinearLayout)super.findViewById(R.id.selectdish_addreview_layout));
    this.addReviewTitle = ((TextView)super.findViewById(R.id.selectdish_addreview_title));
    this.addReviewStatus = ((TextView)super.findViewById(R.id.selectdish_addreview_status));
    this.addReviewTip = ((TextView)super.findViewById(R.id.selectdish_addreview_tip));
    this.addReviewButton = ((NovaButton)super.findViewById(R.id.selectdish_addreview_button));
    this.twoButtonLayout = ((LinearLayout)super.findViewById(R.id.selectdish_orderresult_twobutton));
    this.oneButtonLayout = ((FrameLayout)super.findViewById(R.id.selectdish_orderresult_onebutton));
    this.leftButton = ((NovaButton)super.findViewById(R.id.selectdish_orderresult_left_button));
    this.rightButton = ((NovaButton)super.findViewById(R.id.selectdish_orderresult_right_button));
    this.middleButton = ((NovaButton)super.findViewById(R.id.selectdish_orderresult_middle_button));
    this.leftButtonSubTitle = ((TextView)super.findViewById(R.id.selectdish_orderresult_left_button_subtitle));
    this.rightButtonSubTitle = ((TextView)super.findViewById(R.id.selectdish_orderresult_right_button_subtitle));
    this.middleButtonSubTitle = ((TextView)super.findViewById(R.id.selectdish_orderresult_onebutton_subtitle));
  }

  private void openMenuViewForEdit()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishmenu"));
    localIntent.putExtra("close", false);
    localIntent.putExtra("shopid", this.dataSource.shopId);
    localIntent.putExtra("orderid", this.dataSource.orderId);
    localIntent.putExtra("orderviewid", this.dataSource.viewId);
    localIntent.putExtra("tablenum", this.dataSource.tableNum);
    localIntent.setFlags(67108864);
    startActivity(localIntent);
  }

  private void sendEditOrderNetworkRequest()
  {
    if (this.deleteOrderRequest != null)
    {
      mapiService().abort(this.deleteOrderRequest, this.mapiRequestHandler, true);
      this.deleteOrderRequest = null;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("orderviewid");
    localArrayList.add(this.dataSource.viewId);
    localArrayList.add("type");
    localArrayList.add("2");
    this.deleteOrderRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/orderdish/deleteorder.bin", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    mapiService().exec(this.deleteOrderRequest, this.mapiRequestHandler);
  }

  private void setButton(NovaButton paramNovaButton, TextView paramTextView, String paramString)
  {
    int i = -1;
    switch (paramString.hashCode())
    {
    default:
    case 48:
    case 49:
    case 50:
    case 51:
    case 52:
    case 53:
    }
    while (true)
      switch (i)
      {
      default:
        return;
        if (!paramString.equals("0"))
          continue;
        i = 0;
        continue;
        if (!paramString.equals("1"))
          continue;
        i = 1;
        continue;
        if (!paramString.equals("2"))
          continue;
        i = 2;
        continue;
        if (!paramString.equals("3"))
          continue;
        i = 3;
        continue;
        if (!paramString.equals("4"))
          continue;
        i = 4;
        continue;
        if (!paramString.equals("5"))
          continue;
        i = 5;
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      }
    paramNovaButton.setVisibility(4);
    paramTextView.setVisibility(4);
    return;
    paramNovaButton.setVisibility(0);
    paramTextView.setVisibility(4);
    paramNovaButton.setText("联系客服");
    setLightWeight(paramNovaButton, true);
    if (this.dataSource.status == 20)
      paramNovaButton.setGAString("succeed_service", this.gaUserInfo);
    while (true)
    {
      paramNovaButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          try
          {
            SelectDishOrderResultActivity.this.startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:400-820-5527")));
            return;
          }
          catch (Exception paramView)
          {
            Log.e("SelectDishOrderResultActivity", "can not dial", paramView);
          }
        }
      });
      return;
      if (this.dataSource.status != 30)
        continue;
      paramNovaButton.setGAString("failure_service", this.gaUserInfo);
    }
    paramNovaButton.setVisibility(0);
    if (!TextUtils.isEmpty(this.dataSource.orderButtonSubTitle))
    {
      paramTextView.setVisibility(0);
      paramTextView.setText(this.dataSource.orderButtonSubTitle);
    }
    while (true)
    {
      paramNovaButton.setText("优惠买单");
      setLightWeight(paramNovaButton, false);
      paramNovaButton.setGAString("pay", this.gaUserInfo);
      paramNovaButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(SelectDishOrderResultActivity.this.dataSource.payScheme));
          SelectDishOrderResultActivity.this.startActivity(paramView);
        }
      });
      return;
      paramTextView.setVisibility(4);
    }
    paramNovaButton.setVisibility(0);
    paramTextView.setVisibility(4);
    paramNovaButton.setText("重新付款");
    setLightWeight(paramNovaButton, false);
    paramNovaButton.setGAString("repay", this.gaUserInfo);
    paramNovaButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(SelectDishOrderResultActivity.this.dataSource.payScheme));
        SelectDishOrderResultActivity.this.startActivity(paramView);
      }
    });
    return;
    paramNovaButton.setVisibility(0);
    paramTextView.setVisibility(4);
    paramNovaButton.setText("我要加菜");
    setLightWeight(paramNovaButton, true);
    paramNovaButton.setGAString("add", this.gaUserInfo);
    paramNovaButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishmenu"));
        paramView.putExtra("close", false);
        paramView.putExtra("shopid", SelectDishOrderResultActivity.this.dataSource.shopId);
        paramView.putExtra("orderid", SelectDishOrderResultActivity.this.dataSource.orderId);
        paramView.putExtra("tablenum", SelectDishOrderResultActivity.this.dataSource.tableNum);
        paramView.setFlags(67108864);
        SelectDishOrderResultActivity.this.startActivity(paramView);
      }
    });
    return;
    paramNovaButton.setVisibility(0);
    paramTextView.setVisibility(4);
    paramNovaButton.setText("重新编辑菜品");
    setLightWeight(paramNovaButton, true);
    paramNovaButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SelectDishOrderResultActivity.this.showEditOrderConfirmAlert();
      }
    });
  }

  private void setLightWeight(NovaButton paramNovaButton, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      paramNovaButton.setBackgroundResource(R.drawable.btn_light);
      paramNovaButton.setTextColor(getResources().getColor(R.color.hobbit_deep_gray));
      paramNovaButton.setShadowLayer(0.0F, 0.0F, 1.0F, R.color.white);
      return;
    }
    paramNovaButton.setBackgroundResource(R.drawable.btn_weight);
    paramNovaButton.setTextColor(getResources().getColor(R.color.white));
    paramNovaButton.setShadowLayer(0.0F, 0.0F, 1.0F, 11153940);
  }

  private void showEditOrderConfirmAlert()
  {
    SelectDishAlertDialog localSelectDishAlertDialog = new SelectDishAlertDialog(this, "修改点单订单", "原订单将失效，再次提交菜品后，\n将生成新订单进行下单！", "取消", "继续修改");
    localSelectDishAlertDialog.setCanceledOnTouchOutside(true);
    localSelectDishAlertDialog.setDialogClickListener(new SelectDishAlertDialog.OnDialogClickListener()
    {
      public void onCancelClick(Dialog paramDialog)
      {
        paramDialog.dismiss();
      }

      public void onConfirmClick(Dialog paramDialog)
      {
        paramDialog.dismiss();
        GAHelper.instance().contextStatisticsEvent(SelectDishOrderResultActivity.this, "edit", SelectDishOrderResultActivity.this.gaUserInfo, "tap");
        SelectDishOrderResultActivity.this.showProgressDialog("请求修改订单中，请稍候...");
        SelectDishOrderResultActivity.this.sendEditOrderNetworkRequest();
      }
    });
    localSelectDishAlertDialog.show();
  }

  public View createInfoView(float paramFloat1, float paramFloat2, String paramString1, String paramString2, float paramFloat3, int paramInt)
  {
    View localView = getLayoutInflater().inflate(R.layout.selectdish_orderresult_info_item, null);
    TextView localTextView1 = (TextView)localView.findViewById(R.id.key);
    TextView localTextView2 = (TextView)localView.findViewById(R.id.colon);
    TextView localTextView3 = (TextView)localView.findViewById(R.id.value);
    localTextView1.setTextSize(0, paramFloat3);
    localTextView2.setTextSize(0, paramFloat3);
    localTextView3.setTextSize(0, paramFloat3);
    localTextView1.setTextColor(paramInt);
    localTextView2.setTextColor(paramInt);
    localTextView3.setTextColor(paramInt);
    localTextView1.setWidth((int)paramFloat1);
    localTextView2.setWidth((int)paramFloat2);
    localTextView1.setText(paramString1);
    localTextView3.setText(paramString2);
    return localView;
  }

  public void displayLoadingView(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.selectDishOrderResultLoadingLayout.setVisibility(0);
      this.selectDishOrderResultLoadedLayout.setVisibility(8);
      this.selectDishOrderResultError.setVisibility(8);
      return;
    }
    this.selectDishOrderResultLoadingLayout.setVisibility(8);
    this.selectDishOrderResultLoadedLayout.setVisibility(0);
    this.selectDishOrderResultError.setVisibility(8);
  }

  public String getPageName()
  {
    return "menuorder_orderresult";
  }

  public float getTextWidth(String paramString, float paramFloat)
  {
    TextView localTextView = new TextView(this);
    localTextView.setTextSize(0, paramFloat);
    return localTextView.getPaint().measureText(paramString);
  }

  public void loadPayResult()
  {
    this.gaUserInfo.shop_id = Integer.valueOf(this.dataSource.shopId);
    this.gaUserInfo.title = "menuorder";
    displayLoadingView(false);
    this.selectDishOrderResultLoadedLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshBase<ScrollView> paramPullToRefreshBase)
      {
        SelectDishOrderResultActivity.this.dataSource.requestPayResult();
      }
    });
    initTitle();
    initInfo();
    initOrder();
    initGroupOnInfo();
    initReviewAndButton();
  }

  public void loadPayResultFail()
  {
    this.selectDishOrderResultError.removeAllViews();
    this.selectDishOrderResultError.addView(getFailedView("网络连接失败 点击重新加载", new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        SelectDishOrderResultActivity.this.dataSource.pendingRetry = 19;
        SelectDishOrderResultActivity.this.displayLoadingView(true);
        SelectDishOrderResultActivity.this.dataSource.requestPayResult();
      }
    }));
    this.selectDishOrderResultLoadingLayout.setVisibility(8);
    this.selectDishOrderResultLoadedLayout.setVisibility(8);
    this.selectDishOrderResultError.setVisibility(0);
  }

  public void onClick(View paramView)
  {
    AddReviewUtil.addReview(this, this.dataSource.shopId, this.dataSource.shopName);
    this.dataSource.reviewStatus = SelectDishOrderResultDataSource.ReviewStatus.OPEN_IN_EDIT;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.dataSource = new SelectDishOrderResultDataSource(this);
    this.dataSource.dataLoaderListener = this;
    this.context = getBaseContext();
    if (paramBundle == null)
      this.dataSource.queryType = getIntParam("querytype");
    for (this.dataSource.orderId = getIntParam("orderid"); ; this.dataSource.orderId = paramBundle.getInt("orderid"))
    {
      GAHelper.instance().setGAPageName(getPageName());
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          NewCartManager.getInstance().emptyCart();
          NewCartManager.getInstance().deleteCartStorage();
        }
      }
      , 1000L);
      super.setContentView(R.layout.selectdish_orderresult_layout);
      super.setTitle("");
      initView();
      displayLoadingView(true);
      this.dataSource.requestPayResult();
      return;
      this.dataSource.queryType = paramBundle.getInt("querytype");
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.deleteOrderRequest != null)
    {
      mapiService().abort(this.deleteOrderRequest, this.mapiRequestHandler, true);
      this.deleteOrderRequest = null;
    }
    this.dataSource.releaseRequests();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      exit();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onLeftTitleButtonClicked()
  {
    exit();
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.dataSource.orderId = paramBundle.getInt("orderid");
    this.dataSource.queryType = paramBundle.getInt("querytype");
  }

  protected void onResume()
  {
    super.onResume();
    if (this.dataSource.reviewStatus == SelectDishOrderResultDataSource.ReviewStatus.OPEN_IN_EDIT)
      this.dataSource.requestPayResult();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("querytype", this.dataSource.queryType);
    paramBundle.putInt("orderid", this.dataSource.orderId);
  }

  public class SelectDishOrderDetailAdapter extends BaseAdapter
  {
    public SelectDishOrderDetailAdapter()
    {
    }

    public int getCount()
    {
      return SelectDishOrderResultActivity.this.dataSource.orderEntries.length;
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
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = SelectDishOrderResultActivity.this.getLayoutInflater().inflate(R.layout.selectdish_orderresult_menu_item, null);
      Object localObject1 = SelectDishOrderResultActivity.this.dataSource.orderEntries[paramInt];
      Object localObject2 = (RelativeLayout)paramViewGroup.findViewById(R.id.orderentry);
      Object localObject3 = (TextView)paramViewGroup.findViewById(R.id.giftname);
      Object localObject4 = (TextView)paramViewGroup.findViewById(R.id.skuname);
      TextView localTextView2 = (TextView)paramViewGroup.findViewById(R.id.skuprice);
      TextView localTextView3 = (TextView)paramViewGroup.findViewById(R.id.skuquantity);
      TextView localTextView1 = (TextView)paramViewGroup.findViewById(R.id.orgprice);
      LinearLayout localLinearLayout = (LinearLayout)paramViewGroup.findViewById(R.id.setmeal);
      if (TextUtils.isEmpty(((DPObject)localObject1).getString("GiftTag")))
      {
        paramView = "";
        ((TextView)localObject3).setText(paramView);
        ((TextView)localObject4).setText(((DPObject)localObject1).getString("SkuName"));
        paramView = Double.valueOf(((DPObject)localObject1).getDouble("SkusPrice"));
        localObject3 = Double.valueOf(((DPObject)localObject1).getDouble("SkusOriginPrice"));
        localObject4 = new BigDecimal(SelectDishOrderResultActivity.PRICE_DF.format(paramView));
        localTextView2.setText("¥" + ((BigDecimal)localObject4).stripTrailingZeros().toPlainString());
        localTextView2.setWidth((int)SelectDishOrderResultActivity.this.skusPriceWidth);
        localTextView3.setText(((DPObject)localObject1).getInt("SkuQuantity") + "份");
        localTextView3.setWidth((int)SelectDishOrderResultActivity.this.skuQuantityWidth);
        localTextView1.getPaint().setFlags(16);
        if ((((Double)localObject3).doubleValue() != 0.0D) && (((Double)localObject3).doubleValue() > paramView.doubleValue()))
          break label376;
        localTextView1.setVisibility(8);
        ((RelativeLayout)localObject2).getLayoutParams().height = ViewUtils.dip2px(SelectDishOrderResultActivity.this.context, 45.0F);
        label339: paramView = ((DPObject)localObject1).getArray("SetMealSkus");
        if ((paramView != null) && (paramView.length != 0))
          break label445;
        localLinearLayout.setVisibility(8);
      }
      while (true)
      {
        return paramViewGroup;
        paramView = ((DPObject)localObject1).getString("GiftTag");
        break;
        label376: paramView = new BigDecimal(SelectDishOrderResultActivity.PRICE_DF.format(localObject3));
        localTextView1.setText("¥" + paramView.stripTrailingZeros().toPlainString());
        ((RelativeLayout)localObject2).getLayoutParams().height = ViewUtils.dip2px(SelectDishOrderResultActivity.this.context, 60.0F);
        break label339;
        label445: localLinearLayout.setVisibility(0);
        paramInt = 0;
        while (paramInt < paramView.length)
        {
          localObject1 = SelectDishOrderResultActivity.this.getLayoutInflater().inflate(R.layout.selectdish_orderresult_setmeal_item, null);
          localObject2 = (TextView)((View)localObject1).findViewById(R.id.setmeal);
          localTextView1 = (TextView)((View)localObject1).findViewById(R.id.setmealnum);
          ((TextView)localObject2).setText(paramView[paramInt].getString("Key"));
          localTextView1.setText(paramView[paramInt].getString("Value"));
          localLinearLayout.addView((View)localObject1);
          paramInt += 1;
        }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.ui.SelectDishOrderResultActivity
 * JD-Core Version:    0.6.0
 */