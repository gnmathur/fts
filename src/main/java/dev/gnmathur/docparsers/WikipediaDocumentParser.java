package dev.gnmathur.docparsers;

import dev.gnmathur.WikiEntry;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WikipediaDocumentParser {
    private static final XMLInputFactory factory = XMLInputFactory.newInstance();

    public static List<WikiEntry> parse(InputStream xmlInputStream) throws Exception {
        XMLEventReader eventReader = factory.createXMLEventReader(xmlInputStream);
        Long id = 0L;

        List<WikiEntry> entries = new ArrayList<>();
        String currentElement = null;
        StringBuilder title = new StringBuilder();
        StringBuilder url = new StringBuilder();
        StringBuilder abstractText = new StringBuilder();

        String tempTitle = null;
        String tempUrl = null;
        String tempAbstract = null;

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                currentElement = startElement.getName().getLocalPart();
            }

            if (event.isCharacters() && currentElement != null) {
                Characters characters = event.asCharacters();
                String data = characters.getData();

                switch (currentElement) {
                    case "title":
                        title.append(data);
                        break;
                    case "url":
                        url.append(data);
                        break;
                    case "abstract":
                        abstractText.append(data);
                        break;
                }
            }

            if (event.isEndElement()) {
                String endElementName = event.asEndElement().getName().getLocalPart();

                if (endElementName.equals("doc")) {
                    if (!title.isEmpty() && !url.isEmpty() && !abstractText.isEmpty()) {
                        tempTitle = title.toString();
                        tempUrl = url.toString();
                        tempAbstract = abstractText.toString();
                        id++;
                        entries.add(new WikiEntry(tempTitle, tempUrl, tempAbstract, id));
                    }

                    title.setLength(0);
                    url.setLength(0);
                    abstractText.setLength(0);
                }
            }
        }

        eventReader.close();
        return entries;
    }
}

