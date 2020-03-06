
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.OutputStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class QRCodeUtil {


    private static final String CHARSET = "UTF-8";
    private static final String FORMAT_NAME = "PNG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;


    /**
     * 生成二维码
     *
     * @param content      源内容
     * @param imgPath      生成二维码保存的路径
     * @param needCompress 是否要压缩
     * @return 返回二维码图片
     * @throws Exception
     */
    private static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
        // 插入图片
        QRCodeUtil.insertImage(image, imgPath, needCompress);
        return image;

    }

    /**
     * 在生成的二维码中插入图片
     *
     * @param source
     * @param imgPath
     * @param needCompress
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }


    /**
     * 生成带logo二维码，并保存到磁盘
     *
     * @param content
     * @param imgPath      logo图片
     * @param destPath
     * @param needCompress
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        mkdirs(destPath);
        String file = new Random().nextInt(99999999) + ".jpg";//生成随机文件名
        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
    }

    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir。(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static void encode(String content, String imgPath, String destPath) throws Exception {
        QRCodeUtil.encode(content, imgPath, destPath, false);
    }

    public static void encode(String content, String destPath, boolean needCompress) throws Exception {
        QRCodeUtil.encode(content, null, destPath, needCompress);
    }

    public static void encode(String content, String destPath) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false);
    }

    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    public static void encode(String content, OutputStream output) throws Exception {
        QRCodeUtil.encode(content, null, output, false);
    }


    /**
     * 对彩色照片黑白处理
     *
     * @param srcImg
     * @return
     */
    public static BufferedImage grayImage(final BufferedImage srcImg,int type) {
        int iw = srcImg.getWidth();
        int ih = srcImg.getHeight();
        Graphics2D srcG = srcImg.createGraphics();
        RenderingHints rhs = srcG.getRenderingHints();

        ColorSpace cs = ColorSpace.getInstance(type);
        ColorConvertOp theOp = new ColorConvertOp(cs, rhs);
        BufferedImage dstImg = new BufferedImage(iw, ih,
                BufferedImage.TYPE_INT_RGB);

        theOp.filter(srcImg, dstImg);
        return dstImg;
    }

    /**
     * 从二维码中，解析数据
     *
     * @param path 二维码图片文件
     * @return 返回从二维码中解析到的数据值
     * @throws Exception
     */


    public static String decodeFromQR(String path) throws Exception {

        File file = new File(path);
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        System.out.println(resultStr);
        return resultStr;
    }


    private static HashMap<String, List> getFile(String path, String type, String companyName) {
// get file list where the path has   
        HashMap fileMap = new HashMap();


        if (type.equals("file")) {
            File file = new File(path);
            // get the folder list
            File[] array = file.listFiles();
            List fileList = new ArrayList();

            for (int i = 0; i < array.length; i++) {
                if (array[i].isFile()) {
                    fileList.add(array[i].getPath());
                }
            }
            fileMap.put(path, fileList);
        } else {
            File file = new File(path);
            // get the folder list
            File[] array = file.listFiles();
            List fileList = new ArrayList();
            for (int i = 0; i < array.length; i++) {
                if (array[i].isDirectory()) {
                    fileList.add(array[i].getPath());
                }
                fileMap.put(path, fileList);

            }
        }
        return fileMap;
    }


    public static HashMap getUrls(String path, String companyName) throws Exception {
        HashMap filesPath = getFile(path, "", "");
        HashMap fileAndUrlMap = new HashMap();
        for (Object pathName : filesPath.keySet()
        ) {
            List paths = (List) filesPath.get(pathName);
            for (Object fileFullPath : paths
            ) {
                String fileFullPathSt = fileFullPath.toString();
                HashMap<String, List> hashMap = getFile(fileFullPathSt, "file", companyName);
                List urlsQRCodeList = hashMap.get(fileFullPathSt);
                for (Object urlCode : urlsQRCodeList
                ) {
                    String fileFullName = urlCode.toString();
                    String url = decodeFromQR(fileFullName);


                    fileAndUrlMap.put(fileFullName, url);
                }
            }
        }
        return fileAndUrlMap;

    }

    public static HashMap getUrls(String filePath) {

        String url = "";
        HashMap map = new HashMap();
        List urlList = new ArrayList();
        Map listMap = new HashMap();
        try {
            filePath = filePath.trim().replace("\uFEFF", "");

            File file = new File(filePath);
            // get the folder list
            File[] array = file.listFiles();

            for (int i = 0; i < array.length; i++) {
                if (array[i].isFile()) {
                    String fileName = array[i].getPath();
                    url = decodeFromQR(fileName);
                    System.out.println(url);
                    urlList.add(url);
                    listMap.put(fileName, url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("找不到"+filePath);
        }
        map.put(filePath, listMap);

        return map;

    }


    public static void checkData(String CSVFile, String QRCodePath)  {

        GetData getData = new GetData();
        HashMap csvMap = getData.getDataFromTxt(CSVFile);


        for (Object keys : csvMap.keySet()
        ) {

            String companyName = keys.toString();

            companyName = companyName.replace(" ", "").trim();

            String path = QRCodePath.trim() + companyName.trim().replace("\uFEFF", "") +File.separator.replace("\uFEFF", "");
            HashMap QRCodeMap = getUrls(path);

            String QRCodeStr = JSONObject.toJSONString(QRCodeMap);


            List<String> urlsList = (List) csvMap.get(keys);

            path = path.replace("\uFEFF", "").replace(" ", "");
            Map QRCodeUrlsMap = (Map) QRCodeMap.get(path);


            for (String url : urlsList
            ) {

                DataEntity dataEntity = new DataEntity();
                DataEntity notFoundDadaEntity = new DataEntity();
                DataEntity noResDataEntity = new DataEntity();

                // qrcode 二维码的URL中找到 Excel 中的URL ，校验是否能发送请求成功
                if (QRCodeStr.contains(url)) {
                    OkHttpClient client = new OkHttpClient().newBuilder() //
                            .readTimeout(10, TimeUnit.SECONDS) // 设置读取超时时间
                            .writeTimeout(10, TimeUnit.SECONDS) // 设置写的超时时间
                            .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间
                            .build();
                    Request request = new Request.Builder().url(url).get().build();

                    try {
                        Response response = client.newCall(request).execute();
                        //返回响应码
                        if (response.code() == 200) {

                            for (Object key : QRCodeUrlsMap.keySet()
                            ) {
                                String QRCodeUrl = QRCodeUrlsMap.get(key).toString();
                                if (url.equals(QRCodeUrl)) {
                                    path = key.toString();
                                    break;
                                }

                            }
                            dataEntity.setCompanyName(companyName);
                            dataEntity.setQrCodePath(path);
                            dataEntity.setUrl(url);
                            dataEntity.setFound(true);
                            dataEntity.setGetResponse(true);

                        } else {

                            noResDataEntity.setUrl(url);
                            noResDataEntity.setFound(true);
                            noResDataEntity.setCompanyName(companyName);
                            noResDataEntity.setGetResponse(false);
                            System.out.println("找到对应二维码，但响应码非200----"+JSONObject.toJSONString(noResDataEntity));

                        }

                    }catch (Exception e){
                        //   System.out.println(response.body().string());

                        noResDataEntity.setUrl(url);
                        noResDataEntity.setFound(true);
                        noResDataEntity.setCompanyName(companyName);
                        noResDataEntity.setGetResponse(false);
                        System.out.println("找到对应二维码，但请求不通----"+JSONObject.toJSONString(noResDataEntity));

                    }


                   // System.out.println(JSONObject.toJSONString(dataEntity));

                }
                else {
                    notFoundDadaEntity.setCompanyName(companyName);
                    notFoundDadaEntity.setFound(false);
                    notFoundDadaEntity.setUrl(url);

                    System.out.println("未在二维码中找到"+JSONObject.toJSONString(notFoundDadaEntity));

                }

            }

        }


    }

    public static void main(String[] args) throws Exception {
        //  getFile("/Users/zeng/Documents/", "");
        getUrls("/Users/zeng/Documents/DEMO/中梁壹号院/");
        getUrls("/Users/zeng/Documents/DEMO/九洲云海间");

       // System.out.println(  decode("/Users/zeng/Documents/testCode.jpeg") );
//        String CSVFile = args[0];
//        String QRCodePath = args[1];
//        checkData(CSVFile,QRCodePath);
    //    checkData("/Users/zeng/Documents/DEMO/demo.csv", "/Users/zeng/Documents/DEMO/");
    }

}
