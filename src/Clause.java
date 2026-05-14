import java.util.BitSet;


public class Clause {

    final int propsCount;
    final BitSet positive;
    final BitSet negative;

    public Clause(int propsCount) {
        this.propsCount = propsCount;
        positive = new BitSet(propsCount);
        negative = new BitSet(propsCount);
    }

    public void set(int p, Boolean b) {
        if (Boolean.TRUE.equals(b)) {
            positive.set(p);
        } else {
            positive.clear(p);
        }
        if (Boolean.FALSE.equals(b)) {
            negative.set(p);
        } else {
            negative.clear(p);
        }
    }

    public Boolean get(int p) {
        if (positive.get(p))
            return Boolean.TRUE;
        if (negative.get(p))
            return Boolean.FALSE;
        return null;
    }

    public String toString() {
        StringBuffer b = new StringBuffer(propsCount);
        for (int i = 0; i < propsCount; i++) {

            if (positive.get(i))
                b.append('+');
            else if (negative.get(i))
                b.append('-');
            else
                b.append('.');

        }
        return b.toString();
    }

    public String toProlog() {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < propsCount; i++) {
            if (positive.get(i))
                b.append(b.length() == 0 ? "[" : ",").append(i + 1);
            else if (negative.get(i))
                b.append(b.length() == 0 ? "[" : ",").append('-').append(i + 1);
        }
        return b.append("]").toString();
    }

    public String toDIMACS() {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < propsCount; i++) {
            if (positive.get(i))
                b.append(b.length() == 0 ? "" : " ").append(i + 1);
            else if (negative.get(i))
                b.append(b.length() == 0 ? "" : " ").append('-').append(i + 1);
        }
        return b.toString();
    }

}
