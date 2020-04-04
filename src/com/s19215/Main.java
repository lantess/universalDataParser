package com.s19215;

import XML.*;

public class Main {

    public static void main(String[] args) {
        try {
            XMLTag atg = new XMLTag("note", "test text", "date=12/11/2007 async style=txt rel");
            System.out.println(atg);
        } catch (XMLSyntaxError e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
