package com.dianping.main.find.pictureplaza;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.util.KeyboardUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;

public class FeedCommentListAgent extends PlazaAdapterCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String COMMENT_HEADER_TAG = "060CommentHeader";
  private static final String COMMENT_LIST_TAG = "061CommentList";
  private String mComment;
  private CommentAdapter mCommentAdapter;
  private int mCommentCount = -1;
  private int mCommentIdReplyTo = 0;
  private TextView mCountView;
  private int mFeedAuthorId = 0;
  private String mFeedId;
  private int mFeedType;
  private View mHeaderView;
  private EditText mInputView;
  private boolean mIsRegister = false;
  private NovaTextView mMoreView;
  final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.PlazaFeedAuthorId".equals(paramIntent.getAction()))
        FeedCommentListAgent.access$002(FeedCommentListAgent.this, paramIntent.getIntExtra("FeedAuthorId", 0));
    }
  };
  private int mReplyType = 1;
  private MApiRequest mReqAddComment;
  private MApiRequest mReqDelComment;
  private NovaButton mSendButton;
  private String mUserNickReplyTo = "";

  public FeedCommentListAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void deleteComment(int paramInt)
  {
    Object localObject = this.mCommentAdapter.getItem(paramInt);
    if ((localObject instanceof DPObject))
      sendDelCommentReq(((DPObject)localObject).getInt("CommentId"));
    this.mCommentAdapter.remove(paramInt);
  }

  private boolean hasCommentId(DPObject paramDPObject)
  {
    return paramDPObject.getInt("CommentId") != 0;
  }

  private boolean isCommentAuthor(DPObject paramDPObject)
  {
    UserProfile localUserProfile = getAccount();
    if ((localUserProfile == null) || (paramDPObject == null));
    do
      return false;
    while (localUserProfile.id() != paramDPObject.getInt("UserID"));
    return true;
  }

  private boolean isFeedAuthor()
  {
    UserProfile localUserProfile = getAccount();
    if ((localUserProfile == null) || (this.mFeedAuthorId == 0));
    do
      return false;
    while (localUserProfile.id() != this.mFeedAuthorId);
    return true;
  }

  private boolean isShouldHideInput(View paramView, MotionEvent paramMotionEvent)
  {
    if ((paramView != null) && ((paramView instanceof EditText)))
    {
      paramView = (View)paramView.getParent();
      if (paramView != null)
        break label25;
    }
    label25: int i;
    int j;
    int k;
    int m;
    do
    {
      return false;
      int[] arrayOfInt = new int[2];
      int[] tmp30_29 = arrayOfInt;
      tmp30_29[0] = 0;
      int[] tmp34_30 = tmp30_29;
      tmp34_30[1] = 0;
      tmp34_30;
      paramView.getLocationInWindow(arrayOfInt);
      i = arrayOfInt[0];
      j = arrayOfInt[1];
      k = paramView.getHeight();
      m = paramView.getWidth();
    }
    while ((paramMotionEvent.getX() > i) && (paramMotionEvent.getX() < i + m) && (paramMotionEvent.getY() > j) && (paramMotionEvent.getY() < j + k));
    return true;
  }

  private void replyUser(DPObject paramDPObject)
  {
    if (this.mInputView != null)
    {
      KeyboardUtils.hideKeyboard(this.mInputView);
      KeyboardUtils.popupKeyboard(this.mInputView);
      this.mInputView.setText("");
      String str = "";
      if (paramDPObject.getObject("User") != null)
        str = paramDPObject.getObject("User").getString("Nick");
      this.mInputView.setHint("回复 " + str + "：");
      this.mUserNickReplyTo = str;
      this.mCommentIdReplyTo = paramDPObject.getInt("CommentId");
      this.mReplyType = 2;
    }
  }

  private void resetComment()
  {
    this.mInputView.setText("");
    this.mInputView.setHint("写点评论吧");
    this.mComment = "";
    this.mCommentIdReplyTo = 0;
    this.mReplyType = 1;
    this.mUserNickReplyTo = "";
  }

  private void sendAddCommentReq(String paramString)
  {
    if (this.mReqAddComment != null)
    {
      getFragment().mapiService().abort(this.mReqAddComment, this, true);
      this.mReqAddComment = null;
    }
    String str = Uri.parse("http://m.api.dianping.com/review/addugccomment.bin").toString();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("content");
    localArrayList.add(paramString);
    localArrayList.add("mainid");
    localArrayList.add(this.mFeedId);
    localArrayList.add("feedtype");
    localArrayList.add(String.valueOf(this.mFeedType));
    localArrayList.add("notetype");
    localArrayList.add(String.valueOf(this.mReplyType));
    localArrayList.add("noteid");
    localArrayList.add(String.valueOf(this.mCommentIdReplyTo));
    localArrayList.add("originuserid");
    localArrayList.add(String.valueOf(this.mFeedAuthorId));
    this.mReqAddComment = BasicMApiRequest.mapiPost(str, (String[])localArrayList.toArray(new String[0]));
    getFragment().mapiService().exec(this.mReqAddComment, this);
  }

  private void sendCommentCountBroadcast()
  {
    Intent localIntent = new Intent("com.dianping.action.PlazaFeedCommentCount");
    localIntent.putExtra("commentcount", this.mCommentCount);
    LocalBroadcastManager.getInstance(DPApplication.instance()).sendBroadcast(localIntent);
  }

  private void sendDelCommentReq(int paramInt)
  {
    if (this.mReqDelComment != null)
    {
      getFragment().mapiService().abort(this.mReqDelComment, this, true);
      this.mReqDelComment = null;
    }
    String str = Uri.parse("http://m.api.dianping.com/review/deleteugccomment.bin").toString();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("noteid");
    localArrayList.add(String.valueOf(paramInt));
    localArrayList.add("feedtype");
    localArrayList.add(String.valueOf(this.mFeedType));
    this.mReqDelComment = BasicMApiRequest.mapiPost(str, (String[])localArrayList.toArray(new String[0]));
    getFragment().mapiService().exec(this.mReqDelComment, this);
  }

  private void setItemClickListener(View paramView, DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    paramView.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        if (FeedCommentListAgent.this.hasCommentId(this.val$itemClicked))
          FeedCommentListAgent.this.replyUser(this.val$itemClicked);
      }
    });
  }

  private void setItemLongClickListener(View paramView, DPObject paramDPObject, int paramInt)
  {
    if (paramDPObject == null)
      return;
    paramView.setOnLongClickListener(new View.OnLongClickListener(paramDPObject, paramInt)
    {
      public boolean onLongClick(View paramView)
      {
        if (FeedCommentListAgent.this.hasCommentId(this.val$itemClicked))
          FeedCommentListAgent.this.showOperationMenu(FeedCommentListAgent.access$1200(FeedCommentListAgent.this, this.val$itemClicked.getObject("User")), FeedCommentListAgent.access$1300(FeedCommentListAgent.this), this.val$itemClicked, this.val$itemPosition);
        return false;
      }
    });
  }

  private void showDeleteDialog(int paramInt)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
    localBuilder.setTitle("提示").setMessage("确定要删除评论吗？").setPositiveButton("确定", new DialogInterface.OnClickListener(paramInt)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        FeedCommentListAgent.this.deleteComment(this.val$itemPosition);
        paramDialogInterface.dismiss();
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.dismiss();
      }
    });
    localBuilder.show();
  }

  private void showOperationMenu(boolean paramBoolean1, boolean paramBoolean2, DPObject paramDPObject, int paramInt)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
    localBuilder.setTitle("操作");
    paramDPObject = new DialogInterface.OnClickListener(paramDPObject, paramBoolean1, paramBoolean2, paramInt)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if (paramInt == 0)
          FeedCommentListAgent.this.replyUser(this.val$itemClicked);
        while (true)
        {
          paramDialogInterface.dismiss();
          return;
          if ((paramInt != 1) || ((!this.val$isCommentAuthor) && (!this.val$isFeedAuthor)))
            continue;
          FeedCommentListAgent.this.showDeleteDialog(this.val$itemPosition);
        }
      }
    };
    if ((paramBoolean1) || (paramBoolean2))
      localBuilder.setItems(new String[] { "回复", "删除", "取消" }, paramDPObject);
    while (true)
    {
      localBuilder.show();
      return;
      localBuilder.setItems(new String[] { "回复", "取消" }, paramDPObject);
    }
  }

  View createHeaderView(ViewGroup paramViewGroup)
  {
    this.mHeaderView = this.res.inflate(getContext(), R.layout.find_plaza_feed_comment, paramViewGroup, false);
    this.mCountView = ((TextView)this.mHeaderView.findViewById(R.id.feed_comment_count));
    this.mMoreView = ((NovaTextView)this.mHeaderView.findViewById(R.id.comments_more));
    this.mMoreView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        FeedCommentListAgent.this.mCommentAdapter.getMoreComment();
      }
    });
    return this.mHeaderView;
  }

  public void handleRootViewTouchEvent(View paramView, MotionEvent paramMotionEvent)
  {
    if ((isShouldHideInput(paramView, paramMotionEvent)) && (this.mInputView != null))
    {
      KeyboardUtils.hideKeyboard(this.mInputView);
      resetComment();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mFeedId = getFragment().getStringParam("feedid");
    this.mFeedType = getFragment().getIntParam("feedtype", 0);
    this.mFeedAuthorId = getFragment().getIntParam("feedauthorid", 0);
    if (getFragment().getIntParam("isfromnotificationcenter", 0) == 1)
    {
      localObject = new IntentFilter();
      ((IntentFilter)localObject).addAction("com.dianping.action.PlazaFeedAuthorId");
      LocalBroadcastManager.getInstance(DPApplication.instance()).registerReceiver(this.mReceiver, (IntentFilter)localObject);
      this.mIsRegister = true;
    }
    if ((getFragment().getActivity() instanceof FeedDetailActivity))
      ((FeedDetailActivity)getFragment().getActivity()).setCommentAgent(this);
    Object localObject = (FeedDetailFragment)getFragment();
    this.mInputView = ((EditText)((FeedDetailFragment)localObject).getFragmentView().findViewById(R.id.detail_input_text));
    this.mInputView.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
        if (paramEditable.length() > 0)
        {
          FeedCommentListAgent.this.mSendButton.setEnabled(true);
          FeedCommentListAgent.this.mSendButton.setTextColor(FeedCommentListAgent.this.res.getColor(R.color.light_red));
          return;
        }
        FeedCommentListAgent.this.mSendButton.setEnabled(false);
        FeedCommentListAgent.this.mSendButton.setTextColor(FeedCommentListAgent.this.res.getColor(R.color.light_gray));
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    });
    if (getFragment().getIntParam("needsoftinput", 0) == 1)
      KeyboardUtils.popupKeyboard(this.mInputView);
    this.mInputView.setOnKeyListener(new View.OnKeyListener()
    {
      public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
      {
        if (paramInt == 4)
          FeedCommentListAgent.this.resetComment();
        return false;
      }
    });
    this.mSendButton = ((NovaButton)((FeedDetailFragment)localObject).getFragmentView().findViewById(R.id.do_comment));
    this.mSendButton.setEnabled(false);
    this.mSendButton.setTextColor(this.res.getColor(R.color.light_gray));
    this.mSendButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (FeedCommentListAgent.this.accountService().token() == null)
        {
          FeedCommentListAgent.this.accountService().login(new LoginResultListener()
          {
            public void onLoginCancel(AccountService paramAccountService)
            {
            }

            public void onLoginSuccess(AccountService paramAccountService)
            {
              FeedCommentListAgent.4.this.onClick(null);
            }
          });
          return;
        }
        if (TextUtils.isEmpty(FeedCommentListAgent.this.mInputView.getText().toString().trim()))
        {
          FeedCommentListAgent.this.showToast("评论内容不能为空哦～");
          return;
        }
        FeedCommentListAgent.access$402(FeedCommentListAgent.this, FeedCommentListAgent.this.mInputView.getText().toString());
        FeedCommentListAgent.this.sendAddCommentReq(FeedCommentListAgent.this.mComment);
        if ((FeedCommentListAgent.this.mReplyType == 2) && (!TextUtils.isEmpty(FeedCommentListAgent.this.mUserNickReplyTo)))
          FeedCommentListAgent.access$402(FeedCommentListAgent.this, String.format("[{\"text\":\"回复%s: \",\"textsize\":14,\"textcolor\":\"#999999\",\"backgroundcolor\":\"#FFFFFF\",\"textstyle\":\"Default\",\"strikethrough\":false,\"underline\":false}, {\"text\":\"%s\",\"textsize\":14,\"textcolor\":\"#000000\",\"backgroundcolor\":\"#FFFFFF\",\"textstyle\":\"Default\",\"strikethrough\":false,\"underline\":false}]", new Object[] { FeedCommentListAgent.access$700(FeedCommentListAgent.this), FeedCommentListAgent.access$400(FeedCommentListAgent.this) }));
        FeedCommentListAgent.this.mCommentAdapter.appendUserComment(FeedCommentListAgent.this.mComment);
        FeedCommentListAgent.this.sendCommentCountBroadcast();
        FeedCommentListAgent.this.resetComment();
        KeyboardUtils.hideKeyboard(FeedCommentListAgent.this.mInputView);
      }
    });
    if (paramBundle != null)
    {
      paramBundle = paramBundle.getString("comment");
      if (!TextUtils.isEmpty(paramBundle))
        this.mInputView.setText(paramBundle);
    }
    this.mCommentAdapter = new CommentAdapter(getContext());
    addCell("060CommentHeader", new HeaderAdapter());
    addCell("061CommentList", this.mCommentAdapter);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mReqAddComment != null)
    {
      getFragment().mapiService().abort(this.mReqAddComment, this, true);
      this.mReqAddComment = null;
    }
    if (this.mReqDelComment != null)
    {
      getFragment().mapiService().abort(this.mReqDelComment, this, true);
      this.mReqDelComment = null;
    }
    if (this.mIsRegister);
    try
    {
      LocalBroadcastManager.getInstance(DPApplication.instance()).unregisterReceiver(this.mReceiver);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  protected void onRefresh()
  {
    this.mCommentCount = -1;
    this.mHeaderView = null;
    if (this.mCommentAdapter != null)
      this.mCommentAdapter.reset();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mReqAddComment == paramMApiRequest)
      this.mReqAddComment = null;
    while ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (!TextUtils.isEmpty(paramMApiResponse.message().content())))
    {
      showToast(paramMApiResponse.message().content());
      return;
      if (this.mReqDelComment != paramMApiRequest)
        continue;
      this.mReqDelComment = null;
    }
    showToast("请求失败，请稍后再试");
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mReqAddComment == paramMApiRequest)
      this.mReqAddComment = null;
    do
      return;
    while (this.mReqDelComment != paramMApiRequest);
    this.mReqDelComment = null;
  }

  public Bundle saveInstanceState()
  {
    if ((this.mInputView == null) || (TextUtils.isEmpty(this.mInputView.getText().toString())))
      return null;
    Bundle localBundle = new Bundle();
    localBundle.putString("comment", this.mInputView.getText().toString());
    return localBundle;
  }

  class CommentAdapter extends BasicLoadAdapter
  {
    private Context context;

    public CommentAdapter(Context arg2)
    {
      super();
      this.context = localContext;
      loadNewPage();
    }

    protected void appendDataToList(DPObject[] paramArrayOfDPObject)
    {
      if (paramArrayOfDPObject != null)
      {
        ArrayList localArrayList = new ArrayList();
        int i = 0;
        while (i < paramArrayOfDPObject.length)
        {
          localArrayList.add(paramArrayOfDPObject[i]);
          i += 1;
        }
        this.mData.addAll(0, localArrayList);
      }
    }

    public void appendUserComment(String paramString)
    {
      Object localObject = FeedCommentListAgent.this.getAccount();
      if (localObject == null)
      {
        Toast.makeText(FeedCommentListAgent.this.getContext(), "您还没有登陆哦～", 0).show();
        return;
      }
      localObject = new DPObject("PlazaUser").edit().putString("Avatar", ((UserProfile)localObject).avatar()).putString("Nick", ((UserProfile)localObject).nickName()).putInt("UserID", ((UserProfile)localObject).id()).generate();
      paramString = new DPObject("PlazaComment").edit().putString("Content", paramString).putString("Time", "刚刚").putObject("User", (DPObject)localObject).generate();
      this.mData.add(paramString);
      FeedCommentListAgent.access$1702(FeedCommentListAgent.this, Math.max(FeedCommentListAgent.this.mCommentCount, 0));
      FeedCommentListAgent.access$1708(FeedCommentListAgent.this);
      notifyDataSetChanged();
      paramString = (FeedDetailFragment)FeedCommentListAgent.this.getFragment();
      paramString.getListView().setSelection(paramString.getListView().getAdapter().getCount() - 1);
    }

    public MApiRequest createRequest(int paramInt)
    {
      return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/review/getugccommentlist.bin").buildUpon().appendQueryParameter("start", String.valueOf(paramInt)).appendQueryParameter("limit", String.valueOf(50)).appendQueryParameter("mainid", FeedCommentListAgent.this.mFeedId).appendQueryParameter("feedtype", String.valueOf(FeedCommentListAgent.this.mFeedType)).build().toString(), CacheType.DISABLED);
    }

    public int getCount()
    {
      if (this.mIsEnd)
      {
        if (this.mData.size() == 0)
          return 1;
        return this.mData.size();
      }
      if ((this.mReq != null) || (this.mErrorMsg != null))
        return this.mData.size() + 1;
      return this.mData.size();
    }

    public Object getItem(int paramInt)
    {
      if (((this.mEmptyMsg != null) && (this.mData.size() == 0)) || ((this.mIsEnd) && (this.mData.size() == 0)))
        return EMPTY;
      int i;
      if (this.mReq == null)
      {
        i = paramInt;
        if (this.mErrorMsg == null);
      }
      else
      {
        i = paramInt - 1;
      }
      if ((i >= 0) && (i < this.mData.size()))
        return this.mData.get(i);
      if (this.mErrorMsg == null)
        return LOADING;
      return ERROR;
    }

    public void getMoreComment()
    {
      loadNewPage();
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
        return itemViewWithData((DPObject)localObject, paramInt, paramView, paramViewGroup);
      if (localObject == LOADING)
        return getLoadingView(paramViewGroup, paramView);
      if (localObject == EMPTY)
        return new View(FeedCommentListAgent.this.getContext());
      return getFailedView(this.mErrorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          FeedCommentListAgent.CommentAdapter.this.loadNewPage();
        }
      }
      , paramViewGroup, paramView);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramDPObject = null;
      Object localObject1 = null;
      Object localObject2 = getItem(paramInt);
      if ((localObject2 instanceof DPObject))
      {
        paramDPObject = localObject1;
        if ((paramView instanceof PlazaCommentItem))
          paramDPObject = (PlazaCommentItem)paramView;
        paramView = paramDPObject;
        if (paramDPObject == null)
          paramView = (PlazaCommentItem)LayoutInflater.from(this.context).inflate(R.layout.find_plaza_comment_item, paramViewGroup, false);
        paramView.setCommentData((DPObject)localObject2);
        FeedCommentListAgent.this.setItemClickListener(paramView, (DPObject)localObject2);
        FeedCommentListAgent.this.setItemLongClickListener(paramView, (DPObject)localObject2, paramInt);
        paramDPObject = paramView;
      }
      return paramDPObject;
    }

    protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestComplete(paramBoolean, paramMApiRequest, paramMApiResponse);
      if ((paramBoolean) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        FeedCommentListAgent.access$1702(FeedCommentListAgent.this, Math.max(((DPObject)paramMApiResponse.result()).getInt("RecordCount"), this.mData.size()));
        FeedCommentListAgent.this.sendCommentCountBroadcast();
      }
      notifyDataSetChanged();
    }

    public void remove(int paramInt)
    {
      this.mData.remove(paramInt);
      if (FeedCommentListAgent.this.mCommentCount > 0)
        FeedCommentListAgent.access$1710(FeedCommentListAgent.this);
      if (FeedCommentListAgent.this.mCommentCount <= 0)
        FeedCommentListAgent.this.sendCommentCountBroadcast();
      notifyDataSetChanged();
    }

    public void reset()
    {
      super.reset();
      loadNewPage();
      notifyDataSetChanged();
    }
  }

  class HeaderAdapter extends BaseAdapter
  {
    HeaderAdapter()
    {
    }

    public int getCount()
    {
      if (FeedCommentListAgent.this.mCommentCount > 0)
        return 1;
      return 0;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (FeedCommentListAgent.this.mHeaderView == null)
        FeedCommentListAgent.this.createHeaderView(paramViewGroup);
      if (FeedCommentListAgent.this.mCountView != null)
        FeedCommentListAgent.this.mCountView.setText(FeedCommentListAgent.this.mCommentCount + "条评论");
      if ((FeedCommentListAgent.this.mMoreView != null) && (FeedCommentListAgent.this.mCommentAdapter.isEnd()))
        FeedCommentListAgent.this.mMoreView.setVisibility(8);
      return FeedCommentListAgent.this.mHeaderView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.FeedCommentListAgent
 * JD-Core Version:    0.6.0
 */