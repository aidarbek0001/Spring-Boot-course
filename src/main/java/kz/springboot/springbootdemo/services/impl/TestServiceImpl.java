package kz.springboot.springbootdemo.services.impl;

import kz.springboot.springbootdemo.services.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    private String testData;
    private int testIntData;


    @Override
    public String getTestData() {
        return "Some test Data " + this.testData;
    }

    @Override
    public int getTestDataInt() {
        return testIntData;
    }

    @Override
    public void setTestData(String testData) {
        this.testData = testData;
    }

    @Override
    public void setTestIntData(int testIntData) {
        this.testIntData = testIntData;
    }

    @Override
    public boolean auth(String email, String password) {
        return false;
    }
}
