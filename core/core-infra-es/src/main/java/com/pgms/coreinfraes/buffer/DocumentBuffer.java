package com.pgms.coreinfraes.buffer;

import com.pgms.coreinfraes.document.EventDocument;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DocumentBuffer {

    private static final Queue<EventDocument> eventDocuments = new ConcurrentLinkedQueue<>();

    public static void add(EventDocument eventDocument) {
        eventDocuments.add(eventDocument);
    }

    public static List<Object> getAll() {
       return Arrays.asList(eventDocuments.toArray());
    }

    public static void deleteAll() {
        eventDocuments.clear();
    }
}
