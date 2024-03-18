package client.validators;

import client.Request;
import common.exceptions.WrongArgumentsException;

import java.io.FileReader;
import java.io.IOException;

public class ExecuteScriptValidator extends BaseValidator {
    public Request validate(String commandName, String[] args) {
        try {
            checkIfOneArgument(commandName, args);
            String filename = args[0];
            FileReader reader = new FileReader(filename);
            return new Request(commandName, args, null);
        } catch (WrongArgumentsException | IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
