package linq;

import java.lang.reflect.Field;
import java.util.Comparator;

public class ListComparator implements Comparator<Object> {
    private String fieldName;

    public ListComparator(String fieldName) {
        this.fieldName = fieldName;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public int compare(Object object1, Object object2) {
        try {
            Field field = object1.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Comparable o1FieldValue = (Comparable) field.get(object1);
            Comparable o2FieldValue = (Comparable) field.get(object2);

            if (o1FieldValue == null){ return -1;}
            if (o2FieldValue == null){ return 1;}
            return o1FieldValue.compareTo(o2FieldValue);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Field doesn't exist", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field inaccessible", e);
        }
    }
}
