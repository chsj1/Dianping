package com.dianping.base.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.app.DPFragment;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.model.City;
import com.dianping.model.UserProfile;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import java.util.List;
import java.util.UUID;

public class NovaFragment extends DPFragment
{
  private String activityTitle;
  protected AlertDialog alertDialog;
  protected String callId;
  private ProgressDialog progressDialog;
  Toast toast;

  public int cityId()
  {
    return city().id();
  }

  public void dismissDialog()
  {
    if ((this.alertDialog != null) && (this.alertDialog.isShowing()))
    {
      this.alertDialog.dismiss();
      this.alertDialog = null;
    }
    dismissProgressDialog();
  }

  public void dismissProgressDialog()
  {
    if ((this.progressDialog != null) && (this.progressDialog.isShowing()))
      this.progressDialog.dismiss();
    this.progressDialog = null;
  }

  public UserProfile getAccount()
  {
    Object localObject = accountService().profile();
    if (localObject != null)
      try
      {
        localObject = (UserProfile)((DPObject)localObject).edit().putString("Token", accountService().token()).generate().decodeToObject(UserProfile.DECODER);
        return localObject;
      }
      catch (ArchiveException localArchiveException)
      {
        Log.w(localArchiveException.getLocalizedMessage());
      }
    return (UserProfile)null;
  }

  public Dialog getDialog()
  {
    return this.alertDialog;
  }

  protected void initLeftTitleButton()
  {
    if ((getView() != null) && (getView().findViewById(R.id.left_title_button) != null))
      getView().findViewById(R.id.left_title_button).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          KeyboardUtils.hideKeyboard(paramView);
          if ((NovaFragment.this.onGoBack()) && (!NovaFragment.this.getFragmentManager().popBackStackImmediate()))
            NovaFragment.this.getActivity().finish();
        }
      });
  }

  protected boolean isDPObjectof(Object paramObject)
  {
    return paramObject instanceof DPObject;
  }

  protected boolean isDPObjectof(Object paramObject, String paramString)
  {
    if (!isDPObjectof(paramObject));
    do
      return false;
    while (!((DPObject)paramObject).isClass(paramString));
    return true;
  }

  public boolean isUrlAvailable(String paramString)
  {
    int i = 0;
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
    paramString = localIntent;
    if ((getActivity() instanceof DPActivity))
      paramString = ((DPActivity)getActivity()).urlMap(localIntent);
    if (getActivity().getPackageManager().queryIntentActivities(paramString, 0).size() > 0)
      i = 1;
    return i;
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    this.activityTitle = ((String)getActivity().getTitle());
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.callId = paramBundle.getString("callid");
      return;
    }
    this.callId = UUID.randomUUID().toString();
  }

  public void onDetach()
  {
    super.onDetach();
    getActivity().setTitle(this.activityTitle);
    dismissProgressDialog();
  }

  public void onFragmentDetach()
  {
    super.onDetach();
  }

  public boolean onGoBack()
  {
    return true;
  }

  protected void onProgressDialogCancel()
  {
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    if (paramBundle != null)
      paramBundle.putString("callid", this.callId);
    super.onSaveInstanceState(paramBundle);
  }

  public SharedPreferences preferences(Context paramContext)
  {
    return paramContext.getSharedPreferences(paramContext.getPackageName(), 32768);
  }

  public void showAlertDialog(String paramString)
  {
    if ((this.alertDialog != null) && (this.alertDialog.isShowing()))
      this.alertDialog.dismiss();
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
    localBuilder.setTitle("提示").setMessage(paramString).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
      }
    });
    this.alertDialog = localBuilder.create();
    this.alertDialog.show();
  }

  public void showAlertDialog(String paramString, DialogInterface.OnClickListener paramOnClickListener)
  {
    if ((this.alertDialog != null) && (this.alertDialog.isShowing()))
      this.alertDialog.dismiss();
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
    localBuilder.setMessage(paramString).setCancelable(false).setPositiveButton("确定", paramOnClickListener).setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
      }
    });
    this.alertDialog = localBuilder.create();
    this.alertDialog.show();
  }

  public void showProgressDialog(String paramString)
  {
    showProgressDialog(paramString, null);
  }

  public void showProgressDialog(String paramString, onProgressDialogCancelListener paramonProgressDialogCancelListener)
  {
    if (getActivity() == null);
    do
    {
      return;
      if (this.progressDialog != null)
        this.progressDialog.dismiss();
      if (this.progressDialog != null)
        continue;
      this.progressDialog = new ProgressDialog(getActivity());
      this.progressDialog.setIndeterminate(true);
      this.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(paramonProgressDialogCancelListener)
      {
        public void onCancel(DialogInterface paramDialogInterface)
        {
          NovaFragment.this.onProgressDialogCancel();
          NovaFragment.access$002(NovaFragment.this, null);
          if (this.val$listener != null)
            this.val$listener.onProgressDialogCancel();
        }
      });
      this.progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
      {
        public boolean onKey(DialogInterface paramDialogInterface, int paramInt, KeyEvent paramKeyEvent)
        {
          return paramInt == 84;
        }
      });
    }
    while (this.progressDialog == null);
    this.progressDialog.setMessage(paramString);
    this.progressDialog.show();
  }

  public void showSimpleAlertDialog(String paramString1, String paramString2, String paramString3, DialogInterface.OnClickListener paramOnClickListener1, String paramString4, DialogInterface.OnClickListener paramOnClickListener2)
  {
    if ((this.alertDialog != null) && (this.alertDialog.isShowing()))
      this.alertDialog.dismiss();
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
    localBuilder.setTitle(paramString1).setMessage(paramString2).setPositiveButton(paramString3, paramOnClickListener1).setNegativeButton(paramString4, paramOnClickListener2);
    this.alertDialog = localBuilder.create();
    this.alertDialog.show();
  }

  public void showToast(String paramString)
  {
    if (getActivity() == null)
      return;
    if (this.toast == null)
      this.toast = Toast.makeText(getActivity(), paramString, 1);
    while (true)
    {
      this.toast.show();
      return;
      this.toast.setText(paramString);
    }
  }

  public static abstract interface onProgressDialogCancelListener
  {
    public abstract void onProgressDialogCancel();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.NovaFragment
 * JD-Core Version:    0.6.0
 */