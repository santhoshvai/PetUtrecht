package com.example.svaiyapu.petutrecht.Util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.svaiyapu.petutrecht.R;

@TargetApi(21)
public final class GUIUtils {

	public interface OnRevealAnimationListener {
		void onRevealHide();
		void onRevealShow();
	}

    /**
     *
     * @param ctx Context to retrieve the color and animation duration
     * @param containerView rootView that we must show after circular reveal is done - the start view
     * @param fabView FAB view - the end view
     * @param listener custom listener to communicate between animator listener and activity
     */
	public static void animateRevealHide(final Context ctx,
                                         final View containerView,
                                         final View fabView,
                                         final OnRevealAnimationListener listener) {

        Pair<Integer, Integer> centerCircle = getCircleCenter(containerView);
        final int cx = centerCircle.first;
        final int cy = centerCircle.second;
        final float startRadius = getCircleRadius(containerView.getWidth(), containerView.getHeight());
        final float finalRadius = fabView.getWidth()/2;

        final int color = ctx.getResources().getColor(R.color.colorAccent);

		Animator anim =
			ViewAnimationUtils.createCircularReveal(containerView, cx, cy, startRadius, finalRadius);
		anim.setDuration(getAnimDuration(ctx));

		anim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				containerView.setBackgroundColor(color);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				listener.onRevealHide();
				containerView.setVisibility(View.INVISIBLE);
			}
		});
		anim.start();
	}

    /**
     *
     * @param ctx Context to retrieve the color and animation duration
     * @param containerView rootView that we must show after circular reveal is done - the endview
     * @param fabView FAB view - the start view
     * @param listener custom listener to communicate between animator listener and activity
     */
	public static void animateRevealShow(final Context ctx,
                                         final View containerView,
                                         final View fabView,
                                         final OnRevealAnimationListener listener) {

        float startRadius = fabView.getWidth()/2;
        float finalRadius = getCircleRadius(containerView.getWidth(), containerView.getHeight());

        Pair<Integer, Integer> centerCircle = getCircleCenter(containerView);
        final int cx = centerCircle.first;
        final int cy = centerCircle.second;
        final int color = ctx.getResources().getColor(R.color.colorAccent);

		Animator anim =
			ViewAnimationUtils.createCircularReveal(containerView, cx, cy, startRadius, finalRadius);
		anim.setDuration(getAnimDuration(ctx));
		anim.setStartDelay(50);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		anim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				containerView.setBackgroundColor(color);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				containerView.setVisibility(View.VISIBLE);
				listener.onRevealShow();
			}
		});
		anim.start();
	}

    /**
     *
     * @param ctx context to retrieve animation duration from android resources
     * @return animation duration
     */
	private static final int getAnimDuration(Context ctx) {
		return (ctx.getResources().getInteger(android.R.integer.config_shortAnimTime)) / 2;
	}

    /**
     * Get the radius of the circle that encloses a rectangle,
     * Algorithm uses pythagoras theorem, take the diagonal length and divide by half
     * @param rectWidth width of the rectangle which the circle encloses
     * @param rectHeight height of the rectangle which the circle encloses
     * @return radius of the circle
     */
    private static float getCircleRadius(final int rectWidth, final int rectHeight) {
        return (float) ((Math.hypot(rectWidth, rectHeight)/2));
    }

    private static Pair<Integer, Integer> getCircleCenter(View view) {
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;
        return Pair.create(cx, cy);
    }
}
