package homework.t01;

import minispring.annotations.Component;

/**
 * @author jcwang
 */
@Component
public class Student implements IStudent{
    @Override
    public void study() {
        System.out.println("==== student is studying... ===");
    }
}
