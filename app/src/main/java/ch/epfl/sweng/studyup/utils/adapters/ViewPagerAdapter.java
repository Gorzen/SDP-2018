package ch.epfl.sweng.studyup.utils.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ch.epfl.sweng.studyup.R;

public class ViewPagerAdapter extends PagerAdapter {

    private Context cnx;
    private LayoutInflater layoutInflaterService;
    private Integer [] images = {R.drawable.login_slide1,
            R.drawable.login_slide2,
            R.drawable.login_slide3,
            R.drawable.login_slide4};

    public ViewPagerAdapter(Context context) {
        this.cnx = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflaterService = (LayoutInflater) cnx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflaterService.inflate(R.layout.custom_login_view_pager, null);
        ImageView rulesView = view.findViewById(R.id.rulesView);
        rulesView.setImageResource(images[position]);
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

}
