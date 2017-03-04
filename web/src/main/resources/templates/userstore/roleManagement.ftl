<#assign top_nav_selected = "roleManagement">
<#assign page_title = "Role Management">
<#include "*/header.ftl"/>
<fieldset ng-app="RoleManagement">

    <div ng-controller="roleController">

        <span id="errorMessage" class="text-error">{{errorMessage}}</span>


        <table class="table table-bordered table-striped"  show-filter="true" ng-table="tableParams">
            <tr ng-repeat="r in $data" ng-click="editRole(r)">
                <td title="'ID'">
                    {{r.id}}</td>
                <td title="'Name'" filter="{ roleName: 'text'}" sortable="'roleName'">
                    {{r.roleName}}</td>
            </tr>
        </table>

        <button type="button" class="btn" ng-click="createRole()">Create a New Role</button>

        <img id="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>
        <form class="span10" action="#" method="post" ng-show="roleInfo">
            <div class="row span5 controls">
                <div class="row pull-left">
                    <label class="control-label inline" for="roleName">NAME</label>
                    <input id="roleName" type="text" class="span3 inline" placeholder="Role Name"
                           ng-model="roleInfo.role.roleName" required>
                </div>

            </div>

            <div class="row span5 controls">
                <label><b>Permissions:<b>&nbsp;</label>

                <label ng-repeat="permission in roleInfo.allPermissions">
                    <input
                            type="checkbox"
                            name="selectedPermissions[]"
                            value="{{permission.name}}"
                            ng-checked="selection.indexOf(permission.id) > -1"
                            ng-click="toggleSelection(permission.id)"
                            > {{permission.name}} - {{permission.description}}
                </label>

            </div>
            <button ng-show="roleInfo" type="button" class="btn btn-primary" ng-click="saveRole()">Save</button>

            <button ng-show="roleInfo.role.id" type="button" class="btn btn-danger" ng-click="deleteRole()">Delete</button>

        </form>



    </div>
</fieldset>

<script type="text/javascript">




    var app = angular.module('app', ["ngTable", "kendo.directives", '$strap.directives']);

    app.controller("roleController", function($scope, $filter, $http, $q, NgTableParams) {
        var data = [];
        $scope.allPermissions = [];
        $scope.init = function() {
            $http.get('${contextPath}/tmw/userstore/getAllRoles')
                    .then(function(res){
                        $scope.roles = res.data;
                        data = res.data;

                        $scope.tableParams = new NgTableParams({page: 1, count: 10, sorting: {
                            roleName: 'asc'
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
            $http.get('${contextPath}/tmw/userstore/getAllPermissions')
                    .then(function(res){
                        $scope.allPermissions = res.data;
                    });

        };


        $scope.editRole = function (role) {
            $scope.roleInfo = null;
            $scope.errorMessage = null;
            $scope.loading = true;
            $scope.selection = [];
            $http.get('${contextPath}/tmw/userstore/getRole?id=' + role.id).then(function (res) {
                $scope.loading = false;
                if (res.data.errorMessage) {
                    $scope.roleInfo = null;
                    $scope.errorMessage = res.data.errorMessage;
                } else {
                    $scope.roleInfo = res.data;
                    angular.forEach(res.data.permissions, function(value, key) {
                        //console.log(value);
                        $scope.selection.push(value.id);
                    });
                }

            }, function error(data) {
                $scope.errorMessage = "Request error";
                $scope.roleInfo = null;
            });
        };


        $scope.createRole = function () {
            $scope.errorMessage = null;
            $scope.selection = [];
            $scope.roleInfo = {
                role : {
                    id : "", name: ""
                },
                permissions: [],
                allPermissions: $scope.allPermissions

            };
        };

        $scope.saveRole = function () {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.loading = true;
            $scope.roleInfo.permissions = [];
            angular.forEach($scope.selection, function(value, key) {

                angular.forEach($scope.allPermissions, function(permValue, permkey) {
                    if (permValue.id == value) {
                        $scope.roleInfo.permissions.push(permValue);
                    }
                });
            });

            $http.post('${contextPath}/tmw/userstore/saveRole', $scope.roleInfo).then(
                    function (res, respStatus, headers, config) {
                        $scope.loading = false;
                        if (res.data.errorMessage) {
                            $scope.errorMessage = res.data.errorMessage;
                        } else {
                            $scope.roleInfo = null;
                            $scope.init();
                        }
                    }, function error(data, respStatus, headers, config) {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });


        }

        $scope.deleteRole  = function () {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.loading = true;
            $http.post('${contextPath}/tmw/userstore/deleteRole', $scope.roleInfo).then(
                    function (res, respStatus, headers, config) {
                        $scope.loading = false;
                        if (res.data.errorMessage) {
                            $scope.errorMessage = res.data.errorMessage;
                        } else {
                            $scope.roleInfo = null;
                            $scope.init();
                        }
                    }, function error(data, respStatus, headers, config) {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });

        }


        // Toggle selection for a given fruit by name
        $scope.toggleSelection = function toggleSelection(id) {
            var idx = $scope.selection.indexOf(id);

            // Is currently selected
            if (idx > -1) {
                $scope.selection.splice(idx, 1);
            }
            // Is newly selected
            else {
                $scope.selection.push(id);
            }
        };

        $scope.init();
    });


</script>
<#include "*/footer.ftl"/>
