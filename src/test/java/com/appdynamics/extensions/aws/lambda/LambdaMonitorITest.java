package com.appdynamics.extensions.aws.lambda;

import static com.appdynamics.extensions.aws.util.AWSUtil.getAWSAccessKeysFromCfg;
import static com.appdynamics.extensions.aws.util.AWSUtil.getAWSSecretKeysFromCfg;
import static com.appdynamics.extensions.aws.util.AWSUtil.getConfigFilesFromDir;
import static org.junit.Assert.assertTrue;

import com.appdynamics.extensions.yml.YmlReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import org.junit.Test;
import com.appdynamics.extensions.aws.util.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;

public class LambdaMonitorITest {
	
	private LambdaMonitor classUnderTest = new LambdaMonitor();
	
	@Test
	public void testMetricsCollectionCredentialsEncrypted() throws Exception {
		Map<String, String> args = Maps.newHashMap();
		args.put("config-file","src/test/resources/conf/itest-encrypted-config.yaml");
		
		TaskOutput result = classUnderTest.execute(args, null);
		assertTrue(result.getStatusMessage().contains("successfully completed"));
	}
	
	@Test
	public void testMetricsCollectionWithProxy() throws Exception {
		Map<String, String> args = Maps.newHashMap();
		args.put("config-file","src/test/resources/conf/itest-proxy-config.yaml");
		
		TaskOutput result = classUnderTest.execute(args, null);
		assertTrue(result.getStatusMessage().contains("successfully completed"));
	}

	@Test
	public void testAccountKeysClearedFromConfig() {
		File[] configFiles = getConfigFilesFromDir("src/main/resources/conf");
		File[] configFilesForTests = getConfigFilesFromDir("src/test/resources/conf");

		verifyBlankKeys(getAWSSecretKeysFromCfg(configFiles));
		verifyBlankKeys(getAWSSecretKeysFromCfg(configFiles));
		verifyBlankKeys(getAWSAccessKeysFromCfg(configFilesForTests));
		verifyBlankKeys(getAWSSecretKeysFromCfg(configFilesForTests));
	}

	private void verifyBlankKeys(List<String> keys) {
		for(String key : keys) {
			assertTrue(key.equals(""));
		}
	}
}
