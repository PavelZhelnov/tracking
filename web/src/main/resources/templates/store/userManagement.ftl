<#assign top_nav_selected = "userManagement">
<#assign page_title = "User Management">
<#include "*/header.ftl"/>
<fieldset ng-app="UserManagement">

    <div ng-controller="UserManagementController">


        <!--Search user panel-->
        <div class="span10" style="position: relative; ">
            <div class="controls form-search well">
                <input id="userId" type="text" class="span2 offset2" ng-model="userId" placeholder="Enter User ID">
                <button id="searchUser" class="btn" type="button" ng-click="search()">Search</button>
                <span class="muted">OR if user not found,</span>
                <button type="button" class="btn btn-link" ng-click="createUser(userId)">Create a New User
                </button>
                <img id="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>
                <span id="errorMessage" class="text-error">{{errorMessage}}</span>
            </div>
        </div>

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
                    <label class="control-label inline" for="shortName">SHORT NAME</label>
                    <input id="shortName" type="text" class="span2 inline" placeholder="short name"
                           ng-model="user.uniqueId" ng-readonly="user.id != null" required>
                </div>
                <div class="row offset3">
                    <label class="control-label inline" for="shortName">Job Code</label>
                    <input id="shortName" type="text" class="span2 inline" placeholder="short name"
                           ng-model="user.jobCode" ng-readonly="user.id != null" required>
                </div>
                <span class="badge badge-important" ng-show="!user.active">DELETED</span>

            </div>

            </br>
            </br>


            <label><b>ROLES:<b>&nbsp;</label>
            <select multiple="multiple" ng-model="user.roles" ng-options="role.roleName for role in stateList">
            </select>
            </br>
            <button class="btn" style="color:black;" type="button" ng-click="clearRoles()">Clear Roles</button>
            <hr/>

               <div class="span9" style="margin-left:0;margin-top:20px;">
                   <div class="span2 pull-left" style="margin-left:0;">
                       <div class="btn-group">
                           <label class="control-label inline" for="roles"><b>STORE</b></label>
                           <button id="roles" class="btn dropdown-toggle"
                                   data-toggle="dropdown">{{user==null||user.tibcoStore==null ? 'none' : user.tibcoStore.uniqueId}}<span
                                   class="caret"></span></button>
                           <ul class="dropdown-menu">
                               <li><a href="#" ng-click="changeStore('none'); renderStore('none')">none</a></li>
                           <#list stores as store>
                               <li><a href="#" ng-click="changeStore('${store.uniqueId?c}')">${store.uniqueId?c !""}
                                   - ${store.address !""}, ${store.city!""}, ${store.state!""}, ${store.zip!""}
                                   , ${store.phone!""}</a></li>
                           </#list>
                           </ul>
                       </div>
                   </div>
               </div>
                <div class="span9" style="margin-left:0;margin-top:20px;">
                    <div class="span2 pull-left" style="margin-left:0;">
                       REGION CODE:
                    </div>
                    <div class="span2 pull-left" style="margin-left:0;">
                       <span id="regionCode" class="text-info">{{storeRegionCode}}</span>
                    </div>
                    <div class="span2 pull-left" style="margin-left:0;">
                       DISTRICT:
                    </div>
                    <div class="span2 pull-left" style="margin-left:0;">
                       <span id="district" class="text-info">{{storeDistrict}}</span>
                    </div>
                </div>
                <div class="span9" style="margin-left:0;margin-top:20px;">

                    <div class="span2 pull-left" style="margin-left:0;">
                       STORE NAME:
                    </div>
                    <div class="span2 pull-left" style="margin-left:0;">
                       <span id="storeName" class="text-info">{{storeName}}</span>
                    </div>
                    <div class="span2 pull-left" style="margin-left:0;">
                       STORE ADDRESS:
                    </div>
                   <div class="span2 pull-left" style="margin-left:0;">
                       <span id="storeAddress" class="text-info">{{storeAddress}}</span>
                   </div>

                </div>

            <div>
                <span id="infoMessage" class="text-info">{{infoMessage}}</span>
            </div>

            <br>

            <div class="span9" style="margin-left:0;margin-top:20px;">
                <div class="span2 pull-left" style="margin-left:0;">
                    <button class="btn" style="color:red;" type="button" ng-click="deleteUser()">Delete User</button>
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

    function UserManagementController($scope, $http, $modal, $q) {




        $scope.stateList = [
           <#list roles as role>
             {"id":"${role.id}","roleName":"${role.roleName}","type":"${role.type}"},
           </#list>
        ]

        $scope.displayLocationDeletePopup = false;
        $scope.search = function () {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.order = null;
            $scope.loading = true;
            $http.get('${contextPath}/tmw/storeInfo/user?uniqueId=' + $scope.userId).success(function (data) {
                $scope.loading = false;
                if (data.errorMessage) {
                    $scope.user = null;
                    $scope.errorMessage = data.errorMessage;
                } else {
                    $scope.user = data.user;

                    $scope.renderStore($scope.user.tibcoStore.uniqueId);


                    for (var i = 0; i < $scope.user.roles.length; i++) {
                        for (var j = 0; j < $scope.stateList.length; j++) {
                            if ($scope.stateList[j].type === $scope.user.roles[i].type) {
                                $scope.user.roles[i] = $scope.stateList[j];
                                break;
                            }
                        }
                    }
                }

            }).error(function (data) {
                $scope.errorMessage = "Request error";
                $scope.user = null;
            });
        };

        $scope.renderStore = function (uniqueId) {
            $scope.storeRegionCode = "";
            $scope.storeDistrict = "";
            $scope.storeName = "";
            $scope.storeAddress = "";
            if (uniqueId != 'none') {
                $http.get('${contextPath}/tmw/storeInfo/find?storeId=' + uniqueId).success(function (data) {
                    $scope.loading = false;
                    if (data.errorMessage) {
                    } else {
                        $scope.storeRegionCode = data.regionCode;
                        $scope.storeDistrict = data.district;
                        $scope.storeName = data.name;
                        $scope.storeAddress = data.address;
                    }
                });
            }
        }


        $scope.createUser = function (userId) {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.user = {
                uniqueId: userId,
                active: true,
                roles: [

                ]};
            return true;
        };

        $scope.changeRole = function (roleType, rName) {
            $scope.errorMessage = null;
            $scope.infoMessage = 'Please note that role may be overwritten by system job automatically based on user job code';
            $scope.state = null;
            $scope.user.roles = roleType === "" ? [] : [
                {type: roleType, roleName: rName}
            ];
            return true;
        };

        $scope.changeStore = function (storeId) {
            if (storeId == "none") {
                $scope.user.tibcoStore = null;
                return true;
            }
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.user.tibcoStore = {uniqueId: storeId};
            $scope.renderStore(storeId);

            return true;
        };

        $scope.saveUser = function () {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.loading = true;
            $http.post('${contextPath}/tmw/storeInfo/saveUser', $scope.user).success(
                    function (data, respStatus, headers, config) {
                        $scope.loading = false;
                        if (data.errorMessage) {
                            $scope.errorMessage = data.errorMessage;
                        }
                    }).error(
                    function (data, respStatus, headers, config) {
                        $scope.loading = false;
                        $scope.errorMessage = "Request error";
                    });
        };

        $scope.deleteUser = function () {
            $scope.errorMessage = null;
            $scope.state = null;
            $scope.loading = true;
            $http.get('${contextPath}/tmw/storeInfo/deleteUser?uniqueId=' + $scope.user.uniqueId).success(
                    function (data, respStatus, headers, config) {
                        $scope.loading = false;
                        if (data.errorMessage) {
                            $scope.errorMessage = data.errorMessage;
                        } else {
                            $scope.user = null;
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
            $http.get('${contextPath}/tmw/storeInfo/clearUserRoles?uniqueId=' + $scope.user.uniqueId).success(
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

    }
    var app = angular.module('app', [ "kendo.directives", '$strap.directives']);

</script>
<#include "*/footer.ftl"/>
