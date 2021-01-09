package com.example.demo.pagehelper;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/01/09
 */
public class PageHelperResult<R> {
    private final R data;

    private PageHelperResult(R data) {
        this.data = data;
    }

    /**
     * 只能同包调用
     * @param <R>
     * @param data
     * @return
     */
    protected static <R> PageHelperResult<R> genResult(R data) {
        return new PageHelperResult<R>(data);
    }

}
