package com.currencycloud.client;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.MatchRule;
import com.currencycloud.client.model.DetailedRate;
import com.currencycloud.client.model.Rate;
import com.currencycloud.client.model.Rates;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

public class RatesTest extends BetamaxTestSupport {

//    private String token = "bbdd421bdda373ea69670c9101fa9197";

    @Test
    @Betamax(tape = "can_find", match = {MatchRule.method, MatchRule.uri, MatchRule.body})
    public void testCanFind() throws Exception {
        Rates rates = client.findRates(Arrays.asList("GBPUSD", "EURGBP"), null);

        assertThat(rates, not(nullValue()));
        Collection<String> currencies = rates.getCurrencyPairs();
        assertThat(currencies, not(empty()));

        assertThat(currencies, hasSize(2));

        for (String cp : currencies) {
            Rate b = rates.getRate(cp);
            assertThat(b, is(not(Matchers.nullValue())));
        }

        String cp = currencies.iterator().next();
        Rate rate = rates.getRate(cp);
        assertThat(cp, equalTo("EURGBP"));
        assertThat(rate.getBid(), equalTo(new BigDecimal("0.71445")));
        assertThat(rate.getOffer(), equalTo(new BigDecimal("0.71508")));

        assertThat(rates.getUnavailable(), empty());
    }

    @Test
    @Betamax(tape = "can_provided_detailed_rate", match = {MatchRule.method, MatchRule.uri, MatchRule.body})
    public void testCanProvidedDetailedRate() throws Exception {
        DetailedRate detailedRate = client.detailedRates("GBP", "USD", "buy", new BigDecimal("10000"), null);

        assertThat(detailedRate.getClientSellAmount(), equalTo(new BigDecimal("15234.00")));
        assertThat(detailedRate.getSettlementCutOffTime(), equalTo(parseDateTime("2015-04-29T14:00:00Z")));
    }

}