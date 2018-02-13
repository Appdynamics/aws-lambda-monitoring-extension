/*
 * Copyright 2018. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.lambda;

import com.amazonaws.services.cloudwatch.model.Metric;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import java.util.List;

/**
 * Created by aditya.jagtiani on 11/9/17.
 */
public class LambdaFunctionMatcherPredicate implements Predicate<Metric> {

    private List<String> includeLambdaFunctions;
    private Predicate<CharSequence> patternPredicate;

    public LambdaFunctionMatcherPredicate(List<String> includeLambdaFunctions) {
        this.includeLambdaFunctions = includeLambdaFunctions;
        build();
    }

    private void build() {
        if (includeLambdaFunctions != null && !includeLambdaFunctions.isEmpty()) {
            for (String pattern : includeLambdaFunctions) {
                Predicate<CharSequence> charSequencePredicate = Predicates.containsPattern(pattern);
                if (patternPredicate == null) {
                    patternPredicate = charSequencePredicate;
                } else {
                    patternPredicate = Predicates.or(patternPredicate, charSequencePredicate);
                }
            }
        }
    }

    public boolean apply(Metric metric) {
        String lambdaFunctionIdentifier = metric.getDimensions().get(0).getValue();
        return patternPredicate.apply(lambdaFunctionIdentifier);
    }
}
