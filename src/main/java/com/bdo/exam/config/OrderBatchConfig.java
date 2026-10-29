package com.bdo.exam.config;

import com.bdo.exam.dto.OrderCsvDto;
import com.bdo.exam.entity.Order;
import com.bdo.exam.processor.OrderTransformationProcessor;
import com.bdo.exam.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class OrderBatchConfig {

    private final BatchReaderFactory readerFactory;
    private final OrderTransformationProcessor transformationProcessor;
    private final OrderRepository orderRepository;

    public OrderBatchConfig(BatchReaderFactory readerFactory,
                            OrderTransformationProcessor transformationProcessor,
                            OrderRepository orderRepository) {
        this.readerFactory = readerFactory;
        this.transformationProcessor = transformationProcessor;
        this.orderRepository = orderRepository;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<OrderCsvDto> csvItemReader(@Value("#{jobParameters['filePath']}") String filePath) {
        return readerFactory.createPipeDelimitedReader(filePath);
    }

    @Bean
    public RepositoryItemWriter<Order> orderItemWriter() {
        RepositoryItemWriter<Order> writer = new RepositoryItemWriter<>();
        writer.setRepository(orderRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public TaskExecutor batchTaskExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("batch-thread-");
        executor.setConcurrencyLimit(4); // Multi-threaded processing
        return executor;
    }

    @Bean
    public Step processOrdersStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processOrdersStep", jobRepository)
                .<OrderCsvDto, Order>chunk(20, transactionManager)
                .reader(csvItemReader(null))
                .processor(transformationProcessor)
                .writer(orderItemWriter())
                .taskExecutor(batchTaskExecutor())
                .build();
    }

    @Bean
    public Job processOrdersJob(JobRepository jobRepository, Step processOrdersStep) {
        return new JobBuilder("processOrdersJob", jobRepository)
                .start(processOrdersStep)
                .build();
    }
}
