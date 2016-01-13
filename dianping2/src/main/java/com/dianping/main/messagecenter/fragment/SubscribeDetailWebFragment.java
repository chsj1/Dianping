package com.dianping.main.messagecenter.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.web.client.NovaZeusWebViewClient;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.zeus.client.ZeusWebViewClient;
import com.dianping.zeus.ui.ZeusFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubscribeDetailWebFragment extends NovaZeusFragment
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  String mDesc;
  int mSubType;
  View mSvWeb;
  private String mSwitchId;
  TextView mTvDesc;
  TextView mTvPrevious;
  TextView mTvTip;
  View mVwDivider;
  View mVwLoading;

  private void initView(View paramView)
  {
    this.mTvPrevious = ((TextView)paramView.findViewById(R.id.previous));
    this.mTvPrevious.setOnClickListener(this);
    this.mTvDesc = ((TextView)paramView.findViewById(R.id.desc));
    if (TextUtils.isEmpty(this.url))
      getActivity().finish();
    if (!TextUtils.isEmpty(this.mDesc))
      this.mTvDesc.setText(this.mDesc);
    this.mTvTip = ((TextView)paramView.findViewById(R.id.tip));
    SpannableString localSpannableString = new SpannableString("如果您不希望收到此类广播，点击退订");
    localSpannableString.setSpan(new ClickableSpan()
    {
      public void onClick(View paramView)
      {
        SubscribeDetailWebFragment.this.showCancelDialog();
      }

      public void updateDrawState(TextPaint paramTextPaint)
      {
        paramTextPaint.setColor(-16776961);
        paramTextPaint.setUnderlineText(true);
      }
    }
    , "如果您不希望收到此类广播，点击退订".indexOf("退订"), "如果您不希望收到此类广播，点击退订".indexOf("退订") + "退订".length(), 33);
    this.mTvTip.setText(localSpannableString);
    this.mTvTip.setMovementMethod(LinkMovementMethod.getInstance());
    this.mVwLoading = paramView.findViewById(R.id.loading);
    this.mVwLoading.setVisibility(0);
    this.mVwDivider = paramView.findViewById(R.id.divider);
    this.mSvWeb = paramView.findViewById(R.id.sv_web);
    this.mSvWeb.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getAction() == 1)
        {
          SubscribeDetailWebFragment.this.webView.requestDisallowInterceptTouchEvent(false);
          return false;
        }
        SubscribeDetailWebFragment.this.webView.requestDisallowInterceptTouchEvent(true);
        return false;
      }
    });
  }

  protected ZeusWebViewClient createWebViewClient()
  {
    return new SubscribeDetailWebViewClient(this);
  }

  public int getWebLayoutId()
  {
    return R.layout.subscribe_web;
  }

  public WebView getWebView(View paramView)
  {
    return (WebView)paramView.findViewById(R.id.webview);
  }

  public void handleArgments()
  {
    super.handleArgments();
    Bundle localBundle = getArguments();
    if (localBundle != null)
    {
      this.mSwitchId = localBundle.getString("switchid");
      this.mDesc = localBundle.getString("desc");
      this.mSubType = localBundle.getInt("subtype", -1);
    }
  }

  public void onClick(View paramView)
  {
    if ((paramView.getId() == R.id.previous) && (this.mSubType > -1))
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://subscribelist?type=3&subtype=" + this.mSubType + "&title=" + this.mTitle)));
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mTvTip.setText("您已退订，下次将不会收到");
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    initView(paramView);
    this.webView.setVisibility(8);
  }

  void sendCancelSubscribeRequest()
  {
    Object localObject = new JSONArray();
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", Integer.valueOf(this.mSwitchId));
      localJSONObject.put("status", false);
      ((JSONArray)localObject).put(localJSONObject);
      localObject = BasicMApiRequest.mapiPost("http://m.api.dianping.com/updatenotificationform.bin", new String[] { "config", ((JSONArray)localObject).toString() });
      ((NovaActivity)getActivity()).mapiService().exec((Request)localObject, this);
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
        Log.d("number format erro");
    }
    catch (JSONException localJSONException)
    {
      while (true)
        Log.d("json erro");
    }
  }

  void showCancelDialog()
  {
    new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("是否退订该消息？").setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        SubscribeDetailWebFragment.this.sendCancelSubscribeRequest();
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
      }
    }).show();
  }

  private class SubscribeDetailWebViewClient extends NovaZeusWebViewClient
  {
    public SubscribeDetailWebViewClient(ZeusFragment arg2)
    {
      super();
    }

    public void onPageFinished(WebView paramWebView, String paramString)
    {
      super.onPageFinished(paramWebView, paramString);
      SubscribeDetailWebFragment.this.mVwLoading.setVisibility(8);
      SubscribeDetailWebFragment.this.webView.setVisibility(0);
      SubscribeDetailWebFragment.this.mTvTip.setVisibility(0);
      if (!TextUtils.isEmpty(SubscribeDetailWebFragment.this.mDesc))
        SubscribeDetailWebFragment.this.mTvDesc.setVisibility(0);
      if (SubscribeDetailWebFragment.this.mSubType > -1)
      {
        SubscribeDetailWebFragment.this.mTvPrevious.setVisibility(0);
        SubscribeDetailWebFragment.this.mVwDivider.setVisibility(0);
      }
      if (!TextUtils.isEmpty(SubscribeDetailWebFragment.this.mTitle))
        SubscribeDetailWebFragment.this.setTitle(SubscribeDetailWebFragment.this.mTitle);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.messagecenter.fragment.SubscribeDetailWebFragment
 * JD-Core Version:    0.6.0
 */