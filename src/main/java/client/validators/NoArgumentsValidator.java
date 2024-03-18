package client.validators;

import client.Request;
import common.exceptions.WrongArgumentsException;

public class NoArgumentsValidator extends BaseValidator {

    public Request validate(String command, String[] args) {
        try {
            checkIfNoArguments(command, args);
            return super.validate(command, args);
        } catch (WrongArgumentsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
