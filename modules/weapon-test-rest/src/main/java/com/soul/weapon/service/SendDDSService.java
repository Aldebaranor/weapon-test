package com.soul.weapon.service;

public interface SendDDSService {

    String sendDDSBytestCode(String testCode);

    String stopSendDDSBytestCode(String testCode);

    String stopAll();

    String startAllList();
}
