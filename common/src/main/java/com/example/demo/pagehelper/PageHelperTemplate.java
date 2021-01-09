package com.example.demo.pagehelper;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Component;

@Component
public class PageHelperTemplate {

    public <R> R executePageHelper(int pageNum, int pageSize, PageHelperCallback<R> pageHelperCallback) {
        R execute;
        PageHelper.startPage(pageNum, pageSize);
        try {
            execute = pageHelperCallback.execute();
        } finally {
            PageHelper.clearPage();
        }
        return execute;
    }
}
