<#assign top_nav_selected = "route">
<#assign page_title = "Route Management">
<#include "*/header.ftl"/>
<fieldset ng-app="RouteManagement">
    <legend>Route info:</legend>

    <div ng-controller="RouteController">

        <div class="span10" style="position: relative; margin-bottom: 20px ">
            <div class="input-prepend input-append">
                <span class="add-on" style="height: 20px;">Store:</span>
                <input id="storeId" ng-model="storeId" style="height: 30px;" autocomplete="off" class="span2"
                       type="text" data-provide="typeahead"
                       data-source="[<#list stores as store>&quot;${store.uniqueId?string("0")}&quot;<#if store_has_next>,</#if></#list>]">
                <button id="searchStore" class="btn" type="button" ng-click="search()">Search</button>
            </div>
            <img id="loading" ng-show="loading" style="display:none; position: absolute;left: 300px;top: 5px;"
                 src="${contextPath}/img/ajax-loader.gif" alt="Loading..."/>
            <span id="errorMessage" class="text-error">{{errorMessage}}</span>
            <span id="successMessage" class="text-success">{{successMessage}}</span>

            <form id="showPage" class="span10" action="#" method="post" ng-show="store">
                <label>Target store: <b>{{routes[0].targetStore.uniqueId}}</b></label>

                <div>
                    <table id="workingDayTable" class="table table-bordered">
                        <thead class="">
                        <tr>
                            <th>Store</th>
                            <th>Sunday</th>
                            <th>Monday</th>
                            <th>Tuesday</th>
                            <th>Wednesday</th>
                            <th>Thursday</th>
                            <th>Friday</th>
                            <th>Suterday</th>
                            <th>Days to Deliver</th>
                            <th><a type="button"
                                   class='btn btn-small btn-success editDevice' ng-click="addRoute()"><i
                                    class='icon-plus'></i></a>
                                <input id="targetStoreId" ng-model="targetStoreId" style="height: 30px; width: 50px"
                                       autocomplete="off" class="span2"
                                       type="text" data-provide="typeahead"
                                       data-source="[<#list stores as store><#if store_has_next>,</#if></#list>]"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="route in routes">
                            <td>{{route.sourceStore.uniqueId}}</td>
                            <td><input type="checkbox" ng-model="route.sunday"/></td>
                            <td><input type="checkbox" ng-model="route.monday"/></td>
                            <td><input type="checkbox" ng-model="route.tuesday"/></td>
                            <td><input type="checkbox" ng-model="route.wednesday"/></td>
                            <td><input type="checkbox" ng-model="route.thursday"/></td>
                            <td><input type="checkbox" ng-model="route.friday"/></td>
                            <td><input type="checkbox" ng-model="route.suterday"/></td>
                            <td><input type="text" style="width: 50px" ng-model="route.deliveryDays"/></td>
                            <td>
                                <div>
                                    <a type="button"
                                       class='btn btn-small btn-info'
                                       ng-click="saveRoute(route)"><i class='icon-file'></i></a>
                                    <a type="button"
                                       class='btn btn-small btn-danger deleteDevice'
                                       ng-click="deleteRoute($index,route)"><i class='icon-trash'></i></a>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </form>
        </div>
