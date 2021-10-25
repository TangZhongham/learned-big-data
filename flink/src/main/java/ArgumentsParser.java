import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;

public class ArgumentsParser {

    public static ArgumentParser argParser() {

        ArgumentParser parser = ArgumentParsers
                .newArgumentParser("Flink kafka Window Counting tool")
                .defaultHelp(true)
                .description("This tool is used to consumer from one kakfa topic and produce to another.\n" +
                        "ETL huawei test only");
//                .description("This tool is used to consumer from one kakfa topic and produce to another.\n" +
//                        "Counting nums of data during xxx seconds per window");
//                .description("This tool is used to consumer from one kakfa topic and produce to another.\n" +
//                        "Filter Strategy type < 1000 && id > -1");

        parser.addArgument("--job-name")
                .required(false)
                .type(String.class)
                .metavar("JOB_NAME")
                .dest("jobName")
                .help("Flink Job name");

        // bootstrap.servers
        parser.addArgument("--bootstrap-servers")
                .required(true)
                .type(String.class)
                .metavar("BOOTSTRAP_SERVERS")
                .dest("bootstrapServers")
                .help("consumer msg from this kafka group");

        parser.addArgument("--source-topic")
                .required(true)
                .type(String.class)
                .metavar("SOURCE_TOPIC")
                .dest("sourceTopic")
                .help("consumer msg from this topic");

        // group.id
        parser.addArgument("--group-id")
                .required(true)
                .type(String.class)
                .metavar("GROUP_ID")
                .dest("groupId")
                .help("consumer msg from this kafka group id");

        // broker.list
        parser.addArgument("--broker-list")
                .required(true)
                .type(String.class)
                .metavar("BROKER_LIST")
                .dest("brokerList")
                .help("produce msg to this kafka group");

        // --to-topic
        parser.addArgument("--to-topic")
                .required(true)
                .type(String.class)
                .metavar("TO_TOPIC")
                .dest("toTopic")
                .help("consumer msg to this topic");

//         --window-time
//        parser.addArgument("--window-time")
//                .required(true)
//                .type(String.class)
//                .metavar("WINDOW_TIME")
//                .dest("windowTime")
//                .help("tumbling window seconds, 1 s...2 s...");

        return parser;
    }
}
