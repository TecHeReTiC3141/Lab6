package client.validators;

import client.Request;

public interface IValidator {

    Request validate(String command, String[] args);
}
