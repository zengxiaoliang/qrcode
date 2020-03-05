import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @program: qrcode
 * @description: Get url Data from txt
 * @author: Xiaoliang.Zeng
 * @create: 2020-03-05 09:52
 **/
public class GetData {


    /**
     * 从TXT中获取url、公司信息
     */
    public HashMap getDataFromTxt(String fileName) {


        BufferedReader reader = null;
        String line;
        String[] lineChars;
        String companyName;
        HashMap companyMap = new HashMap();
        try {

            InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
            reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) {

                if (StringUtils.isNotEmpty(line)) {
                    List<String> urlsList = new ArrayList<String>();

                    lineChars = line.split(",");
                    companyName = lineChars[0].trim().replace(" ", "");
                    urlsList.add(lineChars[1]);
                    urlsList.add(lineChars[2]);
                    urlsList.add(lineChars[3]);

                    companyMap.put(companyName, urlsList);

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return companyMap;


    }



}
