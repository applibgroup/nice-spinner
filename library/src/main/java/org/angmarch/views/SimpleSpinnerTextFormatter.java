package org.angmarch.views;

public class SimpleSpinnerTextFormatter implements SpinnerTextFormatter {

    @Override
    public String format(Object item) {
        return new String(item.toString());
    }
}
