import com.alibaba.fastjson.JSONArray;
import com.egova.json.utils.JsonUtils;
import com.egova.utils.FileUtils;
import com.soul.weapon.model.dds.EquipmentStatus;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName demoTest
 * @Description TODO
 * @Author ShiZuan
 * @Date 2022/3/14 13:45
 * @Version
 **/

public class demoTest {

    public static void main(String[] args) throws IOException {

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            System.out.println(random.nextInt(10));
        }

    }



}
