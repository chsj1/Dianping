package com.dianping.tuan.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeliveryListFragment extends BaseTuanListFragment
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>, AddDeliveryFragment.OnAddDeliveryListener
{
  private static final int DEFAULTADDRESSLIMIT = 5;
  View addAddressHint;
  View btnAddAddressView;
  View btnDeleteAddressView;
  MApiRequest deleteDeliveryRequest;
  int displayAddressMaxcount;
  MApiRequest getDeliveryRequest;
  boolean isEditMode;
  List<DPObject> mDeletedList = new ArrayList();
  ArrayList<DPObject> mDeliveryList = new ArrayList();
  TextView rightBtn;
  DPObject selectedDelivery;

  public static DeliveryListFragment newInstance(FragmentActivity paramFragmentActivity, Bundle paramBundle)
  {
    DeliveryListFragment localDeliveryListFragment = new DeliveryListFragment();
    localDeliveryListFragment.setArguments(paramBundle);
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localDeliveryListFragment);
    paramFragmentActivity.setTransition(4097);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commit();
    return localDeliveryListFragment;
  }

  protected void deleteDeliveryList()
  {
    if (this.deleteDeliveryRequest != null)
      return;
    this.mDeletedList.clear();
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    while (i < getListView().getChildCount())
    {
      Object localObject = getListView().getChildAt(i).getTag();
      if ((localObject instanceof DeliveryListFragment.ViewHolder))
      {
        localObject = (DeliveryListFragment.ViewHolder)localObject;
        if (((DeliveryListFragment.ViewHolder)localObject).deleteDeliveryCheck.isChecked())
        {
          localStringBuilder.append(((DPObject)this.mDeliveryList.get(((DeliveryListFragment.ViewHolder)localObject).indexInList)).getInt("ID")).append(",");
          this.mDeletedList.add(this.mDeliveryList.get(((DeliveryListFragment.ViewHolder)localObject).indexInList));
        }
      }
      i += 1;
    }
    if (TextUtils.isEmpty(localStringBuilder.toString()))
    {
      Toast.makeText(getActivity(), "至少选择一个要删除的配送地址", 0).show();
      return;
    }
    showProgressDialog("正在删除...");
    this.deleteDeliveryRequest = mapiPost(this, "http://app.t.dianping.com/removedeliverygn.bin", new String[] { "token", accountService().token(), "ids", localStringBuilder.toString() });
    mapiService().exec(this.deleteDeliveryRequest, this);
  }

  protected void getDeliveryList()
  {
    if (this.getDeliveryRequest != null)
      return;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://app.t.dianping.com/");
    localStringBuilder.append("deliverylistgn.bin");
    localStringBuilder.append("?token=").append(accountService().token());
    this.getDeliveryRequest = mapiGet(this, localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.getDeliveryRequest, this);
    showProgressDialog("加载中...");
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    getActivity().setTitle("收货地址列表");
    setListAdapter(new DeliveryListFragment.Adapter(this, null));
    if ((this.mDeliveryList != null) && (this.mDeliveryList.size() > 0))
    {
      ((BasicAdapter)getListAdapter()).notifyDataSetChanged();
      updateFootView();
      return;
    }
    getDeliveryList();
  }

  public void onAddDelivery(DPObject paramDPObject)
  {
    this.mDeliveryList = new ArrayList();
    ((BaseAdapter)getListAdapter()).notifyDataSetChanged();
    getDeliveryList();
    updateFootView();
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.btnAddAddressView)
      AddDeliveryFragment.newInstance(getActivity()).setOnAddDeliveryListener(this);
    do
    {
      return;
      if (paramView != this.btnDeleteAddressView)
        continue;
      deleteDeliveryList();
      return;
    }
    while (paramView != this.rightBtn);
    boolean bool;
    if (!this.isEditMode)
    {
      bool = true;
      this.isEditMode = bool;
      if (!this.isEditMode)
        break label92;
      this.rightBtn.setText("取消");
    }
    while (true)
    {
      ((BaseAdapter)getListAdapter()).notifyDataSetChanged();
      updateFootView();
      return;
      bool = false;
      break;
      label92: this.rightBtn.setText("编辑");
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getArguments();
    if (paramBundle != null)
    {
      this.mDeliveryList = paramBundle.getParcelableArrayList("deliveryList");
      this.selectedDelivery = ((DPObject)paramBundle.getParcelable("delivery"));
    }
  }

  public void onCreateTitleBar(TitleBar paramTitleBar)
  {
    super.onCreateTitleBar(paramTitleBar);
    TextView localTextView;
    if (getActivity() != null)
    {
      this.rightBtn = new TextView(getActivity());
      localTextView = this.rightBtn;
      if (this.isEditMode)
        break label99;
    }
    label99: for (String str = "编辑"; ; str = "取消")
    {
      localTextView.setText(str);
      this.rightBtn.setGravity(17);
      this.rightBtn.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
      this.rightBtn.setTextSize(2, 15.0F);
      paramTitleBar.addRightViewItem(this.rightBtn, "edit_btn", this);
      return;
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.delivery_list_content, paramViewGroup, false);
    this.btnDeleteAddressView = paramLayoutInflater.findViewById(R.id.delete_selected_address);
    this.btnDeleteAddressView.setOnClickListener(this);
    this.btnAddAddressView = paramLayoutInflater.findViewById(R.id.add_address);
    this.btnAddAddressView.setOnClickListener(this);
    this.addAddressHint = paramLayoutInflater.findViewById(R.id.add_address_hint);
    return paramLayoutInflater;
  }

  public boolean onGoBack()
  {
    if (this.isEditMode)
    {
      this.isEditMode = false;
      this.rightBtn.setText("编辑");
      ((BaseAdapter)getListAdapter()).notifyDataSetChanged();
      updateFootView();
      return false;
    }
    Intent localIntent = new Intent();
    localIntent.putExtra("selectedDelivery", this.selectedDelivery);
    localIntent.putParcelableArrayListExtra("mDeliveryList", this.mDeliveryList);
    FragmentActivity localFragmentActivity = getActivity();
    getActivity();
    localFragmentActivity.setResult(-1, localIntent);
    getActivity().finish();
    return false;
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    paramListView = paramListView.getItemAtPosition(paramInt);
    if ((paramListView instanceof DPObject))
    {
      this.selectedDelivery = ((DPObject)paramListView);
      if (this.isEditMode)
        AddDeliveryFragment.newInstance(getActivity(), (DPObject)paramListView).setOnAddDeliveryListener(this);
    }
    else
    {
      return;
    }
    onGoBack();
  }

  public void onPause()
  {
    super.onPause();
    setMenuVisibility(false);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (getActivity() == null)
      return;
    this.getDeliveryRequest = null;
    this.deleteDeliveryRequest = null;
    dismissProgressDialog();
    paramMApiRequest = paramMApiResponse.message().content();
    Toast.makeText(getActivity(), paramMApiRequest, 1).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissProgressDialog();
    if (this.getDeliveryRequest == paramMApiRequest)
    {
      this.getDeliveryRequest = null;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if ((paramMApiRequest != null) && (paramMApiRequest.getArray("List") != null) && (paramMApiRequest.getArray("List").length > 0))
      {
        this.displayAddressMaxcount = paramMApiRequest.getInt("MaxCount");
        this.mDeliveryList = new ArrayList();
        this.mDeliveryList.addAll(Arrays.asList(paramMApiRequest.getArray("List")));
      }
      ((BasicAdapter)getListAdapter()).notifyDataSetChanged();
    }
    while (true)
    {
      updateFootView();
      return;
      if (this.deleteDeliveryRequest != paramMApiRequest)
        continue;
      this.deleteDeliveryRequest = null;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      ((BasicAdapter)getListAdapter()).notifyDataSetChanged();
      Toast.makeText(getActivity(), paramMApiRequest.getString("Title"), 0).show();
      this.mDeliveryList.removeAll(this.mDeletedList);
      if (this.mDeletedList.contains(this.selectedDelivery))
        this.selectedDelivery = null;
      this.mDeletedList.clear();
      ((BasicAdapter)getListAdapter()).notifyDataSetChanged();
    }
  }

  public void onResume()
  {
    super.onResume();
    setMenuVisibility(true);
  }

  protected void updateFootView()
  {
    if (this.displayAddressMaxcount == 0);
    for (int i = 5; this.isEditMode; i = this.displayAddressMaxcount)
    {
      this.addAddressHint.setVisibility(8);
      this.btnAddAddressView.setVisibility(8);
      this.btnDeleteAddressView.setVisibility(0);
      return;
    }
    if (getListAdapter().getCount() == i)
    {
      this.addAddressHint.setVisibility(0);
      this.btnAddAddressView.setVisibility(8);
      this.btnDeleteAddressView.setVisibility(8);
      return;
    }
    this.addAddressHint.setVisibility(8);
    this.btnDeleteAddressView.setVisibility(8);
    this.btnAddAddressView.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.DeliveryListFragment
 * JD-Core Version:    0.6.0
 */