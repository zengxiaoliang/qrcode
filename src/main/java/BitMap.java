

/**
 * @program: qrcode
 * @description:
 * @author: Xiaoliang.Zeng
 * @create: 2020-03-06 10:19
 **/
public class BitMap {

    /**
     * 生成一张空白的图片
     * @param width
     * @param height
     * @return
//     */
//
//    public static Bitmap createWhiteBitMap(int width, int height) {
//        Bitmap bitmap = Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
//        bitmap.eraseColor(Color.parseColor("#ffffff"));//填充颜色
//        return bitmap;
//
//    }
//
//    /**
//     * 将两个Bitmap合并成一个
//     *
//     * @param first
//     * @param second
//     * @param fromPoint 第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
//     * @return
//     */
//    public static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
//                                       PointF fromPoint) {
//        if (first == null || second == null || fromPoint == null) {
//            return null;
//        }
//        Bitmap newBitmap = Bitmap.createBitmap(
//                first.getWidth(),
//                first.getHeight(), Config.ARGB_4444);
//        Canvas cv = new Canvas(newBitmap);
//        cv.drawBitmap(first, 0, 0, null);
//        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
//        cv.save(Canvas.ALL_SAVE_FLAG);
//        cv.restore();
//
//        return newBitmap;
//    }

}
