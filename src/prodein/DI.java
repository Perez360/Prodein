package prodein;


import java.util.List;

public interface DI {

    
    <T> void bindSingleton(String tag, T object);


    <T> void bindSingleton(T object);

    <T> T instance(Class<T> type);

    <T> boolean unBindSingleton(String tag, Class<T> type);

    boolean unBindSingleton(String tag);

    <T> boolean unBindSingleton(T object);

    <T> boolean unBindSingleton(Class<T> type);

    void unBindAllSingleton();

    <T> T instance(String tag, Class<T> type);

    <T> List<Object> getAllBindings();

}
