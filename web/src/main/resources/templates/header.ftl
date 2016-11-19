<html <#if angular??>ng-app="app"</#if>>
<head>
    <title>${page_title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- ================================================================== -->
    <!-- Bootstrap -->
    <link href="${contextPath}/css/bootstrap.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/datepicker.css" rel="stylesheet" media="screen">
    <!-- data table -->
    <link href="${contextPath}/css/dt_bootstrap.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/kendo.common.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/kendo.bootstrap.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/fullcalendar.css" rel="stylesheet" media="screen">
    <!-- ================================================================== -->
    <!-- jquery -->
    <script src="${contextPath}/js/jquery/jquery.min.js"></script>
    <script src="${contextPath}/js/jquery/jquery-ui.min.js"></script>
    <script src="${contextPath}/js/jquery/jquery.printElement.min.js"></script>
    <!-- data table -->
    <script src="${contextPath}/js/dataTable/jquery.dataTables.min.js"></script>
<#--<script src="${contextPath}/js/dataTable/jquery.tableTools.min.js"></script>-->
    <script src="${contextPath}/js/dataTable/dt_bootstrap.js"></script>
    <!-- bootstrap -->
    <script src="${contextPath}/js/bootstrap/bootstrap.min.js"></script>
    <script src="${contextPath}/js/bootstrap/bootstrap-datepicker.js"></script>
<#--<script src="${contextPath}/js/bootstrap/bootstrap-modal.js"></script>-->
    <!-- other -->
    <script src="${contextPath}/js/support.js"></script>


    <!-- angular -->
<#if angular??>
    <script src="${contextPath}/js/angular/angular.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-strap.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-strap.min.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-strap.tpl.min.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/ui-bootstrap-tpls-0.11.0.min.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/kendo.web.min.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-kendo.js"></script>
    <script src="${contextPath}/js/fullcalendar.min.js"></script>
    <script src="${contextPath}/js/gcal.js"></script>
    <script src="${contextPath}/js/moment.js"></script>
    <script src="${contextPath}/js/moment-timezone.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/calendar.js"></script>

</#if>
</head>
<body>

<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container" style="position: relative;">
            <button data-target=".nav-collapse" data-toggle="collapse" class="btn btn-navbar" type="button">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="${contextPath}/tmw/status" class="brand">Tracking Maintenance Tools (${environment})</a>
        <#if !hide_top_nav??>
            <div class="nav-collapse collapse">
                <ul class="nav">
                    <#if shiro.isPermitted("SHOW_STATUS")>
                        <li <#if top_nav_selected == 'status'>class="active"</#if>><a href="${contextPath}/tmw/status">Status</a>
                        </li>
                    </#if>
                    <li><a href="${contextPath}/tmw/tracking/trackContainer">Track Container</a></li>
                    <#if shiro.isPermitted("SHOW_USERS")|| shiro.isPermitted("SHOW_ORDER_STATUS")|| shiro.isPermitted("SHOW_ORDER_INFO")||
                    shiro.isPermitted("SHOW_ALTERATION_PRICE")|| shiro.isPermitted("SHOW_ALTERED_GARMENT_INFO")||
                    shiro.isPermitted("SHOW_STORE_PRINTERS")||shiro.isPermitted("SHOW_USER_MANAGEMENT") ||shiro.isPermitted("SHOW_DEVICE_MANAGEMENT")
                    ||shiro.isPermitted("SHOW_STORE_INFO_MANAGEMENT") || shiro.isPermitted("SHOW_STORE_INFO_MANAGEMENT")>
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">System Tools<b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <#if shiro.isPermitted("SHOW_USERS")>
                                    <li><a href="${contextPath}/tmw/userstore/userManagement">User management</a></li>
                                </#if>
                                <#if shiro.isPermitted("SHOW_SYSTEM_CONFIG_MANAGEMENT")>
                                    <li><a href="${contextPath}/tmw/storeInfo/config">System Configuration</a></li>
                                </#if>
                                <#if shiro.isPermitted("SHOW_SYSTEM_MONITORING")>
                                    <li><a href="${contextPath}/tmw/monitoring/tests">System Monitoring</a></li>
                                </#if>

                            </ul>
                        </li>
                    </#if>
                </ul>
            </div>
            <#if shiro.principal??>
                <div class="navbar-inverse brand" style="position: absolute; left: 50px;top:35px;">
                ${shiro.principal.email}
                    <#if shiro.principal.roles?? && shiro.principal.roles?size &gt; 0>
                        (${shiro.principal.roles[0].roleName})
                    <#else>
                        (None)
                    </#if>

                </div>

                <div style="position: absolute; left: 1000px;top:2px;">
                    <ul class="nav nav-pills">
                        <li><a href="${contextPath}/tmw/logout">Logout</a></li>
                    </ul>
                </div>
            </#if>
        </#if>
        </div>
    </div>
</div>
<div class="container">
    <div style="padding-top: 60px; padding-bottom: 60px;">