package com.dev.voltsoft.lib.network.parse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class XMLParseHelper
{

    public static Document getDocmentElement(String s)
    {
        Document document = null;

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try
        {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            InputSource inputSource = new InputSource();

            inputSource.setCharacterStream(new StringReader(s));

            document = documentBuilder.parse(inputSource);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return document;
    }

    public static String getValue(Element element, String name)
    {
        NodeList nodeList = element.getElementsByTagName(name);

        return getElementValue(nodeList.item(0));
    }

    public static String getElementValue(Node node)
    {
        Node child = null;

        if (node != null && node.hasChildNodes())
        {
            for (child = node.getFirstChild() ; child != null ; child = node.getNextSibling())
            {
                if (child.getNodeType() == Node.TEXT_NODE)
                {
                    return node.getNodeValue();
                }
            }
        }

        return "";
    }
}
