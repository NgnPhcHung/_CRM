package CRM_APP.Handler;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

public class EmailReader {
    Folder inbox;
    public int unReadMsg = 0;
    private static HashMap<String, String > emailHm = new HashMap<String, String>();
    // Constructor of the calss.

    public EmailReader() {
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        /* Set the mail properties */

        Properties props = System.getProperties();
        // Set manual Properties
        props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.socketFactory.port", "995");
        props.put("mail.pop3.host", "pop.gmail.com");
        try {
            /* Create the session and get the store for read the mail. */
            Session session = Session.getDefaultInstance(
                    System.getProperties(), null);

            Store store = session.getStore("pop3");

            store.connect("pop.gmail.com", 995, "phuchungquanly@gmail.com",
                    "phuchungquanly123");

            /* Mention the folder name which you want to read. */

            // inbox = store.getDefaultFolder();
            // inbox = inbox.getFolder("INBOX");
            inbox = store.getFolder("INBOX");

            /* Open the inbox using store. */
            inbox.open(Folder.READ_ONLY);
            /* Get the messages which is unread in the Inbox */

            Message[] messages = inbox.search(new FlagTerm(new Flags(
                    Flags.Flag.SEEN), false));
            System.out.println("No. of Unread Messages : " + messages.length);
            unReadMsg = messages.length;

            /* Use a suitable FetchProfile */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            inbox.fetch(messages, fp);
            try {
                printAllMessages(messages);
                inbox.close(true);
                store.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            System.exit(2);
        }

    }

    public void printAllMessages(Message[] msgs) throws Exception {
        for (int i = 0; i < msgs.length; i++) {
            printEnvelope(msgs[i]);
        }

    }

    public void printEnvelope(Message message) throws Exception  {
        Address[] a;
        // FROM
        if ((a = message.getFrom()) != null) {
            for (int j = 0; j < a.length; j++) {
                System.out.println("FROM: " + a[j].toString());
            }
        }
        // TO
        if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++) {
                System.out.println("TO: " + a[j].toString());
            }
        }
        String subject = message.getSubject();
        Date receivedDate = message.getReceivedDate();
        Date sentDate = message.getSentDate(); // receivedDate is returning
        // null. So used getSentDate()

        String content = message.getContent().toString();
        System.out.println("Subject : " + subject);
        if (receivedDate != null) {
            System.out.println("Received Date : " + receivedDate.toString());
        }
        System.out.println("Sent Date : " + sentDate.toString());
        System.out.println("Content : " + content);

        getContent(message);
    }

    public void getContent(Message msg){
        try {
            Multipart mp = (Multipart) msg.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {

                HashMap<String, String > mail = new HashMap<String, String>();
                String[] container = dumpPart(mp.getBodyPart(i)).split("-");
                mail.put("from", container[0]);
                mail.put("message", container[1]);
                mail.put("unReadMsg", unReadMsg+"");
                setEmailHm(mail);
            }
        } catch (Exception ex) {
            System.out.println("Exception arise at get Content");
            ex.printStackTrace();
        }
    }

    public String dumpPart(Part p) throws Exception {
        // Dump input stream ..
        InputStream is = p.getInputStream();
        // If "is" is not already buffered, wrap a BufferedInputStream
        // around it.
        if (!(is instanceof BufferedInputStream)) {
            is = new BufferedInputStream(is);

        }
        int c;
        System.out.println("Message : ");
        InputStreamReader isReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        while((str = reader.readLine())!= null){
            sb.append(str);
        }

        String[] output = sb.toString().split("@1#2!3");
        String name  = output[0];
        String content = output[1];
        String res = name + "-" + content;
        return res;
    }

    public static HashMap<String, String> getEmailHm() {
        return emailHm;
    }

    public void setEmailHm(HashMap<String, String> emailHm) {
        this.emailHm = emailHm;
    }
}
