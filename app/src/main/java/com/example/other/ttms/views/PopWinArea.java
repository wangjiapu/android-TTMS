package com.example.other.ttms.views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.other.ttms.R;
import com.example.other.ttms.adapters.AreaAdapter;
import com.example.other.ttms.beans.AreaInfo;

import java.util.List;


/**
 * 自定义popupWindow  用来弹出地区的区域信息
 */
public class PopWinArea extends PopupWindow {

    private View rootView;
    private ListView mlistView;
    private Activity mActivity;
    private List<AreaInfo> mlist;

    private static final int width = 120;
    private static final int height = 200;


    public PopWinArea(Activity activity, List<AreaInfo> list) {
        this.mActivity = activity;
        this.mlist = list;
        rootView = LayoutInflater.from(activity).inflate(R.layout.popwin_area, null);
        setListView(rootView);
        setContentView(rootView);
        setWidth(dip2px(width));
        setHeight(dip2px(height));

        setAnimationStyle(R.style.AnimTools);
        //
    }


    private void setListView(View rootView) {
        mlistView = rootView.findViewById(R.id.area_list);
        AreaAdapter adapter = new AreaAdapter(mActivity, mlist);
        mlistView.setAdapter(adapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mActivity, mlist.get(position) + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * dip转px
     *
     * @param width
     * @return
     */
    private int dip2px(int width) {
        final float scale = mActivity.getResources().getDisplayMetrics().density;
        return (int) (width * scale + 0.5f);
    }

}
