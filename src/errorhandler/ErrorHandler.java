package errorhandler;

import ast.types.ErrorType;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {
    private static ErrorHandler instance;

    private final List<ErrorType> errors = new ArrayList<>();


    private ErrorHandler() {

    }

    public static ErrorHandler getInstance() {
        if (instance == null) {
            instance = new ErrorHandler();
        }
        return instance;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ErrorType error : errors) {
             sb.append(error.toString()).append('\n');
        }
        return sb.toString();
    }

    /**
     * Adds an error to the list
     * @param error Error that is going to be added
     */
    public void addError(ErrorType error) {
        errors.add(error);
    }

    /**
     * Prints all the errors in the list
     * @param ps The print stream to print the errors
     */
    public void showErrors(PrintStream ps) {
        for (ErrorType error : errors) {
            ps.println(error.toString());
        }
    }

    /**
     * Returns whether there are any errors in the list
     * @return True if there is at least 1 error, false otherwise
     */
    public boolean anyError() {
        return !errors.isEmpty();
    }



}
