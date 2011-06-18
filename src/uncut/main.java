package uncut;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.servlet.http.*;
import java.util.regex.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class main extends HttpServlet {
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
        
        resp.setContentType("text/plain");
        
        try {
            URL url = new URL("http://www.uncut-magazin.com/?feed=rss2");
            BufferedInputStream stream = new BufferedInputStream(url.openStream());
            
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(stream);
            stream.close();
            
            Element channel = doc.getDocumentElement();
            NodeList items = doc.getElementsByTagName("item");
            
            for(int i=0; i<items.getLength();i++)
            {
                
                Node nodeitem = items.item(i);
                if (nodeitem.getNodeType() == Node.ELEMENT_NODE) {
                    Element item = (Element) nodeitem;
                    Node content = item.getElementsByTagName("content:encoded").item(0).getFirstChild();
                    Pattern p = Pattern.compile("(<script id=\"clipkit_src_)(\\d+)");
                    Matcher m = p.matcher(content.getNodeValue());
                    String srcString = "";
                    if(m.find()){
                        String id = m.group(2);
                        URL jsonurl = new URL("http://signin.clipkit.de/172/video/" + id + "/player/341/format/4/cache/config.json");
                        BufferedInputStream jsonstream = new BufferedInputStream(jsonurl.openStream());                      
                        //create a byte array
                        byte[] contents = new byte[1024];
                        
                        int bytesRead=0;
                        String jsonstring = new String();
                        
                        while( (bytesRead = jsonstream.read(contents)) != -1){
                            jsonstring += new String(contents, 0, bytesRead);
                        }
                        
                        jsonstring = jsonstring.substring(1,jsonstring.length()-1);
                        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON( jsonstring );
                        srcString = jsonObject.getJSONObject("clip").getString("baseUrl")+"/"+jsonObject.getJSONArray("playlist").getJSONObject(0).getString("url");

                    }
                    
                    

                    
                    Element enclosure = doc.createElement("enclosure");
                    Attr src = doc.createAttribute("src");
                    src.setValue(srcString );
                    Attr type = doc.createAttribute("type");
                    type.setValue("video/mp4");
                    enclosure.setAttributeNode(src);
                    enclosure.setAttributeNode(type);
                    item.appendChild(enclosure);
                }
            }
             

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            
            StreamResult result =  new StreamResult(resp.getWriter());
            transformer.transform(source, result);
            
            
            
        } catch (MalformedURLException e) {
            System.out.println(e.toString());
            
        } catch (IOException e) {
            System.out.println(e.toString());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        
        
        
    }
    
}