package de.dps.springbatch.model;

public class ErrorItem {

    private int errorId;
    private ErrorType errorType;
    private String description;
    private int occuredLineNumber;
    private String errLine;

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrLine() {
        return errLine;
    }

    public void setErrLine(String errLine) {
        this.errLine = errLine;
    }

    public int getOccuredLineNumber() {
        return occuredLineNumber;
    }

    public void setOccuredLineNumber(int occuredLineNumber) {
        this.occuredLineNumber = occuredLineNumber;
    }
}
