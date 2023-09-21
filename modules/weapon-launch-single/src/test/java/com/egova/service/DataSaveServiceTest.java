package com.egova.service;

import com.egova.BootstrapTest;
import dataSave.service.DataSaveService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DataSaveTest
 * @Description TODO
 * @Author ShiZuan
 * @Date 2023/9/21 14:50
 * @Version
 **/
public class DataSaveServiceTest extends BootstrapTest {

    @Autowired
    public DataSaveService dataSaveService;

    @Test
    public void test() throws Exception{
        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");

        List<String> list1 = new ArrayList<>();
        list1.add("10");
        list1.add("11");
        list1.add("12");
        list1.add("13");
        list1.add("14");
        list1.add("15");
        list1.add("16");
        list1.add("17");

        List<List<String>> lists = new ArrayList<>();
        lists.add(list);
        lists.add(list1);
        dataSaveService.saveAsExcel(lists);

        List<String> list3 = new ArrayList<>();
        list3.add("20");
        list3.add("21");
        list3.add("22");
        list3.add("23");
        list3.add("24");
        list3.add("25");
        list3.add("26");
        list3.add("27");

        dataSaveService.alterExcel(list3);
    }

    @Test
    public void alter() throws Exception{
        List<String> list = new ArrayList<>();
        list.add("20");
        list.add("21");
        list.add("22");
        list.add("23");
        list.add("24");
        list.add("25");
        list.add("26");
        list.add("27");

        dataSaveService.alterExcel(list);
    }
}
