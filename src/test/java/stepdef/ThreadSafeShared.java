package stepdef;

import java.util.*;

/**
 * We use a synchronized version of hashMap to avoid concurrency issues
 * this class is just a basic implementation of SharedData, depending on usage/needs
 * this can change but we should still abstract data access and modification to avoid
 * problems.
 */
public class ThreadSafeShared implements SharedData {
    private final Map<String, Object> sharedMap;
    private final List<String> attachments;
    private String partNumber;

    public ThreadSafeShared() {
        this.attachments = Collections.synchronizedList(new LinkedList<String>());
        this.sharedMap = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public void addData(String k, Object v) {
        this.sharedMap.put(k, v);
    }

    @Override
    public Object getData(String k) {
        return this.sharedMap.get(k);
    }

    @Override
    public String getPartNumber() {
        return partNumber;
    }

    @Override
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    @Override
    public void addAttachment(String path) {
        attachments.add(path);
    }

    @Override
    public List<String> getAttachments() {
        return attachments;
    }


}
