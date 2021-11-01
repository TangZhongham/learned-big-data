import java.util.*;

public class OrderedProperties extends Properties {

    private static final long serialVersionUID = -4627607243846121965L;

    private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

    @Override
    public synchronized Enumeration<Object> keys() {
        return Collections.<Object>enumeration(keys);
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        keys.add(key);
//        System.out.println("key=" + key);
//        System.out.println("value=" + value);
        return super.put(key, value);
    }

    @Override
    public synchronized Object remove(Object key) {
        keys.remove(key);
        return super.remove(key);
    }

    @Override
    public Set<Object> keySet() {
        return keys;
    }

    @Override
    public Set<String> stringPropertyNames() {
        Set<String> set = new LinkedHashSet<String>();

        for (Object key : this.keys) {
            set.add((String) key);
        }
        return set;
    }
}
