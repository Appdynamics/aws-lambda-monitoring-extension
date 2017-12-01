package com.appdynamics.extensions.aws.lambda;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.DimensionFilter;
import com.amazonaws.services.cloudwatch.model.Metric;
import com.appdynamics.extensions.aws.config.MetricType;
import com.appdynamics.extensions.aws.metric.NamespaceMetricStatistics;
import com.appdynamics.extensions.aws.metric.StatisticType;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessor;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessorHelper;

import java.util.*;
import java.util.regex.Pattern;

import static com.appdynamics.extensions.aws.lambda.util.Constants.DIMENSIONS;
import static com.appdynamics.extensions.aws.lambda.util.Constants.LAMBDA_DIMENSION_VALUE;
import static com.appdynamics.extensions.aws.lambda.util.Constants.NAMESPACE;

/**
 * @author Satish Muddam
 */
public class LambdaMetricsProcessor implements MetricsProcessor {

    private List<MetricType> metricTypes;
    private Pattern excludeMetricsPattern;
    private List<String> includeLambdaFunctions;

    public LambdaMetricsProcessor(List<MetricType> metricTypes,
                                  Set<String> excludeMetrics, List<String> includeLambdaFunctions) {
        this.metricTypes = metricTypes;
        this.excludeMetricsPattern = MetricsProcessorHelper.createPattern(excludeMetrics);
        this.includeLambdaFunctions = includeLambdaFunctions;
    }

    public List<Metric> getMetrics(AmazonCloudWatch awsCloudWatch, String accountName) {
        List<DimensionFilter> dimensions = new ArrayList<DimensionFilter>();

        DimensionFilter dimensionFilter = new DimensionFilter();
        dimensionFilter.withName(DIMENSIONS);

        dimensions.add(dimensionFilter);

        LambdaFunctionMatcherPredicate predicate = new LambdaFunctionMatcherPredicate(includeLambdaFunctions);
        return MetricsProcessorHelper.getFilteredMetrics(awsCloudWatch,
                NAMESPACE,
                excludeMetricsPattern,
                dimensions,
                predicate);
    }

    public StatisticType getStatisticType(Metric metric) {
        return MetricsProcessorHelper.getStatisticType(metric, metricTypes);
    }

    public Map<String, Double> createMetricStatsMapForUpload(NamespaceMetricStatistics namespaceMetricStats) {
        Map<String, String> dimensionToMetricPathNameDictionary = new HashMap<String, String>();
        dimensionToMetricPathNameDictionary.put(DIMENSIONS, LAMBDA_DIMENSION_VALUE);


        return MetricsProcessorHelper.createMetricStatsMapForUpload(namespaceMetricStats,
                dimensionToMetricPathNameDictionary, false);
    }

    public String getNamespace() {
        return NAMESPACE;
    }

}
