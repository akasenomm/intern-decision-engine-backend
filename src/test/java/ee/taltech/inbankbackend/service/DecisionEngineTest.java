package ee.taltech.inbankbackend.service;

import com.github.vladislavgoltjajev.personalcode.exception.PersonalCodeException;
import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeParser;
import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;
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
    private String givenExamplePersonalCode;
    private String underagePersonalCode;
    private String overAgePersonalCode;


    @BeforeEach
    void setUp() {
        debtorPersonalCode = "37605030299";
        segment1PersonalCode = "50307172740";
        segment2PersonalCode = "38411266610";
        segment3PersonalCode = "35006069515";
        givenExamplePersonalCode = "50307172740";
        underagePersonalCode = "50907172747";
        overAgePersonalCode = "33411266610";
    }

    @Test
    void testTooOld() {
        assertThrows(InvalidAgeException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(overAgePersonalCode, 4000L, 12)));
    }

    @Test
    void testTooYoung() {
        assertThrows(InvalidAgeException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(underagePersonalCode, 4000L, 12)));
    }

    @Test
    void testSuggestHigherAmountThanRequested() throws DecisionEngineException {
        Decision decision = decisionEngine.calculateApprovedLoan(new DecisionRequest(segment2PersonalCode, 3000L, 60));
        assertEquals(10000, decision.getLoanAmount());
        assertEquals(60, decision.getLoanPeriod());
    }


    @Test
    void testDebtorPersonalCode() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(debtorPersonalCode, 4000L, 12)));
    }

    @Test
    void testSegment1PersonalCode() throws DecisionEngineException {
        Decision decision = decisionEngine.calculateApprovedLoan(new DecisionRequest(segment1PersonalCode, 4000L, 12));
        assertEquals(2000, decision.getLoanAmount());
        assertEquals(20, decision.getLoanPeriod());
    }

    @Test
    void testSegment2PersonalCode() throws DecisionEngineException {
        Decision decision = decisionEngine.calculateApprovedLoan(new DecisionRequest(segment2PersonalCode, 4000L, 12));
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testSegment3PersonalCode() throws DecisionEngineException {
        Decision decision = decisionEngine.calculateApprovedLoan(new DecisionRequest(segment3PersonalCode, 4000L, 12));
        assertEquals(10000, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testInvalidPersonalCode() {
        String invalidPersonalCode = "12345678901";
        assertThrows(InvalidPersonalCodeException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(invalidPersonalCode, 4000L, 12)));
    }

    @Test
    void testInvalidLoanAmount() {
        Long tooLowLoanAmount = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT - 1L;
        Long tooHighLoanAmount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT + 1L;

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(segment1PersonalCode, tooLowLoanAmount, 12)));

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(segment1PersonalCode, tooHighLoanAmount, 12)));
    }

    @Test
    void testInvalidLoanPeriod() {
        int tooShortLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD - 1;
        int tooLongLoanPeriod = DecisionEngineConstants.MAXIMUM_LOAN_PERIOD + 1;

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(segment1PersonalCode, 4000L, tooShortLoanPeriod)));

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(segment1PersonalCode, 4000L, tooLongLoanPeriod)));
    }

    @Test
    void testFindSuitableLoanPeriod() throws DecisionEngineException {
        Decision decision = decisionEngine.calculateApprovedLoan(new DecisionRequest(segment2PersonalCode, 2000L, 12));
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testNoValidLoanFound() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(debtorPersonalCode, 10000L, 60)));
    }

    @Test
    void testRestrictedAge() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(new DecisionRequest(debtorPersonalCode, 10000L, 60)));
    }

}

