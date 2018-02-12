/*
 * Copyright 2018. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.lambda.config;

import com.appdynamics.extensions.aws.config.Configuration;

import java.util.List;

/**
 * Created by aditya.jagtiani on 11/9/17.
 */
public class LambdaConfiguration extends Configuration {
    private List<String> includeLambdaFunctions;

    public List<String> getIncludeLambdaFunctions() {
        return this.includeLambdaFunctions;
    }

    public void setIncludeLambdaFunctions(List<String> includeLambdaFunctions) {
        this.includeLambdaFunctions = includeLambdaFunctions;
    }
}
