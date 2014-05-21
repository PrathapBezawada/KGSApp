package com.demo.imageapp.imagezoom;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.krushivignan.R;
import com.example.krushivignan.SoilMapActivity;
import com.nunc.krushivignan.core.AppInfo;

public class ImageViewTouch extends ImageViewTouchBase {

	private static final float SCROLL_DELTA_THRESHOLD = 1.0f;
	static final float MIN_ZOOM = 0.9f;
	protected ScaleGestureDetector mScaleDetector;
	protected GestureDetector mGestureDetector;
	protected int mTouchSlop;
	protected float mCurrentScaleFactor;
	protected float mScaleFactor;
	protected int mDoubleTapDirection;
	protected OnGestureListener mGestureListener;
	protected OnScaleGestureListener mScaleListener;
	protected boolean mDoubleTapToZoomEnabled = true;
	protected boolean mScaleEnabled = true;
	protected boolean mScrollEnabled = true;

	private OnImageViewTouchDoubleTapListener doubleTapListener;

	private static final int SWIPE_MIN_DISTANCE = 60;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;

	private Context context;

	private SharedPreferences prefBookmarkData;

	private Animation inLeftAnimation;
	private Animation outRightAnimation;
	private Animation outLeftAnimation;
	private Animation inRightAnimation;

	public ImageViewTouch(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		prefBookmarkData = context.getSharedPreferences("BOOKMARK_DATA",
				Context.MODE_PRIVATE);

		initAnimations();

	}

	private void initAnimations() {

		// set up animations
		inLeftAnimation = (Animation) AnimationUtils.loadAnimation(context,
				R.anim.flip_in_left);
		outRightAnimation = (Animation) AnimationUtils.loadAnimation(context,
				R.anim.flip_out_right);
		inRightAnimation = (Animation) AnimationUtils.loadAnimation(context,
				R.anim.flip_in_right);
		outLeftAnimation = (Animation) AnimationUtils.loadAnimation(context,
				R.anim.flip_out_left);
	}

	@Override
	protected void init() {
		super.init();
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		mGestureListener = getGestureListener();
		mScaleListener = getScaleListener();

		mScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
		mGestureDetector = new GestureDetector(getContext(), mGestureListener,
				null, true);

		mCurrentScaleFactor = 1f;
		mDoubleTapDirection = 1;
	}

	public void setDoubleTapListener(
			OnImageViewTouchDoubleTapListener doubleTapListener) {
		this.doubleTapListener = doubleTapListener;
	}

	public void setDoubleTapToZoomEnabled(boolean value) {
		mDoubleTapToZoomEnabled = value;
	}

	public void setScaleEnabled(boolean value) {
		mScaleEnabled = value;
	}

	public void setScrollEnabled(boolean value) {
		mScrollEnabled = value;
	}

	public boolean getDoubleTapEnabled() {
		return mDoubleTapToZoomEnabled;
	}

	protected OnGestureListener getGestureListener() {
		return new GestureListener();
	}

	protected OnScaleGestureListener getScaleListener() {
		return new ScaleListener();
	}

	@Override
	protected void onBitmapChanged(Drawable drawable) {
		super.onBitmapChanged(drawable);

		float v[] = new float[9];
		mSuppMatrix.getValues(v);
		mCurrentScaleFactor = v[Matrix.MSCALE_X];
	}

