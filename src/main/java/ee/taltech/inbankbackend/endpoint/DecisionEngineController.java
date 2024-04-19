package ee.taltech.inbankbackend.endpoint;

import ee.taltech.inbankbackend.builders.ResponseBuilder;
import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.*;
import ee.taltech.inbankbackend.service.Decision;
import ee.taltech.inbankbackend.service.DecisionEngine;
import ee.taltech.inbankbackend.validators.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
@CrossOrigin
public class DecisionEngineController {

    private final DecisionEngine decisionEngine;
    private final DecisionResponse response;

    @Autowired
    DecisionEngineController(DecisionEngine decisionEngine, DecisionResponse response) {
        this.decisionEngine = decisionEngine;
        this.response = response;
    }

    /**
     * A REST endpoint that handles requests for loan decisions.
     * The endpoint accepts POST requests with a request body containing the customer's personal ID code,
     * requested loan amount, and loan period.<br><br>
     * - If the loan amount or period is invalid, the endpoint returns a bad request response with an error message.<br>
     * - If the personal ID code is invalid, the endpoint returns a bad request response with an error message.<br>
     * - If an unexpected error occurs, the endpoint returns an internal server error response with an error message.<br>
     * - If no valid loans can be found, the endpoint returns a not found response with an error message.<br>
     * - If a valid loan is found, a DecisionResponse is returned containing the approved loan amount and period.
     *
     * @param request The request body containing the customer's personal ID code, requested loan amount, and loan period
     * @return A ResponseEntity with a DecisionResponse body containing the approved loan amount and period, and an error message (if any)
     */
    @PostMapping("/decision")
    public ResponseEntity<DecisionResponse> requestDecision(@RequestBody DecisionRequest request) {
        try {
            return processDecisionRequest(request);
        } catch (InvalidInputException e) {
            return handleBadRequest(e.getMessage());
        } catch (NoValidLoanException e) {
            return handleNotFound(e.getMessage());
        } catch (Exception e) {
            return handleInternalServerError();
        }
    }

    private ResponseEntity<DecisionResponse> processDecisionRequest(DecisionRequest request) throws InvalidInputException, NoValidLoanException {
        Decision decision = decisionEngine.calculateApprovedLoan(request.getPersonalCode(), request.getLoanAmount(), request.getLoanPeriod());
        DecisionResponse response = buildResponse(decision);
        return ResponseEntity.ok(response);
    }

    private DecisionResponse buildResponse(Decision decision) {
        return new ResponseBuilder()
                .setLoanAmount(decision.getLoanAmount())
                .setLoanPeriod(decision.getLoanPeriod())
                .setErrorMessage(decision.getErrorMessage())
                .build();
    }

    private ResponseEntity<DecisionResponse> handleBadRequest(String errorMessage) {
        DecisionResponse response = buildErrorResponse(errorMessage);
        return ResponseEntity.badRequest().body(response);
    }

    private ResponseEntity<DecisionResponse> handleNotFound(String errorMessage) {
        DecisionResponse response = buildErrorResponse(errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    private ResponseEntity<DecisionResponse> handleInternalServerError() {
        DecisionResponse response = buildErrorResponse(DecisionEngineConstants.ERROR_UNEXPECTED);
        return ResponseEntity.internalServerError().body(response);
    }

    private DecisionResponse buildErrorResponse(String errorMessage) {
        return new ResponseBuilder()
                .setErrorMessage(errorMessage)
                .build();
    }

}
