package com.example.demo.redis.com.example.demo.redis;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Zookeeper config sender for demo
 *
 * @author guonanjun
 */
public class ZookeeperConfigSender {

    private static final int RETRY_TIMES = 3;
    private static final int SLEEP_TIME = 1000;

    public static void main(String[] args) throws Exception {
        final String remoteAddress = "localhost:2181";
        final String groupId = "Sentinel";
        final String dataId = "my-demo";
        final String rule = "[\n"
                + "  {\n"
                + "    \"resource\": \"my-demo\",\n"
                + "    \"controlBehavior\": 0,\n"
                + "    \"count\": 2.0,\n"
                + "    \"grade\": 1,\n"
                + "    \"limitApp\": \"default\",\n"
                + "    \"strategy\": 0\n"
                + "  }\n"
                + "]";

        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(remoteAddress, new ExponentialBackoffRetry(SLEEP_TIME, RETRY_TIMES));
        zkClient.start();
        String path = getPath(groupId, dataId);
        Stat stat = zkClient.checkExists().forPath(path);
        if (stat == null) {
            zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, null);
        }
        zkClient.setData().forPath(path, rule.getBytes());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        zkClient.close();
    }

    private static String getPath(String groupId, String dataId) {
        String path = "";
        if (groupId.startsWith("/")) {
            path += groupId;
        } else {
            path += "/" + groupId;
        }
        if (dataId.startsWith("/")) {
            path += dataId;
        } else {
            path += "/" + dataId;
        }
        return path;
    }
}
