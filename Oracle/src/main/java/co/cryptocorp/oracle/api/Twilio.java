package co.cryptocorp.oracle.api;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
* @author devrandom
*/
@XmlRootElement(name="Response")
public class Twilio {
    @XmlElement(name="Gather")
    private Gather gather;

    @XmlElement(name="Say")
    private List<String> say;

    public Twilio() {
    }

    public Twilio(Gather gather) {
        this.gather = gather;
    }

    public Twilio(List<String> say) {
        this.say = say;
    }

    public Gather getGather() {
        return gather;
    }

    public List<String> getSay() {
        return say;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Gather {
        @XmlAttribute
        private int numDigits;
        @XmlAttribute
        private String action;
        @XmlElement(name="Say")
        private List<String> say;

        public Gather() {
        }

        public Gather(int numDigits, String action, List<String> say) {
            this.say = say;
            this.numDigits = numDigits;
            this.action = action;
        }
    }
}
