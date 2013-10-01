package asia.ivity.android.marqueeview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Provides a simple marquee effect for a single {@link android.widget.TextView}.
 *
 * @author Sebastian Roth <sebastian.roth@gmail.com>
 */
public class MarqueeView extends LinearLayout {
    private TextView mTextField;

    private ScrollView mScrollView;

    private static final int TEXTVIEW_VIRTUAL_WIDTH = 2000;

    private Animation mMoveTextOut = null;
    private Animation mMoveTextIn = null;

    private Paint mPaint;

    private boolean mMarqueeNeeded = false;

    private static final String TAG = MarqueeView.class.getSimpleName();

    private float mTextDifference;

    /**
     * Control the speed. The lower this value, the faster it will scroll.
     */
    private static final int DEFAULT_SPEED = 60;

    /**
     * Control the pause between the animations. Also, after starting this activity.
     */
    private static final int DEFAULT_ANIMATION_PAUSE = 2000;

    private int mSpeed = DEFAULT_SPEED;

    private int mAnimationPause = DEFAULT_ANIMATION_PAUSE;

    private boolean mAutoStart = false;

    private Interpolator mInterpolator = new LinearInterpolator();

    private boolean mCancelled = false;
    private int mWidth;
    private Runnable mAnimationStartRunnable;

    /**
     * Sets the animation speed.
     * The lower the value, the faster the animation will be displayed.
     *
     * @param speed Milliseconds per PX.
     */
    public void setSpeed(int speed) {
        this.mSpeed = speed;
    }

    /**
     * Sets the pause between animations
     *
     * @param pause In milliseconds.
     */
    public void setPauseBetweenAnimations(int pause) {
        this.mAnimationPause = pause;
    }

    /**
     * Sets a custom interpolator for the animation.
     *
     * @param interpolator Animation interpolator.
     */
    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public MarqueeView(Context context) {
        super(context);
        init(context);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        extractAttributes(attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MarqueeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        extractAttributes(attrs);
        init(context);
    }

    private void extractAttributes(AttributeSet attrs) {
        if (getContext() == null) {
            return;
        }

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.asia_ivity_android_marqueeview_MarqueeView);

        if (a == null) {
            return;
        }

        mSpeed = a.getInteger(R.styleable.asia_ivity_android_marqueeview_MarqueeView_speed, DEFAULT_SPEED);
        mAnimationPause = a.getInteger(R.styleable.asia_ivity_android_marqueeview_MarqueeView_pause, DEFAULT_ANIMATION_PAUSE);
        mAutoStart = a.getBoolean(R.styleable.asia_ivity_android_marqueeview_MarqueeView_autoStart, false);

        a.recycle();
    }

    private void init(Context context) {
        // init helper
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (getChildCount() == 0 || getChildCount() > 1) {
            throw new RuntimeException("MarqueeView must have exactly one child element.");
        }

        if (changed) {
            View v = getChildAt(0);
            if (!(v instanceof TextView)) {
                throw new RuntimeException("The child view of this MarqueeView must be a TextView instance.");
            }

            initView(getContext());
        }

        mWidth = getMeasuredWidth();

        // Calculate
        prepare();

        // Setup
        setupTextMarquee();

        if (changed && mAutoStart) {
            startMarquee();
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);

        mTextField.setOnClickListener(l);
    }

    /**
     * Starts the configured marquee effect.
     */
    public void startMarquee() {
        prepareTextFields();

        // Start
        if (mMarqueeNeeded) {
            startTextFieldAnimation();
        }

        mCancelled = false;
    }

    private void startTextFieldAnimation() {
        mAnimationStartRunnable = new Runnable() {
            public void run() {
                mTextField.startAnimation(mMoveTextOut);
            }
        };
        postDelayed(mAnimationStartRunnable, mAnimationPause);
    }

