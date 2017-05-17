package com.example.kevin_sct.beastchat;

import java.net.InetAddress;

/**
 * Created by kevin_sct on 5/16/17.
 */

public class Config {

    //本机唯一UUID
    public static final java.util.UUID UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static InetAddress CONNECTED_OWNER_IP = null;

    public static P2pRole CURRENT_ROLE = P2pRole.NONE;
    public enum P2pRole{
        GROUP_OWNRR,GROUP_MEMBER,NONE
    }
}
