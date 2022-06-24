package com.api.batch_handler.batch;

import com.api.batch_handler.TestConfiguration;
import com.api.batch_handler.repository.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBatchTest // @JobScope이 Test코드에서 사용되기 위함
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { SavePersonConfiguration.class, TestConfiguration.class })
public class SavePersonConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PersonRepository personRepository;

    @After
    public void tearDown() throws Exception {
        personRepository.deleteAll();
    }

    @Test
    public void test_step() {
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("savePersonStep");

        // then
        Assertions.assertThat(
                        jobExecution.getStepExecutions().stream()
                                .mapToInt(StepExecution::getWriteCount)
                                .sum())

                .isEqualTo(personRepository.count())
                .isEqualTo(3);
    }

    @Test
    public void test_allow_duplicate() throws Exception {

        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("allow_duplicate", "false")
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertThat(
                jobExecution.getStepExecutions().stream()
                    .mapToInt(StepExecution::getWriteCount) // Job에 해당되는 Step을 가져와서 write된 items들의 갯수를 센다.
                    .sum())

                .isEqualTo(personRepository.count())
                .isEqualTo(3);
    }

    @Test
    public void test_not_allow_duplicate() throws Exception {

        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("allow_duplicate", "true")
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertThat(
                        jobExecution.getStepExecutions().stream()
                                .mapToInt(StepExecution::getWriteCount) // Job에 해당되는 Step을 가져와서 write된 items들의 갯수를 센다.
                                .sum())

                .isEqualTo(personRepository.count())
                .isEqualTo(100);
    }

}