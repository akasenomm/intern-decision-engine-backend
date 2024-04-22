package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.endpoint.DecisionRequest;
import ee.taltech.inbankbackend.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DecisionEngineTest {

    @InjectMocks
    private DecisionEngine decisionEngine;

    private String debtorPersonalCode;
    private String segment1PersonalCode;
    private String segment2PersonalCode;
    private String segment3PersonalCode;
    private String underagePersonalCode;
    private String overAgePersonalCode;


    @BeforeEach
    void setUp() {
        debtorPersonalCode = "37605030299";
        segment1PersonalCode = "50307172740";
        segment2PersonalCode = "38411266610";
        segment3PersonalCode = "39106069515";
        underagePersonalCode = "50907172747";
        overAgePersonalCode = "33411266610";
    }
    @Test
    void testTooOld() {
        assertThrows(InvalidAgeException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(overAgePersonalCode, 4000L, 12, "ESTONIA")));
    }

    @Test
    void testTooYoung() {
        assertThrows(InvalidAgeException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(underagePersonalCode, 4000L, 12, "ESTONIA")));
    }

    @Test
    void testSuggestHigherAmountThanRequested() throws DecisionEngineException {
        Decision decision = decisionEngine.calculateApprovedLoan(new DecisionRequest(segment2PersonalCode, 3000L, 60, "ESTONIA"));
        assertEquals(10000, decision.getLoanAmount());
        assertEquals(60, decision.getLoanPeriod());
    }

    @Test
    void test() throws InvalidInputException, NoValidLoanException {
        Decision decision = decisionEngine.calculateApprovedLoan(new DecisionRequest("50211022736", 3000L, 40, "LATVIA"));
        System.out.println(decision.getLoanAmount());
        System.out.println(decision.getLoanPeriod());
        System.out.println(decision.getErrorMessage());
    }


    @Test
    void testDebtorPersonalCode() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(debtorPersonalCode, 3000L, 40, "LATVIA")));
    }

    @Test
    void testSegment1PersonalCode() throws DecisionEngineException {
        Decision decision = decisionEngine.calculateApprovedLoan(new DecisionRequest(segment1PersonalCode, 4000L, 12, "ESTONIA"));
        assertEquals(2000, decision.getLoanAmount());
        assertEquals(20, decision.getLoanPeriod());
    }

    @Test
    void testSegment2PersonalCode() throws DecisionEngineException {
        Decision decision = decisionEngine.calculateApprovedLoan(new DecisionRequest(segment2PersonalCode, 4000L, 12, "ESTONIA"));
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testInvalidPersonalCode() {
        String invalidPersonalCode = "12345678901";
        assertThrows(InvalidPersonalCodeException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(invalidPersonalCode, 3000L, 40, "LATVIA")));
    }

    @Test
    void testInvalidLoanAmount() {
        Long tooLowLoanAmount = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT - 1L;
        Long tooHighLoanAmount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT + 1L;

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(segment1PersonalCode, tooLowLoanAmount, 12, "LATVIA")));

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(segment1PersonalCode, tooHighLoanAmount, 12, "LATVIA")));
    }

    @Test
    void testInvalidLoanPeriod() {
        int tooShortLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD - 1;
        int tooLongLoanPeriod = DecisionEngineConstants.MAXIMUM_LOAN_PERIOD + 1;

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(segment1PersonalCode, 4000L, tooShortLoanPeriod, "LATVIA")));

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(segment1PersonalCode, 4000L, tooLongLoanPeriod, "LATVIA")));
    }

    @Test
    void testFindSuitableLoanPeriod() throws DecisionEngineException {
        Decision decision = decisionEngine.calculateApprovedLoan(new DecisionRequest(segment2PersonalCode, 2000L, 12, "LATVIA"));
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testNoValidLoanFound() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(debtorPersonalCode, 10000L, 60, "LATVIA")));
    }

}

