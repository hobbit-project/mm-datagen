//package org.hobbit.sml.datagen.Utils;
//
//import com.rabbitmq.client.MessageProperties;
//import org.apache.commons.io.IOUtils;
//import org.hobbit.core.data.RabbitQueue;
//import org.hobbit.core.rabbit.RabbitMQUtils;
//import org.hobbit.core.rabbit.RabbitQueueFactory;
//
//import java.io.Closeable;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.ByteBuffer;
//import java.util.Arrays;
//
//public class SimpleFileSender2 implements Closeable {
//    private static final int DEFAULT_MESSAGE_SIZE = 65536;
//    private RabbitQueue queue;
//    private int messageSize = 65536;
//
//    public static SimpleFileSender2 create(RabbitQueueFactory factory, String queueName) throws IOException {
//        return new SimpleFileSender2(factory.createDefaultRabbitQueue(queueName));
//    }
//
//    protected SimpleFileSender2(RabbitQueue queue) {
//        this.queue = queue;
//    }
//
//    public void streamData(InputStream is, String name) throws IOException {
//        int messageId = 0;
//        int length = 0;
//        byte[] nameBytes = RabbitMQUtils.writeString(name);
//        byte[] array = new byte[this.messageSize + nameBytes.length + 8];
//        ByteBuffer buffer = ByteBuffer.wrap(array);
//        buffer.putInt(nameBytes.length);
//        buffer.put(nameBytes);
//        int messageIdPos = buffer.position();
//        int dataStartPos = messageIdPos + 4;
//
//        int length;
//        do {
//            buffer.position(messageIdPos);
//            buffer.putInt(messageId);
//            length = is.read(array, dataStartPos, array.length - dataStartPos);
//            this.queue.channel.basicPublish("", this.queue.name, MessageProperties.MINIMAL_PERSISTENT_BASIC, Arrays.copyOf(array, length > 0 ? dataStartPos + length : dataStartPos));
//            ++messageId;
//        } while(length > 0);
//
//    }
//
//    public void setMessageSize(int messageSize) {
//        this.messageSize = messageSize;
//    }
//
//    public void close() {
//        IOUtils.closeQuietly(this.queue);
//    }
//}