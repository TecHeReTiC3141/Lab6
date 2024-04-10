package client.validators;

import common.Request;
import common.exceptions.WrongArgumentsException;

public class AddValidator extends ReadValidator {

    @Override
    public Request validate(String command, String[] args, boolean parse, String username) {
        try {
            if (parse) {
                checkIfOneArgument(command, args);
            } else {
                checkIfNoArguments(command, args);
            }
            return super.validate(command, args, parse, username);
        } catch (WrongArgumentsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
