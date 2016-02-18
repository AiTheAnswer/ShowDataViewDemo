package com.example.lqy.showdataviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lqy on 16/1/29.
 */
public class ShowDataView extends View {
    
    final List<Float> mMileList = new ArrayList<Float>();
    final List<Float> mHeartList = new ArrayList<Float>();
    final List<Float> mSpeedList = new ArrayList<Float>();
    final List<Integer> mTimeList = new ArrayList<Integer>();
    final List<String> mDateList = new ArrayList<String>();
    
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
    
    int mLeftPosition = 0;
    
    int mMaxLeftPosition;
    
    float mX = 0;
    
    float mLength;
    
    boolean mLess12 = false;
    
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
        
        for (int i = 0; i < 20; ++i) {
            mMileList.add((float) (Math.random() * 200));
            mHeartList.add((float) (Math.random() * 150));
            mSpeedList.add((float) (Math.random() * 20));
            mTimeList.add(i);
            mDateList.add(i + "");
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
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
        int listSize = mMileList.size();
        
        if (mMileList.size() == 0) {
            // TODO: 16/2/18 no data
        } else {
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
                    canvas.drawLine(x, y, xx, yy, mPaint);
                }
                x = xx;
                y = yy;
                canvas.drawText(mSpeedList.get(mLeftPosition + i) + "", xx, yy - mAreaHeight / 48 * 3, mPaint);
            }
            
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
                canvas.drawText(mMileList.get(mLeftPosition + i) + " km", mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight / 6 * 7 + mSpaceHeight + (1 - mMileList.get(mLeftPosition + i) / maxMile) * mAreaHeight / 6 * 5 - mAreaHeight / 24, mPaint);
            }
            //draw text
            mPaint.setColor(mTimeColor);
            for (int i = 0; i < listSize; ++i) {
                canvas.drawText(mTimeList.get(mLeftPosition + i) + " min", mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight * 2 + mSpaceHeight + mTextHeight * 0.4f, mPaint);
            }
            mPaint.setColor(mDateColor);
            for (int i = 0; i < listSize; ++i) {
                canvas.drawText(mDateList.get(mLeftPosition + i) + "", mLength + mSpaceLength + i * (mSpaceLength + mDataLength) + mDataLength / 2, mAreaHeight * 2 + mSpaceHeight + mTextHeight * 0.8f, mPaint);
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
        mScreenPosition = mDataLength + mSpaceLength * 2;
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
                        if(mScreenPosition < 0){
                            mScreenPosition = 0;
                        }
                        this.scrollTo((int) mScreenPosition, 0);
                    } else if (mX - event.getX() > 0) {
                        distance = (int) Math.abs(event.getX() - mX);
                        mScreenPosition += distance;
                        if(mScreenPosition > mWidth - mScreenWidth){
                            mScreenPosition = mWidth - mScreenWidth;
                        }
                        this.scrollTo((int) mScreenPosition, 0);
                    }
                } else {
                    if (mX - event.getX() < 0) {
                        distance = (int) Math.abs(event.getX() - mX);
                        mLength += distance;
                        if (mLength > mSpaceLength) {
                            if(mLeftPosition != 0) {
                                mLength = mSpaceLength - mLength;
                            }else{
                                mLength = mSpaceLength;
                            }
                            if (mLeftPosition > 0) {
                                --mLeftPosition;
                            }
                        }
                        this.postInvalidate();
                    } else if (mX - event.getX() > 0) {
                        distance = (int) Math.abs(event.getX() - mX);
                        mLength -= distance;
                        if (mLength < -mDataLength) {
                            if(mLeftPosition != mMaxLeftPosition) {
                                mLength = -mDataLength - mLength;
                            }else{
                                mLength = -mDataLength;
                            }
                            if (mLeftPosition < mMaxLeftPosition) {
                                ++mLeftPosition;
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
