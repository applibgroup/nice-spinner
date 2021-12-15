package org.angmarch.views;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;

public interface OnSpinnerItemSelectedListener {
    void onItemSelected(NiceSpinner parent, Component view, int position, CharSequence text);
}
