package com.noogler.annotationprocessing.processor;

import javax.lang.model.element.Element;

class ProcessingException extends Exception {
    Element element;

    public ProcessingException(Element element, String exceptionMsg, Object... args) {
        super(String.format(exceptionMsg, args));
        this.element = element;
    }

    public Element getElement() {
        return this.element;
    }
}