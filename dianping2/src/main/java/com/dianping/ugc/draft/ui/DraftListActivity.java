package com.dianping.ugc.draft.ui;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.base.basic.DeleteListActivity;
import com.dianping.ugc.draft.UGCDraftManager;
import com.dianping.ugc.draft.UGCMulDeleListAdapter;
import com.dianping.ugc.draft.view.UGCDraftListItem;
import com.dianping.ugc.model.UGCContentItem;
import com.dianping.ugc.model.UGCUploadCommunityPhotoItem;
import com.dianping.v1.R.array;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DraftListActivity extends DeleteListActivity
{
  private DraftListAdapter adapter;
  private View blank = null;
  private final BroadcastReceiver draftReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      Object localObject;
      if ("com.dianping.action.draftitem.added".equals(paramContext))
      {
        paramContext = (UGCContentItem)paramIntent.getExtras().get("item");
        paramIntent = paramContext.id;
        UGCDraftListItem.resendMap.remove(paramIntent);
        localObject = DraftListActivity.this.drafts.iterator();
        while (((Iterator)localObject).hasNext())
        {
          UGCContentItem localUGCContentItem = (UGCContentItem)((Iterator)localObject).next();
          if (!paramIntent.equals(localUGCContentItem.id))
            continue;
          DraftListActivity.this.drafts.remove(localUGCContentItem);
        }
        DraftListActivity.this.drafts.add(0, paramContext);
      }
      while (true)
      {
        if (DraftListActivity.this.adapter != null)
          DraftListActivity.this.adapter.notifyDataSetChanged();
        return;
        if (!"com.dianping.action.draftitem.removed".equals(paramContext))
          continue;
        paramContext = paramIntent.getStringExtra("id");
        UGCDraftListItem.resendMap.remove(paramContext);
        paramIntent = DraftListActivity.this.drafts.iterator();
        if (!paramIntent.hasNext())
          continue;
        localObject = (UGCContentItem)paramIntent.next();
        if (!paramContext.equals(((UGCContentItem)localObject).id))
          break;
        DraftListActivity.this.drafts.remove(localObject);
      }
    }
  };
  private ArrayList<UGCContentItem> drafts = null;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (DraftListActivity.this.adapter != null)
        DraftListActivity.this.adapter.setDraftData();
    }
  };
  private final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      paramAdapterView = (UGCContentItem)DraftListActivity.this.adapter.getItem(paramInt);
      if (DraftListActivity.this.isEdit)
      {
        DraftListActivity.this.adapter.itemBeChecked(paramInt);
        ((UGCDraftListItem)paramView).setChecked(DraftListActivity.this.adapter.getChecked(paramInt));
        DraftListActivity.this.setButtonView(DraftListActivity.this.adapter.getCheckedSize());
        return;
      }
      DraftListActivity.this.switchView(paramAdapterView);
    }
  };
  private final AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener()
  {
    public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      paramView = (UGCContentItem)DraftListActivity.this.adapter.getItem(paramInt);
      if ((DraftListActivity.this.isEdit) || (!paramView.editable))
        return false;
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(DraftListActivity.this);
      if ((paramView instanceof UGCUploadCommunityPhotoItem));
      for (paramAdapterView = ((UGCUploadCommunityPhotoItem)paramView).topicId; ; paramAdapterView = paramView.shopName)
      {
        localBuilder.setTitle(paramAdapterView);
        localBuilder.setItems(R.array.edit_draft_items, new DialogInterface.OnClickListener(paramView, paramAdapterView)
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            if (paramInt == 0)
              DraftListActivity.this.confirmDeleteDraft(this.val$item, this.val$title);
            do
              return;
            while (paramInt != 1);
            DraftListActivity.this.switchView(this.val$item);
          }
        });
        localBuilder.setNegativeButton(R.string.cancel, null);
        localBuilder.show();
        return true;
      }
    }
  };
  private UGCDraftManager ugcDraftManager;

  private void confirmDeleteDraft(UGCContentItem paramUGCContentItem, String paramString)
  {
    new AlertDialog.Builder(this).setTitle(paramString).setMessage(R.string.ugc_draft_confirm_delete).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(paramUGCContentItem)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if ((this.val$draft != null) && (DraftListActivity.this.ugcDraftManager.removeDraft(this.val$draft)))
        {
          DraftListActivity.this.drafts.remove(this.val$draft);
          DraftListActivity.this.adapter.notifyDataSetChanged();
        }
      }
    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
      }
    }).show();
  }

  private void prepareDraftData()
  {
    new Thread()
    {
      public void run()
      {
        DraftListActivity.access$802(DraftListActivity.this, (ArrayList)DraftListActivity.this.ugcDraftManager.getAllDrafts());
        DraftListActivity.this.mHandler.sendEmptyMessage(0);
      }
    }
    .start();
  }

  public String getPageName()
  {
    return "drafts";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getSharedPreferences(getPackageName(), 0).edit().remove("hasNewDraft").commit();
    this.ugcDraftManager = UGCDraftManager.getInstance();
    this.adapter = new DraftListAdapter(null);
    this.listView.setAdapter(this.adapter);
    setEmptyMsg("暂无草稿", false);
    this.listView.setOnItemClickListener(this.mOnItemClickListener);
    this.listView.setOnItemLongClickListener(this.mOnItemLongClickListener);
    setTitleButton("编辑", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = DraftListActivity.this;
        if (!DraftListActivity.this.isEdit);
        for (boolean bool = true; ; bool = false)
        {
          DraftListActivity.access$202(paramView, bool);
          if (DraftListActivity.this.isEdit)
            GAHelper.instance().contextStatisticsEvent(DraftListActivity.this, "button_edit", null, "tap");
          DraftListActivity.this.setActivityIsEdit(DraftListActivity.this.isEdit);
          return;
        }
      }
    });
    this.deleteBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = DraftListActivity.this.adapter.getDeleteList();
        if (paramView.size() == 0)
        {
          DraftListActivity.this.showToast(DraftListActivity.this.getResources().getString(R.string.ugc_toast_select_at_least_onephoto));
          return;
        }
        int i = 0;
        int j = paramView.size();
        while (i < j)
        {
          DraftListActivity.this.ugcDraftManager.removeDraft((UGCContentItem)paramView.get(i));
          i += 1;
        }
        DraftListActivity.this.setActivityIsEdit(false);
        DraftListActivity.this.prepareDraftData();
      }
    });
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.action.draftitem.added");
    paramBundle.addAction("com.dianping.action.draftitem.removed");
    LocalBroadcastManager.getInstance(this).registerReceiver(this.draftReceiver, paramBundle);
    if (accountService().id() == 0)
    {
      accountService().login(new LoginResultListener()
      {
        public void onLoginCancel(AccountService paramAccountService)
        {
          paramAccountService = new Intent("android.intent.action.VIEW", Uri.parse("dianping://home"));
          DraftListActivity.this.startActivity(paramAccountService);
        }

        public void onLoginSuccess(AccountService paramAccountService)
        {
          paramAccountService = new Intent("android.intent.action.VIEW", Uri.parse("dianping://drafts"));
          DraftListActivity.this.startActivity(paramAccountService);
        }
      });
      finish();
      return;
    }
    prepareDraftData();
  }

  public void onDestroy()
  {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(this.draftReceiver);
    super.onDestroy();
  }

  public void setActivityIsEdit(boolean paramBoolean)
  {
    if ((this.drafts == null) || (this.drafts.size() == 0))
      return;
    super.setActivityIsEdit(paramBoolean);
    View localView = this.blank;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localView.setVisibility(i);
      this.adapter.resetCheckList();
      this.adapter.setIsEdit(paramBoolean);
      setButtonView(this.adapter.getCheckedSize());
      return;
    }
  }

  protected void setupView()
  {
    super.setContentView(R.layout.ugc_draft_layout);
    this.blank = findViewById(R.id.ugc_list_blank);
  }

  public void switchView(UGCContentItem paramUGCContentItem)
  {
    if (!paramUGCContentItem.editable)
      return;
    GAHelper.instance().contextStatisticsEvent(this, "item", null, "tap");
    String str = paramUGCContentItem.getType();
    Object localObject = null;
    if ("review".equals(str))
      localObject = "dianping://ugcaddreview";
    while (true)
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
      ((Intent)localObject).putExtra("draft", paramUGCContentItem);
      startActivity((Intent)localObject);
      return;
      if ("uploadcommunityphoto".equals(str))
      {
        localObject = "dianping://ugcaddcommunityphoto";
        continue;
      }
      if (!"uploadphoto".equals(str))
        continue;
      localObject = "dianping://ugcaddshopphoto";
    }
  }

  private class DraftListAdapter extends UGCMulDeleListAdapter<UGCContentItem>
  {
    private DraftListAdapter()
    {
    }

    public int getCount()
    {
      return this.dataList.size();
    }

    public Object getItem(int paramInt)
    {
      if ((this.dataList.size() > 0) && (paramInt < this.dataList.size()))
        return this.dataList.get(paramInt);
      return null;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject1 = null;
      Object localObject2 = null;
      Object localObject3 = getItem(paramInt);
      if ((localObject3 instanceof UGCContentItem))
      {
        localObject3 = (UGCContentItem)localObject3;
        localObject1 = localObject2;
        if ((paramView instanceof UGCDraftListItem))
          localObject1 = (UGCDraftListItem)paramView;
        paramView = (View)localObject1;
        if (localObject1 == null)
          paramView = (UGCDraftListItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.ugc_draft_list_item, paramViewGroup, false);
        paramView.setDraft((UGCContentItem)localObject3);
        paramView.setEditable(this.isEdit);
        paramView.setChecked(getChecked(paramInt));
        localObject1 = paramView;
      }
      return (View)(View)localObject1;
    }

    public void setDraftData()
    {
      if (DraftListActivity.this.drafts != null)
        this.dataList = DraftListActivity.this.drafts;
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.draft.ui.DraftListActivity
 * JD-Core Version:    0.6.0
 */