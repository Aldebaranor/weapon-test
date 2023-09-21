package dataSave.service.impl;

import dataSave.service.DataSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @ClassName DataSaveServiceImpl
 * @Description
 * @Author ShiZuan
 * @Date 2023/9/21 18:32
 * @Version
 **/
@Slf4j
@Service
@Priority(10)
@RequiredArgsConstructor
@Component("data-save")
public class DataSaveServiceImpl implements DataSaveService {

    @Override
    public void saveAsExcel(List<List<String>> list) {
        //创建Workbook类
        Workbook wb=new HSSFWorkbook();
        Sheet sheet=wb.createSheet("表名sheet");//sheet是一张表，创建时可以传入表名字
        Row row1=sheet.createRow(0);//由表创建行，需要传入行标，由0开始
        row1.createCell(0).setCellValue("表头名1");//得到行对象后，按列写入值
        row1.createCell(1).setCellValue("表头名2");
        row1.createCell(2).setCellValue("表头名3");
        for (int i=0;i<list.size();i++) {
            List<String> m=list.get(i);
            //行
            Row row=sheet.createRow(i+1);
            for(int j=0;j<m.size();j++){
                row.createCell(j).setCellValue(m.get(j));
            }
        }

        //下面给出文件和输出流，然后把excel数据写入
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        File file=new File("resource/"+date.format(formatter)+"的消息记录表.xls");
        if(!file.exists()){
            File path = new File("resource");
            if(!path.exists()){
                path.mkdir();
                try{
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try(OutputStream ops=new FileOutputStream(file)){
            wb.write(ops);
            //wb.close();
            ops.close();
            //这里关闭Workbook或者关闭OutputStream都可以，应该是Workbook关闭的时候顺带关闭了OutputStream
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterExcel(List<String> list) throws Exception{

        FileInputStream fs = new FileInputStream("resource/2023-09-21的消息记录表.xls");
        POIFSFileSystem fileSystem = new POIFSFileSystem(fs);

        Workbook wb=new HSSFWorkbook(fileSystem);
        Sheet sheet=wb.getSheetAt(0);//sheet是一张表，创建时可以传入表名字
        Row row=sheet.createRow(sheet.getLastRowNum()+1);//由表创建行，需要传入行标，由0开始

        for (int i=0;i<list.size();i++) {
            row.createCell(i).setCellValue(list.get(i));
        }

        //下面给出文件和输出流，然后把excel数据写入
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        File file=new File("resource/"+date.format(formatter)+"的消息记录表.xls");
        if(!file.exists()){
            File path = new File("resource");
            if(!path.exists()){
                path.mkdir();
                try{
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try(OutputStream ops=new FileOutputStream(file)){
            wb.write(ops);
            //wb.close();
            ops.close();
            //这里关闭Workbook或者关闭OutputStream都可以，应该是Workbook关闭的时候顺带关闭了OutputStream
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveLct(String json,String path) throws Exception {
        //判断excel是否存在
        File file = new File(path + "/LIUCHENGTU.xls");
        boolean exist = true;
        if (!file.exists()) {
            //判断路径是否存在
            File fPath = new File(path);
            if (!fPath.exists()) {
                //创建excel表目录
                fPath.mkdir();
                try {
                    //创建excel表
                    file.createNewFile();
                    exist = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //如果excel是新创建的，则创建Workbook类
        Sheet sheet;
        Workbook wb;
        if (!exist) {
            wb = new HSSFWorkbook();
            sheet = wb.createSheet("sheetName");//sheet是一张表，创建时可以传入表名字
            Row row1 = sheet.createRow(0);//由表创建行，需要传入行标，由0开始
            row1.createCell(0).setCellValue("时间");//得到行对象后，按列写入值
            row1.createCell(1).setCellValue("content");//value转json字符串
        } else {
            POIFSFileSystem fileSystem = new POIFSFileSystem(file);
            wb = new HSSFWorkbook(fileSystem);
            sheet = wb.getSheetAt(0);//sheet是一张表，创建时可以传入表名字
            Row row1 = sheet.createRow(sheet.getLastRowNum() + 1);//由表创建行，需要传入行标，由0开始
        }
        int num = sheet.getLastRowNum();
        Row row = sheet.createRow(num + 1);
//        LocalDate date = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        row.createCell(0).setCellValue(String.valueOf(LocalTime.now()));//设置时间，是否要改成相对时间
        row.createCell(1).setCellValue(json);

        //下面给出文件和输出流，然后把excel数据写入
        try (OutputStream ops = new FileOutputStream(file)) {
            wb.write(ops);
            //wb.close();
            ops.close();
            //这里关闭Workbook或者关闭OutputStream都可以，应该是Workbook关闭的时候顺带关闭了OutputStream
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
