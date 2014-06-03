package org.exoplatform.addons.persistence.utils;

import org.apache.commons.codec.binary.Base64;
import org.exoplatform.addons.persistence.services.ChatService;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class ChatUtils {

  public static String getRoomId(String roomName, String user)
  {
    if (roomName.startsWith(ChatService.TEAM_PREFIX) && roomName.length()>ChatService.TEAM_PREFIX.length()+1)
    {
      return roomName.substring(ChatService.TEAM_PREFIX.length());
    }

    StringBuilder sb = new StringBuilder();
    sb.append("1-team-room;").append(user).append(";").append(roomName).append(";");

    return MessageDigester.getHash(sb.toString());
  }

  public static String getRoomId(String roomName)
  {
    if (roomName.startsWith(ChatService.SPACE_PREFIX) && roomName.length()>ChatService.SPACE_PREFIX.length()+1)
    {
      return roomName.substring(ChatService.SPACE_PREFIX.length());
    }

    StringBuilder sb = new StringBuilder();
    sb.append("1-space-room;").append(roomName).append(";");

    return MessageDigester.getHash(sb.toString());
  }

  public static String getExternalRoomId(String identifier)
  {
    if (identifier.startsWith(ChatService.EXTERNAL_PREFIX) && identifier.length()>ChatService.EXTERNAL_PREFIX.length()+1)
    {
      return identifier.substring(ChatService.EXTERNAL_PREFIX.length());
    }

    StringBuilder sb = new StringBuilder();
    sb.append("1-external-room;").append(identifier).append(";");

    return MessageDigester.getHash(sb.toString());
  }

  public static String getRoomId(List<String> users)
  {
    Collections.sort(users);
    StringBuilder sb = new StringBuilder();
    for (String user:users)
    {
      sb.append(user).append(";");
    }

    String roomId = MessageDigester.getHash(sb.toString());
    return roomId;
  }

  /** Read the object from Base64 string. */
  public static Object fromString( String s ) throws IOException ,
          ClassNotFoundException {
    byte [] data = Base64.decodeBase64( s );
    ObjectInputStream ois = new ObjectInputStream(
            new ByteArrayInputStream(  data ) );
    Object o  = ois.readObject();
    ois.close();
    return o;
  }

  /** Write the object to a Base64 string. */
  public static String toString( Serializable o ) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream( baos );
    oos.writeObject( o );
    oos.close();
    return new String( Base64.encodeBase64( baos.toByteArray() ) );
  }
}
