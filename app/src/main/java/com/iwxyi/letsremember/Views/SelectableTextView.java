package com.iwxyi.letsremember.Views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.Selection;
import android.text.StaticLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ActionMenuView;

/*
 * File Description
 *
 * @author MRXY001
 * @date   2019/3/24 11 54
 * @email  wxy19980615@gmail.com
 */
public class SelectableTextView extends android.support.v7.widget.AppCompatTextView {

    ActionMenuView mActionMenu;
    private boolean isLongPress;
    private boolean isLongPressTouchActionUp;
    private int mStartLine;
    private int mStartTextOffset;
    private boolean isVibrator = false; // 震动
    private boolean isTextJustify = false;
    private int mCurrentLine;
    private int mCurrentTextOffset;
    private float mTouchDownX;
    private float mTouchDownY;
    private float mTouchDownRawY;
    private boolean isForbiddenActionMenu = false;
    private long TRIGGER_LONGPRESS_TIME_THRESHOLD = 500;
    private float TRIGGER_LONGPRESS_DISTANCE_THRESHOLD = 10;
    private OnClickListener mOnClickListener = null;

    private int mTextHighlightColor = Color.YELLOW;
    private int mViewTextWidth;
    private boolean isActionSelectAll = false;

    public SelectableTextView(Context context) {
        super(context);

        init();
    }

    private void init() {
        Resources resources = getContext().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        mViewTextWidth = displayMetrics.widthPixels;
    }

