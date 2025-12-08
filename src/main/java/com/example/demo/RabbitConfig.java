import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.TopicExchange;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "student-events";
    public static final String QUEUE = "student-course-queue";
    public static final String ROUTING_KEY = "student.course.associate";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, false);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
