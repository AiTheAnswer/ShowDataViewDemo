package com.example.lqy.showdataviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * this is just a test
 */
public class ShowDataSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;

    private Canvas mCanvas;

    private boolean mIsDrawing;

    private boolean mDrawNow;

    private int mBegin = 0;

    /**
     * 重要参数，两点之间分为几段描画，数字愈大分段越多，描画的曲线就越精细.
     */
    private static final int STEPS = 12;

    private final List<Float> mMileList = new ArrayList<>();
    private final List<Float> mHeartList = new ArrayList<>();
    private final List<Float> mSpeedList = new ArrayList<>();
    private final List<Integer> mTimeList = new ArrayList<>();
    private final List<String> mDateList = new ArrayList<>();

    List<Cubic> mCubicsX = new LinkedList<>();
    List<Cubic> mCubicsY = new LinkedList<>();
    List<Integer> mPointsX = new LinkedList<>();
    List<Integer> mPointsY = new LinkedList<>();

    int mLineCircleColor = 0xFFFFFFFF;

    int mMileColor = 0xFF1EB0F4;

    int mMileTextColor = 0xFFFF7600;

    int mBackColor = 0xFFFFFFFF;

    int mHeightBackColor = 0xFFDCDFE0;

    int mBarBackColor = 0xFFEDF0F2;

    int mTimeColor = 0xFF777777;

    int mDateColor = 0xFF9E9E9E;

    int mHeartColor = 0xFFF1479C;

    int mSpeedColor = 0xFF15ADF3;

    float mWidth;

    float mScreenWidth;

    float mScreenPosition;

    float mHeight;

    float mDataLength;

    float mSpaceLength;

    float mSpaceHeight;

    float mTextHeight;

    float mAreaHeight;

    Paint mPaint;

    Path mLinePath;

    boolean mUseConner = true;

    int mLeftPosition = 0;

    int mMaxLeftPosition;

    float mX = 0;

    float mLength;

    boolean mLess12 = false;

    DecimalFormat mDf = new DecimalFormat("0.00");

    public ShowDataSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ShowDataSurface(Context context) {
        this(context, null);
    }

    // set or update data must use this
    public void setDataLists(ArrayList<Float> mile,ArrayList<Float> heart,ArrayList<Float> speed,ArrayList<Integer> time,ArrayList<String> date){
        mMileList.addAll(mile);
        mHeartList.addAll(heart);
        mSpeedList.addAll(speed);
        mTimeList.addAll(time);
        mDateList.addAll(date);

//        for(int i=0; i< 2 ;++i){
//            mMileList.add(0, 0f);
//            mHeartList.add(0,0f);
//            mSpeedList.add(0,0f);
//            mTimeList.add(0, 0);
//            mDateList.add(0, "");
//        }

//        postInvalidate();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        mDrawNow = true;
        new Thread(this).start();
//        scrollTo((int) mScreenPosition, 0);
        Log.i("LQY", "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while (mIsDrawing){
            if(mDrawNow) {
                if(mBegin != 5) {
                    mBegin++;
                } else {
                    mDrawNow = false;
                }
                Log.i("LQY","draw");
                draw();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mScreenWidth = MeasureSpec.getSize(widthMeasureSpec);

        mDataLength = mScreenWidth * 0.55f / 10;
        mSpaceLength = mScreenWidth * 0.45f / 10;
        mSpaceHeight = mHeight / 30;
        mTextHeight = mHeight / 9;
        mAreaHeight = (mHeight - mSpaceHeight - mTextHeight) / 2;
        mWidth = (mDataLength + mSpaceLength) * 12;
        mLength = 0;
        mScreenPosition = (mDataLength + mSpaceLength) * 2;
        setMeasuredDimension((int) mWidth, (int) mHeight);
        Log.i("LQY", "onMeasure");
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
//        scrollTo((int) mScreenPosition, 0);
//        mLeftPosition = 2;
        Log.i("LQY", "layout");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mDrawNow = true;

        float distance = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mX == 0) {
                    mX = event.getX();
                }
                if (mLess12) {
                    if (mX - event.getX() < 0) {
                        distance = (int) Math.abs(event.getX() - mX);
                        mScreenPosition -= distance;
                        if (mScreenPosition < 0) {
                            mScreenPosition = 0;
                        }
                        this.scrollTo((int) mScreenPosition, 0);
                    } else if (mX - event.getX() > 0) {
                        distance = (int) Math.abs(event.getX() - mX);
                        mScreenPosition += distance;
                        if (mScreenPosition > (mDataLength + mSpaceLength) * 2) {
                            mScreenPosition = (mDataLength + mSpaceLength) * 2;
                        }
                        this.scrollTo((int) mScreenPosition, 0);
                    }
                } else {
                    // move right
                    if (mX - event.getX() < 0) {
                        distance = (int) Math.abs(event.getX() - mX);
                        if (mLeftPosition != 0) {
                            mLength += distance;
                        }
                        if (mLength > mSpaceLength + mDataLength) {
                            if (mLeftPosition != 0) {
                                mLength = mSpaceLength + mDataLength - mLength;
                            } else {
                                mLength = mSpaceLength + mDataLength;
                            }
                            if (mLeftPosition > 0) {
                                --mLeftPosition;
                            }
                        }
//                        this.postInvalidate();
                    }
                    // move left
                    else if (mX - event.getX() > 0) {
                        distance = (int) Math.abs(event.getX() - mX);
                        if (mLeftPosition != mMaxLeftPosition) {
                            mLength -= distance;

                            if (mLength < -mDataLength - mSpaceLength) {
                                if (mLeftPosition != mMaxLeftPosition) {
                                    mLength = -mDataLength - mSpaceLength - mLength;
                                } else {
//                                    mLength = -(mSpaceLength + mDataLength) * 3;
                                    mLength = -mDataLength - mSpaceLength;
                                }
                                if (mLeftPosition < mMaxLeftPosition) {
                                    ++mLeftPosition;
                                }
                            }
                        } else {
//                            mLength -= distance;
//                            if (mLength < -(mSpaceLength + mDataLength)) {
//                                mLength = -(mSpaceLength + mDataLength);
//                            }
                        }
//                        this.postInvalidate();
                    }
                }
                mX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                mX = 0;
                break;
            default:
                break;
        }
        return true;
    }

    private void drawCurve(Canvas canvas, List<Integer>points_x, List<Integer>points_y, Path curvePath, Paint paint) {

        List<Cubic> calculate_x = calculate(points_x, mCubicsX);
        List<Cubic> calculate_y = calculate(points_y, mCubicsY);
        curvePath
                .moveTo(calculate_x.get(0).eval(0), calculate_y.get(0).eval(0));

        for (int i = 0; i < calculate_x.size(); i++) {
            for (int j = 1; j <= STEPS; j++) {
                float u = j / (float) STEPS;
                curvePath.lineTo(calculate_x.get(i).eval(u), calculate_y.get(i)
                        .eval(u));
            }
        }
        canvas.drawPath(curvePath, paint);
    }

    private List<Cubic> calculate(List<Integer> x, List<Cubic> cubics) {
        cubics.clear();
        int n = x.size() - 1;
        float[] gamma = new float[n + 1];
        float[] delta = new float[n + 1];
        float[] D = new float[n + 1];
        int i;

		/*
		 * We solve the equation [2 1 ] [D[0]] [3(x[1] - x[0]) ] |1 4 1 | |D[1]|
		 * |3(x[2] - x[0]) | | 1 4 1 | | . | = | . | | ..... | | . | | . | | 1 4
		 * 1| | . | |3(x[n] - x[n-2])| [ 1 2] [D[n]] [3(x[n] - x[n-1])]
		 *
		 * by using row operations to convert the matrix to upper triangular and
		 * then back sustitution. The D[i] are the derivatives at the knots.
		 */

        gamma[0] = 1.0f / 2.0f;
        for (i = 1; i < n; i++) {
            gamma[i] = 1 / (4 - gamma[i - 1]);
        }
        gamma[n] = 1 / (2 - gamma[n - 1]);

        delta[0] = 3 * (x.get(1) - x.get(0)) * gamma[0];
        for (i = 1; i < n; i++) {
            delta[i] = (3 * (x.get(i + 1) - x.get(i - 1)) - delta[i - 1])
                    * gamma[i];
        }
        delta[n] = (3 * (x.get(n) - x.get(n - 1)) - delta[n - 1]) * gamma[n];

        D[n] = delta[n];
        for (i = n - 1; i >= 0; i--) {
            D[i] = delta[i] - gamma[i] * D[i + 1];
        }

        for (i = 0; i < n; i++) {
            Cubic c = new Cubic(x.get(i), D[i], 3 * (x.get(i + 1) - x.get(i))
                    - 2 * D[i] - D[i + 1], 2 * (x.get(i) - x.get(i + 1)) + D[i]
                    + D[i + 1]);
            cubics.add(c);
        }
        return cubics;
    }

    private void draw(){
        try{
            mCanvas = mHolder.lockCanvas();
            // TODO: 16/4/14
            long time = System.currentTimeMillis();

            mPaint.setTextSize(20f);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setStrokeWidth(3f);

            //draw background
            mPaint.setColor(mBackColor);
            mCanvas.drawRect(0, 0, mWidth, mHeight, mPaint);
            mPaint.setColor(mHeightBackColor);
            mCanvas.drawRect(0, mAreaHeight, mWidth, mAreaHeight + mSpaceHeight, mPaint);
            mCanvas.drawRect(0, mAreaHeight * 2 + mSpaceHeight, mWidth, mHeight, mPaint);

            if (mMileList == null || mMileList.size() == 0) {
                // TODO: 16/2/18 paint something when there's no data
            } else {
                int listSize = mMileList.size();
                if (mMileList.size() < 13) {
                    mLess12 = true;
                    mSpaceLength = (mWidth - mDataLength * listSize) / (listSize + 1);
                    mLength = 0;
                    mLeftPosition = 0;
                } else {
                    mLess12 = false;
                    listSize = 8;
                    mMaxLeftPosition = mMileList.size() - listSize;
                }
                mPaint.setColor(mBarBackColor);
                for (int i = 0; i < listSize; ++i) {
                    mCanvas.drawRect(mLength + mSpaceLength + i * (mSpaceLength + mDataLength), 0, mLength + (i + 1) * (mSpaceLength + mDataLength), mAreaHeight, mPaint);
                    mCanvas.drawRect(mLength + mSpaceLength + i * (mSpaceLength + mDataLength), mAreaHeight + mSpaceHeight, mLength + (i + 1) * (mSpaceLength + mDataLength), mAreaHeight * 2 + mSpaceHeight, mPaint);
                }

                //draw line
                mPaint.setColor(mSpeedColor);
                float maxSpeed = 0;
//            float x = 0;
//            float y = 0;
                float xx;
                float yy;
                int size = mSpeedList.size();
                for (int i = 0; i < size; ++i) {
                    if (maxSpeed < mSpeedList.get(i)) {
                        maxSpeed = mSpeedList.get(i);
                    }
                }

                for (int i = 0; i < listSize; ++i) {
                    xx = mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2;
                    yy = mAreaHeight / 6 + (1 - mSpeedList.get(mLeftPosition + i) / maxSpeed) * mAreaHeight / 6 * 5;
                    mCanvas.drawCircle(xx, yy, 8, mPaint);
                    mCanvas.drawText(mDf.format(mSpeedList.get(mLeftPosition + i)), xx, yy - mAreaHeight / 48 * 3, mPaint);
                    if (i == listSize - 1) {
                        mCanvas.drawText("速度(m/s)", xx + mDataLength, yy + mAreaHeight / 24, mPaint);
                    }
                }

                mPaint.setStyle(Paint.Style.STROKE);
                mLinePath.reset();
                mPointsX.clear();
                mPointsY.clear();
                if(mUseConner) {
                    for (int i = 0; i < listSize; ++i) {
                        xx = mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2;
                        yy = mAreaHeight / 6 + (1 - mSpeedList.get(mLeftPosition + i) / maxSpeed) * mAreaHeight / 6 * 5;
                        mPointsX.add((int) xx);
                        mPointsY.add((int) yy);
                    }
                    drawCurve(mCanvas, mPointsX, mPointsY, mLinePath, mPaint);
                } else {
                    for (int i = 0; i < listSize; ++i) {
                        xx = mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2;
                        yy = mAreaHeight / 6 + (1 - mSpeedList.get(mLeftPosition + i) / maxSpeed) * mAreaHeight / 6 * 5;
                        if (i == 0) {
                            mLinePath.moveTo(xx, yy);
                        } else {
                            mLinePath.lineTo(xx, yy);
                        }
                    }
                    mCanvas.drawPath(mLinePath, mPaint);
                }
                mPaint.setStyle(Paint.Style.FILL);

                mPaint.setColor(mHeartColor);
                float maxHeart = 0;
                size = mHeartList.size();
                for (int i = 0; i < size; ++i) {
                    if (maxHeart < mHeartList.get(i)) {
                        maxHeart = mHeartList.get(i);
                    }
                }
                for (int i = 0; i < listSize; ++i) {
                    xx = mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2;
                    yy = mAreaHeight / 6 + (1 - mHeartList.get(mLeftPosition + i) / maxHeart) * mAreaHeight / 6 * 5;
                    mCanvas.drawCircle(xx, yy, 8, mPaint);
                    mCanvas.drawText(mHeartList.get(mLeftPosition + i) + "", xx, yy - mAreaHeight / 48 * 3, mPaint);
                    if (i == listSize - 1) {
                        mCanvas.drawText("心率(t/min)", xx + mDataLength, yy + mAreaHeight / 24, mPaint);
                    }
                }

                mPaint.setStyle(Paint.Style.STROKE);
                mLinePath.reset();
                mPointsX.clear();
                mPointsY.clear();
                if(mUseConner) {
                    for (int i = 0; i < listSize; ++i) {
                        xx = mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2;
                        yy = mAreaHeight / 6 + (1 - mHeartList.get(mLeftPosition + i) / maxHeart) * mAreaHeight / 6 * 5;
                        mPointsX.add((int) xx);
                        mPointsY.add((int) yy);
                    }
                    drawCurve(mCanvas, mPointsX, mPointsY, mLinePath, mPaint);
                } else {
                    for (int i = 0; i < listSize; ++i) {
                        xx = mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2;
                        yy = mAreaHeight / 6 + (1 - mHeartList.get(mLeftPosition + i) / maxHeart) * mAreaHeight / 6 * 5;
                        if (i == 0) {
                            mLinePath.moveTo(xx, yy);
                        } else {
                            mLinePath.lineTo(xx, yy);
                        }
                    }
                    mCanvas.drawPath(mLinePath, mPaint);
                }
                mPaint.setStyle(Paint.Style.FILL);

                mPaint.setColor(mLineCircleColor);
                for (int i = 0; i < listSize; ++i) {
                    mCanvas.drawCircle(mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight / 6 + (1 - mHeartList.get(mLeftPosition + i) / maxHeart) * mAreaHeight / 6 * 5, 5, mPaint);
                    mCanvas.drawCircle(mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight / 6 + (1 - mSpeedList.get(mLeftPosition + i) / maxSpeed) * mAreaHeight / 6 * 5, 5, mPaint);
                }
                //draw bar
                mPaint.setColor(mMileColor);
                float maxMile = 0;
                size = mMileList.size();
                for (int i = 0; i < size; ++i) {
                    if (maxMile < mMileList.get(i)) {
                        maxMile = mMileList.get(i);
                    }
                }
                for (int i = 0; i < listSize; ++i) {
                    mCanvas.drawRect(mLength + mSpaceLength + i * (mSpaceLength + mDataLength), mAreaHeight / 6 * 7 + mSpaceHeight + (1 - mMileList.get(mLeftPosition + i) / maxMile) * mAreaHeight / 6 * 5, mLength + (i + 1) * (mSpaceLength + mDataLength), mAreaHeight * 2 + mSpaceHeight, mPaint);
                }
                mPaint.setColor(mMileTextColor);
                for (int i = 0; i < listSize; ++i) {
                    mCanvas.drawText(mDf.format(mMileList.get(mLeftPosition + i)) + " km", mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight / 6 * 7 + mSpaceHeight + (1 - mMileList.get(mLeftPosition + i) / maxMile) * mAreaHeight / 6 * 5 - mAreaHeight / 24, mPaint);
                }
                //draw text
                mPaint.setColor(mTimeColor);
                for (int i = 0; i < listSize; ++i) {
                    mCanvas.drawText(mTimeList.get(mLeftPosition + i) + " min", mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight * 2 + mSpaceHeight + mTextHeight * 0.4f, mPaint);
                }
                mPaint.setColor(mDateColor);
                for (int i = 0; i < listSize; ++i) {
                    mCanvas.drawText(mDateList.get(mLeftPosition + i), mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight * 2 + mSpaceHeight + mTextHeight * 0.8f, mPaint);
                }

            }
            Log.i("LQY", "time ----> " + (System.currentTimeMillis() - time));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void initView(){
        mHolder = getHolder();
        mHolder.addCallback(this);
        // ???
//        setFocusable(true);
//        setFocusableInTouchMode(true);
//        this.setKeepScreenOn(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mLinePath = new Path();
    }
}
