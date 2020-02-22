package com.example.yichuguanjia2;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PullXmlUtil {
    private InputStream is;
    public PullXmlUtil(InputStream is){
        this.is=is;
    }
    public List getAllElement() throws XmlPullParserException, IOException {
        List myData=null;
        XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
        XmlPullParser xpp=factory.newPullParser();
        xpp.setInput(is,"UTF-8");
        int eventType=xpp.getEventType();
        String elementName="";
        while(eventType!=XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    myData=new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    elementName=xpp.getName();
                    break;
                case XmlPullParser.TEXT:
                    if(elementName.equals("string"))
                        myData.add(xpp.getText());
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType=xpp.next();
        }
        return myData;
    }
}

