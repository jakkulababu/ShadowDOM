package stepdef;


import java.util.List;

public interface SharedData {
    void addData(String k, Object v);

    Object getData(String k);


    String getPartNumber();

    void setPartNumber(String partNumber);

    void addAttachment(String path);

    List<String> getAttachments();


}
