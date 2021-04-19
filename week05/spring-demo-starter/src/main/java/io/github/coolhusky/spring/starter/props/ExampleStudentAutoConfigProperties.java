package io.github.coolhusky.spring.starter.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author jcwang
 */
@ConfigurationProperties(prefix = "example.autoconfig")
@Data
public class ExampleStudentAutoConfigProperties {
   private List<StudentProperty> students;
}
