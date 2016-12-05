<#assign top_nav_selected = "trackContainer">
<#assign page_title = "Track Container">
<#include "*/header.ftl"/>
<fieldset ng-app="TrackContainer">

    <div ng-controller="TrackContainerController">


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

    function TrackContainerController($scope, $http, $modal, $q) {

        $scope.search = function () {
            $scope.errorMessage = null;
            $scope.loading = true;
            $http.get('${contextPath}/tmw/tracking/find?container=' + $scope.container).success(function (data) {
                $scope.loading = false;
                if (data.errorMessage) {
                    $scope.message = null;
                    $scope.errorMessage = data.errorMessage;
                } else {
                    $scope.message = data.message;
                }

            }).error(function (data) {
                $scope.errorMessage = "Request error";
                $scope.message = null;
                $scope.loading = false;
            });
        };

    }
    var app = angular.module('app', [ "kendo.directives", '$strap.directives']);

</script>
<#include "*/footer.ftl"/>
