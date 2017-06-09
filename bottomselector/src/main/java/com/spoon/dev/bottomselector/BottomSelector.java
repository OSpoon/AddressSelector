package com.spoon.dev.bottomselector;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class BottomSelector extends Dialog {
    private AddressSelector selector;

    private BottomSelector(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        selector = new AddressSelector(context);
        setContentView(selector.getView());
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = (int) Math.ceil(context.getResources().getDisplayMetrics().density * 256);
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
    }

    public static BottomSelector show(Context context, OnAddressSelectedListener listener) {
        BottomSelector dialog = new BottomSelector(context, R.style.bottom_dialog);
        dialog.selector.setDialog(dialog);
        dialog.selector.setOnAddressSelectedListener(listener);
        dialog.show();
        return dialog;
    }
}