</fieldset>
<script type="text/javascript">

    function RouteController($scope, $http) {

        $scope.search = function () {
            $scope.errorMessage = null;
            $scope.succesMessage = null;
            $scope.targetStoreId = null;
            $scope.store = null;
            $scope.routes = null;
            $scope.loading = true;
            $scope.storeId = $("#storeId").val();
            $http.get('${contextPath}/tmw/storeInfo/routes?storeId=' + $scope.storeId).success(function (data) {
                $scope.loading = false;
                if (data.errorMessage) {
                    $scope.store = null;
                    $scope.errorMessage = data.errorMessage;
                } else {
                    $scope.store = data.store;
                    $scope.routes = data.routes;
                }
            }).error(function (data) {
                $scope.errorMessage = "Request error";
                $scope.store = null;
            });

        }

        $scope.searchTarget = function (sotreType) {
            $scope.targetStoreId = null;
            $scope.errorMessage = null;
            $scope.successMessage = null;
            $scope.targetStore = null;
            $scope.loading = true;
            $scope.targetStoreId = $("#targetStoreId").val();
            var sourceStore, targetStore;

            $http.get('${contextPath}/tmw/storeInfo/targetStore?storeId=' + $scope.targetStoreId).success(function (data) {
                $scope.loading = false;
                if (data.errorMessage) {
                    $scope.targetStore = null;
                    $scope.errorMessage = data.errorMessage;
                } else {
                    switch (sotreType) {
                        case 'SIMPLE':
                        case 'SELLING':{
                            if (!(data.store.storeType === 'CTS' || data.store.storeType === 'OWERFLOW')) {
                                alert('Target store must be CTS or Owerflow');
                                return;
                            }
                            sourceStore = $scope.store, targetStore = data.store;
                            break;
                        }
                        case 'CTS':
                        case 'OWERFLOW':{
                            if (!(data.store.storeType === 'SIMPLE' || data.store.storeType === 'SELLING')) {
                                alert('Store must be SIMPLE or SELLING');
                                return;
                            }
                            sourceStore = data.store, targetStore = $scope.store;
                            break;
                        }
                        default :
                            break;
                    }
                    $scope.routes.push({id: null,
                        sourceStore: sourceStore,
                        targetStore: targetStore,
                        sunday: false,
                        monday: false,
                        tuesday: false,
                        wednesday: false,
                        thursday: false,
                        friday: false,
                        suterday: false,
                        deliveryDays: 1
                    });

                }
            }).error(function (data) {
                $scope.errorMessage = "Request error";
                $scope.store = null;
            });

        }

        $scope.addRoute = function (route) {
            switch ($scope.store.storeType) {
                case 'SIMPLE':
                case 'SELLING':
                {
                    if ($scope.routes.length > 0) {
                        alert('Store can have only one target!')
                        return;
                    }
                    $scope.targetStoreId = $("#targetStoreId").val();
                    if (!$scope.targetStoreId) {
                        alert('Target store is requiried')
                        return;
                    }

                    break;
                }
                case 'CTS':
                case 'OWERFLOW':
                {
                    $scope.targetStoreId = $("#targetStoreId").val();
                    if (!$scope.targetStoreId) {
                        alert('Selling store is requiried');
                        return;
                    }

                    break;
                }
            }
            $scope.searchTarget($scope.store.storeType);
        }
        $scope.saveRoute = function (route) {
            $scope.targetStoreId = null;
            $scope.errorMessage = null;
            $scope.successMessage = null;
            $scope.loading = true;
            var url;
            if (!route.id) {
                url = '${contextPath}/tmw/storeInfo/addRoute';
            } else {
                url = '${contextPath}/tmw/storeInfo/saveRoute';
            }

            $http.post(url, route).success(
                    function (data) {
                        $scope.loading = false;
                        if (data.errorMessage) {
                            $scope.errorMessage = data.errorMessage;
                        } else {
                            $scope.loading = false;
                            $scope.successMessage = "Success";
                        }
                    }).error(
                    function () {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });
        }

        $scope.deleteRoute = function (index, route) {
            $scope.errorMessage = null;
            $scope.successMessage = null;
            if ($scope.routes.length === 0) {
                return;
            }
            if (!route.id) {
                $scope.routes.splice(index, 1);
                return;
            }

            $scope.errorMessage = null;
            $scope.loading = true;
            $http.post('${contextPath}/tmw/storeInfo/deleteRoute', route).success(
                    function (data) {
                        $scope.loading = false;
                        if (data.errorMessage) {
                            $scope.errorMessage = data.errorMessage;
                        } else {
                            if (storeId != $scope.store.uniqueId) $scope.routes.splice(index, 1);
                        }
                    }).error(function () {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });
        }
    }

    var app = angular.module('app', ['ui.calendar', 'ui.bootstrap']);

</script>
<#include "*/footer.ftl"/>
