package org.vaadin.components;

import com.vaadin.data.HasValue;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import org.vaadin.layout.Metrics;
import org.vaadin.style.MaterialIcons;
import org.vaadin.style.Styles;

/**
 * Created by jonte on 29/08/2017.
 */
public class MDTextAreaBox extends CssLayout {

    private static final long serialVersionUID = 1L;

    private Label label = new Label();
    private Label icon = new Label();
    private CssLayout ripple = new CssLayout();
    private Label helper = new Label();
    private String helperText;
    private TextArea field = new TextArea() {
        @Override
        public void setComponentError(ErrorMessage componentError) {
            super.setComponentError(componentError);
            setError(componentError == null ? null : ((AbstractErrorMessage) componentError).getMessage());
        }
    };
    private int rows;

    public MDTextAreaBox(String label) {
        this(label, true);
    }

    public MDTextAreaBox(String label, boolean light) {
        String primaryStyleName = light ? Styles.TextFieldBoxes.LIGHT : Styles.TextFieldBoxes.DARK;
        setPrimaryStyleName(primaryStyleName);

        this.label.setValue(label);
        this.label.setPrimaryStyleName(primaryStyleName + "-label");
        this.label.addStyleName("hint");
        this.label.setWidthUndefined();

        this.icon.setPrimaryStyleName(primaryStyleName + "-icon");
        this.icon.setContentMode(ContentMode.HTML);
        this.icon.setVisible(false);

        this.ripple.setPrimaryStyleName(primaryStyleName + "-ripple");

        this.field.setPrimaryStyleName(primaryStyleName + "-input");
        this.field.addFocusListener(event -> {
            addStyleName("focus");
            this.label.removeStyleName("hint");
        });
        this.field.addBlurListener(event -> {
            removeStyleName("focus");
            if (field.getValue().isEmpty()) {
                this.label.addStyleName("hint");
            }
        });
        this.field.addValueChangeListener(event -> updateFloatingLabelPosition(event.getValue()));

        this.helper.setPrimaryStyleName(primaryStyleName + "-helper");
        this.helper.setWidthUndefined();

        addComponents(this.label, icon, field, ripple, this.helper);
    }

    public void setRows(int rows) {
        this.rows = rows;
        this.field.setHeight(Metrics.TextArea.BASE_HEIGHT + ((rows - 1) * Metrics.TextArea.LINE_HEIGHT), Unit.PIXELS);
        this.ripple.setHeight(Metrics.TextArea.BASE_HEIGHT + ((rows - 1) * Metrics.TextArea.LINE_HEIGHT), Unit.PIXELS);
    }

    public int getRows() {
        return rows;
    }

    public TextArea getField() {
        return field;
    }

    public String getLabel() {
        return label.getValue();
    }

    public void setLabel(String label) {
        this.label.setValue(label);
    }

    public String getHelper() {
        return this.helper.getValue();
    }

    public void setHelper(String text) {
        this.helperText = text;

        removeStyleName("error");
        this.helper.setValue(helperText);
    }

    public void setError(String text) {
        if (text == null) {
            removeStyleName("error");
            this.helper.setValue(helperText);
        } else {
            addStyleName("error");
            this.helper.setValue(text);
        }
    }

    public void setIcon(MaterialIcons icon) {
        if (icon == null) {
            this.icon.setVisible(false);
            removeStyleName("with-icon");
        } else {
            addStyleName("with-icon");
            this.icon.setVisible(true);
            this.icon.setValue(icon.getHtml());
        }
    }

    public Registration addValueChangeListener(HasValue.ValueChangeListener<String> listener) {
        return field.addValueChangeListener(listener);
    }

    @Override
    public void setComponentError(ErrorMessage componentError) {
        field.setComponentError(componentError);
    }

    public void setValue(String value) {
        this.field.setValue(value);
        updateFloatingLabelPosition(value);
    }

    private void updateFloatingLabelPosition(String value) {
        if (value == null || value.isEmpty()) {
            this.label.addStyleName("hint");
        } else {
            this.label.removeStyleName("hint");
        }
    }

}