	@Override
	protected void _setImageDrawable(final Drawable drawable,
			final boolean reset, final Matrix initial_matrix,
			final float maxZoom) {
		super._setImageDrawable(drawable, reset, initial_matrix, maxZoom);
		mScaleFactor = getMaxZoom() / 3;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mScaleDetector.onTouchEvent(event);
		if (!mScaleDetector.isInProgress())
			mGestureDetector.onTouchEvent(event);
		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			if (getScale() < 1f) {
				zoomTo(1f, 50);
			}
			break;
		}
		return true;
	}

	@Override
	protected void onZoom(float scale) {
		super.onZoom(scale);
		if (!mScaleDetector.isInProgress())
			mCurrentScaleFactor = scale;
	}

	protected float onDoubleTapPost(float scale, float maxZoom) {
		if (mDoubleTapDirection == 1) {
			if ((scale + (mScaleFactor * 2)) <= maxZoom) {
				return scale + mScaleFactor;
			} else {
				System.out.println("mDoubleTapDirection :: "
						+ mDoubleTapDirection);
				mDoubleTapDirection = -1;
				return maxZoom;
			}
		} else {
			System.out.println("mDoubleTapDirection11111 :: "
					+ mDoubleTapDirection);
			mDoubleTapDirection = 1;
			return 1f;
		}
	}

	/**
	 * Determines whether this ImageViewTouch can be scrolled.
	 * 
	 * @param direction
	 *            - positive direction value means scroll from right to left,
	 *            negative value means scroll from left to right
	 * 
	 * @return true if there is some more place to scroll, false - otherwise.
	 */
	public boolean canScroll(int direction) {

		RectF bitmapRect = getBitmapRect();
		updateRect(bitmapRect, mScrollRect);
		Rect imageViewRect = new Rect();
		getGlobalVisibleRect(imageViewRect);

		if (bitmapRect.right >= imageViewRect.right) {
			if (direction < 0) {
				return Math.abs(bitmapRect.right - imageViewRect.right) > SCROLL_DELTA_THRESHOLD;
			}
		}

		double bitmapScrollRectDelta = Math.abs(bitmapRect.left
				- mScrollRect.left);
		return bitmapScrollRectDelta > SCROLL_DELTA_THRESHOLD;
	}

	public class GestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			Log.i(LOG_TAG, "onDoubleTap. double tap enabled? "
					+ mDoubleTapToZoomEnabled);
			if (mDoubleTapToZoomEnabled) {
				float scale = getScale();
				float targetScale = scale;
				targetScale = onDoubleTapPost(scale, getMaxZoom());
				targetScale = Math.min(getMaxZoom(),
						Math.max(targetScale, MIN_ZOOM));
				mCurrentScaleFactor = targetScale;
				zoomTo(targetScale, e.getX(), e.getY(), 200);
				invalidate();
			}
			if (null != doubleTapListener) {
				doubleTapListener.onDoubleTap();
			}

			return super.onDoubleTap(e);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			if (isLongClickable()) {
				if (!mScaleDetector.isInProgress()) {
					setPressed(true);
					performLongClick();
				}
			}
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (!mScrollEnabled)
				return false;

			if (e1 == null || e2 == null)
				return false;
			if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1)
				return false;
			if (mScaleDetector.isInProgress())
				return false;
			if (getScale() == 1f)
				return false;
			scrollBy(-distanceX, -distanceY);
			invalidate();
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			if (mCurrentScaleFactor <= 1.0) {
				try {
					if (e1.getX() > e2.getX()
							&& Math.abs(e1.getX() - e2.getX()) > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

						System.out.println("ApplicationInfo.position ::"
								+ AppInfo.position);
						System.out
								.println("ApplicationInfo.photoDetailsList.size() :: "
										+ AppInfo.photoDetailsList
												.size());

						if (AppInfo.position + 1 < AppInfo.photoDetailsList
								.size()) {

							AppInfo.position = AppInfo.position + 1;
							
							Bitmap bitmap = BitmapFactory
									.decodeFile(AppInfo.photoDetailsList
											.get(AppInfo.position));

							ImageViewTouch _newImageView = new ImageViewTouch(
									context, null);
							_newImageView.setImageBitmap(AppInfo.getResizedBitmap(bitmap));
							
							System.gc();

							SoilMapActivity.imageContainerLayout
									.setOutAnimation(outLeftAnimation);
							SoilMapActivity.imageContainerLayout
									.setInAnimation(inRightAnimation);

							SoilMapActivity.imageContainerLayout
									.addView(_newImageView);
							SoilMapActivity.imageContainerLayout
									.showNext();

							if (AppInfo.oldImageView != null) {
								SoilMapActivity.imageContainerLayout
										.removeView(AppInfo.oldImageView);
							}
							AppInfo.oldImageView = _newImageView;

						}

					} else if (e1.getX() < e2.getX()
							&& e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

						System.out.println("ApplicationInfo.position :: "
								+ AppInfo.position);

						if (AppInfo.position > 0) {

							AppInfo.position = AppInfo.position - 1;

							Bitmap bitmap = BitmapFactory
									.decodeFile(AppInfo.photoDetailsList
											.get(AppInfo.position));
							ImageViewTouch _newImageView = new ImageViewTouch(
									context, null);
							
							_newImageView.setImageBitmap(AppInfo.getResizedBitmap(bitmap));
							System.gc();

							SoilMapActivity.imageContainerLayout
									.setOutAnimation(outRightAnimation);
							SoilMapActivity.imageContainerLayout
									.setInAnimation(inLeftAnimation);

							SoilMapActivity.imageContainerLayout
									.addView(_newImageView);
							SoilMapActivity.imageContainerLayout
									.showPrevious();

							if (AppInfo.oldImageView != null)
								SoilMapActivity.imageContainerLayout
										.removeView(AppInfo.oldImageView);
							AppInfo.oldImageView = _newImageView;

						}

					}
				} catch (Exception e) {

					e.printStackTrace();

				}
			}

			return true;

		}
	}

	public class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {

		@SuppressWarnings("unused")
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float span = detector.getCurrentSpan() - detector.getPreviousSpan();
			float targetScale = mCurrentScaleFactor * detector.getScaleFactor();
			if (mScaleEnabled) {
				targetScale = Math.min(getMaxZoom(),
						Math.max(targetScale, MIN_ZOOM));
				zoomTo(targetScale, detector.getFocusX(), detector.getFocusY());
				mCurrentScaleFactor = Math.min(getMaxZoom(),
						Math.max(targetScale, MIN_ZOOM));
				mDoubleTapDirection = 1;
				invalidate();
				return true;
			}
			return false;
		}
	}


	public interface OnImageViewTouchDoubleTapListener {
		void onDoubleTap();
	}
}
