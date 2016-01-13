package com.dianping.base.basic;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaButton;

public class DeleteListActivity extends NovaListActivity
{
  protected NovaButton deleteBtn;
  protected View deleteView;
  protected boolean isEdit = false;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.deleteView = findViewById(R.id.delete_layout);
    this.deleteBtn = ((NovaButton)findViewById(R.id.delete_btn));
    setTitleButton("编辑", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = DeleteListActivity.this;
        if (!DeleteListActivity.this.isEdit);
        for (boolean bool = true; ; bool = false)
        {
          paramView.isEdit = bool;
          DeleteListActivity.this.setActivityIsEdit(DeleteListActivity.this.isEdit);
          return;
        }
      }
    });
  }

  public void setActivityIsEdit(boolean paramBoolean)
  {
    this.isEdit = paramBoolean;
    if (paramBoolean)
    {
      this.titleButton.setText("取消");
      this.deleteView.setVisibility(0);
      return;
    }
    this.titleButton.setText("编辑");
    this.deleteView.setVisibility(8);
  }

  public void setButtonView(int paramInt)
  {
    if (paramInt > 0)
    {
      this.deleteBtn.setText("删除(" + paramInt + ")");
      this.deleteBtn.setBackgroundResource(R.drawable.common_action_btn_delete_bg);
      return;
    }
    this.deleteBtn.setText("删除");
    this.deleteBtn.setBackgroundResource(R.drawable.common_action_btn_disable);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.DeleteListActivity
 * JD-Core Version:    0.6.0
 */