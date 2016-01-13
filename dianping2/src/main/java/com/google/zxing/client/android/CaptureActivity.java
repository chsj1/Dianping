package com.google.zxing.client.android;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.util.PermissionCheckHelper;
import com.dianping.util.PermissionCheckHelper.PermissionCallbackListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.android.result.ResultHandlerFactory;
import com.google.zxing.client.android.result.supplement.SupplementalInfoRetriever;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.URIParsedResult;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CaptureActivity extends Activity
  implements SurfaceHolder.Callback
{
  private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;
  private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;
  private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES;
  private static final String PRODUCT_SEARCH_URL_PREFIX = "http://www.google";
  private static final String PRODUCT_SEARCH_URL_SUFFIX = "/m/products/scan";
  private static final int REQUEST_CHECK_URL = 1;
  private static final String TAG = CaptureActivity.class.getSimpleName();
  private static final String[] ZXING_URLS = { "http://zxing.appspot.com/scan", "zxing://scan/" };
  private AmbientLightManager ambientLightManager;
  private BeepManager beepManager;
  private CameraManager cameraManager;
  private String characterSet;
  private Collection<BarcodeFormat> decodeFormats;
  private Map<DecodeHintType, ?> decodeHints;
  private CaptureActivityHandler handler;
  private boolean hasFinish;
  private boolean hasSurface;
  private InactivityTimer inactivityTimer;
  private Result lastResult;
  private SurfaceHolder mSurfaceHolder;
  private View resultView;
  private Result savedResultToShow;
  private ScanFromWebPageManager scanFromWebPageManager;
  private IntentSource source;
  private String sourceUrl;
  private TextView statusView;
  private ViewfinderView viewfinderView;

  static
  {
    DISPLAYABLE_METADATA_TYPES = EnumSet.of(ResultMetadataType.ISSUE_NUMBER, ResultMetadataType.SUGGESTED_PRICE, ResultMetadataType.ERROR_CORRECTION_LEVEL, ResultMetadataType.POSSIBLE_COUNTRY);
  }

  private void clearScanFrame()
  {
    if (this.statusView != null)
      this.statusView.setVisibility(8);
    this.viewfinderView.setVisibility(8);
  }

  private void decodeOrStoreSavedBitmap(Bitmap paramBitmap, Result paramResult)
  {
    if (this.handler == null)
    {
      this.savedResultToShow = paramResult;
      return;
    }
    if (paramResult != null)
      this.savedResultToShow = paramResult;
    if (this.savedResultToShow != null)
    {
      paramBitmap = Message.obtain(this.handler, R.id.decode_succeeded, this.savedResultToShow);
      this.handler.sendMessage(paramBitmap);
    }
    this.savedResultToShow = null;
  }

  private void displayFrameworkBugMessageAndExit()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(getString(R.string.app_name));
    localBuilder.setMessage(getString(R.string.msg_camera_framework_bug));
    localBuilder.setPositiveButton(R.string.button_ok, new FinishListener(this));
    localBuilder.setOnCancelListener(new FinishListener(this));
    localBuilder.show();
  }

  private static void drawLine(Canvas paramCanvas, Paint paramPaint, ResultPoint paramResultPoint1, ResultPoint paramResultPoint2, float paramFloat)
  {
    if ((paramResultPoint1 != null) && (paramResultPoint2 != null))
      paramCanvas.drawLine(paramFloat * paramResultPoint1.getX(), paramFloat * paramResultPoint1.getY(), paramFloat * paramResultPoint2.getX(), paramFloat * paramResultPoint2.getY(), paramPaint);
  }

  private void drawResultPoints(Bitmap paramBitmap, float paramFloat, Result paramResult)
  {
    ResultPoint[] arrayOfResultPoint = paramResult.getResultPoints();
    Paint localPaint;
    if ((arrayOfResultPoint != null) && (arrayOfResultPoint.length > 0))
    {
      paramBitmap = new Canvas(paramBitmap);
      localPaint = new Paint();
      localPaint.setColor(getResources().getColor(R.color.result_points));
      if (arrayOfResultPoint.length != 2)
        break label80;
      localPaint.setStrokeWidth(4.0F);
      drawLine(paramBitmap, localPaint, arrayOfResultPoint[0], arrayOfResultPoint[1], paramFloat);
    }
    while (true)
    {
      return;
      label80: if ((arrayOfResultPoint.length == 4) && ((paramResult.getBarcodeFormat() == BarcodeFormat.UPC_A) || (paramResult.getBarcodeFormat() == BarcodeFormat.EAN_13)))
      {
        drawLine(paramBitmap, localPaint, arrayOfResultPoint[0], arrayOfResultPoint[1], paramFloat);
        drawLine(paramBitmap, localPaint, arrayOfResultPoint[2], arrayOfResultPoint[3], paramFloat);
        return;
      }
      localPaint.setStrokeWidth(10.0F);
      int j = arrayOfResultPoint.length;
      int i = 0;
      while (i < j)
      {
        paramResult = arrayOfResultPoint[i];
        if (paramResult != null)
          paramBitmap.drawPoint(paramResult.getX() * paramFloat, paramResult.getY() * paramFloat, localPaint);
        i += 1;
      }
    }
  }

  private void initCamera(SurfaceHolder paramSurfaceHolder)
  {
    if (paramSurfaceHolder == null)
      throw new IllegalStateException("No SurfaceHolder provided");
    if (this.cameraManager.isOpen())
    {
      Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
      return;
    }
    try
    {
      this.cameraManager.openDriver(paramSurfaceHolder);
      if (this.handler == null)
        this.handler = new CaptureActivityHandler(this, this.decodeFormats, this.decodeHints, this.characterSet, this.cameraManager);
      decodeOrStoreSavedBitmap(null, null);
      return;
    }
    catch (java.io.IOException paramSurfaceHolder)
    {
      Log.w(TAG, paramSurfaceHolder);
      displayFrameworkBugMessageAndExit();
      return;
    }
    catch (java.lang.RuntimeException paramSurfaceHolder)
    {
      Log.w(TAG, "Unexpected error initializing camera", paramSurfaceHolder);
      displayFrameworkBugMessageAndExit();
    }
  }

  private static boolean isZXingURL(String paramString)
  {
    if (paramString == null);
    while (true)
    {
      return false;
      String[] arrayOfString = ZXING_URLS;
      int j = arrayOfString.length;
      int i = 0;
      while (i < j)
      {
        if (paramString.startsWith(arrayOfString[i]))
          return true;
        i += 1;
      }
    }
  }

  private void prepareInitCamera()
  {
    if (this.mSurfaceHolder != null)
    {
      PermissionCheckHelper.instance();
      if (PermissionCheckHelper.isPermissionGranted(this, "android.permission.CAMERA"))
        initCamera(this.mSurfaceHolder);
    }
    else
    {
      return;
    }
    PermissionCheckHelper localPermissionCheckHelper = PermissionCheckHelper.instance();
    String str = getResources().getString(R.string.rationale_camera);
    1 local1 = new PermissionCheckHelper.PermissionCallbackListener()
    {
      public void onPermissionCheckCallback(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt)
      {
        if ((paramInt == 111) && (paramArrayOfInt.length > 0) && (paramArrayOfInt[0] == 0))
        {
          if (CaptureActivity.this.mSurfaceHolder != null)
            CaptureActivity.this.initCamera(CaptureActivity.this.mSurfaceHolder);
          return;
        }
        CaptureActivity.this.finish();
      }
    };
    localPermissionCheckHelper.requestPermissions(this, 111, new String[] { "android.permission.CAMERA" }, new String[] { str }, local1);
  }

  private void resetStatusView()
  {
    if (this.resultView != null)
      this.resultView.setVisibility(8);
    if (this.statusView != null)
    {
      this.statusView.setText(R.string.msg_default_status);
      this.statusView.setVisibility(0);
    }
    this.viewfinderView.setVisibility(0);
    this.lastResult = null;
  }

  private void sendReplyMessage(int paramInt, Object paramObject, long paramLong)
  {
    if (this.handler != null)
    {
      paramObject = Message.obtain(this.handler, paramInt, paramObject);
      if (paramLong > 0L)
        this.handler.sendMessageDelayed(paramObject, paramLong);
    }
    else
    {
      return;
    }
    this.handler.sendMessage(paramObject);
  }

  public void drawViewfinder()
  {
    this.viewfinderView.drawViewfinder();
  }

  CameraManager getCameraManager()
  {
    return this.cameraManager;
  }

  public Handler getHandler()
  {
    return this.handler;
  }

  ViewfinderView getViewfinderView()
  {
    return this.viewfinderView;
  }

  public void handleDecode(Result paramResult, Bitmap paramBitmap, float paramFloat)
  {
    this.inactivityTimer.onActivity();
    this.lastResult = paramResult;
    ResultHandler localResultHandler = ResultHandlerFactory.makeResultHandler(this, paramResult);
    int i;
    if (paramBitmap != null)
    {
      i = 1;
      if (i != 0)
      {
        this.beepManager.playBeepSoundAndVibrate();
        drawResultPoints(paramBitmap, paramFloat, paramResult);
      }
      switch (2.$SwitchMap$com$google$zxing$client$android$IntentSource[this.source.ordinal()])
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      }
    }
    while (true)
    {
      clearScanFrame();
      return;
      i = 0;
      break;
      handleDecodeExternally(paramResult, localResultHandler, paramBitmap);
      continue;
      if ((this.scanFromWebPageManager == null) || (!this.scanFromWebPageManager.isScanFromWebPage()))
      {
        handleDecodeInternally(paramResult, localResultHandler, paramBitmap);
        continue;
      }
      handleDecodeExternally(paramResult, localResultHandler, paramBitmap);
      continue;
      SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      if ((i != 0) && (localSharedPreferences.getBoolean("preferences_bulk_mode", false)))
      {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_bulk_mode_scanned) + " (" + paramResult.getText() + ')', 0).show();
        restartPreviewAfterDelay(1000L);
        continue;
      }
      handleDecodeInternally(paramResult, localResultHandler, paramBitmap);
    }
  }

  public void handleDecodeExternally(Result paramResult, ResultHandler paramResultHandler, Bitmap paramBitmap)
  {
    if (paramBitmap != null)
      this.viewfinderView.drawResultBitmap(paramBitmap);
    long l;
    if (getIntent() == null)
      l = 1500L;
    int i;
    while (true)
    {
      if (l > 0L)
      {
        String str = String.valueOf(paramResult);
        paramBitmap = str;
        if (str.length() > 32)
          paramBitmap = str.substring(0, 32) + " ...";
        if (this.statusView != null)
          this.statusView.setText(getString(paramResultHandler.getDisplayTitle()) + " : " + paramBitmap);
      }
      if (this.source != IntentSource.NATIVE_APP_INTENT)
        break;
      paramResultHandler = new Intent(getIntent().getAction());
      paramResultHandler.addFlags(524288);
      paramResultHandler.putExtra("SCAN_RESULT", paramResult.toString());
      paramResultHandler.putExtra("SCAN_RESULT_FORMAT", paramResult.getBarcodeFormat().toString());
      paramBitmap = paramResult.getRawBytes();
      if ((paramBitmap != null) && (paramBitmap.length > 0))
        paramResultHandler.putExtra("SCAN_RESULT_BYTES", paramBitmap);
      paramResult = paramResult.getResultMetadata();
      if (paramResult != null)
      {
        if (paramResult.containsKey(ResultMetadataType.UPC_EAN_EXTENSION))
          paramResultHandler.putExtra("SCAN_RESULT_UPC_EAN_EXTENSION", paramResult.get(ResultMetadataType.UPC_EAN_EXTENSION).toString());
        paramBitmap = (Number)paramResult.get(ResultMetadataType.ORIENTATION);
        if (paramBitmap != null)
          paramResultHandler.putExtra("SCAN_RESULT_ORIENTATION", paramBitmap.intValue());
        paramBitmap = (String)paramResult.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
        if (paramBitmap != null)
          paramResultHandler.putExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL", paramBitmap);
        paramResult = (Iterable)paramResult.get(ResultMetadataType.BYTE_SEGMENTS);
        if (paramResult != null)
        {
          i = 0;
          paramResult = paramResult.iterator();
          while (true)
            if (paramResult.hasNext())
            {
              paramBitmap = (byte[])paramResult.next();
              paramResultHandler.putExtra("SCAN_RESULT_BYTE_SEGMENTS_" + i, paramBitmap);
              i += 1;
              continue;
              l = getIntent().getLongExtra("RESULT_DISPLAY_DURATION_MS", 1500L);
              break;
            }
        }
      }
      sendReplyMessage(R.id.return_scan_result, paramResultHandler, l);
    }
    do
    {
      return;
      if (this.source != IntentSource.PRODUCT_SEARCH_LINK)
        continue;
      i = this.sourceUrl.lastIndexOf("/scan");
      paramResult = this.sourceUrl.substring(0, i) + "?q=" + paramResultHandler.getDisplayContents() + "&source=zxing";
      sendReplyMessage(R.id.launch_product_query, paramResult, l);
      return;
    }
    while ((this.source != IntentSource.ZXING_LINK) || (this.scanFromWebPageManager == null) || (!this.scanFromWebPageManager.isScanFromWebPage()));
    paramResult = this.scanFromWebPageManager.buildReplyURL(paramResult, paramResultHandler);
    this.scanFromWebPageManager = null;
    sendReplyMessage(R.id.launch_product_query, paramResult, l);
  }

  public void handleDecodeInternally(Result paramResult, ResultHandler paramResultHandler, Bitmap paramBitmap)
  {
    paramBitmap = ResultParser.parseResult(paramResult);
    if (paramBitmap.getType().equals(ParsedResultType.URI))
      paramResult = (URIParsedResult)paramBitmap;
    do
    {
      try
      {
        paramResult = Uri.parse(paramResult.getURI());
        Log.i(TAG, "scan uri: " + paramResult);
        if (paramResult != null)
        {
          paramResult = Uri.parse("dianping://parseqrurl?qrurl=" + URLEncoder.encode(paramResult.toString(), "utf-8"));
          Log.i(TAG, "startActivity with url " + paramResult);
          startActivityForResult(new Intent("android.intent.action.VIEW", paramResult), 1);
        }
        return;
      }
      catch (Exception paramResult)
      {
        paramResult.printStackTrace();
        return;
      }
      paramBitmap = paramResultHandler.getDisplayContents();
      Object localObject1 = PreferenceManager.getDefaultSharedPreferences(this);
      if ((paramResultHandler.getDefaultButtonID() != null) && (((SharedPreferences)localObject1).getBoolean("preferences_auto_open_web", false)))
      {
        paramResultHandler.handleButtonPress(paramResultHandler.getDefaultButtonID().intValue());
        return;
      }
      clearScanFrame();
      if (this.resultView != null)
        this.resultView.setVisibility(0);
      ((TextView)findViewById(R.id.format_text_view)).setText(paramResult.getBarcodeFormat().toString());
      ((TextView)findViewById(R.id.type_text_view)).setText(paramResultHandler.getType().toString());
      localObject1 = DateFormat.getDateTimeInstance(3, 3);
      ((TextView)findViewById(R.id.time_text_view)).setText(((DateFormat)localObject1).format(new Date(paramResult.getTimestamp())));
      localObject1 = (TextView)findViewById(R.id.meta_text_view);
      View localView = findViewById(R.id.meta_text_view_label);
      ((TextView)localObject1).setVisibility(8);
      localView.setVisibility(8);
      Object localObject2 = paramResult.getResultMetadata();
      if (localObject2 != null)
      {
        paramResult = new StringBuilder(20);
        localObject2 = ((Map)localObject2).entrySet().iterator();
        while (((Iterator)localObject2).hasNext())
        {
          Map.Entry localEntry = (Map.Entry)((Iterator)localObject2).next();
          if (!DISPLAYABLE_METADATA_TYPES.contains(localEntry.getKey()))
            continue;
          paramResult.append(localEntry.getValue()).append('\n');
        }
        if (paramResult.length() > 0)
        {
          paramResult.setLength(paramResult.length() - 1);
          ((TextView)localObject1).setText(paramResult);
          ((TextView)localObject1).setVisibility(0);
          localView.setVisibility(0);
        }
      }
      paramResult = (TextView)findViewById(R.id.contents_text_view);
      paramResult.setText(paramBitmap);
      paramResult.setTextSize(2, Math.max(22, 32 - paramBitmap.length() / 4));
      paramResult = (TextView)findViewById(R.id.contents_supplement_text_view);
      paramResult.setText("");
      paramResult.setOnClickListener(null);
    }
    while (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("preferences_supplemental", true));
    SupplementalInfoRetriever.maybeInvokeRetrieval(paramResult, paramResultHandler.getResult(), this);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (paramInt2 == -1))
    {
      this.hasFinish = true;
      finish();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().addFlags(128);
    setContentView(R.layout.capture);
    this.hasSurface = false;
    this.inactivityTimer = new InactivityTimer(this);
    this.beepManager = new BeepManager(this);
    this.ambientLightManager = new AmbientLightManager(this);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    if (getResources().getConfiguration().orientation == 1)
    {
      setRequestedOrientation(1);
      return;
    }
    setRequestedOrientation(0);
  }

  protected void onDestroy()
  {
    this.inactivityTimer.shutdown();
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = true;
    switch (paramInt)
    {
    default:
    case 27:
    case 80:
    case 4:
      do
      {
        bool = super.onKeyDown(paramInt, paramKeyEvent);
        return bool;
        if (this.source != IntentSource.NATIVE_APP_INTENT)
          continue;
        setResult(0);
        finish();
        return true;
      }
      while (((this.source != IntentSource.NONE) && (this.source != IntentSource.ZXING_LINK)) || (this.lastResult == null));
      restartPreviewAfterDelay(0L);
      return true;
    case 25:
      this.cameraManager.setTorch(false);
      return true;
    case 24:
    }
    this.cameraManager.setTorch(true);
    return true;
  }

  protected void onPause()
  {
    if (this.handler != null)
    {
      this.handler.quitSynchronously();
      this.handler = null;
    }
    this.inactivityTimer.onPause();
    this.ambientLightManager.stop();
    this.beepManager.close();
    this.cameraManager.closeDriver();
    if (!this.hasSurface)
      ((SurfaceView)findViewById(R.id.preview_view)).getHolder().removeCallback(this);
    clearScanFrame();
    super.onPause();
  }

  protected void onResume()
  {
    super.onResume();
    this.cameraManager = new CameraManager(getApplication());
    this.viewfinderView = ((ViewfinderView)findViewById(R.id.viewfinder_view));
    this.viewfinderView.setCameraManager(this.cameraManager);
    this.resultView = findViewById(R.id.result_view);
    this.statusView = ((TextView)findViewById(R.id.status_view));
    this.handler = null;
    this.lastResult = null;
    resetStatusView();
    this.beepManager.updatePrefs();
    this.ambientLightManager.start(this.cameraManager);
    this.inactivityTimer.onResume();
    Object localObject1 = getIntent();
    this.source = IntentSource.NONE;
    this.sourceUrl = null;
    this.scanFromWebPageManager = null;
    this.decodeFormats = null;
    this.characterSet = null;
    Object localObject2;
    String str;
    if (localObject1 != null)
    {
      localObject2 = ((Intent)localObject1).getAction();
      str = ((Intent)localObject1).getDataString();
      if (!"com.google.zxing.client.android.SCAN".equals(localObject2))
        break label351;
      this.source = IntentSource.NATIVE_APP_INTENT;
      this.decodeFormats = DecodeFormatManager.parseDecodeFormats((Intent)localObject1);
      this.decodeHints = DecodeHintManager.parseDecodeHints((Intent)localObject1);
      int i;
      if ((((Intent)localObject1).hasExtra("SCAN_WIDTH")) && (((Intent)localObject1).hasExtra("SCAN_HEIGHT")))
      {
        i = ((Intent)localObject1).getIntExtra("SCAN_WIDTH", 0);
        int j = ((Intent)localObject1).getIntExtra("SCAN_HEIGHT", 0);
        if ((i > 0) && (j > 0))
          this.cameraManager.setManualFramingRect(i, j);
      }
      if (((Intent)localObject1).hasExtra("SCAN_CAMERA_ID"))
      {
        i = ((Intent)localObject1).getIntExtra("SCAN_CAMERA_ID", -1);
        if (i >= 0)
          this.cameraManager.setManualCameraId(i);
      }
      localObject2 = ((Intent)localObject1).getStringExtra("PROMPT_MESSAGE");
      if ((localObject2 != null) && (this.statusView != null))
        this.statusView.setText((CharSequence)localObject2);
    }
    while (true)
    {
      this.characterSet = ((Intent)localObject1).getStringExtra("CHARACTER_SET");
      localObject1 = ((SurfaceView)findViewById(R.id.preview_view)).getHolder();
      if (!this.hasSurface)
        break;
      this.mSurfaceHolder = ((SurfaceHolder)localObject1);
      prepareInitCamera();
      return;
      label351: if ((str != null) && (str.contains("http://www.google")) && (str.contains("/m/products/scan")))
      {
        this.source = IntentSource.PRODUCT_SEARCH_LINK;
        this.sourceUrl = str;
        this.decodeFormats = DecodeFormatManager.PRODUCT_FORMATS;
        continue;
      }
      if (!isZXingURL(str))
        continue;
      this.source = IntentSource.ZXING_LINK;
      this.sourceUrl = str;
      localObject2 = Uri.parse(str);
      this.scanFromWebPageManager = new ScanFromWebPageManager((Uri)localObject2);
      this.decodeFormats = DecodeFormatManager.parseDecodeFormats((Uri)localObject2);
      this.decodeHints = DecodeHintManager.parseDecodeHints((Uri)localObject2);
    }
    ((SurfaceHolder)localObject1).addCallback(this);
  }

  public void restartPreviewAfterDelay(long paramLong)
  {
    if (this.handler != null)
      this.handler.sendEmptyMessageDelayed(R.id.restart_preview, paramLong);
    resetStatusView();
  }

  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    if (paramSurfaceHolder == null)
      Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
    if (!this.hasSurface)
    {
      this.hasSurface = true;
      if (!this.hasFinish)
      {
        this.mSurfaceHolder = paramSurfaceHolder;
        prepareInitCamera();
      }
    }
  }

  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    this.hasSurface = false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.CaptureActivity
 * JD-Core Version:    0.6.0
 */