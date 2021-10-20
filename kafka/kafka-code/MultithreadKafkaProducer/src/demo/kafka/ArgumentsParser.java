package demo.kafka;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;

import static net.sourceforge.argparse4j.impl.Arguments.store;

public class ArgumentsParser {

    public static ArgumentParser argParser() {

        ArgumentParser parser = ArgumentParsers
                .newArgumentParser("Kafka data generator, Kafka 造数脚本\n")
                .defaultHelp(true)
                .description("This tool is used for producing Kafka msgs during huawei test.\n" +
                        "Message type: id,name,type,unix-time");

        // bootstrap.servers
        parser.addArgument("--bootstrap-servers")
                .required(true)
                .type(String.class)
                .metavar("BOOTSTRAP_SERVERS")
                .dest("BOOTSTRAP_SERVERS")
                .help("kafka bootstrap-server, multi-proved");

        // -topic
        parser.addArgument("--topic")
                .required(true)
                .type(String.class)
                .metavar("TOPIC")
                .dest("Topic")
                .help("Produce msg to this topic");
//        // broker.list
//        parser.addArgument("--broker-list")
//                .required(true)
//                .type(String.class)
//                .metavar("BROKER_LIST")
//                .dest("brokerList")
//                .help("produce msg to this kafka group");

        // TOPIC Args, 消息数量、消息格式（时间戳还是unix)
        parser.addArgument("--msg-num")
                .required(false)
                .type(String.class)
                .metavar("MSG_NUM")
                .dest("msg_num")
                .setDefault(100000000)
                .help("produce xx nums of msg to this topic, default 1e");

        parser.addArgument("--time-type")
                .required(false)
                .type(String.class)
                .metavar("TIME_TYPE")
                .dest("time_type")
                .setDefault("unix-time")
                .help("unix-time or timestamp, default unix-time, timestamps like: \"yyyy-MM-dd HH:mm:ss\" or \"MM/dd/yyyy HH:mm:ss\" ");

        parser.addArgument("--thread-num")
                .required(false)
                .type(Integer.class)
                .metavar("THREAD_NUM")
                .dest("thread_num")
                .setDefault(10)
                .help("use num of thread to produce.");

        parser.addArgument("--producer.config")
                .action(store())
                .required(false)
                .type(String.class)
                .metavar("CONFIG-FILE")
                .dest("producerConfigFile")
                .help("producer config properties file.");

        return parser;
    }
}

