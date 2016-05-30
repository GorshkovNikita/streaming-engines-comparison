package diploma.processors;

/**
 * Created by Никита on 21.04.2016.
 */
public interface Processor<R, T> {
    R process(T object);
}
