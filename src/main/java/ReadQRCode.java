/**
 * @program: qrcode
 * @description:
 * @author: Xiaoliang.Zeng
 * @create: 2020-03-06 15:33
 **/

import com.swetake.util.Qrcode;
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.util.Color;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ReadQRCode {


    static int width = 90;
    static int height = 90;

        public static void main(String[] args) throws IOException {
            //图片路径
            File file = new File("/Users/zeng/Documents/DEMO/九洲云海间/1582955788.png");
            //读取图片到缓冲区
            BufferedImage bufferedImage = ImageIO.read(file);


           //QRCode解码器
            QRCodeDecoder codeDecoder = new QRCodeDecoder();
            /**
             *codeDecoder.decode(new MyQRCodeImage())
             *这里需要实现QRCodeImage接口，移步最后一段代码
             */
            //通过解析二维码获得信息
            String result = new String(codeDecoder.decode(new MyQRCodeImage(bufferedImage)), "utf-8");
            System.out.println(result);
        }
    }

