package com.ubfx.library.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.ubfx.library.R;
import com.ubfx.library.quickadapter.BaseAdapterHelper;
import com.ubfx.library.quickadapter.QuickAdapter;
import com.ubfx.library.utils.DrawableUtils;
import com.ubfx.library.utils.ViewUtils;
import com.ubfx.theme.ThemeMgr;
import com.ubfx.theme.UBResourceHelper;

import java.util.List;

/**
 * Created by limeng on 2017/9/4.
 */

public class TitleMenuView extends LinearLayout {
    private TextView tvTitle;
    private ImageView ivArrow;

    private DialogPlus menuDialog;

    private List<String> menus;
    private OnMenuItemSelectedListener mMenuItemSelectedListener;

    public void setMenus(List<String> menus) {
        this.menus = menus;
        if (menus != null && menus.size() > 0) {
            tvTitle.setText(menus.get(0));
        }

        ivArrow.setVisibility((menus == null || menus.size() <= 1) ? GONE : VISIBLE);
    }

    public void setMenuItemSelectedListener(OnMenuItemSelectedListener listener) {
        mMenuItemSelectedListener = listener;
    }

    public TitleMenuView(Context context) {
        super(context);
        init(context);
    }

    public TitleMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lib_item_title_menu_layout, this, false);
        addView(view);

        tvTitle = view.findViewById(R.id.tv_menu_title);
        tvTitle.setTextColor(ThemeMgr.get().getThemeProvider().fontMain());
        ivArrow = view.findViewById(R.id.iv_menu_arrow);

        Drawable drawable = DrawableUtils.tint(context.getResources().getDrawable(R.drawable.lib_icon_new_down_arrow_black).mutate(), ThemeMgr.get().getThemeProvider().fontMain());
        ivArrow.setImageDrawable(drawable);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuDialog();
            }
        });
    }

    public void showMenuDialog() {
        if (menus.size() <= 1) {
            return;
        }

        if (menuDialog != null) {
            menuDialog.dismiss();
            return;
        }

        if (UBResourceHelper.isNight(getContext())) {
            Drawable drawable = DrawableUtils.tint(getContext().getResources().getDrawable(R.drawable.lib_icon_new_up_arrow_black), ThemeMgr.get().getThemeProvider().fontMain());
            ivArrow.setImageDrawable(drawable);
        }else {
            ivArrow.setImageResource(R.drawable.lib_icon_new_up_arrow_black);
        }

        int[] location = new int[2];
        getLocationOnScreen(location);

        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);

        Activity activity = ViewUtils.getActivity(this);
        if (activity != null) {
            if ((activity.getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) == View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) {
                // full screen
                rect.top = 0;
            }
        }

        menuDialog = createTitleMenu(getContext(), getHeight() + location[1] - rect.top, menus, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvTitle.setText(menus.get(position));
                menuDialog.dismiss();

                if (mMenuItemSelectedListener != null) {
                    mMenuItemSelectedListener.onMenuItemSelected(position);
                }
            }
        }, new OnDismissListener() {
            @Override
            public void onDismiss(DialogPlus dialog) {
                ivArrow.setImageResource(R.drawable.lib_icon_new_down_arrow_black);
                menuDialog = null;
            }
        });
        menuDialog.show();
    }

    public void setCurrentItem(int position) {
        if (position >= 0 && position < menus.size()) {
            tvTitle.setText(menus.get(position));
        }
    }

    public interface OnMenuItemSelectedListener {
        void onMenuItemSelected(int position);
    }

    private DialogPlus createTitleMenu(Context context, int topMargin, List<String> menus, AdapterView.OnItemClickListener itemClickListener, OnDismissListener dismissListener) {
        QuickAdapter adapter = new QuickAdapter<String>(context, R.layout.lib_item_title_menu_item_layout, menus) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.tv_menu_name, item);
                helper.setTextColor(R.id.tv_menu_name,ThemeMgr.get().getThemeProvider().fontMain());
            }
        };

        final DialogPlus dialogPlus;
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.lib_dialog_title_menu, null);

        DialogPlusBuilder builder = DialogPlus.newDialog(context)
                .setOnDismissListener(dismissListener)
                .setContentHolder(new ViewHolder(view))
                .setOutMostMargin(0, topMargin, 0, 0)
                .setCancelable(true)
                .setContentBackgroundResource(android.R.color.transparent)
                .setGravity(Gravity.TOP);
        dialogPlus = builder.create();

        View viewSep = new View(context);
        viewSep.setBackgroundColor(ThemeMgr.get().getThemeProvider().grayLightest());
        view.addView(viewSep, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

        LinearLayout llContent = view.findViewById(R.id.ll_dialog_content);
        llContent.setBackgroundColor(ThemeMgr.get().getThemeProvider().backgroundMain());

        ListView listView = view.findViewById(R.id.lv_dialog_action);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
        listView.setDividerHeight(0);
        listView.setBackgroundColor(ThemeMgr.get().getThemeProvider().backgroundMain());

        return dialogPlus;
    }
}
