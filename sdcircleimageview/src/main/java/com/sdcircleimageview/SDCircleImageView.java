package com.sdcircleimageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class SDCircleImageView extends ImageView {
    private Bitmap.Config bitmapConfig;
    private static final String TAG = SDCircleImageView.class.getSimpleName();
    private static final ImageView.ScaleType SCALE_TYPE = ImageView.ScaleType.CENTER_CROP;

    private Rect mRect = new Rect();
    private Rect mBorderRect = new Rect();

    private Paint paint = new Paint();
    private Paint borderPaint = new Paint();
    private Paint shadowPaint = new Paint();

    private Context context;
    private float innerCircleRadius;

    private int mPadding = 0;
    private int borderColor;
    private float borderWidth;
    private int innerColor;
    private int textColor;
    private int shadowColor;
    private float shadowSize;
    private float totalSideBorder;
    private Bitmap innerBitmapImage;

    private boolean showBorder;
    private AttributeSet attrs;

    private String textForeground;
    private int textSize;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    public SDCircleImageView(Context context) {
        super(context);
        init(context, null);
    }

    public SDCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SDCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        super.setScaleType(SCALE_TYPE);
        this.context = context;
        this.attrs = attrs;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SDCircleImageView);
        String borderColorStr = typedArray.getString(R.styleable.SDCircleImageView_borderColor);
        borderColor = (borderColorStr == null) ? Color.parseColor("#00574B") : Color.parseColor(borderColorStr);
        String innerColorStr = typedArray.getString(R.styleable.SDCircleImageView_innerColor);
        innerColor = (innerColorStr == null) ? Color.parseColor("#3AD1CC") : Color.parseColor(innerColorStr);

        String textColorStr = typedArray.getString(R.styleable.SDCircleImageView_textColor);
        textColor = (textColorStr == null) ? Color.parseColor("#3AD1CC") : Color.parseColor(textColorStr);

        String shadowColorStr = typedArray.getString(R.styleable.SDCircleImageView_shadowColor);
        shadowColor = (shadowColorStr == null) ? Color.parseColor("#000000") : Color.parseColor(shadowColorStr);

        int innerBitmapResId = typedArray.getResourceId(R.styleable.SDCircleImageView_innerBitmap, -1);
        if (innerBitmapResId > 0) {
            innerBitmapImage = BitmapFactory.decodeResource(context.getResources(), innerBitmapResId);
        }

        textForeground = typedArray.getString(R.styleable.SDCircleImageView_textForeground);
        borderWidth = typedArray.getDimension(R.styleable.SDCircleImageView_borderWidth, 2);
        shadowSize = typedArray.getDimension(R.styleable.SDCircleImageView_shadowSize, 0);
        showBorder = typedArray.getBoolean(R.styleable.SDCircleImageView_showBorder, false);

        if (!showBorder) {
            borderWidth = 0;
        }

        totalSideBorder = borderWidth + shadowSize;

        Log.i(TAG, "ShadowColor =" + shadowColor + ", ShadowSize =" + shadowSize);
        typedArray.recycle();

        setup();
    }

    private void setup() {
        setScaleType(ScaleType.CENTER_CROP);

        paint.setColor(innerColor);
        paint.setStyle(Paint.Style.FILL);
        mRect.left = mPadding + (int) totalSideBorder;
        mRect.right = getWidth() - mPadding - (int) totalSideBorder;
        mRect.top = mPadding + (int) totalSideBorder;
        mRect.bottom = getHeight() - mPadding - (int) totalSideBorder;

        innerCircleRadius = Math.min((mRect.height() / 2.0f), (mRect.width() / 2.0f));

        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setAntiAlias(true);
        borderPaint.setDither(true);

        shadowPaint.setColor(shadowColor);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setStrokeWidth(shadowSize);
        shadowPaint.setAntiAlias(true);
        shadowPaint.setDither(true);

        mBorderRect.left = mPadding;
        mBorderRect.right = getWidth() - mPadding;
        mBorderRect.top = mPadding;
        mBorderRect.bottom = getHeight() - mPadding;

        invalidate();
    }

    public Bitmap getBitmapClippedCircle(Bitmap bitmap, int width, int height) {
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_4444;
        }
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, bitmapConfig);

        final Path path = new Path();
        path.addCircle(
                (float) (width / 2)
                , (float) (height / 2)
                , (float) Math.min(width / 2, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);

        int dimen = Math.min(width, height);
        if (!bitmap.isRecycled()) {
            bitmap = Bitmap.createScaledBitmap(bitmap, dimen, dimen, true);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }

        bitmap.recycle();
        return outputBitmap;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(innerColor);

        if (shadowSize > 0) {
            float mShadowRadius = Math.min(((mBorderRect.height()) / 2.0f), ((mBorderRect.height()) / 2.0f));
            canvas.drawCircle(mBorderRect.centerX() + shadowSize, mBorderRect.centerY() + shadowSize, mShadowRadius, shadowPaint);
        }

        if (textForeground != null && textForeground.length() > 0) {
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), innerCircleRadius, paint);
            paint.setColor(textColor);

            if (textSize == 0) {
                textSize = determineMaxTextSize(textForeground, mRect.width() + borderWidth);
            }

            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);  // centers horizontally

            canvas.drawText(
                    textForeground,
                    canvas.getWidth() / 2,
                    ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)),
                    paint
            );

        } else if (innerBitmapImage != null) {
            canvas.drawBitmap(getBitmapClippedCircle(innerBitmapImage, mRect.width(), mRect.height()), totalSideBorder, totalSideBorder, paint);
        } else {
            //paint.setColor(innerColor);
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), innerCircleRadius, paint);
        }

        if (showBorder) {
            float mBorderRadius = Math.min(((mBorderRect.height() - totalSideBorder) / 2.0f), ((mBorderRect.height() - totalSideBorder) / 2.0f));
            canvas.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(), mBorderRadius, borderPaint);
        }


    }

    private int determineMaxTextSize(String str, float maxWidth) {
        int size = 0;
        Paint paint = new Paint();

        do {
            paint.setTextSize(++size);
        } while (paint.measureText(str) < maxWidth);

        return size;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

    public void setInnerColor(int innerColor) {
        this.innerColor = innerColor;
        invalidate();
    }

    public void setInnerBitmapImage(Bitmap innerBitmapImage) {
        this.innerBitmapImage = innerBitmapImage;
        invalidate();
    }

    public void showBorder(boolean show) {
        this.showBorder = show;
        invalidate();
    }

    public void setBitmapConfig(Bitmap.Config bitmapConfig) {
        this.bitmapConfig = bitmapConfig;
    }

    public String getTextForeground() {
        return textForeground;
    }

    public void setTextForeground(String textForeground) {
        this.textForeground = textForeground;
    }
}
