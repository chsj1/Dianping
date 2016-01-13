package com.dianping.takeaway.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.SimpleMsg;
import com.dianping.takeaway.entity.TakeawayAddress;
import com.dianping.takeaway.entity.TakeawayAddressDataSource;
import com.dianping.takeaway.entity.TakeawayAddressDataSource.DataLoadListener;
import com.dianping.takeaway.entity.TakeawayAddressDataSource.DataStatus;
import com.dianping.takeaway.entity.TakeawayHistoryAddressAdapter;
import com.dianping.takeaway.entity.TakeawayNetLoadStatus;
import com.dianping.takeaway.entity.TakeawaySuggestAddressAdapter;
import com.dianping.takeaway.view.TAToastView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaImageView;
import java.util.List;

public class TakeawayAddressActivity extends NovaActivity
  implements TakeawayAddressDataSource.DataLoadListener
{
  protected final int KEYBOARD_SHOW = 40;
  protected final int PAGE_SWITCH = 20;
  protected final int TOAST_DISAPPER = 30;
  protected NovaActivity activity = this;
  protected TakeawayAddressDataSource addressDataSource;
  protected EditText addressEditText;
  protected ImageButton backBtn;
  protected Button cancelBtn;
  protected ImageView clearButton;
  protected Context context = this;
  protected View emptyView;
  public TakeawayHistoryAddressAdapter historyAdapter;
  protected TextView historyHeaderView;
  protected ListView historyListView;
  protected View historyView;
  protected NovaImageView locateBtn;
  protected Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
      case 20:
      case 30:
      case 40:
      }
      while (true)
      {
        super.handleMessage(paramMessage);
        return;
        if (TakeawayAddressActivity.this.addressDataSource.locateAddress == null)
          continue;
        TakeawayAddressActivity.this.getAddressSuccess(TakeawayAddressActivity.this.addressDataSource.locateAddress);
        continue;
        TakeawayAddressActivity.this.taToastView.hideToast();
        continue;
        InputMethodManager localInputMethodManager = (InputMethodManager)TakeawayAddressActivity.this.addressEditText.getContext().getSystemService("input_method");
        TakeawayAddressActivity.this.addressEditText.requestFocus();
        localInputMethodManager.showSoftInput(TakeawayAddressActivity.this.addressEditText, 2);
      }
    }
  };
  protected TakeawaySuggestAddressAdapter suggestAdapter;
  protected ListView suggestListView;
  protected TAToastView taToastView;
  protected TitleBar titleBar;

  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.addressDataSource.canClick)
      return super.dispatchTouchEvent(paramMotionEvent);
    return false;
  }

  protected void getAddressSuccess(TakeawayAddress paramTakeawayAddress)
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("Address", paramTakeawayAddress.address);
    localIntent.putExtra("Lat", paramTakeawayAddress.lat);
    localIntent.putExtra("Lng", paramTakeawayAddress.lng);
    setResult(-1, localIntent);
    finish();
  }

  protected String getEditTextContent()
  {
    return this.addressEditText.getText().toString().trim();
  }

  public String getPageName()
  {
    return "takeawayselectaddress";
  }

  protected void hideSuggestListView()
  {
    this.addressDataSource.suggestList.clear();
    this.suggestAdapter.notifyDataSetChanged();
    this.suggestListView.setVisibility(8);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void loadFinish(TakeawayAddressDataSource.DataStatus paramDataStatus, Object paramObject)
  {
    switch (13.$SwitchMap$com$dianping$takeaway$entity$TakeawayAddressDataSource$DataStatus[paramDataStatus.ordinal()])
    {
    default:
      return;
    case 1:
    }
    dismissDialog();
    this.addressDataSource.getClass();
    showToastMsg("无法获取您当前的位置");
  }

  public void loadSuggestFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject)
  {
    switch (13.$SwitchMap$com$dianping$takeaway$entity$TakeawayNetLoadStatus[paramTakeawayNetLoadStatus.ordinal()])
    {
    default:
    case 1:
      do
      {
        return;
        paramTakeawayNetLoadStatus = ((DPObject)paramObject).getArray("GeoResult");
      }
      while (paramTakeawayNetLoadStatus == null);
      this.addressDataSource.suggestList.clear();
      int j = paramTakeawayNetLoadStatus.length;
      int i = 0;
      while (i < j)
      {
        paramObject = paramTakeawayNetLoadStatus[i];
        paramObject = new TakeawayAddress(paramObject.getString("Address"), paramObject.getString("AddressDetail"), paramObject.getDouble("Lat"), paramObject.getDouble("Lng"));
        this.addressDataSource.suggestList.add(paramObject);
        i += 1;
      }
      this.suggestAdapter.notifyDataSetChanged();
      paramObject = this.emptyView;
      if (paramTakeawayNetLoadStatus.length == 0);
      for (i = 0; ; i = 8)
      {
        paramObject.setVisibility(i);
        return;
      }
    case 2:
    }
    paramTakeawayNetLoadStatus = (MApiResponse)paramObject;
    if ((paramTakeawayNetLoadStatus != null) && (paramTakeawayNetLoadStatus.message() != null))
    {
      showToastMsg(paramTakeawayNetLoadStatus.message().content());
      return;
    }
    showToastMsg("网络不给力哦，请稍后再试");
  }

  public void locateFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject)
  {
    switch (13.$SwitchMap$com$dianping$takeaway$entity$TakeawayNetLoadStatus[paramTakeawayNetLoadStatus.ordinal()])
    {
    default:
    case 3:
    case 2:
    case 1:
    }
    while (true)
    {
      return;
      showCustomToast("正在定位...", -1L, true);
      return;
      this.addressDataSource.getClass();
      showCustomToast("无法获取您当前的位置", 1000L, false);
      return;
      paramTakeawayNetLoadStatus = (DPObject)paramObject;
      paramObject = paramTakeawayNetLoadStatus.getString("Address");
      if (!"1".equals(this.addressDataSource.source))
        break;
      if (TextUtils.isEmpty(paramObject))
        continue;
      showCustomToast("已定位到\"" + paramObject + "\"附近", -1L, false);
      this.addressDataSource.locateAddress = new TakeawayAddress(paramTakeawayNetLoadStatus.getString("Address"), paramTakeawayNetLoadStatus.getDouble("Lat"), paramTakeawayNetLoadStatus.getDouble("Lng"));
      this.addressDataSource.addAddress(this.addressDataSource.locateAddress);
      this.mHandler.sendEmptyMessageDelayed(20, 1500L);
      return;
    }
    this.addressDataSource.canClick = true;
    setEditTextContent(paramObject, true);
    this.taToastView.hideToast();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.takeaway_address);
    this.addressDataSource = new TakeawayAddressDataSource(this.activity);
    this.addressDataSource.setDataLoadListener(this);
    this.suggestAdapter = new TakeawaySuggestAddressAdapter(this.context, this.addressDataSource.suggestList);
    this.historyAdapter = new TakeawayHistoryAddressAdapter(this.context, this.addressDataSource.historyList);
    setupTitleBar();
    setupContentView();
  }

  protected void onDestroy()
  {
    this.addressDataSource.onDestroy();
    super.onDestroy();
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 4) && ((this.addressEditText.hasFocus()) || (!TextUtils.isEmpty(this.addressEditText.getText()))))
    {
      updateViewIfInput(false);
      return true;
    }
    return super.onKeyUp(paramInt, paramKeyEvent);
  }

  protected void setEditTextContent(String paramString, boolean paramBoolean)
  {
    if (paramString == null)
      return;
    this.addressEditText.setText(paramString);
    this.addressEditText.setSelection(paramString.length());
    setSoftInputVisibility(paramBoolean);
  }

  protected void setSoftInputVisibility(boolean paramBoolean)
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)this.addressEditText.getContext().getSystemService("input_method");
    if (paramBoolean)
    {
      this.mHandler.sendEmptyMessageDelayed(40, 300L);
      return;
    }
    localInputMethodManager.hideSoftInputFromWindow(this.addressEditText.getWindowToken(), 0);
  }

  protected void setupContentView()
  {
    this.clearButton = ((ImageView)findViewById(R.id.clear_btn));
    this.locateBtn = ((NovaImageView)findViewById(R.id.lacate_btn));
    this.addressEditText = ((EditText)findViewById(R.id.address_edit));
    this.addressEditText.setHint("请输入要更换的位置");
    this.addressEditText.clearFocus();
    this.clearButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayAddressActivity.this.setEditTextContent("", true);
      }
    });
    this.locateBtn.setGAString("locate");
    this.locateBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayAddressActivity.this.setSoftInputVisibility(false);
        TakeawayAddressActivity.this.addressEditText.clearFocus();
        TakeawayAddressActivity.this.addressDataSource.canClick = false;
        TakeawayAddressActivity.this.addressDataSource.sendLocateRequest();
        TakeawayAddressActivity.this.statisticsEvent("takeaway6", "takeaway6_locate_click", null, 0);
      }
    });
    this.addressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramView, boolean paramBoolean)
      {
        if (paramBoolean)
          TakeawayAddressActivity.this.updateViewIfInput(true);
      }
    });
    this.addressEditText.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
        if (!TextUtils.isEmpty(paramEditable))
        {
          TakeawayAddressActivity.this.clearButton.setVisibility(0);
          TakeawayAddressActivity.this.locateBtn.setVisibility(8);
          if ((paramEditable.length() >= 2) && (TakeawayAddressActivity.this.addressDataSource.permitSuggest))
          {
            TakeawayAddressActivity.this.historyView.setVisibility(8);
            TakeawayAddressActivity.this.suggestListView.setVisibility(0);
            TakeawayAddressActivity.this.addressDataSource.sendSuggestRequest(paramEditable.toString());
            return;
          }
          TakeawayAddressActivity.this.hideSuggestListView();
          return;
        }
        TakeawayAddressActivity.this.clearButton.setVisibility(8);
        TakeawayAddressActivity.this.locateBtn.setVisibility(0);
        TakeawayAddressActivity.this.emptyView.setVisibility(8);
        TakeawayAddressActivity.this.hideSuggestListView();
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    });
    this.addressEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
    {
      public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
      {
        int j = 0;
        int i;
        if (paramInt != 3)
        {
          i = j;
          if (paramInt == 0)
          {
            i = j;
            if (paramKeyEvent.getKeyCode() != 66);
          }
        }
        else
        {
          if (!TextUtils.isEmpty(TakeawayAddressActivity.this.getEditTextContent()))
            break label57;
          TakeawayAddressActivity.this.showToastMsg("请输入地址");
        }
        while (true)
        {
          i = 1;
          return i;
          label57: TakeawayAddressActivity.this.setSoftInputVisibility(false);
          TakeawayAddressActivity.this.addressEditText.clearFocus();
          TakeawayAddressActivity.this.statisticsEvent("takeaway6", "takeaway6_add_input", TakeawayAddressActivity.this.getEditTextContent(), 0);
        }
      }
    });
    this.suggestListView = ((ListView)findViewById(R.id.suggestion_listview));
    this.suggestListView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        TakeawayAddressActivity.this.setSoftInputVisibility(false);
        return false;
      }
    });
    this.suggestListView.setAdapter(this.suggestAdapter);
    this.suggestListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        paramAdapterView = (TakeawayAddress)TakeawayAddressActivity.this.addressDataSource.suggestList.get(paramInt);
        TakeawayAddressActivity.this.setEditTextContent(paramAdapterView.address, false);
        TakeawayAddressActivity.this.addressDataSource.permitSuggest = false;
        paramView = TakeawayAddressActivity.this.addressDataSource;
        TakeawayAddressActivity.this.addressDataSource.getClass();
        paramView.sendValidateRequest(2, paramAdapterView);
        TakeawayAddressActivity.this.statisticsEvent("takeaway6", "takeaway6_add_suggestclk", "", paramInt + 1);
        paramAdapterView = TakeawayAddressActivity.this.addressDataSource.getGAUserInfo();
        paramAdapterView.query_id = TakeawayAddressActivity.this.addressDataSource.queryId;
        paramAdapterView.index = Integer.valueOf(paramInt);
        GAHelper.instance().contextStatisticsEvent(TakeawayAddressActivity.this.addressDataSource.getActivity(), "suggest", paramAdapterView, "tap");
      }
    });
    this.historyView = findViewById(R.id.history_layout);
    updateHistoryViewVisibility();
    this.historyHeaderView = ((TextView)findViewById(R.id.history_header));
    this.historyHeaderView.setText("曾经换过的");
    this.historyListView = ((ListView)findViewById(R.id.history_listview));
    this.historyListView.setAdapter(this.historyAdapter);
    this.historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        if (TakeawayAddressActivity.this.addressDataSource.isLoacting())
          return;
        TakeawayAddressActivity.this.addressDataSource.permitSuggest = false;
        paramAdapterView = (TakeawayAddress)TakeawayAddressActivity.this.addressDataSource.historyList.get(TakeawayAddressActivity.this.addressDataSource.historyList.size() - 1 - paramInt);
        TakeawayAddressActivity.this.setEditTextContent(paramAdapterView.address, false);
        paramView = TakeawayAddressActivity.this.addressDataSource;
        TakeawayAddressActivity.this.addressDataSource.getClass();
        paramView.sendValidateRequest(2, paramAdapterView);
        TakeawayAddressActivity.this.statisticsEvent("takeaway6", "takeaway6_locate_history", null, 0);
        GAHelper.instance().contextStatisticsEvent(TakeawayAddressActivity.this.addressDataSource.getActivity(), "history", null, "tap");
      }
    });
    this.emptyView = findViewById(R.id.emptyView);
    this.emptyView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        TakeawayAddressActivity.this.setSoftInputVisibility(false);
        return false;
      }
    });
    this.taToastView = ((TAToastView)findViewById(R.id.taToastView));
  }

  protected void setupTitleBar()
  {
    this.titleBar = super.getTitleBar();
    this.backBtn = ((ImageButton)getLayoutInflater().inflate(R.layout.takeaway_back_btn, null));
    this.backBtn.setPadding(ViewUtils.dip2px(this, 15.0F), ViewUtils.dip2px(this, 5.0F), ViewUtils.dip2px(this, 15.0F), ViewUtils.dip2px(this, 5.0F));
    this.backBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayAddressActivity.this.finish();
      }
    });
    this.cancelBtn = ((Button)getLayoutInflater().inflate(R.layout.takeaway_cancel_btn, null));
    this.cancelBtn.setPadding(ViewUtils.dip2px(this, 15.0F), 0, ViewUtils.dip2px(this, 15.0F), 0);
    this.cancelBtn.setText("取消");
    this.cancelBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayAddressActivity.this.updateViewIfInput(false);
      }
    });
    this.titleBar.setCustomLeftView(this.backBtn);
    this.titleBar.setTitle("更换位置");
  }

  protected void showCustomToast(String paramString, long paramLong, boolean paramBoolean)
  {
    if (TextUtils.isEmpty(paramString))
    {
      this.taToastView.hideToast();
      this.addressDataSource.canClick = true;
    }
    do
    {
      return;
      this.taToastView.showToast(paramString, paramBoolean);
    }
    while (paramLong <= 0L);
    this.mHandler.sendEmptyMessageDelayed(30, paramLong);
  }

  protected void showToastMsg(String paramString)
  {
    paramString = Toast.makeText(this, paramString, 0);
    paramString.setGravity(17, 0, 0);
    paramString.show();
  }

  protected void updateHistoryViewVisibility()
  {
    if ((!this.addressEditText.hasFocus()) && (!this.addressDataSource.historyList.isEmpty()))
    {
      this.historyView.setVisibility(0);
      return;
    }
    this.historyView.setVisibility(8);
  }

  protected void updateViewIfInput(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      setSoftInputVisibility(true);
      this.titleBar.setCustomLeftView(this.cancelBtn);
      this.historyView.setVisibility(8);
    }
    do
    {
      return;
      this.titleBar.setCustomLeftView(this.backBtn);
      setEditTextContent("", false);
      this.addressEditText.clearFocus();
      hideSuggestListView();
      this.emptyView.setVisibility(8);
    }
    while (this.historyAdapter.getCount() <= 0);
    this.historyView.setVisibility(0);
  }

  public void validateAddressFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject)
  {
    switch (13.$SwitchMap$com$dianping$takeaway$entity$TakeawayNetLoadStatus[paramTakeawayNetLoadStatus.ordinal()])
    {
    default:
    case 3:
    case 4:
    case 1:
      do
      {
        return;
        showProgressDialog("载入中...");
        return;
        dismissDialog();
        return;
      }
      while (paramObject == null);
      paramTakeawayNetLoadStatus = (DPObject)paramObject;
      paramTakeawayNetLoadStatus = new TakeawayAddress(getEditTextContent(), paramTakeawayNetLoadStatus.getDouble("Lat"), paramTakeawayNetLoadStatus.getDouble("Lng"));
      this.addressDataSource.addAddress(paramTakeawayNetLoadStatus);
      getAddressSuccess(paramTakeawayNetLoadStatus);
      return;
    case 2:
    }
    paramTakeawayNetLoadStatus = (MApiResponse)paramObject;
    if ((paramTakeawayNetLoadStatus != null) && (paramTakeawayNetLoadStatus.message() != null))
    {
      statisticsEvent("takeaway6", "takeaway6_add_error", getEditTextContent(), 0);
      showToastMsg(paramTakeawayNetLoadStatus.message().content());
      return;
    }
    showToastMsg("网络不给力哦，请稍后再试");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayAddressActivity
 * JD-Core Version:    0.6.0
 */