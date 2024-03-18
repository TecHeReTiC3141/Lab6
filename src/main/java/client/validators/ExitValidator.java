package client.validators;

import client.Request;
import common.exceptions.ExitException;

public class ExitValidator extends BaseValidator {

    public Request validate(String command, String[] args) {
        throw new ExitException();
    }
}
