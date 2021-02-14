package app.utils;

import javafx.scene.control.Alert;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    /**
     * This class provides various helper methods which will me used
     * throughout the entire application.
     * @author      Sajmir Doko
     * @since       1.0.0
    */
public class HelperMethods {

    /**
     * This method is used to validate a string if it is an email address
     * or not.
     * {@link}              https://stackoverflow.com/a/8204716
     * @param emailStr      Accepts an string to validate as email address.
     * @return boolean      Returns true or false.
     * @author              Sajmir Doko
     * @since               1.0.0
     */
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(emailStr);
        return matcher.find();
    }

    /**
     * This method is used to create an alertBox.
     * @param infoMessage   First parameter is a string to be used as ContentText.
     * @param headerText    First parameter is a string to be used as HeaderText.
     * @param title         Third parameter is a string to be used as alertBox title.
     * @author              Sajmir Doko
     * @since               1.0.0
     */
    public static void alertBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(String.valueOf(infoMessage));
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    /**
     * This method is used to validate a string if it complies with
     * required password specifications or not.
     * {@link}              https://stackoverflow.com/a/3802238
     * @param password      Accepts a string to validate as password.
     * @return boolean      Returns true or false.
     * @author              Sajmir Doko
     * @since               1.0.0
     */
    public static boolean passwordValidation(String password){
//        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
//        ^                 # start-of-string
//        (?=.*[0-9])       # a digit must occur at least once
//        (?=.*[a-z])       # a lower case letter must occur at least once
//        (?=.*[A-Z])       # an upper case letter must occur at least once
//        (?=.*[@#$%^&+=])  # a special character must occur at least once
//        (?=\S+$)          # no whitespace allowed in the entire string
//        .{8,}             # anything, at least eight places though
//        $                 # end-of-string
        return password.matches("^.{6,16}$");
    }

}
