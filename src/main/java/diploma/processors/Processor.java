package diploma.processors;

/**
 * Created by Никита on 21.04.2016.
 */
public interface Processor<T> {
    void process(T object);
}
