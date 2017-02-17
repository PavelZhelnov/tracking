package com.tmw.tracking.web.controller;

public class BaseController {
    protected final String environment = System.getProperties().getProperty("tracking.env", "N/A").toUpperCase();

}
