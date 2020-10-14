import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.*;

class LinearCollection<T> implements Iterable<T> {
    // CLASS INVARIANT: mCollectionType != null
    private final Class<?> mCollectionType;
    // CLASS INVARIANT: mValueType != null and is a valid type argument to mCollectionType
    private final Class<?> mValueType;
    // CLASS INVARIANT: mCollection != null
    private final Object mCollection;
    // CLASS INVARIANT: mAddMethod != null and can be called with a T object
    private final Method mAddMethod;
    // CLASS INVARIANT: mClearMethod != null and can be called with no arguments
    private final Method mClearMethod;
    // CLASS INVARIANT: mIsEmptyMethod != null and can be called with no arguments
    private final Method mIsEmptyMethod;
    // CLASS INVARIANT: mIteratorMethod != null and can be called with no arguments
    private final Method mIteratorMethod;
    // CLASS INVARIANT: mSizeMethod != null and can be called with no arguments
    private final Method mSizeMethod;


    // PRE: collectionType != null and is a generic type accepting 1 parameter for which valueType would be valid
    public <C> LinearCollection(Class<C> collectionType, Class<T> valueType) throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        mCollectionType = collectionType;
        mValueType = valueType;
        mCollection = mCollectionType.newInstance();
        Class parTypes[] = new Class[]{ Object.class };
        mAddMethod = mCollectionType.getMethod("add", parTypes);
        parTypes = new Class[]{};
        mClearMethod = mCollectionType.getMethod("clear", parTypes);
        mIsEmptyMethod = mCollectionType.getMethod("isEmpty", parTypes);
        mIteratorMethod = mCollectionType.getMethod("iterator", parTypes);
        mSizeMethod = mCollectionType.getMethod("size", parTypes);
    }

    public void push_back(T value) throws InvocationTargetException, IllegalAccessException {
        mAddMethod.invoke(mCollection, value);
    }

    public void clear() throws InvocationTargetException, IllegalAccessException {
        mClearMethod.invoke(mCollection);
    }

    public boolean empty() throws InvocationTargetException, IllegalAccessException {
        return (Boolean)mIsEmptyMethod.invoke(mCollection);
    }

    public int size() throws InvocationTargetException, IllegalAccessException {
        return (Integer)mSizeMethod.invoke(mCollection);
    }

    public Iterator<T> iterator() {
        try {
            return (Iterator<T>)mIteratorMethod.invoke(mCollection);
        } catch (InvocationTargetException | IllegalAccessException e1) {
            return null;
        }
    }
}

public class Main3 {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        LinearCollection<Object> collection;
        for (Class<?> cls : new Class<?>[]{ ArrayList.class, ArrayDeque.class,
                                            ConcurrentLinkedQueue.class, CopyOnWriteArrayList.class,
                                            LinkedBlockingDeque.class, LinkedBlockingQueue.class,
                                            LinkedList.class, Stack.class, Vector.class}) {
            System.out.println(cls.getCanonicalName());
            collection = new LinearCollection<>(cls, Object.class);
            for (String arg : args) {
                collection.push_back(arg);
            }
            System.out.println(collection.size());
            for (Object s : collection) {
                System.out.println(s);
            }
        }
    }
}

