<#assign top_nav_selected = "driverManagement">
<#assign page_title = "Driver List Management">
<#include "*/header.ftl"/>
<fieldset ng-app="DriverManagement">

    <div ng-controller="driverController">

        <span id="errorMessage" class="text-error">{{errorMessage}}</span>


        <table class="table table-bordered table-striped"  show-filter="true" ng-table="tableParams">
            <tr ng-repeat="d in $data">
                <td title="'ID'">
                    {{d.id}}</td>
                <td title="'First Name'" filter="{ firstName: 'text'}" sortable="'firstName'">
                    {{d.firstName}}</td>
                <td title="'Last Name'" filter="{ lastName: 'text'}" sortable="'lastName'">
                    {{d.lastName}}</td>

            </tr>
        </table>

        <img id="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>

    </div>
</fieldset>

<script type="text/javascript">

    var app = angular.module('app', ["ngTable", "kendo.directives", '$strap.directives']);

    app.controller("driverController", function($scope, $filter, $http, $q, NgTableParams) {
        var data = [];
        $scope.init = function() {
            $http.get('${contextPath}/tmw/dict/getAllDrivers')
                    .then(function(res){
                        data = res.data;

                        $scope.tableParams = new NgTableParams({page: 1, count: 10, sorting: {
                            type: 'asc'
                        }}, {getData: function($defer, params) {
                            if (params !=null) {
                                filteredData = params.filter() ?
                                        $filter('filter')(data, params.filter()) :
                                        data;

                                var orderedData = params.sorting() ?
                                        $filter('orderBy')(filteredData, params.orderBy()) : filteredData;
                                var page=orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                                $scope.data=page;
                                $defer.resolve(page);
                            }
                        }});

                    });

        };

        $scope.init();
    });


</script>
<#include "*/footer.ftl"/>
