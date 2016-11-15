<#assign top_nav_selected = "deviceManagement">
<#assign page_title = " Management">
<#include "*/header.ftl"/>
<link href="${contextPath}/css/bootstrap3/css/bootstrap.css" rel="stylesheet" media="screen">
<script src="${contextPath}/css/bootstrap3/js/bootstrap.min.js"></script>

<fieldset ng-app="ConfigeManagement">
    <legend>Setup config:</legend>

    <div ng-controller="ConfigeManagementController">
        <form ng-show="dynamicConfig">
            <table id="configTable" class="table table-bordered table-striped" style="padding-top: 5px;">
                <thead>
                <tr style="background-color: #1a1a1a; color: #ffffff; width: 100px">
                    <th>Config</th>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Send mail :</td>
                    <td>
                        <div class="btn-group" ng-model="dynamicConfig.allowSendMail" bs-radio-group>
                            <label class="btn btn-default"><input class="btn btn-default" value="true" type="radio"
                                                                  ng-click="editConfig()">YES</label>
                            <label class="btn btn-default"><input class="btn btn-default" value="false" type="radio"
                                                                  ng-click="editConfig()">NO</label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>Print :</td>
                    <td>
                        <div class="btn-group" ng-model="dynamicConfig.allowPrint" bs-radio-group>
                            <label class="btn btn-default"><input class="btn btn-default" value="true" type="radio"
                                                                  ng-click="editConfig()">YES</label>
                            <label class="btn btn-default"><input class="btn btn-default" value="false" type="radio"
                                                                  ng-click="editConfig()">NO</label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>Send SFTP :</td>
                    <td>
                        <div class="btn-group" ng-model="dynamicConfig.allowSendSFTP" bs-radio-group>
                            <label class="btn btn-default"><input class="btn btn-default" value="true" type="radio"
                                                                  ng-click="editConfig()">YES</label>
                            <label class="btn btn-default"><input class="btn btn-default" value="false" type="radio"
                                                                  ng-click="editConfig()">NO</label>
                    </td>
                </tr>
                <tr>
                    <td>Verify LDAP credentials :</td>
                    <td>
                        <div class="btn-group" ng-model="dynamicConfig.verifyLDAP" bs-radio-group>
                            <label class="btn btn-default"><input class="btn btn-default" value="true" type="radio"
                                                                  ng-click="editConfig()">YES</label>
                            <label class="btn btn-default"><input class="btn btn-default" value="false" type="radio"
                                                                  ng-click="editConfig()">NO</label>
                    </td>
                </tr>
                <tr>
                    <td>Verify Client Version :</td>
                    <td>
                        <div class="btn-group" ng-model="dynamicConfig.verifyClient" bs-radio-group>
                            <label class="btn btn-default"><input class="btn btn-default" value="true" type="radio"
                                                                  ng-click="editConfig()">YES</label>
                            <label class="btn btn-default"><input class="btn btn-default" value="false" type="radio"
                                                                  ng-click="editConfig()">NO</label>
                    </td>
                </tr>
                </tbody>

            </table>

        <#-- <button type="button" class="btn btn-default" ng-click="restartServer()">Restart Server</button>
          <label>Note: Linux only</label>-->
        </form>

</fieldset>
<script type="text/javascript">

    function ConfigeManagementController($scope, $http) {
        $http.get('${contextPath}/tmw/storeInfo/dynamicConfig').success(function (data) {
                    $scope.loading = false;
                    if (data.errorMessage) {
                        $scope.errorMessage = data.errorMessage;
                    } else {
                        $scope.dynamicConfig = data.dynamicConfig;
                    }
                }
        ).error(function (data) {
                    $scope.errorMessage = "Request error";
                });

        $scope.editConfig = function () {
            $scope.errorMessage = null;
            $scope.loading = true;
            $http.post('${contextPath}/tmw/storeInfo/editConfig', $scope.dynamicConfig).success(
                    function (data) {
                        $scope.loading = false;
                        if (data.errorMessage) {
                            $scope.errorMessage = data.errorMessage;
                        } else {
                            $scope.dynamicConfig = data.dynamicConfig;
                        }
                    }).error(
                    function () {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });

        }
        $scope.restartServer = function () {
            $scope.errorMessage = null;
            $scope.loading = true;
            $http.get('${contextPath}/tmw/storeInfo/restartServer');
        }
    }

    var app = angular.module('app', ['mgcrea.ngStrap']);


</script>
<#include "*/footer.ftl"/>
