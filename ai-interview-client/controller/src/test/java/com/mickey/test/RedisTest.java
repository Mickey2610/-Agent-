package com.mickey.test;

import redis.clients.jedis.Jedis;

public class RedisTest {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("127.0.0.1", 6379)) {
            jedis.auth("default", "mq20011103"); // 如果没有用户名可只传密码
            System.out.println("Connected to Redis");
            System.out.println("Ping: " + jedis.ping());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}