package com.tmw.tracking.web.support;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.freemarker.FreemarkerViewProcessor;
import com.tmw.tracking.utils.GuiceInstanceHolder;
import org.apache.shiro.SecurityUtils;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom {@link FreemarkerViewProcessor}
 * @author dmikhalishin@provectus-it.com
 * @see FreemarkerViewProcessor
 */
public class CustomFreemarkerViewProcessor extends FreemarkerViewProcessor {

    private static final String CONTEXT_PATH = "contextPath";
    private static final String SHIRO = "shiro";

    public CustomFreemarkerViewProcessor(@Context ResourceConfig resourceConfig) {
        super(resourceConfig);
    }

    /**
     * {@inheritDoc}
     * @see FreemarkerViewProcessor#writeTo(String, com.sun.jersey.api.view.Viewable, java.io.OutputStream)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void writeTo(final String resolvedPath, final Viewable viewable, final OutputStream out) throws IOException {
        final String contextPath = GuiceInstanceHolder.getInjector().getInstance(ServletContext.class).getContextPath();
        if(viewable.getModel() != null && viewable.getModel() instanceof Map){
            ((Map)viewable.getModel()).put(CONTEXT_PATH, contextPath);
            ((Map)viewable.getModel()).put(SHIRO, SecurityUtils.getSubject());
            super.writeTo(resolvedPath, viewable, out);
        } else if (viewable.getModel() == null){
            super.writeTo(resolvedPath, new Viewable(viewable.getTemplateName(), new HashMap<String, String>(){{put(CONTEXT_PATH, contextPath);}}), out);
        } else {
            super.writeTo(resolvedPath, viewable, out);
        }
    }
}
