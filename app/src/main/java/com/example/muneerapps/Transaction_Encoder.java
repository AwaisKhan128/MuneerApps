package com.example.muneerapps;

import android.util.Base64;

public class Transaction_Encoder {
    public String encoded,decoded;

    public Transaction_Encoder()
    {

    }

    public String getEncoded(String text)
    {
        return encoded = Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }

    public String getDecoded(String encoded)
    {

       return new String(Base64.decode(encoded, Base64.DEFAULT));
    }



}
