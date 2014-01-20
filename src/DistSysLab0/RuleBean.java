package distSysLab0;

import org.apache.log4j.Logger;

import distSysLab0.Message.MessageKind;

public class RuleBean {
    static Logger logger = Logger.getLogger(RuleBean.class);

    public enum MessageAction {
        DROP, DUPLICATE, DELAY, NONE;
    }

    private MessageAction action;
    private String src;
    private String dest;
    private MessageKind kind;
    private int id = -1;
    private int Nth = -1;
    private int everyNth = -1;

    public RuleBean() {

    }

    public RuleBean(MessageAction action) {
        super();
        this.action = action;
    }

    public MessageAction getAction() {
        return action;
    }

    public void setAction(MessageAction action) {
        this.action = action;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public MessageKind getKind() {
        return kind;
    }

    public void setKind(MessageKind kind) {
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNth() {
        return Nth;
    }

    public void setNth(int nth) {
        Nth = nth;
    }

    public int getEveryNth() {
        return everyNth;
    }

    public void setEveryNth(int everyNth) {
        this.everyNth = everyNth;
    }

    @Override
    public String toString() {
        return "[action=" + action + ", src=" + src + ", dest=" + dest + 
               ", kind=" + kind + ", id=" + id + ", Nth=" + Nth + ", everyNth="
               + everyNth + "]";
    }
}
