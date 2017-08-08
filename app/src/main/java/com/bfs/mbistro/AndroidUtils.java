package com.bfs.mbistro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import javax.security.auth.x500.X500Principal;

public class AndroidUtils {

  public static final int INVALID_RESOURCE_ID = 0;
  private static final X500Principal DEBUG_DN =
      new X500Principal("CN=Android Debug,O=Android,C=US");
  private static final String DRAWABLE_DEF_TYPE = "drawable";

  public static float dipToPixels(Context context, float dipValue) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
  }

  /**
   * Checks if there is at least one activity to handle provided intent
   *
   * @param context the context
   * @param intent intent with data for which handling activity is searched
   * @return true if there is activity to handle provided intent, false otherwise
   */
  static boolean hasActivityToStartIntent(@NonNull Context context, @Nullable Intent intent) {
    if (intent != null) {
      PackageManager manager = context.getPackageManager();
      List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
      return !infos.isEmpty();
    }
    return false;
  }

  public static void setTextOrHideIfEmpty(CharSequence text, TextView view) {
    if (JavaUtils.isNotNulNorEmptyString(text)) {
      view.setText(text);
      setVisibilityIfDifferent(view, View.VISIBLE);
    } else {
      setVisibilityIfDifferent(view, View.GONE);
    }
  }

  public static void setVisibilityIfDifferent(View view, int visibility) {
    if (view != null && view.getVisibility() != visibility) {
      view.setVisibility(visibility);
    }
  }

  public static float getFloatFromResources(Resources resources, int floatResId) {
    TypedValue floatValue = new TypedValue();
    resources.getValue(floatResId, floatValue, true);
    return floatValue.getFloat();
  }

  public static int getAttributeDimensionValue(Context context, int attr) {
    TypedValue typedValue = new TypedValue();
    context.getTheme().resolveAttribute(attr, typedValue, true);
    int[] attribute = { attr };
    TypedArray array = context.obtainStyledAttributes(typedValue.resourceId, attribute);
    int value = array.getDimensionPixelSize(0, -1);
    array.recycle();
    return value;
  }

  public static void setBackgroundCompatKeepPadding(View view, Drawable background) {
    int left = view.getPaddingLeft();
    int top = view.getPaddingTop();
    int right = view.getPaddingRight();
    int bottom = view.getPaddingBottom();
    setBackgroundCompat(view, background);
    view.setPadding(left, top, right, bottom);
  }

  @SuppressWarnings("deprecation")
  public static void setBackgroundCompat(View view, Drawable background) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      view.setBackground(background);
    } else {
      view.setBackgroundDrawable(background);
    }
  }

  public static boolean isSupportedAppInstalled(Context context, Intent intent) {
    List<ResolveInfo> supportedApps = context.getPackageManager().queryIntentActivities(intent, 0);
    return !supportedApps.isEmpty();
  }

  @SuppressWarnings("unchecked")
  public static <K, V> HashMap<K, V> getHashMapFromBundle(Bundle bundle, String key) {
    return (HashMap<K, V>) bundle.getSerializable(key);
  }

  public static void hideKeyboard(Activity activity) {
    if (activity != null) {
      InputMethodManager input =
          (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      View currentFocus = activity.getCurrentFocus();
      if (currentFocus != null && input != null && input.isActive()) {
        input.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
      }
    }
  }

  @SuppressWarnings("deprecation") public static Point getScreenSizeCompat(Activity activity) {
    Point result = new Point();
    Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

      defaultDisplay.getSize(result);
    } else {
      int width = defaultDisplay.getWidth();
      int height = defaultDisplay.getHeight();
      result.set(width, height);
    }
    return result;
  }

  public static void vibrate(Context context, long duration) {
    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    vibrator.vibrate(duration);
  }

  public static boolean containsIntentFlag(Intent intent, int flagToCheck) {
    return (intent.getFlags() & flagToCheck) != 0;
  }

  @SuppressWarnings("NumericCastThatLosesPrecision")
  public static Bitmap textAsBitmap(String text, Paint paint, double margin) {
    int width = (int) (paint.measureText(text) + margin);
    float baseline = (int) (-paint.ascent() + margin);
    int height = (int) (baseline + paint.descent() + margin);
    Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(image);
    canvas.drawText(text, 0, baseline, paint);
    return image;
  }

  /**
   * Returns the id of a drawable with a given name.
   * <br>
   * Example usage :  "ic_launcher" will return R.drawable.ic_launcher
   */
  public static int getDrawableId(Context context, String resIdName) {
    return getIdentifier(context, resIdName, DRAWABLE_DEF_TYPE);
  }

  private static int getIdentifier(Context context, String resIdName, String defType) {
    return JavaUtils.isNotNulNorEmptyString(resIdName) ? context.getResources()
        .getIdentifier(resIdName, defType, context.getPackageName()) : INVALID_RESOURCE_ID;
  }

  public static int getAppVersion(Context context) {
    try {
      PackageInfo packageInfo =
          context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return 0;
    }
  }

  @SuppressWarnings("deprecation") @SuppressLint("NewApi")
  public static void copyTextToClipboardCompat(Context context, CharSequence text,
      CharSequence label) {
    int currentApiVersion = Build.VERSION.SDK_INT;
    if (currentApiVersion >= Build.VERSION_CODES.HONEYCOMB) {
      ClipboardManager clipboard =
          (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
      ClipData clip = ClipData.newPlainText(label, text);
      clipboard.setPrimaryClip(clip);
    } else {
      ClipboardManager clipboard =
          (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
      clipboard.setText(text);
    }
  }

  public static Drawable tintDrawable(Context context, @DrawableRes int drawableResId,
      @ColorRes int colorResId) {

    Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
    int color = ContextCompat.getColor(context, colorResId);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      drawable.setTint(color);
      drawable.setTintMode(PorterDuff.Mode.SRC_IN);
    } else {
      drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
    return drawable;
  }

  public static Drawable tintDrawable(Context context, Drawable drawable,
      @ColorRes int colorResId) {
    int color = ContextCompat.getColor(context, colorResId);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      drawable.setTint(color);
      drawable.setTintMode(PorterDuff.Mode.SRC_IN);
    } else {
      drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
    return drawable;
  }

  public static Drawable tintDrawable(Drawable drawable, @ColorInt int color) {
    PorterDuff.Mode mode = PorterDuff.Mode.DST_ATOP;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      drawable.setTint(color);
      drawable.setTintMode(mode);
    } else {
      drawable.setColorFilter(color, mode);
    }
    return drawable;
  }

  public static Drawable getDrawableCompat(Context context, @DrawableRes int resId) {
    //noinspection IfMayBeConditional
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      return context.getResources().getDrawable(resId, context.getTheme());
    } else {
      return context.getResources().getDrawable(resId);
    }
  }

  public static boolean isFragmentAlive(Fragment fragment) {
    boolean isPresent = fragment.isAdded() && !fragment.isDetached() && fragment.getView() != null;
    return fragment.getActivity() != null && isPresent && !fragment.isRemoving();
  }

  public static boolean isServiceRunning(Context context, Class serviceClass) {
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
        Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Shows a {@link Snackbar}.
   *
   * @param mainTextStringId The id for the string resource for the Snackbar text.
   * @param actionStringId The text of the action item.
   * @param listener The listener associated with the Snackbar action.
   */
  public static void showSnackbar(Activity activity, final int mainTextStringId,
      final int actionStringId, View.OnClickListener listener) {
    Snackbar.make(activity.findViewById(android.R.id.content), activity.getString(mainTextStringId),
        Snackbar.LENGTH_INDEFINITE).setAction(activity.getString(actionStringId), listener).show();
  }

  /**
   * Shows a {@link Snackbar}.
   *
   * @param mainTextStringId The id for the string resource for the Snackbar text.
   */
  public static void showShortSnackbar(Activity activity, final int mainTextStringId) {
    Snackbar.make(activity.findViewById(android.R.id.content), activity.getString(mainTextStringId),
        Snackbar.LENGTH_SHORT).show();
  }

  /**
   * Carries out (lollipop-compliant) backwards compatible tinting that also includes wrapping the
   * drawable.
   *
   * @param drawableId the drawable resource id.
   * @param tintColor the color in which to tint.
   * @return the wrapped and tinted drawable.
   */
  public static Drawable getTintedVectorDrawable(@DrawableRes int drawableId,
      @ColorInt int tintColor, Context context) {
    Drawable drawable = null;
    if (drawableId > 0) {
      VectorDrawableCompat drawableCompat = getVectorDrawable(drawableId, context);
      if (tintColor == 0) {
        drawable = drawableCompat;
      } else {
        drawable = drawableCompat.mutate();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, tintColor);
      }
    }
    return drawable;
  }

  /**
   * Use this if you don't have a context handy or don't need to set a specific theme, the method
   * will resort to using the application context.
   *
   * @param drawableId the resource id of the drawable
   * @return the created {@link VectorDrawableCompat} instance.
   */
  public static VectorDrawableCompat getVectorDrawable(@DrawableRes int drawableId,
      Context context) {
    return VectorDrawableCompat.create(context.getResources(), drawableId, context.getTheme());
  }

  public static void dismissSnackbar(@Nullable Snackbar snackbar) {
    if (snackbar != null && snackbar.isShown()) {
      snackbar.dismiss();
    }
  }
}