package com.netflix.config.sources;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.config.PollResult;
import com.netflix.config.PolledConfigurationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DynamoDbContextualConfigurationSource extends AbstractDynamoDbConfigurationSource<Object> implements PolledConfigurationSource {
    private static final Logger log = LoggerFactory.getLogger(DynamoDbConfigurationSource.class);
    private static final String evaluationAttributePropertyName = "com.netflix.config.dynamo.evaluationAttributeName";
    private static final String evaluationAttributeNameDefault = "evaluationType";

    private final DynamicStringProperty evaluationAttributeName = DynamicPropertyFactory.getInstance()
            .getStringProperty(evaluationAttributePropertyName, evaluationAttributeNameDefault);

    public DynamoDbContextualConfigurationSource() {
        super();
    }

    public DynamoDbContextualConfigurationSource(ClientConfiguration clientConfiguration) {
        super(clientConfiguration);
    }

    public DynamoDbContextualConfigurationSource(AWSCredentials credentials) {
        super(credentials);
    }

    public DynamoDbContextualConfigurationSource(AWSCredentials credentials, ClientConfiguration clientConfiguration) {
        super(credentials, clientConfiguration);
    }

    public DynamoDbContextualConfigurationSource(AWSCredentialsProvider credentialsProvider) {
        super(credentialsProvider);
    }

    public DynamoDbContextualConfigurationSource(AWSCredentialsProvider credentialsProvider, ClientConfiguration clientConfiguration) {
        super(credentialsProvider, clientConfiguration);
    }

    public DynamoDbContextualConfigurationSource(AmazonDynamoDB dbClient) {
        super(dbClient);
    }

    @Override
    protected synchronized Map<String, Object> loadPropertiesFromTable(String table) {
        Map<String, Object> propertyMap = new HashMap<String, Object>();
        Key lastKeyEvaluated = null;
        do {
            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(table)
                    .withExclusiveStartKey(lastKeyEvaluated);
            ScanResult result = dbClient.scan(scanRequest);
            for (Map<String, AttributeValue> item : result.getItems()) {
                EvaluationTypes evalType = EvaluationTypes.valueOf(item.get(evaluationAttributeName.get()).getS().toUpperCase());
                String propertyName = item.get(keyAttributeName.get()).getS();
                String propertyValue = item.get(valueAttributeName.get()).getS();

                switch (evalType) {
                    case EAGER:
                        try {
                            propertyMap.put(propertyName, evaluatePropertyValue(propertyValue));
                        } catch (Exception ex) {
                            log.warn("Property: " + propertyName + " could not be evaluated and was skipped", ex);
                        }
                        break;
                    case LAZY:
                        propertyMap.put(propertyName, propertyValue);
                        break;
                    default:
                        log.warn("Property: {} with value: {} does not have a valid evaluation type: {}",
                                new Object[]{propertyName, propertyValue, evalType});
                        break;
                }

            }
            lastKeyEvaluated = result.getLastEvaluatedKey();
        } while (lastKeyEvaluated != null);
        return propertyMap;
    }

    @Override
    public PollResult poll(boolean initial, Object checkPoint) throws Exception {
        String table = tableName.get();
        Map<String, Object> map = loadPropertiesFromTable(table);
        log.info("Successfully polled Dynamo for a new configuration based on table:" + table);
        return PollResult.createFull(map);
    }

    private String evaluatePropertyValue(String jsonValue) {
        //TODO actually evaluate the property
        return "";
    }

    public enum EvaluationTypes {
        EAGER,
        LAZY;
    }
}
