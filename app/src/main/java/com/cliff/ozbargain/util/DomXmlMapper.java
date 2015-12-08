package com.cliff.ozbargain.util;

import com.cliff.ozbargain.model.Deal;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.spec.ECField;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Clifford on 30/11/2015.
 */
public class DomXmlMapper {
    private static final DateFormat rssDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

    private static final String TAG=DomXmlMapper.class.getSimpleName();
    public static List<Deal> xmlToDeal(InputStream xmlData) throws Exception {
        List<Deal> dealList = new ArrayList<>(10);
        Document dealsDoc = getDomDocument(xmlData);
        xmlData.close();
        NodeList deals = dealsDoc.getElementsByTagName("item");
        Node dealNode=null;
        NodeList dealDetails = null;
        Node dealDetail = null;

        for (int i=0; i<deals.getLength();i++){
            dealNode=deals.item(i);
            dealDetails=dealNode.getChildNodes();
            Deal deal = new Deal();
            for (int j = 0; j < dealDetails.getLength(); j++) {
                dealDetail= dealDetails.item(j);
                extractDealfromNode(deal, dealDetail);
            }
            dealList.add(deal);
        }
        return dealList;
    }

    public static List<Deal> xmlToDeal(String xmlData) throws Exception {
        List<Deal> dealList = new ArrayList<>(10);
        Document dealsDoc = getDomDocument(xmlData);
        NodeList deals = dealsDoc.getElementsByTagName("item");
        Node dealNode=null;
        NodeList dealDetails = null;
        Node dealDetail = null;

        for (int i=0; i<deals.getLength();i++){
            dealNode=deals.item(i);
            dealDetails=dealNode.getChildNodes();
            Deal deal = new Deal();
            for (int j = 0; j < dealDetails.getLength(); j++) {
                dealDetail= dealDetails.item(j);
                extractDealfromNode(deal, dealDetail);
            }
            dealList.add(deal);
        }
        return dealList;
    }

    private static Deal extractDealfromNode(Deal deal, Node dealDetail) {

        switch (dealDetail.getNodeName().toLowerCase()){
            case "title" : deal.setTitle(dealDetail.getTextContent());break;
            case "description" : deal.setDescription(dealDetail.getTextContent());break;
            case "pubdate" :
                try {
                    deal.setDate(rssDateFormat.parse(dealDetail.getTextContent()));
                } catch (Exception e) {
                    deal.setDate(new Date());
                }
                break;
            case "link" : deal.setOzDealLink(dealDetail.getTextContent());break;
            case "category" : deal.getCategories().add(dealDetail.getTextContent()); break;
            case "dc:creator" : deal.setCreator(dealDetail.getTextContent());break;
            case "comments" : deal.getComments().addAll(getDealComments(dealDetail.getTextContent()));break;
            case "ozb:meta" : processOZBMeta(deal, dealDetail);break;


        }
        return deal;

    }

    private static void processOZBMeta(Deal deal, Node dealDetail) {
        NamedNodeMap metaDetail = dealDetail.getAttributes();


        deal.setPosRating(toInt(metaDetail.getNamedItem("votes-pos").getTextContent()));
        deal.setNegRating(toInt(metaDetail.getNamedItem("votes-neg").getTextContent()));
        deal.setExtDealUrl(metaDetail.getNamedItem("url").getTextContent());

        deal.setCommentCount(toInt(metaDetail.getNamedItem("comment-count").getTextContent()));
        deal.setClickCount(toInt(metaDetail.getNamedItem("click-count").getTextContent()));
        L.d(TAG, deal.getTitle());
        deal.setImageUri(getAttributeValue(metaDetail.getNamedItem("image")));
        L.d(TAG, deal.getImageUri());

    }

    private static String getAttributeValue(Node attribute) {
        return attribute!=null?attribute.getTextContent():"";
    }


    //TODO : to be processed
    private static List<String> getDealComments(String commentsUrl) {
        return Collections.emptyList();
    }

    private static Document getDomDocument(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(inputStream);

        } catch (ParserConfigurationException e) {
            L.d(TAG,"Error while parsing xml data"+e.getMessage(),e);
            throw e;
        } catch (SAXException e) {
            L.d(TAG, "Error while parsing xml data" + e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            L.d(TAG,"Error while parsing xml data"+e.getMessage(),e);
            throw e;
        }
    }


    private static Document getDomDocument(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            return builder.parse(is);

        } catch (ParserConfigurationException e) {
            L.d(TAG,"Error while parsing xml data"+e.getMessage(),e);
            throw e;
        } catch (SAXException e) {
            L.d(TAG, "Error while parsing xml data" + e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            L.d(TAG,"Error while parsing xml data"+e.getMessage(),e);
            throw e;
        }
    }

    private static int toInt(String integerVal){
        try {
            return Integer.parseInt(integerVal);
        }catch (Exception e){
            return 0;
        }
    }
}
