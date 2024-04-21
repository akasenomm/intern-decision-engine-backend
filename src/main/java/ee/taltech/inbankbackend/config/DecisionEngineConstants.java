package ee.taltech.inbankbackend.config;

/**
 * Holds all necessary constants for the decision engine.
 */
public class DecisionEngineConstants {
    public static final Integer MINIMUM_LOAN_AMOUNT = 2000;
    public static final Integer MAXIMUM_LOAN_AMOUNT = 10000;
    public static final Integer MAXIMUM_LOAN_PERIOD = 60;
    public static final Integer MINIMUM_LOAN_PERIOD = 12;
    public static final Integer SEGMENT_1_CREDIT_MODIFIER = 100;
    public static final Integer SEGMENT_2_CREDIT_MODIFIER = 300;
    public static final Integer SEGMENT_3_CREDIT_MODIFIER = 1000;
    public static final String ERROR_UNEXPECTED = "An unexpected error occurred";
    public static final int MINIMUM_CUSTOMER_AGE = 18;
    public static final int LIFE_EXPECTANCY_ESTONIA = 76;
    public static final int LIFE_EXPECTANCY_LATVIA = 73;
    public static final int LIFE_EXPECTANCY_LITHUANIA = 75;

}
