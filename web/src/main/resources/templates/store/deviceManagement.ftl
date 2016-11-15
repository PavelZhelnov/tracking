<#assign top_nav_selected = "deviceManagement">
<#assign page_title = "Device Management">
<#include "*/header.ftl"/>
<fieldset ng-app="DeviceManagement">
    <legend>Setup info:</legend>

    <div ng-controller="DeviceManagementController">

        <div class="span10" style="position: relative; margin-bottom: 20px ">
            <div class="input-prepend input-append">
                <span class="add-on" style="height: 20px;">Store:</span>
                <input id="storeId" ng-model="storeId" ng-minlength="1" style="height: 30px;"
                       autocomplete="off" class="span2"
                       type="text" data-provide="typeahead"
                       data-source="[<#list stores as store>&quot;${store.uniqueId?string("0")}&quot;<#if store_has_next>,</#if></#list>]">
                <button id="searchStore" class="btn" type="button" ng-click="search()">Search</button>
            </div>
            <img id="loading" ng-show="loading" style="display:none; position: absolute;left: 300px;top: 5px;"
                 src="${contextPath}/img/ajax-loader.gif" alt="Loading..."/>
            <span id="errorMessage" class="text-error">{{errorMessage}}</span>
        </div>

        <form id="showPage" class="span10" action="#" method="post" ng-show="store">
            <table id="devicesTable" class="table table-bordered table-striped" style="padding-top: 5px;">
                <thead>
                <tr style="background-color: #1a1a1a; color: #ffffff">
                    <th>ID</th>
                    <th>Deivce ID</th>
                    <th>Device name</th>
                    <th>Store ID</th>
                    <th><a type="button"
                           class='btn btn-small btn-success editDevice' ng-click="addDevice()"><i
                            class='icon-plus'></i></a></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="storeDevice in devices">
                    <td>{{storeDevice.id}}</td>
                    <td>
                        <input type="text" ng-model="storeDevice.deviceId" required style="width: 350px"
                               class="deviceId"
                               key="{{storeDevice.deviceId}}"
                               value="{{storeDevice.deviceId}}"/>
                    </td>
                    <td><input type="text" ng-model="storeDevice.deviceName" ng-trim="true"
                               class="deviceName"
                               key="{{storeDevice.deviceName}}"
                               value="{{storeDevice.deviceName}}"/></td>
                    <td>
                        <input id="storeId" ng-model="storeDevice.store.uniqueId" ng-minlength="1"
                               style="height: 30px;"
                               autocomplete="off" class="span2"
                               type="text"
                               <#--data-provide="typeahead"-->
                               <#--data-source="[<#list stores as store>&quot;${store.uniqueId?string("0")}&quot;<#if store_has_next>,</#if></#list>]"-->
                               key="{{storeDevice.store.uniqueId}}"
                               value="{{storeDevice.store.uniqueId}}"/>
                    </td>
                    <td>
                        <div><a type="button"
                                class='btn btn-small btn-info editDevice'
                                ng-click="editDevice($index,storeDevice.id,storeDevice.deviceId,storeDevice.deviceName,storeDevice.store.uniqueId)">
                            <i class='icon-file'></i></a>&nbsp;
                            <a type="button"
                               class='btn btn-small btn-danger deleteDevice'
                               ng-click="deleteDevice($index,storeDevice.id)"><i class='icon-trash'></i></a>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
</fieldset>
<script type="text/javascript">

    function DeviceManagementController($scope, $http) {
        $scope.search = function () {
            $scope.errorMessage = null;
            $scope.store = null;
            $scope.devices = null;
            $scope.loading = true;
            $scope.storeId = $("#storeId").val();
            $http.get('${contextPath}/tmw/storeInfo/device?storeId=' + $scope.storeId).success(function (data) {
                $scope.loading = false;
                if (data.errorMessage) {
                    $scope.store = null;
                    $scope.errorMessage = data.errorMessage;
                } else {
                    $scope.store = data.store;
                    $scope.devices = data.devices;
                }
            }).error(function (data) {
                        $scope.errorMessage = "Request error";
                        $scope.store = null;
                    });

        }

        $scope.addDevice = function () {
            $scope.storeDevice = {"deviceID": "", "deviceName": ""};
            $scope.devices.push($scope.storeDevice);
        };

        $scope.editDevice = function (index, id, deviceId, deviceName, storeId) {
            $scope.errorMessage = null;
            $scope.loading = true;

            var data = {id: id, deviceId: deviceId, deviceName: deviceName, storeId: storeId};
            $http.post('${contextPath}/tmw/storeInfo/editDevice', data).success(
                    function (data) {
                        $scope.loading = false;
                        if (data.errorMessage) {
                            $scope.errorMessage = data.errorMessage;
                        } else {
                           if(storeId!=$scope.store.uniqueId) $scope.devices.splice(index, 1);
                        }
                    }).error(
                    function () {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });

        }

        $scope.deleteDevice = function (index, id) {
            $scope.errorMessage = null;
            $scope.loading = true;

            $http.delete('${contextPath}/tmw/storeInfo/deleteDevice?id=' + id).success(
                    function (data) {
                        $scope.loading = false;
                        if (data.errorMessage) {
                            $scope.errorMessage = data.errorMessage;
                        } else {
                            $scope.devices.splice(index, 1);
                        }
                    }).error(
                    function () {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });
        }
    }

    var app = angular.module('app', ['kendo.directives', '$strap.directives']);
    app.directive('dateFormat', function () {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModelCtrl) {
                ngModelCtrl.$formatters.unshift(function (valueFromModel) {
                    return new Date(valueFromModel);
                });

                ngModelCtrl.$parsers.push(function (valueFromInput) {
                    return valueFromInput.getTime();
                });
            }
        };
    });

</script>
<#include "*/footer.ftl"/>
