package org.angmarch.views;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;
import ohos.agp.components.element.VectorElement;
import ohos.agp.render.ColorMatrix;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

import java.util.List;
import java.util.Optional;


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
public class NiceSpinner<T> extends Text implements Component.TouchEventListener {

    private static final int MAX_LEVEL = 10000;
    private static final int VERTICAL_OFFSET = 1;
    private static final String INSTANCE_STATE = "instance_state";
    private static final String SELECTED_INDEX = "selected_index";
    private static final String IS_POPUP_SHOWING = "is_popup_showing";
    private static final String IS_ARROW_HIDDEN = "is_arrow_hidden";
    private static final String ARROW_DRAWABLE_RES_ID = "arrow_drawable_res_id";

    private int selectedIndex;
    private VectorElement arrowDrawable;
    private NiceSpinnerBaseAdapter adapter;

    private OnSpinnerItemSelectedListener onSpinnerItemSelectedListener;
    private List<T> items;

    private boolean isArrowHidden;
    private Color textColor;
    private int backgroundSelector;
    private String arrowDrawableTint;
    private int displayHeight;
    private int parentVerticalOffset;
    private int dropDownListPaddingBottom;
    Element arrowDrawableResId;
    private SpinnerTextFormatter spinnerTextFormatter = new SimpleSpinnerTextFormatter();
    private SpinnerTextFormatter selectedTextFormatter = new SimpleSpinnerTextFormatter();
    private PopUpTextAlignment horizontalAlignment;
    private Context context ;
    private String backgroundColor;
    private ToastDialog toastDialog;
    private int popupTextAlignmentvalue;