    @Override
    protected boolean getDefaultEditable() {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Layout layout = getLayout();
        int currentLine; // 当前所在行

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d("SelectableTextView", "ACTION_DOWN");
                // 每次按下时，创建ActionMenu菜单，创建不成功，屏蔽长按事件
                if (null == mActionMenu) {
                    mActionMenu = createActionMenu();
                }
                mTouchDownX = event.getX();
                mTouchDownY = event.getY();
                mTouchDownRawY = event.getRawY();
                isLongPress = false;
                isVibrator = false;
                isLongPressTouchActionUp = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("SelectableTextView", "ACTION_MOVE");
                // 先判断是否禁用了ActionMenu功能，以及ActionMenu是否创建失败，
                // 二者只要满足了一个条件，退出长按事件
                if (!isForbiddenActionMenu || mActionMenu.getChildCount() == 0) {
                    // 手指移动过程中的字符偏移
                    currentLine = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int mWordOffset_move = layout.getOffsetForHorizontal(currentLine, (int) event.getX());
                    // 判断是否触发长按事件
                    if (event.getEventTime() - event.getDownTime() >= TRIGGER_LONGPRESS_TIME_THRESHOLD
                            && Math.abs(event.getX() - mTouchDownX) < TRIGGER_LONGPRESS_DISTANCE_THRESHOLD
                            && Math.abs(event.getY() - mTouchDownY) < TRIGGER_LONGPRESS_DISTANCE_THRESHOLD) {

                        Log.d("SelectableTextView", "ACTION_MOVE 长按");
                        isLongPress = true;
                        isLongPressTouchActionUp = false;
                        mStartLine = currentLine;
                        mStartTextOffset = mWordOffset_move;

                        // 每次触发长按时，震动提示一次
                        if (!isVibrator) {
                            //mVibrator.vibrate(30);
                            isVibrator = true;
                        }
                    }
                    if (isLongPress) {
                        if (!isTextJustify)
                            requestFocus();
                        mCurrentLine = currentLine;
                        mCurrentTextOffset = mWordOffset_move;
                        // 通知父布局不要拦截触摸事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                        // 选择字符
                        Selection.setSelection(getEditableText(), Math.min(mStartTextOffset, mWordOffset_move),
                                Math.max(mStartTextOffset, mWordOffset_move));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("SelectableTextView", "ACTION_UP");
                // 处理长按事件
                if (isLongPress) {
                    currentLine = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int mWordOffsetEnd = layout.getOffsetForHorizontal(currentLine, (int) event.getX());
                    // 至少选中一个字符
                    mCurrentLine = currentLine;
                    mCurrentTextOffset = mWordOffsetEnd;
                    int maxOffset = getEditableText().length() - 1;
                    if (mStartTextOffset > maxOffset)
                        mStartTextOffset = maxOffset;
                    if (mCurrentTextOffset > maxOffset)
                        mCurrentTextOffset = maxOffset;
                    if (mCurrentTextOffset == mStartTextOffset) {
                        if (mCurrentTextOffset == layout.getLineEnd(currentLine) - 1)
                            mStartTextOffset -= 1;
                        else
                            mCurrentTextOffset += 1;
                    }


                    Selection.setSelection(getEditableText(), Math.min(mStartTextOffset, mCurrentTextOffset),
                            Math.max(mStartTextOffset, mCurrentTextOffset));
                    // 计算菜单显示位置
                    int mPopWindowOffsetY = calculatorActionMenuYPosition((int) mTouchDownRawY, (int) event.getRawY());
                    // 弹出菜单
                    showActionMenu(mPopWindowOffsetY, mActionMenu);
                    isLongPressTouchActionUp = true;
                    isLongPress = false;

                } else if (event.getEventTime() - event.getDownTime() < TRIGGER_LONGPRESS_TIME_THRESHOLD) {
                    // 由于onTouchEvent最终返回了true,onClick事件会被屏蔽掉，因此在这里处理onClick事件
                    if (null != mOnClickListener)
                        mOnClickListener.onClick(this);
                }
                // 通知父布局继续拦截触摸事件
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    /**
     * 绘制选中的文字的背景
     *
     * @param canvas
     */
    private void drawSelectedTextBackground(Canvas canvas) {
        if (mStartTextOffset == mCurrentTextOffset)
            return;

        // 文字背景高亮画笔
        Paint highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint.setStyle(Paint.Style.FILL);
        highlightPaint.setColor(mTextHighlightColor);
        highlightPaint.setAlpha(60);

        // 计算开始位置和结束位置的字符相对view最左侧的x偏移
        float startToLeftPosition = calculatorCharPositionToLeft(mStartLine, mStartTextOffset);
        float currentToLeftPosition = calculatorCharPositionToLeft(mCurrentLine, mCurrentTextOffset);

        // 行高
        int h = getLineHeight();
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();

        // 创建三个矩形，分别对应：
        // 所有选中的行对应的矩形，起始行左侧未选中文字的对应的矩形，结束行右侧未选中的文字对应的矩形
        RectF rect_all, rect_lt, rect_rb;
        // sdk版本控制
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mStartTextOffset < mCurrentTextOffset) {
                rect_all = new RectF(paddingLeft, mStartLine * h + paddingTop,
                        mViewTextWidth + paddingLeft, (mCurrentLine + 1) * h + paddingTop);
                rect_lt = new RectF(paddingLeft, mStartLine * h + paddingTop,
                        startToLeftPosition, (mStartLine + 1) * h + paddingTop);
                rect_rb = new RectF(currentToLeftPosition, mCurrentLine * h + paddingTop,
                        mViewTextWidth + paddingLeft, (mCurrentLine + 1) * h + paddingTop);
            } else {
                rect_all = new RectF(paddingLeft, mCurrentLine * h + paddingTop,
                        mViewTextWidth + paddingLeft, (mStartLine + 1) * h + paddingTop);
                rect_lt = new RectF(paddingLeft, mCurrentLine * h + paddingTop,
                        currentToLeftPosition, (mCurrentLine + 1) * h + paddingTop);
                rect_rb = new RectF(startToLeftPosition, mStartLine * h + paddingTop,
                        mViewTextWidth + paddingLeft, (mStartLine + 1) * h + paddingTop);
            }

            // 创建三个路径，分别对应上面三个矩形
            Path path_all = new Path();
            Path path_lt = new Path();
            Path path_rb = new Path();
            path_all.addRect(rect_all, Path.Direction.CCW);
            path_lt.addRect(rect_lt, Path.Direction.CCW);
            path_rb.addRect(rect_rb, Path.Direction.CCW);
            // 将左上角和右下角的矩形从path_all中减去
            path_all.addRect(rect_all, Path.Direction.CCW);
            path_all.op(path_lt, Path.Op.DIFFERENCE);
            path_all.op(path_rb, Path.Op.DIFFERENCE);

            canvas.drawPath(path_all, highlightPaint);

        } else {
            Path path_all = new Path();
            path_all.moveTo(startToLeftPosition, (mStartLine + 1) * h + paddingTop);
            path_all.lineTo(startToLeftPosition, mStartLine * h + paddingTop);
            path_all.lineTo(mViewTextWidth + paddingLeft, mStartLine * h + paddingTop);
            path_all.lineTo(mViewTextWidth + paddingLeft, mCurrentLine * h + paddingTop);
            path_all.lineTo(currentToLeftPosition, mCurrentLine * h + paddingTop);
            path_all.lineTo(currentToLeftPosition, (mCurrentLine + 1) * h + paddingTop);
            path_all.lineTo(paddingLeft, (mCurrentLine + 1) * h + paddingTop);
            path_all.lineTo(paddingLeft, (mStartLine + 1) * h + paddingTop);
            path_all.lineTo(startToLeftPosition, (mStartLine + 1) * h + paddingTop);

            canvas.drawPath(path_all, highlightPaint);
        }
        canvas.save();
        canvas.restore();
    }

    /**
     * 计算字符距离控件左侧的位移
     *
     * @param line       字符所在行
     * @param charOffset 字符偏移量
     */
    private float calculatorCharPositionToLeft(int line, int charOffset) {

        String text_str = getText().toString();


        Layout layout = getLayout();
        int lineStart = layout.getLineStart(line);
        int lineEnd = layout.getLineEnd(line);

        String line_str = text_str.substring(lineStart, lineEnd);

        if (line_str.equals("\n"))
            return getPaddingLeft();
        // 最左侧
        if (lineStart == charOffset)
            return getPaddingLeft();
        // 最右侧
        if (charOffset == lineEnd - 1)
            return mViewTextWidth + getPaddingLeft();

        float desiredWidth = StaticLayout.getDesiredWidth(text_str, lineStart, lineEnd, getPaint());

        // 中间位置
        // 计算相邻字符之间需要填充的宽度
        // (TextView内容的实际宽度 - 该行字符串的宽度)/（字符个数-1）
        float insert_blank = (mViewTextWidth - desiredWidth) / (line_str.length() - 1);
        // 计算当前字符左侧所有字符的宽度
        float allLeftCharWidth = StaticLayout.getDesiredWidth(text_str.substring(lineStart, charOffset), getPaint());

        // 相邻字符之间需要填充的宽度 + 当前字符左侧所有字符的宽度
        return insert_blank * (charOffset - lineStart) + allLeftCharWidth + getPaddingLeft();

    }

    private void showActionMenu(int mPopWindowOffsetY, ActionMenuView mActionMenu) {

    }

    private int calculatorActionMenuYPosition(int mTouchDownRawY, int rawY) {
        return mTouchDownRawY;
    }

    private ActionMenuView createActionMenu() {
        return null;
    }
}
