package com.wind.service.util;

import com.wind.manager.exception.AdvertException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * xml解析工具类
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/19 10:51
 **/
public class XmlUtils {
    private XmlUtils() {

    }

    /**
     * xm格式的string转Document
     *
     * @param str
     * @return
     */
    public static Document parseDocument(String str) {
        StringBuilder sXML = new StringBuilder();
        sXML.append(str);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try (InputStream in = new ByteArrayInputStream(sXML.toString().getBytes("utf-8"))) {
            doc = dbf.newDocumentBuilder().parse(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return doc;
    }

    /**
     * 获取360的accesstoken
     * @param xmlStr
     * @return
     */
    public static String get360AccessToken(String xmlStr){
        Document doc = parseDocument(xmlStr);
        NodeList nodeList = doc.getElementsByTagName("account_clientLogin_response");
        Element element = (Element) nodeList.item(0);
        NodeList childNodeList = element.getElementsByTagName("accessToken");
        if (childNodeList.getLength() >= 1) {
            return childNodeList.item(0).getTextContent();
        }
        Element failuresElement = (Element) element.getElementsByTagName("failures").item(0);
        Element itemElement = (Element) failuresElement.getElementsByTagName("item").item(0);
        throw new AdvertException("360 获取accessToken失败:" + itemElement.getElementsByTagName("message").item(0).getTextContent());
    }

    public static String getNodeValueByXmlStr(String xmlStr, String nodeName) throws Exception {
        Document doc = parseDocument(xmlStr);
        NodeList nodeList = doc.getChildNodes();
        return getNodeValue(nodeList, nodeName);

    }

    public static String getNodeValue(Node node, String nodeName) {
        NodeList nodeList = node.getChildNodes();
        return getNodeValue(nodeList, nodeName);
    }

    public static String getNodeValue(NodeList nodeList, String nodeName) {
        Node tempNode = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            tempNode = nodeList.item(i);
            if (nodeName.equals(tempNode.getNodeName())) {
                return tempNode.getTextContent();
            } else {
                getNodeValue(tempNode, nodeName);
            }
        }
        return null;
    }

    public static String getFailuresMessage(Node node) {
        Node firstChild = node.getFirstChild();
        Node lastChild = firstChild.getLastChild();
        return lastChild.getTextContent();
    }

    /**
     * 获取节点的值
     *
     * @param doc
     * @param nodeName
     * @return
     */
    public static String getNodeValue(Document doc, String nodeName) {
        NodeList nodeList = doc.getElementsByTagName(nodeName);
        Node nod = nodeList.item(0);
        return nod.getNodeValue();
    }
}
