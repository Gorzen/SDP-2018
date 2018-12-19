package ch.epfl.sweng.studyup.utils.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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
    private Integer[] images;

    public ViewPagerAdapter(Context context, Integer[] images) {
        this.cnx = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflaterService = (LayoutInflater) cnx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflaterService.inflate(R.layout.custom_login_view_pager, null);
        ImageView rulesView = view.findViewById(R.id.rulesView);
        rulesView.setImageResource(images[position]);
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

}