    public NiceSpinner(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public NiceSpinner(Context context, AttrSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NiceSpinner(Context context, AttrSet attrs, int defStyleAttr) {
        super(context, attrs,"");
        init(context, attrs);
    }


    private void init(Context context, AttrSet attrs) {
        int defaultPadding = 12;

        this.setTextAlignment(TextAlignment.VERTICAL_CENTER);
        setPadding(24, defaultPadding, defaultPadding,
                defaultPadding);
        setClickable(true);
        backgroundSelector = ResourceTable.Graphic_selectdialog;
        setBackground(ElementScatter.getInstance(context).parse(backgroundSelector));
        textColor = getDefaultTextColor(context);
        setTextColor(textColor);
        if (attrs != null) {
            textColor = attrs.getAttr("textTint").isPresent() ?
                    attrs.getAttr("textTint").get().getColorValue() : Color.BLACK;
            setTextColor(textColor);
            arrowDrawableTint = attrs.getAttr("arrowTint").isPresent() ?
                    attrs.getAttr("arrowTint").get().getStringValue() : "#000000";
            isArrowHidden = attrs.getAttr("hideArrow").isPresent() ?
                    attrs.getAttr("hideArrow").get().getBoolValue() : false;
            arrowDrawableResId = attrs.getAttr("arrowDrawable").isPresent() ?
                    attrs.getAttr("arrowDrawable").get().getElement() : new VectorElement(mContext, ResourceTable.Media_ic_arrow_drop_down_black_24);
            dropDownListPaddingBottom = attrs.getAttr("dropDownListPaddingBottom").isPresent() ?
                    attrs.getAttr("dropDownListPaddingBottom").get().getDimensionValue() : 12;
            backgroundColor = attrs.getAttr("background_color").isPresent() ?
                    attrs.getAttr("background_color").get().getStringValue() : "#f2f2f2";
            popupTextAlignmentvalue = attrs.getAttr("popupTextAlignment").isPresent()?
                    attrs.getAttr("popupTextAlignment").get().getIntegerValue(): 0;
            popupTextAlignment(popupTextAlignmentvalue);
        }

        setTouchEventListener(this);
        initComponent();
    }

    private void popupTextAlignment(int popupTextAlignmentvalue){
        if (popupTextAlignmentvalue == 2) {
            horizontalAlignment =  PopUpTextAlignment.CENTER;
        } else if (popupTextAlignmentvalue == 1) {
            horizontalAlignment =  PopUpTextAlignment.END;
        } else {
            horizontalAlignment = PopUpTextAlignment.START;
        }

    }

    private void measureDisplayHeight() {
        Optional<Display> optionalDisplay = DisplayManager.getInstance().getDefaultDisplay(context);
        displayHeight = optionalDisplay.get().getAttributes().height;
    }

    private int getParentVerticalOffset() {
        if (parentVerticalOffset > 0) {
            return parentVerticalOffset;
        }
        int[] locationOnScreen = new int[2];
        return parentVerticalOffset = locationOnScreen[VERTICAL_OFFSET];
    }

    private Color getDefaultTextColor(Context context) {
        return Color.BLACK;
    }

    public Object getItemAtPosition(int position) {
        return adapter.getItemInDataset(position);
    }

    public Object getSelectedItem() {
        return adapter.getItemInDataset(selectedIndex);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Set the default spinner item using its index
     *
     * @param position the item's position
     */
    public void setSelectedIndex(int position) {
        if (adapter != null) {
            if (position >= 0 && position <= adapter.getCount()) {
                adapter.setSelectedIndex(position);
                selectedIndex = position;
            } else {
                throw new IllegalArgumentException("Position must be lower than adapter count!");
            }
        }
    }

    public void setSpinnerTextFormatter(SpinnerTextFormatter spinnerTextFormatter) {
        this.spinnerTextFormatter = spinnerTextFormatter;
    }

    public void setSelectedTextFormatter(SpinnerTextFormatter textFormatter) {
        this.selectedTextFormatter = textFormatter;
    }

    private int getPopUpHeight() {
        return Math.max(verticalSpaceBelow(), verticalSpaceAbove());
    }

    private int verticalSpaceAbove() {
        return getParentVerticalOffset();
    }

    private int verticalSpaceBelow() {
        return displayHeight - getParentVerticalOffset() - getEstimatedHeight();
    }

    public void hideArrow() {
        isArrowHidden = true;
        setArrowDrawableOrHide(arrowDrawable);
    }

    public void showArrow() {
        isArrowHidden = false;
        setArrowDrawableOrHide(arrowDrawable);
    }

    public boolean isArrowHidden() {
        return isArrowHidden;
    }

    public void setDropDownListPaddingBottom(int paddingBottom) {
        dropDownListPaddingBottom = paddingBottom;
        initArrowDrawable();
    }

    public int getDropDownListPaddingBottom() {
        return dropDownListPaddingBottom;
    }

    private void initComponent() {
        setTextAlignment(TextAlignment.VERTICAL_CENTER);
        setTextColor(textColor);
        this.setTextSize(50);
      //  setBackground(ElementScatter.getInstance(context).parse(backgroundSelector));
        initArrowDrawable();
    }

    private void initArrowDrawable() {
        try {
            VectorElement vectorElement = new VectorElement(mContext, ResourceTable.Media_ic_arrow_drop_down_black_24);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setScale(HexToColor(arrowDrawableTint).getRed(),
                    HexToColor(arrowDrawableTint).getGreen(),
                    HexToColor(arrowDrawableTint).getBlue(), HexToColor(arrowDrawableTint).getAlpha());
            vectorElement.setColorMatrix(colorMatrix);
            setArrowDrawableOrHide(vectorElement);

        } catch (Exception e) {
           //Exception
        }
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

    private void setArrowDrawableOrHide(VectorElement vectorElement) {
        if (isArrowHidden) {
            setAroundElements(null, null, null, null);
        } else {
            setAroundElements(null, null, vectorElement, null);
        }
    }


    @Override
    public boolean onTouchEvent(Component component, TouchEvent event) {
        if (event.getAction() == TouchEvent.PRIMARY_POINT_UP) {
            if (isEnabled() && isClickable()) {
                showDropDown();
            }
        }
        return true;
    }

    /**
     * Set the dropdown items
     *
     * @param items A list of items
     */
    public void setItems(List<T> items) {
        if (selectedTextFormatter != null) {
            this.setText(selectedTextFormatter.format(items.get(0)));
        } else {
            this.setText((String) items.get(0));
        }
        this.items = items;
    }

    public void setTextCustomColor(Color textColor) {
        this.textColor = textColor;
        initComponent();
    }

    public void setHideArrow(boolean hideArrow) {
        this.isArrowHidden = hideArrow;
        initComponent();
    }

    public boolean getHideArrow() {
        return isArrowHidden;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        initComponent();
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundSelector(int backgroundSelector) {
        this.backgroundSelector = backgroundSelector;
        initComponent();
    }

    /**
     * Show the dropdown menu
     */
    public void showDropDown() {
        VectorElement vectorElement = null;
        try {
            vectorElement = new VectorElement(mContext, ResourceTable.Media_ic_arrow_drop_up_black_24);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setScale(HexToColor(arrowDrawableTint).getRed(),
                    HexToColor(arrowDrawableTint).getGreen(),
                    HexToColor(arrowDrawableTint).getBlue(), HexToColor(arrowDrawableTint).getAlpha());
            vectorElement.setColorMatrix(colorMatrix);
        } catch (Exception e) {
           //Exception
        }
        if (isArrowHidden) {
            setAroundElements(null, null, null, null);
        } else {
            setAroundElements(null, null, vectorElement, null);
        }


        new PopupCustomDialog.Builder(mContext)
                .items(items)
                .setSpinnerTextFormat(spinnerTextFormatter)
                .setPopupPaddingBottom(dropDownListPaddingBottom)
                .setCustomTextColor(textColor)
                .setBackgroundColor(backgroundColor)
                .setBackgroundSelecorColor(backgroundSelector)
                .setTextAlignment(horizontalAlignment)
                .itemsCallback(new PopupCustomDialog.ListCallback() {
                    @Override
                    public void onSelection(PopupCustomDialog dialog, Component view, int which, CharSequence text) {
                        setTextToSpinner(text.toString());
                        if (onSpinnerItemSelectedListener != null) {
                            onSpinnerItemSelectedListener.onItemSelected(NiceSpinner.this,view, which, text.toString());
                        }
                        dismissDropDown();
                    }
                })
                .show(this);
    }

    public void setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener onSpinnerItemSelectedListener) {
        this.onSpinnerItemSelectedListener = onSpinnerItemSelectedListener;
    }

    public interface OnItemSelectedListener<T> {

        /**
         * <p>Callback method to be invoked when an item in this view has been selected. This callback is invoked only when
         * the newly selected position is different from the previously selected position or if there was no selected
         * item.</p>
         *
         * @param view     The {@link NiceSpinner} view
         * @param position The position of the view in the adapter
         * @param text     The row text of the item that is selected
         */
        void onItemSelected(NiceSpinner view, int position, String text);
    }

    /**
     * Closes the dropdown menu
     */
    public void dismissDropDown() {
        VectorElement vectorElement = null;
        try {
            vectorElement = new VectorElement(mContext, ResourceTable.Media_ic_arrow_drop_down_black_24);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setScale(HexToColor(arrowDrawableTint).getRed(),
                    HexToColor(arrowDrawableTint).getGreen(),
                    HexToColor(arrowDrawableTint).getBlue(), HexToColor(arrowDrawableTint).getAlpha());
            vectorElement.setColorMatrix(colorMatrix);
        } catch (Exception e) {
           //Exception
        }
        if (isArrowHidden) {
            setAroundElements(null, null, null, null);
        } else {
            setAroundElements(null, null, vectorElement, null);
        }
    }

    private void setTextToSpinner(String message) {
        this.setText(message);
    }

}

