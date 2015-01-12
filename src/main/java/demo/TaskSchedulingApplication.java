package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class TaskSchedulingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskSchedulingApplication.class, args);
    }

    @Bean
    TaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}

@Controller
class ScheduleController {

    public static final CronTrigger EVERY_TEN_SECONDS = new CronTrigger("0/10 * * * * *");

    @Autowired
    TaskScheduler taskScheduler;

    ScheduledFuture<?> scheduledFuture;

    @RequestMapping("start")
    ResponseEntity<Void> start() {
        scheduledFuture = taskScheduler.schedule(printHour(), EVERY_TEN_SECONDS);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping("stop")
    ResponseEntity<Void> stop() {
        scheduledFuture.cancel(false);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    private Runnable printHour() {
        return () -> System.out.println("hello " + Instant.now().toEpochMilli());
    }
}
