package lk.ijse.project_01.Util;

import javafx.scene.control.TextField;
import java.util.regex.Pattern;

public class Regex {

    public static boolean isTextMatched(Pattern pattern, TextField textField) {
        return pattern.matcher(textField.getText()).matches();
    }

    public static boolean setTextColor(Pattern pattern, TextField textField) {
        boolean matched = isTextMatched(pattern, textField);
        if (matched) {
            textField.setStyle("-fx-text-inner-color: green;");
        } else {
            textField.setStyle("-fx-text-inner-color: red;");
        }
        return matched;
    }

    public static Pattern getIdPattern() {
        return Pattern.compile("^(E)[0-9]{3}$");
    }

    public static Pattern getNamePattern() {
        return Pattern.compile("^[A-Za-z ]{3,}$");
    }

    public static Pattern getAddressPattern() {
        return Pattern.compile("^[A-Za-z0-9 ,]{4,}$");
    }

    public static Pattern getMobilePattern() {
        return Pattern.compile("^(07[0-9]{8})$");
    }

    public static Pattern getSalaryPattern() {
        return Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
    }

    public static final Pattern NAME = getNamePattern();
    public static final Pattern PASSWORD = Pattern.compile("^.{4,}$");
    public static final Pattern PHONENO = getMobilePattern();

    public static Pattern getPasswordPattern() {
        return Pattern.compile("^(E00-)[0-9]{3}$");
    }
}
