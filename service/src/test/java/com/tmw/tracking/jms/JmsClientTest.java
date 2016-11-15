package com.tmw.tracking.jms;

/**
 * @author ak140 on 7/21/16.
 * https://www.ibm.com/support/knowledgecenter/SSFKSJ_8.0.0/com.ibm.mq.dev.doc/q032190_.htm
 *
 * This tool allows to view and put messages to MQ
 * https://sourceforge.net/projects/jackibmmqexplorer/files/
 *
 */
public class JmsClientTest {

    public static String MQ_QUEUE_NAME = "UNIV.TAILORAPP.ITEM.IB";
    public static String MQ_CONNECTION_HOSTNAME = "mqhatstsubcorp.tmw.com";
    public static int MQ_CONNECTION_PORT = 2024;
    public static String MQ_CONNECTION_CHANNEL = "ENTAPPS.LB.SVRCON";


    /*private Connection _connectionInternal;

    public Connection getConnection() {
        if (this._connectionInternal == null) {
            this._connectionInternal = this.initConnection();
        }
        return this._connectionInternal;
    }

    private Connection initConnection() {

//        ConnectionFactory factory = new
        MQQueueConnectionFactory factory = new MQQueueConnectionFactory();

        Connection connection = null;
        try {
            factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            //        factory.setQueueManager("QM1");
            factory.setHostName(MQ_CONNECTION_HOSTNAME);
            factory.setPort(MQ_CONNECTION_PORT);
            factory.setChannel(MQ_CONNECTION_CHANNEL);


            connection = factory.createQueueConnection();


        } catch (JMSException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(connection);


        return connection;
    }

    @Test
    public void testJms() {

        Connection connection = getConnection();

        try {
            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }


        Session session = null;
        try {
            session = connection.createSession();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(session);


        Queue queue = null;
        try {
            queue = new MQQueue(MQ_QUEUE_NAME);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(queue);

        QueueBrowser queueBrowser = null;
        try {
             queueBrowser = session.createBrowser(queue);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(queueBrowser);

        Enumeration msgs = null;
        try {
            msgs = queueBrowser.getEnumeration();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(msgs);
        if ( !msgs.hasMoreElements() ) {
            System.out.println("No messages in queue");
        } else {
            Message tempMsg = (Message)msgs.nextElement();
            System.out.println("Message: " + tempMsg);
        }


        // end
        try {
            session.close();
            connection.stop();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }*/

}
