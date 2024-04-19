package ee.taltech.inbankbackend.builders;

import ee.taltech.inbankbackend.endpoint.DecisionResponse;

public class ResponseBuilder {

    private final DecisionResponse response;

    public ResponseBuilder() {
        this.response = new DecisionResponse();
    }

    public ResponseBuilder setLoanAmount(Integer loanAmount) {
        response.setLoanAmount(loanAmount);
        return this;
    }

    public ResponseBuilder setLoanPeriod(Integer loanPeriod) {
        response.setLoanPeriod(loanPeriod);
        return this;
    }

    public ResponseBuilder setErrorMessage(String errorMessage) {
        response.setErrorMessage(errorMessage);
        return this;
    }

    public DecisionResponse build() {
        return response;
    }
}
