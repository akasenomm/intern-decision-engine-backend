package ee.taltech.inbankbackend.config;
import ee.taltech.inbankbackend.endpoint.DecisionRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculator for life user life expectancy based on country and gender
 */
public class LifeExpectancyCalculator {

    public static Map<Country, Integer> maleLifeExpectancies = new HashMap<>();
    public static Map<Country, Integer> femaleLifeExpectancies = new HashMap<>();

    static {
        // Initialize
        maleLifeExpectancies.put(Country.ESTONIA, DecisionEngineConstants.LIFE_EXPECTANCY_ESTONIA_MALE);
        maleLifeExpectancies.put(Country.LATVIA, DecisionEngineConstants.LIFE_EXPECTANCY_LATVIA_MALE);
        maleLifeExpectancies.put(Country.LITHUANIA, DecisionEngineConstants.LIFE_EXPECTANCY_LITHUANIA_MALE);

        femaleLifeExpectancies.put(Country.ESTONIA, DecisionEngineConstants.LIFE_EXPECTANCY_ESTONIA_FEMALE);
        femaleLifeExpectancies.put(Country.LATVIA, DecisionEngineConstants.LIFE_EXPECTANCY_LATVIA_FEMALE);
        femaleLifeExpectancies.put(Country.LITHUANIA, DecisionEngineConstants.LIFE_EXPECTANCY_LITHUANIA_FEMALE);
    }

    /**
     * Returns customer expected life expectancy.
     * Male personal codes start with even number 1..3..5, female numbers start with 2..4..5
     * Check gender and return life expectancy from the right map.
     * @param request decision request
     * @return life expectancy as int
     */
    public static int getCustomerExpectedAge(DecisionRequest request) {
        Country countryEnum = Country.valueOf(request.getCountry());
        return request.getPersonalCode().charAt(0) % 2 == 0 ? femaleLifeExpectancies.get(countryEnum) : maleLifeExpectancies.get(countryEnum);
    }
}
