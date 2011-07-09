//A simple Google App Engine application to make a valid podcast feed from the RSS Feed of the TV-Show www.uncut-magazin.com
//Copyright (C) 2011  cylonbrain <cylonbrain@gmail.com>
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.


package uncut;
//DataStore
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.net.URL;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
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
        

        
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        
        System.out.println("GET: " + req.getRequestURI());
        if(req.getRequestURI().equalsIgnoreCase("/feed")){
            resp.setContentType("application/rss+xml; charset=UTF-8");
            try {
                URL url = new URL("http://www.uncut-magazin.com/feed/");
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
                        if(m.find()){
                            String id = m.group(2);
                            URL jsonurl = new URL("http://signin.clipkit.de/172/video/" + id + "/player/341/format/4/cache/config.json");
                            BufferedInputStream jsonstream = new BufferedInputStream(jsonurl.openStream());                      
                            String jsonstring = getStringFromInputStream(jsonstream);
                            jsonstring = jsonstring.substring(1,jsonstring.length()-1);
                            JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON( jsonstring );
                            
                            String baseUrl = jsonObject.getJSONObject("clip").getString("baseUrl");
                            
                            JSONArray contentURLs = jsonObject.getJSONArray("playlist");
                            String contentURL = contentURLs.getJSONObject(0).getString("url");;
                            for(int j=0;j < contentURLs.size(); j++){
                                 contentURL = contentURLs.getJSONObject(j).getString("url");
                                if(contentURL.matches(".*\\.mp4.*")) break;
                            }
                           
                            String srcString = baseUrl +"/"+ contentURL;
                            
                            
                            System.out.println(srcString);
                            Element enclosure = doc.createElement("enclosure");
                            Attr src = doc.createAttribute("url");
                            src.setValue(srcString );
                            Attr type = doc.createAttribute("type");
                            type.setValue("video/mp4");
                            Attr length = doc.createAttribute("length");
                            length.setValue("250000000");
                            enclosure.setAttributeNode(length);
                            enclosure.setAttributeNode(type);
                            enclosure.setAttributeNode(src);
                            item.appendChild(enclosure);
                            
                            /*Key episodeKey = KeyFactory.createKey("Episodes", id);
                            Entity episode = new Entity("episode", episodeKey);
                            episode.setProperty("url", srcString);
                            datastore.put(episode);*/
                        }
                        
                    }
                }
                 

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                
                StreamResult result =  new StreamResult(resp.getWriter());
                transformer.transform(source, result);
                
                
                
            } catch (MalformedURLException e) {
                System.out.println(e.toString());
                
            } catch (SocketTimeoutException e) {
                System.out.println("Feed currently not avilable. Try it agian later." + e.toString());
            } catch (IOException e) {
                System.out.println("Feed not avilable" + e.toString());
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        
        
    }
    public void parseFeed(){
        
    }
    public String getStringFromInputStream(InputStream stream) throws IOException{
        byte[] contents = new byte[1024];
        int bytesRead=0;
        String oString = new String();
        while( (bytesRead = stream.read(contents)) != -1){
            oString += new String(contents, 0, bytesRead);
        }
        return oString;
    }
    
}