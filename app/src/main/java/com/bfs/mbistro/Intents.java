package com.bfs.mbistro;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import timber.log.Timber;

public class Intents {

  private static final String GOOGLE_PLAY_APP_DEEP_LINK = "market://details?id=%1$s";
  private static final String GOOGLE_PLAY_BROWSER_DEEP_LINK =
      "https://play.google.com/store/apps/details?id=%1$S";

  public static void makeEmail(Context context, String address, String subject, String content) {
    makeEmail(context, new String[] { address }, subject, content);
  }

  public static void makeEmail(Context context, String[] addresses, String subject,
      String content) {
    makeEmail(context, addresses, subject, content, null);
  }

  public static void makeEmail(Context context, String[] addresses, String subject, String content,
      ArrayList<Uri> attachments) {
    Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
    if (addresses != null) {
      for (int i = 0; i < addresses.length; i++) {
        if (addresses[i] == null) {
          addresses[i] = "";
        }
      }
      emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
    }
    if (!TextUtils.isEmpty(subject)) {
      emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
    }
    if (!TextUtils.isEmpty(content)) {
      emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content));
    }

    if (!JavaUtils.isNotNulNorEmpty(attachments)) {
      emailIntent.setType("*/*");
      emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
    } else {
      emailIntent.setType("message/rfc822");
    }

    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    context.startActivity(Intent.createChooser(emailIntent, "Select email application."));
  }

  public static boolean isSupportedByExternalApp(@NonNull Context context, @NonNull Intent intent) {
    List<ResolveInfo> acts = context.getPackageManager().queryIntentActivities(intent, 0);
    return !acts.isEmpty();
  }

  public static void openAppInGooglePlay(@NonNull Context context, @NonNull String packageName) {
    try {
      context.startActivity(new Intent(Intent.ACTION_VIEW,
          Uri.parse(String.format(GOOGLE_PLAY_APP_DEEP_LINK, packageName))));
    } catch (ActivityNotFoundException e) {
      Timber.e(e);
      context.startActivity(new Intent(Intent.ACTION_VIEW,
          Uri.parse(String.format(GOOGLE_PLAY_BROWSER_DEEP_LINK, packageName))));
    }
  }

  public static void makeWebsite(Context context, String url) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    context.startActivity(intent);
  }

  public static void makeMaps(Activity activity, BigDecimal lat, BigDecimal lng, String address) {
    if (lat != null && lng != null) {
      makeMaps(activity, lat.doubleValue(), lng.doubleValue(), address);
    }
  }

  private static void makeMaps(Activity activity, double lat, double lng, String address) {
    Uri browser = Uri.parse("http://maps.google.com/maps?q=" + address + "@" + lat + "," + lng);
    activity.startActivity(new Intent(Intent.ACTION_VIEW, browser));
  }

  public static void showNavigation(Context context, BigDecimal latitude, BigDecimal longitude,
      String label) {
    final String navigationBaseQuery = "google.navigation:q=";
    String address = null;
    if (latitude == null
        || longitude == null
        || latitude.signum() == 0 && longitude.signum() == 0) {
      if (!TextUtils.isEmpty(label)) {
        address = String.format("%1$s", label);
      }
    } else {
      /**
       * The Google Map app works only with English format for coordinates (ex: 39.214, 47.3214).
       * German format uses ',' instead of '.' that's why we should specify the locale here.
       */
      address = String.format(Locale.ENGLISH, "%1$f, %2$f", latitude.doubleValue(),
          longitude.doubleValue());
    }
    if (address != null) {
      Uri uri = Uri.parse(navigationBaseQuery + address);
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      if (AndroidUtils.hasActivityToStartIntent(context, intent)) {
        context.startActivity(intent);
      }
    }
  }
}