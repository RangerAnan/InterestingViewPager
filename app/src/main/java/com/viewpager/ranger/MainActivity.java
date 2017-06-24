package com.viewpager.ranger;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.viewpager.ranger.pageTransformer.AlphaPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * viewpager一屏显示多个页面
 * 1.Android:clipChildren="false"--子view在绘制时不要裁切它的显示范围
 * <p>
 * 2.设置滑动动画setPageTransformer
 * <p>
 * 3.无限轮播方案:将适配的view多配置两个，放在viewpager第一个与最后一个。滑动最后一个时，直接设置setCurrentItem切换到第一个。
 * <p>
 * 4.轮播的小点显示
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private List<ImageView> viewList;
    private int currentItem = 1;

    private int[] imgResources = {R.mipmap.a1, R.mipmap.a2, R.mipmap.a3, R.mipmap.a4, R.mipmap.a5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp = (ViewPager) findViewById(R.id.vp);

        //设置page间距
        vp.setPageMargin(20);
        vp.setOffscreenPageLimit(3);
        vp.setPageTransformer(false, new AlphaPageTransformer());

        //设置子view
        viewList = new ArrayList<>();
        initViewPagerChildView();

        vp.setAdapter(new MyAdapter());
        vp.setCurrentItem(6);

        vp.addOnPageChangeListener(this);

        //
        //test2
    }


    /**
     * 由于一屏显示多个页面，viewpager设置两遍childView，再包装边界页面
     * 图片显示顺序：a5,a1,a2...,a5,a1,a2...,a5,a1
     */
    private void initViewPagerChildView() {
        viewList.clear();
        for (int i = 0; i < (imgResources.length * 2) + 2; i++) {
            ImageView view = new ImageView(MainActivity.this);
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

               /* else if (vp.getCurrentItem() == 2) {
                    //bug:如果连续滑动2个item，跳过了item为1的时候，就会出现边界
                    vp.setCurrentItem(imgResources.length + 2, false);

                } else if (vp.getCurrentItem() == 3) {
                    vp.setCurrentItem(imgResources.length + 3, false);

                } else if (vp.getCurrentItem() == 4) {
                    vp.setCurrentItem(imgResources.length + 4, false);

                } else if (vp.getCurrentItem() == 5) {
                    vp.setCurrentItem(imgResources.length, false);

                }*/

                Log.i("MainActivity", "--CurrentItem:" + vp.getCurrentItem());
                break;
            case ViewPager.SCROLL_STATE_SETTLING:   //滑动结束
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:   //拖动中
                break;
        }
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
