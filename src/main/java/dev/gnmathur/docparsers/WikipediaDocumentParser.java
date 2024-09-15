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

        // Temporary variables for batching parsed data
        String tempTitle = null;
        String tempUrl = null;
        String tempAbstract = null;

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            // Handle the start of an element
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                currentElement = startElement.getName().getLocalPart();
            }

            // Handle character data (text between tags)
            if (event.isCharacters() && currentElement != null) {
                Characters characters = event.asCharacters();
                String data = characters.getData();

                // Append the data directly without trimming or additional processing
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

            // Handle the end of an element
            if (event.isEndElement()) {
                String endElementName = event.asEndElement().getName().getLocalPart();

                // When we reach the end of a "doc" element, create a new WikiEntry
                if (endElementName.equals("doc")) {
                    if (!title.isEmpty() && !url.isEmpty() && !abstractText.isEmpty()) {
                        tempTitle = title.toString();
                        tempUrl = url.toString();
                        tempAbstract = abstractText.toString();
                        id++;
                        entries.add(new WikiEntry(tempTitle, tempUrl, tempAbstract, id));
                    }

                    // Reset the StringBuilder for the next entry
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

