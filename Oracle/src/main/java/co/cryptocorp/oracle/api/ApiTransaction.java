package co.cryptocorp.oracle.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.bitcoin.core.AddressFormatException;

import java.util.List;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import co.cryptocorp.oracle.core.HexDeserializer;
import co.cryptocorp.oracle.core.HexSerializer;

/**
 * @author devrandom
 */
public class ApiTransaction {
    @JsonProperty("bytes")
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    private final byte[] bytes;
    private final List<byte[]> inputScripts;
    private final List<String> chainPaths;
    private final List<byte[]> inputTransactions;
    private final List<String> outputChainPaths;
//    private ArrayList<DeterministicKey> masterKeys; TODO for later

    public ApiTransaction(@JsonProperty("bytes") @JsonDeserialize(using = HexDeserializer.class)
                          byte[] bytes,
                          @JsonProperty("inputScripts") @JsonDeserialize(contentUsing = HexDeserializer.class)
                          List<byte[]> inputScripts,
                          @JsonProperty("chainPaths")
                          List<String> chainPaths,
                          @JsonProperty("inputTransactions") @JsonDeserialize(contentUsing = HexDeserializer.class)
                          List<byte[]> inputTransactions,
                          @JsonProperty("outputChainPaths")
                          List<String> outputChainPaths
//                       ,@JsonProperty("masterKeys") TODO for later
//                       List<String> masterKeyStrings
    ) throws AddressFormatException {
        this.bytes = bytes;
        this.inputScripts = inputScripts;
        this.chainPaths = chainPaths;
        this.inputTransactions = inputTransactions;
        this.outputChainPaths = outputChainPaths;


//        if (masterKeyStrings != null) { TODO later
//            masterKeys = Lists.newArrayList();
//            for (String key: masterKeyStrings) {
//                masterKeys.add(HDUtil.parseDeterministicKey(key));
//            }
//        }
    }

    @JsonIgnore
    public ApiTransaction(@JsonProperty("bytes") @JsonDeserialize(using = HexDeserializer.class)
                          byte[] bytes) {
        this.bytes = bytes;
        this.inputScripts = null;
        this.chainPaths = null;
        this.inputTransactions = null;
        this.outputChainPaths = null;
//        this.masterKeys = null; TODO for later
    }

    @JsonSerialize(using=HexSerializer.class)
    public byte[] getBytes() {
        return bytes;
    }

    @JsonSerialize(contentUsing=HexSerializer.class)
    public List<byte[]> getInputScripts() { return inputScripts; }

    public List<String> getChainPaths() {
        return chainPaths;
    }

    @JsonSerialize(contentUsing=HexSerializer.class)
    public List<byte[]> getInputTransactions() { return inputTransactions; }

    public List<String> getOutputChainPaths() {
        return outputChainPaths;
    }

//    public List<DeterministicKey> getMasterKeys() { TODO for layer
//        return masterKeys;
//    }
}
