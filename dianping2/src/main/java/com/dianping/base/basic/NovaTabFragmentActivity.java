package com.dianping.base.basic;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.NovaTabBaseFragment;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public abstract class NovaTabFragmentActivity extends NovaActivity
{
  public static boolean isShowShopImg = true;
  public static boolean isShowTuanImg = true;
  private Button deleteButton;
  private View deleteLayout;
  protected TextView deleteText;
  boolean isEdit = false;
  boolean isFragment1Editable = false;
  boolean isFragment2Editable = false;
  NovaTabBaseFragment mCurrentFragment;
  NovaTabBaseFragment mFragment1;
  NovaTabBaseFragment mFragment2;
  private TextView mTitle;
  String[] tabtitles = null;

  private void onImageSwitchChanged()
  {
    if (this.mFragment1 != null)
      this.mFragment1.onImageSwitchChanged();
    if (this.mFragment2 != null)
      this.mFragment2.onImageSwitchChanged();
  }

  public abstract NovaTabBaseFragment[] getFragments();

  public abstract String[] getTabTitles();

  public boolean isEdit()
  {
    return this.isEdit;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    NovaConfigUtils.showDialogInMobileNetworkWhenFirstStart(this);
    hideTitleBar();
    setContentView(R.layout.base_tabfragment_layout);
    findViewById(R.id.left_view).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        NovaTabFragmentActivity.this.finish();
      }
    });
    this.deleteText = ((TextView)findViewById(R.id.modification));
    this.deleteText.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = NovaTabFragmentActivity.this;
        if (!NovaTabFragmentActivity.this.isEdit);
        for (boolean bool = true; ; bool = false)
        {
          paramView.setActivityIsEdit(bool);
          return;
        }
      }
    });
    this.deleteLayout = findViewById(R.id.delete_layout);
    this.deleteButton = ((Button)findViewById(R.id.delete_btn));
    this.deleteButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (NovaTabFragmentActivity.this.mCurrentFragment != null)
          NovaTabFragmentActivity.this.mCurrentFragment.onDeleteButtonClicked();
      }
    });
    Object localObject = findViewById(R.id.fragment1);
    View localView = findViewById(R.id.fragment2);
    ((View)localObject).setVisibility(0);
    localView.setVisibility(4);
    paramBundle = (ShopListTabView)findViewById(R.id.title_bar_tab);
    this.tabtitles = getTabTitles();
    if (this.tabtitles != null)
    {
      if (this.tabtitles.length > 0)
        paramBundle.setLeftTitleText(this.tabtitles[0]);
      if (this.tabtitles.length > 1)
        paramBundle.setRightTitleText(this.tabtitles[1]);
    }
    this.mTitle = ((TextView)findViewById(R.id.title_bar_title));
    paramBundle.setTabChangeListener(new ShopListTabView.TabChangeListener((View)localObject, localView)
    {
      public void onTabChanged(int paramInt)
      {
        if ((NovaTabFragmentActivity.this.mCurrentFragment != null) && (NovaTabFragmentActivity.this.isEdit))
          NovaTabFragmentActivity.this.mCurrentFragment.onEditModeChanged(false);
        if (paramInt == 0)
        {
          this.val$fragment1layout.setVisibility(0);
          this.val$fragment2layout.setVisibility(4);
          NovaTabFragmentActivity.this.mCurrentFragment = NovaTabFragmentActivity.this.mFragment1;
          NovaTabFragmentActivity.this.setActivityEditable(NovaTabFragmentActivity.this.isFragment1Editable, NovaTabFragmentActivity.this.mCurrentFragment);
        }
        while (true)
        {
          if (NovaTabFragmentActivity.this.isEdit)
            NovaTabFragmentActivity.this.setActivityIsEdit(false);
          return;
          this.val$fragment2layout.setVisibility(0);
          this.val$fragment1layout.setVisibility(4);
          NovaTabFragmentActivity.this.mCurrentFragment = NovaTabFragmentActivity.this.mFragment2;
          NovaTabFragmentActivity.this.setActivityEditable(NovaTabFragmentActivity.this.isFragment2Editable, NovaTabFragmentActivity.this.mCurrentFragment);
        }
      }
    });
    localObject = getFragments();
    if (localObject != null)
    {
      if (localObject.length > 0)
      {
        this.mFragment1 = localObject[0];
        if (this.mFragment1 != null)
          getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, this.mFragment1).commit();
        this.mCurrentFragment = this.mFragment1;
      }
      if (localObject.length > 1)
      {
        this.mFragment2 = localObject[1];
        if (this.mFragment2 != null)
          getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, this.mFragment2).commit();
      }
    }
    if (this.mFragment1 != null)
    {
      if (this.mFragment2 == null)
        break label360;
      paramBundle.setVisibility(0);
      this.mTitle.setVisibility(8);
    }
    while (true)
    {
      setActivityEditable(true, this.mCurrentFragment);
      return;
      label360: paramBundle.setVisibility(8);
      this.mTitle.setVisibility(0);
    }
  }

  public void onResume()
  {
    super.onResume();
    boolean bool = NovaConfigUtils.isShowImageInMobileNetwork();
    if (isShowShopImg != bool)
    {
      isShowShopImg = bool;
      onImageSwitchChanged();
    }
  }

  public void setActivityEditable(boolean paramBoolean, NovaTabBaseFragment paramNovaTabBaseFragment)
  {
    int j = 0;
    int i = 0;
    if (paramNovaTabBaseFragment == this.mFragment1)
    {
      this.isFragment1Editable = paramBoolean;
      if (this.mCurrentFragment == this.mFragment1)
      {
        paramNovaTabBaseFragment = this.deleteText;
        if (!paramBoolean)
          break label44;
        paramNovaTabBaseFragment.setVisibility(i);
      }
    }
    label44: 
    do
    {
      do
      {
        return;
        i = 8;
        break;
      }
      while (paramNovaTabBaseFragment != this.mFragment2);
      this.isFragment2Editable = paramBoolean;
    }
    while (this.mCurrentFragment != this.mFragment2);
    paramNovaTabBaseFragment = this.deleteText;
    if (paramBoolean);
    for (i = j; ; i = 8)
    {
      paramNovaTabBaseFragment.setVisibility(i);
      return;
    }
  }

  public void setActivityIsEdit(boolean paramBoolean)
  {
    this.isEdit = paramBoolean;
    if (paramBoolean)
    {
      this.deleteText.setText("取消");
      this.deleteLayout.setVisibility(0);
    }
    while (true)
    {
      if (this.mCurrentFragment != null)
        this.mCurrentFragment.onEditModeChanged(paramBoolean);
      return;
      this.deleteText.setText("编辑");
      this.deleteLayout.setVisibility(8);
    }
  }

  public void setButtonView(int paramInt)
  {
    if (paramInt > 0)
    {
      this.deleteButton.setText("删除(" + paramInt + ")");
      this.deleteButton.setBackgroundResource(R.drawable.common_action_btn_delete_bg);
      return;
    }
    this.deleteButton.setText("删除");
    this.deleteButton.setBackgroundResource(R.drawable.common_action_btn_disable);
  }

  public void setTitle(String paramString)
  {
    this.mTitle.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.NovaTabFragmentActivity
 * JD-Core Version:    0.6.0
 */