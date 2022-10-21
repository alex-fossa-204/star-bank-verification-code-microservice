package dev.alexfossa204.starbank.microservice.config.kafka;

public class KafkaConstant {

    public static final String PUBLISH_GENERATED_VERIFICATION_CODE = "publish_generated_verification_code";

    public static final String GENERATE_VERIFICATION_CODE_TOPIC = "generate_verification_code_topic";

    public static final String SET_VERIFICATION_CODE_AS_USED_TOPIC = "set_verification_code_as_used_topic";

    public static int DEFAULT_PARTITION_FACTOR = 1;

    public static short DEFAULT_REPLICATION_FACTOR = 1;

}
