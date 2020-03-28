package ro.ubb.socket.common.infrastructure;

import java.io.*;

public class Message {
  public static final int PORT = 1234;
  public static final String HOST = "localhost";

  private String header;
  private String body;

  public Message() {}

  public Message(String header, String body) {
    this.header = header;
    this.body = body;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public void writeTo(OutputStream os) throws IOException {
    String messageToPrint = header + System.lineSeparator() + body + System.lineSeparator();
    // System.out.println(messageToPrint);
    os.write(messageToPrint.getBytes());
  }

  public void readFrom(InputStream is) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String bufferMessage = "";
    do {
      System.out.println(bufferMessage);
      bufferMessage += br.readLine();
      bufferMessage += System.lineSeparator();
    } while (br.ready());
    bufferMessage = bufferMessage.substring(0, bufferMessage.length() - System.lineSeparator().length());
    String[] inputParsed = bufferMessage.split(System.lineSeparator(), 2);//fixme: it splits the data badly, so conversion crashes
                                                                                // for some reason when it splits the rows it splits the last \n off leaving \r at the end which crashes the converison
    header = inputParsed[0];                                                    // try print students for client
    if (inputParsed.length > 1) {
      body = inputParsed[1];
    }
  }

  @Override
  public String toString() {
    return "Message{" + "header='" + header + '\'' + ", body='" + body + '\'' + '}';
  }
}
