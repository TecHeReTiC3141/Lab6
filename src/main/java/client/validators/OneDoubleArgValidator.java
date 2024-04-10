package client.validators;

import common.Request;
import common.exceptions.WrongArgumentsException;

public class OneDoubleArgValidator extends BaseValidator {

    public Request validate(String command, String[] args, String username) {
        try {
            checkIfOneArgument(command, args);
            double firstArg = Double.parseDouble(args[0]);
            return super.validate(command, args, username);
        } catch (WrongArgumentsException  e) {
            System.out.println(e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Аргумент должен быть целым числом");
            return null;
        }
    }
}
