/**
 * @program: qrcode
 * @description:
 * @author: Xiaoliang.Zeng
 * @create: 2020-03-06 15:15
 **/


import jp.sourceforge.qrcode.data.QRCodeImage;

import java.awt.image.BufferedImage;

public class MyQRCodeImage implements QRCodeImage {






        BufferedImage bufferedImage;

        public MyQRCodeImage(BufferedImage bufferedImage){
            this.bufferedImage=bufferedImage;
        }

        //宽
        @Override
        public int getWidth() {

            return bufferedImage.getWidth()/10;
        }

        //高
        @Override
        public int getHeight() {
            return bufferedImage.getHeight()/10;
        }

        //像素还是颜色
        @Override
        public int getPixel(int i, int j) {
            return bufferedImage.getRGB(i,j);
        }
    }

