package client.validators;

import client.Request;
import common.exceptions.WrongArgumentsException;

public class OneDoubleArgValidator extends BaseValidator {

    public Request validate(String command, String[] args) {
        try {
            checkIfOneArgument(command, args);
            double firstArg = Double.parseDouble(args[0]);
            return super.validate(command, args);
        } catch (WrongArgumentsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
