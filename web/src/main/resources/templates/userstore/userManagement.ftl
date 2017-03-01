<#assign top_nav_selected = "userManagement">
<#assign page_title = "User Management">
<#include "*/header.ftl"/>
<fieldset ng-app="UserManagement">

    <div ng-controller="userController">

        <span id="errorMessage" class="text-error">{{errorMessage}}</span>


        <table class="table table-bordered table-striped" show-filter="true"  ng-table="tableParams">
            <tr ng-repeat="u in users" ng-click="editUser(u)">
                <td data-title="'ID'">
                    {{r.id}}</td>
                <td data-title="'First Name'" filter="{ firstName: 'text'}" sortable="'firstName'">
                    {{u.firstName}}</td>
                <td data-title="'Last Name'" filter="{ lastName: 'text'}" sortable="'lastName'">
                    {{u.lastName}}</td>
                <td data-title="'Email'" filter="{ email: 'text'}" sortable="'emailName'">
                    {{u.email}}</td>
                <td data-title="'Active'" sortable="'active'">
                    {{u.active}}</td>
            </tr>
        </table>


        <button type="button" class="btn" ng-click="createUser(userId)">Create a New User</button>

        <!--Search user panel-->
        <!--div class="span10" style="position: relative; ">
            <div class="controls form-search well">
                <input id="userId" type="text" class="span3 offset2" ng-model="userId" placeholder="Enter User Email">
                <button id="searchUser" class="btn" type="button" ng-click="search()">Search</button>
                <span class="muted">OR if user not found,</span>
                <button type="button" class="btn btn-link" ng-click="createUser(userId)">Create a New User
                </button>
                <img id="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>
                <span id="errorMessage" class="text-error">{{errorMessage}}</span>
            </div>
        </div-->

        <form id="showPage" class="span10" action="#" method="post" ng-show="user">
            <h4 class="span7 pull-left">{{ title }}</h4>

            <div class="row span10 controls">
                <div class="row pull-left">
                    <label class="control-label inline" for="firstName">NAME</label>
                    <input id="firstName" type="text" class="span2 inline" placeholder="first name"
                           ng-model="user.firstName" required>
                    <input id="lastName" type="text" class="span2 inline" placeholder="last name"
                           ng-model="user.lastName" required>
                </div>
                <div class="row offset3">
                    <label class="control-label inline" for="shortName">EMAIL</label>
                    <input id="shortName" type="text" class="span3 inline" placeholder="Email"
                           ng-model="user.email" ng-readonly="user.id != null" required>
                </div>
                <div class="row pull-left" ng-show="user.id == null" >
                    <label class="control-label inline" for="shortName">PASSWORD</label>
                    <input id="shortName" type="text" class="span2 inline" placeholder="Password"
                           ng-model="user.password" required>
                </div>


                <span class="badge badge-important" ng-show="!user.active">DELETED</span>
                <span class="badge badge-success" ng-show="user.active">ACTIVE</span>

            </div>

            </br>
            </br>


            <label><b>ROLES:<b>&nbsp;</label>
            <select multiple="multiple" ng-model="user.roles" ng-options="role.roleName for role in roleList">
            </select>
            </br>
            <button class="btn" style="color:black;" type="button" ng-click="clearRoles()">Clear Roles</button>
            <hr/>

            <div>
                <span id="infoMessage" class="text-info">{{infoMessage}}</span>
            </div>

            <br>

            <div class="span9" style="margin-left:0;margin-top:20px;">
                <div class="span2 pull-left" style="margin-left:0;">
                    <button class="btn" style="color:red;" ng-show="user.id!=null" type="button" ng-click="switchUser(false)">Delete User</button>
                    <button class="btn" style="color:darkgreen;" ng-show="!user.active" type="button" ng-click="switchUser(true)">Actiate User</button>
                </div>

                <div class="span5 pull-right">
                    <button class="btn" type="button" ng-click="search()">Cancel</button>
                    <button class="btn btn-primary" type="button" ng-click="saveUser()">Save</button>
                </div>
            </div>

        </form>
    </div>
