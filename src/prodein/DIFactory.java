package prodein;


import prodein.exceptions.BindingException;
import prodein.exceptions.NoBindingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DIFactory implements DI {
    private final Map<String, Object> taggedBindings = new HashMap<>();
    private final List<Object> unTaggedBindings = new ArrayList<>();

    @Override
    public void bindSingleton(String tag, Object object) {
        if (isClassNameExist(object))
            try {
                throw new BindingException("Cannot override an existing bind class");
            } catch (BindingException e) {
                throw new RuntimeException(e);
            }

        else if (isTagExists(tag))
            try {
                throw new BindingException("Binding tag already exists");
            } catch (BindingException e) {
                throw new RuntimeException(e);
            }
        else if (isInstanceExist(object))
            try {
                throw new BindingException("Cannot override an existing bind instance");
            } catch (BindingException e) {
                throw new RuntimeException(e);
            }


        if (tag == null)
            unTaggedBindings.add(object);
        else
            taggedBindings.put(tag, object);

    }

    @Override
    public void bindSingleton(Object object) {
        if (isInstanceExist(object))
            try {
                throw new BindingException("Cannot override an existing bind instance");
            } catch (BindingException e) {
                throw new RuntimeException(e);
            }
        else if (isClassNameExist(object))
            try {
                throw new BindingException("Cannot override an existing bind class>>> " + object.getClass().getSimpleName());
            } catch (BindingException e) {
                throw new RuntimeException(e);
            }
        unTaggedBindings.add(object);
    }

    @Override
    public <T> boolean unBindSingleton(String tag, Class<T> type) {
        T object = (T) taggedBindings.get(tag);
        if (type.isAssignableFrom(object.getClass()))
            return taggedBindings.remove(tag, object);
        return false;
    }

    @Override
    public boolean unBindSingleton(String tag) {
        Object object = taggedBindings.get(tag);
        return taggedBindings.remove(tag, object);
    }

    @Override
    public boolean unBindSingleton(Object object) {
        for (Map.Entry<String, Object> stringObjectEntry : taggedBindings.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();
            if (Objects.deepEquals(object, stringObjectEntry.getValue()))
                return taggedBindings.remove(key, value);
        }
        return false;
    }

    @Override
    public <T> boolean unBindSingleton(Class<T> type) {
        for (Map.Entry<String, Object> stringObjectEntry : taggedBindings.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();

            if (!type.isAssignableFrom(value.getClass())) {
                return taggedBindings.remove(key, value);
            }
        }
        return false;
    }

    @Override
    public void unBindAllSingleton() {
        taggedBindings.clear();
        unTaggedBindings.clear();
    }

    @Override
    public <T> T instance(Class<T> type) {
        List<Object> test = new ArrayList<>();
        test.addAll(unTaggedBindings);
        test.addAll(taggedBindings.values());

        for (Object object : unTaggedBindings) {

            if (type.isAssignableFrom(object.getClass())) {
                return type.cast(object);
            }
        }
        try {
            throw new NoBindingException("No binding found for>>>> " + type.getSimpleName());
        } catch (NoBindingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T instance(String tag, Class<T> type) {
        Object object = type.cast(taggedBindings.get(tag));
        if (object != null && type.isAssignableFrom(object.getClass())) {
            return type.cast(object);
        }

        try {
            throw new NoBindingException("No binding found for>>>> " + type.getSimpleName());
        } catch (NoBindingException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean isTagExists(String tag) {
        return taggedBindings.containsKey(tag);
    }

    private Boolean isInstanceExist(Object object) {
        List<Object> test = new ArrayList<>();
        test.addAll(unTaggedBindings);
        test.addAll(taggedBindings.values());
        return test.contains(object);
    }

    private Boolean isClassNameExist(Object object) {
        List<Object> test = new ArrayList<>();
        test.addAll(taggedBindings.values());
        test.addAll(unTaggedBindings);

        for (Object finder : test) {
            if (object.getClass().getSimpleName().equals(finder.getClass().getSimpleName()))
                return true;
        }
        return false;
    }

    @Override
    public List<Object> getAllBindings() {
        List<Object> test = new ArrayList<>(unTaggedBindings);
        test.addAll(taggedBindings.values());
        return test;
    }

}
