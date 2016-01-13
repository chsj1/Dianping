package com.dianping.main.user.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.UserProfile;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.KeyboardUtils.SoftKeyboardController;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class EditNickNameActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  boolean changed = false;
  String newUserName;
  EditText nickNameEt;
  MApiRequest updateProfileReq;

  private void initView()
  {
    this.nickNameEt = ((EditText)findViewById(R.id.itemInput));
    if (this.nickNameEt != null)
      this.nickNameEt.setOnEditorActionListener(new TextView.OnEditorActionListener()
      {
        public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
        {
          if (paramInt == 6)
            KeyboardUtils.getSoftKeyboardController(EditNickNameActivity.this.nickNameEt).hide();
          return false;
        }
      });
    UserProfile localUserProfile = getAccount();
    if (localUserProfile != null)
      this.nickNameEt.setText(localUserProfile.nickName());
    findViewById(R.id.save_profile).setOnClickListener(new View.OnClickListener(localUserProfile)
    {
      public void onClick(View paramView)
      {
        if (this.val$profile == null)
          return;
        if (!EditNickNameActivity.this.nickNameEt.getText().toString().trim().equals(this.val$profile.nickName()))
          EditNickNameActivity.this.changed = true;
        if (!EditNickNameActivity.this.changed)
        {
          Toast.makeText(EditNickNameActivity.this, "您还没有任何修改", 0).show();
          return;
        }
        EditNickNameActivity.this.updateProfileReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/updateprofile.bin", new String[] { "token", this.val$profile.token(), "nickname", EditNickNameActivity.this.nickNameEt.getText().toString() });
        EditNickNameActivity.this.mapiService().exec(EditNickNameActivity.this.updateProfileReq, EditNickNameActivity.this);
        EditNickNameActivity.this.newUserName = EditNickNameActivity.this.nickNameEt.getText().toString();
      }
    });
  }

  public void onClick(View paramView)
  {
    if ((this.nickNameEt == null) || (getAccount() == null))
    {
      finish();
      return;
    }
    if (!this.nickNameEt.getText().toString().trim().equals(getAccount().nickName()))
      this.changed = true;
    if (this.changed)
    {
      new AlertDialog.Builder(this).setTitle("提示").setMessage("尚未提交，确定放弃修改吗？").setPositiveButton("确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          EditNickNameActivity.this.finish();
        }
      }).setNegativeButton("取消", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      }).show();
      return;
    }
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.edit_nickname);
    initView();
    if (this.leftTitleButton != null)
      this.leftTitleButton.setOnClickListener(this);
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      if (getAccount() == null)
        return true;
      if (!this.nickNameEt.getText().toString().trim().equals(getAccount().nickName()))
        this.changed = true;
      if (this.changed)
      {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("尚未提交，确定放弃修改吗？").setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            EditNickNameActivity.this.finish();
          }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
          }
        }).show();
        return true;
      }
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.updateProfileReq) && (paramMApiResponse.message() != null))
      showMessageDialog(paramMApiResponse.message());
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.updateProfileReq) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      dismissDialog();
      statisticsEvent("profile5", "profile5_name_save", "", 0);
      Toast.makeText(this, "保存成功", 0).show();
      this.changed = false;
      paramMApiRequest = new Intent("com.dianping.action.PROFILE_EDIT");
      paramMApiRequest.putExtra("newUserName", this.newUserName);
      sendBroadcast(paramMApiRequest);
      sendBroadcast(new Intent("com.dianping.action.USER_EDIT"));
      finish();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.EditNickNameActivity
 * JD-Core Version:    0.6.0
 */