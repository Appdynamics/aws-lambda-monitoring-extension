package com.appdynamics.extensions.aws.lambda;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.services.cloudwatch.model.Metric;
import com.appdynamics.extensions.aws.config.Dimension;
import com.appdynamics.extensions.aws.config.IncludeMetric;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.concurrent.atomic.LongAdder;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.apache.*, javax.xml.*"})
public class LambdaMetricsProcessorTest {

    @Mock
    AmazonCloudWatch amazonCloudWatch;

    @Mock
    ListMetricsResult listMetricsResult;


    private LambdaMetricsProcessor lambdaMetricsProcessor = new LambdaMetricsProcessor(new ArrayList<IncludeMetric>(), new ArrayList<Dimension>());

    @Before
    public void init() throws Exception {
        Mockito.when(amazonCloudWatch.listMetrics(Mockito.any(ListMetricsRequest.class))).thenReturn(listMetricsResult);
        Mockito.when(listMetricsResult.getMetrics()).thenReturn(new ArrayList<Metric>());
    }

    @Test
    public void testGetMetrics() {
        Assert.assertNotNull(lambdaMetricsProcessor.getMetrics(amazonCloudWatch, null, new LongAdder()));
    }

}
