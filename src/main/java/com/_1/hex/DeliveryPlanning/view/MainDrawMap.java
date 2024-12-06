package com._1.hex.DeliveryPlanning.view;


import com._1.hex.DeliveryPlanning.model.Intersection;
import com._1.hex.DeliveryPlanning.model.StreetMap;
import com._1.hex.DeliveryPlanning.service.GraphService;
import com._1.hex.DeliveryPlanning.service.XmlParser;
import org.jgrapht.alg.util.Pair;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.List;

@Component
public class MainDrawMap {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable()  {

            @Override
            public void run() {
                DrawMap drawMap = new DrawMap();
                XmlParser xmlParser = new XmlParser();
                GraphService graphService = new GraphService();

                StreetMap map;
                String xmlPath = "src/main/java/com/_1/hex/DeliveryPlanning/utils/petitPlan.xml";
                try {
                    map = xmlParser.parse(xmlPath);
                    graphService.addMap(map);
                    
                    Intersection source = map.getIntersectionById(208769039L);
                    Intersection destination = map.getIntersectionById(25173820L);
                
                    Pair<List<Integer>, Double> path = graphService.computeTheShortestPath(source,destination);
                    drawMap.defineRoute(path.getFirst());

                    
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (XMLStreamException e) {
                    throw new RuntimeException(e);
                }

                drawMap.defineStreetMap(map);

                drawMap.setVisible(true);

            }
        });
    }
}
