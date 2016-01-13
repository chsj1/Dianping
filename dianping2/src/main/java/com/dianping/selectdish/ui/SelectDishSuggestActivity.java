package com.dianping.selectdish.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.SimpleMsg;
import com.dianping.selectdish.entity.SelectDishSuggestAdapter;
import com.dianping.selectdish.entity.SelectDishSuggestDataSource;
import com.dianping.selectdish.entity.SelectDishSuggestDataSource.DataLoadStatus;
import com.dianping.selectdish.entity.SelectDishSuggestDataSource.SelectDishSuggestDataLoaderListener;
import com.dianping.selectdish.model.SuggestDishInfo;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Iterator;

public class SelectDishSuggestActivity extends NovaActivity
  implements AdapterView.OnItemClickListener, View.OnClickListener, SelectDishSuggestDataSource.SelectDishSuggestDataLoaderListener
{
  protected SelectDishSuggestDataSource dataSource;
  protected ListView listView;
  protected SelectDishSuggestAdapter suggestAdapter;
  protected Button suggestButton;
  protected View suggestLayout;
  protected View title;

  private void initView()
  {
    if (!TextUtils.isEmpty(this.dataSource.shopName))
      super.setTitle(this.dataSource.shopName);
    this.title = findViewById(R.id.title);
    this.listView = ((ListView)findViewById(R.id.list));
    this.suggestAdapter = new SelectDishSuggestAdapter(this.dataSource);
    this.listView.setAdapter(this.suggestAdapter);
    this.listView.setOnItemClickListener(this);
    this.suggestLayout = findViewById(R.id.suggest_layout);
    this.suggestButton = ((Button)findViewById(R.id.suggest_btn));
    this.suggestButton.setOnClickListener(this);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void loadSubmitSuggestDish(SelectDishSuggestDataSource.DataLoadStatus paramDataLoadStatus, Object paramObject)
  {
    switch (2.$SwitchMap$com$dianping$selectdish$entity$SelectDishSuggestDataSource$DataLoadStatus[paramDataLoadStatus.ordinal()])
    {
    default:
      return;
    case 1:
      showProgressDialog("推荐中，请稍候");
      return;
    case 2:
      dismissDialog();
      if ((paramObject instanceof DPObject))
        if (!TextUtils.isEmpty(((DPObject)paramObject).getString("Message")))
          showToast(((DPObject)paramObject).getString("Message"));
      while (true)
      {
        finish();
        return;
        showToast("推荐成功");
        continue;
        showToast("推荐成功");
      }
    case 3:
    }
    dismissDialog();
    if (paramObject != null)
      if (!TextUtils.isEmpty(((MApiResponse)paramObject).message().content()))
        showToast(((MApiResponse)paramObject).message().content());
    while (true)
    {
      finish();
      return;
      showToast("推荐失败，请稍后重试");
      continue;
      showToast("推荐失败，请稍后重试");
    }
  }

  public void loadSuggestDishList(SelectDishSuggestDataSource.DataLoadStatus paramDataLoadStatus, Object paramObject)
  {
    switch (2.$SwitchMap$com$dianping$selectdish$entity$SelectDishSuggestDataSource$DataLoadStatus[paramDataLoadStatus.ordinal()])
    {
    case 1:
    default:
    case 2:
      do
      {
        return;
        this.title.setVisibility(0);
        this.suggestLayout.setVisibility(0);
      }
      while ((this.dataSource.suggestDishList == null) || (this.dataSource.suggestDishList.isEmpty()));
      this.suggestAdapter.notifyDataSetChanged();
      return;
    case 3:
    }
    Toast.makeText(getApplicationContext(), this.dataSource.errorMsg, 0).show();
    this.suggestAdapter.notifyDataSetChanged();
  }

  public void onClick(View paramView)
  {
    if (paramView == this.suggestButton)
    {
      int i = 0;
      paramView = this.dataSource.suggestDishList.iterator();
      while (paramView.hasNext())
      {
        if (!((SuggestDishInfo)paramView.next()).checked)
          continue;
        i += 1;
      }
      if (i > 0)
        this.dataSource.submitSuggestDish();
    }
    else
    {
      return;
    }
    Toast.makeText(this, "请选择推荐菜", 0).show();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (accountService().token() == null)
      accountService().login(new LoginResultListener()
      {
        public void onLoginCancel(AccountService paramAccountService)
        {
          SelectDishSuggestActivity.this.suggestAdapter.notifyDataSetChanged();
        }

        public void onLoginSuccess(AccountService paramAccountService)
        {
          SelectDishSuggestActivity.this.suggestAdapter.reset();
          SelectDishSuggestActivity.this.dataSource.reqSuggestDish();
        }
      });
    super.setContentView(R.layout.selectdish_activity_suggestdish);
    this.dataSource = new SelectDishSuggestDataSource(this);
    this.dataSource.dataLoaderListener = this;
    this.dataSource.fetchParams(paramBundle);
    initView();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.dataSource.releaseRequests();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.suggestAdapter.getItem(paramInt);
    if ((paramAdapterView instanceof SuggestDishInfo))
    {
      paramAdapterView = (SuggestDishInfo)paramAdapterView;
      if (paramAdapterView.checked)
        break label49;
    }
    label49: for (boolean bool = true; ; bool = false)
    {
      paramAdapterView.checked = bool;
      this.suggestAdapter.notifyDataSetChanged();
      suggestItemCheck();
      return;
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
  }

  public void showToast(String paramString)
  {
    paramString = Toast.makeText(getApplicationContext(), paramString, 1);
    paramString.setGravity(17, 0, 0);
    paramString.show();
  }

  public void suggestItemCheck()
  {
    if ((this.dataSource.suggestDishList == null) || (this.dataSource.suggestDishList.size() == 0))
    {
      this.suggestButton.setEnabled(false);
      return;
    }
    boolean bool2 = false;
    Iterator localIterator = this.dataSource.suggestDishList.iterator();
    boolean bool1;
    while (true)
    {
      bool1 = bool2;
      if (!localIterator.hasNext())
        break;
      if (!((SuggestDishInfo)localIterator.next()).checked)
        continue;
      bool1 = true;
    }
    this.suggestButton.setEnabled(bool1);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.ui.SelectDishSuggestActivity
 * JD-Core Version:    0.6.0
 */