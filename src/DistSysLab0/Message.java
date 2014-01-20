package distSysLab0;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    static Logger logger = Logger.getLogger(Message.class);
    
    public enum MessageKind {
        ACK {
            public String toString() {
                return "ack";
            }
        },
        LOOKUP {
            public String toString() {
                return "lookup";
            }
        },
        DEFAULT {
            public String toString() {
                return "default";
            }
        };
    }

    private int seqNum;
    private String src;
    private String dest;
    private String kind;
    private Object data;

    /**
     * Constructor of message
     * 
     * @param dest
     * @param kind
     * @param data
     */
    public Message(String dest, String kind, Object data) {
        this.dest = dest;
        this.kind = kind;
        this.data = data;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
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

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Seq: " + this.seqNum + ", " + this.src + " to " + this.dest +
               ". Kind: " + this.kind + " with " + this.data;
    }
}
