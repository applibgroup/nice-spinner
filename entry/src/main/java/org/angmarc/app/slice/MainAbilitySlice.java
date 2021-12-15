package org.angmarc.app.slice;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import org.angmarc.app.ResourceTable;
import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.angmarch.views.SpinnerTextFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainAbilitySlice extends AbilitySlice {
    private ToastDialog toastDialog;
    private NiceSpinner spinner;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        setupDefault();
        setupTintedWithCustomClass();
    }

    private void setupDefault() {
        spinner = (NiceSpinner) findComponentById(ResourceTable.Id_nice_spinner);
        List<String> dataset = new LinkedList<String>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        spinner.setItems(dataset);
        spinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, Component view, int position, CharSequence text) {
                showToast("Clicked: " + text);
            }
        });
    }

    private void setupTintedWithCustomClass() {
        final NiceSpinner spinner = (NiceSpinner)findComponentById(ResourceTable.Id_tinted_nice_spinner);
        List<Person> people = new ArrayList<>();

        people.add(new Person("Tony", "Stark"));
        people.add(new Person("Steve", "Rogers"));
        people.add(new Person("Bruce", "Banner"));

        SpinnerTextFormatter textFormatter = new SpinnerTextFormatter<Person>() {
            @Override
            public String format(Person person) {
                return new String(person.getName() + " " + person.getSurname());
            }
        };

        spinner.setSpinnerTextFormatter(textFormatter);
        spinner.setSelectedTextFormatter(textFormatter);
        spinner.setBackgroundSelector(ResourceTable.Graphic_selectdialog_changecolor);
        spinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {

            @Override
            public void onItemSelected(NiceSpinner parent, Component view, int position, CharSequence text) {
                showToast("Clicked: " + text);
            }
        });

        spinner.setItems(people);
    }

    private void showToast(String message) {
        if (toastDialog != null) {
            toastDialog.cancel();
            toastDialog = null;
        }
        toastDialog = new ToastDialog(this);
        ComponentContainer componentContainer = (ComponentContainer) LayoutScatter.getInstance(this).parse(ResourceTable.Layout_item_toast, null, false);
        Text messageTV = (Text) componentContainer.findComponentById(ResourceTable.Id_toast_text);
        DependentLayout background = (DependentLayout) componentContainer.findComponentById(ResourceTable.Id_background);
        messageTV.setText(message);
        ShapeElement infoShapeElement = new ShapeElement();
        infoShapeElement.setRgbColor(new RgbColor(163, 194, 194));
        background.setBackground(infoShapeElement);

        toastDialog.setContentCustomComponent(componentContainer);
        toastDialog.setAlignment(TextAlignment.BOTTOM);
        toastDialog.setSize(1000, 140).setDuration(500).show();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}

class Person {

    private String name;
    private String surname;

    Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    String getName() {
        return name;
    }

    String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}