package com.example.myapplication.arch.entity;

import javax.annotation.concurrent.Immutable;

@Immutable
public class RuleRandomIdentity implements RandomIdentity<String>, Identity<String> {

    private String value;
    /**
     * 前缀
     */
    private String prefix;
    /**
     * 随机种子
     */
    private int seed;

    @Override
    public String value() {
        return "";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String emptyId() {
        return "";
    }

    @Override
    public String next() {
        return "";
    }
}
