package com.dianping.takeaway.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.RatingBar;
import com.dianping.base.widget.RatingBar.OnRatingChangedListener;
import com.dianping.base.widget.ScoreView;
import com.dianping.base.widget.ScoreView.OnRatingChangedListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TakeawayReviewAddActivity extends NovaActivity
  implements View.OnTouchListener
{
  private final int MAX_LENGTH = 1000;
  Context context = this;
  protected List<TAEvaluationDish> dishList = new ArrayList();
  private TAEvaluationDishListAdapter dishListAdapter;
  protected ListView dishListView;
  private View editLayout;
  private EditText editReviewText;
  private MApiRequest getEvaluationDishListRequest;
  private TextView numberTipText;
  private RatingBar ratingBar;
  private MApiRequest reviewRequest;
  private ViewGroup scoreLayout;
  private List<ScoreEntity> scoreList;
  private String shopName;
  private int source;
  private TextView subTitleView;
  private ViewGroup tipsLayout;
  private TextView titleView;
  private String viewOrderId;

  private void dealEditTextFoucs()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
    if ((localInputMethodManager != null) && (getCurrentFocus() != null) && (getCurrentFocus().getWindowToken() != null))
    {
      localInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
      this.editReviewText.getParent().getParent().requestDisallowInterceptTouchEvent(false);
    }
  }

  private void getEvaluationDishListTask(String paramString)
  {
    if (this.getEvaluationDishListRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://waimai.api.dianping.com/reviewconfirm.ta").buildUpon();
    localBuilder.appendQueryParameter("orderviewid", paramString);
    this.getEvaluationDishListRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.NORMAL);
    super.mapiService().exec(this.getEvaluationDishListRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayReviewAddActivity.access$002(TakeawayReviewAddActivity.this, null);
        TakeawayReviewAddActivity.this.dishListView.setVisibility(8);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayReviewAddActivity.access$002(TakeawayReviewAddActivity.this, null);
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = ((DPObject)paramMApiResponse.result()).getArray("DishItems");
          if ((paramMApiRequest != null) && (paramMApiRequest.length != 0))
          {
            TakeawayReviewAddActivity.this.dishList.clear();
            int j = paramMApiRequest.length;
            int i = 0;
            while (i < j)
            {
              paramMApiResponse = paramMApiRequest[i];
              paramMApiResponse = new TakeawayReviewAddActivity.TAEvaluationDish(paramMApiResponse.getInt("DishId"), paramMApiResponse.getString("Title"), 0);
              TakeawayReviewAddActivity.this.dishList.add(paramMApiResponse);
              i += 1;
            }
            TakeawayReviewAddActivity.this.dishListAdapter.notifyDataSetChanged();
            TakeawayReviewAddActivity.this.dishListView.setVisibility(0);
          }
        }
        else
        {
          return;
        }
        TakeawayReviewAddActivity.this.dishListView.setVisibility(8);
      }
    });
  }

  private void initEditView(View paramView)
  {
    this.editReviewText = ((EditText)paramView.findViewById(R.id.review_edt));
    this.editReviewText.setHint("输入评价，人人为我，我为人人");
    this.editReviewText.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
        TakeawayReviewAddActivity.this.numberTipText.setText("您还可以输入" + (1000 - paramEditable.toString().length()) + "字");
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    });
    this.numberTipText = ((TextView)paramView.findViewById(R.id.number_tip));
    this.numberTipText.setText("您还可以输入1000字");
    this.editReviewText.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        paramView.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
      }
    });
    this.editReviewText.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayReviewAddActivity.this.scrollView();
      }
    });
    this.editReviewText.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramView, boolean paramBoolean)
      {
        if (paramBoolean)
          TakeawayReviewAddActivity.this.scrollView();
      }
    });
    findViewById(R.id.review_root).setOnTouchListener(this);
  }

  private void initScoreData()
  {
    if (this.scoreList != null)
      return;
    this.scoreList = new ArrayList(2);
    ScoreEntity localScoreEntity = new ScoreEntity(null);
    localScoreEntity.title = "口味";
    String[] arrayOfString = new String[5];
    arrayOfString[0] = "非常好   ";
    arrayOfString[1] = "很好   ";
    arrayOfString[2] = "好   ";
    arrayOfString[3] = "一般   ";
    arrayOfString[4] = "差   ";
    localScoreEntity.scoreTextes = arrayOfString;
    this.scoreList.add(localScoreEntity);
    localScoreEntity = new ScoreEntity(null);
    localScoreEntity.title = "服务";
    localScoreEntity.scoreTextes = arrayOfString;
    this.scoreList.add(localScoreEntity);
  }

  private void initScoreView(ViewGroup paramViewGroup)
  {
    initScoreData();
    int i = 0;
    while (i < this.scoreList.size())
    {
      ScoreEntity localScoreEntity = (ScoreEntity)this.scoreList.get(i);
      ScoreView localScoreView = new ScoreView(this.context);
      localScoreEntity.scroeView = localScoreView;
      localScoreView.setLabel(localScoreEntity.title);
      localScoreView.setScoreText(localScoreEntity.scoreTextes);
      localScoreView.setTag(Integer.valueOf(i));
      localScoreView.setOnRatingChangedListener(((Integer)localScoreView.getTag()).intValue(), new ScoreView.OnRatingChangedListener(localScoreView)
      {
        public void afterRatingChanged(int paramInt)
        {
        }

        public void onRatingChanged(int paramInt)
        {
          paramInt = this.val$scoreView.score() / 10;
          int i = ((Integer)this.val$scoreView.getTag()).intValue();
          ((TakeawayReviewAddActivity.ScoreEntity)TakeawayReviewAddActivity.this.scoreList.get(i)).score = paramInt;
        }
      });
      paramViewGroup.addView(localScoreView);
      i += 1;
    }
  }

  private void scrollView()
  {
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        int i = TakeawayReviewAddActivity.this.getWindow().findViewById(16908290).getTop();
        int[] arrayOfInt = new int[2];
        TakeawayReviewAddActivity.this.editReviewText.getLocationInWindow(arrayOfInt);
        int j = arrayOfInt[0];
        int k = arrayOfInt[1];
        TakeawayReviewAddActivity.this.findViewById(R.id.review_root).scrollBy(j, k - i);
      }
    }
    , 100L);
  }

  private void setupView()
  {
    super.setContentView(R.layout.takeaway_submitview);
    super.getTitleBar().setCustomContentView(getLayoutInflater().inflate(R.layout.title_bar_double, null));
    this.titleView = ((TextView)findViewById(R.id.title));
    this.subTitleView = ((TextView)findViewById(R.id.subtitle));
    this.titleView.setText("添加评价");
    this.subTitleView.setText(this.shopName);
    super.getTitleBar().addRightViewItem("发表", "submit", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayReviewAddActivity.this.submitReview();
      }
    });
    this.tipsLayout = ((ViewGroup)findViewById(R.id.tipsLayout));
    if (this.source == 2)
      this.tipsLayout.setVisibility(8);
    statisticsEvent("takeaway6", "takeaway6_comment_track", "", this.source);
    this.scoreLayout = ((ViewGroup)findViewById(R.id.scoreLay));
    initScoreView(this.scoreLayout);
    this.ratingBar = ((RatingBar)findViewById(R.id.ratingbar));
    this.ratingBar.setOnTouchListener(this);
    this.ratingBar.setOnRatingChangedListener(new RatingBar.OnRatingChangedListener()
    {
      public void afterRatingChanged(int paramInt)
      {
        int i = paramInt;
        if (paramInt < 10)
        {
          i = 10;
          TakeawayReviewAddActivity.this.ratingBar.setStar(10);
        }
        if (!TakeawayReviewAddActivity.this.scoreLayout.isShown())
        {
          Iterator localIterator = TakeawayReviewAddActivity.this.scoreList.iterator();
          while (localIterator.hasNext())
          {
            TakeawayReviewAddActivity.ScoreEntity localScoreEntity = (TakeawayReviewAddActivity.ScoreEntity)localIterator.next();
            localScoreEntity.score = (i / 10);
            localScoreEntity.scroeView.setScore(i / 10 - 1);
          }
          TakeawayReviewAddActivity.this.scoreLayout.setVisibility(0);
        }
      }

      public void onRatingChanged(int paramInt)
      {
      }
    });
    this.editLayout = findViewById(R.id.review_lay);
    initEditView(this.editLayout);
    this.dishListView = ((ListView)findViewById(R.id.dish_list));
    this.dishListAdapter = new TAEvaluationDishListAdapter(null);
    this.dishListView.setAdapter(this.dishListAdapter);
  }

  public String getPageName()
  {
    return "takeawayreview";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.viewOrderId = super.getStringParam("orderid");
    this.source = super.getIntParam("source", 0);
    this.shopName = super.getStringParam("shopname");
    setupView();
    getEvaluationDishListTask(this.viewOrderId);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.reviewRequest != null)
    {
      super.mapiService().abort(this.reviewRequest, null, true);
      this.reviewRequest = null;
    }
    if (this.getEvaluationDishListRequest != null)
    {
      super.mapiService().abort(this.getEvaluationDishListRequest, null, true);
      this.getEvaluationDishListRequest = null;
    }
  }

  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() != 0)
      return false;
    paramView.requestFocus();
    dealEditTextFoucs();
    return false;
  }

  protected void submitReview()
  {
    this.ratingBar.requestFocus();
    if (this.reviewRequest != null)
      return;
    if (this.ratingBar.star() <= 0)
    {
      showToast("您还没有为星级评分");
      return;
    }
    int i = 0;
    while (i < this.scoreList.size())
    {
      if (((ScoreEntity)this.scoreList.get(i)).score < 0)
      {
        showToast("您还没有为" + ((ScoreEntity)this.scoreList.get(i)).title + "评分");
        return;
      }
      i += 1;
    }
    super.showProgressDialog("正在提交...");
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("totalscore");
    localArrayList.add(String.valueOf(String.valueOf(this.ratingBar.star() / 10)));
    localArrayList.add("packagescore");
    localArrayList.add(String.valueOf(((ScoreEntity)this.scoreList.get(1)).score));
    localArrayList.add("taste");
    localArrayList.add(String.valueOf(((ScoreEntity)this.scoreList.get(0)).score));
    String str = this.editReviewText.getText().toString();
    if (!TextUtils.isEmpty(str))
    {
      localArrayList.add("content");
      localArrayList.add(str);
    }
    localArrayList.add("orderviewid");
    localArrayList.add(this.viewOrderId);
    if (!TextUtils.isEmpty(accountService().token()))
    {
      localArrayList.add("token");
      localArrayList.add(accountService().token());
    }
    localArrayList.add("uuid");
    localArrayList.add(Environment.uuid());
    str = "";
    Iterator localIterator = this.dishList.iterator();
    while (localIterator.hasNext())
    {
      TAEvaluationDish localTAEvaluationDish = (TAEvaluationDish)localIterator.next();
      str = String.format("%s%s,%s|", new Object[] { str, Integer.valueOf(localTAEvaluationDish.dishId), Integer.valueOf(localTAEvaluationDish.favouredStatus) });
    }
    localArrayList.add("dishcontent");
    localArrayList.add(str);
    this.reviewRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/review.ta", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.reviewRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayReviewAddActivity.access$702(TakeawayReviewAddActivity.this, null);
        TakeawayReviewAddActivity.this.dismissDialog();
        if ((paramMApiResponse != null) && (paramMApiResponse.message() != null))
        {
          TakeawayReviewAddActivity.this.showToast(paramMApiResponse.message().content());
          return;
        }
        TakeawayReviewAddActivity.this.showToast("网络不给力哦，请稍后再试");
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayReviewAddActivity.access$702(TakeawayReviewAddActivity.this, null);
        TakeawayReviewAddActivity.this.dismissDialog();
        Object localObject = "";
        paramMApiRequest = (MApiRequest)localObject;
        if (paramMApiResponse != null)
        {
          paramMApiRequest = (MApiRequest)localObject;
          if ((paramMApiResponse.result() instanceof DPObject))
            paramMApiRequest = ((DPObject)paramMApiResponse.result()).getString("Content");
        }
        localObject = TakeawayReviewAddActivity.this;
        paramMApiResponse = paramMApiRequest;
        if (TextUtils.isEmpty(paramMApiRequest))
          paramMApiResponse = "评价成功";
        ((TakeawayReviewAddActivity)localObject).showToast(paramMApiResponse);
        paramMApiRequest = new Intent("com.dianping.takeaway.UPDATE_ORDER");
        paramMApiRequest.putExtra("orderviewid", TakeawayReviewAddActivity.this.viewOrderId);
        TakeawayReviewAddActivity.this.sendBroadcast(paramMApiRequest);
        TakeawayReviewAddActivity.this.finish();
      }
    });
    statisticsEvent("takeaway6", "takeaway6_comment_confirmclk", "", this.source);
  }

  private static class ScoreEntity
  {
    public int score;
    public String[] scoreTextes;
    public ScoreView scroeView;
    public String title;
  }

  private static class TAEvaluationDish
  {
    int dishId;
    String dishTitle;
    int favouredStatus;

    TAEvaluationDish(int paramInt1, String paramString, int paramInt2)
    {
      this.dishId = paramInt1;
      this.dishTitle = paramString;
      this.favouredStatus = paramInt2;
    }
  }

  private class TAEvaluationDishListAdapter extends BaseAdapter
  {
    private TAEvaluationDishListAdapter()
    {
    }

    public int getCount()
    {
      return TakeawayReviewAddActivity.this.dishList.size();
    }

    public Object getItem(int paramInt)
    {
      return TakeawayReviewAddActivity.this.dishList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = TakeawayReviewAddActivity.this.getLayoutInflater().inflate(R.layout.takeaway_evaluation_dish_item, null, true);
      paramView = (TakeawayReviewAddActivity.TAEvaluationDish)TakeawayReviewAddActivity.this.dishList.get(paramInt);
      ViewUtils.setVisibilityAndContent((TextView)paramViewGroup.findViewById(R.id.dish_name), paramView.dishTitle);
      ImageView localImageView = (ImageView)paramViewGroup.findViewById(R.id.evaluate_icon);
      localImageView.setOnClickListener(new View.OnClickListener(paramView, localImageView)
      {
        public void onClick(View paramView)
        {
          this.val$item.favouredStatus = (1 - this.val$item.favouredStatus);
          paramView = this.val$evaluateIcon;
          if (this.val$item.favouredStatus == 0);
          for (int i = R.drawable.wm_evaluation_love_normal; ; i = R.drawable.wm_evaluation_love_press)
          {
            paramView.setImageResource(i);
            TakeawayReviewAddActivity.this.statisticsEvent("takeaway6", "takeaway6_comment_dishclk", TakeawayReviewAddActivity.this.viewOrderId, 0);
            return;
          }
        }
      });
      return paramViewGroup;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayReviewAddActivity
 * JD-Core Version:    0.6.0
 */