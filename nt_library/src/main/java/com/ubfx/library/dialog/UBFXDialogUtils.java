package com.ubfx.library.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.model.IPickerViewData;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.ubfx.library.R;
import com.ubfx.library.dialog.builder.ConfirmDialogBuilder;
import com.ubfx.library.dialog.builder.SingleImageDialogBuilder;
import com.ubfx.library.quickadapter.BaseAdapterHelper;
import com.ubfx.library.quickadapter.QuickAdapter;
import com.ubfx.library.utils.DrawableUtils;
import com.ubfx.library.utils.ImageLoadUtils;
import com.ubfx.library.utils.ScreenUtils;
import com.ubfx.theme.ThemeMgr;

import java.util.List;

/**
 * Created by chuanzheyang on 17/2/9.
 */

public class UBFXDialogUtils {

    public static DialogPlus createListDialog(Context context, boolean showDivider, BaseAdapter adapter, String title, AdapterView.OnItemClickListener itemClickListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ListView view = new ListView(context);
        view.setAdapter(adapter);
        view.setOnItemClickListener(itemClickListener);
        view.setBackgroundColor(ThemeMgr.get().getThemeProvider().backgroundMain());
        if (!showDivider) {
            view.setDivider(null);
            view.setDividerHeight(0);
        }
        int marginTop = (int) (ScreenUtils.getScreenSize(context).heightPixels * 0.2);


        DialogPlusBuilder builder = DialogPlus.newDialog(context)
                .setMargin(0, marginTop, 0, 0)
                .setContentHolder(new ViewHolder(view))
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM);
        final DialogPlus dialogPlus;
        if (!TextUtils.isEmpty(title)) {
            View header = inflater.inflate(R.layout.lib_dialog_header, null);
            header.setBackgroundColor(ThemeMgr.get().getThemeProvider().backgroundMain());
            TextView tvHeader = header.findViewById(R.id.lib_tv_dialog_title);
            tvHeader.setTextColor(ThemeMgr.get().getThemeProvider().fontMain());
            tvHeader.setText(title);
            builder.setHeader(header);
            dialogPlus = builder.create();
            header.findViewById(R.id.lib_btn_dialog_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogPlus.dismiss();
                }
            });

        } else {
            dialogPlus = builder.create();
        }
        return dialogPlus;
    }

    public static DialogPlus createListDialog(Context context, BaseAdapter adapter, String title, AdapterView.OnItemClickListener itemClickListener) {
        return createListDialog(context, true, adapter, title, itemClickListener);
    }


    public static DialogPlus createActionDialog(Context context, List<String> actions, AdapterView.OnItemClickListener itemClickListener) {
        return createActionDialog(context, actions, itemClickListener, null);
    }


    public static DialogPlus createActionDialog(Context context, List<String> actions, final AdapterView.OnItemClickListener itemClickListener,
                                                OnDismissListener dismissListener, final OnCancelListener cancelListener) {
        QuickAdapter adapter = new QuickAdapter<String>(context, R.layout.lib_dialog_action_item, actions) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.lib_tv_dialog_action_title, item);
                helper.setTextColor(R.id.lib_tv_dialog_action_title, ThemeMgr.get().getThemeProvider().fontMain());
            }
        };
        final DialogPlus dialogPlus;
        int margin = ScreenUtils.dp2px(context, 10);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lib_dialog_action, null);
        view.setBackgroundColor(Color.TRANSPARENT);

        DialogPlusBuilder builder = DialogPlus.newDialog(context)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setContentHolder(new ViewHolder(view))
                .setMargin(margin, 0, margin, margin)
                .setCancelable(true)
                .setContentBackgroundResource(android.R.color.transparent)
                .setGravity(Gravity.BOTTOM);
        dialogPlus = builder.create();
        ListView listView = view.findViewById(R.id.lib_lv_dialog_action);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogPlus.dismiss();
                itemClickListener.onItemClick(parent, view, position, id);
            }
        });
        listView.setBackground(DrawableUtils.createDrawable(ScreenUtils.dp2px(context, 4), ThemeMgr.get().getThemeProvider().backgroundMain()));

        TextView viewCancel = view.findViewById(R.id.lib_btn_dialog_action_cancel);
        viewCancel.setTextColor(ThemeMgr.get().getThemeProvider().fontMain());
        viewCancel.setBackground(DrawableUtils.createDrawable(ScreenUtils.dp2px(context, 4), ThemeMgr.get().getThemeProvider().backgroundMain()));
        viewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
                if (cancelListener != null) {
                    cancelListener.onCancel(dialogPlus);
                }
            }
        });
        return dialogPlus;

    }

    public static DialogPlus createActionDialog(Context context, List<String> actions, final AdapterView.OnItemClickListener itemClickListener, OnDismissListener dismissListener) {
        return createActionDialog(context, actions, itemClickListener, dismissListener, null);
    }

    /**
     * Use create confirm dialog with ConfirmDialogBuilder please
     */
    @Deprecated
    public static DialogPlus createConfirmDialog(Context context,
                                                 String title, String content,
                                                 final OnConfirmListener confirmListener, final OnCancelListener cancelListener) {
        ConfirmDialogBuilder builder = new ConfirmDialogBuilder(context);
        builder.setTitle(title).setContent(content);
        builder.setCancelListener(cancelListener);
        if (cancelListener == null) {
            builder.setCancelDesc("");
        }
        builder.setConfirmListener(confirmListener);
        if (confirmListener == null) {
            builder.setConfirmDesc("");
        }

        return createConfirmDialog(context, builder);
    }

    /**
     * Use create confirm dialog with ConfirmDialogBuilder please
     */
    @Deprecated
    public static DialogPlus createConfirmDialog(Context context, String title, String hintText,
                                                 String confirmDesc, String cancelDesc, final OnConfirmListener confirmListener, final OnCancelListener cancelListener, final OnDismissListener dismissListener) {

        ConfirmDialogBuilder builder = new ConfirmDialogBuilder(context);
        builder.setTitle(title).setContent(hintText).setDismissListener(dismissListener);
        builder.setCancelListener(cancelListener);
        builder.setCancelDesc(cancelDesc);
        builder.setConfirmListener(confirmListener);
        builder.setConfirmDesc(confirmDesc);

        return createConfirmDialog(context, builder);
    }

    public static DialogPlus createConfirmDialog(Context context, final ConfirmDialogBuilder confirmBuilder) {
        final DialogPlus dialogPlus;
        int margin = (int) (ScreenUtils.getScreenSize(context).widthPixels * 1.0 / 10.0) - ScreenUtils.dp2px(context, 5);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lib_dialog_confirm, null);
        DialogPlusBuilder builder = DialogPlus.newDialog(context)
                .setOnDismissListener(confirmBuilder.getDismissListener())
                .setOnCancelListener(confirmBuilder.getCancelListener())
                .setContentHolder(new ViewHolder(view))
                .setMargin(margin, 0, margin, margin)
                .setCancelable(confirmBuilder.isCancelable())
                .setContentBackgroundResource(android.R.color.transparent)
                .setGravity(Gravity.CENTER);
        dialogPlus = builder.create();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
                if (R.id.lib_btn_confirm == v.getId()) {
                    if (confirmBuilder.getConfirmListener() != null) {
                        confirmBuilder.getConfirmListener().onConfirm();
                    }
                } else if (R.id.lib_btn_cancel == v.getId()) {
                    if (confirmBuilder.getCancelListener() != null) {
                        confirmBuilder.getCancelListener().onCancel(dialogPlus);
                    }
                }
            }
        };
        LinearLayout linearLayout = view.findViewById(R.id.ll_dialog_confirm);
        linearLayout.setBackground(DrawableUtils.createDrawable(0, ThemeMgr.get().getThemeProvider().backgroundMain()));
        TextView tvTitle = view.findViewById(R.id.lib_tv_title);
        tvTitle.setText(confirmBuilder.getTitle());
        tvTitle.setTextColor(ThemeMgr.get().getThemeProvider().fontMain());
        TextView tvCancel = view.findViewById(R.id.lib_btn_cancel);
        if (TextUtils.isEmpty(confirmBuilder.getCancelDesc())) {
            tvCancel.setVisibility(View.GONE);
        } else {
            tvCancel.setText(confirmBuilder.getCancelDesc());
            tvCancel.setOnClickListener(listener);
        }


        TextView tvConfirm = view.findViewById(R.id.lib_btn_confirm);

        if (TextUtils.isEmpty(confirmBuilder.getConfirmDesc())) {
            tvConfirm.setVisibility(View.GONE);
        } else {
            tvConfirm.setText(confirmBuilder.getConfirmDesc());
            tvConfirm.setOnClickListener(listener);
        }
        TextView tvContent = view.findViewById(R.id.lib_dialog_content);
        tvContent.setText(confirmBuilder.getContent());
        tvContent.setTextColor(ThemeMgr.get().getThemeProvider().fontMain());
        return dialogPlus;

    }

    /**
     * Use create confirm dialog with ConfirmDialogBuilder please
     */
    @Deprecated
    public static DialogPlus createConfirmDialog(Context context, String title, String hintText, OnConfirmListener comfirmListener) {
        return createConfirmDialog(context, title, hintText, comfirmListener, null);
    }


    public static boolean showSingleImgDialog(final Activity context, @NonNull final SingleImageDialogBuilder model) {

        final DialogPlus dialogPlus;

        int margin = (int) (ScreenUtils.getScreenSize(context).widthPixels * 0.126);

        int width = ScreenUtils.getScreenSize(context).widthPixels - margin * 2;


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lib_dialog_single_img, null);

        ImageView imageView = view.findViewById(R.id.lib_iv_dialog_single_img);

        imageView.getLayoutParams().width = width;
        imageView.getLayoutParams().height = (int) ((width) * 9.0 / 7.0);

        DialogPlusBuilder builder = DialogPlus.newDialog(context)
                .setOutAnimationDynamic(model.getOutAnimation())
                .setContentHolder(new ViewHolder(view))
                .setMargin(margin, 0, margin, 0)
                .setCancelable(model.isCancelable())
                .setContentBackgroundResource(android.R.color.transparent)
                .setGravity(Gravity.CENTER);
        if (model.isCancelable()) {
            builder.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(@NonNull DialogPlus dialog) {
                    if (model.getCancelClickListener() != null) {
                        model.getCancelClickListener().onClick(dialog.getHolderView());
                    }
                }
            });
        }
        dialogPlus = builder.create();

        if (model.getDataSource() != null) {
            ImageLoadUtils.loadImageNoPlaceholder(model.getDataSource().getDialogImg(), imageView);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
                if (model.getImageClickListener() != null) {
                    model.getImageClickListener().onClick(v);
                }
            }
        });

        ImageView ivClose = view.findViewById(R.id.lib_iv_dialog_close);
        if (model.isCancelable()) {
            ivClose.setVisibility(View.VISIBLE);

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogPlus.dismiss();
                    if (model.getCancelClickListener() != null) {
                        model.getCancelClickListener().onClick(v);
                    }
                }
            });
        } else {
            ivClose.setVisibility(View.GONE);
        }

        if (dialogPlus.isShowing()) {
            return false;
        }
        dialogPlus.show();
        return true;
    }


    //******************************  Picker View ************************************************************************

    /**
     * @param context
     * @param actions  item must implements ActionGetter !!!
     * @param listener
     * @return
     */
    public static OptionsPickerView showPickerDialog(Activity context, List actions, OptionsPickerView.OnOptionsSelectListener listener) {
        int fontAlpha = ColorUtils.setAlphaComponent(ThemeMgr.get().getThemeProvider().fontMain(), 100);
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, listener)
                .setBgColor(ThemeMgr.get().getThemeProvider().backgroundMain())
                .setTitleBgColor(ThemeMgr.get().getThemeProvider().backgroundLight())
                .setTextColorCenter(ThemeMgr.get().getThemeProvider().fontMain())
                .setTextColorOut(fontAlpha)
                .setDividerColor(ThemeMgr.get().getThemeProvider().borderLight())
                .build();
        pvOptions.setPicker(actions);
        return pvOptions;
    }

    public static OptionsPickerView showPickerDialog(Activity context, List actions1, List actions2, OptionsPickerView.OnOptionsSelectListener listener) {
        int fontAlpha = ColorUtils.setAlphaComponent(ThemeMgr.get().getThemeProvider().fontMain(), 100);
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, listener)
                .setBgColor(ThemeMgr.get().getThemeProvider().backgroundMain())
                .setTitleBgColor(ThemeMgr.get().getThemeProvider().backgroundLight())
                .setTextColorCenter(ThemeMgr.get().getThemeProvider().fontMain())
                .setTextColorOut(fontAlpha)
                .setDividerColor(ThemeMgr.get().getThemeProvider().borderLight())
                .build();
        pvOptions.setNPicker(actions1, actions2, null);
        return pvOptions;
    }


    public interface ActionGetter extends IPickerViewData {

    }

    //******************************  Picker View ************************************************************************


}
