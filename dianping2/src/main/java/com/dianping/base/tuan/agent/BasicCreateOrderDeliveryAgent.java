package com.dianping.base.tuan.agent;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.widget.BasicSingleItem;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.List;

public abstract class BasicCreateOrderDeliveryAgent extends TuanCellAgent
  implements View.OnClickListener
{
  public static final String TAG = BasicCreateOrderDeliveryAgent.class.getSimpleName();
  private final String CELL_ORDER_DELIVERY;
  private BasicSingleItem addressView;
  private View deliveryLayer;
  private BasicSingleItem deliveryTypeItemView;
  private AlertDialog dialog;
  private EditText memoEt;
  private CreateOrderDeliveryModel model;
  private View rootView;
  private final TextWatcher textWatcher = new TextWatcher()
  {
    public void afterTextChanged(Editable paramEditable)
    {
    }

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
      BasicCreateOrderDeliveryAgent localBasicCreateOrderDeliveryAgent = BasicCreateOrderDeliveryAgent.this;
      if (BasicCreateOrderDeliveryAgent.this.memoEt == null);
      for (paramCharSequence = ""; ; paramCharSequence = BasicCreateOrderDeliveryAgent.this.memoEt.getText().toString())
      {
        localBasicCreateOrderDeliveryAgent.onDeliveryMessageChanged(paramCharSequence);
        return;
      }
    }
  };

  public BasicCreateOrderDeliveryAgent(Object paramObject, String paramString)
  {
    super(paramObject);
    this.CELL_ORDER_DELIVERY = paramString;
  }

  private void showDeliveryTimeListDialog(List<Pair<String, String>> paramList)
  {
    if (paramList == null)
      return;
    String[] arrayOfString = new String[paramList.size()];
    int i = 0;
    while (i < paramList.size())
    {
      arrayOfString[i] = ((String)((Pair)paramList.get(i)).second);
      i += 1;
    }
    if (this.dialog == null)
      this.dialog = new AlertDialog.Builder(getContext()).setTitle("请选择您配送的时间").setItems(arrayOfString, new DialogInterface.OnClickListener(paramList)
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          BasicCreateOrderDeliveryAgent.this.deliveryTypeItemView.setTitle((CharSequence)((Pair)this.val$dpDeliveryTypePairs.get(paramInt)).second);
          BasicCreateOrderDeliveryAgent.this.onDeliveryTimeChanged((Pair)this.val$dpDeliveryTypePairs.get(paramInt));
        }
      }).create();
    if (this.dialog.isShowing())
      this.dialog.dismiss();
    this.dialog.show();
  }

  public CreateOrderDeliveryModel getModel()
  {
    return this.model;
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.address)
      onDeliveryListButtonClick();
    do
      return;
    while ((i != R.id.delivery) || (this.model == null) || (this.model.allDeliveryTimePair == null));
    showDeliveryTimeListDialog(this.model.allDeliveryTimePair);
  }

  public abstract void onDeliveryAdressChanged(DPObject paramDPObject);

  public abstract void onDeliveryListButtonClick();

  public abstract void onDeliveryMessageChanged(String paramString);

  public abstract void onDeliveryTimeChanged(Pair<String, String> paramPair);

  public abstract void requestUserDeliveryList();

  protected void setupAddressView(DPObject paramDPObject)
  {
    onDeliveryAdressChanged(paramDPObject);
    if (paramDPObject == null)
      return;
    SpannableString localSpannableString = new SpannableString(paramDPObject.getString("Receiver"));
    localSpannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_17)), 0, localSpannableString.length(), 33);
    paramDPObject = new SpannableString(paramDPObject.getString("ShowAddress"));
    paramDPObject.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_17)), 0, paramDPObject.length(), 33);
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    localSpannableStringBuilder.append(localSpannableString).append("\n").append(paramDPObject);
    this.addressView.setTitle(localSpannableStringBuilder);
  }

  protected void setupView()
  {
    this.rootView = this.res.inflate(getContext(), R.layout.create_order_delivery, null, false);
    this.deliveryLayer = this.rootView.findViewById(R.id.delivery_info_layout);
    this.memoEt = ((EditText)this.rootView.findViewById(R.id.say_something));
    this.memoEt.addTextChangedListener(this.textWatcher);
    this.addressView = ((BasicSingleItem)this.rootView.findViewById(R.id.address));
    this.addressView.setOnClickListener(this);
    SpannableString localSpannableString = new SpannableString("请输入收货人");
    localSpannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_17)), 0, localSpannableString.length(), 33);
    localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_gray)), 0, localSpannableString.length(), 33);
    Object localObject = new SpannableString("请输入收货地址");
    ((SpannableString)localObject).setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_17)), 0, ((SpannableString)localObject).length(), 33);
    ((SpannableString)localObject).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_gray)), 0, ((SpannableString)localObject).length(), 33);
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    localSpannableStringBuilder.append(localSpannableString).append("\n").append((CharSequence)localObject);
    this.addressView.setTitle(localSpannableStringBuilder);
    this.deliveryTypeItemView = ((BasicSingleItem)this.rootView.findViewById(R.id.delivery));
    this.deliveryTypeItemView.setOnClickListener(this);
    localSpannableString = new SpannableString("请选择收货方式");
    localSpannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_17)), 0, localSpannableString.length(), 33);
    localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_gray)), 0, localSpannableString.length(), 33);
    localObject = new SpannableStringBuilder();
    ((SpannableStringBuilder)localObject).append(localSpannableString);
    this.deliveryTypeItemView.setTitle((CharSequence)localObject);
    this.deliveryLayer.setVisibility(8);
  }

  protected final void updateView(CreateOrderDeliveryModel paramCreateOrderDeliveryModel)
  {
    if (paramCreateOrderDeliveryModel == null)
      return;
    this.model = paramCreateOrderDeliveryModel;
    removeAllCells();
    if (this.model.isDelivery)
    {
      this.deliveryLayer.setVisibility(0);
      if (this.model.canBuy)
        break label279;
      this.addressView.setEnabled(false);
      this.deliveryTypeItemView.setEnabled(false);
      label58: if ((this.model.selectedDelivery == null) && (this.model.selectedDeliveryTimePair == null))
        break label298;
      Object localObject = this.model.selectedDelivery;
      paramCreateOrderDeliveryModel = this.model.selectedDeliveryTimePair;
      if (this.model.isDelivery)
      {
        if (localObject != null)
        {
          onDeliveryAdressChanged((DPObject)localObject);
          SpannableString localSpannableString = new SpannableString(((DPObject)localObject).getString("Receiver"));
          localSpannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_17)), 0, localSpannableString.length(), 33);
          localObject = new SpannableString(((DPObject)localObject).getString("ShowAddress"));
          ((SpannableString)localObject).setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_17)), 0, ((SpannableString)localObject).length(), 33);
          SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
          localSpannableStringBuilder.append(localSpannableString).append("\n").append((CharSequence)localObject);
          this.addressView.setTitle(localSpannableStringBuilder);
        }
        if (paramCreateOrderDeliveryModel != null)
        {
          onDeliveryTimeChanged(paramCreateOrderDeliveryModel);
          this.deliveryTypeItemView.setTitle((CharSequence)paramCreateOrderDeliveryModel.second);
        }
      }
    }
    while (true)
    {
      addCell(this.CELL_ORDER_DELIVERY, this.rootView);
      return;
      this.deliveryLayer.setVisibility(8);
      break;
      label279: this.addressView.setEnabled(true);
      this.deliveryTypeItemView.setEnabled(true);
      break label58;
      label298: if ((!this.model.isDelivery) || (this.model.allDeliveryTimePair == null) || (this.model.allDeliveryTimePair.size() <= 0))
        continue;
      paramCreateOrderDeliveryModel = (Pair)this.model.allDeliveryTimePair.get(0);
      onDeliveryTimeChanged(paramCreateOrderDeliveryModel);
      this.deliveryTypeItemView.setTitle((CharSequence)paramCreateOrderDeliveryModel.second);
      requestUserDeliveryList();
    }
  }

  public static class CreateOrderDeliveryModel
  {
    public final List<Pair<String, String>> allDeliveryTimePair;
    public final boolean canBuy;
    public final boolean isDelivery;
    public final DPObject selectedDelivery;
    public final Pair<String, String> selectedDeliveryTimePair;

    public CreateOrderDeliveryModel(boolean paramBoolean1, DPObject paramDPObject, Pair<String, String> paramPair, List<Pair<String, String>> paramList, boolean paramBoolean2)
    {
      this.isDelivery = paramBoolean1;
      this.allDeliveryTimePair = paramList;
      this.selectedDelivery = paramDPObject;
      this.selectedDeliveryTimePair = paramPair;
      this.canBuy = paramBoolean2;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.BasicCreateOrderDeliveryAgent
 * JD-Core Version:    0.6.0
 */