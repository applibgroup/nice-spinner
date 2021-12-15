/*
 *  * Copyright (C) 2021 Huawei Device Co., Ltd.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.angmarch.views;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.PopupDialog;
import ohos.app.Context;

import java.util.List;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;

public class PopupCustomDialog extends PopupDialog implements Component.ClickedListener, NiceSpinnerBaseAdapter.InternalListCallback {
    private static final String TAG = PopupCustomDialog.class.getSimpleName();
    private ListContainer listContainer;
    final Builder builder;

    private PopupCustomDialog(Builder builder, Component component) {
        super(builder.getContext(), component);
        this.builder = builder;
        ComponentContainer rootLayout = (ComponentContainer) LayoutScatter.getInstance(builder.getContext()).parse(getInflateLayout(builder), null, false);
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setShape(ShapeElement.RECTANGLE);
        shapeElement.setRgbColor(new RgbColor(HexToColor(builder.backgroundColor)));
        Component boxroot = rootLayout.findComponentById(ResourceTable.Id_box_root);
        boxroot.setBackground(shapeElement);

        prepareDialogView(rootLayout, builder);
        setCustomComponent(rootLayout);
    }

    public static RgbColor HexToColor(String hex)
    {
        hex = hex.replace("#", "");
        switch (hex.length()) {
            case 6:
                return new RgbColor(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16));
            case 8:
                return new RgbColor(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16),
                        Integer.valueOf(hex.substring(6, 8), 16));
            default:
                return new RgbColor(255,255,255,255);
        }

    }

    private void prepareDialogView(ComponentContainer rootLayout, Builder builder) {
        listContainer = UiTUtil.getComponent(rootLayout, ResourceTable.Id_listContainer);

        if (listContainer != null && builder.baseItemProvider == null) {
            builder.baseItemProvider = new NiceSpinnerAdapterWrapper( this,builder.getContext(),builder.textColor, builder.backgroundSelecorElement, builder.spinnerTextFormatter,  builder.horizontalTextAlignment, builder.popupPaddingBottom);
            listContainer.enableScrollBar(Component.VERTICAL,true);
            listContainer.setScrollbarColor(Color.DKGRAY);
        }
        invalidateList();
    }

    @Override
    public void onClick(Component component) {
        hide();
    }


    @Override
    public boolean onItemSelected(PopupCustomDialog dialog, Component itemView, int position, CharSequence text, boolean longPress) {
        if (!longPress && builder.listCallback != null) {
            if(builder.spinnerTextFormatter != null) {
                builder.listCallback.onSelection(this, itemView, position, builder.spinnerTextFormatter.format(builder.items.get(position)));
            } else {
                builder.listCallback.onSelection(this, itemView, position, (String) builder.items.get(position));
            }
            destroy();
        }
        return true;
    }

    public static class Builder<T> {
        private Context context;
        BaseItemProvider baseItemProvider;
        ListCallback listCallback;
        String backgroundColor;
        int backgroundSelecorElement;
        Color textColor;
        int popupPaddingBottom;
        PopUpTextAlignment horizontalTextAlignment;
        SpinnerTextFormatter spinnerTextFormatter;
        List<T> items;


        public Builder(Context context) {
            this.context = context;
        }

        public final Context getContext() {
            return context;
        }

        public Builder setBackgroundColor(String color){
            this.backgroundColor = color;
            return this;
        }

        public Builder setTextAlignment(PopUpTextAlignment horizontalTextAlignment){
            this.horizontalTextAlignment = horizontalTextAlignment;
            return this;
        }

        public Builder setBackgroundSelecorColor(int backgroundSelecorElement){
            this.backgroundSelecorElement = backgroundSelecorElement;
            return this;
        }

        public Builder setCustomTextColor(Color textColor){
            this.textColor = textColor;
            return this;
        }

        public Builder setSpinnerTextFormat(SpinnerTextFormatter spinnerTextFormat){
            this.spinnerTextFormatter = spinnerTextFormat;
            return this;
        }

        public Builder setPopupPaddingBottom( int bottom){
            this.popupPaddingBottom = bottom;
            return this;
        }

        public Builder items(List<T> items) {
            this.items = items;
            return this;
        }

        public Builder itemsCallback(ListCallback callback) {
            this.listCallback = callback;
            return this;
        }

        public void show(Component component) {
            new PopupCustomDialog(this, component).setMode(LayoutAlignment.BOTTOM | LayoutAlignment.LEFT).setSize(component.getWidth(),MATCH_CONTENT)
                    .show();
        }
    }

    public interface ListCallback {
        void onSelection(PopupCustomDialog dialog, Component itemView, int position, CharSequence text);
    }

    private int getInflateLayout(Builder builder) {
        return ResourceTable.Layout_popup_list_layout;
    }

    private void invalidateList() {
        if (listContainer == null) {
            return;
        } else if ((builder.items == null || builder.items.size() == 0) && builder.baseItemProvider == null) {
            return;
        }
        listContainer.setItemProvider(builder.baseItemProvider);
        ((NiceSpinnerAdapterWrapper) builder.baseItemProvider).setCallback(this);
    }
}
