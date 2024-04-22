package ee.taltech.inbankbackend.validators;

import com.github.vladislavgoltjajev.personalcode.exception.PersonalCodeException;
import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeParser;
import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;
import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.endpoint.DecisionRequest;
import ee.taltech.inbankbackend.exceptions.*;

public class RequestValidator {

    static final EstonianPersonalCodeValidator validator = new EstonianPersonalCodeValidator();
    static final EstonianPersonalCodeParser parser = new EstonianPersonalCodeParser();


    private static int getCustomerExpectedAge() {
        int[] lifeExpectancies = {
                DecisionEngineConstants.LIFE_EXPECTANCY_ESTONIA,
                DecisionEngineConstants.LIFE_EXPECTANCY_LATVIA,
                DecisionEngineConstants.LIFE_EXPECTANCY_LITHUANIA
        };

        int total = 0;
        for (int expectancy : lifeExpectancies) {
            total += expectancy;
        }

        return total / lifeExpectancies.length;
    }

    /**
     * Verify that all inputs are valid according to business rules.
     * If inputs are invalid, then throws corresponding exceptions.
     *
     * @throws InvalidPersonalCodeException If the provided personal ID code is invalid
     * @throws InvalidLoanAmountException If the requested loan amount is invalid
     * @throws InvalidLoanPeriodException If the requested loan period is invalid
     */
    public static void verifyInputs(String personalCode, Long loanAmount, int loanPeriod)
            throws InvalidInputException {

        // Used to check for the validity of the presented ID code.
        if (!validator.isValid(personalCode)) {
            throw new InvalidPersonalCodeException("Invalid personal ID code!");
        }


        int customerAge = getCustomerAge(personalCode);
        int maxAllowedAge = getCustomerExpectedAge() - DecisionEngineConstants.MAXIMUM_LOAN_PERIOD / 12 ;
        if (!(DecisionEngineConstants.MINIMUM_CUSTOMER_AGE <= customerAge && maxAllowedAge >= customerAge)) {
            throw new InvalidAgeException("Restricted age");
        }

        if (!(DecisionEngineConstants.MINIMUM_LOAN_AMOUNT <= loanAmount)
                || !(loanAmount <= DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT)) {
            throw new InvalidLoanAmountException("Invalid loan amount!");
        }
        if (!(DecisionEngineConstants.MINIMUM_LOAN_PERIOD <= loanPeriod)
                || !(loanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD)) {
            throw new InvalidLoanPeriodException("Invalid loan period!");
        }

    }

    private static int getCustomerAge(String personalCode) throws InvalidAgeException {
        int age;
        try {
            age = parser.getAge(personalCode).getYears();
        } catch (PersonalCodeException e) {
            throw new InvalidAgeException("Could not extract age from personal code");
        }
        return age;
    }

}
