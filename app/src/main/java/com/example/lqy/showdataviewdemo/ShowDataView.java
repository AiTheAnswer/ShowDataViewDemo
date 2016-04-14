package com.example.lqy.showdataviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lqy on 16/1/29.
 */
public class ShowDataView extends View {

    public static final float RIGHT_ANGLE = 90f * 3.1415f / 180f;

    private final List<Float> mMileList = new ArrayList<Float>();
    private final List<Float> mHeartList = new ArrayList<Float>();
    private final List<Float> mSpeedList = new ArrayList<Float>();
    private final List<Integer> mTimeList = new ArrayList<Integer>();
    private final List<String> mDateList = new ArrayList<String>();

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

    public ShowDataView(Context context) {
        this(context, null);
    }

    public ShowDataView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mLinePath = new Path();
    }

    // set or update data must use this
    public void setDataLists(ArrayList<Float> mile,ArrayList<Float> heart,ArrayList<Float> speed,ArrayList<Integer> time,ArrayList<String> date){
        mMileList.addAll(mile);
        mHeartList.addAll(heart);
        mSpeedList.addAll(speed);
        mTimeList.addAll(time);
        mDateList.addAll(date);

        for(int i=0; i< 2 ;++i){
            mMileList.add(0, 0f);
            mHeartList.add(0,0f);
            mSpeedList.add(0,0f);
            mTimeList.add(0, 0);
            mDateList.add(0, "");
        }

        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long time = System.currentTimeMillis();
        super.onDraw(canvas);

        mPaint.setTextSize(20f);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeWidth(3f);

        //draw background
        mPaint.setColor(mBackColor);
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
        mPaint.setColor(mHeightBackColor);
        canvas.drawRect(0, mAreaHeight, mWidth, mAreaHeight + mSpaceHeight, mPaint);
        canvas.drawRect(0, mAreaHeight * 2 + mSpaceHeight, mWidth, mHeight, mPaint);

        if (mMileList == null || mMileList.size() == 0) {
            // TODO: 16/2/18 no data
        } else {
            int listSize = mMileList.size();
            if (mMileList.size() < 13) {
                mLess12 = true;
                mSpaceLength = (mWidth - mDataLength * listSize) / (listSize + 1);
                mLength = 0;
                mLeftPosition = 0;
            } else {
                mLess12 = false;
                listSize = 12;
                mMaxLeftPosition = mMileList.size() - listSize;
            }
            mPaint.setColor(mBarBackColor);
            for (int i = 0; i < listSize; ++i) {
                canvas.drawRect(mLength + mSpaceLength + i * (mSpaceLength + mDataLength), 0, mLength + (i + 1) * (mSpaceLength + mDataLength), mAreaHeight, mPaint);
                canvas.drawRect(mLength + mSpaceLength + i * (mSpaceLength + mDataLength), mAreaHeight + mSpaceHeight, mLength + (i + 1) * (mSpaceLength + mDataLength), mAreaHeight * 2 + mSpaceHeight, mPaint);
            }

            //draw line
            mPaint.setColor(mSpeedColor);
            float maxSpeed = 0;
            float x = 0;
            float y = 0;
            float xx = 0;
            float yy = 0;
            float minx = 0;
            float miny = 0;
            float angle = 0;
            int size = mSpeedList.size();
            for (int i = 0; i < size; ++i) {
                if (maxSpeed < mSpeedList.get(i)) {
                    maxSpeed = mSpeedList.get(i);
                }
            }

            for (int i = 0; i < listSize; ++i) {
                xx = mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2;
                yy = mAreaHeight / 6 + (1 - mSpeedList.get(mLeftPosition + i) / maxSpeed) * mAreaHeight / 6 * 5;
                canvas.drawCircle(xx, yy, 8, mPaint);
                if (x != 0) {
//                    canvas.drawLine(x, y, xx, yy, mPaint);
                }
                x = xx;
                y = yy;
                canvas.drawText(mDf.format(mSpeedList.get(mLeftPosition + i)), xx, yy - mAreaHeight / 48 * 3, mPaint);
                if (i == listSize - 1) {
                    canvas.drawText("速度(m/s)", xx + mDataLength, yy + mAreaHeight / 24, mPaint);
                }
            }

            mPaint.setStyle(Paint.Style.STROKE);
            mLinePath.reset();
            for (int i = 0; i < listSize; ++i) {
                xx = mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2;
                yy = mAreaHeight / 6 + (1 - mSpeedList.get(mLeftPosition + i) / maxSpeed) * mAreaHeight / 6 * 5;
                if (mUseConner) {
                    if (i == 0) {
                        mLinePath.moveTo(xx, yy);
                    } else {
                        minx = x > xx ? xx : x;
                        miny = y > yy ? yy : y;
                        angle = (float) Math.atan(x / y);
                        if (yy > y) {
                            x = (int) (Math.abs(x - xx) / 2);
                            y = (int) (Math.abs(y - yy) / 2);
                            if (x < y) {
                                mLinePath.quadTo(minx + x, miny + y - y * angle / RIGHT_ANGLE + 10, minx + x, miny + y);
                                mLinePath.quadTo(minx + x, miny + y + y * angle / RIGHT_ANGLE + 10, xx, yy);
                            } else {
                                mLinePath.quadTo(minx + x - x * angle / RIGHT_ANGLE + 10, miny + y, minx + x, miny + y);
                                mLinePath.quadTo(minx + x + x * angle / RIGHT_ANGLE + 10, miny + y, xx, yy);
                            }
                        } else {
                            x = (int) (Math.abs(x - xx) / 2);
                            y = (int) (Math.abs(y - yy) / 2);
                            if (x < y) {
                                mLinePath.quadTo(minx + x, miny + y + y * angle / RIGHT_ANGLE + 10, minx + x, miny + y);
                                mLinePath.quadTo(minx + x, miny + y - y * angle / RIGHT_ANGLE + 10, xx, yy);
                            } else {
                                mLinePath.quadTo(minx + x - x * angle / RIGHT_ANGLE + 10, miny + y, minx + x, miny + y);
                                mLinePath.quadTo(minx + x + x * angle / RIGHT_ANGLE + 10, miny + y, xx, yy);
                            }
                        }
                    }
                } else {
                    if (i == 0) {
                        mLinePath.moveTo(xx, yy);
                    } else {
                        mLinePath.lineTo(xx, yy);
                    }
                }
                x = xx;
                y = yy;
            }
            canvas.drawPath(mLinePath, mPaint);
            mPaint.setStyle(Paint.Style.FILL);


            x = 0;
            y = 0;
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
                canvas.drawCircle(xx, yy, 8, mPaint);
                if (x != 0) {
                    canvas.drawLine(x, y, xx, yy, mPaint);
                }
                x = xx;
                y = yy;
                canvas.drawText(mHeartList.get(mLeftPosition + i) + "", xx, yy - mAreaHeight / 48 * 3, mPaint);
                if (i == listSize - 1) {
                    canvas.drawText("心率(t/min)", xx + mDataLength, yy + mAreaHeight / 24, mPaint);
                }
            }

            mPaint.setColor(mLineCircleColor);
            for (int i = 0; i < listSize; ++i) {
                canvas.drawCircle(mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight / 6 + (1 - mHeartList.get(mLeftPosition + i) / maxHeart) * mAreaHeight / 6 * 5, 5, mPaint);
                canvas.drawCircle(mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight / 6 + (1 - mSpeedList.get(mLeftPosition + i) / maxSpeed) * mAreaHeight / 6 * 5, 5, mPaint);
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
                canvas.drawRect(mLength + mSpaceLength + i * (mSpaceLength + mDataLength), mAreaHeight / 6 * 7 + mSpaceHeight + (1 - mMileList.get(mLeftPosition + i) / maxMile) * mAreaHeight / 6 * 5, mLength + (i + 1) * (mSpaceLength + mDataLength), mAreaHeight * 2 + mSpaceHeight, mPaint);
            }
            mPaint.setColor(mMileTextColor);
            for (int i = 0; i < listSize; ++i) {
                canvas.drawText(mDf.format(mMileList.get(mLeftPosition + i)) + " km", mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight / 6 * 7 + mSpaceHeight + (1 - mMileList.get(mLeftPosition + i) / maxMile) * mAreaHeight / 6 * 5 - mAreaHeight / 24, mPaint);
            }
            //draw text
            mPaint.setColor(mTimeColor);
            for (int i = 0; i < listSize; ++i) {
                canvas.drawText(mTimeList.get(mLeftPosition + i) + " min", mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight * 2 + mSpaceHeight + mTextHeight * 0.4f, mPaint);
            }
            mPaint.setColor(mDateColor);
            for (int i = 0; i < listSize; ++i) {
                canvas.drawText(mDateList.get(mLeftPosition + i), mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight * 2 + mSpaceHeight + mTextHeight * 0.8f, mPaint);
            }

        }
        Log.i("LQY", "time ----> " + (System.currentTimeMillis() - time));
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
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        scrollTo((int) mScreenPosition, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

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
                        this.postInvalidate();
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
                                    mLength = -(mSpaceLength + mDataLength) * 3;
                                }
                                if (mLeftPosition < mMaxLeftPosition) {
                                    ++mLeftPosition;
                                }
                            }
                        } else {
                            mLength -= distance;
                            if (mLength < -(mSpaceLength + mDataLength) * 2) {
                                mLength = -(mSpaceLength + mDataLength) * 2;
                            }
                        }
                        this.postInvalidate();
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
}
