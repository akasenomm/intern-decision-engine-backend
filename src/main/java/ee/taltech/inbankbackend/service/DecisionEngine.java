package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.endpoint.DecisionRequest;
import ee.taltech.inbankbackend.exceptions.*;
import ee.taltech.inbankbackend.validators.RequestValidator;
import org.springframework.stereotype.Service;

/**
 * A service class that provides a method for calculating an approved loan amount and period for a customer.
 * The loan amount is calculated based on the customer's credit modifier,
 * which is determined by the last four digits of their ID code.
 */
@Service
public class DecisionEngine {
    private int modifier;

    /**
     * Calculates the maximum loan amount and period for the customer based on their ID code,
     * the requested loan amount, loan period, and customer country.
     * The loan period must be between 12 and 60 months (inclusive).
     * The loan amount must be between 2000 and 10000€ months (inclusive).
     *
     * @return A Decision object containing the approved loan amount and period, and an error message (if any)
     * @throws NoValidLoanException If there is no valid loan found for the given ID code, loan amount and loan period
     * @throws InvalidInputException If any user input is invalid
     */

    public Decision calculateApprovedLoan(DecisionRequest request) throws NoValidLoanException, InvalidInputException {
        RequestValidator.verifyInputs(request);
        modifier = calculateModifier(request.getPersonalCode());
        int outputLoanPeriod = adjustLoanPeriod(request.getLoanPeriod());
        int outputLoanAmount = calculateOutputLoanAmount(outputLoanPeriod);
        return new Decision(outputLoanAmount, outputLoanPeriod, null);
    }

    /**
     * Calculates the credit modifier of the customer to according to the last four digits of their ID code.
     * Debt - 0000...2499
     * Segment 1 - 2500...4999
     * Segment 2 - 5000...7499
     * Segment 3 - 7500...9999
     *
     * @return Segment to which the customer belongs.
     */
    public int calculateModifier(String personalCode) throws NoValidLoanException {
        int segment = Integer.parseInt(personalCode.substring(personalCode.length() - 4));

        if (segment < 2500) {
            throw new NoValidLoanException("No valid loan for debtor");
        } else if (segment < 5000) {
            return DecisionEngineConstants.SEGMENT_1_CREDIT_MODIFIER;
        } else if (segment < 7500) {
            return DecisionEngineConstants.SEGMENT_2_CREDIT_MODIFIER;
        }

        return DecisionEngineConstants.SEGMENT_3_CREDIT_MODIFIER;
    }

    /**
     * Calculates loan period for the customer to satisfy minimum and maximum loan amounts
     * @param loanPeriod requested loan period
     * @return suggested loan period
     * @throws NoValidLoanException can not find loan within allowed period
     */
    private int adjustLoanPeriod(int loanPeriod) throws NoValidLoanException {
        if (highestValidLoanAmount(loanPeriod) < DecisionEngineConstants.MINIMUM_LOAN_AMOUNT) {
            loanPeriod = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT / modifier;
        }

        if (loanPeriod > DecisionEngineConstants.MAXIMUM_LOAN_PERIOD) {
            throw new NoValidLoanException("No valid loan found!");
        }
        return loanPeriod;
    }

    /**
     * Simple function to satisfy maximum loan restriction. Returns whichever is lower
     * @param loanPeriod Allowed period
     * @return Calculates max sum based on allowed period. If sum higher than restriction, returns limit.
     */
    private int calculateOutputLoanAmount(int loanPeriod) {
        return Math.min(DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT, highestValidLoanAmount(loanPeriod));
    }

    /**
     * Calculates the largest valid loan for the current credit modifier and loan period.
     *
     * @return Largest valid loan amount
     */
    private int highestValidLoanAmount(int loanPeriod) {
        return modifier * loanPeriod;
    }

}
