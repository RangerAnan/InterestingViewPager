package com.viewpager.ranger;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.viewpager.ranger.pageTransformer.AlphaPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * 一屏展示两个页面
 */
public class TestVpActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    private ViewPager vp;
    private List<ImageView> viewList;
    private int currentItem = 1;

    private int[] imgResources = {R.mipmap.a1, R.mipmap.a2, R.mipmap.a3, R.mipmap.a4, R.mipmap.a5};
    private int marginLeft;
    private float startX;
    private float moveX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testvp);
        vp = (ViewPager) findViewById(R.id.vp);

        //设置page间距
        vp.setPageMargin(20);
        vp.setOffscreenPageLimit(3);
        vp.setPageTransformer(false, new AlphaPageTransformer());

        //动态设置viewpager的marginLeft
        setViewpagerMargin();

        //设置子view
        viewList = new ArrayList<>();
        initViewPagerChildView();

        vp.setAdapter(new MyAdapter());
        vp.setCurrentItem(6);

        //vp的滑动事件只能监听当前的item,这里就不适用了。
        vp.addOnPageChangeListener(this);

        vp.setOnTouchListener(this);


    }

    /**
     * 由于一屏显示多个页面，viewpager设置两遍childView，再包装边界页面
     * 图片显示顺序：a5,a1,a2...,a5,a1,a2...,a5,a1
     */
    private void initViewPagerChildView() {
        viewList.clear();
        for (int i = 0; i < (imgResources.length * 2) + 2; i++) {
            ImageView view = new ImageView(TestVpActivity.this);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (i == 0) {
                //第一个位置显示最后一张图片
                view.setImageResource(imgResources[imgResources.length - 1]);
            } else if (i == (imgResources.length * 2) + 1) {
                //最后一个位置显示第一张图片
                view.setImageResource(imgResources[0]);
            } else {
                //存储两遍资源视图
                if (i <= imgResources.length) {
                    view.setImageResource(imgResources[i - 1]);
                } else {
                    view.setImageResource(imgResources[i - imgResources.length - 1]);
                }
            }
            viewList.add(view);
        }
    }


    private void setViewpagerMargin() {

        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        marginLeft = (width - 20) / 2;

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vp.getLayoutParams();
        layoutParams.leftMargin = marginLeft;
        vp.setLayoutParams(layoutParams);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i("MainActivity", "--state:" + state);
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:     //暂停状态
                //偷梁换柱
                //如果当前item滑到第二个的时候，将其设置成资源的第六个
                if (vp.getCurrentItem() == 1) {
                    vp.setCurrentItem(imgResources.length + 1, false);
                } else if (vp.getCurrentItem() == imgResources.length * 2) {
                    //如果当前item变化到倒数第二个时，将其设置成资源的第五个（同一个资源的切换）
                    vp.setCurrentItem(imgResources.length, false);
                }

                Log.i("MainActivity", "--CurrentItem:" + vp.getCurrentItem());
                break;
            case ViewPager.SCROLL_STATE_SETTLING:   //滑动结束
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:   //拖动中
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = v.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = v.getX();
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return false;
    }


    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = viewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }
}
