package com.damosdesigns.sendkisses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by damosdesigns on 7/2/16.
 */
public class Util {

    //SharedPreferencesUtils

    private static SharedPreferences getSharedPrefHandle(Context context) {
        return context.getSharedPreferences(
                context.getString(R.string.shared_pref), Context.MODE_PRIVATE);
    }

    public static void writeToSharedPreferences(Context context, int prefKeyID, boolean value) {
        SharedPreferences.Editor editor = getSharedPrefHandle(context).edit();
        editor.putBoolean(context.getString(prefKeyID), value);
        editor.commit();
    }

    public static void writeToSharedPreferences(Context context, int prefKeyID, int value) {
        SharedPreferences.Editor editor = getSharedPrefHandle(context).edit();
        editor.putInt(context.getString(prefKeyID), value);
        editor.commit();
    }

    public static void writeToSharedPreferences(Context context, int prefKeyID, String value) {
        SharedPreferences.Editor editor = getSharedPrefHandle(context).edit();
        editor.putString(context.getString(prefKeyID), value);
        editor.commit();
    }

    public static boolean readSharedPref(Context context, int prefKeyID) {
        return getSharedPrefHandle(context).getBoolean(context.getString(prefKeyID), false);
    }

    public static int readSharedPref(Context context, int prefKeyID, int defaultValue) {
        return getSharedPrefHandle(context).getInt(context.getString(prefKeyID), defaultValue);
    }

    public static String readSharedPref(Context context, int prefKeyID, String defaultValue) {
        return getSharedPrefHandle(context).getString(context.getString(prefKeyID), defaultValue);
    }


    // AnimationUtils

    public static void textAnimation(final TextView textView, int to, final String suffix) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, to);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("" + (int) animation.getAnimatedValue() + suffix);
            }
        });
        animator.start();
    }

    public static void circularReveal(View toReveal) {
        // get the center for the clipping circle
        int cx = toReveal.getWidth() / 2;
        int cy = toReveal.getHeight() / 2;

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(toReveal, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        toReveal.setVisibility(View.VISIBLE);
        anim.start();
    }

    public static void circularHide(final View toHide) {
        // get the center for the clipping circle
        int cx = toHide.getWidth() / 2;
        int cy = toHide.getHeight() / 2;

        // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(cx, cy);

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(toHide, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                toHide.setVisibility(View.INVISIBLE);
            }
        });

        // start the animation
        anim.start();

    }

    public static int returnRandomMaterialColor(Context context) {
        int[] androidColors = context.getResources().getIntArray(R.array.random_material_colors);
        return androidColors[new Random().nextInt(androidColors.length)];
    }
}
