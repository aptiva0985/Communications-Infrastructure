package distSysLab0;

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
