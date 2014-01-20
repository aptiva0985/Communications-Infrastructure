package distSysLab0;

import org.apache.log4j.Logger;

import distSysLab0.Message.MessageKind;

public class RuleBean {
    static Logger logger = Logger.getLogger(RuleBean.class);

    public enum RuleAction {
        DROP, DUPLICATE, DELAY, NONE;
    }

    private RuleAction action;
    private String src;
    private String dest;
    private MessageKind kind;
    private Boolean duplicate = null;
    private int seqNum = -1;

    public RuleBean() {

    }

    public RuleBean(RuleAction action) {
        super();
        this.action = action;
    }

    public RuleAction getAction() {
        return action;
    }

    public void setAction(RuleAction action) {
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
    
    public Boolean getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(Boolean duplicate) {
        this.duplicate = duplicate;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public boolean isMatch(Message message) {
        if(getSrc() == null || getSrc().equalsIgnoreCase(message.getSrc())) {
            if(getDest() == null || getDest().equalsIgnoreCase(message.getDest())) {
                if(getKind() == null || getKind().equals(message.getKind())) {
                    if(getSeqNum() == -1 || getSeqNum() == message.getSeqNum()) {
                        if(getDuplicate() == null || getDuplicate() == message.getDuplicate()) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "[action=" + action + ", src=" + src + ", dest=" + dest + ", kind=" + kind + ", SeqNum=" + seqNum + "]";
    }
}
