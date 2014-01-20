package distSysLab0;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import distSysLab0.Message.MessageKind;
import distSysLab0.RuleBean.MessageAction;

public class ConfigParser {
    public static int NUM_NODE;
    private String configurationFile;

    public ConfigParser(String configurationFile) {
        this.configurationFile = configurationFile;
    }

    /**
     * Read configuration part in config file.
     */
    public HashMap<String, NodeBean> readConfig() throws UnknownHostException {
        HashMap<String, NodeBean> nodeList = new HashMap<String, NodeBean>();
        Map<String, ArrayList<Map<String, Object>>> obj = init();

        for(Map.Entry<String, ArrayList<Map<String, Object>>> entrys : obj.entrySet()) {
            Iterator<Map<String, Object>> i = entrys.getValue().iterator();
            while(i.hasNext()) {
                Map<String, Object> node = (Map<String, Object>) i.next();
                NodeBean bean = new NodeBean();
                for(Map.Entry<String, Object> entry : node.entrySet()) {
                    if(entry.getKey().equalsIgnoreCase("Name"))
                        bean.setName(entry.getValue().toString());
                    if(entry.getKey().equalsIgnoreCase("IP"))
                        bean.setIp(entry.getValue().toString());
                    if(entry.getKey().equalsIgnoreCase("Port"))
                        bean.setPort(Integer.parseInt((entry.getValue().toString())));
                }
                nodeList.put(bean.getName(), bean);
            }
        }
        return nodeList;
    }

    /**
     * Read send rule part in config file.
     */
    public ArrayList<RuleBean> readSendRules() {
        ArrayList<RuleBean> sendRules = new ArrayList<RuleBean>();
        Map<String, ArrayList<Map<String, Object>>> obj = init();

        for(Map.Entry<String, ArrayList<Map<String, Object>>> entrys : obj.entrySet()) {
            Iterator<Map<String, Object>> i = entrys.getValue().iterator();
            while (i.hasNext()) {
                Map<String, Object> details = (Map<String, Object>) i.next();
                RuleBean bean = new RuleBean();
                for (Map.Entry<String, Object> innerdetails : details.entrySet()) {
                    if (innerdetails.getKey().equalsIgnoreCase("Action")) {
                        if (innerdetails.getValue().toString().equalsIgnoreCase("Drop"))
                            bean.setAction(MessageAction.DROP);
                        if (innerdetails.getValue().toString().equalsIgnoreCase("Delay"))
                            bean.setAction(MessageAction.DELAY);
                        if (innerdetails.getValue().toString().equalsIgnoreCase("Duplicate"))
                            bean.setAction(MessageAction.DUPLICATE);
                    }

                    if (innerdetails.getKey().equalsIgnoreCase("Src"))
                        bean.setSrc(innerdetails.getValue().toString());
                    if (innerdetails.getKey().equalsIgnoreCase("Dest"))
                        bean.setDest(innerdetails.getValue().toString());
                    if (innerdetails.getKey().equalsIgnoreCase("Kind")) {
                        if (innerdetails.getValue().toString().equalsIgnoreCase("Lookup"))
                            bean.setKind(MessageKind.LOOKUP);
                        if (innerdetails.getValue().toString().equalsIgnoreCase("Ack"))
                            bean.setKind(MessageKind.ACK);
                        if (innerdetails.getValue().toString().equalsIgnoreCase("None"))
                            bean.setKind(MessageKind.DEFAULT);
                    }
                }
                sendRules.add(bean);
            }
        }
        return sendRules;
    }

    /**
     * Read receive rule part in config file.
     */
    public ArrayList<RuleBean> readRecvRules() {
        ArrayList<RuleBean> recvRules = new ArrayList<RuleBean>();
        Map<String, ArrayList<Map<String, Object>>> obj = init();

        for(Map.Entry<String, ArrayList<Map<String, Object>>> entrys : obj.entrySet()) {
            Iterator<Map<String, Object>> i = entrys.getValue().iterator();
            while (i.hasNext()) {
                Map<String, Object> details = (Map<String, Object>) i.next();
                RuleBean bean = new RuleBean();
                for (Map.Entry<String, Object> entry : details.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase("Action")) {
                        if (entry.getValue().toString().equalsIgnoreCase("Drop"))
                            bean.setAction(MessageAction.DROP);
                        if (entry.getValue().toString().equalsIgnoreCase("Delay"))
                            bean.setAction(MessageAction.DELAY);
                        if (entry.getValue().toString().equalsIgnoreCase("Duplicate"))
                            bean.setAction(MessageAction.DUPLICATE);
                    }

                    if (entry.getKey().equalsIgnoreCase("Src"))
                        bean.setSrc(entry.getValue().toString());
                }
                recvRules.add(bean);
            }
        }
        return recvRules;
    }

    @SuppressWarnings("unchecked")
    private Map<String, ArrayList<Map<String, Object>>> init() {
        Map<String, ArrayList<Map<String, Object>>> raw = null;
        try {
            // Reads the file and builds configure map
            Yaml yaml = new Yaml();
            File optionFile = new File(configurationFile);
            FileReader fr = new FileReader(optionFile);
            BufferedReader br = new BufferedReader(fr);
            raw =  (Map<String, ArrayList<Map<String, Object>>>) yaml.load(br);
        }
        catch (Exception e) {
            System.out.println("File Read Error: " + e.getMessage());
        }
        return raw;
    }
}
