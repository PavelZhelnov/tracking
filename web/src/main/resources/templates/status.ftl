
<#assign top_nav_selected = "status">
<#assign page_title = "Status">
<#include "header.ftl"/>
<div>
    <h3>Application is running. Version: ${version}</h3>
    <hr/>
<#if shiro.isPermitted("JOB_STATUS")>
    <div id="schedulerInfo">
        <h3>Job info:</h3>
        <div ng-controller='TimeCtrl'>
            <h4>Current server time {{ clock  | date:'medium'}}</h4>
            <h5 style="color: #ff0000"><b>NOTE:</b> dates are in timezone :
                <select id="timezone" class="timezone">
                    <option name="UTC" value="UTC" selected>UTC</option>
                    <option name="PDT" value="PDT">PDT</option>
                    <option name="CST" value="CST">CST</option>
                </select>
            </h5>
        </div>

        <div id="jobDivs">
            <table class="table table-bordered" style="width: 100%;">
                <tr>
                    <th>Description</th>
                    <th>Start date</th>
                    <th>End date</th>
                    <th>Next run</th>
                    <th>Status</th>
                    <th></th>
                </tr>

                <#list jobInfo as info>
                    <tr>
                        <td><span class="span" key="${info.name}">${info.description}</span></td>
                        <td>
                            <div id="startDate"><#if info.startDate??>${info.startDate?string("yyyy-MM-dd HH:mm:ss")}<#else>&nbsp;</#if></div>
                            <div id="startDateHid" style="display:none; visibility: hidden;"><#if info.startDate??>${info.startDate?string("yyyy-MM-dd HH:mm:ss")}<#else>&nbsp;</#if></div>
                        </td>
                        <td>
                            <div id="endDate"><#if info.endDate??>${info.endDate?string("yyyy-MM-dd HH:mm:ss")}<#else>&nbsp;</#if></div>
                            <div id="endDateHid" style="display:none; visibility: hidden;"><#if info.endDate??>${info.endDate?string("yyyy-MM-dd HH:mm:ss")}<#else>&nbsp;</#if></div>
                        </td>
                        <td>
                            <div id="nextFireTime"><#if info.nextFireTime??>${info.nextFireTime?string("yyyy-MM-dd HH:mm:ss")}<#else>Unknown</#if></div>
                            <div id="nextFireTimeHid" style="display:none; visibility: hidden;"><#if info.nextFireTime??>${info.nextFireTime?string("yyyy-MM-dd HH:mm:ss")}<#else>Unknown</#if></div>
                        </td>
                        <td><#if info.running>Running<#else>Stopped</#if></td>
                        <td><button name="startJob" type="button" class="btn btn-mini btn-info" <#if info.running>disabled</#if>>Run</button></td>
                        <td><button name="enableJob" type="button" class="btn btn-mini btn-info" enabled="<#if info.enabled>false<#else>true</#if>"> <#if info.enabled>Disable<#else>Enable</#if></button></td>
                    </tr>
                </#list>
            </table>
        </div>
    </div>
</#if>
</div>

