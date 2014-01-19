package distSysLab0;

public class RuleBean {
    private MessageAction action;
    private String src;
    private String dest;
    private MessageKind kind;

    // seqnum and duplicate?

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
    
    @Override
    public String toString() {
            return dest + " " + action;
    }
}
