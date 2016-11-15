package com.tmw.tracking.web.controller;
/**
 * User: cvs5
 * Date: 12/29/15
 * Time: 3:31 PM
 */

public class BaseController {
    protected final String environment = System.getProperties().getProperty("tracking.env", "N/A").toUpperCase();

}
