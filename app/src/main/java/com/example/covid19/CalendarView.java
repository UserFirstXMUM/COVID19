package com.example.covid19;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.covid19.database.UserDBHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 自定义的日历控件
 * Created by xiaozhu on 2016/8/1.
 */
public class CalendarView extends View {

    private Context context;
    /**
     * 画笔
     */
    private Paint paint;
    /***
     * 当前的时间
     */
    private Calendar calendar;
    /**
     * 选中监听
     */
    private OnSelectChangeListener listener;
    /**
     * 是否在本月里画其他月的日子
     */
    private boolean drawOtherDays = true;

    String user_id;

    private OnDrawDays onDrawDays;

    private ArrayList<Integer> normal_day= new ArrayList<Integer>();
    private ArrayList<Integer> abnormal_day= new ArrayList<Integer>();

    /**
     * 改变日期，并更改当前状态，由于绘图是在calendar基础上进行绘制的，所以改变calendar就可以改变图片
     *
     * @param calendar
     */
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        if ((calendar.get(Calendar.MONTH) + "" + calendar.get(Calendar.YEAR)).equals(com.example.covid19.DayManager.getCurrentTime())) {
            com.example.covid19.DayManager.setCurrent(com.example.covid19.DayManager.getTempcurrent());
        } else {
            com.example.covid19.DayManager.setCurrent(-1);
        }
        invalidate();
    }

    public CalendarView(Context context,String user_id) {
        super(context);
        this.context = context;
        //初始化控件
        this.user_id=user_id;
        initView();
    }


    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //初始化控件
        initView();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //初始化控件
        initView();
    }

    /***
     * 初始化控件
     */
    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        calendar = Calendar.getInstance();
        com.example.covid19.DayManager.setCurrent(calendar.get((Calendar.DAY_OF_MONTH)));
        com.example.covid19.DayManager.setTempcurrent(calendar.get(Calendar.DAY_OF_MONTH
        ));
        get_days();
        for (int i=0;i<normal_day.size();i++)
        {
            com.example.covid19.DayManager.addOutDays(normal_day.get(i));
        }
        for (int i=0;i<abnormal_day.size();i++)
        {
            com.example.covid19.DayManager.addAbnormalDays(abnormal_day.get(i));
        }
        com.example.covid19.DayManager.setCurrentTime(calendar.get(Calendar.MONTH) + "" + calendar.get(Calendar.YEAR));
    }
    private void get_days()
    {
        UserDBHelper db =new UserDBHelper(getContext());
        Cursor result=db.getReport(user_id);
        result.moveToFirst();
        for (int i=0;i<result.getCount();i++)
        {
            String date=result.getString(result.getColumnIndex("DATE"));
            int statue=result.getInt(result.getColumnIndex("STATUE"));
            int day=Integer.valueOf(date.substring(date.lastIndexOf("/")+1));
            if (statue==0)
            {
                abnormal_day.add(day);
            }
            else
            {
                normal_day.add(day);
            }
            result.moveToNext();
        }

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取day集合并绘制
        List<Day> days = com.example.covid19.DayManager.createDayByCalendar(calendar, getMeasuredWidth(), getMeasuredHeight(), drawOtherDays);
        for (Day day : days) {

            canvas.save();
            canvas.translate(day.location_x * day.width, day.location_y * day.height);
            if (this.onDrawDays == null || !onDrawDays.drawDay(day, canvas, context, paint)) {
                day.drawDays(canvas, context, paint);
            }

            if (this.onDrawDays != null) {
                onDrawDays.drawDayAbove(day, canvas, context, paint);
            }

            canvas.restore();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            //判断点击的是哪个日期
            float x = event.getX();
            float y = event.getY();
            //计算点击的是哪个日期
            int locationX = (int) (x * 7 / getMeasuredWidth());
            int locationY = (int) ((calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + 1) * y / getMeasuredHeight());
            if (locationY == 0) {
                return super.onTouchEvent(event);
            } else if (locationY == 1) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                System.out.println("xiaozhu" + calendar.get(Calendar.DAY_OF_WEEK) + ":" + locationX);
                if (locationX < calendar.get(Calendar.DAY_OF_WEEK) - 1) {
                    return super.onTouchEvent(event);
                }
            } else if (locationY == calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                if (locationX > calendar.get(Calendar.DAY_OF_WEEK) + 1) {
                    return super.onTouchEvent(event);
                }
            }
            calendar.set(Calendar.WEEK_OF_MONTH, (int) locationY);
            calendar.set(Calendar.DAY_OF_WEEK, (int) (locationX + 1));
            com.example.covid19.DayManager.setSelect(calendar.get(Calendar.DAY_OF_MONTH));
            if (listener != null) {
                listener.selectChange(this, calendar.getTime());
            }
            invalidate();

        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置日期选择改变监听
     *
     * @param listener
     */
    public void setOnSelectChangeListener(OnSelectChangeListener listener) {

        this.listener = listener;
    }

    /**
     * 是否画本月外其他日子
     *
     * @param drawOtherDays true 表示画，false表示不画 ，默认为true
     */
    public void setDrawOtherDays(boolean drawOtherDays) {
        this.drawOtherDays = drawOtherDays;
        invalidate();
    }

    /**
     * 日期选择改变监听的接口
     */
    public interface OnSelectChangeListener {
        void selectChange(com.example.covid19.CalendarView calendarView, Date date);
    }

    /**
     * 画天数回调
     */
    public interface OnDrawDays {
        /**
         * 层次在原画下
         * 画天的回调，返回true 则覆盖默认的画面，返回
         *
         * @return
         */
        boolean drawDay(Day day, Canvas canvas, Context context, Paint paint);

        /**
         * 层次在原画上
         */
        void drawDayAbove(Day day, Canvas canvas, Context context, Paint paint);
    }

    public void setOnDrawDays(OnDrawDays onDrawDays) {
        this.onDrawDays = onDrawDays;
    }

}
