package org.example.demo1.aws;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class BudgetNotificationService {

    private final SnsClient snsClient;
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private String topicArn;
    private String queueUrl;
    private String queueArn;

    private static final String BUDGET_TOPIC = "BudgetOverflowTopic";
    private static final String BUDGET_QUEUE = "BudgetOverflowQueue";

    public BudgetNotificationService() {
        URI localStackUri = URI.create("http://localhost:4566");

        var credentials = StaticCredentialsProvider.create(
                AwsBasicCredentials.create("test", "test")
        );

        this.snsClient = SnsClient.builder()
                .endpointOverride(localStackUri)
                .credentialsProvider(credentials)
                .region(Region.US_EAST_1)
                .build();

        this.sqsClient = SqsClient.builder()
                .endpointOverride(localStackUri)
                .credentialsProvider(credentials)
                .region(Region.US_EAST_1)
                .build();

        this.objectMapper = new ObjectMapper();
    }

    public void initialize() throws Exception {
        try {
            this.topicArn = snsClient.createTopic(CreateTopicRequest.builder()
                    .name(BUDGET_TOPIC)
                    .build()).topicArn();

            System.out.println(" SNS Topic created: " + this.topicArn);

            this.queueUrl = sqsClient.createQueue(CreateQueueRequest.builder()
                    .queueName(BUDGET_QUEUE)
                    .build()).queueUrl();

            System.out.println(" SQS Queue created: " + this.queueUrl);

            this.queueArn = sqsClient.getQueueAttributes(GetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributeNames(QueueAttributeName.QUEUE_ARN)
                    .build()).attributes().get(QueueAttributeName.QUEUE_ARN);

            String policy = """
                    {
                      "Version": "2012-10-17",
                      "Statement": [
                        {
                          "Effect": "Allow",
                          "Principal": "*",
                          "Action": "sqs:SendMessage",
                          "Resource": "%s",
                          "Condition": {
                            "ArnEquals": {
                              "aws:SourceArn": "%s"
                            }
                          }
                        }
                      ]
                    }
                    """.formatted(queueArn, topicArn);

            sqsClient.setQueueAttributes(SetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributes(Map.of(QueueAttributeName.POLICY, policy))
                    .build());

            snsClient.subscribe(SubscribeRequest.builder()
                    .topicArn(topicArn)
                    .protocol("sqs")
                    .endpoint(queueArn)
                    .returnSubscriptionArn(true)
                    .build());

            System.out.println(" SQS queue subscribed to SNS topic");

        } catch (Exception e) {
            System.err.println(" Failed to initialize AWS services: " + e.getMessage());
            throw e;
        }
    }

   //todo use builder design here

    public void publishBudgetOverflow(String userEmail, double expenseAmount,
                                      double currentSpent, double monthlyBudget) throws Exception {
        try {
            BudgetOverflowMessage message = new BudgetOverflowMessage();
            message.userEmail = userEmail;
            message.expenseAmount = expenseAmount;
            message.currentSpent = currentSpent;
            message.monthlyBudget = monthlyBudget;
            message.overflowAmount = (currentSpent + expenseAmount) - monthlyBudget;
            message.timestamp = java.time.LocalDateTime.now().toString();
            message.action = "BUDGET_OVERFLOW";

            String messageJson = objectMapper.writeValueAsString(message);

            snsClient.publish(PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(messageJson)
                    .subject("Budget Overflow Alert")
                    .build());

            System.out.println(" Budget overflow message published for: " + userEmail);

        } catch (Exception e) {
            System.err.println(" Failed to publish budget overflow: " + e.getMessage());
            throw e;
        }
    }

    public void processMessages() throws Exception {
        try {
            List<Message> messages = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10) // Proces uptil 10 messages
                    .waitTimeSeconds(5) // Long polling
                    .build()).messages();

            for (Message message : messages) {
                System.out.println(" Received budget overflow message:");

                handleBudgetOverflowMessage(message);

                sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle())
                        .build());
            }

        } catch (Exception e) {
            System.err.println(" Error processing messages: " + e.getMessage());
        }
    }

    private void handleBudgetOverflowMessage(Message sqsMessage) throws Exception {
        try {
            Map<String, Object> snsWrapper = objectMapper.readValue(sqsMessage.body(), Map.class);
            String actualMessage = (String) snsWrapper.get("Message");

            BudgetOverflowMessage budgetData = objectMapper.readValue(actualMessage, BudgetOverflowMessage.class);

            sendNotificationToUser(budgetData);

        } catch (Exception e) {
            System.err.println(" Error handling budget message: " + e.getMessage());
        }
    }

    private void sendNotificationToUser(BudgetOverflowMessage budgetData) {
        // Display the budget alert
        System.out.printf("""
             BUDGET ALERT for %s
             Attempted Expense: $%.2f
             Current Spent: $%.2f
             Monthly Budget: $%.2f
            ️Overflow: $%.2f
             EXPENSE REJECTED!
            %n""",
                budgetData.userEmail,
                budgetData.expenseAmount,
                budgetData.currentSpent,
                budgetData.monthlyBudget,
                budgetData.overflowAmount
        );
    }

    public void startMessageProcessor() {
        Thread processorThread = new Thread(() -> {
            System.out.println(" Starting budget notification processor...");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    processMessages();
                    Thread.sleep(3000); // Check every 3 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println(" Error in message processor: " + e.getMessage());
                }
            }
        });

        processorThread.setDaemon(true);
        processorThread.start();
    }

    // Cleanup
    public void cleanup() {
        try {
            snsClient.deleteTopic(DeleteTopicRequest.builder().topicArn(topicArn).build());
            sqsClient.deleteQueue(DeleteQueueRequest.builder().queueUrl(queueUrl).build());
            System.out.println(" Cleanup completed");
        } catch (Exception e) {
            System.err.println(" Cleanup failed: " + e.getMessage());
        }
    }


    public void close() {
        snsClient.close();
        sqsClient.close();
    }

    public static class BudgetOverflowMessage {
        public String userEmail;
        public double expenseAmount;
        public double currentSpent;
        public double monthlyBudget;
        public double overflowAmount;
        public String timestamp;
        public String action;
    }
}

