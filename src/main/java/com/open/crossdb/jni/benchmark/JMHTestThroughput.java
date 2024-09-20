//package com.open.crossdb.jni.benchmark;
//
//import com.open.crossdb.jni.api.IDB;
//import com.open.crossdb.jni.core.DBBuilder;
//import com.open.crossdb.jni.core.LoadLibraryUtils;
//import org.openjdk.jmh.annotations.*;
//import org.openjdk.jmh.runner.RunnerException;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Properties;
//import java.util.Random;
//import java.util.concurrent.TimeUnit;
//
//@State(Scope.Benchmark)
//@BenchmarkMode(Mode.Throughput)
//@OutputTimeUnit(TimeUnit.SECONDS)
//public class JMHTestThroughput {
//    public final static Properties properties = openProperties();
//
//    IDB db;
//
//    ITable<TestPO> table;
//    TestPO testInsertPO = new TestPO(62);
//    TestPO resultPO = new TestPO(62);
//
//    @Setup
//    public void setup() {
//        LoadLibraryUtils.loadLibrary("cdfdb");
//        //createdb
//        db = new DBBuilder()
//                .dbName("db1")
//                .dbPath("dev")
//                .properties(new Properties())
//                .build();
//
//        //createtable
//        table = db.createTable("test", "test");
//
//        ITransaction tx = db.begin();
//        for (int i = 0; i < 100000; i++) {
//            testInsertPO.setId(i);
//            testInsertPO.setAge(1);
//            testInsertPO.setSex(88);
////            testInsertPO.setName("xiaowang");
//
//            table.upsert(testInsertPO, tx);
//        }
//        tx.commit();
//    }
//
//    @Benchmark
//    public void upsertOne() {
//        Random random2 = new Random();
//        int random = random2.nextInt(10000);
//        int random1 = random2.nextInt(10000);
//        testInsertPO.setId((random));
//        testInsertPO.setAge(random1);
//        testInsertPO.setSex(random1);
////        testInsertPO.setName("xiaowang");
//
//        ITransaction tx = db.begin();
//        table.upsert(testInsertPO, tx);
//        tx.commit();
//    }
//    @Benchmark
//    public void selectByPKOne() {
//        int random = (int) (Math.random() * 10000 - 1);
//        resultPO.setId((random));
//        resultPO.setAge(0);
//
//        table.selectByPK(resultPO, null);
////        if (!resultPO.getName().equals("xiaowang")){
////            System.out.println("missed____________name________________");
////        }
//        if (resultPO.getAge() != 1  ){
//            System.out.println("missed_____________age_______________" + resultPO.getAge()+ ":" +resultPO.getName()+":"+resultPO.getSex() +"ID:" +resultPO.getId());
//        }
//    }
//
//
////    @Group("group1")
////    @GroupThreads(2)
////    @Benchmark
////    public void upsert() {
////
////        int random = (int) (Math.random() * 100000 - 1);
////        testInsertPO.setId((random));
////        testInsertPO.setAge(1);
////        testInsertPO.setSex(99);
//////        testInsertPO.setName("xiaowang");
////
////        ITransaction tx = db.begin();
////        table.upsert(testInsertPO, tx);
////        tx.commit();
////    }
////
////
////    @Group("group1")
////    @GroupThreads(8)
////    @Benchmark
////    public void selectByPK() {
////        int random = (int) (Math.random() * 10000 - 1);
////        resultPO.setId((random));
////
////
////        table.selectByPK(resultPO, null);
//////        if (!resultPO.getName().equals("xiaowang")){
//////            System.out.println("missed____________name________________");
//////        }
////        if (resultPO.getAge() != 1  ){
////            System.out.println("missed_____________age_______________" + resultPO.getAge()+ ":" +resultPO.getName()+":"+resultPO.getSex() +"ID:" +resultPO.getId());
////        }
////    }
//
//    public static void main(String[] args) throws RunnerException {
//
//        RunJMHUtils.run(properties, JMHTestThroughput.class);
//    }
//
//    private static Properties openProperties() {
//        Properties p = new Properties();
//        try {
//            p.load(Files.newInputStream(Paths.get("conf/jmh.properties")));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return p;
//    }
//}
