package com.dianping.takeaway.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.SimpleMsg;
import com.dianping.takeaway.entity.TakeawayNetLoadStatus;
import com.dianping.takeaway.entity.TakeawayPoiDataSource;
import com.dianping.takeaway.entity.TakeawayPoiDataSource.DataLoadListener;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.Arrays;
import java.util.List;

public class TakeawayPoiSearchActivity extends NovaActivity
  implements View.OnClickListener, TakeawayPoiDataSource.DataLoadListener
{
  protected NovaActivity activity = this;
  private Button cancelButton;
  private ImageView clearButton;
  protected Context context = this;
  protected View emptyView;
  protected TakeawayPoiDataSource poiDataSource;
  private EditText poiEditView;
  private TakeawayPoiAdapter poiListAdapter;
  private ListView poiListView;

  private void setupView()
  {
    this.clearButton = ((ImageView)findViewById(R.id.clear_button));
    this.clearButton.setOnClickListener(this);
    this.cancelButton = ((Button)findViewById(R.id.cancel_button));
    this.cancelButton.setOnClickListener(this);
    this.poiEditView = ((EditText)findViewById(R.id.poi_search));
    KeyboardUtils.popupKeyboard(this.poiEditView);
    this.poiEditView.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
        paramEditable = paramEditable.toString();
        ImageView localImageView = TakeawayPoiSearchActivity.this.clearButton;
        if (TextUtils.isEmpty(paramEditable));
        for (int i = 8; ; i = 0)
        {
          localImageView.setVisibility(i);
          TakeawayPoiSearchActivity.this.poiDataSource.getSuggestPoiTask(paramEditable);
          return;
        }
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    });
    this.poiListView = ((ListView)findViewById(R.id.poi_list));
    this.poiListView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getAction() == 0)
        {
          TakeawayPoiSearchActivity.this.poiEditView.clearFocus();
          TakeawayPoiSearchActivity.this.forceHideKeyboard();
        }
        return false;
      }
    });
    this.poiListAdapter = new TakeawayPoiAdapter();
    this.poiListView.setAdapter(this.poiListAdapter);
    this.poiListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        paramView = new Intent();
        paramView.putExtra("PoiAddress", (DPObject)paramAdapterView.getItemAtPosition(paramInt));
        TakeawayPoiSearchActivity.this.setResult(-1, paramView);
        TakeawayPoiSearchActivity.this.finish();
      }
    });
    this.emptyView = findViewById(R.id.empty_view);
    this.emptyView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        TakeawayPoiSearchActivity.this.forceHideKeyboard();
        return false;
      }
    });
    if (!TextUtils.isEmpty(this.poiDataSource.keyword))
    {
      setEditTextContent(this.poiDataSource.keyword);
      this.clearButton.setVisibility(0);
    }
  }

  public void finish()
  {
    forceHideKeyboard();
    super.finish();
  }

  protected void forceHideKeyboard()
  {
    ((InputMethodManager)getSystemService("input_method")).hideSoftInputFromWindow(this.poiEditView.getWindowToken(), 0);
  }

  public void loadPoiFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject)
  {
    switch (5.$SwitchMap$com$dianping$takeaway$entity$TakeawayNetLoadStatus[paramTakeawayNetLoadStatus.ordinal()])
    {
    default:
    case 1:
      do
      {
        return;
        paramTakeawayNetLoadStatus = ((DPObject)paramObject).getArray("PoiResults");
      }
      while (paramTakeawayNetLoadStatus == null);
      this.poiDataSource.poiList.clear();
      this.poiDataSource.poiList.addAll(Arrays.asList(paramTakeawayNetLoadStatus));
      this.poiListAdapter.notifyDataSetChanged();
      if (!this.poiDataSource.isLocal)
      {
        if (paramTakeawayNetLoadStatus.length == 0)
        {
          this.emptyView.setVisibility(0);
          this.poiListView.setVisibility(8);
          return;
        }
        this.emptyView.setVisibility(8);
        this.poiListView.setVisibility(0);
        return;
      }
      this.emptyView.setVisibility(8);
      this.poiListView.setVisibility(0);
      return;
    case 2:
    }
    MApiResponse localMApiResponse = (MApiResponse)paramObject;
    paramObject = "";
    paramTakeawayNetLoadStatus = paramObject;
    if (localMApiResponse != null)
    {
      paramTakeawayNetLoadStatus = paramObject;
      if (localMApiResponse.message() != null)
        paramTakeawayNetLoadStatus = localMApiResponse.message().content();
    }
    paramObject = paramTakeawayNetLoadStatus;
    if (TextUtils.isEmpty(paramTakeawayNetLoadStatus))
      paramObject = "网络不给力哦，请稍后再试";
    super.showShortToast(paramObject);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.clear_button)
    {
      this.poiDataSource.poiList.clear();
      this.poiListAdapter.notifyDataSetChanged();
      setEditTextContent("");
    }
    do
      return;
    while (i != R.id.cancel_button);
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.hideTitleBar();
    super.setContentView(R.layout.takeaway_poi_search);
    super.getWindow().setBackgroundDrawable(null);
    this.poiDataSource = new TakeawayPoiDataSource(this.activity);
    this.poiDataSource.setDataLoadListener(this);
    this.poiDataSource.keyword = getStringParam("poi");
    setupView();
    if (TextUtils.isEmpty(this.poiDataSource.keyword))
      this.poiDataSource.getSuggestPoiTask("");
  }

  protected void onDestroy()
  {
    this.poiDataSource.onDestroy();
    super.onDestroy();
  }

  protected void setEditTextContent(String paramString)
  {
    if (paramString == null)
      return;
    this.poiEditView.setText(paramString);
    this.poiEditView.setSelection(paramString.length());
  }

  class TakeawayPoiAdapter extends BasicAdapter
  {
    TakeawayPoiAdapter()
    {
    }

    public int getCount()
    {
      return TakeawayPoiSearchActivity.this.poiDataSource.poiList.size();
    }

    public Object getItem(int paramInt)
    {
      return TakeawayPoiSearchActivity.this.poiDataSource.poiList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject;
      if (paramView == null)
      {
        paramView = LayoutInflater.from(TakeawayPoiSearchActivity.this.context).inflate(R.layout.takeaway_poi_search_item, null);
        paramViewGroup = new TakeawayPoiSearchActivity.ViewHolder();
        paramViewGroup.curTagView = paramView.findViewById(R.id.current_tag);
        paramViewGroup.poiView = ((TextView)paramView.findViewById(R.id.poi_content));
        paramViewGroup.poiDespView = ((TextView)paramView.findViewById(R.id.poi_desp));
        paramView.setTag(paramViewGroup);
        localDPObject = (DPObject)TakeawayPoiSearchActivity.this.poiDataSource.poiList.get(paramInt);
        if ((!TakeawayPoiSearchActivity.this.poiDataSource.isLocal) || (paramInt != 0))
          break label161;
        paramViewGroup.curTagView.setVisibility(0);
      }
      while (true)
      {
        ViewUtils.setVisibilityAndContent(paramViewGroup.poiView, localDPObject.getString("Poi"));
        ViewUtils.setVisibilityAndContent(paramViewGroup.poiDespView, localDPObject.getString("PoiDesc"));
        return paramView;
        paramViewGroup = (TakeawayPoiSearchActivity.ViewHolder)paramView.getTag();
        break;
        label161: paramViewGroup.curTagView.setVisibility(8);
      }
    }
  }

  static class ViewHolder
  {
    public View curTagView;
    public TextView poiDespView;
    public TextView poiView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayPoiSearchActivity
 * JD-Core Version:    0.6.0
 */