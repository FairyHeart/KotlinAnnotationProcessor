package com.anno.processor.java.test;

/**
 * @author: GuaZi.
 * @date : 2020-04-03.
 */
public class Test {

    public static void main(String[] args) {

        User user = new User("liucj", "baidu@126.com", "16657135766");
        User user2 = new User("liucj", "baidu", "123");

        if (RegexValidUtils.check(user)) {
            System.out.println("验证通过");
        }

        if (RegexValidUtils.check(user2)) {
            System.out.println("验证通过");
        }
    }
}
