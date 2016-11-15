package com.tmw.tracking.service;

import java.util.Map;

/**
 * Created by vandreev on 11/21/2014.
 */
public interface ReportService {

    byte[] generatePDF(String xslFO) throws Exception ;

    byte[] generatePDF(String xslFO, Map<String, Object> valueObjectMap) throws Exception ;
    
    byte[] generatePdfFromHtml(String htmlBody);
    
    String reportBody(final Map<String, Object> data, final String templateURL);


}
