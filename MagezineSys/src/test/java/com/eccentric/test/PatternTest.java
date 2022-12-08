/*
 * author:eccentric
 * time:2022/11/10
 */
package com.eccentric.test;

import org.junit.Test;

import java.util.regex.Pattern;

public class PatternTest {
    @Test
    public void test(){
        System.out.println(Pattern.matches("17373011502", "^1[345679]\\d{9}"));
    }
}
