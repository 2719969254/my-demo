package com.example.demo.design.factoryPattern.abastractFactoryPattern;

public interface CourseFactory {
    INote createNote();
    IVideo createVideo();
}
