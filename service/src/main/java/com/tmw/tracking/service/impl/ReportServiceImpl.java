package com.tmw.tracking.service.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.service.ReportService;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.service.exceptions.ServiceException;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Template;
import org.apache.commons.lang.StringUtils;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;

import javax.inject.Inject;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
/**
 * User: enedzvetsky@provectus-it.com
 */
@Singleton
public class ReportServiceImpl implements ReportService {
    public static final String TIME_ZONE="timeZone";
    private final static Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    // (reuse to render multiple documents)
    private final static FopFactory fopFactory = FopFactory.newInstance();

    static {
        fopFactory.setStrictValidation(false);
        fopFactory.setTargetResolution(203);
    }

    @Inject
    public ReportServiceImpl() {

    }
    
    public byte[] generatePdfFromHtml(String htmlBody) {
        if (StringUtils.isEmpty(htmlBody)) {
            throw new ServiceException("PDF cannot be created, html is empty");
        }
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
// Now we don't use QR code
//            renderer.getSharedContext().setReplacedElementFactory(
//                    new BarcodeReplacedElementFactory(
//                            renderer.getOutputDevice()
//                    ));
            ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(renderer.getOutputDevice());
            callback.setSharedContext(renderer.getSharedContext());
            renderer.getSharedContext().setUserAgentCallback(callback);
            renderer.setDocumentFromString(htmlBody);
            renderer.layout();
            renderer.createPDF(os);
            return os.toByteArray();
        } catch (Exception e) {
            logger.error("PDF cannot be created", e);
            throw new ServiceException("PDF cannot be created. " + Utils.exceptionReason(e));
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

    }
    
    private static class ResourceLoaderUserAgent extends ITextUserAgent {
        public ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
            super(outputDevice);
        }

        protected InputStream resolveAndOpenStream(String uri) {
            InputStream is = super.resolveAndOpenStream(uri);
            return is;
        }
    }


    @Override
    public byte[] generatePDF(String xslFO, Map<String, Object> valueObjectMap) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        StringReader source = new StringReader(xslFO);
        try {
            // Step 3: Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, outStream);

            // Step 4: Setup JAXP using identity transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(); // identity transformer

            // Step 5: Setup input and output for XSLT transformation
            // Setup input stream
            Source src = new StreamSource(source);

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Step 6: Transform external objects
            for (Map.Entry<String, Object> entry: valueObjectMap.entrySet()) {
                transformer.setParameter(entry.getKey(), entry.getValue());
            }

            // Step 7: Start XSLT transformation and FOP processing
            transformer.transform(src, res);

        } catch(Exception e) {
            logger.error("Incorrect xsl-fo source " + e.getMessage(), e);
            throw new ServiceException("Incorrect xsl-fo source");
        } finally {
            outStream.close();
            source.close();
        }
        return outStream.toByteArray();

    }

    @Override
    public byte[] generatePDF(String xslFO) throws Exception {
        return generatePDF(xslFO, new HashMap<String, Object>());
    }

    /**
     * Generate report
     *
     * @param data        - input parameters
     * @param templateURL - template
     * @return - generated result
     */
    @Override
    public String reportBody(final Map<String, Object> data, final String templateURL) {
        BeansWrapper defaultInstance = BeansWrapper.getDefaultInstance();
        data.put("statics", defaultInstance.getStaticModels());
        try {
            final Template template = Utils.getFreemarkerTemplate(templateURL);
            TimeZone timeZone = (TimeZone) data.get(TIME_ZONE);
            if(timeZone != null) {
                template.setTimeZone(timeZone);
            }
            final StringWriter writer = new StringWriter();
            template.process(data, writer);
            return writer.toString();
        } catch(Exception e) {
            logger.error(Utils.errorToString(e));
            throw new ServiceException("Email cannot be send.");
        }
    }

}
