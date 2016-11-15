<#assign top_nav_selected = "jobCodeMapping">
<#assign page_title = "Job Code Mapping">
<#include "*/header.ftl"/>
<fieldset ng-app="JobCodeMapping">

    <div ng-controller="JobCodeMappingController">


        <div class="input-prepend input-append" style="position: relative;">
            <span class="add-on" style="height: 20px;width: 70px;">Search by:</span>

            <div id="errorMessage" class="control-group" style="position: absolute;top: 0;left:78px;">
                <div class="controls">
                    <input id="searchBy" type="text" autocomplete="off" style="height: 30px;width: 156px;" ng-model="searchBy">
                    <span class="help-inline"></span>
                    <button id="showJobCodeMapping" class="btn btn-info" ng-click="search()">Search</button>
                    <span class="help-inline"></span>
                    <button id="clearFilterJobCodeMapping" class="btn btn-info" ng-click="clearFilter()">Clear filter</button>
                    <span class="help-inline"></span>
                    <button id="addJobCodeMapping" class="btn btn-success" ng-click="addJobCodeMapping()">Add another one Job Mapping</button>
                </div>
            </div>

            <div style="position: relative;">
                <span id="jobCodeMappingErrorMessage" style="color: #d14"></span>
                &nbsp;&nbsp;&nbsp;
                <img id="loading" ng-show="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..."/>
            </div>

        </div>
        <br/>

        <span id="errorMessage" class="text-error">{{errorMessage}}</span>
        <span id="okMessage" class="text-info">{{okMessage}}</span>


        <div class="tab-pane" id="jobCodeMapping" ng-model="jobCodeMapping">
            <table id="jobCodeMappingTable" class="table table-bordered">
                <caption style="text-align:left;"><b>Job Code Mapping:</b></caption>
                <thead>
                <tr>
                    <th>Role (*)</th>
                    <th>People Soft Job Code (*)</th>
                    <th>Universe Job Code</th>
                    <th>Report Role</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="roleJobCode in jobCodeMapping | startFrom:currentPage*pageSize | limitTo:pageSize">
                    <td>
                        <input type="hidden" value="{{roleJobCode.id}}" ng-model="roleJobCode.id">
                        <select ng-model="roleJobCode.role.type" ng-options="o for o in roles track by o" value="{{o}}">
                            <option value=""></option>
                        </select></td>
                    <td><input type="text" ng-model="roleJobCode.peopleJobCode" id="peopleJobCode"></td>
                    <td><input type="text" ng-model="roleJobCode.univJobCode" id="univJobCode"></td>
                    <td><select ng-model="roleJobCode.reportRole" ng-options="o for o in reportRoles" value="{{o}}">
                        <option value=""></option>
                    </select></td>
                    <td>
                        <button id="editJobCodeMapping" class="btn btn-info" ng-click="editJobCodeMapping(roleJobCode)">Update</button>&nbsp;
                        <button id="deleteJobCodeMapping" class="btn btn-danger" ng-click="deleteJobCodeMapping(roleJobCode)">Delete</button></td>
                </tr>
                </tbody>

                <tr>
                    <td colspan="5" align="center">
                        <button ng-disabled="currentPage == 0" ng-click="currentPage=currentPage-1" class="btn btn-info">
                        Previous
                    </button>
                        {{currentPage+1}}/{{numberOfPages()}}
                        <button ng-disabled="currentPage >= jobCodeMapping.length/pageSize - 1" ng-click="currentPage=currentPage+1" class="btn btn-info">
                            Next
                        </button>
                    </td>
                </tr>

            </table>



        </div>




    </div>
</fieldset>

<script type="text/javascript">

    function JobCodeMappingController($scope, $http) {

        $scope.addJobCodeMapping = function() {
            $scope.jobCodeMapping.splice(0,0,{id:null, role: null, peopleJobCode: null, univJobCode: null, reportRole:null});
            $scope.currentPage = 0;
        }

        $scope.editJobCodeMapping = function(roleJobCode) {
            $scope.errorMessage = null;
            $scope.okMessage = null;

            $http.post('${contextPath}/tmw/storeInfo/updateJobCodeMapping', roleJobCode).success(function (data) {
                $scope.loading = false;
                if (data.errorMessage) {
                    $scope.errorMessage = data;
                } else {
                    $scope.okMessage = "Record updated successfully!";
                    //$scope.search();
                }
            }).error(function (data) {
                $scope.errorMessage = "Request error: " + data;
            });

        }

        $scope.deleteJobCodeMapping = function(roleJobCode) {
            $scope.errorMessage = null;
            $scope.okMessage = null;
            $http.post('${contextPath}/tmw/storeInfo/deleteJobCodeMapping', roleJobCode).success(function (data) {
                $scope.loading = false;
                if (data.errorMessage) {
                    $scope.errorMessage = data;
                } else {
                    $scope.okMessage = "Record deleted successfully!";
                    $scope.search();
                }
            }).error(function (data) {
                $scope.errorMessage = "Request error: " + data;
            });

        }

        $scope.clearFilter = function() {
            $scope.searchBy = "";
            $scope.search();
        }

        $scope.search = function () {
            $scope.errorMessage = null;
            $scope.okMessage = null;
            $scope.jobCodeMapping = null;
            $scope.loading = true;
            $search = $scope.searchBy;
            if ($search == null) {
                $search = '';
            }

            $http.get('${contextPath}/tmw/storeInfo/searchJobCodeMapping?searchBy=' + $search).success(function (data) {
                $scope.loading = false;
                if (data.errorMessage) {
                    $scope.store = null;
                    $scope.errorMessage = data.errorMessage;
                } else {
                    $scope.jobCodeMapping = data.jobCodeMapping;
                    $scope.roles = data.roles;
                    $scope.reportRoles = data.reportRoles;
                }
            }).error(function (data) {
                $scope.errorMessage = "Request error";
                $scope.jobCodeMapping = null;
            });

        };


        $scope.currentPage = 0;
        $scope.pageSize = 10;
        $scope.numberOfPages=function(){
            if ($scope.jobCodeMapping != null) {
                return Math.ceil($scope.jobCodeMapping.length / $scope.pageSize);
            } else {
                return 1;
            }
        }

        $scope.search();
    }

    var app = angular.module('app', ['kendo.directives', '$strap.directives']);

    app.filter('startFrom', function() {
        return function(input, start) {
            start = +start; //parse to int
            if (input !=null) {
                return input.slice(start);
            } else {
                return 0;
            }
        }
    });



</script>
<#include "*/footer.ftl"/>
