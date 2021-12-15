package org.angmarch.views;

import ohos.agp.components.*;
import ohos.agp.components.element.ElementScatter;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;

/*
 * Copyright (C) 2015 Angelo Marchesin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@SuppressWarnings("unused")
public abstract class NiceSpinnerBaseAdapter<T> extends BaseItemProvider {

    private final PopUpTextAlignment horizontalAlignment;
    private final SpinnerTextFormatter spinnerTextFormatter;

    private Color textColor;
    private int backgroundSelector;

    int selectedIndex;
    private Context context;

    private PopupCustomDialog mDialog;
    private NiceSpinnerAdapterWrapper.InternalListCallback mCallback;
    private int popupBottomPadding;

    NiceSpinnerBaseAdapter(PopupCustomDialog dialog,
            Context context,
            Color textColor,
            int backgroundSelector,
            SpinnerTextFormatter spinnerTextFormatter,
            PopUpTextAlignment horizontalAlignment,
                           int popupBottomPadding
    ) {
        this.spinnerTextFormatter = spinnerTextFormatter;
        mDialog = dialog;
        this.context = context;
        this.backgroundSelector = backgroundSelector;
        this.textColor = textColor;
        this.horizontalAlignment = horizontalAlignment;
        this.popupBottomPadding = popupBottomPadding;
    }


    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        if (component == null) {
            component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_spinner_list_item, componentContainer, false);
        }
        onPrepareView(position, component);
        return component;
    }

    void setCallback(NiceSpinnerAdapterWrapper.InternalListCallback callback) {
        mCallback = callback;
    }

    private void onPrepareView(int index, Component component) {
        Text title = (Text) component.findComponentById(ResourceTable.Id_tv_tinted_spinner);
        try {
            title.setBackground(ElementScatter.getInstance(context).parse(backgroundSelector));
        } catch (Exception e){
           //Exception
        }
        title.setTextSize(50);
        title.setTextColor(textColor);
        if (horizontalAlignment == PopUpTextAlignment.START) {
            title.setTextAlignment(TextAlignment.START);
        } else if(horizontalAlignment == PopUpTextAlignment.END) {
            title.setTextAlignment(TextAlignment.END);
        } else {
            title.setTextAlignment(TextAlignment.CENTER);
        }

        if (spinnerTextFormatter != null) {
            title.setText(spinnerTextFormatter.format(getItem(index)));
        } else {
            title.setText((String) getItem(index));
        }
        title.setPaddingBottom(popupBottomPadding);
        component.setClickedListener(view -> {
            onItemClicked(view, index);
        });
    }

    private void onItemClicked(Component component, int index) {
        if (mCallback != null) {
            mCallback.onItemSelected(mDialog, component, index, null, false);
        }
    }

    private void setTextHorizontalAlignment(Text textView) {
        switch (horizontalAlignment) {
            case START:
                textView.setTextAlignment(TextAlignment.START);
                break;
            case END:
                textView.setTextAlignment(TextAlignment.END);
                break;
            case CENTER:
                textView.setTextAlignment(TextAlignment.HORIZONTAL_CENTER);
                break;
        }
    }

    interface InternalListCallback {
        boolean onItemSelected(PopupCustomDialog dialog, Component itemView, int position, CharSequence text, boolean longPress);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    public abstract T getItemInDataset(int position);

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public abstract int getCount();

    private static class ViewHolder {

        private Text textView;

        private ViewHolder(Text textView) {
            this.textView = textView;
        }
    }
}
