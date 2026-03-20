package com.pado;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

public class ParameterizedTypeReferenceTest {

    static class Book<T> {
        T value;
    }

    static class Novel extends Book<List<Integer>>{ }

    @Test
    void test1() throws NoSuchFieldException {
        Book<List<String>> book = new Book<>();
        System.out.println(book.getClass().getDeclaredField("value").getType());
        //컴파일 시 Object로 치환되는 Generic
    }

    @Test
    void test2() {
        Novel novel = new Novel();
        System.out.println(novel.getClass().getGenericSuperclass());
        //자식 클래스를 사용하여 Generic을 살리는 방법
        //클래스 선언 시 명시된 타입은 GenericSuperClass에 보존됨
    }

    private ParameterizedTypeReference<List<String>> p1 = new ParameterizedTypeReference<>() {}; //뒤에 {}를 붙이는 것으로 익명 자식 클래스 생성

    @Test
    void test3() {
        System.out.println(p1.getType());
        //test2를 쉽게 사용할 수 있도록 ParameterizedTypeReference 만들어짐
    }
}