</fieldset>

<script type="text/javascript">

    var app = angular.module('app', ["ngTable", "kendo.directives", '$strap.directives']);

    app.controller("userController", ["NgTableParams",  "$scope", "$filter", "$http", "$modal", "$q", function ( NgTableParams, $scope, $filter, $http, $modal, $q) {

        $scope.roleList = [
        <#list roles as role>
            {"id":"${role.id}","roleName":"${role.roleName}"},
        </#list>
        ]

        $scope.init = function() {
            $http.get('${contextPath}/tmw/userstore/getAllUsers')
                    .then(function(res){
                        $scope.users = res.data;
                    });

        };

        $scope.editUser = function (user) {
            $scope.user = null;
            $scope.errorMessage = null;
            $scope.loading = true;
            $http.get('${contextPath}/tmw/userstore/find?id=' + user.id).success(function (data) {
                $scope.loading = false;
                if (data.errorMessage) {
                    $scope.user = null;
                    $scope.errorMessage = data.errorMessage;
                } else {
                    $scope.user = data.user;


                    for (var i = 0; i < $scope.user.roles.length; i++) {
                        for (var j = 0; j < $scope.roleList.length; j++) {
                            console.log($scope.roleList[j].id + " " + $scope.user.roles[i].id);
                            if ($scope.roleList[j].id == $scope.user.roles[i].id) {
                                $scope.user.roles[i] = $scope.roleList[j];
                                break;
                            }
                        }
                    }
                }

            }).error(function (data) {
                $scope.errorMessage = "Request error";
                $scope.roleInfo = null;
            });
        };

        $scope.createUser = function () {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.user = {
                email: "",
                active: true,
                roles: [

                ]};
            return true;
        };

        $scope.saveUser = function () {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.loading = true;
            $http.post('${contextPath}/tmw/userstore/save', $scope.user).success(
                    function (data, respStatus, headers, config) {
                        $scope.loading = false;
                        if (data.errorMessage) {
                            $scope.errorMessage = data.errorMessage;
                        } else {
                            $scope.init();
                        }
                    }).error(
                    function (data, respStatus, headers, config) {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });
        };

        $scope.switchUser = function (mode) {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.loading = true;
            $http.get('${contextPath}/tmw/userstore/switch?email=' + $scope.user.email + '&mode='+mode).success(
                    function (data, respStatus, headers, config) {
                        $scope.loading = false;
                        if (data.errorMessage) {
                            $scope.errorMessage = data.errorMessage;
                        } else {
                            $scope.user = null;
                            $scope.init();
                        }
                    }).error(
                    function (data, respStatus, headers, config) {
                        $scope.errorMessage = "Request error";
                        $scope.loading = false;
                    });
        };

        $scope.clearRoles = function () {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.loading = true;
            $http.get('${contextPath}/tmw/userstore/clearRoles?email=' + $scope.user.email).success(
                    function (data, respStatus, headers, config) {
                        $scope.loading = false;
                        if (data.errorMessage) {
                            $scope.errorMessage = data.errorMessage;
                        } else {
                            $scope.user.roles = [];

                        }
                    }).error(
                    function (data, respStatus, headers, config) {
                        $scope.errorMessage = "Request error";
                        $scope.loading = false;
                    });
        };

        $scope.init();


        /*$scope.cols = [
            {field: "firstName", title: "First Name", filter: {firstName: "text"}, show: true},
            {field: "lastName", title: "Last Name", filter: {lastName: "text"}, show: true},
            {field: "email", title: "Email", filter: {lastName: "text"}, show: true},
            {field: "active", title: "Active", show: true}
        ];*/

        $scope.tableParams = new NgTableParams(
                {
                    page: 1,
                    count: 5
                },
                { dataset: $scope.users });


    }]);


</script>
<#include "*/footer.ftl"/>
