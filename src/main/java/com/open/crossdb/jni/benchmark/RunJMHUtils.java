package com.open.crossdb.jni.benchmark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public class RunJMHUtils {

    public static void run(Properties properties, Class clazz) throws RunnerException {
        ChainedOptionsBuilder builder = new OptionsBuilder().include(clazz.getSimpleName());

        // JMH Arg
        if ("true".equals(properties.getProperty("jmh.gc", "true"))){
            builder.addProfiler(GCProfiler.class);
        }
        builder.warmupIterations(Integer.parseInt(properties.getProperty("jmh.warmupIterations", "1")));
        builder.warmupTime(TimeValue.seconds(Integer.parseInt(properties.getProperty("jmh.warmupTime", "1"))));
        builder.measurementIterations(Integer.parseInt(properties.getProperty("jmh.measurementIterations", "1")));
        builder.measurementTime(TimeValue.seconds(Integer.parseInt(properties.getProperty("jmh.measurementTime", "1"))));
        builder.forks(Integer.parseInt(properties.getProperty("jmh.forks", "1")));
        builder.shouldDoGC(true);

        // JVM Arg
        builder.jvmArgs("-Xmx" + properties.getProperty("jvm.Xmx", "8g"),
                "-Xms" + properties.getProperty("jvm.Xms", "8g")
//                ,
//                "-XX:+PrintGCDetails", "-Xloggc:./gc.log",
//                "-XX:+HeapDumpBeforeFullGC",
//                "-XX:HeapDumpPath=./dump"
//                "-XX:+UseG1GC",
//                "-XX:+ParallelRefProcEnabled", "-XX:ParallelGCThreads=8","-XX:ConcGCThreads=2"
        );


        // Thread Arg
        builder.threads(Integer.parseInt(properties.getProperty("thread.size","1")));
        builder.param("dataSize", properties.getProperty("thread.dataSize","10000"));

        // Thread Group Arg
        String groupThreadSize = properties.getProperty("thread.groupThreadSize");
        if (groupThreadSize != null && !groupThreadSize.trim().equals("")){
            String[] groupThreadSizeStr = groupThreadSize.split(",");
            int[] groupThreadSizeInt = Arrays.stream(groupThreadSizeStr).mapToInt(Integer::parseInt).toArray();
            builder.threadGroups(groupThreadSizeInt);
        }

        // Data Arg
        builder.param("objectSmall",properties.getProperty("data.objectSmall","true"));
        builder.param("init",properties.getProperty("data.init","true"));

        // Arg End, start build
        Options options = builder.build();
        new Runner(options).run();
    }


    public static void main(String[] args) throws RunnerException {
        Properties properties = openProperties();
        run(properties, RunJMHUtils.class);
    }

    private static Properties openProperties() {
        Properties p = new Properties();
        try {
            p.load(Files.newInputStream(Paths.get("conf/jmh.properties")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return p;
    }

}