<script type="text/javascript">
    var oneHourMs = 3600000;
    var timeZoneOffset = (new Date().getTimezoneOffset()) * 60000;  // get difference between UTC and local time in milliseconds
    var PDTtoUTCoffset = 8;
    var CSTtoUTCoffset = 6
    var PDT = 'PDT';
    var CST = 'CST';
    var UTC = 'UTC';
    //var EETtoUTCoffset = -2;

    var localTimeZone = resolveLocalTimeZone(timeZoneOffset);

    function resolveLocalTimeZone(offset) {
        UTChoursOffset = Math.round(offset/oneHourMs);
        //UTChoursOffset = -7;
        switch (UTChoursOffset) {
            case PDTtoUTCoffset: return PDT; //UTC-7
            case CSTtoUTCoffset: return CST; //UTC-6
            //case EETtoUTCoffset: return 'EET'; //UTC+2
            default: return UTC;
        }
    }

    $(document).ready(function() {
        var scheduledInfo = $("#schedulerInfo");
        scheduledInfo.find("button[name='startJob']").click(function(){
            var jobName = $(this).parent().parent().find("td:first span").attr("key");
            $.ajax({
                type: 'GET',
                'url': "${contextPath}/tmw/startJob?key=" + jobName ,
                dataType: "text",
                success: function(result) {
                    if(result == 'true'){
                        location.reload();
                    }
                }
            });
        });
        scheduledInfo.find("button[name='enableJob']").click(function(){
            var button = $(this);
            var jobName = button.parent().parent().find("td:first span").attr("key");
            var enabled = button.attr("enabled");
            $.ajax({
                type: 'GET',
                'url': "${contextPath}/tmw/enableJob?key=" + jobName + "&enabled=" + enabled ,
                dataType: "text",
                success: function(result) {
                    if(result == 'true'){
                        button.attr("enabled", enabled == 'false' ? "true" : "false");
                        button.text(enabled == 'false' ? "Enable" : "Disable");
                    }
                }
            });
        });
        //set default value for timezone combobox
        $('#timezone').on('change', function() {
            triggerDatecolumnRecalculation();
        });
        setDefaultTimeZoneSelect();

    });

    function setDefaultTimeZoneSelect() {
        var timeZoneCombo = document.getElementById('timezone');
        timeZoneCombo.value = localTimeZone;
    }

    function triggerDatecolumnRecalculation() {
        timeZone = document.getElementById('timezone').value;

        recalculateColumn('startDate', timeZone);
        recalculateColumn('endDate', timeZone);
        recalculateColumn('nextFireTime', timeZone);

    }

    function recalculateColumn(columnName, timeZone) {
        $.each($('#schedulerInfo #jobDivs #'+columnName), function (i, dateColumn) {
            var original = $('#schedulerInfo #jobDivs #'+columnName+'Hid')[i].innerText;
            if (original) {
                var timeStamp = parseDateTextToTimeStamp(original);
                dateColumn.innerText = recalculateWithTimeZone(timeStamp, timeZone);
            }

        });
    }

    function parseDateTextToTimeStamp(dateText) {
        return Date.parse(dateText);
    }

    function recalculateWithTimeZone(timeStamp, timeZone) {
        switch (timeZone) {
            case UTC: break;
            case PDT: timeStamp -= PDTtoUTCoffset * oneHourMs; break;
            case CST: timeStamp -= CSTtoUTCoffset * oneHourMs; break;
            //case 'EET': timeStamp -= EETtoUTCoffset * oneHourMs; break;
            default: break;
        }
        return new Date(timeStamp).format("yyyy-MM-dd h:mm:ss");
    }

    function TimeCtrl($scope, $timeout) {
        $scope.clock = "loading clock..."; // initialise the time variable
        $scope.tickInterval = 1000 //ms

        var tick = function () {
            timeZone = document.getElementById('timezone').value;

            $scope.clock = Date.now() + timeZoneOffset;    // get the current time in UTC
            $timeout(tick, $scope.tickInterval); // reset the timer
        }

        // Start the timer
        $timeout(tick, $scope.tickInterval);
    }

    Date.prototype.format = function(format) //author: meizz
    {
        var o = {
            "M+" : this.getMonth()+1, //month
            "d+" : this.getDate(),    //day
            "h+" : this.getHours(),   //hour
            "m+" : this.getMinutes(), //minute
            "s+" : this.getSeconds(), //second
            "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
            "S" : this.getMilliseconds() //millisecond
        }

        if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
                (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)if(new RegExp("("+ k +")").test(format))
            format = format.replace(RegExp.$1,
                    RegExp.$1.length==1 ? o[k] :
                            ("00"+ o[k]).substr((""+ o[k]).length));
        return format;
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

<#include "footer.ftl"/>