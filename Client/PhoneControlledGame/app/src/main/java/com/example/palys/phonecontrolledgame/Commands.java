package com.example.palys.phonecontrolledgame;

/**
 * Created by Natala on 2015-05-10.
 */
public enum Commands {

    AUTHENTICATE,
    REQUEST_NAME,
    ACK,
    END;

    public byte[] getBytes(){
//        byte[] bytes = new byte[toString().length()+2];
//        bytes[0] = 0x02;
//        bytes[bytes.length-1] = 0x03;
//        System.arraycopy(toString().getBytes(),0,bytes,1,toString().length());
//        return bytes;
        return toString().getBytes();
    }

}
