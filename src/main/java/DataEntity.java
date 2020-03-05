/**
 * @program: qrcode
 * @description:
 * @author: Xiaoliang.Zeng
 * @create: 2020-03-05 13:20
 **/
public class DataEntity {

    private String qrCodePath;
    private String companyName;
    private boolean isFound;
    private boolean isGetResponse;
    private String url;

    public String getQrCodePath() {
        return qrCodePath;
    }

    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }

    public boolean isGetResponse() {
        return isGetResponse;
    }

    public void setGetResponse(boolean getResponse) {
        isGetResponse = getResponse;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
