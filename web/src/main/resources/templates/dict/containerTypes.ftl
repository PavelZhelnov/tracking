<#assign top_nav_selected = "containerTypesManagement">
<#assign page_title = "Container Types Management">
<#include "*/header.ftl"/>
<fieldset ng-app="ContainerTypeManagement">

    <div ng-controller="containerTypeController">

        <span id="errorMessage" class="text-error">{{errorMessage}}</span>


        <table class="table table-bordered table-striped"  show-filter="true" ng-table="tableParams">
            <tr ng-repeat="t in $data" ng-click="editType(t)">
                <td title="'ID'">
                    {{t.id}}</td>
                <td title="'Type'" filter="{ type: 'text'}" sortable="'type'">
                    {{t.type}}</td>
                <td title="'Length'" filter="{ length: 'number'}" sortable="'length'">
                    {{t.length}}</td>

            </tr>
        </table>

        <button type="button" class="btn" ng-click="createType()">Create a New Container Type</button>

        <img id="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>
        <form class="span10" action="#" method="post" ng-show="containerType">
            <div class="row span5 controls">
                <div class="row pull-left">
                    <label class="control-label inline" for="type">Type</label>
                    <input id="type" type="text" class="span3 inline" placeholder="Type Name"
                           ng-model="containerType.type" required>
                    <label class="control-label inline" for="length">Length</label>
                    <input id="length" type="text" class="span3 inline" placeholder="Length Parameter"
                           ng-model="containerType.length" required>

                </div>

            </div>

            <button ng-show="containerType" type="button" class="btn btn-primary" ng-click="saveType()">Save</button>

            <button ng-show="containerType.id" type="button" class="btn btn-danger" ng-click="deleteType()">Delete</button>

        </form>



    </div>
</fieldset>

<script type="text/javascript">

    var app = angular.module('app', ["ngTable", "kendo.directives", '$strap.directives']);

    app.controller("containerTypeController", function($scope, $filter, $http, $q, NgTableParams) {
        var data = [];
        $scope.init = function() {
            $http.get('${contextPath}/tmw/dict/getAllContainerTypes')
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


        $scope.editType = function (containerType) {
            $scope.containerType = null;
            $scope.errorMessage = null;
            $scope.loading = true;
            $scope.selection = [];
            $http.get('${contextPath}/tmw/dict/getContainerType?id=' + containerType.id).then(function (res) {
                $scope.loading = false;
                if (res.data.errorMessage) {
                    $scope.containerType = null;
                    $scope.errorMessage = res.data.errorMessage;
                } else {
                    $scope.containerType = res.data;
                }

            }, function error(data) {
                $scope.errorMessage = "Request error";
                $scope.roleInfo = null;
            });
        };


        $scope.createType = function () {
            $scope.errorMessage = null;
            $scope.selection = [];
            $scope.containerType = {
                id : "",
                type : "",
                length: ""
            };
        };

        $scope.saveType = function () {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.loading = true;

            $http.post('${contextPath}/tmw/dict/saveContainerType', $scope.containerType).then(
                    function (res, respStatus, headers, config) {
                        $scope.loading = false;
                        if (res.data.errorMessage) {
                            $scope.errorMessage = res.data.errorMessage;
                        } else {
                            $scope.containerType = null;
                            $scope.init();
                        }
                    }, function error(data, respStatus, headers, config) {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });


        }

        $scope.deleteType  = function () {
            $scope.errorMessage = null;
            $scope.loading = true;
            $http.post('${contextPath}/tmw/dict/deleteContainerType', $scope.containerType).then(
                    function (res, respStatus, headers, config) {
                        $scope.loading = false;
                        if (res.data.errorMessage) {
                            $scope.errorMessage = res.data.errorMessage;
                        } else {
                            $scope.containerType = null;
                            $scope.init();
                        }
                    }, function error(data, respStatus, headers, config) {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });

        }
        $scope.init();
    });


</script>
<#include "*/footer.ftl"/>
