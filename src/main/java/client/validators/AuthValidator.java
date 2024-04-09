package client.validators;

import common.Request;
import common.exceptions.WrongArgumentsException;

public class AuthValidator extends BaseValidator {
    public static final String USERNAME_REGEXP = "[0-9A-Za-z]{3,12}";
    public static final String PASSWORD_REGEXP = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,30}";

    @Override
    public Request validate(String command, String[] args) {
        try {
            checkIfTwoArguments(command, args);
            String username = args[0];
            String password = args[1];
            if (!username.matches(USERNAME_REGEXP)) {
                throw new WrongArgumentsException("Username must be 3-12 characters long and contain only letters and digits");
            }
            if (!password.matches(PASSWORD_REGEXP)) {
                throw new WrongArgumentsException("Password must be 6-30 characters long and contain at least one digit, one lowercase letter, and one uppercase letter");
            }
            return super.validate(command, args);
        } catch (WrongArgumentsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