    /**
     * Disables the animations.
     */
    public void reset() {
        Log.d(TAG, "Resetting animation.");

        mCancelled = true;

        if (mAnimationStartRunnable != null) {
            removeCallbacks(mAnimationStartRunnable);
        }

        mTextField.clearAnimation();

        prepareTextFields();

        mMoveTextOut.reset();
        mMoveTextIn.reset();

        mScrollView.removeView(mTextField);
        mScrollView.addView(mTextField);

        mTextField.setEllipsize(TextUtils.TruncateAt.END);

        invalidate();
    }

    private void prepareTextFields() {
        mTextField.setEllipsize(TextUtils.TruncateAt.END);
        cutTextView(mTextField);
    }

    private void setupTextMarquee() {
        final int duration = (int) (mTextDifference * mSpeed);

        mMoveTextOut = new TranslateAnimation(0, -mTextDifference, 0, 0);
        mMoveTextOut.setDuration(duration);
        mInterpolator = new LinearInterpolator();
        mMoveTextOut.setInterpolator(mInterpolator);
        mMoveTextOut.setFillAfter(true);

        mMoveTextIn = new TranslateAnimation(-mTextDifference, 0, 0, 0);
        mMoveTextIn.setDuration(duration);
        mMoveTextIn.setStartOffset(mAnimationPause);
        mMoveTextIn.setInterpolator(mInterpolator);
        mMoveTextIn.setFillAfter(true);

        mMoveTextOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                expandTextView(mTextField);
            }

            public void onAnimationEnd(Animation animation) {
                if (mCancelled) {
                    return;
                }

                mTextField.startAnimation(mMoveTextIn);
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });

        mMoveTextIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {

                cutTextView(mTextField);

                if (mCancelled) {
                    return;
                }
                startTextFieldAnimation();
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void prepare() {
        // Remember current state
        final float diff1 = mTextDifference;

        // Measure
        mPaint.setTextSize(mTextField.getTextSize());
        mPaint.setTypeface(mTextField.getTypeface());
        float mTextTextWidth = mPaint.measureText(mTextField.getText().toString());

        // See how much functions are needed at all
        mMarqueeNeeded = mTextTextWidth > mWidth;

        mTextDifference = Math.abs((mTextTextWidth - mWidth)) + 5;

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "mTextTextWidth: " + mTextTextWidth);
            Log.d(TAG, "getMeasuredWidth: " + mWidth);

            Log.d(TAG, "mMarqueeNeeded: " + mMarqueeNeeded);

            Log.d(TAG, "mTextDifference: " + mTextDifference);
        }

        if (diff1 != mTextDifference) {
            setupTextMarquee();
        }
    }

    private void initView(Context context) {
        // Scroll View
        LayoutParams sv1lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        sv1lp.gravity = Gravity.CENTER_HORIZONTAL;
        mScrollView = new ScrollView(context);

        // Scroll View 1 - Text Field
        mTextField = (TextView) getChildAt(0);
        removeView(mTextField);
        Log.d(TAG, "Count: " + getChildCount());
//                new TextView(context);
//        mTextField.setSingleLine(true);
//        mTextField.setTextColor(Color.WHITE);
//        mTextField.setEllipsize(TextUtils.TruncateAt.END);
//        mTextField.setTypeface(null, Typeface.BOLD);
        mScrollView.addView(mTextField, new ScrollView.LayoutParams(TEXTVIEW_VIRTUAL_WIDTH, LayoutParams.WRAP_CONTENT));

        addView(mScrollView, sv1lp);
    }

    private void expandTextView(TextView textView) {
        ViewGroup.LayoutParams lp = textView.getLayoutParams();
        lp.width = 2000;
        textView.setLayoutParams(lp);
    }

    private void cutTextView(TextView textView) {
        if (textView.getWidth() != mWidth) {
            ViewGroup.LayoutParams lp = textView.getLayoutParams();
            lp.width = mWidth;
            textView.setLayoutParams(lp);
        }
    }
}