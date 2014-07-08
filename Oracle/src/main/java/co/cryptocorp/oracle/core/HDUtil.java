package co.cryptocorp.oracle.core;

import com.google.bitcoin.crypto.ChildNumber;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.ValidationException;

/**
 * @author devrandom
 */
public class HDUtil {
    private static final byte[] xprv = new byte[]{0x04, (byte) 0x88, (byte) 0xAD, (byte) 0xE4};
    private static final byte[] xpub = new byte[]{0x04, (byte) 0x88, (byte) 0xB2, (byte) 0x1E};
    private static final byte[] tprv = new byte[]{0x04, (byte) 0x35, (byte) 0x83, (byte) 0x94};
    private static final byte[] tpub = new byte[]{0x04, (byte) 0x35, (byte) 0x87, (byte) 0xCF};
/* TODO for later
    public static DeterministicKey parseDeterministicKey(String serialized) throws AddressFormatException {
        byte[] data = Base58.decodeChecked(serialized);

        if (data.length != 78) {
            throw new AddressFormatException("invalid extended key");
        }
        byte[] type = Arrays.copyOf(data, 4);
        boolean hasPrivate;
        if (Arrays.equals(type, xprv) || Arrays.equals(type, tprv)) {
            hasPrivate = true;
        } else if (Arrays.equals(type, xpub) || Arrays.equals(type, tpub)) {
            hasPrivate = false;
        } else {
            throw new AddressFormatException("invalid magic number for an extended key");
        }

        int depth = data[4] & 0xff;

        int parent = data[5] & 0xff;
        parent <<= 8;
        parent |= data[6] & 0xff;
        parent <<= 8;
        parent |= data[7] & 0xff;
        parent <<= 8;
        parent |= data[8] & 0xff;

        int sequence = data[9] & 0xff;
        sequence <<= 8;
        sequence |= data[10] & 0xff;
        sequence <<= 8;
        sequence |= data[11] & 0xff;
        sequence <<= 8;
        sequence |= data[12] & 0xff;

        // Ignore depth, parent, sequence for now

        byte[] chainCode = Arrays.copyOfRange(data, 13, 13 + 32);
        byte[] pubOrPriv = Arrays.copyOfRange(data, 13 + 32, data.length);
        DeterministicKey key;
        ImmutableList<ChildNumber> childPath = ImmutableList.of();
        if (hasPrivate) {
            key = new DeterministicKey(childPath, chainCode, new BigInteger(1, pubOrPriv), null);
        } else {
            key = new DeterministicKey(childPath, chainCode, ECKey.CURVE.getCurve().decodePoint(pubOrPriv), null, null);
        }
        return key;
    }

    public static List<ChildNumber> parseChainPath(String chainPath) {
        if (chainPath.isEmpty())
            return Lists.newArrayList();
        String[] components = chainPath.split("/");
        List<ChildNumber> result = Lists.newArrayListWithExpectedSize(components.length);
        for (int i = 0; i < components.length; i++) {
            String component = components[i];
            boolean prime = false;
            if (component.endsWith("'")) {
                component = component.substring(0, component.length() - 1);
                prime = true;
            }
            result.add(new ChildNumber(Integer.valueOf(component), prime));
        }
        return result;
    }
*/
    public static void enforceChainPathConstraints(List<ChildNumber> path) throws ValidationException {
        if (path.size() > 4)
            throw new RuntimeException("HD path length must not be > 4");
        Iterator<ChildNumber> iter = path.iterator();
        while (iter.hasNext()) {
            ChildNumber child = iter.next();
            if (child.getI() > 100 && !iter.hasNext())
                throw new RuntimeException("intermediate HD path index must not be > 100");
            if (child.getI() > 100000)
                throw new RuntimeException("last HD path index must not be > 100000");
        }
    }
}
