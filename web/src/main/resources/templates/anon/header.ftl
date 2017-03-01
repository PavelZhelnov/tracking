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
            <a href="${contextPath}/tmw/status" class="brand">So-Tracking Management</a>
        <#if !hide_top_nav??>
            <div class="nav-collapse collapse">
                <ul class="nav">
                    <li><a href="${contextPath}/tmw/tracking/trackContainer">Track Container</a></li>
                    <li><a href="${contextPath}/tmw/anon/showpage?page=contact">Contact</a></li>
                    <li><a href="${contextPath}/tmw/anon/showpage?page=test">Test Page</a></li>

                    <#if shiro.principal??>
                        <li><a href="${contextPath}/tmw/status">Admin</a></li>
                    <#else>
                        <li><a href="${contextPath}/tmw/login">Login</a></li>
                    </#if>
                </ul>
            </div>
       </#if>
        </div>
    </div>
</div>
<div class="container">
<div style="padding-top: 60px; padding-bottom: 60px;">