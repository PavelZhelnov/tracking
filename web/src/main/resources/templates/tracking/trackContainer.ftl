<#assign top_nav_selected = "trackContainer">
<#assign page_title = "Track Container">
<#include "../anon/header.ftl"/>
<fieldset ng-app="TrackContainer">

    <div ng-controller="trackingController">


        <div class="span10" style="position: relative; ">
            <div class="controls form-search well">
                <input id="container" type="text" class="span3 offset2" ng-model="container" placeholder="Enter Container Number">
                <button id="searchContainer" class="btn" type="button" ng-click="search()">Search Container</button>
                <img id="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>
                <span id="errorMessage" class="text-error">{{errorMessage}}</span>
            </div>
        </div>

        <form id="showPage" class="span10" action="#" method="post" ng-show="message">
                <div class="row pull-left" ng-bind="message"/>
        </form>
    </div>
</fieldset>

<script type="text/javascript">

    var app = angular.module('app', ["ngTable", "kendo.directives", '$strap.directives']);

    app.controller("trackingController", function($scope, $filter, $http, $q) {

        $scope.search = function () {
            $scope.errorMessage = null;
            $scope.loading = true;
            $http.get('${contextPath}/tmw/tracking/find?container=' + $scope.container).then(function (res) {
                $scope.loading = false;
                if (res.data.errorMessage) {
                    $scope.message = null;
                    $scope.errorMessage = res.data.errorMessage;
                } else {
                    $scope.message = res.data.message;
                }

            }, function error(data) {
                $scope.errorMessage = "Request error";
                $scope.message = null;
                $scope.loading = false;
            });
        };

    });


</script>
<#include "*/footer.ftl"/>
