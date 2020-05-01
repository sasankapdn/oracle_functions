package com.example.fn;
import com.fnproject.fn.testing.*;
import org.junit.*;
import static org.junit.Assert.*;

public class StateTaxCalcTest {

    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void calcTotalCost(){
        testing.givenEvent().withBody("{\"state\":\"Virginia\",\"price\":5.0}").enqueue();
        testing.thenRun(StateTaxCalc.class,"calcTotalCost");
        FnResult result = testing.getOnlyResult();
        assertEquals("{\"state\":\"Virginia\",\"price\":5.0,\"tax_rate\":0.043,\"tax\":0.22,\"total_cost\":5.22}", result.getBodyAsString());
    }



}