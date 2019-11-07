package com.cgi.extraction.exception;

public class AucunActiveLinkException extends Exception {
    public AucunActiveLinkException(){
        super("Aucune ligne trouvée");
    }
}
