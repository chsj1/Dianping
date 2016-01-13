package com.dianping.selectdish.ui;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class SelectDishJoinRoomActivity extends NovaActivity
{
  private static final int JOIN_ROOM_STATUS_SUCCESS = 1;
  private static final String MESSAGE_JOIN_ROOM_FAILED = "加入失败";
  private static final String MESSAGE_JOIN_ROOM_REQUESTING = "请稍后...";
  private static final int RESULT_OK = 1;
  private static final int ROOM_PASSWORD_LENGTH = 4;
  private EditText firstNumEditText;
  private MApiRequest joinRoomRequest;
  private final RequestHandler<MApiRequest, MApiResponse> mapiRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      SelectDishJoinRoomActivity.access$502(SelectDishJoinRoomActivity.this, null);
      SelectDishJoinRoomActivity.this.dismissDialog();
      paramMApiRequest = null;
      if (paramMApiResponse.message() != null)
        paramMApiRequest = paramMApiResponse.message().content();
      SelectDishJoinRoomActivity localSelectDishJoinRoomActivity = SelectDishJoinRoomActivity.this;
      paramMApiResponse = paramMApiRequest;
      if (TextUtils.isEmpty(paramMApiRequest))
        paramMApiResponse = "加入失败";
      localSelectDishJoinRoomActivity.showToast(paramMApiResponse);
      SelectDishJoinRoomActivity.this.clearAllInput();
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      SelectDishJoinRoomActivity.access$502(SelectDishJoinRoomActivity.this, null);
      SelectDishJoinRoomActivity.this.dismissDialog();
      paramMApiRequest = paramMApiResponse.result();
      if ((paramMApiRequest == null) || (!(paramMApiRequest instanceof DPObject)))
      {
        SelectDishJoinRoomActivity.this.showToast("加入失败");
        SelectDishJoinRoomActivity.this.clearAllInput();
        return;
      }
      paramMApiRequest = (DPObject)paramMApiRequest;
      if (paramMApiRequest.getInt("Status") != 1)
      {
        SelectDishJoinRoomActivity.this.showToast(paramMApiRequest.getString("Message"));
        SelectDishJoinRoomActivity.this.clearAllInput();
        return;
      }
      SelectDishJoinRoomActivity.this.openOrderDishRoom(paramMApiRequest);
      SelectDishJoinRoomActivity.this.clearAllInput();
    }
  };
  private String[] passwordArray;
  private int shopId;
  private TextView[] textViewArray;
  private final TextWatcher textWatcher = new TextWatcher()
  {
    private String getInputPassword()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int i = 0;
      while (i < SelectDishJoinRoomActivity.this.passwordArray.length)
      {
        if (SelectDishJoinRoomActivity.this.passwordArray[i] != null)
          localStringBuilder.append(SelectDishJoinRoomActivity.this.passwordArray[i]);
        i += 1;
      }
      return localStringBuilder.toString();
    }

    private void updateViewWhenNewTextAdded(String paramString)
    {
      int i = 0;
      while (true)
      {
        if (i < SelectDishJoinRoomActivity.this.passwordArray.length)
        {
          if (SelectDishJoinRoomActivity.this.passwordArray[i] == null)
          {
            SelectDishJoinRoomActivity.this.passwordArray[i] = paramString;
            SelectDishJoinRoomActivity.this.textViewArray[i].setText(paramString);
          }
        }
        else
        {
          SelectDishJoinRoomActivity.this.firstNumEditText.removeTextChangedListener(this);
          SelectDishJoinRoomActivity.this.firstNumEditText.setText(SelectDishJoinRoomActivity.this.passwordArray[0]);
          if (SelectDishJoinRoomActivity.this.firstNumEditText.getText().length() >= 1)
            SelectDishJoinRoomActivity.this.firstNumEditText.setSelection(1);
          SelectDishJoinRoomActivity.this.firstNumEditText.addTextChangedListener(this);
          return;
        }
        i += 1;
      }
    }

    public void afterTextChanged(Editable paramEditable)
    {
    }

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
      if (paramCharSequence == null);
      do
      {
        do
        {
          return;
          paramCharSequence = paramCharSequence.toString();
          paramInt1 = paramCharSequence.length();
          if (paramInt1 == 0)
          {
            SelectDishJoinRoomActivity.this.updateViewWhenTextDeleted();
            return;
          }
          if (paramInt1 != 1)
            continue;
          SelectDishJoinRoomActivity.this.passwordArray[0] = paramCharSequence;
          return;
        }
        while (paramInt1 != 2);
        updateViewWhenNewTextAdded(paramCharSequence.substring(1));
        paramCharSequence = getInputPassword();
      }
      while (paramCharSequence.length() != 4);
      SelectDishJoinRoomActivity.this.requestJoinOrderDishRoom(paramCharSequence);
      SelectDishJoinRoomActivity.this.showProgressDialog("请稍后...");
    }
  };

  private void clearAllInput()
  {
    this.firstNumEditText.setText("");
    int i = this.passwordArray.length - 1;
    while (i >= 0)
    {
      this.passwordArray[i] = null;
      this.textViewArray[i].setText(null);
      i -= 1;
    }
  }

  private void initDatas(Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      this.shopId = paramBundle.getInt("shopid");
      return;
    }
    this.shopId = getIntParam("shopid");
  }

  private void initViews()
  {
    super.setTitle("一起点");
    this.passwordArray = new String[4];
    this.textViewArray = new TextView[4];
    this.firstNumEditText = ((EditText)findViewById(R.id.sd_first_number_edittext));
    this.firstNumEditText.addTextChangedListener(this.textWatcher);
    this.firstNumEditText.post(new Runnable()
    {
      public void run()
      {
        KeyboardUtils.popupKeyboard(SelectDishJoinRoomActivity.this.firstNumEditText);
      }
    });
    this.textViewArray[0] = this.firstNumEditText;
    this.textViewArray[1] = ((TextView)findViewById(R.id.sd_second_number_textview));
    this.textViewArray[2] = ((TextView)findViewById(R.id.sd_third_number_textview));
    this.textViewArray[3] = ((TextView)findViewById(R.id.sd_forth_number_textview));
    findViewById(R.id.sd_number_layout).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        KeyboardUtils.popupKeyboard(SelectDishJoinRoomActivity.this.firstNumEditText);
      }
    });
  }

  private void openOrderDishRoom(DPObject paramDPObject)
  {
    Object localObject = paramDPObject.getString("Message");
    if (!TextUtils.isEmpty((CharSequence)localObject))
      showToast((String)localObject);
    localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishmenutogether"));
    ((Intent)localObject).putExtra("orderdishroominfo", paramDPObject);
    ((Intent)localObject).putExtra("shopid", this.shopId);
    startActivityForResult((Intent)localObject, 0);
  }

  private void requestJoinOrderDishRoom(String paramString)
  {
    this.joinRoomRequest = BasicMApiRequest.mapiPost(Uri.parse("http://m.api.dianping.com/orderdish/joinorderdishroom.hbt").buildUpon().toString(), new String[] { "shopid", String.valueOf(this.shopId), "password", paramString });
    mapiService().exec(this.joinRoomRequest, this.mapiRequestHandler);
  }

  private void updateViewWhenTextDeleted()
  {
    this.firstNumEditText.removeTextChangedListener(this.textWatcher);
    this.firstNumEditText.setText(this.passwordArray[0]);
    if (this.firstNumEditText.getText().length() >= 1)
      this.firstNumEditText.setSelection(1);
    int i = this.passwordArray.length - 1;
    while (true)
    {
      if (i >= 0)
      {
        if (this.passwordArray[i] != null)
        {
          this.passwordArray[i] = null;
          this.textViewArray[i].setText(null);
        }
      }
      else
      {
        this.firstNumEditText.addTextChangedListener(this.textWatcher);
        return;
      }
      this.textViewArray[i].setText(null);
      i -= 1;
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    switch (paramInt2)
    {
    default:
      return;
    case 1:
    }
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.selectdish_join_room_layout);
    initDatas(paramBundle);
    initViews();
  }

  protected void onDestroy()
  {
    if (this.joinRoomRequest != null)
    {
      mapiService().abort(this.joinRoomRequest, this.mapiRequestHandler, true);
      this.joinRoomRequest = null;
    }
    super.onDestroy();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("shopid", this.shopId);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.ui.SelectDishJoinRoomActivity
 * JD-Core Version:    0.6.0
 */