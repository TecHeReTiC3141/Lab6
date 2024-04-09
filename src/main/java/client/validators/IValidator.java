package client.validators;

import common.Request;

public interface IValidator {

    Request validate(String command, String[] args);
}
