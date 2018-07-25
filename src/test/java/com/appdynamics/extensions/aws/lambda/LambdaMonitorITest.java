/*
 * Copyright 2018. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.lambda;

import com.google.common.collect.Maps;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class LambdaMonitorITest {

    private LambdaMonitor classUnderTest = new LambdaMonitor();

    @Test
    public void testMetricsCollectionCredentialsEncrypted() throws Exception {
        Map<String, String> args = Maps.newHashMap();
        args.put("config-file", "src/test/resources/conf/itest-encrypted-config.yml");

        TaskOutput result = classUnderTest.execute(args, null);
        assertTrue(result.getStatusMessage().contains(String.format("Monitor {} completes.")));
    }

    @Test
    public void testMetricsCollectionWithProxy() throws Exception {
        Map<String, String> args = Maps.newHashMap();
        args.put("config-file", "src/test/resources/conf/itest-proxy-config.yml");

        TaskOutput result = classUnderTest.execute(args, null);
        assertTrue(result.getStatusMessage().contains(String.format("Monitor {} completes.")));
    }

    @Test
    public void testAccountKeysClearedFromConfig() {
        Yaml yaml = new Yaml();
        Map<String, ?> configMap = new HashMap<>();
        try {
            configMap = (Map<String, ?>) yaml.load(new FileReader(new File("src/main/resources/conf/config.yml")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        verifyBlankKeys((List) configMap.get("accounts"));
    }

    private void verifyBlankKeys(List accountKeys) {
        Map<String, String> accountKeysMap = (Map<String, String>) (accountKeys.get(0));
        for (Map.Entry<String, String> entry : accountKeysMap.entrySet()) {
            if (!entry.getKey().equals("regions"))
                assertTrue(!entry.getValue().equals(""));
        }
    }
}
