package client.validators;

import common.Request;
import common.exceptions.WrongArgumentsException;

public class NoArgumentsValidator extends BaseValidator {

    public Request validate(String command, String[] args, String username) {
        try {
            checkIfNoArguments(command, args);
            return super.validate(command, args, username);
        } catch (WrongArgumentsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
