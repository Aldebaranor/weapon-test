package dataSave.service;

import java.util.List;

public interface DataSaveService {
    void saveAsExcel(List<List<String>> list);
    void alterExcel(List<String> list) throws Exception;
    void saveLct(String json,String path) throws Exception;
}
